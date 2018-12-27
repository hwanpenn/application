package com.shineyue.bpm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shineyue.bpm.domain.SysUser;

import javax.transaction.Transactional;

public interface SysUserRepository extends JpaRepository<SysUser, Long>{
	
	SysUser findByUsername(String username);

	SysUser findById(Long id);

	SysUser save(SysUser sysUser);

	@Transactional
	int deleteById(Long id);
}
