package com.shineyue.bpm.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shineyue.bpm.dao.SysUserRepository;
import com.shineyue.bpm.domain.SysRole;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.http.HttpResult;
import com.shineyue.bpm.http.HttpService;
import com.shineyue.bpm.util.BpmConfigUtil;
import com.shineyue.bpm.util.UserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员管理类
 *
 * @author liubo
 * @version 2017-12-15 16:45
 **/
@RestController
public class AdminManangeController {
    private static final Log log = LogFactory.getLog(AdminManangeController.class);

    private final ObjectMapper objectMapper;

    private final SysUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final BpmConfigUtil config;

    @Autowired
    public AdminManangeController( ObjectMapper objectMapper,SysUserRepository userRepository,PasswordEncoder passwordEncoder,BpmConfigUtil config){
        this.objectMapper=objectMapper;
        this.userRepository=userRepository;
        this.passwordEncoder = passwordEncoder;
        this.config = config;
    }

    /**
     * 管理员增加
     * @param values 参数
     */
    @RequestMapping(value = { "/admin/addAdmin" }, method = { RequestMethod.POST }, produces = {"application/json" })
    public ObjectNode addAdmin(@RequestParam Map<String, String> values){
        ObjectNode modelNode = objectMapper.createObjectNode();
        boolean success = false;
        String msg = "";
        try {
            Long id = Long.parseLong(values.get("id"));
            String username = values.get("username");
            String password = passwordEncoder.encode("1234");
            String caption = values.get("caption");
            String fzxbm = values.get("fzxbm");
            String fzjbzxbm = values.get("fzjbzxbm");
            String fjgbm = values.get("fjgbm");
            List<SysRole> roles = new ArrayList<SysRole>();
            SysRole sysRole = new SysRole();
            sysRole.setId(1L);
            sysRole.setName("ROLE_ADMIN");
            roles.add(sysRole);
            SysUser sysUser = new SysUser(id,username,password,caption,fzxbm,fzjbzxbm,fjgbm,roles);
            SysUser user = userRepository.save(sysUser);
            success = true;
            msg = "增加成功！";

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            msg = "增加失败！";
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }

    /**
     * 普通用户增加
     * @param values 参数
     */
    @RequestMapping(value = { "/admin/addUser" }, method = { RequestMethod.POST }, produces = {"application/json" })
    public ObjectNode addUser(@RequestParam Map<String, String> values){
        ObjectNode modelNode = objectMapper.createObjectNode();
        boolean success = false;
        String msg;
        try {
            Long id = Long.parseLong(values.get("id"));
            String username = values.get("username");
            String password = passwordEncoder.encode("1234");
            String caption = values.get("caption");
            String fzxbm = values.get("fzxbm");
            String fzjbzxbm = values.get("fzjbzxbm");
            String fjgbm = values.get("fjgbm");
            List<SysRole> roles = new ArrayList<SysRole>();
            SysRole sysRole = new SysRole();
            sysRole.setId(2L);
            sysRole.setName("ROLE_USER");
            roles.add(sysRole);
            SysUser sysUser = new SysUser(id,username,password,caption,fzxbm,fzjbzxbm,fjgbm,roles);
            SysUser user = userRepository.save(sysUser);
            success = true;
            msg = "增加成功！";

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            msg = "增加失败！";
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }

    /**
     * 权限去除
     * @param values 参数
     */
    @RequestMapping(value = { "/admin/deleteUser" }, method = { RequestMethod.POST }, produces = {"application/json" })
    public ObjectNode deleteUser(@RequestParam Map<String, String> values){
        ObjectNode modelNode = objectMapper.createObjectNode();
        boolean success = false;
        String msg = "";
        try {
            Long id = Long.parseLong(values.get("id"));
            int deleted = userRepository.deleteById(id);
            if(deleted>0){
                success = true;
                msg = "权限去除成功！";
            }else{
                success = false;
                msg = "权限去除失败！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            msg = "权限去除失败！";
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }

    /**
     * 查询所有用户
     * @param keyword 关键字
     * @param limit 每页显示
     * @param page 当前页
     */
    @RequestMapping(value="/queryManagers",method=RequestMethod.GET,produces={"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public  ObjectNode queryManagers (String keyword,int limit,int page){
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
            m.put("querykey", keyword!=null?keyword:"");
            m.put("pagernumber", page);
            m.put("pagersize", limit);
            m.put("tenantId", user.getFzxbm()!=null?user.getFzxbm():"");
            HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort()+"/BPM/organization/manager_query.service",m,objectMapper.writeValueAsString(jsonObject));
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

    /**
     * 密码修改
     * @param values 参数
     */
    @RequestMapping(value = { "/admin/updatePassword" }, method = { RequestMethod.POST }, produces = {"application/json" })
    public ObjectNode updatePassword(@RequestParam Map<String, String> values){
        ObjectNode modelNode = objectMapper.createObjectNode();
        SysUser user = UserUtil.getCurrentUser();
        boolean success = false;
        String msg = "";
        try {
            String oldPassword = values.get("oldPassWord");
            String newPassword = values.get("newPassWord");
            boolean validateOldPassWord = passwordEncoder.matches(oldPassword,user.getPassword());
            if(!validateOldPassWord){
                success=false;
                msg="原始密码错误！";
            }else{
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                success = true;
                msg = "修改成功！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            msg = "密码修改失败！";
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }

    /**
     * 重置密码
     * @param values 参数
     */
    @RequestMapping(value = { "/admin/resetPassword" }, method = { RequestMethod.POST }, produces = {"application/json" })
    public ObjectNode resetPassword(@RequestParam Map<String, String> values){
        ObjectNode modelNode = objectMapper.createObjectNode();
        SysUser user = UserUtil.getCurrentUser();
        boolean success = false;
        String msg = "";
        try {
            Long id = Long.parseLong(values.get("id"));
            SysUser sysUser = userRepository.findById(id);
            sysUser.setPassword(passwordEncoder.encode("1234"));
            userRepository.save(sysUser);
            success=true;
            msg = "重置密码成功！";
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            msg = "重置密码失败！";
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }
}
