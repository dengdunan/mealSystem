package com.servlet;

/**
 * 基本工具类
 * @author dda
 *
 */
public class BaseUtil {
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(CharSequence str) {
		//如果str为空或者str等于空或者str长度为0，则返回空，否则不为空
		if (str == null ||"null".equals(str)|| str.length() == 0)
			return true;
		else
			return false;
	}
	/**
	 * 打印转换成字符串类型的数据
	 * @param iString
	 */
	public static void LogII(Object iString) {
		System.out.println("System.out:"+String.valueOf(iString));
	}

	
}
