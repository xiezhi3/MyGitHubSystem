package com.wn.entity;

public class Users {
	private int id;
	private String username;
	private String password;
	private String token;
	
	public Users() {
	}
	public Users(int id, String username, String password, String token) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.token = token;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
