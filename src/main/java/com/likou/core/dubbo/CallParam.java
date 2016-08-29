package com.likou.core.dubbo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by jiangli on 16/8/4.
 */
public class CallParam implements Serializable{

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private String requestId;

    private String requestFrom;

    private long requestTime;

    private long receiveTime;

    private long spendTime;

    private Class className ;

    private Method method ;

    private JSONObject params;

    public CallParam(String requestId, String requestFrom) {
        this.requestId = requestId;
        this.requestFrom = requestFrom;
        this.setRequestTime();
        this.params = new JSONObject(new TreeMap<String,Object>());
    }

    public void add(String name,Object value){
        params.put(name,value);
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getValue(String name){
        return params.get(name);
    }

    public <T> T getValue(String name , Class<T> t){
        return params.getObject(name,t);
    }
    public void setRequestTime(){
        this.requestTime = new Date().getTime();
    }
    public void setReceiveTime(){
        this.receiveTime = new Date().getTime();
        this.spendTime = this.receiveTime - this.requestTime;
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
        sb.append("\t\t\"requestId\":\"").append(requestId).append("\",\n");
        sb.append("\t\t\"requestFrom\":\"").append(requestFrom).append("\",\n");
        sb.append("\t\t\"requestTime\":\"").append(sdf.format(new Date(requestTime))).append("\",");
        sb.append("\"receiveTime\":\"").append(sdf.format(new Date(receiveTime))).append("\",");
        sb.append("\"spendTime\":\"").append(spendTime).append("ms\",\n");
        sb.append("\t\t\"classname\":\"").append(className.toString()).append("\",");
        sb.append("\"method\":\"").append(method.getName()).append("ms\",\n");
        sb.append("\t\t\"params\":").append(params.toJSONString()).append("\n");
        sb.append("\t\t}");
        return sb.toString();
    }
}
