package com.driverstack.yunos.dao.mybatisMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.driverstack.yunos.domain.User;

public interface UserMapper {
	@Select("SELECT * FROM User WHERE id = #{id}")
	User getUser(@Param("id") String id);

	User findUserByFirstName(String firstName);

	User findUserByEmail(String email);
}
