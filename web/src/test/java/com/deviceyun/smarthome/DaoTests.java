package com.deviceyun.smarthome;

import static junit.framework.Assert.assertNotNull;

import javax.transaction.Transactional;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.deviceyun.smarthome.dao.mybatisMapper.DeviceMapper;
import com.deviceyun.smarthome.dao.mybatisMapper.UserMapper;
import com.deviceyun.smarthome.domain.Device;
import com.deviceyun.smarthome.domain.User;

@ContextConfiguration(locations = "classpath:/com/deviceyun/smarthome/DaoTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DaoTests {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DeviceMapper deviceMapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void testMybatisMappers() throws Exception {
		assertNotNull(userMapper);
		User user = userMapper.findUserByFirstName("jack");
		Assert.assertNotNull(user);

		assertNotNull(deviceMapper);
		Device device = deviceMapper
				.getDevice("2cabad60-011f-11e4-9191-0800200c9a66");
		Assert.assertNotNull(device);

	}

	@Test
	public void testHibernate() throws Exception {
		assertNotNull(sessionFactory);

		org.hibernate.Query query = sessionFactory.getCurrentSession()
				.createQuery("from User u where u.firstName = :firstName");
		query.setString("firstName", "jack");
		User user = (User) query.uniqueResult();
		Assert.assertNotNull(user);
	}
}
