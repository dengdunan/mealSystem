package com.bean;

/**
 * 用户实体类
 * @author dda
 *
 */
public class users {
	//id值
	private int id;
	//登录的id值
	private String loginid;
	//密码
	private String passwords;
	//名字
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getPasswords() {
		return passwords;
	}
	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

	
}
