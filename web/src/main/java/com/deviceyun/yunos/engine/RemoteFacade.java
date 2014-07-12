package com.deviceyun.yunos.engine;

import java.util.Map;

import com.deviceyun.yunos.DeviceManagerImpl;
import com.deviceyun.yunos.api.device.FunctionalDevice;

public class RemoteFacade {
	public static final String API_KEY = "apiKey";
	public static final String DEVICE_ID = "deviceId";
	public static final String OPERATION = "operation";

	private DeviceManagerImpl deviceManager;

	public RemoteFacade(DeviceManagerImpl deviceManager) {
		this.deviceManager = deviceManager;
	}

	public Object urlApi(Map<String, String> parameters) {
		// String apiKey = parameters.get(API_KEY);

		String deviceId = parameters.get(DEVICE_ID);
		String operation = parameters.get(OPERATION);

		FunctionalDevice device = deviceManager.getDevice(deviceId);
		// perform operation on device
		//Object ret = device.invoke(operation, null);
		return null;
	}
}
