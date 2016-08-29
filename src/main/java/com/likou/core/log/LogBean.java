package com.likou.core.log;


import com.alibaba.fastjson.JSONObject;
import com.likou.common.character.IDGen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by jiangli on 16/8/3.
 */
public class LogBean implements Serializable{
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    protected String uuid;
    protected JSONObject message;
    protected long startTime;
    protected long endTime;
    protected long spendTime;

    public LogBean() {
        message = new JSONObject(new TreeMap<String,Object>());
        uuid = IDGen.get32ID();
    }

    public void setStartTime() {
        this.startTime = new Date().getTime();
    }

    public void setEndTime() {
        this.endTime = new Date().getTime();
        this.spendTime = this.endTime - this.startTime;
    }

    public void add(String field,Object message){
        this.message.put(field,message);
    }
    public void setParams(Object[] params){
        this.message.put("params",params);
    }
    public void setParams(JSONObject paramJson){
        this.message.put("params",paramJson);
    }
    public void setResult(Object result){
        this.message.put("result",result);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t{\n");
        sb.append("\t\t\"uuid\":\"").append(uuid).append("\",");
        sb.append("\"start\":\"").append(sdf.format(new Date(startTime))).append("\",");
        sb.append("\"end\":\"").append(sdf.format(new Date(endTime))).append("\",");
        sb.append("\"spend\":\"").append(spendTime).append("ms\",\n");
        sb.append("\t\t\"message\":").append(message.toJSONString()).append("\n");
        sb.append("\t\t}");
        return sb.toString();
    }
}
