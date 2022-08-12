package com.bob.skill.encrypt.springboot.init;

import com.bob.skill.encrypt.springboot.annotation.Decrypt;
import com.bob.skill.encrypt.springboot.annotation.DecryptIgnore;
import com.bob.skill.encrypt.springboot.annotation.Encrypt;
import com.bob.skill.encrypt.springboot.annotation.EncryptIgnore;
import com.bob.skill.encrypt.util.RequestUriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author bob
 */
public class ApiEncryptDataInit implements ApplicationContextAware {
	
	private final Logger logger = LoggerFactory.getLogger(ApiEncryptDataInit.class);
	
	/**
	 * 需要对响应内容进行加密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> responseEncryptUriList = new ArrayList<>();
	
	/**
	 * 需要对请求内容进行解密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> requestDecryptUriList = new ArrayList<>();
    
	/**
	 * 忽略加密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> responseEncryptUriIgnoreList = new ArrayList<>();
	
	/**
	 * 忽略对请求内容进行解密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> requestDecryptUriIgnoreList = new ArrayList<>();

	/**
	 * Url参数需要解密的配置
	 * 比如：/user/list?name=加密内容<br>
	 * 格式：Key API路径  Value 需要解密的字段
	 * 示列：/user/list  [name,age]
	 */
	public static Map<String, List<String>> requestDecryptParamMap = new HashMap<>();

	/**
	 * 应用的上下文路径
	 */
	private String contextPath;
	
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    	this.contextPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
		Map<String, Object> beanMap = ctx.getBeansWithAnnotation(Controller.class);
        initData(beanMap);
        initRequestDecryptParam(ctx.getEnvironment());
    }

	/**
	 * 初始化Url 参数解密配置
	 * @param environment 环境变量
	 */
	private void initRequestDecryptParam(Environment environment) {
		for (PropertySource<?> source : ((AbstractEnvironment) environment).getPropertySources()) {
			if (source instanceof EnumerablePropertySource) {
				for (String name : ((EnumerablePropertySource) source).getPropertyNames()) {
					if (name.startsWith("spring.encrypt.requestDecryptParam")) {
						String[] keys = name.split("\\.");
						String key = keys[keys.length - 1];
						String property = environment.getProperty(name);
						requestDecryptParamMap.put(key.replace("$", ":"), Arrays.asList(property.split(",")));
					}
				}
			}
		}
	}

	/**
	 * 初始化加解密数据列表(注解配置的uri信息)
	 * @param beanMap Controller类beanMap
	 */
	private void initData(Map<String, Object> beanMap) {
		if (beanMap != null) {
            for (Object bean : beanMap.values()) {
                Class<?> clz = bean.getClass();
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                	Encrypt encrypt = AnnotationUtils.findAnnotation(method, Encrypt.class);
                	if (encrypt != null) {
                		// 注解中的URI优先级高
                    	String uri = encrypt.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = RequestUriUtils.getApiUri(clz, method, contextPath);
						}
                        logger.debug("Encrypt URI: {}", uri);
                        responseEncryptUriList.add(uri);
                	}
                	Decrypt decrypt = AnnotationUtils.findAnnotation(method, Decrypt.class);
                    if (decrypt != null) {
                    	String uri = decrypt.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = RequestUriUtils.getApiUri(clz, method, contextPath);
						}

                    	String decryptParam = decrypt.decryptParam();
						if (StringUtils.hasText(decryptParam)) {
							requestDecryptParamMap.put(uri, Arrays.asList(decryptParam.split(",")));
						}

                        logger.debug("Decrypt URI: {}", uri);
                        requestDecryptUriList.add(uri);
                    }
                    EncryptIgnore encryptIgnore = AnnotationUtils.findAnnotation(method, EncryptIgnore.class);
                	if (encryptIgnore != null) {
                		// 注解中的URI优先级高
                    	String uri = encryptIgnore.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = RequestUriUtils.getApiUri(clz, method, contextPath);
						}
                        logger.debug("EncryptIgnore URI: {}", uri);
                        responseEncryptUriIgnoreList.add(uri);
                	}
                	DecryptIgnore decryptIgnore = AnnotationUtils.findAnnotation(method, DecryptIgnore.class);
                    if (decryptIgnore != null) {
                    	String uri = decryptIgnore.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = RequestUriUtils.getApiUri(clz, method, contextPath);
						}
                        logger.debug("DecryptIgnore URI: {}", uri);
                        requestDecryptUriIgnoreList.add(uri);
                    }
                }
            }
        }
	}
}
