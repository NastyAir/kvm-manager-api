package com.example.kvmmanger.config.security;

import com.example.kvmmanger.config.security.jwt.AuthenticationFailHandler;
import com.example.kvmmanger.config.security.jwt.AuthenticationSuccessHandler;
import com.example.kvmmanger.config.security.jwt.JWTAuthenticationFilter;
import com.example.kvmmanger.config.security.jwt.RestAccessDeniedHandler;
import com.example.kvmmanger.config.security.logout.MyLogoutHandler;
import com.example.kvmmanger.config.security.logout.MyLogoutSuccessHandler;
import com.example.kvmmanger.config.security.permission.MyFilterSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Security 核心配置类
 * 开启控制权限至Controller
 *
 * @author Exrickx
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth.token.redis}")
    private Boolean tokenRedis;

    @Value("${auth.tokenExpireTime}")
    private Integer tokenExpireTime;

    private final IgnoredUrlsProperties ignoredUrlsProperties;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailHandler failHandler;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final MyLogoutHandler myLogoutHandler;
    private final MyLogoutSuccessHandler myLogoutSuccessHandler;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public WebSecurityConfig(IgnoredUrlsProperties ignoredUrlsProperties, AuthenticationSuccessHandler successHandler, AuthenticationFailHandler failHandler, RestAccessDeniedHandler accessDeniedHandler, MyLogoutHandler myLogoutHandler, MyLogoutSuccessHandler myLogoutSuccessHandler, StringRedisTemplate redisTemplate) {
        this.ignoredUrlsProperties = ignoredUrlsProperties;
        this.successHandler = successHandler;
        this.failHandler = failHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.myLogoutHandler = myLogoutHandler;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
        this.redisTemplate = redisTemplate;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        //除配置文件忽略路径其它所有请求都需经过认证和授权
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        registry.and()
                //默认配置一个Bean Name为corsConfigurationSource
                .cors().and()
                //表单登录方式
                .formLogin()
//                .loginPage("/login")
                //登录请求url
                .loginProcessingUrl("/login")

                .permitAll()

                //成功处理类
                .successHandler(successHandler)
                //失败
                .failureHandler(failHandler)
//                .and().antMatcher(HttpMethod.GET)
                .and().logout().logoutUrl("/logout")
                //注销处理类
                .addLogoutHandler(myLogoutHandler)
                //注销成功处理类
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .and()
                //允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                //任何请求
                .anyRequest()
                //需要身份认证
                .authenticated()
                .and()
                //关闭跨站请求防护
                .csrf().disable()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                //添加自定义权限过滤器
//                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                //添加JWT过滤器 除已配置的其它请求都需经过此过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), tokenRedis, tokenExpireTime, redisTemplate));
    }
}
