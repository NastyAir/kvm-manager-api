package com.example.kvmmanger.common.util;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.contant.RetCode;

public class RetResponse {

    public static <T> Result<T> success() {
        return new Result<T>().setCode(RetCode.SUCCESS.getCode()).setMsg(RetCode.SUCCESS.getText());
    }

    public static <T> Result<T> success(String message) {
        return new Result<T>().setCode(RetCode.SUCCESS.getCode()).setMsg(message);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode(RetCode.SUCCESS.getCode()).setMsg(RetCode.SUCCESS.getText()).setResult(data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>().setCode(RetCode.FAIL.getCode()).setMsg(message);
    }

    public static <T> Result<T> make(RetCode code) {
        return new Result<T>().setCode(code.getCode()).setMsg(code.getText());
    }

    public static <T> Result<T> make(int code, String msg) {
        return new Result<T>().setCode(code).setMsg(msg);
    }

    public static <T> Result<T> make(int code, String msg, T data) {
        return new Result<T>().setCode(code).setMsg(msg).setResult(data);
    }
}
