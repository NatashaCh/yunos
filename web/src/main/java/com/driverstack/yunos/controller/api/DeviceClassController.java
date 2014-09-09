package com.driverstack.yunos.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driverstack.yunos.remote.vo.DeviceClass;
import com.driverstack.yunos.remote.vo.HardwareType;
import com.driverstack.yunos.remote.vo.Model;
import com.driverstack.yunos.service.RemoteService;

/**
 * Restful API for remote service
 * 
 * @author jackding
 * 
 */
@RestController
@RequestMapping("/api/1.0/device_classes")
// @Secured("ROLE_USER")
public class DeviceClassController {

	@Autowired
	private RemoteService remoteService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<DeviceClass> getModels(@RequestParam("locale")String locale) {
		return remoteService.getDeviceClasses(locale);
	}
	
	
}