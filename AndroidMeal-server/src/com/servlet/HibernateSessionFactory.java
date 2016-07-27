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
 * Hibernate�־ò�Ự����
 * @author nuo
 *
 */
public class HibernateSessionFactory {

	//�����ļ�·��
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	//ȡ��Session�̳߳�
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	//ʵ��������
	private static Configuration configuration = new Configuration();
	//����Ự����
	private static org.hibernate.SessionFactory sessionFactory;
	//���������ļ�
	private static String configFile = CONFIG_FILE_LOCATION;

	//��̬���أ�����ִ�иö�
	static {
		try {
			//���������ļ�
			configuration.configure(configFile);
			//�½����������Ự����
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	private HibernateSessionFactory() {
	}
	
	/**
	 * �õ��Ự����
	 * @return
	 * @throws HibernateException
	 */
	public static Session getSession() throws HibernateException {
		//�Ự������̳߳��л�ȡ��
		Session session = threadLocal.get();

		//����ỰΪ�ջ��߻Ựû�п���
		if (session == null || !session.isOpen()) {
			//����Ự����Ϊ�գ������½����Ự����
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			//����Ự������Ϊ�գ������ûỰ�����п����Ự������Ϊnul��
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			//�̳߳����ûỰ
			threadLocal.set(session);
		}

		return session;
	}

	/**
	 * �ؽ��Ự����
	 */
	public static void rebuildSessionFactory() {
		try {
			//���������ļ�
			configuration.configure(configFile);
			//�Ự�����Ľ���
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	/**
	 * �رջỰ
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		//�Ự���̳߳��л��
		Session session = threadLocal.get();
		//���̳߳�Ϊ��
		threadLocal.set(null);

		//����Ự��Ϊ�ա����Ự�ر�
		if (session != null) {
			session.close();
		}
	}

	/**
	 * ��ûỰ����
	 * @return
	 */
	public static org.hibernate.SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * ���������ļ�
	 * @param configFile
	 */
	public static void setConfigFile(String configFile) {
		HibernateSessionFactory.configFile = configFile;
		sessionFactory = null;
	}

	/**
	 * ��������ļ�
	 * @return
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * ��ѯsql���
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
	 * ResultSet תlist
	 * �����ݿ�õ�һ���б�
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
	 * ִ�����ݿ���²���
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
	 * ִ�в�ѯ���Ĳ����������ݿ��У�
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