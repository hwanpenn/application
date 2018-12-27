package com.shineyue.bpm.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.shineyue.bpm.http.ClientEvictExpiredConnections.IdleConnectionEvictor;
import com.shineyue.bpm.util.BpmConfigUtil;
import com.shineyue.bpm.util.SpringUtil;;

public class HttpService {
	private static final Log log = LogFactory.getLog(HttpService.class);
	private static PoolingHttpClientConnectionManager cm = null;
	private static IdleConnectionEvictor e = null;
	private static BpmConfigUtil config=SpringUtil.getBean(BpmConfigUtil.class);
	static {
		cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(config.getMaxTotal());
        cm.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
		e=new IdleConnectionEvictor(cm);
		e.start();
	}
	
	  public static HttpResult doPost(String url, Map<String, Object> map, String jsonObject)
			    throws Exception
			  {
		  log.info("-----------访问地址-------"+url);
			    CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
			    HttpPost httpPost = new HttpPost(url);
			    CloseableHttpResponse response = null;
			    try
			    {
					if (map != null) {
						List<NameValuePair> list = new ArrayList<NameValuePair>();
						// 遍历map，组装成form表单
						for (Map.Entry<String, Object> entry : map.entrySet()) {
							list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
							}
						// 构造一个form表单式的实体
						UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
						// 将请求实体设置到httpPost对象中
						httpPost.setEntity(urlEncodedFormEntity);
					}
			      httpPost.setHeader("usr", jsonObject);

			      response = httpClient.execute(httpPost);
			      int ret = response.getStatusLine().getStatusCode();
			      if (ret != 200) {
			        throwHttpxception("没返回正确代码,代码为：" + ret, null);
			      }
			      return new HttpResult(Integer.valueOf(ret), EntityUtils.toString(response.getEntity(), "UTF-8"));
			    }catch(Exception e){
			    	e.printStackTrace();
					log.error("运行异常", e);
					log.error(e.getMessage());
					return new HttpResult(Integer.valueOf(404), "请求异常");
				} finally {
			      if (response != null)
			        response.close();
			    }
			  }

			  private static void throwHttpxception(String errorMessage, Throwable e) throws HttpException {
			    if (e != null) {
			      throw new HttpException(errorMessage, e);
			    }
			    throw new HttpException(errorMessage);
			  }
}
