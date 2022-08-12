package com.huangzu.bob.learn.controller;

import com.bob.skill.encrypt.springboot.annotation.Encrypt;
import com.bob.skill.encrypt.springboot.annotation.EncryptIgnore;
import com.huangzu.bob.learn.entity.User;
import com.huangzu.bob.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Bob
 * @date 2022/6/29 16:53
 */
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("test")
    @EncryptIgnore
    public String a() {
        return "test";
    }

    @GetMapping("test2")
    @Encrypt
    public String b() {
        return "test2";
    }

    @GetMapping("test3")
    public String c() {
        return "test3";
    }

    @GetMapping("test4")
    public String d() {
        return "test4";
    }

    @GetMapping("test5")
    public List<User> d5() {
        List<User> users = userService.listUsers();
        System.out.println(users);
        return users;
    }
}
