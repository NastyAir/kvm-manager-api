package com.example.kvmmanger.config.security.logout;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyLogoutHandler implements LogoutHandler {
    @Override
//    @SysLog(moudle = Constant.LOG_MOUDLE_SYSTEM_AUTH, event = Constant.LOG_EVENT_LOGOUT, logType = Constant.LOG_TYPE_DEFENSE)
    public void logout(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) {
        request.getSession().invalidate();
    }
}
