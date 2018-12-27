package com.shineyue.bpm.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="sys_user")
public class SysUser implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String username;
	private String password;
	private String caption;
	private String fzxbm;
	private String fzjbzxbm;
	private String fjgbm;
	@ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
	private List<SysRole> roles;

	public SysUser() {
	}

	public SysUser(Long id,String username, String password, String caption, String fzxbm, String fzjbzxbm, String fjgbm, List<SysRole> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.caption = caption;
		this.fzxbm = fzxbm;
		this.fzjbzxbm = fzjbzxbm;
		this.fjgbm = fjgbm;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { 
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		List<SysRole> roles=this.getRoles();
		for(SysRole role:roles){
			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<SysRole> getRoles() {
		return roles;
	}
	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}
	public String getFzxbm() {
		return fzxbm;
	}
	public void setFzxbm(String fzxbm) {
		this.fzxbm = fzxbm;
	}
	public String getFzjbzxbm() {
		return fzjbzxbm;
	}
	public void setFzjbzxbm(String fzjbzxbm) {
		this.fzjbzxbm = fzjbzxbm;
	}
	public String getFjgbm() {
		return fjgbm;
	}
	public void setFjgbm(String fjgbm) {
		this.fjgbm = fjgbm;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
