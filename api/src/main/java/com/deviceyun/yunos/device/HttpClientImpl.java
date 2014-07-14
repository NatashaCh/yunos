package com.deviceyun.yunos.device;

import java.util.Map;

public class HttpClientImpl implements HttpClient {

	@Override
	public String get(Map<String, String> parameters) {

		System.out.print("HttpClientImpl:");
		for (String name : parameters.keySet()) {
			String value = parameters.get(name);
			System.out.print(name + "=" + value);
			System.out.print("&");
		}

		return "OK";
	}

}
