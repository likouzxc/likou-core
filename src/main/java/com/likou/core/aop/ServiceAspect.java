package com.likou.core.aop;

import com.alibaba.fastjson.JSONObject;
import com.likou.core.annotation.ParamAnnotation;
import com.likou.core.dubbo.CallParam;
import com.likou.core.dubbo.CallResult;
import com.likou.core.log.ExtendLogBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.TreeMap;

public class ServiceAspect {

    private static Log dubboLogger = LogFactory.getLog("dubbo");
    private static Log serviceLogger = LogFactory.getLog("service");

    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        boolean isDubbo = false;
        Exception exception = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        ExtendLogBean log = new ExtendLogBean(pjp.getSignature().getDeclaringType(),method);
        Object[] args = pjp.getArgs().clone();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        for(int i = 0;i<parameterTypes.length;i++){
            if(parameterTypes[i] == CallParam.class){
                isDubbo = true;
                CallParam callParam = (CallParam) args[i];
                callParam.setReceiveTime();
                callParam.setClassName(pjp.getSignature().getDeclaringType());
                callParam.setMethod(method);
                log.add("requestId",callParam.getRequestId());
                dubboLogger.info(callParam);
                break;
            }
        }
        if(!isDubbo){
            ParamAnnotation paramAnnotation = method.getAnnotation(ParamAnnotation.class);
            boolean isExistsParam = paramAnnotation == null?false:true;

            if(isExistsParam){
                String[] paramsName = paramAnnotation.values();
                JSONObject jsonObject = new JSONObject(new TreeMap<String,Object>());
                int i = 0;
                for(;i<args.length;i++){
                    jsonObject.put(paramsName[i],args[i]);
                }
                log.setParams(jsonObject);
            }else {
                log.setParams(args);
            }
        }
        Object object = null;
        log.setStartTime();
        try {
            object = pjp.proceed();
        }catch (Exception e){
            e.printStackTrace();
            if(returnType == CallResult.class){
                object = CallResult.FAILURE(0,e.getMessage());
            }else{
                exception = e;
            }
        }
        log.setEndTime();
        log.setResult(object);
        if(isDubbo){
            if (exception == null) {
                dubboLogger.info(log);
            }else{
                dubboLogger.error(log);
                throw exception;
            }
        }else{
            if (exception == null) {
                serviceLogger.info(log);
            }else{
                serviceLogger.error(log);
                throw exception;
            }
        }

        return object;
    }
}
