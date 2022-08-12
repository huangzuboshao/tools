package com.huangzu.bob.learn.entity;

import com.bob.skill.encrypt.datamasking.DataMasking;
import com.bob.skill.encrypt.datamasking.SensitiveTypeEnum;

/**
 * @author Bob
 * @date 2022/8/9 11:07
 */
public class User {

    private String id;

    @DataMasking(type = SensitiveTypeEnum.NAME)
    private String name;

    @DataMasking(type = SensitiveTypeEnum.CUSTOMER,symbol = "*")
    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
