package com.huangzu.bob.learn.loadbalance;

import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;

import java.util.Map;
import java.util.Random;

/**
 * 策略3-随机权重
 *
 * 先求和所有的权重值，再随机生成一个总权重之内的索引。
 * 遍历之前配置的服务器列表，用随机索引与每个节点的权重值进行判断。
 *  如果小于，则代表当前请求应该落入目前这个节点；
 *  如果大于，则代表随机索引超出了目前节点的权重范围，则减去当前权重，继续与其他节点判断。
 * 最终随机出的索引总会落入到一个节点的权重范围内，最后返回对应的节点 IP。
 *
 * @author Bob
 * @date 2022/8/22 14:25
 */
public class RandomWeightStrategy {

    static Random random = new Random();

    public static Server getServer() {
        //计算总权重
        int totalWeight = 0;
        for (Map.Entry<Server, Integer> serverIntegerEntry : ServerUtils.WEIGHT_SERVERS.entrySet()) {
            totalWeight += serverIntegerEntry.getValue();
        }
        int index = random.nextInt(totalWeight);
        System.out.print(index+"为当前索引值 \t");
        Server targetServer = null;
        for (Map.Entry<Server, Integer> serverIntegerEntry : ServerUtils.WEIGHT_SERVERS.entrySet()) {
            Server server = serverIntegerEntry.getKey();
            Integer weight = serverIntegerEntry.getValue();
            if (weight > index) {
                targetServer = server;
                break;
            }
            index = index - weight;
        }
        return targetServer;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getServer());
        }
    }

}
