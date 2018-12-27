package com.shineyue.bpm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shineyue.bpm.dao.SysUserRepository;
import com.shineyue.bpm.domain.SysUser;

public class CustomUserService implements UserDetailsService {
	@Autowired
	SysUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		SysUser user = userRepository.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("用户名不存在");
		}
		return user; 
	}

}
