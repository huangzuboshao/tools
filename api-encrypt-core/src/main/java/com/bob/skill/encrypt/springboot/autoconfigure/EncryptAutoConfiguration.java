package com.bob.skill.encrypt.springboot.autoconfigure;

import com.bob.skill.encrypt.algorithm.EncryptAlgorithm;
import com.bob.skill.encrypt.core.EncryptionConfig;
import com.bob.skill.encrypt.filter.EncryptionFilter;
import com.bob.skill.encrypt.springboot.endpoint.EncryptEndpoint;
import com.bob.skill.encrypt.springboot.init.ApiEncryptDataInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * 加解密自动配置
 *
 * @author bob
 */
@Configuration
@ConditionalOnWebApplication
@EnableAutoConfiguration
@EnableConfigurationProperties(EncryptionConfig.class)
public class EncryptAutoConfiguration {

    @Autowired
    private EncryptionConfig encryptionConfig;

    @Autowired(required = false)
    private EncryptAlgorithm encryptAlgorithm;

    /**
     * 用于解决@PathVariable风格的URI
     */
    @Autowired(required = false)
    private DispatcherServlet dispatcherServlet;

    /**
     * 不要用泛型注册Filter,泛型在Spring Boot 2.x版本中才有
     *
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<EncryptionFilter> filterRegistration() {
        FilterRegistrationBean<EncryptionFilter> registration = new FilterRegistrationBean<>();
        if (encryptAlgorithm != null) {
            registration.setFilter(new EncryptionFilter(encryptionConfig, encryptAlgorithm, dispatcherServlet));
        } else {
            registration.setFilter(new EncryptionFilter(encryptionConfig, dispatcherServlet));
        }
        registration.addUrlPatterns(encryptionConfig.getUrlPatterns());
        registration.setName("EncryptionFilter");
        registration.setOrder(encryptionConfig.getOrder());
        return registration;
    }

    @Bean
    public ApiEncryptDataInit apiEncryptDataInit() {
        return new ApiEncryptDataInit();
    }

    @Bean
    public EncryptEndpoint encryptEndpoint() {
        return new EncryptEndpoint();
    }
}
