package com.example.kvmmanger.common.exception;


import com.example.kvmmanger.common.contant.RetCode;

public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }
    public BusinessException(RetCode code, String msg) {
        super(msg);
        this.code = code.getCode();
    }
    public BusinessException(RetCode code) {
        super(code.getText());
        this.code = code.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
