package com.likou.core.exception;

/**
 * Created by jiangli on 16/7/30.
 */
public class DubboException extends Exception{

    public DubboException() {
    }

    public DubboException(String message) {
        super(message);
    }
}
