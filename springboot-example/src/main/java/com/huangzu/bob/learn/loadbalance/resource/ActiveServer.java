package com.huangzu.bob.learn.loadbalance.resource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 活跃数节点
 *
 * @author Bob
 * @date 2022/8/22 15:13
 */
public class ActiveServer {
    /**
     * 节点信息
     */
    private Server server;

    /**
     * 活跃数
     */
    private AtomicInteger active;

    public ActiveServer() {
    }

    public ActiveServer(Server server, int active) {
        this.server = server;
        this.active = new AtomicInteger(active);
    }

    public Server getServer() {
        active.incrementAndGet();
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public AtomicInteger getActive() {
        return active;
    }

    public void setActive(AtomicInteger active) {
        this.active = active;
    }
}
