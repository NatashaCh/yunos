package com.driverstack.yunos.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.driverstack.yunos.api.ApiUtils;
import com.driverstack.yunos.api.Parameter;
import com.driverstack.yunos.api.ParameterType;
import com.driverstack.yunos.core.DeviceManager;
import com.driverstack.yunos.core.exception.DriverNotSetException;
import com.driverstack.yunos.dao.ApplicationDao;
import com.driverstack.yunos.dao.DeviceDao;
import com.driverstack.yunos.dao.GenericDao;
import com.driverstack.yunos.domain.DeviceClass;
import com.driverstack.yunos.domain.Model;
import com.driverstack.yunos.domain.Token;
import com.driverstack.yunos.domain.Vendor;
import com.driverstack.yunos.driver.device.FunctionalDevice;
import com.driverstack.yunos.driver.device.PhysicalDevice;
import com.driverstack.yunos.remote.vo.ConfigurationItem;
import com.driverstack.yunos.remote.vo.Device;
import com.driverstack.yunos.remote.vo.Driver;
import com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem;

/**
 * remote service for client, such as mobile app
 * 
 * @author jackding
 * 
 */
@Component
public class RemoteServiceImpl implements RemoteService {
	@Autowired
	private GenericDao genericDao;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private DeviceClassService deviceClassService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private DeviceManager deviceManager;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private FunctionalDeviceService functionalDeviceService;

	@Autowired
	private DriverService driverService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ApplicationDao applicationDao;

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Override
	public String login(String userId, String password) {
		Token t = authenticationService.login(userId, password);

		if (t != null)
			return t.getId();
		else
			return null;
	}

	@Override
	public List<Device> queryUserDevices(String userId) {
		List<com.driverstack.yunos.domain.Device> domainDeviceList = deviceService
				.listByUserId(userId);
		List<Device> remoteDeviceList = new ArrayList<Device>();

		for (com.driverstack.yunos.domain.Device d : domainDeviceList) {
			Device remoteDevice = createRemoteDevice(d);
			remoteDeviceList.add(remoteDevice);
		}
		return remoteDeviceList;
	}

	protected Device createRemoteDevice(
			com.driverstack.yunos.domain.Device domainDevice) {
		Device remoteDevice = new Device();
		com.driverstack.yunos.remote.vo.HardwareType hardwareType = new com.driverstack.yunos.remote.vo.HardwareType();
		hardwareType.setVendor(domainDevice.getModel().getVendor()
				.getShortName());
		hardwareType.setModel(domainDevice.getModel().getName());

		BeanUtils.copyProperties(domainDevice, remoteDevice, "model");

		remoteDevice.setHardwareType(hardwareType);

		remoteDevice.setVendorId(domainDevice.getModel().getVendor().getId());
		remoteDevice.setDeviceClassId(domainDevice.getModel().getDeviceClass()
				.getId());
		remoteDevice.setModelId(domainDevice.getModel().getId());
		if (domainDevice.getDriver() != null)
			remoteDevice.setDriverId(domainDevice.getDriver().getId());

		remoteDevice.setDefaultFunctionalDeviceIndex(domainDevice
				.getDefaultFunctionalDeviceIndex());

		return remoteDevice;
	}

	@Override
	public Device getDevice(String deviceId) {
		com.driverstack.yunos.domain.Device domainDevice = deviceDao
				.get(deviceId);

		Device remoteDevice = createRemoteDevice(domainDevice);

		return remoteDevice;
	}

	@Override
	public void addDevice(String userId, Device device) {
		com.driverstack.yunos.domain.Device domainDevice = new com.driverstack.yunos.domain.Device();
		BeanUtils.copyProperties(device, domainDevice);

		deviceService.save(domainDevice);
	}

