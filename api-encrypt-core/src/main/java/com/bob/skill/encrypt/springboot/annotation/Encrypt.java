package com.bob.skill.encrypt.springboot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密注解
 * 
 * <p>加了此注解的接口将进行数据加密操作<p>
 * 
 * @author bob
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {
	String value() default "";
	
}
