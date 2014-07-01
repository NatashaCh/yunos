package com.deviceyun.smarthome.api.driver;

import java.util.Date;
import java.util.List;

import com.deviceyun.smarthome.api.device.Device;
import com.deviceyun.smarthome.api.device.DeviceInfo;

/**
 * a driver submit by developers.
 * 
 * @author jackding
 * 
 */
public interface Driver {

	String getAuthor();

	Date getReleaseDate();

	String getSdkVersion();

	List<SupportedDevice> getSupportedDevices();

	List<ConfigItem> getConfigItems();

	Device createDevice(DeviceInfo info);
}
