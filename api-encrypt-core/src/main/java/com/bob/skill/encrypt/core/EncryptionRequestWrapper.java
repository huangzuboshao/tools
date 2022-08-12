package com.bob.skill.encrypt.core;


import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bob
 */
public class EncryptionRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 请求体
     */
    private byte[] requestBody;

    /**
     * 参数集
     */
    private Map<String, String> paramMap = new HashMap<>();

    public EncryptionRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }

    @Override
    public String getParameter(String name) {
        return this.paramMap.get(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        if (paramMap.containsKey(name)) {
            return new String[]{getParameter(name)};
        }
        return super.getParameterValues(name);
    }

    public String getRequestData() {
        return new String(requestBody);
    }

    public void setRequestData(String requestData) {
        this.requestBody = requestData.getBytes();
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}
