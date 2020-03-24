package com.taoyuan.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局的数据返回类
 * @ProjectName permission
 * @ClassName JsonData
 * @Date 2019/12/12 8:55
 * @Author taoyuan
 * @Version 1.0
 */
@Setter
@Getter
public class JsonData {

    private boolean ret;

    private String message;

    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    /**
     * 当我们请求成功的时候，我们会向前端返回请求的数据及我们想给前端发送的信息
     * @param data
     * @param message
     * @return
     */
    public static JsonData success(Object data, String message) {
        JsonData jsonData = new JsonData(true);
        jsonData.message = message;
        jsonData.data = data;
        return jsonData;
    }

    /**
     * 当前端请求成功的时候，我们会向前端返回请求数据，但是不想发送任何信息
     * @param data
     * @return
     */
    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        return jsonData;
    }

    /**
     * 当前端请求成功的情况下，我们既不返回数据，也不想发送任何信息
     * @return
     */
    public static JsonData success() {
        return new JsonData(true);
    }

    /**
     * 当前端请求失败的时候，我们会向前端发送请求失败的信息，虽然不想发送任何信息，但是还是要发
     * @param message
     * @return
     */
    public static JsonData fail(String message) {
        JsonData jsonData = new JsonData(false);
        jsonData.message = message;
        return jsonData;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("ret", ret);
        result.put("message", message);
        result.put("data", data);
        return result;
    }
}