	@Override
	public void updateDevice(Device device) {

		com.driverstack.yunos.domain.Device domainDevice = (com.driverstack.yunos.domain.Device) genericDao
				.get(com.driverstack.yunos.domain.Device.class, device.getId());

		// update fields:modelId, deviceCLassId,name.location,description
		if (!device.getModelId().equals(domainDevice.getModel().getId())) {
			Model domainModel = (Model) genericDao.get(Model.class,
					device.getModelId());
			domainDevice.setModel(domainModel);
		}

		domainDevice.setName(device.getName());
		domainDevice.setLocation(device.getLocation());
		domainDevice.setDescription(device.getDescription());

		com.driverstack.yunos.domain.Driver driver = null;
		if (device.getDriverId() != null)
			driver = (com.driverstack.yunos.domain.Driver) genericDao.get(
					com.driverstack.yunos.domain.Driver.class,
					device.getDriverId());

		domainDevice.setDriver(driver);
		domainDevice.setDefaultFunctionalDeviceIndex(device
				.getDefaultFunctionalDeviceIndex());

		deviceService.update(domainDevice);
	}

	@Override
	public void removeDevice(String deviceId) {
		deviceService.remove(deviceId);
	}

	@Override
	public List<ConfigurationItem> getDeviceInitialConfiguration(
			String deviceId, String driverId) {
		com.driverstack.yunos.domain.Device domainDevice = (com.driverstack.yunos.domain.Device) genericDao
				.load(com.driverstack.yunos.domain.Device.class, deviceId);
		com.driverstack.yunos.domain.Driver domainDriver = (com.driverstack.yunos.domain.Driver) genericDao
				.load(com.driverstack.yunos.domain.Driver.class, driverId);

		List<com.driverstack.yunos.domain.ConfigurationItem> domainItems = deviceService
				.createConfiguration(domainDevice, domainDriver);

		List<ConfigurationItem> remoteItems = new ArrayList<ConfigurationItem>();
		for (com.driverstack.yunos.domain.ConfigurationItem domainItem : domainItems) {
			remoteItems.add(domainItem.toRemoteVO());
		}

		return remoteItems;
	}

	@Override
	public List<ConfigurationItem> getDeviceConfiguration(String deviceId) {
		com.driverstack.yunos.domain.Device domainDevice = (com.driverstack.yunos.domain.Device) genericDao
				.get(com.driverstack.yunos.domain.Device.class, deviceId);

		List<ConfigurationItem> list = new ArrayList<ConfigurationItem>();
		Map<String, com.driverstack.yunos.domain.ConfigurationItem> map = domainDevice
				.getUserConfigurationItems();
		for (com.driverstack.yunos.domain.ConfigurationItem domainItem : map
				.values()) {
			list.add(domainItem.toRemoteVO());
		}

		return list;
	}

	@Override
	public void updateDeviceConfiguration(String deviceId,
			List<ConfigurationItem> configuration) {

		com.driverstack.yunos.domain.Device domainDevice = deviceDao
				.get(deviceId);
		Map<String, com.driverstack.yunos.domain.ConfigurationItem> map = domainDevice
				.getUserConfigurationItems();
		map.clear();

		for (ConfigurationItem remoteItem : configuration) {
			com.driverstack.yunos.domain.ConfigurationItem domainItem = new com.driverstack.yunos.domain.ConfigurationItem(
					remoteItem);
			domainDevice.addConfigurationItem(domainItem);
		}

		genericDao.saveOrUpdate(domainDevice);
	}

