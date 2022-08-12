package com.huangzu.bob.learn.config;

import com.bob.skill.encrypt.algorithm.RsaEncryptAlgorithm;
import com.bob.skill.encrypt.core.EncryptionConfig;
import com.bob.skill.encrypt.filter.EncryptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

/**
 * @author Bob
 * @date 2022/6/29 17:12
 */
@Configuration
public class EncryptionConfiguration {

    @Bean
    @Primary
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FilterRegistrationBean filterRegistration() {
        EncryptionConfig config = new EncryptionConfig();
        config.setKey("d86d7bab3d6ac01ad9dc6a897652f2d2");
        config.setRequestDecryptUriList(Arrays.asList("/save", "/decryptEntityXml"));
        config.setResponseEncryptUriList(Arrays.asList("/test4", "/encryptEntity", "/save", "/encryptEntityXml", "/decryptEntityXml"));
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //registration.setFilter(new EncryptionFilter(config));
        registration.setFilter(new EncryptionFilter(config, new RsaEncryptAlgorithm(),null));
        registration.addUrlPatterns("/*");
        registration.setName("EncryptionFilter");
        registration.setOrder(1);
        return registration;
    }
}
