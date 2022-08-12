package com.bob.skill.encrypt.core;

import com.bob.skill.encrypt.springboot.init.ApiEncryptDataInit;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 加解密配置类
 *
 * @author bob
 */
@ConfigurationProperties(prefix = "spring.encrypt")
@Setter
public class EncryptionConfig {

    /**
     * AES加密Key
     */
    private String key = "d86d7bab3d6ac01ad9dc6a897652f2d2";

    /**
     * 需要对响应内容进行加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriList = new ArrayList<>();

    /**
     * 需要对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecryptUriList = new ArrayList<>();

    /**
     * 忽略加密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> responseEncryptUriIgnoreList = new ArrayList<>();

    /**
     * 忽略对请求内容进行解密的接口URI<br>
     * 比如：/user/list<br>
     * 不支持@PathVariable格式的URI
     */
    private List<String> requestDecryptUriIgnoreList = new ArrayList<>();

    /**
     * 响应数据编码
     */
    private String responseCharset = "UTF-8";

    /**
     * 开启调试模式，调试模式下不进行加解密操作，用于像Swagger这种在线API测试场景
     */
    private boolean debug = false;

    /**
     * 过滤器拦截模式
     */
    private String[] urlPatterns = new String[]{"/*"};

    /**
     * 过滤器执行顺序
     */
    private int order = 1;

    public EncryptionConfig() {

    }

    public EncryptionConfig(String key, List<String> responseEncryptUriList, List<String> requestDecryptUriList, String responseCharset, boolean debug) {
        super();
        this.key = key;
        this.responseEncryptUriList = responseEncryptUriList;
        this.requestDecryptUriList = requestDecryptUriList;
        this.responseCharset = responseCharset;
        this.debug = debug;
    }

    public String getKey() {
        return key;
    }

    public List<String> getResponseEncryptUriList() {
        return Stream.of(responseEncryptUriList, ApiEncryptDataInit.responseEncryptUriList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> getRequestDecryptUriList() {
        return Stream.of(requestDecryptUriList, ApiEncryptDataInit.requestDecryptUriList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public boolean isDebug() {
        return debug;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public int getOrder() {
        return order;
    }

    public List<String> getResponseEncryptUriIgnoreList() {
        // 配置和注解两种方式合并
        return Stream.of(responseEncryptUriIgnoreList, ApiEncryptDataInit.responseEncryptUriIgnoreList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> getRequestDecryptUriIgnoreList() {
        // 配置和注解两种方式合并
        return Stream.of(requestDecryptUriIgnoreList, ApiEncryptDataInit.requestDecryptUriIgnoreList).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> getRequestDecryptParams(String uri) {
        List<String> params = ApiEncryptDataInit.requestDecryptParamMap.get(uri);
        if (CollectionUtils.isEmpty(params)) {
            return new ArrayList<>();
        }
        return params;
    }
}