	@Override
	public Object operateDevice(String deviceId, int functionalDeviceIndex,
			String operation, Map<String, String> parameters) {

		com.driverstack.yunos.domain.Device domainDevice = deviceDao
				.get(deviceId);
		PhysicalDevice physicalDevice = deviceManager
				.getPhysicalDeviceObject(domainDevice);

		FunctionalDevice functionalDevice = physicalDevice
				.getFunctionDevice(functionalDeviceIndex);
		// perform operation on device
		// Object ret = device.invoke(operation, null);
		Method method = ApiUtils.getMethod(functionalDevice, operation);

		List<Parameter> paramList = ApiUtils.getParameterInfo(method);

		int paramSize = paramList.size();
		Object[] parameterObjects = new Object[paramSize];

		for (int i = 0; i < paramSize; i++) {
			Parameter p = paramList.get(i);
			String value = parameters.get(p.getName());

			ParameterType pt = ParameterType.getType(p.getType());
			Object objValue = pt.fromString(value);

			parameterObjects[i] = objValue;
		}

		try {
			return method.invoke(functionalDevice, parameterObjects);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<com.driverstack.yunos.remote.vo.DeviceClass> getDeviceClasses(
			String vendorId, String locale) {

		List<DeviceClass> domainDeviceClasses;
		if (vendorId != null) {
			Vendor vendor = (Vendor) genericDao.load(Vendor.class, vendorId);

			domainDeviceClasses = deviceClassService.find(vendor, locale);
		} else
			domainDeviceClasses = genericDao.getAll(DeviceClass.class);

		List<com.driverstack.yunos.remote.vo.DeviceClass> remoteObjects = new ArrayList<com.driverstack.yunos.remote.vo.DeviceClass>();
		for (DeviceClass d : domainDeviceClasses) {
			com.driverstack.yunos.remote.vo.DeviceClass r = new com.driverstack.yunos.remote.vo.DeviceClass();
			BeanUtils.copyProperties(d.get(locale), r);

			remoteObjects.add(r);
		}

		return remoteObjects;
	}

	@Override
	public List<com.driverstack.yunos.remote.vo.Vendor> getAllVendors(
			String locale) {
		Locale aLocale = Locale.forLanguageTag(locale.replaceAll("_", "-"));

		List<com.driverstack.yunos.domain.Vendor> domainVendors = genericDao
				.getAll(Vendor.class);

		List<com.driverstack.yunos.remote.vo.Vendor> remoteVendors = new ArrayList<com.driverstack.yunos.remote.vo.Vendor>();
		for (com.driverstack.yunos.domain.Vendor d : domainVendors) {
			com.driverstack.yunos.remote.vo.Vendor r = new com.driverstack.yunos.remote.vo.Vendor();
			BeanUtils.copyProperties(d.get(locale), r);

			remoteVendors.add(r);
		}

		return remoteVendors;
	}

	@Override
	public List<com.driverstack.yunos.remote.vo.Model> getModels(
			String vendorId, String deviceClassId, String locale) {

		com.driverstack.yunos.domain.Vendor vendor = (com.driverstack.yunos.domain.Vendor) genericDao
				.load(com.driverstack.yunos.domain.Vendor.class, vendorId);

		com.driverstack.yunos.domain.DeviceClass deviceClass = null;
		if (deviceClassId != null)
			deviceClass = (com.driverstack.yunos.domain.DeviceClass) genericDao
					.load(com.driverstack.yunos.domain.DeviceClass.class,
							deviceClassId);

		List<com.driverstack.yunos.domain.Model> domainModels = modelService
				.getModels(vendor, deviceClass, locale);

		List<com.driverstack.yunos.remote.vo.Model> remoteModels = new ArrayList<com.driverstack.yunos.remote.vo.Model>();
		for (com.driverstack.yunos.domain.Model dm : domainModels) {
			com.driverstack.yunos.remote.vo.Model rm = new com.driverstack.yunos.remote.vo.Model();
			BeanUtils.copyProperties(dm.get(locale), rm);

			remoteModels.add(rm);
		}

		return remoteModels;
	}

	@Override
	public List<Driver> getAvailableDrivers(String modelId) {

		com.driverstack.yunos.domain.Model model = (com.driverstack.yunos.domain.Model) genericDao
				.load(com.driverstack.yunos.domain.Model.class, modelId);
		List<com.driverstack.yunos.domain.Driver> domainDrivers = driverService
				.findAvailableDrivers(model);

		List<Driver> remoteObjects = new ArrayList<Driver>();
		for (com.driverstack.yunos.domain.Driver domainObject : domainDrivers) {
			Driver driver = new Driver(domainObject.getId(),
					domainObject.getName(), domainObject.getVersion(),
					domainObject.getDeveloperName());

			remoteObjects.add(driver);
		}

		return remoteObjects;
	}

	@Override
	public List<DriverConfigurationDefinitionItem> getDriverConfigurationDefinitionItems(
			String driverId, String locale) {

		com.driverstack.yunos.domain.Driver driver = (com.driverstack.yunos.domain.Driver) genericDao
				.load(com.driverstack.yunos.domain.Driver.class, driverId);

		List<com.driverstack.yunos.domain.DriverConfigurationDefinitionItem> domainItems = driver
				.getConfigurationDefinition().getItems();

		List<com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem> remoteItems = new ArrayList<com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem>();
		for (com.driverstack.yunos.domain.DriverConfigurationDefinitionItem di : domainItems) {
			com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem ri = new com.driverstack.yunos.remote.vo.DriverConfigurationDefinitionItem();

			BeanUtils.copyProperties(di.get(locale), ri);

			remoteItems.add(ri);
		}

		return remoteItems;

	}

	@Override
	public List<com.driverstack.yunos.remote.vo.FunctionalDevice> getFunctionalDevices(
			String deviceId, String locale) {
		List<com.driverstack.yunos.remote.vo.FunctionalDevice> voFunctionalDeviceList = new ArrayList<com.driverstack.yunos.remote.vo.FunctionalDevice>();
		/**
		 * the functional device list is runtime value.
		 */

		com.driverstack.yunos.domain.Device domainDevice = deviceDao
				.get(deviceId);

		if (domainDevice.getDriver() != null) {
			PhysicalDevice pd = deviceManager
					.getPhysicalDeviceObject(domainDevice);

			// runtime FD to domain object

			for (FunctionalDevice fd : pd.getFunctionDevices()) {

				String className = fd.getClass().getInterfaces()[0]
						.getCanonicalName();
				com.driverstack.yunos.domain.FunctionalDevice domainFD = functionalDeviceService
						.getByClassName(className);

				domainFD.setLocale(locale);

				com.driverstack.yunos.remote.vo.FunctionalDevice voFD = new com.driverstack.yunos.remote.vo.FunctionalDevice(
						deviceId, domainFD.getOrganization().getCodeName(),
						domainFD.getArtifactId(), domainFD.getOrganization()
								.getShortName(), domainFD.getDisplayName());

				voFunctionalDeviceList.add(voFD);
			}

			// set index
			for (int i = 0; i < voFunctionalDeviceList.size(); i++)
				voFunctionalDeviceList.get(i).setIndex(i);
		}

		return voFunctionalDeviceList;
	}

	@Override
	public List<com.driverstack.yunos.remote.vo.FunctionalDevice> queryUserFunctionalDevices(
			String userId, String organizationId, String artifactId,
			String locale) {

		// 1. query device(physical) from DB.
		List<com.driverstack.yunos.domain.Device> domainDeviceList = deviceService
				.listByUserId(userId);

		// 2. collect functional device in memory
		List<com.driverstack.yunos.remote.vo.FunctionalDevice> voFunctionalDeviceList = new ArrayList<com.driverstack.yunos.remote.vo.FunctionalDevice>();

		for (com.driverstack.yunos.domain.Device dd : domainDeviceList) {

			if (dd.getDriver() == null)
				continue;

			PhysicalDevice pd = deviceManager.getPhysicalDeviceObject(dd);

			List<FunctionalDevice> runtimeFDList = pd.getFunctionDevices();
			for (FunctionalDevice rfd : runtimeFDList) {
				String className = rfd.getClass().getInterfaces()[0]
						.getCanonicalName();
				com.driverstack.yunos.domain.FunctionalDevice domainFD = functionalDeviceService
						.getByClassName(className);

				domainFD.setLocale(locale);

				String deviceId = dd.getId();
				com.driverstack.yunos.remote.vo.FunctionalDevice voFD = new com.driverstack.yunos.remote.vo.FunctionalDevice(
						deviceId, domainFD.getOrganization().getCodeName(),
						domainFD.getArtifactId(), domainFD.getOrganization()
								.getShortName(), domainFD.getDisplayName());

				// filter by orgId and artifactId
				if (voFD.getOrganizationId().equals(organizationId)
						&& voFD.getArtifactId().equals(artifactId))
					voFunctionalDeviceList.add(voFD);
			}
		}

		return voFunctionalDeviceList;
	}
}
