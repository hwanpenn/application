package com.shineyue.bpm.config;

import com.shineyue.bpm.dao.SysUserRepository;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.util.DecryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 密码校验
 *
 * @author liubo
 * @version 2018-12-13 11：29
 **/
@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password1 = DecryptUtil.DESCrypt((String) authentication.getCredentials());
        SysUser user = userRepository.findByUsername(username);
        if(user==null||!passwordEncoder.matches(password1, user.getPassword())){
            throw new DisabledException("用户名或密码错误！");
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user,authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
