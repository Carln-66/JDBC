package com.carl1.statementdemo;

public class User {

	private String user;
	private int password;

	public User() {
	}

	public User(String user, int password) {
		super();
		this.user = user;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [user=" + user + ", password=" + password + "]";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

}
