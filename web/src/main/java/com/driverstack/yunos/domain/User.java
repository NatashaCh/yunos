package com.driverstack.yunos.domain;

import javax.persistence.Column;
import javax.persistence.Id;

@javax.persistence.Entity
public class User {
	@Id
	private String id;
	@Column
	private String passwordHash;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
