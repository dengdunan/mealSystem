package com.mm.mealapp.activity;

/**
 * ͨ��appӦ������
 */
public class CommonApplication extends com.miebo.utils.CommonApplication {

	//����һ���ٶȵ�ͼ����Կ
	public static final String strKeyBaiduMap = "F32d0b874fbade29187984040945de4e";
	//�Ƿ��Ѿ�ˢ������
	private boolean isRefreshComment;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	//�ж��Ƿ�ˢ������
	public boolean isRefreshComment() {
		return isRefreshComment;
	}

	//�����Ƿ���Ҫˢ������
	public void setRefreshComment(boolean isRefreshComment) {
		this.isRefreshComment = isRefreshComment;
	}

}
