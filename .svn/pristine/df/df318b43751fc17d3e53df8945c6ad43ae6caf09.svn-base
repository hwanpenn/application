package com.shineyue.bpm.util;

import com.shineyue.bpm.domain.SysUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserUtil {
	  public static SysUser getCurrentUser()
	  {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
		  SysUser user = null;
	    if ((securityContext != null) && (securityContext.getAuthentication() != null)) {
	      Object principal = securityContext.getAuthentication().getPrincipal();
	      if ((principal instanceof SysUser)) {
	        user = (SysUser)principal;
	      }
	    }

	    return user;
	  }
}
