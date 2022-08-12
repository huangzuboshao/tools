package com.bob.skill.encrypt.util;


import com.bob.skill.encrypt.constant.HttpMethodTypeConstant;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * 请求uri工具
 *
 * @author bob
 */
public class RequestUriUtils {

    /**
     * 分割符
     */
    private static final String SEPARATOR = "/";

    /**
     * 获取api uri
     *
     * @param clz         类
     * @param method      方法
     * @param contextPath 路径上下文
     */
    public static String getApiUri(Class<?> clz, Method method, String contextPath) {
        String methodType = "";
        StringBuilder uri = new StringBuilder();
        RequestMapping reqMapping = AnnotationUtils.findAnnotation(clz, RequestMapping.class);
        if (reqMapping != null && !StringUtils.isEmpty(reqMapping.value())) {
            uri.append(formatUri(reqMapping.value()[0]));
        }
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (getMapping != null && !StringUtils.isEmpty(getMapping.value())) {
            methodType = HttpMethodTypeConstant.GET;
            uri.append(formatUri(getMapping.value()[0]));

        } else if (postMapping != null && !StringUtils.isEmpty(postMapping.value())) {
            methodType = HttpMethodTypeConstant.POST;
            uri.append(formatUri(postMapping.value()[0]));

        } else if (putMapping != null && !StringUtils.isEmpty(putMapping.value())) {
            methodType = HttpMethodTypeConstant.PUT;
            uri.append(formatUri(putMapping.value()[0]));

        } else if (deleteMapping != null && !StringUtils.isEmpty(deleteMapping.value())) {
            methodType = HttpMethodTypeConstant.DELETE;
            uri.append(formatUri(deleteMapping.value()[0]));

        } else if (requestMapping != null && !StringUtils.isEmpty(requestMapping.value())) {
            RequestMethod requestMethod = RequestMethod.GET;
            if (requestMapping.method().length > 0) {
                requestMethod = requestMapping.method()[0];
            }
            methodType = requestMethod.name().toLowerCase() + ":";
            uri.append(formatUri(requestMapping.value()[0]));
        }
        if (StringUtils.hasText(contextPath) && !SEPARATOR.equals(contextPath)) {
            if (contextPath.endsWith(SEPARATOR)) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
            return methodType + contextPath + uri.toString();
        }
        return methodType + uri.toString();
    }

    /**
     * 格式化uri
     *
     * @param uri uri
     */
    private static String formatUri(String uri) {
        if (uri.startsWith(SEPARATOR)) {
            return uri;
        }
        return SEPARATOR + uri;
    }
}
