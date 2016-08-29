package com.likou.core.log;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by jiangli on 16/8/3.
 */
public class URILogBean extends ExtendLogBean implements Serializable{

    protected String[] uri;

    public URILogBean(){
        super();
    }

    public URILogBean(Class className, Method method ) {
        super(className,method);
    }

    public void setUri(String[] uri) {
        this.uri = uri;
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
        sb.append("\"method\":\"").append(method.getName()).append("\",");
        sb.append("\"uri\":\"").append(JSONObject.toJSONString(uri)).append("\",\n");
        sb.append("\t\t\"message\":").append(message.toJSONString()).append("\n");
        sb.append("\t\t}");
        return sb.toString();
    }
}
