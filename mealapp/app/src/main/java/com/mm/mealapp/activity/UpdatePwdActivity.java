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

import com.mm.mealapp.utils.BaseActivity;
import com.miebo.utils.BaseUtil;
import com.miebo.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新密码的界面(修改密码的界面)
 * @author dda
 * 
 */
public class UpdatePwdActivity extends BaseActivity {

	//登录和注册按钮
	private Button btnLogin, btnRegister;
	//登录Id值
	private EditText etLoginID;
	//老密码值
	private EditText etPasswordOld;
	//新密码值
	private EditText etPassword;
	//新密码确认值
	private EditText etPasswordOK;
	//工具类
	private HttpUtil httpHelper;
	//进程对话框
	private ProgressDialog dialog;
	//加载异步任务
	private loadAsyncTask loginAsyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updpwd);
		//初始化视图
		findview();
		//设置监听器
		setListener();
		//初始化http工具类
		httpHelper = new HttpUtil();

	}

	/**
	 * 初始化视图，实例化各个控件
	 */
	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("修改密码");
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etLoginID = (EditText) findViewById(R.id.etLoginID);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
		etPasswordOld = (EditText) findViewById(R.id.etPasswordOld);

	}

	//设置监听器
	private void setListener() {
		btnRegister.setOnClickListener(new btnRegisterOnClickListener());

		btnLogin.setOnClickListener(this);
	}

	//注册监听器
	@SuppressWarnings("unchecked")
	private class btnRegisterOnClickListener implements OnClickListener {


		@Override
		public void onClick(View v) {
			//判断登录iD的长度是否为0
			if (etLoginID.getText().length() == 0) {
				toastUtil.show("请输入账号");
				return;
			}
			//判断登录的老密码是否为0
			if (etPasswordOld.getText().length() == 0) {
				toastUtil.show("请输入旧密码");
				return;
			}
			//判断登录的新密码是否为0
			if (etPassword.getText().length() == 0) {
				toastUtil.show("请输入密码");
				return;
			}
			//判断登录的确认新密码是否为0
			if (etPasswordOK.getText().length() == 0) {
				toastUtil.show("请再次输入密码");
				return;
			}
			//判断输入的新密码和确认输入的新密码是否一致
			if (!etPassword.getText().toString().equals(etPasswordOK.getText().toString())) {
				toastUtil.show("两次输入的密码不一致");
				return;
			}
			//隐藏键盘
			BaseUtil.HideKeyboard(UpdatePwdActivity.this);
			loginAsyncTask = new loadAsyncTask();
			loginAsyncTask.execute("");

		}
	};

	/**
	 * 异步加载任务，主要处理更新密码的操作
	 */
	@SuppressWarnings("deprecation")
	private class loadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			//实例化进度条对话框
			dialog = new ProgressDialog(UpdatePwdActivity.this);
			//设置标题
			dialog.setTitle("提示");
			//设置消息
			dialog.setMessage("处理中,请稍后..");
			//设置取消按钮
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//如果异步任务不为空，则将其置为空，并弹出消息，操作被取消
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
			//获得服务器端的servlet地址
			String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
			//将值存入集合中
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Action", "updatePwd");
			map.put("loginid", etLoginID.getText());
			map.put("passwords", etPasswordOld.getText());
			map.put("passwords_new", etPasswordOK.getText());
			//将值传至服务器，得到返回的值
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
			//如果result值不为空且值为1，则提示修改成功
			if (result != null && result.trim().equals("1")) {
				toastUtil.show("修改成功");
				finish();
			}
			//如果result不为空且值为-1，则提示旧密码错误
			else if (result != null && result.trim().equals("-1")) {
				toastUtil.show("旧密码错误");
			}
			//否则弹出修改失败
			else {
				toastUtil.show("修改失败");
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
		case R.id.btnLogin:
			finish();
			break;

		default:
			break;
		}
	}

}
