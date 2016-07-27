package com.mm.mealapp.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.miebo.utils.CommonApplication;
import com.miebo.utils.HttpUtil;
import com.miebo.utils.OnLineUser;
import com.miebo.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * BaseActivity�Ĺ�����
 */
public class BaseActivity extends Activity implements OnClickListener {
    //����˵�ַ
    protected String serverUrl;
    //��˾����
    protected ToastUtil toastUtil;
    //��ͼ
    protected Intent intent;
    //json����
    protected JSONArray jsonArray;
    //json����
    protected JSONObject jsonObject;
    //htttp������
    protected HttpUtil httpHelper;
    //�������Ի���
    protected ProgressDialog dialog;
    //�û�ʵ����
    protected OnLineUser user;
    //ͨ��Ӧ������
    protected CommonApplication application;

    public BaseActivity() {
    }

    /**
     * ʵ�����������
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.toastUtil = new ToastUtil(this.getApplicationContext());
        this.httpHelper = new HttpUtil();
        this.application = (CommonApplication) this.getApplicationContext();
        this.user = this.application.getOnlineUser();
    }

    /**
     * ����¼�
     * @param v
     */
    public void onClick(View v) {
    }
}
