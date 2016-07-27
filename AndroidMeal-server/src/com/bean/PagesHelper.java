package com.bean;

/**
 * 页面帮助类
 * @author dda
 *
 */
public class PagesHelper {
	//表名
	private String _tablename = "";
	//主要类型
	private String _primary = "";
	//列名
	private String _columnname = "";
	//过滤的文本
	private String _filter = "";
	//订单名
	private String _order = "";
	//当前索引值
	private int _currentIndex = 0;  
	//页面大小为10
	private int _pagesize = 10; 

	public void setTableName(String _tablename) {
		this._tablename = _tablename;
	}

	public void setPrimary(String _primary) {
		this._primary = _primary;
	}

	public void setCurrentIndex(int _currentIndex) {
		this._currentIndex = _currentIndex;
	}

	public void setPageSize(int _pagesize) {
		this._pagesize = _pagesize;
	}

	public void setColumnName(String _columnname) {
		this._columnname = _columnname;
	}

	public void setFilter(String _filter) {
		this._filter = _filter;
	}

	public void setOrder(String _order) {
		this._order = _order;
	}

	//转换成列表字符串
	public String ToListString() {
		//判断订单是否为空,为空则_primary，不为空则_order
		_order = _order == "" ? _primary : _order;

		//SQL语句查询页面
		String SQLPage = "SELECT "+_columnname+" FROM " + _tablename + " WHERE " + _order
				+ " <= ";
		//SQL页面在数据库中得到页面的页数，并按照降序排列
		SQLPage += "(SELECT " + _order + " FROM " + _tablename + "  ORDER BY "
				+ _order + " desc LIMIT "
				+ (_currentIndex - 1 < 0 ? 0 : (_currentIndex - 1)) + ", 1 )  "
				+ _filter + " ORDER BY " + _order + " desc LIMIT " + _pagesize;
		System.out.println(SQLPage);
		return SQLPage;
	}

	//转换数量字符串
	public String ToCountString() {
		//返回订单的个数
		return "select count(1) from " + _tablename + " where 1=1 " + _filter;

	}
}
