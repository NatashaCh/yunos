package com.driverstack.yunos;

import static junit.framework.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.driverstack.yunos.remote.vo.ConfigurationItem;
import com.driverstack.yunos.service.RemoteService;

@ContextConfiguration(locations = "classpath:/com/driverstack/yunos/ServiceTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RemoteServiceTests {

	@Autowired
	private RemoteService remoteService;

	@Test
	public void testAll() throws Exception {
		assertNotNull(remoteService);

		List<com.driverstack.yunos.remote.vo.Device> devices = remoteService
				.listDevice("jackding");

		assertNotNull(devices);

		Assert.assertFalse(devices.isEmpty());

	}

	@Test
	public void testDeviceConfiguration() throws Exception {

		String deviceId = "cb170afb-087f-11e4-b721-08002785c3ec";
		List<ConfigurationItem> items = remoteService
				.getDeviceConfiguration(deviceId);
		
		Map<String, ConfigurationItem> map = new HashMap<String, ConfigurationItem>();
		for(ConfigurationItem item : items)
			map.put(item.getName(), item);

		ConfigurationItem portItem = map.get("port");
		
		Assert.assertEquals("port", portItem.getName());
		Assert.assertEquals("588", portItem.getValue());

	}

}
