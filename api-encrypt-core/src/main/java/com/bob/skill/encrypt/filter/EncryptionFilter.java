package com.bob.skill.encrypt.filter;

import com.bob.skill.encrypt.algorithm.AesEncryptAlgorithm;
import com.bob.skill.encrypt.algorithm.EncryptAlgorithm;
import com.bob.skill.encrypt.core.EncryptionConfig;
import com.bob.skill.encrypt.core.EncryptionRequestWrapper;
import com.bob.skill.encrypt.core.EncryptionResponseWrapper;
import com.bob.skill.encrypt.exceptions.EncryptException;
import com.bob.skill.encrypt.util.RequestUriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 数据加解密过滤器
 *
 * @author bob
 */
public class EncryptionFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(EncryptionFilter.class);

    /**
     * 加解密配置
     */
    private final EncryptionConfig encryptionConfig;

    /**
     * 用作路径匹配
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 默认为Aes对撑加密
     */
    private EncryptAlgorithm encryptAlgorithm = new AesEncryptAlgorithm();

    /**
     * 调度分发Servlet
     */
    private DispatcherServlet dispatcherServlet;

    public EncryptionFilter() {
        this.encryptionConfig = new EncryptionConfig();
    }

    public EncryptionFilter(EncryptionConfig config) {
        this.encryptionConfig = config;
    }

    public EncryptionFilter(EncryptionConfig config, DispatcherServlet dispatcherServlet) {
        this.encryptionConfig = config;
        this.dispatcherServlet = dispatcherServlet;
    }

    public EncryptionFilter(EncryptionConfig config, EncryptAlgorithm encryptAlgorithm, DispatcherServlet dispatcherServlet) {
        this.encryptionConfig = config;
        this.encryptAlgorithm = encryptAlgorithm;
        this.dispatcherServlet = dispatcherServlet;
    }

    public EncryptionFilter(String key) {
        EncryptionConfig config = new EncryptionConfig();
        config.setKey(key);
        this.encryptionConfig = config;
    }

    public EncryptionFilter(String key, List<String> responseEncryptUriList, List<String> requestDecryptUriList, String responseCharset, boolean debug) {
        this.encryptionConfig = new EncryptionConfig(key, responseEncryptUriList, requestDecryptUriList, responseCharset, debug);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String prefixUri = request.getMethod().toLowerCase() + ":" + request.getRequestURI();
        logger.debug("EncryptionFilter RequestURI: {}", prefixUri);

        // 调试模式不走加解密
        if (encryptionConfig.isDebug()) {
            chain.doFilter(request, response);
            return;
        }
        //判断加解密配置和当前uri匹配条件
        boolean decryptionStatus = this.contains(encryptionConfig.getRequestDecryptUriList(), prefixUri, request);
        boolean encryptionStatus = this.contains(encryptionConfig.getResponseEncryptUriList(), prefixUri, request);
        boolean decryptionIgnoreStatus = this.contains(encryptionConfig.getRequestDecryptUriIgnoreList(), prefixUri, request);
        boolean encryptionIgnoreStatus = this.contains(encryptionConfig.getResponseEncryptUriIgnoreList(), prefixUri, request);
        EncryptionResponseWrapper responseWrapper = null;
        EncryptionRequestWrapper requestWrapper = null;

        // 没有配置具体加解密的URI默认全部都开启加解密
        if (CollectionUtils.isEmpty(encryptionConfig.getRequestDecryptUriList()) && CollectionUtils.isEmpty(encryptionConfig.getResponseEncryptUriList())) {
            decryptionStatus = true;
            encryptionStatus = true;
        }
        // 接口在忽略加密列表中
        if (encryptionIgnoreStatus) {
            encryptionStatus = false;
        }
        // 接口在忽略解密列表中
        if (decryptionIgnoreStatus) {
            decryptionStatus = false;
        }
        // 没有加解密操作
        if (!decryptionStatus && !encryptionStatus) {
            chain.doFilter(request, response);
            return;
        }

        // 配置了需要解密才处理
        if (decryptionStatus) {
            requestWrapper = new EncryptionRequestWrapper(request);
            processDecryption(requestWrapper, request);
        }

        if (encryptionStatus) {
            responseWrapper = new EncryptionResponseWrapper(response);
        }

        // 同时需要加解密
        if (encryptionStatus && decryptionStatus) {
            chain.doFilter(requestWrapper, responseWrapper);
        } else if (encryptionStatus) {
            // 只需要响应加密
            chain.doFilter(request, responseWrapper);
        } else {
            // 只需要请求解密
            chain.doFilter(requestWrapper, response);
        }

        // 配置了需要加密才处理
        if (encryptionStatus) {
            String responseData = responseWrapper.getResponseData();
            writeEncryptContent(responseData, servletResponse);
        }
    }

    /**
     * 请求解密处理
     *
     * @param requestWrapper 加解密请求Wrapper
     * @param request 请求
     */
    private void processDecryption(EncryptionRequestWrapper requestWrapper, HttpServletRequest request) {
        String requestData = requestWrapper.getRequestData();
        String uri = request.getRequestURI();
        logger.debug("RequestData: {}", requestData);
        try {
            if (!StringUtils.endsWithIgnoreCase(request.getMethod(), RequestMethod.GET.name())) {
                String decryptRequestData = encryptAlgorithm.decrypt(requestData, encryptionConfig.getKey());
                logger.debug("DecryptRequestData: {}", decryptRequestData);
                requestWrapper.setRequestData(decryptRequestData);
            }

            // url参数解密
            Map<String, String> paramMap = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String prefixUri = request.getMethod().toLowerCase() + ":" + uri;
                if (encryptionConfig.getRequestDecryptParams(prefixUri).contains(paramName)) {
                    String paramValue = request.getParameter(paramName);
                    String decryptParamValue = encryptAlgorithm.decrypt(paramValue, encryptionConfig.getKey());
                    paramMap.put(paramName, decryptParamValue);
                }
            }
            requestWrapper.setParamMap(paramMap);
        } catch (Exception e) {
            logger.error("请求数据解密失败", e);
            throw new EncryptException(e);
        }
    }

    /**
     * 输出加密内容
     *
     * @param responseData
     * @param response 响应
     * @throws IOException
     */
    private void writeEncryptContent(String responseData, ServletResponse response) throws IOException {
        logger.debug("ResponseData: {}", responseData);
        ServletOutputStream out = null;
        try {
            responseData = encryptAlgorithm.encrypt(responseData, encryptionConfig.getKey());
            logger.debug("EncryptResponseData: {}", responseData);
            response.setContentLength(responseData.length());
            response.setCharacterEncoding(encryptionConfig.getResponseCharset());
            out = response.getOutputStream();
            out.write(responseData.getBytes(encryptionConfig.getResponseCharset()));
        } catch (Exception e) {
            logger.error("响应数据加密失败", e);
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * uri是否与加解密配置项(配置文件、注解接口)匹配
     * @param list 加解密配置项(配置文件、注解接口)
     * @param prefixUri
     * @param request
     * @return
     */
    private boolean contains(List<String> list, String prefixUri, HttpServletRequest request) {
        //list为配置的(/index)和注解(GET:/index)
        if (list.contains(request.getRequestURI()) || list.contains(prefixUri)) {
            return true;
        }
        // 优先用AntPathMatcher，其实用这个也够了，底层是一样的，下面用的方式兜底
        for (String u : list) {
            if (antPathMatcher.match(u, prefixUri)) {
                return true;
            }
        }

        try {
            // 支持RestFul风格API
            // 采用Spring MVC内置的匹配方式将当前请求匹配到对应的Controller Method上，获取注解进行匹配是否要加解密
            HandlerExecutionChain handler = getHandler(request);
            if (Objects.isNull(handler)) {
                return false;
            }

            if (Objects.nonNull(handler.getHandler()) && handler.getHandler() instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
                String apiUri = RequestUriUtils.getApiUri(handlerMethod.getClass(), handlerMethod.getMethod(), request.getContextPath());
                if (list.contains(apiUri)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (Objects.isNull(dispatcherServlet)) {
            return null;
        }
        if (dispatcherServlet.getHandlerMappings() != null) {
            for (HandlerMapping mapping : dispatcherServlet.getHandlerMappings()) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }
}
