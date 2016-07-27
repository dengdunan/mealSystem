package com.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.bean.PagesHelper;

@SuppressWarnings({ "unchecked", "serial","rawtypes" })
/**
 * ����servlet,�����б����ʾ����������������(���޸ĺ�ɾ������)
 * @author dda
 *
 */
public class GridServlet extends HttpServlet {
	//����Ự����Ϊ��
	private Session session = null;
	//GridServlet���캯��
	public GridServlet() {
		super();
		//�����ݳ־û�������ȡ�ûỰ����
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
		//��Hibernate�Ự�����л��session();
		session = HibernateSessionFactory.getSession();
		//��������ı����ʽΪUTF-8
		request.setCharacterEncoding("UTF-8");
		//������Ӧ�����ʽΪUTF-8
		response.setCharacterEncoding("UTF-8");
		//������Ӧ����������
		response.setContentType("text/html; charset=UTF-8");
		//���Action����
		String action = request.getParameter("Action");
		//��ӡactionֵ
		System.out.println("ִ��GridServlet:" + action);
		//��sqlString��rs��Ϊnull
		String sqlString = "";
		ResultSet rs = null;
		//ʵ�����б�
		List list = new ArrayList();
		//ҳ����СΪ10
		int pageSize = 10;
		//��ǰҳΪ0
		int currentpage = 0;
		//�õ���ǰҳ��
		currentpage = Integer.valueOf(request.getParameter("currentpage"));
		//��ǰҳ����1���Ƚϣ�ȡ���е����ֵ
		currentpage = Math.max(currentpage, 1);

		//action�����Ƿ���getlist
		if (action.equals("getlist")) {
			String msg = "";
			//�������Ĳ���msg��Ϊ��
			if (request.getParameter("msg") != null) {
				//ת�������ĵı����ʽ
				msg = getChinese(request.getParameter("msg"));
				//��ӡmsg��Ϣ
				System.out.println("msg  " + msg);
			}
			//ҳ����СΪ6
			pageSize = 6;
			//ʵ����ҳ�������
			PagesHelper model = new PagesHelper();
			//�����������ҳ���������
			model.setTableName("dishes ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and title like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//��ǰҳ��ȥcurrentpage, pagecount�е���Сֵ
			currentpage = Math.min(currentpage, pagecount);
			//��ʼҳ
			int start = (currentpage - 1) * pageSize + 1;
			//����ҳ
			int limit = pageSize;
			//�����ݼ���ҳ���������
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			//ִ�в�ѯ����
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			//��ӡ��ѯ�����б�
			System.out.println(model.ToListString());
			//������Ӧ��������Լ���ҳ���ض����λ��
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../index.jsp").forward(request,
					response);
		}
		//���action�Ĳ���Ϊgetuserlist
		if (action.equals("getuserlist")) {
			//msg��Ϊ��
			String msg = "";
			//���msg��Ϊ��
			if (request.getParameter("msg") != null) {
				//ת�������ĸ�ʽ
				msg = getChinese(request.getParameter("msg"));
				//��ӡ��Ϣ
				System.out.println("msg  " + msg);
			}
			//ҳ���СΪ6
			pageSize = 6;
			//ʵ����ҳ�������
			PagesHelper model = new PagesHelper();
			//����������Լ��뵽model��
			model.setTableName("users ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and name like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//��ǰҳȡcurrentpage, pagecount���е���Сֵ
			currentpage = Math.min(currentpage, pagecount);
			//��ʼҳ
			int start = (currentpage - 1) * pageSize + 1;
			//����ҳ
			int limit = pageSize;
			//����������Լ��뵽model��
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			//��ѯ�б��е�����
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			//��ӡ���б��е�����
			System.out.println(model.ToListString());
			//���������������Ժ͵�ַ���ض���
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../userlist.jsp").forward(request,
					response);
		}
		//���action����ֵΪgettypelist
		if (action.equals("gettypelist")) {
			String msg = "";
			//msg��Ϊ��ʱ
			if (request.getParameter("msg") != null) {
				//ת��msg�ı����ʽ
				msg = getChinese(request.getParameter("msg"));
				System.out.println("msg  " + msg);
			}
			//ҳ����СΪ6
			pageSize = 6;
			//ʵ����ҳ�������
			PagesHelper model = new PagesHelper();
			//����ʵ����ı���������������Ŀ���������Ϣ
			model.setTableName("types ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and typename like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//��ǰҳ���ҳ���������бȽϣ�ȡ����С��һҳ
			currentpage = Math.min(currentpage, pagecount);
			//��ʼҳ
			int start = (currentpage - 1) * pageSize + 1;
			//����ҳ��
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			//ִ�в�ѯ����ä�����õ��б�
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			//������������datalist
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			//������������currentpage
			request.setAttribute("currentpage", currentpage);
			//������������pagecount
			request.setAttribute("pagecount", pagecount);
			//������������totalCount
			request.setAttribute("total", totalCount);
			//��������ת����·��
			request.getRequestDispatcher("../typelist.jsp").forward(request,
					response);
		}
		
		//���action����Ϊgetorderlist
		if (action.equals("getorderlist")) {
			String msg = "";
			//��ȡ�õ���msg�����Ƿ�Ϊ��
			if (request.getParameter("msg") != null) {
				//ת�������ʽ
				msg = getChinese(request.getParameter("msg"));
				System.out.println("msg  " + msg);
			}
			pageSize = 6;
			//ִ�в�ѯ�������Ƿ񶩵������
			PagesHelper model = new PagesHelper();
			model.setTableName("orders INNER JOIN users ON orders.userid=users.id INNER JOIN dishes ON dishes.id=orders.dishesid");
			model.setColumnName("orders.id,orders.username,orders.seat,orders.price,orders.amount,users.name,orders.price*orders.amount as total,dishes.title, case status when 1 then '�Ѿ����' WHEN 0 then '������' else '��ȡ��' end status1");
			model.setOrder("orders.id");
			model.setFilter(" and users.name like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//���õ�ǰҳΪcurrentpage, pagecount�е���Сֵ
			currentpage = Math.min(currentpage, pagecount);
			//��ʼҳ
			int start = (currentpage - 1) * pageSize + 1;
			//������
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			BaseUtil.LogII(model.ToListString());
			//ִ�в�ѯ����
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			//���������������ԣ�����datalist,currentpage,total
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../orderlist.jsp").forward(request,
					response);
		}

	}

	/**
	 * ȡ������
	 * 
	 * @param ԭ�ַ�
	 * @return
	 */
	private String getChinese(String str) {
		//���strΪ�գ��򷵻�null
		if (str == null) {
			return "";
		}
		try {
			//����ת�������ʽΪUTF-8
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ִ��GridServlet");
	}

	@Override
	public void init() throws ServletException {

	}

}
