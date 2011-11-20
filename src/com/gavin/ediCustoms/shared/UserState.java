package com.gavin.ediCustoms.shared;

import java.io.Serializable;

public class UserState implements Serializable{
	private UserType userType;
	private Long id;
	private String name;
	
	public UserState(){}
	
	public UserState(UserType userType, Long id) {
		this.userType=userType;
		this.id=id;
	}

	public enum UserType {
		USER, ADMIN
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
