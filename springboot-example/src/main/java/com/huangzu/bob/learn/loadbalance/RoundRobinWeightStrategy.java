package com.huangzu.bob.learn.loadbalance;

import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;
import com.huangzu.bob.learn.loadbalance.resource.WeightServer;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略4 - 平滑加权轮询
 *
 * @author Bob
 * @date 2022/8/22 14:51
 */
public class RoundRobinWeightStrategy {
    /**
     * 初始化存储每个节点的权重容器
     */
    private static Map<Server, WeightServer> weightMap = new HashMap<>();

    /**
     * 计算总权重值，只需要计算一次，因此放在静态代码块中执行
     */
    private static int weightTotal = 0;

    static {
        sumWeightTotal();
    }

    /**
     * 求和总权重值，后续动态伸缩节点时，再次调用该方法即可。
     */
    public static void sumWeightTotal() {
        for (WeightServer weightOfServer : ServerUtils.WEIGHT_OF_SERVERS) {
            weightTotal += weightOfServer.getWeight();
        }
    }

    /**
     * 获取处理本次请求的具体服务器IP
     *
     * @return
     */
    public static Server getServer() {
        // 判断权重容器中是否有节点信息
        if (weightMap.isEmpty()) {
            ServerUtils.WEIGHT_OF_SERVERS.forEach(t -> {
                //初始化时，每个节点的动态权重值都为0
                weightMap.put(t.getServer(), new WeightServer(t.getServer(), t.getWeight(), 0));
            });
        }
        // 每次请求时，更改动态权重值
        for (WeightServer weightServer : weightMap.values()) {
            weightServer.setDynamicWeight(weightServer.getDynamicWeight() + weightServer.getWeight());
        }

        // 判断权重容器中最大的动态权重值
        WeightServer maxCurrentWeight = null;
        for (WeightServer weightServer : weightMap.values()) {
            if (maxCurrentWeight == null || weightServer.getDynamicWeight() > maxCurrentWeight.getDynamicWeight()) {
                maxCurrentWeight = weightServer;
            }
        }

        // 最后用最大的动态权重值减去所有节点的总权重值
        maxCurrentWeight.setDynamicWeight(maxCurrentWeight.getDynamicWeight() - weightTotal);

        // 返回最大的动态权重值对应的节点IP
        return maxCurrentWeight.getServer();
    }

    public static void main(String[] args) {
        // 使用for循环模拟10次请求
        for (int i = 1; i <= 20; i++) {
            System.out.println("第" + i + "个请求：" + getServer());
        }
    }
}