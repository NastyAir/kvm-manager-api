package com.example.kvmmanger.config.security.jwt;

import com.alibaba.fastjson.JSON;
import com.example.kvmmanger.common.contant.SecurityConstant;
import com.example.kvmmanger.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.kvmmanger.common.contant.RetCode.AUTH_INVALIDATION;
import static com.example.kvmmanger.common.contant.RetCode.FAIL;

/**
 * @author Exrickx
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private Boolean tokenRedis;

    private Integer tokenExpireTime;

    private StringRedisTemplate redisTemplate;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, Boolean tokenRedis, Integer tokenExpireTime,StringRedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenRedis = tokenRedis;
        this.tokenExpireTime = tokenExpireTime;
        this.redisTemplate = redisTemplate;
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityConstant.HEADER);
        if(StringUtils.isBlank(header)){
            header = request.getParameter(SecurityConstant.HEADER);
        }
        boolean notValid = StringUtils.isBlank(header) || (!tokenRedis && !header.startsWith(SecurityConstant.TOKEN_SPLIT));
        if (notValid) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(header, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {

        // 用户名
        String username = null;
        // 权限
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(tokenRedis){
            // redis
            String v = redisTemplate.opsForValue().get(SecurityConstant.TOKEN_PRE + header);
            if(StringUtils.isBlank(v)){
//                RetResponse.out(response, RetResponse.make(AUTH_INVALIDATION));
//                return null;
                throw new BusinessException(AUTH_INVALIDATION);
            }
//            TokenUser user = new Gson().fromJson(v, TokenUser.class);
//            username = user.getUsername();
//            for(String ga : user.getPermissions()){
//                authorities.add(new SimpleGrantedAuthority(ga));
//            }
//            if(tokenRedis && !user.getSaveLogin()){
//                 若未保存登录状态重新设置失效时间
//                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, v, tokenExpireTime, TimeUnit.MINUTES);
//                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + header, v, tokenExpireTime, TimeUnit.MINUTES);
//            }
        }else{
            // JWT
            try {
                // 解析token
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                        .parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, ""))
                        .getBody();

                //获取用户名
                username = claims.getSubject();
                //获取权限
                String authority = claims.get(SecurityConstant.AUTHORITIES).toString();

                if(StringUtils.isNotBlank(authority)){
                    List<String> list = JSON.parseArray(authority).toJavaList(String.class);
//                    List<String> list = new Gson().fromJson(authority, new TypeToken<List<String>>(){}.getType());
                    for(String ga : list){
                        authorities.add(new SimpleGrantedAuthority(ga));
                    }
                }
            } catch (ExpiredJwtException e) {
//                RetResponse.out(response, RetResponse.make(AUTH_INVALIDATION));
                throw new BusinessException(AUTH_INVALIDATION);

            } catch (Exception e){
                log.error(e.toString());
                throw new BusinessException(FAIL);
//                RetResponse.out(response, RetResponse.make(FAIL));
            }
        }

        if(StringUtils.isNotBlank(username)) {
            //此处password不能为null
            User principal = new User(username, "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        }
        return null;
    }
}

