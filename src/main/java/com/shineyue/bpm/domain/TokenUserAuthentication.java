package com.shineyue.bpm.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security中存放认证用户
 *
 * @author liubo
 * @version 2017-12-05 11:35
 **/
public class TokenUserAuthentication implements Authentication {
    private static final long serialVersionUID = 3730332217518791533L;

    private SysUser SysUser;

    private Boolean authentication = false;

    public TokenUserAuthentication(SysUser SysUser, Boolean authentication) {
        this.SysUser = SysUser;
        this.authentication = authentication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
       /* List<SysRole> roles=SysUser.getRoles();
        for(SysRole role:roles){
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }*/
        return auths;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getDetails() {
        return SysUser;
    }

    @Override
    public Object getPrincipal() {
        return SysUser.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authentication;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authentication = isAuthenticated;
    }

    @Override
    public String getName() {
        return SysUser.getUsername();
    }

}
