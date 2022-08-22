package com.huangzu.bob.learn.loadbalance.resource;

/**
 * 带权重的服务
 * @author Bob
 * @date 2022/8/22 14:40
 */
public class WeightServer {

    /**
     * 节点信息
     */
    private Server server;
    /**
     * 节点权重值
     */
    private Integer weight;
    /**
     * 动态权重值
     */
    private Integer dynamicWeight;

    public WeightServer() {
    }

    public WeightServer(Server server, Integer weight) {
        this(server, weight, 0);
    }

    public WeightServer(Server server, Integer weight, Integer dynamicWeight) {
        this.server = server;
        this.weight = weight;
        this.dynamicWeight = dynamicWeight;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getDynamicWeight() {
        return dynamicWeight;
    }

    public void setDynamicWeight(Integer dynamicWeight) {
        this.dynamicWeight = dynamicWeight;
    }
}
