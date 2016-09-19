package com.likou.core.interceptors;

import com.likou.common.character.IDGen;
import com.likou.common.net.CookieUtils;
import com.likou.core.dubbo.CallParam;
import com.likou.core.dubbo.DubboServiceFactory;
import com.likou.core.dubbo.UserProvider;
import com.likou.core.web.Contents;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * Created by jiangli on 16/9/18.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    DubboServiceFactory serviceFactory;

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        HttpSession session = httpServletRequest.getSession(true);

        String t = CookieUtils.getCookieByName(httpServletRequest,"t");
        String i = CookieUtils.getCookieByName(httpServletRequest,"i");
        String sessionID = CookieUtils.getCookieByName(httpServletRequest,"sessionID");
        String uuid = CookieUtils.getCookieByName(httpServletRequest,"uuid");

        if(StringUtils.isBlank(t) || StringUtils.isBlank(sessionID) || StringUtils.isBlank(i) || StringUtils.isBlank(uuid)){
            httpServletResponse.sendRedirect(Contents.getLoginURL());
            return false;
        }else{
            UserProvider userProvider = serviceFactory.getDubboService(UserProvider.class);
            CallParam callParam = new CallParam(IDGen.get32ID(),"system");
            callParam.add("t",t);
            callParam.add("i",i);
            callParam.add("sessionID",sessionID);
            callParam.add("uuid",uuid);
            if(userProvider.isLogin(callParam).isSuccess()){
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", "i", i);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", "t", t);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", "sessionID", sessionID);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", "uuid", uuid);
                return true;
            }else{
                httpServletResponse.sendRedirect(Contents.getLoginURL());
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", "i");
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(),"/", "t");
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", "sessionID");
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", "uuid");
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
