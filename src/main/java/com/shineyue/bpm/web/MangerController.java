package com.shineyue.bpm.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.domain.ZipBean;
import com.shineyue.bpm.http.HttpResult;
import com.shineyue.bpm.http.HttpService;
import com.shineyue.bpm.util.BpmConfigUtil;
import com.shineyue.bpm.util.CreateModelDefinitionZip;
import com.shineyue.bpm.util.ReadZipFIle;
import com.shineyue.bpm.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value={"/manager"})
public class MangerController {
	private static final Log log = LogFactory.getLog(MangerController.class);
	private final BpmConfigUtil config;
	private final ObjectMapper objectMapper;

	@Autowired
	public MangerController(BpmConfigUtil config,ObjectMapper objectMapper){
		this.objectMapper=objectMapper;
		this.config=config;
	}

	/**
	 * 获取流程模型集合
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/models/query_Models" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getEditorJson(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = null;
		SysUser user = UserUtil.getCurrentUser();
		try {
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("appkey", values.get("key"));
			m.put("querykey", values.get("keyword"));
			m.put("pagernumber", (Integer.parseInt(values.get("page").toString())-1)*Integer.parseInt((values.get("limit"))));
			m.put("pagersize", values.get("limit"));
			m.put("tenantId", user.getFzxbm());
			HttpResult hr = HttpService.doPost(
					"http://" +config.getAddress() + ":" +config.getPort()
							+ "/BPM/manage/bpmflow_query.service",
					m, objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			modelNode = objectMapper.createObjectNode();
			modelNode.put("count", data.get("totalNumber").asText());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("currentUser",user.getCaption());
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("info", "查询异常！");
			modelNode.put("currentUser",user.getCaption());
			modelNode.put("code",0);
			modelNode.put("msg",e.getMessage());
		}
		return modelNode;
	}
	
	/**
	 * 流程模型创建
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/model/processInfoSave" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode processInfoSave(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();;
		try {
			SysUser user = UserUtil.getCurrentUser();
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			String key = (values.containsKey("key") ? values.get("key").toString(): " ");
			String name = (values.containsKey("name") ? values.get("name").toString() : " ");

			if (StringUtils.isBlank(name)) {
				modelNode.put("success", false);
				modelNode.put("msg", "流程名称不能为空");
				return modelNode;
			}

			if (StringUtils.isBlank(key)) {
				modelNode.put("success", false);
				modelNode.put("msg", "应用KEY不能为空");
				return modelNode;
			}
			String description = (values.containsKey("description") ? values.get("description").toString() : " ");
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("appKey", key);
			m.put("description", description);
			m.put("name", name);
			m.put("tenantId", user.getFzxbm());
			HttpResult hr = HttpService.doPost(
					"http://" + config.getAddress() + ":" + config.getPort()
							+ "/BPM/manage/processmodel_create.service",
					m, objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			if(data.get("success").asBoolean()){
				modelNode.put("id", data.get("id").toString());
				modelNode.put("success", true);
			}else{
				modelNode.put("success", false);
				modelNode.put("msg", data.get("msg").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("msg", "创建异常！");
		}
		return modelNode;
	}
	
	/**
	 * 流程发布
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/model/deployProcess" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode deployProcess(@RequestParam Map<String, Object>  values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String s_rnt = "";
		try {
			SysUser user = UserUtil.getCurrentUser();
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			String source = (values.containsKey("source") ? values.get("source").toString(): " ");
			String sourceMc = (values.containsKey("sourceMc") ? values.get("sourceMc").toString() : " ");
			String[] mod_arr = source.trim().split(";");
			String[] modelMc_arr = sourceMc.trim().split(";");
			for (int i = 0; i < mod_arr.length; i++) {
				if (!mod_arr[i].trim().equals("")) {
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("modelId", mod_arr[i]);
					m.put("tenantId",user.getFzxbm());
					HttpResult hr = HttpService.doPost(
							"http://" + config.getAddress() + ":" + config.getPort()
									+ "/BPM/manage/process_deploy.service",
							m, objectMapper.writeValueAsString(jsonObject));
					JsonNode data = this.objectMapper.readTree(hr.getBody());
					success=data.get("success").asBoolean();
					if (!success) {
						s_rnt = "流程(" + modelMc_arr[i].trim() + ")"+data.get("msg").toString()+"，请检查！";
						break;
					}
				}
			}
			if (success) {
				s_rnt = "发布流程成功！";
			}
			
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			s_rnt = e.getMessage();
		}
		modelNode.put("msg", s_rnt);
		modelNode.put("success", success);
		return modelNode;
	}
	
	/**
	 * 流程取消发布
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/model/unDeployProcess" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode unDeployProcess(@RequestParam Map<String, Object>  values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();;
		boolean success = false;
		String s_rnt = "";
		try {
			SysUser user = UserUtil.getCurrentUser();
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			String source = (values.containsKey("source") ? values.get("source").toString(): " ");
			String sourceMc = (values.containsKey("sourceMc") ? values.get("sourceMc").toString() : " ");
			String deploymentId = (values.containsKey("deploymentId") ? values.get("deploymentId").toString() : " ");
			// 确定是否包含所有的流程实例
			boolean includeAllInstances = (boolean) (values.containsKey("includeAllInstances") ? values
								.get("includeAllInstances") : false);
			String[] mod_arr = source.trim().split(";");
			String[] modelMc_arr = sourceMc.trim().split(";");
			String[] deployment_arr = deploymentId.trim().split(";");
			for (int i = 0; i < mod_arr.length; i++) {
				if (!mod_arr[i].trim().equals("")) {
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("modelId",mod_arr[i].trim());
					m.put("isForceDeleteAllHistory", includeAllInstances);
					m.put("deploymentId", deployment_arr[i].trim());
					m.put("tenantId", user.getFzxbm());
					HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/process_undeploy.service",m,objectMapper.writeValueAsString(jsonObject));
					JsonNode data = this.objectMapper.readTree(hr.getBody());
					success=data.get("success").asBoolean();
					if (!success) {
						s_rnt = "取消发布(" + modelMc_arr[i].trim() + ")流程失败，请检查！";
						break;
					}
				}
			}
			if (success) {
				s_rnt = "取消发布流程成功！";
			}
			modelNode.put("success", true);
			modelNode.put("msg", s_rnt);
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			modelNode.put("success", false);
			modelNode.put("msg", "取消发布异常！");
		}
		return modelNode;
	}

	/**
	 * 流程导入
	 * @param file 参数变量
	 */
	@RequestMapping(value = { "/model/impProcess" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode impProcess(@RequestParam("file") MultipartFile file) {
		String fileName = file.getOriginalFilename();
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		boolean isOverride = true;
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			if (fileName!=null&&fileName.endsWith(".zip")) {
				//开始解析zip
				InputStream is = file.getInputStream();
				ZipBean zipBean = ReadZipFIle.readZip(is);
				if (StringUtils.isBlank(zipBean.getMsg())) {
					//msg为空解析成功
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tenantId",user.getFzxbm());
					map.put("modelMap",zipBean.getModles());
					map.put("isOverride", true);
					HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/newprocessmodel_import.service",map,objectMapper.writeValueAsString(jsonObject));
					JsonNode data = this.objectMapper.readTree(hr.getBody());
					success = data.get("success").asBoolean();
					msg = data.get("msg").asText();
				} else {
					success = false;
					msg = zipBean.getMsg();
				}
			}else{
				success=false;
				msg="导入文件不是zip文件";
			}
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		return modelNode;
	}

	/**
	 * 流程模型导出
	 * @param response response
	 * @param modelIds 模型id
	 */
	@RequestMapping(value = "/modle-definitions/{modelIds}/export", method = RequestMethod.GET)
	public void exportModelDefinition(HttpServletResponse response,@PathVariable String modelIds){
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("zxbm", user.getFzxbm());
		jsonObject.put("zjbzxbm", user.getFzjbzxbm());
		jsonObject.put("jgbm", user.getFjgbm());
		jsonObject.put("khbh", "");
		jsonObject.put("ywfl", "10");
		jsonObject.put("ywlb", "");
		jsonObject.put("blqd", "zxb");
		jsonObject.put("ffbm", "");
		jsonObject.put("userid", user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id",modelIds);
		try {
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/newprocessmodel_export.service",map,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			String modelsMap = data.get("modelMap").asText();
			success = data.get("success").asBoolean();
			msg = data.get("msg").asText();
			if(!success){
				success = false;
			}else{
				CreateModelDefinitionZip.createModelDefinitnionZip(response,modelsMap);
			}
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			success = false;
			msg = e.getMessage();
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
	}

	/**
	 * 流程删除
	 * @param values 参数
	 */
	@RequestMapping(value = { "/model/processDel" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode processDel(@RequestParam Map<String, Object>  values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		int errCount = 0; // 标记出错记录数
		String s_rnt = "";
		boolean isOverride = true;
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String source = (values.containsKey("source") ? values.get("source").toString() : " ");
			String sourceMc = (values.containsKey("sourceMc") ? values.get("sourceMc").toString() : " ");
			String processKey = (values.containsKey("processKey")? values.get("processKey").toString() : " ");
			String[] mod_arr = source.trim().split(";");
			String[] modelMc_arr = sourceMc.trim().split(";");
			String[] processKey_arr = processKey.trim().split(";");
			String msg="";
			for (int i = 0; i < mod_arr.length; i++) {
				if (!mod_arr[i].trim().equals("")) {
					try {
						Map<String,Object> m = new HashMap<String,Object>();
						m.put("modelId", mod_arr[i].trim());
						m.put("processKey", processKey_arr[i].trim());
						m.put("tenantId", user.getFzxbm());
						HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/processmodel_delete.service",m,objectMapper.writeValueAsString(jsonObject));
						JsonNode data = this.objectMapper.readTree(hr.getBody());
						success=data.get("success").asBoolean();
						msg=data.get("msg").toString();
					} catch (Exception e) {
						e.printStackTrace();
						success = false;
						errCount = errCount + 1;
						s_rnt = s_rnt + "删除(" + modelMc_arr[i].trim() + ")失败，"
								+ e.getMessage();
					}
				}
			}
			if (success) {
				s_rnt = "所有选中流程删除成功！";
			} else {
				if (mod_arr.length > errCount) {
					s_rnt = s_rnt + msg+"<br>";
				}
			}
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		modelNode.put("success", success);
		modelNode.put("msg", s_rnt);
		return modelNode;
	}

	/**
	 * 复制流程
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/model/copyProcess" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode copyProcess(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String id = "";
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String key = (values.containsKey("key") ? values.get("key").toString() : " ");
			String name = (values.containsKey("name") ? values.get("name").toString() : " ");
			String description = (values.containsKey("description") ? values.get("description").toString() : " ");
			String modelId = (values.containsKey("modelId") ? values.get("modelId").toString() : " ");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("name", name);
			m.put("description", description);
			m.put("appKey", key);
			m.put("modelId", modelId);
			m.put("tenantId", user.getFzxbm());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/processmodel_copy.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			id = data.get("id").asText();
			if (!id.trim().equals("")) {
				success = true;
			} else {
				success = false;
				msg = "服务端异常，获取到的ID为空！";
			}
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		modelNode.put("id", id);
		return modelNode;
	}

	/**
	 * 流程定义查询
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/processes/getBpmProcessDefinistionList" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getBpmProcessDefinistionList(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = null;
		try {
			SysUser user = UserUtil.getCurrentUser();
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			String key = (values.containsKey("key") ? values.get("key"): " ");
			String processKey = (values.containsKey("processKey") ? values.get("processKey") : "null");
			String state = (values.containsKey("state") ? values.get("state"): "null");
			String query = (values.containsKey("query") ? values.get("query") : "null");
			int start = (values.containsKey("page") ? (Integer.parseInt(values.get("page").toString())-1)*Integer.parseInt((values.get("limit")))
					 : 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);

			Map<String,Object> m = new HashMap<String,Object>();
			m.put("key", key);
			m.put("processKey", processKey);
			m.put("tenantId", user.getFzxbm());
			m.put("state", state);
			m.put("query", query);
			m.put("start",start);
			m.put("size", size);
			HttpResult hr = HttpService.doPost(
					"http://" +config.getAddress() + ":" +config.getPort()
							+ "/BPM/moniter/processdefinition_query.service",
					m, objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			modelNode = objectMapper.createObjectNode();
			modelNode.put("count", data.get("totalNumber").asText());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常！");
		}
		return modelNode;
	}

	/**
	 * 取消流程定义
	 * @param values 参数集合
	 */
	@RequestMapping(value = { "/process/unDeployProcessDefinition" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode unDeployProcessDefinition(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		boolean isActivation = false;
		String msg = "";
		String info = "";
		String errorName = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String deploymentId = (values.containsKey("deploymentId") ? values.get("deploymentId").toString() : " ");
			boolean includeAllInstances = (values.containsKey("includeAllInstances") ? Boolean.parseBoolean(values.get("includeAllInstances").toString()) : false);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("deploymentId", deploymentId);
			m.put("includeAllInstance", includeAllInstances);
			m.put("tenantId", user.getFzxbm());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/processdefinition_delete.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			isActivation = Boolean.parseBoolean(data.get("success").toString());
			if (isActivation) {
				msg = "操作成功";
				success = true;
			} else {
				msg = "操作失败";
				success = false;
			}
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			info = errorName + " : " + e.getMessage();
			msg = "操作失败";
			success = false;
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		modelNode.put("info", info);
		return modelNode;
	}

	/**
	 *激活或暂停流程定义
	 * @param values 参数值
	 */
	@RequestMapping(value = { "/process/activateOrSuspendProcessDefintion" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode activateOrSuspendProcessDefintion(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		boolean isActivation = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			boolean activation = (values.containsKey("activation") ? Boolean.parseBoolean(values.get("activation").toString()) : false);
			String processDefinitionId = (values.containsKey("processDefinitionId") ? values.get("processDefinitionId").toString() : " ");
			String validationTime = (values.containsKey("validationTime") ? values.get("validationTime").toString() : " ");
			boolean includeAllInstances = (values.containsKey("includeAllInstances") ? Boolean.parseBoolean(values.get("includeAllInstances").toString()) : false);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processDefinitionId", processDefinitionId);
			m.put("activation", activation);
			m.put("includeProcessInstances", includeAllInstances);
			m.put("validationTime", validationTime);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/definition_activateorsuspend.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			isActivation = Boolean.parseBoolean(data.get("success").toString());
			if (isActivation) {
				msg = "操作成功";
				success = true;
			} else {
				msg = "操作失败";
				success = false;
			}
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			info = "操作异常：" + e.getMessage();
			msg = "操作失败";
			success = false;
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		modelNode.put("info", info);
		return modelNode;
	}

	/**
	 * 流程实例查询
	 * @param values 参数值
	 */
	@RequestMapping(value = { "/processInstances/getBpmProcessInstanceList" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getBpmProcessInstanceList(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = null;
		try {
			SysUser user = UserUtil.getCurrentUser();
			Map<String, Object> jsonObject = new HashMap<String, Object>();
			jsonObject.put("zxbm", user.getFzxbm());
			jsonObject.put("zjbzxbm", user.getFzjbzxbm());
			jsonObject.put("jgbm", user.getFjgbm());
			jsonObject.put("khbh", "");
			jsonObject.put("ywfl", "10");
			jsonObject.put("ywlb", "");
			jsonObject.put("blqd", "zxb");
			jsonObject.put("ffbm", "");
			jsonObject.put("userid", user.getId());
			String key = (values.containsKey("key") ? values.get("key"): " ");
			String processDefinitionId = (values.containsKey("processDefinitionId") ? values.get("processDefinitionId") : "");
			String state = (values.containsKey("state") ? values.get("state"): "");
			String query = (values.containsKey("keyword") ? values.get("keyword").toString() : "");
			int start = (values.containsKey("page") ? (Integer.parseInt(values.get("page").toString())-1)*Integer.parseInt((values.get("limit")))
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("key", key);
			m.put("processDefinitionId", processDefinitionId);
			m.put("state", state);
			m.put("keyword", query);
			m.put("dir", "");
			m.put("tenantId", user.getFzxbm());
			m.put("start", start);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost(
					"http://" +config.getAddress() + ":" +config.getPort()
							+ "/BPM/moniter/processinstance_query.service",
					m, objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			modelNode = objectMapper.createObjectNode();
			modelNode.put("count", data.get("totalNumber").asText());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常！");
		}
		return modelNode;
	}

	/**
	 * 删除流程实例
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/DeleteProcessInstance" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode activateDeleteProcessInstance(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId").toString() : " ");
			String deleteReason = (values.containsKey("deleteReason") ? values.get("deleteReason").toString() : " ");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			m.put("deleteReason", deleteReason);
			m.put("userId", user.getId());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/processinstance_delete.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			success=data.get("success").asBoolean();
		}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			msg = "操作失败";
			success = false;
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		modelNode.put("info", info);
		return modelNode;
	}

	/**
	 * 激活或暂停流程实例
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/activateOrSuspendProcessInstance" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode activateOrSuspendProcessInstance(@RequestParam Map<String, String> values) {
		ObjectNode modelNode  = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");

			boolean activation = (values.containsKey("activation") ? Boolean.parseBoolean(values.get("activation")) : false);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			m.put("activation", activation);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/processinstance_activateorsuspend.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			if(data.get("success").asBoolean()){
				msg = "操作成功";
				success = true;
			}else{
				success= false;
				msg = data.get("msg").asText();
			}

			}catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			e.printStackTrace();
			msg = "操作失败";
			success = false;
		}
		modelNode.put("success", success);
		modelNode.put("msg", msg);
		modelNode.put("info", info);
		return modelNode;
	}

	/**
	 * 流程实例详细
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/getProcessInstanceDetailInfo" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getProcessInstanceDetailInfo(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/processinstancedetailinfo_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("success", true);
			modelNode.put("totals",1);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
	return modelNode;
	}

	/**
	 * 获取流程实例活动记录
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/getProcessInstanceActivityDetailInfo" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getProcessInstanceActivityDetailInfo(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");
			int start = (values.containsKey("page") ? Integer.parseInt(values.get("page"))-1
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			m.put("start", start);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/instanceactivityinfo_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("count",data.get("totalNumber").asLong());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 * 获取流程实例任务信息
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/getProcessInstanceTaskInfo" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getProcessInstanceTaskInfo(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");
			int start = (values.containsKey("page") ? Integer.parseInt(values.get("page"))-1
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			m.put("start", start);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/instancetaskdetailinfo_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("count",data.get("totalNumber").asLong());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 * 获取流程实例变量信息
	 * @values 参数
	 */
	@RequestMapping(value = { "/processInstance/getProcessInstanceVariableInfo" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getProcessInstanceVariableInfo(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");
			int start = (values.containsKey("page") ? Integer.parseInt(values.get("page"))-1
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			m.put("start", start);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/processinstancevariable_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("count",data.get("totalNumber").asLong());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 * 获取流程活动图
	 * @values 参数
	 */
	@RequestMapping(value = { "/processInstance/getProcessDiagram" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode getProcessDiagram(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String processInstanceId = (values.containsKey("processInstanceId") ? values.get("processInstanceId") : "");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("processInstanceId", processInstanceId);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/task/processdiagram_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("src", "data:image/png;base64,"+data.get("base64").asText());
			modelNode.put("success", true);
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("info", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 *获取流程作业
	 * @param values 参数
	 */
	@RequestMapping(value = { "/job/findJobList" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode findJobList(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String keyword = (values.containsKey("keyword") ? values.get("keyword") : "");
			int start = (values.containsKey("page") ? Integer.parseInt(values.get("page"))-1
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("keyword", keyword);
			m.put("tenantId",user.getFzxbm());
			m.put("start",start);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/joblist_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("count", data.get("totalNumber").asText());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 *获取定时启动流程作业
	 * @param values 参数
	 */
	@RequestMapping(value = { "/job/findStartJobList" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode findStartJobList(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String keyword = (values.containsKey("keyword") ? values.get("keyword") : "");
			int start = (values.containsKey("page") ? Integer.parseInt(values.get("page"))-1
					: 0);
			int size = (values.containsKey("limit") ? Integer.parseInt(values.get("limit"))
					: 20);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("keyword", keyword);
			m.put("tenantId",user.getFzxbm());
			m.put("start",start*size);
			m.put("pageNumber", size);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/startjoblist_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			modelNode.put("count", data.get("totalNumber").asText());
			modelNode.put("success", true);
			modelNode.put("data", data.get("items"));
			modelNode.put("code",0);
			modelNode.put("msg","");
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("code",0);
			modelNode.put("msg", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 * 执行或删除流程作业
	 * @param values 参数
	 */
	@RequestMapping(value = { "/job/executeOrDeleteJob" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode executeOrDeleteJob(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String jobId = (values.containsKey("jobId") ? values.get("jobId") : "");
			boolean isDeleteJob = (values.containsKey("isDeleteJob") ? Boolean.parseBoolean(values.get("isDeleteJob")) : false);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("jobId", jobId);
			m.put("isDeleteJob",isDeleteJob);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/job_executeordelete.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			if(Boolean.parseBoolean(data.get("success").asText())){
				msg = "操作成功";
				success = true;
			}else{
				msg=data.get("msg").asText();
				success = false;
			}
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			modelNode.put("info", "查询异常：" + e.getMessage());
			e.printStackTrace();
		}
		modelNode.put("success",success);
		modelNode.put("msg",msg);
		return modelNode;
	}

	/**
	 *候选人修改
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/updateCandidates" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode updateCandidates(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String taskId = (values.containsKey("taskId") ? values.get("taskId") : "");
			String users = (values.containsKey("users") ? values.get("users") : "");
			String groups = (values.containsKey("groups") ? values.get("groups") : "");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("taskId",taskId);
			m.put("users", users);
			m.put("groups", groups);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/user_update.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			msg=data.get("msg").asText();
			success=data.get("success").asBoolean();
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			success=false;
			msg=e.getMessage();
			e.printStackTrace();
		}
		modelNode.put("success",success);
		modelNode.put("msg",msg);
		return modelNode;
	}

	@RequestMapping(value = { "/color/queryCurrentColor" }, method = { RequestMethod.GET }, produces = {"application/json" })
	public ObjectNode queryCurrentColor(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			m.put("tenantId",user.getFzxbm());
			m.put("state",1);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/task/processimagecolor_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			String currentColor = data.get("color").asText();
			modelNode.put("currentColor",currentColor);
			m.put("state",2);
			HttpResult hr1 = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/task/processimagecolor_query.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data1 = this.objectMapper.readTree(hr1.getBody());
			String historyColor = data1.get("color").asText();
			modelNode.put("historyColor",historyColor);
			modelNode.put("success", true);
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			modelNode.put("success", false);
			e.printStackTrace();
		}
		return modelNode;
	}

	/**
	 *流程图颜色修改
	 * @param values 参数
	 */
	@RequestMapping(value = { "/processInstance/manageColor" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode manageColor(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String color = (values.containsKey("color") ? values.get("color") : "");
			String state = (values.containsKey("state") ? values.get("state") : "");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("color",color);
			m.put("state", state);
			m.put("tenantId", user.getFzxbm());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/color_manage.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			msg = data.get("msg").asText();
			success = data.get("success").asBoolean();
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			success = false;
			msg = e.getMessage();
			e.printStackTrace();
		}
		modelNode.put("success",success);
		modelNode.put("msg",msg);
		return modelNode;
	}

	/**
	 * 表单修改
	 * @param values
	 * @return
	 */
	@RequestMapping(value = { "/processInstance/updateFormKey" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode updateFormKey(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String taskId = (values.containsKey("taskId") ? values.get("taskId") : "");
			String formkey = (values.containsKey("formkey") ? values.get("formkey") : "");
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("taskid",taskId);
			m.put("formkey", formkey);
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/moniter/formkey_update.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			msg=data.get("msg").asText();
			success=data.get("success").asBoolean();
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			msg=e.getMessage();
			e.printStackTrace();
		}
		modelNode.put("success",success);
		modelNode.put("msg",msg);
		return modelNode;
	}

    @RequestMapping(value = { "/queryTimeOut" }, method = { RequestMethod.GET }, produces = {"application/json" })
    public ObjectNode queryTimeOut() {
        ObjectNode modelNode = objectMapper.createObjectNode();
        boolean success = false;
        String msg = "";
        String info = "";
        SysUser user = UserUtil.getCurrentUser();
        Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			Map<String,Object> m = new HashMap<>();
			m.put("tenantId",user.getFzxbm());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/queryTimeOut.service",m,objectMapper.writeValueAsString(jsonObject));
			JsonNode data = this.objectMapper.readTree(hr.getBody());
			if("0".equals(data.asText())){
				modelNode.put("day",0);
				modelNode.put("hour",0);
				modelNode.put("minute",0);
			}else{
				modelNode.put("day",data.get("day").asInt());
				modelNode.put("hour",data.get("hour").asInt());
				modelNode.put("minute",data.get("minute").asInt());
			}
			//data.asText().equals("0")
        } catch (Exception e) {
            log.error("运行异常", e);
            log.error(e.getMessage());
            msg=e.getMessage();
            e.printStackTrace();
        }
        modelNode.put("success",success);
        modelNode.put("msg",msg);
        return modelNode;
    }

	@RequestMapping(value = { "/updateTimeOut" }, method = { RequestMethod.POST }, produces = {"application/json" })
	public ObjectNode updateTimeOut(@RequestParam Map<String, String> values) {
		ObjectNode modelNode = objectMapper.createObjectNode();
		boolean success = false;
		String msg = "";
		String info = "";
		SysUser user = UserUtil.getCurrentUser();
		Map<String, Object> jsonObject = new HashMap<String, Object>();
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
			String day = values.get("day");
			String hour = values.get("hour");
			String minute = values.get("minute");
			Map<String,Object> m = new HashMap<>();
			m.put("day",day);
			m.put("hour", hour);
			m.put("minute", minute);
			m.put("tenantId",user.getFzxbm());
			HttpResult hr = HttpService.doPost("http://" + config.getAddress() + ":" + config.getPort()+"/BPM/manage/managerTimeOut.service",m,objectMapper.writeValueAsString(jsonObject));
			msg="修改成功";
			success=true;
		} catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
			msg=e.getMessage();
			e.printStackTrace();
		}
		modelNode.put("success",success);
		modelNode.put("msg",msg);
		return modelNode;
	}
}
