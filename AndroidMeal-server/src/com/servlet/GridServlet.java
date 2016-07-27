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
 * 网格servlet,进行列表的显示及其其他操作功能(如修改和删除功能)
 * @author dda
 *
 */
public class GridServlet extends HttpServlet {
	//定义会话内容为空
	private Session session = null;
	//GridServlet构造函数
	public GridServlet() {
		super();
		//从数据持久化工厂中取得会话内容
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
		//从Hibernate会话工厂中获得session();
		session = HibernateSessionFactory.getSession();
		//设置请求的编码格式为UTF-8
		request.setCharacterEncoding("UTF-8");
		//设置响应编码格式为UTF-8
		response.setCharacterEncoding("UTF-8");
		//设置响应的内容类型
		response.setContentType("text/html; charset=UTF-8");
		//获得Action参数
		String action = request.getParameter("Action");
		//打印action值
		System.out.println("执行GridServlet:" + action);
		//将sqlString，rs置为null
		String sqlString = "";
		ResultSet rs = null;
		//实例化列表
		List list = new ArrayList();
		//页数大小为10
		int pageSize = 10;
		//当前页为0
		int currentpage = 0;
		//得到当前页数
		currentpage = Integer.valueOf(request.getParameter("currentpage"));
		//当前页数与1做比较，取其中的最大值
		currentpage = Math.max(currentpage, 1);

		//action参数是否是getlist
		if (action.equals("getlist")) {
			String msg = "";
			//如果请求的参数msg不为空
			if (request.getParameter("msg") != null) {
				//转换成中文的编码格式
				msg = getChinese(request.getParameter("msg"));
				//打印msg消息
				System.out.println("msg  " + msg);
			}
			//页数大小为6
			pageSize = 6;
			//实例化页面帮助类
			PagesHelper model = new PagesHelper();
			//将数据添加入页面帮助类中
			model.setTableName("dishes ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and title like '%" + msg + "%'");
			// 总共多少条
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// 多少页
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//当前页面去currentpage, pagecount中的最小值
			currentpage = Math.min(currentpage, pagecount);
			//开始页
			int start = (currentpage - 1) * pageSize + 1;
			//限制页
			int limit = pageSize;
			//将数据加入页面帮助类中
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			//执行查询操作
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			//打印查询到的列表
			System.out.println(model.ToListString());
			//设置响应的相关属性及其页面重定向的位置
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../index.jsp").forward(request,
					response);
		}
		//如果action的参数为getuserlist
		if (action.equals("getuserlist")) {
			//msg置为空
			String msg = "";
			//如果msg不为空
			if (request.getParameter("msg") != null) {
				//转换成中文格式
				msg = getChinese(request.getParameter("msg"));
				//打印消息
				System.out.println("msg  " + msg);
			}
			//页面大小为6
			pageSize = 6;
			//实例化页面帮助类
			PagesHelper model = new PagesHelper();
			//设置相关属性加入到model中
			model.setTableName("users ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and name like '%" + msg + "%'");
			// 总共多少条
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// 多少页
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//当前页取currentpage, pagecount其中的最小值
			currentpage = Math.min(currentpage, pagecount);
			//开始页
			int start = (currentpage - 1) * pageSize + 1;
			//限制页
			int limit = pageSize;
			//设置相关属性加入到model中
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			//查询列表中的数据
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			//打印出列表中的数据
			System.out.println(model.ToListString());
			//设置请求的相关属性和地址的重定向
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../userlist.jsp").forward(request,
					response);
		}
		//如果action参数值为gettypelist
		if (action.equals("gettypelist")) {
			String msg = "";
			//msg不为空时
			if (request.getParameter("msg") != null) {
				//转换msg的编码格式
				msg = getChinese(request.getParameter("msg"));
				System.out.println("msg  " + msg);
			}
			//页数大小为6
			pageSize = 6;
			//实例化页面帮助类
			PagesHelper model = new PagesHelper();
			//设置实体类的表名，列名，和条目及其过滤信息
			model.setTableName("types ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and typename like '%" + msg + "%'");
			// 总共多少条
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// 多少页
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//当前页面和页面总数进行比较，取其中小的一页
			currentpage = Math.min(currentpage, pagecount);
			//开始页
			int start = (currentpage - 1) * pageSize + 1;
			//限制页数
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			//执行查询操作盲，并得到列表
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			//设置请求属性datalist
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			//设置请求属性currentpage
			request.setAttribute("currentpage", currentpage);
			//设置请求属性pagecount
			request.setAttribute("pagecount", pagecount);
			//设置请求属性totalCount
			request.setAttribute("total", totalCount);
			//设置请求转发的路径
			request.getRequestDispatcher("../typelist.jsp").forward(request,
					response);
		}
		
		//如果action参数为getorderlist
		if (action.equals("getorderlist")) {
			String msg = "";
			//所取得到的msg内容是否为空
			if (request.getParameter("msg") != null) {
				//转换编码格式
				msg = getChinese(request.getParameter("msg"));
				System.out.println("msg  " + msg);
			}
			pageSize = 6;
			//执行查询操作，是否订单已完成
			PagesHelper model = new PagesHelper();
			model.setTableName("orders INNER JOIN users ON orders.userid=users.id INNER JOIN dishes ON dishes.id=orders.dishesid");
			model.setColumnName("orders.id,orders.username,orders.seat,orders.price,orders.amount,users.name,orders.price*orders.amount as total,dishes.title, case status when 1 then '已经完成' WHEN 0 then '进行中' else '已取消' end status1");
			model.setOrder("orders.id");
			model.setFilter(" and users.name like '%" + msg + "%'");
			// 总共多少条
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// 多少页
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			//设置当前页为currentpage, pagecount中的最小值
			currentpage = Math.min(currentpage, pagecount);
			//开始页
			int start = (currentpage - 1) * pageSize + 1;
			//限制数
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);
			BaseUtil.LogII(model.ToListString());
			//执行查询操作
			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			//设置请求的相关属性，例如datalist,currentpage,total
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
	 * 取得中文
	 * 
	 * @param 原字符
	 * @return
	 */
	private String getChinese(String str) {
		//如果str为空，则返回null
		if (str == null) {
			return "";
		}
		try {
			//否则转换编码格式为UTF-8
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("执行GridServlet");
	}

	@Override
	public void init() throws ServletException {

	}

}
