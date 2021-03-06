package com.driverstack.yunos.driver.config;

import com.driverstack.yunos.driver.config.annotation.Configure;
import com.driverstack.yunos.driver.config.annotation.Item;

@Configure(resourceFile = "config/sampleConfig", supportedlocales = { "zh_CN" })
public class SampleConfig {
	@Item
	private String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
