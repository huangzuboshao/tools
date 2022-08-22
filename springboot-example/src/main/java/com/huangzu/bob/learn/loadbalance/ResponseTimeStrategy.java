package com.huangzu.bob.learn.loadbalance;

import cn.hutool.core.thread.NamedThreadFactory;
import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;
import com.huangzu.bob.learn.loadbalance.resource.WeightServer;

import java.util.concurrent.*;

/**
 * 策略7 - 最少响应时间
 *
 * @author Bob
 * @date 2022/8/22 15:34
 */
public class ResponseTimeStrategy {
    /**
     * 创建一个定长的线程池，用于去执行ping任务
     */
    static ExecutorService pingServerPool = new ThreadPoolExecutor(ServerUtils.SERVERS.size(), ServerUtils.SERVERS.size(),
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("owner-thread-", false)
    );
    static CountDownLatch countDownLatch = new CountDownLatch(ServerUtils.SERVERS.size());

    public static Server getServer() throws InterruptedException {
        // 创建一个接收结果返回的server节点对象
        final WeightServer resultServer = new WeightServer();
        // 根据集群节点数量初始化一个异步任务数组
        CompletableFuture<Server>[] cfs = new CompletableFuture[ServerUtils.SERVERS.size()];
        // 遍历整个服务器列表，为每个节点创建一个ping任务
        for (Server server : ServerUtils.SERVERS) {
            // 获取当前节点在集群列表中的下标
            int index = ServerUtils.SERVERS.indexOf(server);
            // 为每个节点创建一个ping任务，并交给pingServerPool线程池执行
            CompletableFuture<Server> cf = CompletableFuture.supplyAsync(server::ping,pingServerPool);
            // 将创建好的异步任务加入数组中
            cfs[index] = cf;
        }
        // 将创建好的多个Ping任务组合成一个聚合任务并执行
        CompletableFuture<Object> cfAnyOf = CompletableFuture.anyOf(cfs);

        // 监听执行完成后的回调，谁先执行完成则返回谁
        cfAnyOf.thenAccept(node -> {
            System.out.println("最先响应检测请求的节点为：" + node);
            resultServer.setServer((Server) node);
        });
        //  阻塞主线程一段时间，防止CompletableFuture退出
        countDownLatch.await();

        // 返回最先响应检测请求（ping）的节点作为本次处理客户端请求的节点
        return resultServer.getServer();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++){
            System.out.println("第"+ i + "个请求：" + getServer());
        }
        pingServerPool.shutdown();
    }
}
