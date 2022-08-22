package com.huangzu.bob.learn.loadbalance;

import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 策略1-轮询
 *
 * @author Bob
 * @date 2022/8/19 16:37
 */
public class RoundRobinStrategy {
    /**
     * 用于记录当前请求的序列号
     */
    private static AtomicInteger requestIndex = new AtomicInteger(0);

    /**
     * 从集群节点中选取一个节点处理请求
     *
     * @return
     */
    public static Server getServer() {
        // 用请求序列号取余集群节点数量，求得本次处理请求的节点下标
        int index = requestIndex.get() % ServerUtils.SERVERS.size();
        // 从服务器列表中获取具体的节点IP地址信息
        Server server = ServerUtils.SERVERS.get(index);
        // 自增一次请求序列号，方便下个请求计算
        requestIndex.incrementAndGet();
        // 返回获取到的服务器IP地址
        return server;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getServer());
        }
    }
}
