package com.servlet;

/**
 * ����������
 * @author dda
 *
 */
public class BaseUtil {
	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(CharSequence str) {
		//���strΪ�ջ���str���ڿջ���str����Ϊ0���򷵻ؿգ�����Ϊ��
		if (str == null ||"null".equals(str)|| str.length() == 0)
			return true;
		else
			return false;
	}
	/**
	 * ��ӡת�����ַ������͵�����
	 * @param iString
	 */
	public static void LogII(Object iString) {
		System.out.println("System.out:"+String.valueOf(iString));
	}

	
}
