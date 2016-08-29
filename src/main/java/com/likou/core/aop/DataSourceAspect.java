package com.likou.core.aop;

import com.likou.core.annotation.DataSource;
import com.likou.core.db.MasterDBDataSourceHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class DataSourceAspect {

    public void before(JoinPoint point) {
        try {
            Method m = ((MethodSignature) point.getSignature()).getMethod();
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource data = m.getAnnotation(DataSource.class);
                MasterDBDataSourceHolder.putDataSource(data.value());
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        System.out.println("beginning----");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        Object object = pjp.proceed();
        System.out.println("ending----");
        return object;
    }

}
