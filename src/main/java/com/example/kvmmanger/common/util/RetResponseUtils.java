package com.example.kvmmanger.common.util;

import com.alibaba.fastjson.JSON;
import com.example.kvmmanger.common.Result;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

public class RetResponseUtils {
    public static void out(ServletResponse response, Result result) {

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
//            log.error(e + "输出JSON出错");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
