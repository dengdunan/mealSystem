package com.mm.mealapp.api;

/**
 * ����ʵ����
 */
public class orders {
	private int id;//idֵ
	private int userid;//�û�idֵ
	private int dishesid;//�����Ʒidֵ
	private String username;//�û���
	private String seat;//��λ

	private double price;//�۸�
	private double amount;//����
	private String createtime;//����ʱ��

	private String img_url;//ͼƬ��ַ
	private int status;//״ֵ̬

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

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
