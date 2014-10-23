package com.driverstack.yunos.domain.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.driverstack.yunos.domain.Device;
import com.driverstack.yunos.domain.User;

/**
 * it is a entity.
 * 
 * @author jack
 * 
 */
@javax.persistence.Entity
public class Token {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column
	private String password;
	
	@JoinColumn(name = "userId")
	@ManyToOne
	private User owner;

	@Column
	private Date createTime;

	@Column
	private Date expireTime;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "token")
	@MapKey(name = "device")
	Map<Device, TokenDeviceAuthorization> deviceAuthorizations = new HashMap<Device, TokenDeviceAuthorization>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "token")
	@MapKey(name = "user")
	Map<User, TokenUserAuthorization> userAuthorizations = new HashMap<User, TokenUserAuthorization>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Map<Device, TokenDeviceAuthorization> getDeviceAuthorizations() {
		return deviceAuthorizations;
	}

	public void setDeviceAuthorizations(
			Map<Device, TokenDeviceAuthorization> deviceAuthorizations) {
		this.deviceAuthorizations = deviceAuthorizations;
	}

	public Map<User, TokenUserAuthorization> getUserAuthorizations() {
		return userAuthorizations;
	}

	public void setUserAuthorizations(
			Map<User, TokenUserAuthorization> userAuthorizations) {
		this.userAuthorizations = userAuthorizations;
	}

	public void addAuthorization(TokenDeviceAuthorization a) {
		deviceAuthorizations.put(a.getDevice(), a);
	}

	public void addAuthorization(TokenUserAuthorization a) {
		userAuthorizations.put(a.getUser(), a);
	}
}
