package com.bob.skill.encrypt.datamasking;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 *
 * @author Bob
 * @date 2022/8/9 9:56
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface DataMasking {
    /**
     * 脱敏数据类型
     */
    SensitiveTypeEnum type() default SensitiveTypeEnum.CUSTOMER;

    /**
     * 前置不需要打码的长度
     */
    int prefixNoMaskLength() default 0;

    /**
     * 后置不需要打码的长度
     */
    int suffixNoMaskLength() default 0;

    /**
     * 用什么打码
     */
    String symbol() default "*";
}
