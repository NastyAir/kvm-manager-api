package com.example.kvmmanger.config.security.jwt;

import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.common.exception.LoginFailLimitException;
import com.example.kvmmanger.common.util.RetResponse;
import com.example.kvmmanger.common.util.RetResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Exrickx
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${auth.loginTimeLimit}")
    private Integer loginTimeLimit;

    @Value("${auth.loginAfterTime}")
    private Integer loginAfterTime;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            recordLoginTime(username);
            String key = "loginTimeLimit:"+username;
            String value = redisTemplate.opsForValue().get(key);
            if(StringUtils.isBlank(value)){
                value = "0";
            }
            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = loginTimeLimit - loginFailTime;
            log.info("用户"+username+"登录失败，还有"+restLoginTime+"次机会");
            if(restLoginTime<=3&&restLoginTime>0){
                throw new BusinessException(RetCode.AUTH_FAIL,"用户名或密码错误，还有"+restLoginTime+"次尝试机会");
//                RetResponseUtils.out(response, RetResponse.error("用户名或密码错误，还有"+restLoginTime+"次尝试机会"));
            } else if(restLoginTime<=0) {
                throw new BusinessException(RetCode.AUTH_FAIL,"登录错误次数超过限制，请"+loginAfterTime+"分钟后再试");
//                RetResponseUtils.out(response, RetResponse.error("登录错误次数超过限制，请"+loginAfterTime+"分钟后再试"));
            } else {
                throw new BusinessException(RetCode.AUTH_FAIL,"用户名或密码错误");
//                RetResponseUtils.out(response, RetResponse.error("用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
//            RetResponseUtils.out(response, RetResponse.error("账户被禁用，请联系管理员"));
            throw new BusinessException(RetCode.AUTH_FAIL,"账户被禁用，请联系管理员");

        } else if (e instanceof LoginFailLimitException){
//            RetResponseUtils.out(response, RetResponse.error(((LoginFailLimitException) e).getMsg()));
            throw new BusinessException(RetCode.AUTH_FAIL,((LoginFailLimitException) e).getMsg());
        } else {
            throw new BusinessException(RetCode.AUTH_FAIL,"登录失败，其他内部错误");
//            RetResponseUtils.out(response, RetResponse.error("登录失败，其他内部错误"));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    public boolean recordLoginTime(String username){

        String key = "loginTimeLimit:"+username;
        String flagKey = "loginFailFlag:"+username;
        String value = redisTemplate.opsForValue().get(key);
        if(StringUtils.isBlank(value)){
            value = "0";
        }
        //获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redisTemplate.opsForValue().set(key, String.valueOf(loginFailTime), loginAfterTime, TimeUnit.MINUTES);
        if(loginFailTime>=loginTimeLimit){

            redisTemplate.opsForValue().set(flagKey, "fail", loginAfterTime, TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}
