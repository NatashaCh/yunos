package com.driverstack.yunos.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.driverstack.yunos.domain.Driver;
import com.driverstack.yunos.domain.DriverConfigurationDefinitionItem;
import com.driverstack.yunos.domain.Model;
import com.driverstack.yunos.domain.DeviceClass;


public interface DriverService {
	/**
	 * developer upload driver.jar file. validate it. if it is ok, save it to
	 * file system and create a record in DB.
	 * 
	 * @param driverJarFile
	 */
	Serializable upload(InputStream in);

	Driver get(Serializable id);
	/**
	 * find drivers
	 * @param developerName
	 * @param driverName
	 * @param version
	 * @return
	 */
	List<Driver> find(String developerName, String driverName, String version);
	
	void delete(Serializable id);
	
	List<Driver> findAvailableDrivers(Model model);
	
}
