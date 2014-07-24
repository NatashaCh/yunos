package com.deviceyun.yunos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deviceyun.yunos.service.RemoteServiceImpl;
/**
 * Restful API for remote service
 * 
 * @author jackding
 *
 */
@RestController
@RequestMapping("/devices")
public class DeviceManagerController {

	@Autowired
	private RemoteServiceImpl remoteService;

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public String login(String userId, String password) {

		// return remoteFacade.getDevicesByUser(userId);
		return null;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public List<com.deviceyun.yunos.remote.vo.Device> listByUserId(
			@PathVariable String userId) {
		return remoteService.getDevicesByUser(userId);

	}
}
