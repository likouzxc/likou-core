package com.likou.core.dubbo;

/**
 * Created by jiangli on 16/9/19.
 */
public interface UserProvider {

    public CallResult isLogin(CallParam param) throws Exception;
}
