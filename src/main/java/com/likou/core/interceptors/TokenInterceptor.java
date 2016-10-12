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

/**
 * Created by jiangli on 16/9/18.
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    DubboServiceFactory serviceFactory;

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {




    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String sessionID = CookieUtils.getCookieByName(httpServletRequest, Contents.SESSIONID);




        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
