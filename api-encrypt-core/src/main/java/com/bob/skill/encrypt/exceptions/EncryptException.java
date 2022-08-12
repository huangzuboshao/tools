package com.bob.skill.encrypt.exceptions;

/**
 * 加解密异常
 * @author Bob
 * @date 2022/6/30 10:24
 */
public class EncryptException extends RuntimeException{

    public EncryptException() {
    }

    public EncryptException(String msg) {
        super(msg);
    }

    public EncryptException(Throwable cause) {
        super(cause);
    }
}
