package com.likou.core.web;

import org.springframework.stereotype.Component;

/**
 * Created by jiangli on 16/7/1.
 */
@Component
public class Contents {
    /**
     * 主站host地址
     */
    private static String host;
    /**
     * 资源host地址
     */
    private static String resourceHost;
    /**
     * cookie地址
     */
    private static String cookieHost;
    /**
     * 登录地址
     */
    private static String loginURL;

    /**
     * 请求来源
     */
    private static String requestFrom;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Contents.host = host;
    }

    public static String getResourceHost() {
        return resourceHost;
    }

    public static void setResourceHost(String resourceHost) {
        Contents.resourceHost = resourceHost;
    }

    public static String getRequestFrom() {
        return requestFrom;
    }

    public static void setRequestFrom(String requestFrom) {
        Contents.requestFrom = requestFrom;
    }

    public static String getCookieHost() {
        return cookieHost;
    }

    public static void setCookieHost(String cookieHost) {
        Contents.cookieHost = cookieHost;
    }

    public static String getLoginURL() {
        return loginURL;
    }

    public static void setLoginURL(String loginURL) {
        Contents.loginURL = loginURL;
    }
}
