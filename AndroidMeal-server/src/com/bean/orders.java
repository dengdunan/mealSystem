package com.bean;

/**
 * ����ʵ����
 * @author dda
 *
 */
public class orders {
	//idֵ
	private int id;
	//�û�id
	private int userid;
	//��͵�id
	private int dishesid;
	//�û���
	private String username;
	//��λ
	private String seat;
	//�۸�
	private double price;
	//����
	private double amount;
	//��������ʱ��
	private String createtime;
	//����״̬
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
