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
 * BaseActivity的工具类
 */
public class BaseActivity extends Activity implements OnClickListener {
    //服务端地址
    protected String serverUrl;
    //吐司工具
    protected ToastUtil toastUtil;
    //意图
    protected Intent intent;
    //json数组
    protected JSONArray jsonArray;
    //json对象
    protected JSONObject jsonObject;
    //htttp工具类
    protected HttpUtil httpHelper;
    //进度条对话框
    protected ProgressDialog dialog;
    //用户实体类
    protected OnLineUser user;
    //通用应用设置
    protected CommonApplication application;

    public BaseActivity() {
    }

    /**
     * 实例化相关属性
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
     * 点击事件
     * @param v
     */
    public void onClick(View v) {
    }
}
