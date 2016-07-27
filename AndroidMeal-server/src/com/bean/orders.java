package com.bean;

/**
 * 订单实体类
 * @author dda
 *
 */
public class orders {
	//id值
	private int id;
	//用户id
	private int userid;
	//点餐的id
	private int dishesid;
	//用户名
	private String username;
	//座位
	private String seat;
	//价格
	private double price;
	//数量
	private double amount;
	//订单生成时间
	private String createtime;
	//订单状态
	private int status;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getDishesid() {
		return dishesid;
	}

	public void setDishesid(int dishesid) {
		this.dishesid = dishesid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
