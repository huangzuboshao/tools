package com.huangzu.bob.learn.loadbalance.resource;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 资源类
 *
 * @author Bob
 * @date 2022/8/19 16:31
 */
public class Server {
    /**
     * ip
     */
    private final String ip;

    /**
     * 端口
     */
    private final Integer port;


    public Server(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public Server ping() {
        long start = System.currentTimeMillis();
        // 生成一个1000~3000之间的随机数
        int random = ThreadLocalRandom.current().nextInt(1000, 2000);
        try {
            // 随机休眠一段时间，模拟不同的响应速度
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 最后返回自身的IP
        String s = this.toString();
        System.out.println("当前线程：" + Thread.currentThread().getName() + "\t ms：" + (System.currentTimeMillis() - start) + "\t this:" + s);
        return this;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
