package com.huangzu.bob.learn;

import com.bob.skill.encrypt.springboot.annotation.EnableEncrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Bob
 * @date 2022/6/29 16:52
 */
@EnableEncrypt
@SpringBootApplication
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }
}
