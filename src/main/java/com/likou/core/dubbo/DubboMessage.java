package com.likou.core.dubbo;

/**
 * 常量提示
 * Created by jiangli on 16/7/31.
 */
public interface DubboMessage {

    /**
     * 无配置此服务
     */
    public static String NOSERVICE = "No configuration of the service!" ;
    /**
     * 在服务注册中心未找到此服务
     */
    public static String UNDEFINED = "This service was not found in the service registry!" ;
}
