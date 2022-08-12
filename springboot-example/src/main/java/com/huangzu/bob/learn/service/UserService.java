package com.huangzu.bob.learn.service;

import com.huangzu.bob.learn.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bob
 * @date 2022/8/9 11:08
 */
@Service
public class UserService {

    private static List<User> list = new ArrayList<>();
    static {
        User user = new User();
        user.setId("123");
        user.setName("张三");
        user.setPhoneNumber("15928090422");
        list.add(user);
    }
    public List<User> listUsers() {
        return list;
    }
}
