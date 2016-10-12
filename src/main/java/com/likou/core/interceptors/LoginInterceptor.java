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

        String t = CookieUtils.getCookieByName(httpServletRequest,Contents.T);
        String i = CookieUtils.getCookieByName(httpServletRequest,Contents.I);
        String sessionID = CookieUtils.getCookieByName(httpServletRequest,Contents.SESSIONID);
        String uuid = CookieUtils.getCookieByName(httpServletRequest,Contents.UUID);

        if(StringUtils.isBlank(t) || StringUtils.isBlank(sessionID) || StringUtils.isBlank(i) || StringUtils.isBlank(uuid)){
            httpServletResponse.sendRedirect(Contents.getLoginURL());
            return false;
        }else{
            UserProvider userProvider = serviceFactory.getDubboService(UserProvider.class);
            CallParam callParam = new CallParam(IDGen.get32ID(),"system");
            callParam.add(Contents.T,t);
            callParam.add(Contents.I,i);
            callParam.add(Contents.SESSIONID,sessionID);
            callParam.add(Contents.UUID,uuid);
            if(userProvider.isLogin(callParam).isSuccess()){
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.I, i);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.T, t);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.SESSIONID, sessionID);
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.UUID, uuid);
                return true;
            }else{
                httpServletResponse.sendRedirect(Contents.getLoginURL());
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", Contents.I);
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(),"/", Contents.T);
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", Contents.SESSIONID);
                CookieUtils.delCookie(httpServletRequest,httpServletResponse ,Contents.getCookieHost(), "/", Contents.UUID);
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
