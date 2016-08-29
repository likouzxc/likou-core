package com.likou.core.aop;

import com.alibaba.fastjson.JSONObject;
import com.likou.core.annotation.ParamAnnotation;
import com.likou.core.log.URILogBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.TreeMap;

public class WebAspect {

    private static Log logger = LogFactory.getLog("web");

    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        boolean isRequest = false;
        boolean isExistsParam = false;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        URILogBean log = new URILogBean(pjp.getSignature().getDeclaringType(),method);
        Annotation[] annotations = method.getAnnotations();
        Object[] args = pjp.getArgs().clone();
        String[] paramsName = null;

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if(requestMapping != null){
            isRequest = true;
            String[] value = requestMapping.value();
            log.setUri(value);
            ParamAnnotation paramAnnotation = method.getAnnotation(ParamAnnotation.class);
            if(paramAnnotation != null){
                isExistsParam = true;
                paramsName = paramAnnotation.values();
            }
        }

        if(isRequest){
            Class<?>[] parameterTypes = method.getParameterTypes();
            for(int i = 0;i<parameterTypes.length;i++){
                if(parameterTypes[i] == Model.class){
                    args[i] = null;
                }
            }
            if(isExistsParam){
                JSONObject jsonObject = new JSONObject(new TreeMap<String,Object>());
                for(int i = 0;i<args.length;i++){
                    jsonObject.put(paramsName[i],args[i]);
                }
                log.setParams(jsonObject);
            }else{
                log.setParams(args);
            }
        }
        Object object = null;
        log.setStartTime();
        try {
            object = pjp.proceed();
        }catch (Exception e){
            e.printStackTrace();
            if(isRequest){
                object = "404";
            }else{
                throw e;
            }
        }
        log.setEndTime();
        log.setResult(object);
        if(isRequest)logger.info(log);
        return object;
    }
}
