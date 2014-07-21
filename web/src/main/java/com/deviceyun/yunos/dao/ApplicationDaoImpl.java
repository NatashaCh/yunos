package com.deviceyun.yunos.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deviceyun.yunos.domain.Application;

@Component
public class ApplicationDaoImpl implements ApplicationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Application get(String id) {
		Session session = sessionFactory.getCurrentSession();
		return (Application) session.get(Application.class, id);
	}
}
