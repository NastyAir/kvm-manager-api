package com.example.kvmmanger.common;

import java.util.HashMap;

public class RestMessage extends HashMap<String, Object> {

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public void setCode(Integer code) {
        this.put("code", code);
    }

    public String getMsg() {
        return (String) this.get("msg");
    }

    public void setMsg(String msg) {
        this.put("msg", msg);
    }

    public Object getData() {
        return this.get("data");
    }

    public void setData(Object data) {
        this.put("data", data);
    }
}
