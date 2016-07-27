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
 * ����Servlet,ֻ���¼�����أ�������ȡ������λ��Ԥ���ȵȹ���
 * @author nuo
 *
 */
@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class ServletService extends HttpServlet {

	//��ʽ������
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	java.text.SimpleDateFormat formatdate = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");
	java.util.Date currentTime = new java.util.Date();// �õ���ǰϵͳʱ��

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
	 * �ͻ�����ʹ��Get��ʽ�����������
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//��ʼ���Ự
		this.request = request;
		session = HibernateSessionFactory.getSession();
		session.flush();
		session.clear();
		//ɫ��������ı����ʽ����Ӧ�ı����ʽ
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//���Actionֵ��������Ϣ
		String action = request.getParameter("Action");
		System.out.println(action);
		String write = "";
		String sqlString = "";
		System.out.print(action);
		// ��¼��֤
		if (action.equals("login")) {
			write = login();

		}
		//��ȡһ������
		if (action.equals("getOneRow")) {
			write = getOneRow();
		}
		//ִ��ɾ������
		if (action.equals("Del")) {
			write = Del();
		}
		//ȡ����������
		if (action.equals("cancelOrders")) {
			write = cancelOrders();
		}

		// ����Ա��¼��֤
		if (action.equals("adminlogin")) {
			write = adminlogin();
		}
		//��õ���б�
		if (action.equals("getdisheslist")) {
			write = getdisheslist();

		}
		//����ҵĶ����б�
		if (action.equals("getmyorderslist")) {
			write = getmyorderslist();
		}
		//������λ״̬
		if (action.equals("ChangeStatus")) {
			write = changestatus();
		}
		//�����Ʒ����
		if (action.equals("edit")) {
			write = edit();
		}
		//ע���û�
		if (action.equals("edituser")) {
			write = edituser();
		}
		//�༭����
		if (action.equals("edittype")) {
			write = edittype();
		}
		System.out.println(write);
		out.println(write);
		out.flush();
		out.close();
		

	}

	/**
	 * �ͻ�����ʹ��Post��ʽ�����������
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//��ʼ���Ự
		session = HibernateSessionFactory.getSession();
		//�ػ�ˢ��
		session.flush();
		//�Ự���
		session.clear();
		this.request = request;
		//ɫ��������ı����ʽ����Ӧ�ı����ʽ
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//���Actionֵ��������Ϣ
		String action = request.getParameter("Action");
		String write = "";
		// ע��
		if (action.equals("register")) {
			System.out.println(request.getParameter("id"));
			users model = new users();
			//�ж�id�Ƿ�Ϊ�ջ���id�Ƿ�Ϊ0
			if (request.getParameter("id") == null
					|| request.getParameter("id").equals("0")) {
				model = new users();

			} 
			//����ִ���û��Ĳ�ѯ����
			else {
				model = (users) (session.createQuery(
						" from users where id=" + request.getParameter("id"))
						.list().get(0));
			}
			//�����������ʵ�����У��������ϴ������ݿ���
			model.setLoginid(request.getParameter("loginid"));
			model.setPasswords(request.getParameter("password"));
			model.setName(getChinese(request.getParameter("name")));

			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			write = "1";
		}
		//ִ�и�����Ĥ����
		if (action.equals("updatePwd")) {
			write = updatePwd();
		}
		//�ύ��������
		if (action.equals("createorder")) {
			write = createorder();
		}
		out.println(write);
		out.flush();
		out.close();
	}

	/**
	 * ��õ�¼����Ϣ
	 */
	private String login() {
		String write = "";
		//��¼��
		String loginid = request.getParameter("loginid");
		//����
		String passwords = request.getParameter("passwords");
		//��ѯ���ݿ��е�����
		List<users> list = session.createQuery(
				" from users where loginid='" + loginid + "' and passwords='"
						+ passwords + "'").list();
		//�����ѯ�����ݣ���д��write��
		if (list.size() > 0) {
			write = JSONArray.fromObject(list.get(0)).toString();
		}
		return write;
	}
	
	/**
	 * ��ù���Ա��¼��Ϣ
	 */
	private String adminlogin() {
		String write = "";
		//��¼��
		String loginid = request.getParameter("loginid");
		//����
		String passwords = request.getParameter("passwords");
		//��ѯ���ݿ��е�����
		List<users> list = session.createQuery(
				" from admins where loginid='" + loginid + "' and passwords='"
						+ passwords + "'").list();
		//�����ѯ�����ݣ�������writeΪ1������Ϊ0
		if (list.size() > 0) {
			write = "1";
		} else {
			write = "0";
		}
		return write;
	}

	/**
	 * ����ҵĶ�����Ϣ
	 * @return
	 */
	private String getmyorderslist() {
		
		
		String write = "";
		//��ѯ�����е�����
		String sqlString = "select orders.status, orders.id,orders.userid,orders.username,orders.seat,orders.dishesid,orders.price,orders.amount,orders.createtime,dishes.title,dishes.img_url FROM orders INNER JOIN dishes on orders.dishesid=dishes.id ";
		sqlString += " where  userid =" + request.getParameter("userid");

		sqlString += " order by orders.id desc";
		ResultSet rs = HibernateSessionFactory.queryBySql(sqlString);
		//�õ������ݿ��з��صĽ��
		List list = HibernateSessionFactory.convertList(rs);
		//�����ѯ�����ݣ�����ʹ��jsonд���write��
		if (list.size() > 0) {
			JSONArray json = JSONArray.fromObject(list);
			write = json.toString();
		}
		return write;
	}

	/**
	 * ��õ���б���Ϣ
	 * @return
	 */
	private String getdisheslist() {
		String write = "";
		//����sql��ѯ���
		String sqlString = "from dishes where 1=1 ";
		//���msg��Ϊ��
		if (request.getParameter("msg") != null) {
			//������sql��ѯ���
			sqlString += " and title like '%"
					+ getChinese(request.getParameter("msg")) + "%'";
		}
		sqlString += " order by id desc";
		//�����ݿ������õ����б���Ϣ
		List list = session.createQuery(sqlString).list();
		//�����ѯ�����ݣ�����ʹ��jsonд���write��
		if (list.size() > 0) {
			JSONArray json = JSONArray.fromObject(list);
			write = json.toString();
		}
		return write;
	}
	
	/**
	 * ���������Ϣ
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String edit() throws UnsupportedEncodingException {
		//�õ�idֵ
		int id = Integer.valueOf((request.getParameter("ID")));
		dishes model;
		//���idΪ0�����ʼ��modelʵ���࣬����Ӳ�ѯ�в�ѯ����
		if (id == 0) {
			model = new dishes();

		} else {
			model = (dishes) (session
					.createQuery(" from dishes where id=" + id).list().get(0));
		}
		//���ͼƬ��ַ��Ϊ�գ���ͼƬ��С����0��������ͼƬ��·��
		if (request.getParameter("img_url") != null
				&& request.getParameter("img_url").length() > 0) {
			model.setImg_url(request.getParameter("img_url"));
		}

		//�������ݽ���ʵ�����У�����Ʒ�������Ϣ���룩
		model.setIntro(getChinese(request.getParameter("intro")));
		model.setTitle(getChinese(request.getParameter("title")));
		model.setPrice(Float.valueOf(request.getParameter("price")));
		model.setAmount(Float.valueOf(request.getParameter("amount")));
		model.setTypeid(Integer.valueOf(request.getParameter("typeid")));
		model.setTypename(getChinese(request.getParameter("typename")));
		Transaction tran = session.beginTransaction();

		//���id��Ϊ0����������ݣ�����ͱ�������
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}

	/**
	 * ���ȡ����������Ϣ
	 * @return
	 */
	private String cancelOrders() {
		//���idֵ
		int id = Integer.valueOf((request.getParameter("ID")));
		orders model;
		//�����ݿ��ѯ�еõ�������
		model = (orders) (session.createQuery(" from orders where id=" + id)
				.list().get(0));
		//�õ���λ��ֵ������
		String[] ss = model.getSeat().split(",");
		//��ѯ��Ԥ����λ�����ݣ������䱣������
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
	//�޸Ķ���״̬
	private String changestatus() {
		//���idֵ
		int id = Integer.valueOf((request.getParameter("ID")));
		//���״ֵ̬
		int status= Integer.valueOf((request.getParameter("status")));
		orders model;
		//�����ݿ��ѯ�еõ�������
		model = (orders) (session.createQuery(" from orders where id=" + id)
				.list().get(0));
		//����״ֵ̬
		model.setStatus(status);
		Transaction tran = session.beginTransaction();
		//��������
		session.save(model);
		tran.commit();
		return "1";
	}

	//ע���û�
	private String edituser() throws UnsupportedEncodingException {
		//���idֵ
		int id = Integer.valueOf((request.getParameter("ID")));
		users model;
		//��idΪ0ʱ����ʼ��users,��������ݿ��в�ѯ����
		if (id == 0) {
			model = new users();

		} else {
			model = (users) (session.createQuery(" from users where id=" + id)
					.list().get(0));
		}
		//����model�����Ϣ
		model.setLoginid(getChinese(request.getParameter("loginid")));
		model.setName(getChinese(request.getParameter("name")));
		model.setPasswords(request.getParameter("passwords"));
		Transaction tran = session.beginTransaction();
		//���id��Ϊ0����������ݣ�����ͱ�������
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}
	
	//���������Ϣ
	private String edittype() throws UnsupportedEncodingException {
		//���idֵ
		int id = Integer.valueOf((request.getParameter("ID")));
		types model;
		//��idΪ0ʱ����ʼ��types,��������ݿ��в�ѯ����
		if (id == 0) {
			model = new types();

		} else {
			model = (types) (session.createQuery(" from types where id=" + id)
					.list().get(0));
		}
		//����model�����Ϣ
		model.setTypename(getChinese(request.getParameter("typename")));
		
		Transaction tran = session.beginTransaction();
		//���id��Ϊ0����������ݣ�����ͱ�������
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}
	

	// �ύ����
	private String createorder() throws UnsupportedEncodingException {
		orders model = null;
		//�������еõ�������
		List list = session.createQuery(
				" from orders where id=" + request.getParameter("id")).list();
		//����б�Ĵ�СΪ0����ʵ����orders()�������õ�ǰ��ʱ�䣬����������еõ�����
		if (list.size() == 0) {
			model = new orders();
			model.setCreatetime(formatdate.format(currentTime));
		} else {
			model = (orders) list.get(0);
		}
		//�ӻỰ�в�ѯ���ݣ������ö�����ص�����
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
		//��ѯ��Ԥ����λ�����ݣ������䱣������
		String[] ss = model.getSeat().split(",");
		for (int i = 0; i < ss.length; i++) {
			seats s = (seats) session
					.createQuery(" from seats where id=" + ss[i]).list().get(0);
			s.setState(1);
			session.save(s);
		}

		Transaction tran = session.beginTransaction();
		//����model�����ݵ��Ự��
		session.save(model);
		tran.commit();
		return "1";
	}

	/**
	 * �޸�����
	 * 
	 * @return
	 */
	public String updatePwd() throws UnsupportedEncodingException {
		//�����ݿ��ѯ�󲢷�������
		List list = session.createQuery(
				" from users where loginid='" + request.getParameter("loginid")
						+ "' and passwords='"
						+ request.getParameter("passwords") + "'").list();
		//����б���Ϊ0�����������뱣�������ݿ���
		if (list.size() == 0) {
			return "-1";// �˺Ż��������
		} else {
			users model = (users) list.get(0);
			model.setPasswords(request.getParameter("passwords_new"));
			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			return "1";// �޸ĳɹ�
		}

	}

	/**
	 * ȡ������
	 * 
	 * @param ԭ�ַ�
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
	 * ���õĻ�ȡһ�����ݷ���
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getOneRow() throws UnsupportedEncodingException {
		//�б�Ϊ��
		List list = null;
		//���id��ֵΪ��
		if (request.getParameter("ID") == null) {
			//��ѯ���ݲ��õ����ص�ֵ
			list = session
					.createQuery(" from " + request.getParameter("Table"))
					.list();
		} else {
			//��Ϊ��ʱ����ѯ�����ҵõ����ص�ֵ
			list = session.createQuery(
					" from " + request.getParameter("Table") + " where id="
							+ request.getParameter("ID")).list();
		}
		//ת����json��ʽ���з���
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	/**
	 * ɾ�������Ĳ���
	 * @return
	 */
	public String Del() {
		//�õ�idֵ
		int ID = Integer.valueOf(request.getParameter("ID"));
		//�õ�tableֵ
		String Table = request.getParameter("Table");
		String PK_Name = "id";
		//ɾ�������Ĳ���
		String sql = "delete from " + Table + " where " + PK_Name + "=" + ID;
		HibernateSessionFactory.updateExecute(sql);
		return "1";

	}

	@Override
	public void init() throws ServletException {

	}

}
