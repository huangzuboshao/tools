package com.bob.skill.encrypt.springboot.endpoint;

import com.bob.skill.encrypt.core.EncryptionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * 加解密配置信息端点
 *
 * @author bob
 */
@Endpoint(id = "encrypt-config")
public class EncryptEndpoint {

    @Autowired
    private EncryptionConfig encryptionConfig;

    @ReadOperation
    public Map<String, Object> encryptConfig() {
        Map<String, Object> data = new HashMap<>(8);
        data.put("responseEncryptUriList", encryptionConfig.getResponseEncryptUriList());
        data.put("responseEncryptUriIgnoreList", encryptionConfig.getResponseEncryptUriIgnoreList());
        data.put("requestDecryptUriList", encryptionConfig.getRequestDecryptUriList());
        data.put("requestDecryptUriIgnoreList", encryptionConfig.getRequestDecryptUriIgnoreList());
        return data;
    }
}
