package com.example.kvmmanger.common.contant;


public enum CodeConstant {
    SUCCESS("成功", 1),
    FAIL("未知错误", 2),
    RECORD_NOT_FOUND("目标未找到", 3),
    RECORD_ALREADY_EXISTS("目标已存在", 4),
    PARAMETER_ERROR("参数错误", 5),
    ;

    private String text;
    private int code;

    CodeConstant(String text, int code) {
        this.text = text;
        this.code = code;
    }

    public static String getText(int code) {
        for (CodeConstant c : CodeConstant.values()) {
            if (c.getCode() == code) {
                return c.text;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
