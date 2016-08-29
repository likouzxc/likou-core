package com.likou.core.dubbo;

import java.io.Serializable;

/**
 * Created by jiangli on 16/8/4.
 */
public class CallResult<T> implements Serializable {

    /**
     * 调用是否成功
     */
    private boolean success;

    /**
     * 操作返回码
     */
    private int code;

    /**
     * 消息信息
     */
    private String message;

    /**
     * 结果数据
     */
    private T data;

    private CallResult(){

    }
    public static <T> CallResult<T> SUCCESS(){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(true);
        return callResult;
    }
    public static <T> CallResult<T> SUCCESS(T t){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(true);
        callResult.setData(t);
        return callResult;
    }
    public static <T> CallResult<T> SUCCESS(int code , T t){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(true);
        callResult.setCode(code);
        callResult.setData(t);
        return callResult;
    }
    public static <T> CallResult<T> FAILURE(){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(false);
        return callResult;
    }
    public static <T> CallResult<T> FAILURE(int code ){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(false);
        callResult.setCode(code);
        return callResult;
    }
    public static <T> CallResult<T> FAILURE(int code , String message){
        CallResult<T> callResult = new CallResult<T>();
        callResult.setSuccess(false);
        callResult.setCode(code);
        callResult.setMessage(message);
        return callResult;
    }

    public boolean isSuccess() {
        return success;
    }

    private void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }
}
