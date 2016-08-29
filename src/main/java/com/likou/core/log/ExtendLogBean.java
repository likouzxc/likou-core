package com.likou.core.log;


import com.likou.common.character.IDGen;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by jiangli on 16/8/3.
 */
public class ExtendLogBean extends LogBean implements Serializable{

    protected Class className ;
    protected Method method ;

    public ExtendLogBean(){
        super();
    }

    public ExtendLogBean(Class className, Method method ) {
        super();
        this.className = className;
        this.method = method;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t{\n");
        sb.append("\t\t\"uuid\":\"").append(uuid).append("\",");
        sb.append("\"start\":\"").append(sdf.format(new Date(startTime))).append("\",");
        sb.append("\"end\":\"").append(sdf.format(new Date(endTime))).append("\",");
        sb.append("\"spend\":\"").append(spendTime).append("ms\",\n");
        sb.append("\t\t\"classname\":\"").append(className.toString()).append("\",");
        sb.append("\"method\":\"").append(method.getName()).append("\",\n");
        sb.append("\t\t\"message\":").append(message.toJSONString()).append("\n");
        sb.append("\t\t}");
        return sb.toString();
    }
}
