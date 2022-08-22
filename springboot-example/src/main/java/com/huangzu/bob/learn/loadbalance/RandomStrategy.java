package com.huangzu.bob.learn.loadbalance;

import com.huangzu.bob.learn.loadbalance.resource.Server;
import com.huangzu.bob.learn.loadbalance.resource.ServerUtils;

import java.util.Random;

/**
 * 策略2-随机
 *
 * @author Bob
 * @date 2022/8/22 14:22
 */
public class RandomStrategy {
    static Random random = new Random();

    public static Server getServer() {
        return ServerUtils.SERVERS.get(random.nextInt(ServerUtils.SERVERS.size()));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getServer());
        }
    }

}
