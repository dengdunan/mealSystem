package com.mm.mealapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.mealapp.utils.BaseActivity;
import com.miebo.utils.BaseUtil;
import com.miebo.utils.HttpUtil;
import com.miebo.utils.OnLineUser;
import com.miebo.utils.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 登录界面
 *
 * @author dda
 */
public class LoginActivity extends BaseActivity {

    //定义界面的注册按钮，登陆按钮
    private Button btnLogin, btnRegister;
    //定义界面的用户ID文本框和密码文本框
    private EditText etLoginID, etPassword;
    //定义多选框，是否记住密码
    private CheckBox ckbSavePwd;
    //http操作
    private HttpUtil httpHelper;
    //进度对话框
    private ProgressDialog dialog;
    //异步加载登录任务
    private loadAsyncTask loginAsyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化界面
        findview();
        //设置监听器
        setListener();
        //实例化http工具，并预设用户和密码
        httpHelper = new HttpUtil();
        etLoginID.setText("test00");
        etPassword.setText("111111");
    }

    //设置IP地址
    public void configIP(View source) {
        //使用SPUtil工具设置IP
        SPUtil.set(this, "IP", "");
        //跳转界面至StartActivity
        intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.setLoginUser(null);
    }

    //初始化界面，实例化各个控件，如登录，注册，密码等控件
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("登录");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ckbSavePwd = (CheckBox) findViewById(R.id.ckbSavePwd);

    }

    //设置登录监听器和注册监听器，设置用户文框和密码文本框的内容
    private void setListener() {
        btnLogin.setOnClickListener(new btnLoginOnClickListener());
        etLoginID.setText(SPUtil.get(LoginActivity.this, "loginid", ""));
        etPassword.setText(SPUtil.get(LoginActivity.this, "password", ""));
        btnRegister.setOnClickListener(this);
    }

    //登录监听器
    @SuppressWarnings("unchecked")
    private class btnLoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            //用户长度为0的情况，弹出提示
            if (etLoginID.getText().length() == 0) {
                toastUtil.show("请输入账号");
                return;
            }
            //密码长度为0的情况，弹出提示
            if (etPassword.getText().length() == 0) {
                toastUtil.show("请输入密码");
                return;
            }
            //隐藏键盘
            BaseUtil.HideKeyboard(LoginActivity.this);
            //实例化异步登录任务
            loginAsyncTask = new loadAsyncTask();
            loginAsyncTask.execute("");

        }
    }

    ;

    //异步加载登录任务
    @SuppressWarnings("deprecation")
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //实例化进度条对话框
            dialog = new ProgressDialog(LoginActivity.this);
            //设置标题
            dialog.setTitle("提示");
            //设置消息
            dialog.setMessage("登录中,请稍后..");
            //设置取消按键为true
            dialog.setCancelable(true);
            //设置按钮
            dialog.setButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //如果loginAsyncTask不为空，将其置为空，并弹出吐司
                    if (loginAsyncTask != null) {
                        loginAsyncTask.cancel(true);
                        loginAsyncTask = null;
                        toastUtil.show("登录被取消");
                    }
                }
            });
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // //获取服务端Urlservlet目录的地址
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=login";
            urlString += "&loginid=" + etLoginID.getText() + "&passwords=" + etPassword.getText();
            //获取从服务器返回的信息
            String json = httpHelper.HttpRequest(urlString);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loginAsyncTask = null;
            // //提示对话框消失
            dialog.dismiss();
            //判断是否登录成功，如果返回值长度为0则登录失败，否则显示登录成功，并保存用户登录信息
            if (result.trim().length() == 0) {
                toastUtil.show("登录失败");
                return;
            } else {
                //保存登录名
                SPUtil.set(LoginActivity.this, "loginid", etLoginID.getText().toString());
                //如果保存密码按钮被选中，则保存密码；否则保存空密码
                if (ckbSavePwd.isChecked()) {
                    SPUtil.set(LoginActivity.this, "password", etPassword.getText().toString());
                } else {
                    SPUtil.set(LoginActivity.this, "password", "");
                }
                try {
                    //解析json数据
                    jsonArray = new JSONArray(result);
                    jsonObject = jsonArray.getJSONObject(0);
                    // 保存登录用户信息
                    CommonApplication application = (CommonApplication) getApplicationContext();
                    OnLineUser model = new OnLineUser();
                    model.setId(jsonObject.getInt("id"));
                    model.setLoginid(etLoginID.getText().toString());
                    model.setName(jsonObject.getString("name"));
                    application.setLoginUser(model);
                    //弹出吐司，登录成功
                    toastUtil.show(model.getName() + ",登录成功");
                    //跳转界面至DishesListActivity
                    intent = new Intent(LoginActivity.this, DishesListActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 处理按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击了注册按钮，跳转至注册页面
            case R.id.btnRegister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
