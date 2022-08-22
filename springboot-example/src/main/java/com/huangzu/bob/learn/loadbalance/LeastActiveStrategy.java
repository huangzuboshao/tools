package com.huangzu.bob.learn.loadbalance;

import com.huangzu.bob.learn.loadbalance.resource.ActiveServer;
import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;

/**
 * 策略6 - 最小活跃数
 *
 * @author Bob
 * @date 2022/8/22 15:22
 */
public class LeastActiveStrategy {

    /**
     * 活跃度衰减器
     */
    public static void attenuator() {
        new Thread(() -> {
            // 遍历集群中的所有节点
            for (ActiveServer server : ServerUtils.ACTIVE_OF_SERVERS) {
                // 如果活跃度不为0
                if (server.getActive().get() != 0) {
                    // 则自减一个活跃度
                    server.getActive().getAndDecrement();
                }
            }
            try {
                // 每隔 2 秒中衰减一次活跃度
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Server getServer() {
        // 初始化最小活跃数和最小活跃数的节点
        int leastActive = Integer.MAX_VALUE;
        ActiveServer leastServer = new ActiveServer();
        // 遍历集群中的所有节点
        for (ActiveServer server : ServerUtils.ACTIVE_OF_SERVERS) {
            // 找出活跃数最小的节点
            if (leastActive > server.getActive().get()) {
                leastActive = server.getActive().get();
                leastServer = server;
            }
        }

        // 返回活跃数最小的节点IP
        return leastServer.getServer();
    }

    public static void main(String[] args) {
        attenuator();
        for (int i = 1; i <= 10; i++) {
            System.out.println("第" + i + "个请求：" + getServer());
        }
    }
}
