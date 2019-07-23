package com.example.kvmmanger.common.contant;

public class MessageTypeConstant {
    public static final String SPARK_MESSAGE = "SPARK_MESSAGE";
    public static final String WARNING_MESSAGE = "WARNING_MESSAGE";
    public static final String TASK_MESSAGE = "TASK_MESSAGE";
    public static final String SYSTEM_MESSAGE = "SYSTEM_MESSAGE";


    public static String getBusinessTypeByType(String type) {
        switch (type) {
            case MessageTypeConstant.SPARK_MESSAGE:
                return MessageTypeConstant.SPARK_MESSAGE;
            case MessageTypeConstant.TASK_MESSAGE:
                return MessageTypeConstant.TASK_MESSAGE;
            case MessageTypeConstant.SYSTEM_MESSAGE:
                return MessageTypeConstant.SYSTEM_MESSAGE;
            case MessageTypeConstant.WARNING_MESSAGE:
                return MessageTypeConstant.WARNING_MESSAGE;
            default:
                return "";
        }
    }
}
