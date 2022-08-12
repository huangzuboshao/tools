package com.bob.skill.encrypt.datamasking;

/**
 * 脱敏类型
 *
 * @author Bob
 * @date 2022/8/9 10:35
 */
public enum SensitiveTypeEnum {

    /**
     * 自定义
     */
    CUSTOMER,

    /**
     * 姓名
     */
    NAME,

    /**
     * 身份证
     */
    ID_NUM,

    /**
     * 手机号码
     */
    PHONE_NUM
}
