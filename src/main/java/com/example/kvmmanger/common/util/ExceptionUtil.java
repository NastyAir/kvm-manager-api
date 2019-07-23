package com.example.kvmmanger.common.util;

public class ExceptionUtil {
    public static String getStackInfo(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString());
        sb.append("\n");
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            sb.append("    at ");
            sb.append(element.getClassName());
            sb.append(".");
            sb.append(element.getMethodName());
            sb.append("(");
            sb.append(element.getFileName());
            sb.append(":");
            sb.append(element.getLineNumber());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }
}
