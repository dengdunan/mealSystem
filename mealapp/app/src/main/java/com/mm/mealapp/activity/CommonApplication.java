package com.mm.mealapp.activity;

/**
 * 通用app应用设置
 */
public class CommonApplication extends com.miebo.utils.CommonApplication {

	//定义一个百度地图的秘钥
	public static final String strKeyBaiduMap = "F32d0b874fbade29187984040945de4e";
	//是否已经刷新内容
	private boolean isRefreshComment;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	//判断是否刷新内容
	public boolean isRefreshComment() {
		return isRefreshComment;
	}

	//设置是否需要刷新内容
	public void setRefreshComment(boolean isRefreshComment) {
		this.isRefreshComment = isRefreshComment;
	}

}
