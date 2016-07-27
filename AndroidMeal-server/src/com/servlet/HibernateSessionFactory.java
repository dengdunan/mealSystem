package com.servlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate持久层会话工厂
 * @author nuo
 *
 */
public class HibernateSessionFactory {

	//配置文件路径
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	//取得Session线程池
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	//实例化配置
	private static Configuration configuration = new Configuration();
	//定义会话工厂
	private static org.hibernate.SessionFactory sessionFactory;
	//定义配置文件
	private static String configFile = CONFIG_FILE_LOCATION;

	//静态加载，最先执行该段
	static {
		try {
			//配置配置文件
			configuration.configure(configFile);
			//新建（建立）会话工厂
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	private HibernateSessionFactory() {
	}
	
	/**
	 * 得到会话对象
	 * @return
	 * @throws HibernateException
	 */
	public static Session getSession() throws HibernateException {
		//会话对象从线程池中获取到
		Session session = threadLocal.get();

		//如果会话为空或者会话没有开启
		if (session == null || !session.isOpen()) {
			//如果会话工厂为空，则重新建立会话工厂
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			//如果会话工厂不为空，则设置会话功能中开启会话，否则为nul。
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			//线程池设置会话
			threadLocal.set(session);
		}

		return session;
	}

	/**
	 * 重建会话工厂
	 */
	public static void rebuildSessionFactory() {
		try {
			//配置配置文件
			configuration.configure(configFile);
			//会话工厂的建立
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
	 * 关闭会话
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		//会话从线程池中获得
		Session session = threadLocal.get();
		//置线程池为空
		threadLocal.set(null);

		//如果会话不为空。将会话关闭
		if (session != null) {
			session.close();
		}
	}

	/**
	 * 获得会话工厂
	 * @return
	 */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * 设置设置文件
	 * @param configFile
	 */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
	 * 获得设置文件
	 * @return
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * 查询sql语句
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ResultSet queryBySql(String sql) {
		ResultSet rs = null;
		try {
			Connection con = getSession().connection();
			Statement sta = con.createStatement();
			rs = sta.executeQuery(sql);

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return rs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * ResultSet 转list
	 * 从数据库得到一个列表
	 * @param rs
	 * @return
	 */
	public static List convertList(ResultSet rs) {
		List listOfRows = new ArrayList();
		try {
			ResultSetMetaData md = rs.getMetaData();
			int num = md.getColumnCount();
			while (rs.next()) {
				Map mapOfColValues = new HashMap(num);
				for (int i = 1; i <= num; i++) {
					BaseUtil.LogII(md.getColumnName(i));
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
				}
				listOfRows.add(mapOfColValues);
			
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return listOfRows;
	}

	/**
	 * 执行数据库更新操作
	 * @param sql
	 * @return
	 */
	public static int updateExecute(String sql) {
		int result = 0;
		try {
			Session session = getSession();
			Connection con = session.connection();
			Transaction tran = session.beginTransaction();
			tran.begin();
			Statement sta = con.createStatement();
			result = sta.executeUpdate(sql);
			tran.commit();
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return result;
	}

	/**
	 * 执行查询金额的操作（从数据库中）
	 * @param sql
	 * @return
	 */
	public static String executeScalar(String sql) {
		ResultSet rs = queryBySql(sql);
		String s = "";
		try {
			while (rs.next()) {
				s = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
}