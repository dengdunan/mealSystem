package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bean.dishes;
import com.bean.orders;
import com.bean.seats;
import com.bean.types;
import com.bean.users;

/**
 * 服务Servlet,只想登录的严重，订单的取消和座位的预定等等功能
 * @author nuo
 *
 */
@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class ServletService extends HttpServlet {

	//格式化日期
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	java.text.SimpleDateFormat formatdate = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");
	java.util.Date currentTime = new java.util.Date();// 得到当前系统时间

	private Session session = null;
	private HttpServletRequest request;

	public ServletService() {
		super();
		session = HibernateSessionFactory.getSession();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	/**
	 * 客户请求使用Get方式进行网络访问
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//初始化会话
		this.request = request;
		session = HibernateSessionFactory.getSession();
		session.flush();
		session.clear();
		//色或者请求的编码格式和响应的编码格式
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//获得Action值参数的信息
		String action = request.getParameter("Action");
		System.out.println(action);
		String write = "";
		String sqlString = "";
		System.out.print(action);
		// 登录验证
		if (action.equals("login")) {
			write = login();

		}
		//获取一行数据
		if (action.equals("getOneRow")) {
			write = getOneRow();
		}
		//执行删除操作
		if (action.equals("Del")) {
			write = Del();
		}
		//取消订单操作
		if (action.equals("cancelOrders")) {
			write = cancelOrders();
		}

		// 管理员登录验证
		if (action.equals("adminlogin")) {
			write = adminlogin();
		}
		//获得点餐列表
		if (action.equals("getdisheslist")) {
			write = getdisheslist();

		}
		//获得我的订单列表
		if (action.equals("getmyorderslist")) {
			write = getmyorderslist();
		}
		//更改座位状态
		if (action.equals("ChangeStatus")) {
			write = changestatus();
		}
		//点餐商品详情
		if (action.equals("edit")) {
			write = edit();
		}
		//注册用户
		if (action.equals("edituser")) {
			write = edituser();
		}
		//编辑类型
		if (action.equals("edittype")) {
			write = edittype();
		}
		System.out.println(write);
		out.println(write);
		out.flush();
		out.close();
		

	}

	/**
	 * 客户请求使用Post方式进行网络访问
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//初始化会话
		session = HibernateSessionFactory.getSession();
		//回话刷新
		session.flush();
		//会话清除
		session.clear();
		this.request = request;
		//色或者请求的编码格式和响应的编码格式
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//获得Action值参数的信息
		String action = request.getParameter("Action");
		String write = "";
		// 注册
		if (action.equals("register")) {
			System.out.println(request.getParameter("id"));
			users model = new users();
			//判断id是否为空或者id是否为0
			if (request.getParameter("id") == null
					|| request.getParameter("id").equals("0")) {
				model = new users();

			} 
			//否则执行用户的查询操作
			else {
				model = (users) (session.createQuery(
						" from users where id=" + request.getParameter("id"))
						.list().get(0));
			}
			//将其存入数据实体类中，并将其上传到数据库中
			model.setLoginid(request.getParameter("loginid"));
			model.setPasswords(request.getParameter("password"));
			model.setName(getChinese(request.getParameter("name")));

			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			write = "1";
		}
		//执行更新面膜操作
		if (action.equals("updatePwd")) {
			write = updatePwd();
		}
		//提交订单操作
		if (action.equals("createorder")) {
			write = createorder();
		}
		out.println(write);
		out.flush();
		out.close();
	}

	/**
	 * 获得登录的信息
	 */
	private String login() {
		String write = "";
		//登录名
		String loginid = request.getParameter("loginid");
		//密码
		String passwords = request.getParameter("passwords");
		//查询数据库中的数据
		List<users> list = session.createQuery(
				" from users where loginid='" + loginid + "' and passwords='"
						+ passwords + "'").list();
		//如果查询到数据，则写入write中
		if (list.size() > 0) {
			write = JSONArray.fromObject(list.get(0)).toString();
		}
		return write;
	}
	
	/**
	 * 获得管理员登录信息
	 */
	private String adminlogin() {
		String write = "";
		//登录名
		String loginid = request.getParameter("loginid");
		//密码
		String passwords = request.getParameter("passwords");
		//查询数据库中的数据
		List<users> list = session.createQuery(
				" from admins where loginid='" + loginid + "' and passwords='"
						+ passwords + "'").list();
		//如果查询到数据，则设置write为1，否则为0
		if (list.size() > 0) {
			write = "1";
		} else {
			write = "0";
		}
		return write;
	}

	/**
	 * 获得我的订单信息
	 * @return
	 */
	private String getmyorderslist() {
		
		
		String write = "";
		//查询数据中的数据
		String sqlString = "select orders.status, orders.id,orders.userid,orders.username,orders.seat,orders.dishesid,orders.price,orders.amount,orders.createtime,dishes.title,dishes.img_url FROM orders INNER JOIN dishes on orders.dishesid=dishes.id ";
		sqlString += " where  userid =" + request.getParameter("userid");

		sqlString += " order by orders.id desc";
		ResultSet rs = HibernateSessionFactory.queryBySql(sqlString);
		//得到从数据库中返回的结果
		List list = HibernateSessionFactory.convertList(rs);
		//如果查询到数据，则将其使用json写入进write中
		if (list.size() > 0) {
			JSONArray json = JSONArray.fromObject(list);
			write = json.toString();
		}
		return write;
	}

	/**
	 * 获得点餐列表信息
	 * @return
	 */
	private String getdisheslist() {
		String write = "";
		//定义sql查询语句
		String sqlString = "from dishes where 1=1 ";
		//如果msg不为空
		if (request.getParameter("msg") != null) {
			//则设置sql查询语句
			sqlString += " and title like '%"
					+ getChinese(request.getParameter("msg")) + "%'";
		}
		sqlString += " order by id desc";
		//从数据库中所得到的列表信息
		List list = session.createQuery(sqlString).list();
		//如果查询到数据，则将其使用json写入进write中
		if (list.size() > 0) {
			JSONArray json = JSONArray.fromObject(list);
			write = json.toString();
		}
		return write;
	}
	
	/**
	 * 获得详情信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String edit() throws UnsupportedEncodingException {
		//得到id值
		int id = Integer.valueOf((request.getParameter("ID")));
		dishes model;
		//如果id为0，则初始化model实体类，否则从查询中查询数据
		if (id == 0) {
			model = new dishes();

		} else {
			model = (dishes) (session
					.createQuery(" from dishes where id=" + id).list().get(0));
		}
		//如果图片地址不为空，且图片大小大于0，则设置图片的路径
		if (request.getParameter("img_url") != null
				&& request.getParameter("img_url").length() > 0) {
			model.setImg_url(request.getParameter("img_url"));
		}

		//设置数据进入实体类中（将商品详情的信息存入）
		model.setIntro(getChinese(request.getParameter("intro")));
		model.setTitle(getChinese(request.getParameter("title")));
		model.setPrice(Float.valueOf(request.getParameter("price")));
		model.setAmount(Float.valueOf(request.getParameter("amount")));
		model.setTypeid(Integer.valueOf(request.getParameter("typeid")));
		model.setTypename(getChinese(request.getParameter("typename")));
		Transaction tran = session.beginTransaction();

		//如果id不为0，则更新数据，否则就保存数据
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}

	/**
	 * 获得取消订单的信息
	 * @return
	 */
	private String cancelOrders() {
		//获得id值
		int id = Integer.valueOf((request.getParameter("ID")));
		orders model;
		//从数据库查询中得到的数据
		model = (orders) (session.createQuery(" from orders where id=" + id)
				.list().get(0));
		//得到座位的值的数组
		String[] ss = model.getSeat().split(",");
		//查询到预订座位的数据，并将其保存起来
		for (int i = 0; i < ss.length; i++) {
			seats s = (seats) session
					.createQuery(" from seats where id=" + ss[i]).list().get(0);
			s.setState(0);
			session.save(s);
		}
		Transaction tran = session.beginTransaction();
		session.delete(model);
		tran.commit();
		return "1";
	}
	//修改订单状态
	private String changestatus() {
		//获得id值
		int id = Integer.valueOf((request.getParameter("ID")));
		//获得状态值
		int status= Integer.valueOf((request.getParameter("status")));
		orders model;
		//从数据库查询中得到的数据
		model = (orders) (session.createQuery(" from orders where id=" + id)
				.list().get(0));
		//设置状态值
		model.setStatus(status);
		Transaction tran = session.beginTransaction();
		//保存数据
		session.save(model);
		tran.commit();
		return "1";
	}

	//注册用户
	private String edituser() throws UnsupportedEncodingException {
		//获得id值
		int id = Integer.valueOf((request.getParameter("ID")));
		users model;
		//当id为0时，初始化users,否则从数据库中查询数据
		if (id == 0) {
			model = new users();

		} else {
			model = (users) (session.createQuery(" from users where id=" + id)
					.list().get(0));
		}
		//设置model类的信息
		model.setLoginid(getChinese(request.getParameter("loginid")));
		model.setName(getChinese(request.getParameter("name")));
		model.setPasswords(request.getParameter("passwords"));
		Transaction tran = session.beginTransaction();
		//如果id不为0，则更新数据，否则就保存数据
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}
	
	//获得类型信息
	private String edittype() throws UnsupportedEncodingException {
		//获得id值
		int id = Integer.valueOf((request.getParameter("ID")));
		types model;
		//当id为0时，初始化types,否则从数据库中查询数据
		if (id == 0) {
			model = new types();

		} else {
			model = (types) (session.createQuery(" from types where id=" + id)
					.list().get(0));
		}
		//设置model类的信息
		model.setTypename(getChinese(request.getParameter("typename")));
		
		Transaction tran = session.beginTransaction();
		//如果id不为0，则更新数据，否则就保存数据
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}
	

	// 提交订单
	private String createorder() throws UnsupportedEncodingException {
		orders model = null;
		//从数据中得到的数据
		List list = session.createQuery(
				" from orders where id=" + request.getParameter("id")).list();
		//如果列表的大小为0，则实例化orders()，且设置当前的时间，否则从数据中得到数据
		if (list.size() == 0) {
			model = new orders();
			model.setCreatetime(formatdate.format(currentTime));
		} else {
			model = (orders) list.get(0);
		}
		//从会话中查询数据，并设置订单相关的属性
		dishes dishesModel = (dishes) session
				.createQuery(
						" from dishes where id="
								+ request.getParameter("dishesid")).list()
				.get(0);
		model.setUserid(Integer.valueOf(request.getParameter("userid")));
		model.setUsername(getChinese(request.getParameter("username")));
		model.setAmount(Double.valueOf(request.getParameter("amount")));
		model.setPrice(dishesModel.getPrice());
		model.setSeat(request.getParameter("seat"));
		model.setDishesid(dishesModel.getId());
		//查询到预订座位的数据，并将其保存起来
		String[] ss = model.getSeat().split(",");
		for (int i = 0; i < ss.length; i++) {
			seats s = (seats) session
					.createQuery(" from seats where id=" + ss[i]).list().get(0);
			s.setState(1);
			session.save(s);
		}

		Transaction tran = session.beginTransaction();
		//保存model的内容到会话中
		session.save(model);
		tran.commit();
		return "1";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String updatePwd() throws UnsupportedEncodingException {
		//从数据库查询后并返回数据
		List list = session.createQuery(
				" from users where loginid='" + request.getParameter("loginid")
						+ "' and passwords='"
						+ request.getParameter("passwords") + "'").list();
		//如果列表长度为0，否则将其密码保存如数据库中
		if (list.size() == 0) {
			return "-1";// 账号或密码错误
		} else {
			users model = (users) list.get(0);
			model.setPasswords(request.getParameter("passwords_new"));
			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			return "1";// 修改成功
		}

	}

	/**
	 * 取得中文
	 * 
	 * @param 原字符
	 * @return
	 */
	private String getChinese(String str) {
		if (str == null) {
			return "";
		}
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		}
	}

	/**
	 * 公用的获取一行数据方法
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getOneRow() throws UnsupportedEncodingException {
		//列表为空
		List list = null;
		//如果id的值为空
		if (request.getParameter("ID") == null) {
			//查询数据并得到返回的值
			list = session
					.createQuery(" from " + request.getParameter("Table"))
					.list();
		} else {
			//不为空时，查询数据且得到返回的值
			list = session.createQuery(
					" from " + request.getParameter("Table") + " where id="
							+ request.getParameter("ID")).list();
		}
		//转化成json格式进行返回
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	/**
	 * 删除订单的操作
	 * @return
	 */
	public String Del() {
		//得到id值
		int ID = Integer.valueOf(request.getParameter("ID"));
		//得到table值
		String Table = request.getParameter("Table");
		String PK_Name = "id";
		//删除订单的操作
		String sql = "delete from " + Table + " where " + PK_Name + "=" + ID;
		HibernateSessionFactory.updateExecute(sql);
		return "1";

	}

	@Override
	public void init() throws ServletException {

	}

}
