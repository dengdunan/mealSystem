package com.mm.mealapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miebo.utils.BaseUtil;
import com.miebo.utils.HttpUtil;
import com.mm.mealapp.utils.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册界面
 *
 * @author dda
 */
public class RegisterActivity extends BaseActivity {

    //登录按钮和注册按钮
    private Button btnLogin, btnRegister;
    //登录Id文本框，密码文本框，确认密码正确文本框，名字文本框
    private EditText etLoginID, etPassword, etPasswordOK, etName;
    //http工具类
    private HttpUtil httpHelper;
    //进度条对话框
    private ProgressDialog dialog;
    //异步加载任务
    private loadAsyncTask loginAsyncTask;
    //id值
    private int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化视图
        findview();
        //设置监听器
        setListener();
        //实例化http工具
        httpHelper = new HttpUtil();
        //如果用户名不为空，则进行相关的注册操作
        if (user != null) {
            id = user.getId();
            ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("修改信息");
            etLoginID.setText(user.getLoginid());
            etName.setText(user.getName());
            btnRegister.setText("修改");
            btnLogin.setVisibility(View.GONE);
        }

    }

    //初始化视图，实例化各个控件
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("注册");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
        etName = (EditText) findViewById(R.id.etName);

    }

    //设置监听器，设置注册和登录的监听器
    private void setListener() {
        btnRegister.setOnClickListener(new btnRegisterOnClickListener());

        btnLogin.setOnClickListener(this);
    }

    /**
     * 注册按钮监听器
     */
    @SuppressWarnings("unchecked")
    private class btnRegisterOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            //如果学号的长度为0，则弹出提示
            if (etLoginID.getText().length() == 0) {
                toastUtil.show("请输入学号");
                return;
            }
            //如果姓名的长度为0，则弹出提示
            if (etName.getText().length() == 0) {
                toastUtil.show("请输入姓名");
                return;
            }
            //如果密码的长度为0，则弹出提示
            if (etPassword.getText().length() == 0) {
                toastUtil.show("请输入密码");
                return;
            }
            //如果再次输入密码的长度为0，则弹出提示
            if (etPasswordOK.getText().length() == 0) {
                toastUtil.show("请再次输入密码");
                return;
            }
            //如果输入密码和再次输入密码的不一致，则弹出提示
            if (!etPassword.getText().toString().equals(etPasswordOK.getText().toString())) {
                toastUtil.show("两次输入的密码不一致");
                return;
            }
            //隐藏自带键盘
            BaseUtil.HideKeyboard(RegisterActivity.this);
            //执行异步加载任务操作
            loginAsyncTask = new loadAsyncTask();
            loginAsyncTask.execute("");

        }
    }

    ;

    /**
     * 异步加载任务，提交用户注册的信息到服务器上
     */
    @SuppressWarnings("deprecation")
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //弹出进度条对话框
            dialog = new ProgressDialog(RegisterActivity.this);
            //设置标题
            dialog.setTitle("提示");
            //设置消息
            dialog.setMessage("处理中,请稍后..");
            //设置取消按钮
            dialog.setCancelable(true);
            dialog.setButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //如果异步加载任务不为空，则使异步加载任务为空，并弹出吐司
                    if (loginAsyncTask != null) {
                        loginAsyncTask.cancel(true);
                        loginAsyncTask = null;
                        toastUtil.show("操作被取消");
                    }
                }
            });
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            //获得服务器端的Servlet地址
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
            //将值存入map集合中
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Action", "register");
            map.put("id", id);
            map.put("loginid", etLoginID.getText());
            map.put("password", etPassword.getText());
            map.put("name", etName.getText());
            //将map的值传入服务器，得到服务器返回的值
            String result = httpHelper.HttpPost(urlString, map);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            BaseUtil.LogII("result " + result);
            //将异步任务置为空
            loginAsyncTask = null;
            //提示对话框消失
            dialog.dismiss();
            //如果值不为空且值为1，如果为空，则弹出注册失败的消息
            if (result != null && result.trim().equals("1")) {
                //如果id为0，则弹出注册成功的吐司
                if (id == 0) {
                    toastUtil.show("注册成功");

                }
                //否则弹出修改成功的吐司
                else {
                    toastUtil.show("修改成功");

                }
                finish();
            } else {
                toastUtil.show("注册失败");
            }
        }
    }

    /**
     * 按钮点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录按钮的监听器
            case R.id.btnLogin:
                finish();
                break;

            default:
                break;
        }
    }

}
