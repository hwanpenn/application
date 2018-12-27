package com.shineyue.bpm.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.http.HttpResult;
import com.shineyue.bpm.http.HttpService;
import com.shineyue.bpm.util.BpmConfigUtil;
import com.shineyue.bpm.util.UserUtil;

@RestController
public class OrganizationController {
	private static final Log log = LogFactory.getLog(OrganizationController.class);
	


	private final BpmConfigUtil config;
	private final ObjectMapper objectMapper;

	@Autowired
	public OrganizationController(BpmConfigUtil config,ObjectMapper objectMapper){
		this.objectMapper=objectMapper;
		this.config=config;
	}

	/**
	 * 获取表单列表
	 * @return
	 */
	@RequestMapping(value="/forms",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(value = HttpStatus.OK)
	public ObjectNode getFormList(String keyword, int firstNumber, int pageSize,String userId){
		if(keyword.equals("undefined")||keyword.trim().equals("")){
			keyword="";
		}
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		ObjectNode modelNode = null;
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("querykey", keyword);
			m.put("pagernumber", firstNumber);
			m.put("pagersize", pageSize);
			m.put("tenantId", user.getFzxbm());
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/forms_query.service",m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return modelNode;
	}

	@RequestMapping(value="/getUpdateForms",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getFormList(String keyword,int limit,int page){
		return this.getFormList(keyword,(page-1)*limit,limit,"");
	}

	
	/**
	 * 获取岗位列表 ，URL：/getJob?keyword=bpm&firstNumber=0&pageSize=100
	 *
	 * @param keyword 搜索关键字
	 * @param firstNumber 开始行号
	 * @param pageSize  每页长度
	 */
	@RequestMapping(value="/getJob",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public  ObjectNode getJob (String keyword, int firstNumber, int pageSize,String userId){
		ObjectNode modelNode = null;
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			if(keyword==null||keyword.equals("undefined")){
				keyword=" ";
			}
			m.put("querykey", keyword);
			m.put("pagernumber", firstNumber);
			m.put("pagersize", pageSize);
			m.put("tenantId", user.getFzxbm());
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/post_query.service", m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
			
	}

	@RequestMapping(value="/getUpdateJobs",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getUpdateJobs(String keyword,int limit,int page){
		return this.getJob(keyword,(page-1)*limit,limit,"");
	}
	
	/**
	 * 获取角色列表 ，URL：/getRole?keyword=bpm&firstNumber=0&pageSize=100
	 * POST 请求，配合EXTJS 分页
	 * @param keyword 搜索关键字
	 * @param firstNumber 开始行号
	 * @param pageSize  每页长度
	 */
	@RequestMapping(value="/getRole",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public  ObjectNode getRole (String keyword, int firstNumber, int pageSize,String userId){
		ObjectNode modelNode = null;
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			if(keyword==null||keyword.equals("undefined")){
				keyword=" ";
			}
			m.put("querykey", keyword);
			m.put("pagernumber", firstNumber);
			m.put("pagersize", pageSize);
			 m.put("tenantId", user.getFzxbm());
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/role_query.service", m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
			
	}

	@RequestMapping(value="/getUpdateRoles",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getUpdateRoles(String keyword,int limit,int page){
		return this.getRole(keyword,(page-1)*limit,limit,"");
	}
	
	/**
	 * 获取部门列表 ，URL：/getDept?keyword=bpm&firstNumber=0&pageSize=100
	 * POST 请求，配合EXTJS 分页
	 * @param keyword 搜索关键字
	 * @param firstNumber 开始行号
	 * @param pageSize  每页长度
	 */
	@RequestMapping(value="/getDept",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public  ObjectNode getDept (String keyword, int firstNumber, int pageSize,String userId){		
		ObjectNode modelNode = null;
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			if(keyword==null||keyword.equals("undefined")){
				keyword=" ";
			}
			m.put("querykey", keyword);
			m.put("pagernumber", firstNumber);
			m.put("pagersize", pageSize);
			 m.put("tenantId", user.getFzxbm());
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/dept_query.service", m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	@RequestMapping(value="/getUpdateDepts",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getUpdateDepts(String keyword,int limit,int page){
		return this.getDept(keyword,(page-1)*limit,limit,"");
	}
	
	/**
	 * 获取规则列表 ，URL：/getDept?keyword=bpm&firstNumber=0&pageSize=100
	 * POST 请求，配合EXTJS 分页
	 * @param keyword 搜索关键字
	 * @param firstNumber 开始行号
	 * @param pageSize  每页长度
	 */
	@RequestMapping(value="/getRule",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public  ObjectNode getRule (String keyword,String userId,int firstNumber, int pageSize){	
		ObjectNode modelNode = null;
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/rules_query.service",m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	@RequestMapping(value="/getUpdateRules",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getUpdateRules(String keyword,int limit,int page){
		return this.getRule(keyword,"",(page-1)*limit,limit);
	}
	
	/**
	 * 获取用户列表 ，URL：/getUsers?keyword=bpm&firstNumber=0&pageSize=100
	 * POST 请求，配合EXTJS 分页
	 * @param keyword 搜索关键字
	 * @param firstNumber 开始行号
	 * @param pageSize  每页长度
	 */
	@RequestMapping(value="/getUsers",method=RequestMethod.GET,produces={"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public  ObjectNode getUsers (String keyword, int firstNumber, int pageSize,String userId){
		ObjectNode modelNode = null;
		 SysUser user = UserUtil.getCurrentUser();
	      Map<String,Object> jsonObject = new HashMap<String,Object>();
	      jsonObject.put("zxbm", user.getFzxbm());
	      jsonObject.put("zjbzxbm", user.getFzjbzxbm());
	      jsonObject.put("jgbm", user.getFjgbm());
	      jsonObject.put("khbh", "");
	      jsonObject.put("ywfl", "10");
	      jsonObject.put("ywlb", "");
	      jsonObject.put("blqd", "zxb");
	      jsonObject.put("ffbm", "");
	      jsonObject.put("userid", user.getId());
		try {
			Map<String,Object> m = new HashMap<String,Object>();
			if(keyword==null||keyword.equals("undefined")){
				keyword=" ";
			}
			m.put("querykey", keyword);
			m.put("pagernumber", firstNumber);
			m.put("pagersize", pageSize);
			 m.put("tenantId", user.getFzxbm());
			 HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/user_query.service", m,objectMapper.writeValueAsString(jsonObject));
			modelNode = (ObjectNode) objectMapper.readTree(hr.getBody());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	@RequestMapping(value="/getUpdateUsers",method=RequestMethod.GET,produces={"application/json"})
	public ObjectNode getUpdateUsers(String keyword,int limit,int page){
		return this.getUsers(keyword,(page-1)*limit,limit,"");
	}

}
