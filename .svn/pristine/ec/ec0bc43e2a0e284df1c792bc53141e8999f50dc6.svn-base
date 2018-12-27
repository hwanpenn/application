package com.shineyue.bpm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class BpmConfigUtil {

	@Value("${shineyue.bpm.address}")
	private String address;
	
	@Value("${shineyue.bpm.port}")
	private String port;
	
	@Value("${shineyue.bpm.httpclient.maxTotal}")
	private int maxTotal;
	
	@Value("${shineyue.bpm.httpclient.defaultMaxPerRoute}")
	private int defaultMaxPerRoute;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

}
