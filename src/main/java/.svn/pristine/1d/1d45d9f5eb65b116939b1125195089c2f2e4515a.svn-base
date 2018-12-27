package com.shineyue.bpm.config;

import com.shineyue.bpm.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * jwt token验证类，验证成功放入SecurityContext
 *
 * @author liubo
 * @version 2017-12-05 11:43
 **/
@Slf4j
public class VerifyTokenFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    public VerifyTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<Authentication> authentication = jwtTokenUtil.verifyToken(request);
            SecurityContextHolder.getContext().setAuthentication(authentication.orElse(null));
            filterChain.doFilter(request,response);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //可以在这里指定重定向还是返回错误接口示例
        }
    }
}
