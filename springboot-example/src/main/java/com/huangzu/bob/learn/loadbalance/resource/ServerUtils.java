package com.huangzu.bob.learn.loadbalance.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bob
 * @date 2022/8/19 16:38
 */
public class ServerUtils {

    /**
     * 服务节点
     */
    public static List<Server> SERVERS = new ArrayList<>();

    /**
     * 带权重服务节点
     */
    public static Map<Server, Integer> WEIGHT_SERVERS = new HashMap<>(16);


    /**
     * 平滑加权轮询
     */
    public static List<WeightServer> WEIGHT_OF_SERVERS = new ArrayList<>();

    /**
     * 最小活跃数
     */
    public static List<ActiveServer> ACTIVE_OF_SERVERS = new ArrayList<>();

    static {
        Server server1 = new Server("1.1.1.1", 8080);
        Server server2 = new Server("2.2.2.2", 8080);
        Server server3 = new Server("3.3.3.3", 8080);
        Server server4 = new Server("4.4.4.4", 8080);
        SERVERS.add(server1);
        SERVERS.add(server2);
        SERVERS.add(server3);
        SERVERS.add(server4);

        WEIGHT_SERVERS.put(server1, 15);
        WEIGHT_SERVERS.put(server2, 5);
        WEIGHT_SERVERS.put(server3, 20);
        WEIGHT_SERVERS.put(server4, 10);
    }

    static {
        Server server1 = new Server("1.1.1.1", 8080);
        Server server2 = new Server("2.2.2.2", 8080);
        Server server3 = new Server("3.3.3.3", 8080);
        Server server4 = new Server("4.4.4.4", 8080);

        WEIGHT_OF_SERVERS.add(new WeightServer(server1, 4));
        WEIGHT_OF_SERVERS.add(new WeightServer(server2, 1));
        WEIGHT_OF_SERVERS.add(new WeightServer(server3, 3));
        WEIGHT_OF_SERVERS.add(new WeightServer(server4, 2));
    }

    static{
        Server server1 = new Server("1.1.1.1", 8080);
        Server server2 = new Server("2.2.2.2", 8080);
        Server server3 = new Server("3.3.3.3", 8080);
        Server server4 = new Server("4.4.4.4", 8080);

        ACTIVE_OF_SERVERS.add(new ActiveServer(server1, 0));
        ACTIVE_OF_SERVERS.add(new ActiveServer(server2, 0));
        ACTIVE_OF_SERVERS.add(new ActiveServer(server3, 0));
        ACTIVE_OF_SERVERS.add(new ActiveServer(server4, 0));
    }

}
