package com.bean;

/**
 * 管理员实体类
 */
public class admins {
	//id值
	private int id;	
	//用户id
	private String loginid;
	//密码
	private String passwords;
	
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
}
