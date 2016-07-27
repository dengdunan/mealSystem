package com.bean;

/**
 * ҳ�������
 * @author dda
 *
 */
public class PagesHelper {
	//����
	private String _tablename = "";
	//��Ҫ����
	private String _primary = "";
	//����
	private String _columnname = "";
	//���˵��ı�
	private String _filter = "";
	//������
	private String _order = "";
	//��ǰ����ֵ
	private int _currentIndex = 0;  
	//ҳ���СΪ10
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

	//ת�����б��ַ���
	public String ToListString() {
		//�ж϶����Ƿ�Ϊ��,Ϊ����_primary����Ϊ����_order
		_order = _order == "" ? _primary : _order;

		//SQL����ѯҳ��
		String SQLPage = "SELECT "+_columnname+" FROM " + _tablename + " WHERE " + _order
				+ " <= ";
		//SQLҳ�������ݿ��еõ�ҳ���ҳ���������ս�������
		SQLPage += "(SELECT " + _order + " FROM " + _tablename + "  ORDER BY "
				+ _order + " desc LIMIT "
				+ (_currentIndex - 1 < 0 ? 0 : (_currentIndex - 1)) + ", 1 )  "
				+ _filter + " ORDER BY " + _order + " desc LIMIT " + _pagesize;
		System.out.println(SQLPage);
		return SQLPage;
	}

	//ת�������ַ���
	public String ToCountString() {
		//���ض����ĸ���
		return "select count(1) from " + _tablename + " where 1=1 " + _filter;

	}
}
