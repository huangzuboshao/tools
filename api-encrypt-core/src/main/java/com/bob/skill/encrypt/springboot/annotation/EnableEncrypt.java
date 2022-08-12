package com.bob.skill.encrypt.springboot.annotation;

import com.bob.skill.encrypt.springboot.autoconfigure.EncryptAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用加密Starter
 *
 * <p>在Spring Boot启动类上加上此注解</p>
 *
 * @author bob
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptAutoConfiguration.class})
public @interface EnableEncrypt {
}
