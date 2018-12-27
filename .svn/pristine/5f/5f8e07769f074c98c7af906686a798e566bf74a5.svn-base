package com.shineyue.bpm.web;

import com.shineyue.bpm.domain.SysRole;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.util.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {
	@RequestMapping({ "/" })
	public String index(Model model) {
		SysUser user = UserUtil.getCurrentUser();
		List<SysRole> roles = user.getRoles();
		for(SysRole role:roles){
			if(role.getName().equals("ROLE_SUPERADMIN")){
				return "super";
			}else if(role.getName().equals("ROLE_ADMIN")){
				return "admin";
			}
		}
		return "index";
	}

}
