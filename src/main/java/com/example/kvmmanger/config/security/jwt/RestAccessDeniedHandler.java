package com.example.kvmmanger.config.security.jwt;


import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.common.util.RetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.kvmmanger.common.contant.RetCode.AUTH_NO_ACCESS;

/**
 * @author Exrickx
 */
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

//        RetResponse.out(response,RetResponse.make(AUTH_NO_ACCESS));
        throw new BusinessException(AUTH_NO_ACCESS);

    }

}
