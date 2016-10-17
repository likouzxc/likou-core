package com.likou.core.bean;

import com.likou.common.net.CookieUtils;
import com.likou.core.web.Contents;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by home on 2016/10/18.
 */
public class LoginCookieBean {

    public LoginCookieBean(HttpServletRequest request){
        this.t = CookieUtils.getCookieByName(request, Contents.T);
        this.i = CookieUtils.getCookieByName(request,Contents.I);
        this.sessionID = CookieUtils.getCookieByName(request,Contents.SESSIONID);
        this.uuid = CookieUtils.getCookieByName(request,Contents.UUID);
    }
    private String sessionID;
    private String i;
    private String t;
    private String uuid;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
