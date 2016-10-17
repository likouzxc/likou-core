package com.likou.core.interceptors;

import com.likou.common.character.IDGen;
import com.likou.common.net.CookieUtils;
import com.likou.common.servlet.RequestUtils;
import com.likou.core.bean.LoginCookieBean;
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
import java.net.URLEncoder;
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

        String requstURL = RequestUtils.getRequstURL(httpServletRequest);

        LoginCookieBean cookieBean = new LoginCookieBean(httpServletRequest);
        if(StringUtils.isBlank(cookieBean.getT()) || StringUtils.isBlank(cookieBean.getSessionID())
                || StringUtils.isBlank(cookieBean.getI()) || StringUtils.isBlank(cookieBean.getUuid())){
            httpServletResponse.sendRedirect(Contents.getLoginURL()+ URLEncoder.encode(requstURL,"UTF-8"));
            return false;
        }else{
            UserProvider userProvider = serviceFactory.getDubboService(UserProvider.class);
            CallParam callParam = new CallParam(IDGen.get32ID(),"system");
            callParam.add("cookieBean",cookieBean);
            if(userProvider.isLogin(callParam).isSuccess()){
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.I, cookieBean.getI());
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.T, cookieBean.getT());
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.SESSIONID, cookieBean.getSessionID());
                CookieUtils.addCookie(httpServletResponse,Contents.getCookieHost(), "/", Contents.UUID, cookieBean.getUuid());
                return true;
            }else{
                httpServletResponse.sendRedirect(Contents.getLoginURL()+ URLEncoder.encode(requstURL,"UTF-8"));
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
