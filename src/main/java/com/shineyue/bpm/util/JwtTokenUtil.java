package com.shineyue.bpm.util;

import com.shineyue.bpm.domain.TokenUserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.shineyue.bpm.domain.SysUser;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * Jwt Token
 *
 * @author liubo
 * @version 2017-12-05 11:22
 **/
public class JwtTokenUtil {
    /**
     * token过期时间
     */
    private static final long VALIDITY_TIME_MS = 24 * 60 * 60 * 1000; // 24 hours  validity
    /**
     * header中标识
     */
    private static final String AUTH_HEADER_NAME = "x-authorization";

    /**
     * 签名密钥
     */
  //  @Value("${jwt.token.secret}")
    private String secret;

    /**
     * 验签
     */
    public Optional<Authentication> verifyToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.isEmpty()){
            final SysUser user = parse(token.trim());
            if (user != null) {
                return Optional.of(new TokenUserAuthentication(user, true));
            }
        }else{
            return Optional.of(new TokenUserAuthentication(null, false));
        }
        return Optional.empty();
    }

    /**
     * 从用户中创建一个jwt token
     * @param userDTO 用户对象
     */
    public String create(SysUser userDTO) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(userDTO.getUsername())
                .claim("id", userDTO.getId())
                .claim("fjgbm", userDTO.getFjgbm())
                .claim("fzjbzxbm", userDTO.getFzjbzxbm())
                .claim("fzxbm",userDTO.getFzxbm())
               // .claim("roles", userDTO.getRoles())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 从token中获取对象
     * @param token token
     */
    public SysUser parse(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        SysUser SysUser = new SysUser();
        SysUser.setId(NumberUtils.toLong(claims.getId()));
        SysUser.setUsername(claims.get("username",String.class));
        SysUser.setFjgbm(claims.get("fjgbm",String.class));
        SysUser.setFzjbzxbm(claims.get("fzjbzxbm",String.class));
        SysUser.setFzxbm(claims.get("fzxbm",String.class));
        return SysUser;
    }

}
