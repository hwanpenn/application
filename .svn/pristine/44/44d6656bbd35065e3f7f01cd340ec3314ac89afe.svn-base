package com.shineyue.bpm.web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.shineyue.bpm.dao.SysUserRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shineyue.bpm.domain.SysUser;
import com.shineyue.bpm.http.HttpResult;
import com.shineyue.bpm.http.HttpService;
import com.shineyue.bpm.util.BpmConfigUtil;
import com.shineyue.bpm.util.UserUtil;

@RestController
@RequestMapping("/modelEditor")
public class ModelController {
	 private static final Log log = LogFactory.getLog(ModelController.class);



	private final BpmConfigUtil config;
	private final ObjectMapper objectMapper;
	private final SysUserRepository userRepository;

	@Autowired
	public ModelController(BpmConfigUtil config,ObjectMapper objectMapper,SysUserRepository userRepository){
		this.objectMapper=objectMapper;
		this.config=config;
		this.userRepository=userRepository;
	}

	  @RequestMapping(value={"/model/{modelId}/json"}, method={RequestMethod.GET}, produces={"application/json"})
	  public ObjectNode getEditorJson(@PathVariable String modelId)
	  {
	    ObjectNode modelNode = null;
	    try {
	    	String userId = "";
			if(modelId.indexOf(",")>0){
	    		String[] modelAndUserId = modelId.split(",");
	    		modelId=modelAndUserId[0];
				userId=modelAndUserId[1];
			}
	      SysUser user = UserUtil.getCurrentUser();
	      if(user==null){
			  user = userRepository.findById(Long.parseLong(userId));
		  }
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
	      Map<String,Object> m = new HashMap<String,Object>();
	      m.put("modelId", modelId);
	      m.put("tenantId", user.getFzxbm());
	      HttpResult hr = HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort() + "/BPM/manage/modeleditorjson_query.service", m, objectMapper.writeValueAsString(jsonObject));
	      JsonNode data = this.objectMapper.readTree(hr.getBody());
	      ObjectMapper objectMapper = new ObjectMapper();
	      modelNode = objectMapper.createObjectNode();
	      modelNode.put("name", data.get("name").asText());
	      modelNode.put("modelId", data.get("id").asText());
	      modelNode.put("category", data.get("category").asText());
	      modelNode.put("revision", data.get("revision").asText());
	      modelNode.put("description", data.get("description").asText());
	      modelNode.put("isDeploy", data.get("deployed").asText());
	      ObjectNode editorJsonNode = (ObjectNode)objectMapper.readTree(data.get("model").asText());
	      modelNode.put("model", editorJsonNode);
	    }
	    catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
	      throw new RuntimeException("Error while loading stencil set", e);
	    }

	    return modelNode;
	  }
	  
	  /**
	   * 流程模型保存
	   * @param modelId
	   * @param values
	   */
	  @RequestMapping(value="/model/{modelId}/save", method = RequestMethod.POST, produces={"application/json"})
	  public void saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {
	    try {
			String userId = "";
			if(modelId.indexOf(",")>0){
				String[] modelAndUserId = modelId.split(",");
				modelId=modelAndUserId[0];
				userId=modelAndUserId[1];
			}
			SysUser user = UserUtil.getCurrentUser();
			if(user==null){
				user = userRepository.findById(Long.parseLong(userId));
			}
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
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("modelId",modelId);
			m.put("name", values.getFirst("name"));
			m.put("description", values.getFirst("description"));
			m.put("jsonXml", values.getFirst("json_xml"));
			m.put("svgXml", values.getFirst("svg_xml"));
			JsonNode data = this.objectMapper.readTree(values.getFirst("json_xml"));
			String category = data.get("properties").get("process_namespace").asText();
			m.put("category",category);
			HttpResult hr =HttpService.doPost("http://" + this.config.getAddress() + ":" + this.config.getPort() +"/BPM/manage/processmodel_save.service",m, objectMapper.writeValueAsString(jsonObject));
	    } catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
	    	throw new RuntimeException("Error while loading stencil set", e);
	    }
	  }
	  
	  @RequestMapping(value={"/editor/stencilset"}, method={org.springframework.web.bind.annotation.RequestMethod.GET}, produces={"application/json;charset=utf-8"})
	  @ResponseBody
	  public String getStencilset() {
	  	InputStream stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
	    try {
	      return IOUtils.toString(stencilsetStream, "utf-8");
	    } catch (Exception e) {
			log.error("运行异常", e);
			log.error(e.getMessage());
	      throw new RuntimeException("Error while loading stencil set", e);
	    }
	  }
}
