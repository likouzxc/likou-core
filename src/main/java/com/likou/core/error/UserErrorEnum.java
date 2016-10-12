package com.likou.core.error;

/**
 * Created by jiangli on 16/10/12.
 */
public enum UserErrorEnum {

    FORMAT_USERNAME(-10001,"用户名格式错误"),
    FORMAT_EMAIL(-10002,"邮箱格式错误"),
    FORMAT_MOBIL(-10003,"手机号格式错误"),
    FORMAT_PASSWORD(-10004,"密码位数不够"),
    EXSIT_USERNAME(-20001,"用户名已经存在"),
    EXSIT_EMAIL(-20002,"邮箱已经存在"),
    EXSIT_MOBIL(-20003,"手机号已经存在"),
    EXSIT_PASSWORD(-20004,"两次密码不一致"),
    ERROR_CAPTCHA(-30001,"验证码错误"),
    ERROR_SYSTEM(-30002,"系统异常"),
    OK(0,"正确");


    private int errorID;
    private String message;

    private UserErrorEnum(int errorID, String message) {
        this.errorID = errorID;
        this.message = message;
    }

    public int getErrorID() {
        return errorID;
    }

    public void setErrorID(int errorID) {
        this.errorID = errorID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
