package com.example.kvmmanger.common.util;

import com.alibaba.fastjson.JSON;
import com.example.kvmmanger.common.Result;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

@Slf4j
public class RetResponseUtils {
    public static void out(ServletResponse response, Result result) {
        try (PrintWriter out = response.getWriter();) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out.write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e + "输出JSON出错");
        }
    }
}
