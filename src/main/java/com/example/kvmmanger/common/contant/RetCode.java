package com.example.kvmmanger.common.contant;


public enum RetCode {
    SUCCESS("成功", 1001),
    FAIL("未知错误", 1002),
    RECORD_NOT_FOUND("目标未找到", 1003),
    RECORD_ALREADY_EXISTS("目标已存在", 1004),
    PARAMETER_ERROR("参数错误", 1005),

    AUTH_INVALIDATION("登录已失效，请重新登录", 601),
    AUTH_FAIL("认证失败", 601),
    AUTH_NO_ACCESS("抱歉，您没有访问权限", 603),
    ;

    private String text;
    private int code;

    RetCode(String text, int code) {
        this.text = text;
        this.code = code;
    }

    public static String getText(int code) {
        for (RetCode c : RetCode.values()) {
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
