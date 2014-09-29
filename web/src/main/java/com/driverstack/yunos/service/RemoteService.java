package com.driverstack.yunos.service;

import java.util.List;
import java.util.Map;

import com.driverstack.yunos.remote.vo.ConfigurationItem;
import com.driverstack.yunos.remote.vo.Device;
import com.driverstack.yunos.remote.vo.Driver;
import com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem;
import com.driverstack.yunos.remote.vo.FunctionalDevice;

public interface RemoteService {
	/**
	 * login to get token, which will be used for later calling API
	 * 
	 * @param userId
	 * @param password
	 * @return token
	 */
	String login(String userId, String password);

	/**
	 * list devices of a user.
	 * 
	 * @param userId
	 * @return
	 */
	List<Device> queryUserDevices(String userId);

	Device getDevice(String deviceId);

	/**
	 * user add a new device. it returns a device ID;
	 * 
	 * @param userId
	 * @param device
	 */
	void addDevice(String userId, Device device);

	/**
	 * can not delete others device
	 * 
	 * @param deviceId
	 */
	void removeDevice(String deviceId);

	/**
	 * get initial device configuration. note that the initial configuration are
	 * not bound to the device until you save them.
	 * 
	 * @param deviceId
	 * @return
	 */
	List<ConfigurationItem> getDeviceInitialConfiguration(String deviceId,
			String driverId);

	/**
	 * get device configuration.
	 * 
	 * @param deviceId
	 * @return
	 */
	List<ConfigurationItem> getDeviceConfiguration(String deviceId);

	/**
	 * update user configuration
	 * 
	 * @param deviceId
	 * @param config
	 */
	void updateDeviceConfiguration(String deviceId,
			List<ConfigurationItem> configuration);

	/**
	 * operate a device. the functional device API is varying.
	 * 
	 * @param deviceId
	 * @param functionalDeviceIndex
	 * @param operationName
	 * @param parameters
	 * @return
	 */
	Object operateDevice(String deviceId, int functionalDeviceIndex,
			String operationName, Map<String, String> parameters);

	/**
	 * 
	 * @param locale
	 *            such as en_US
	 * @return
	 */
	List<com.driverstack.yunos.remote.vo.Vendor> getAllVendors(String locale);

	List<com.driverstack.yunos.remote.vo.DeviceClass> getDeviceClasses(
			String vendorId, String locale);

	List<com.driverstack.yunos.remote.vo.Model> getModels(String vendorId,
			String deviceClassId, String locale);

	List<Driver> getAvailableDrivers(String modelId);

	List<DriverConfigurationDefinitionItem> getDriverConfigurationDefinitionItems(
			String driverId, String locale);

	void updateDevice(Device device);

	List<FunctionalDevice> getFunctionalDevices(String deviceId, String locale);

	List<FunctionalDevice> queryUserFunctionalDevices(String userId,
			String organizationId, String artifactId, String locale);
	

}