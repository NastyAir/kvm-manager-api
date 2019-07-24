package com.example.kvmmanger.common;

import java.io.Serializable;

/**
 * @author
 * 前后端交互数据标准
 */
//@Builder
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String msg;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Result<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getResult() {
        return result;
    }

    public Result<T> setResult(T result) {
        this.result = result;
        return this;
    }
}
