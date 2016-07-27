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
 * ��������Ľ���(�޸�����Ľ���)
 * @author dda
 * 
 */
public class UpdatePwdActivity extends BaseActivity {

	//��¼��ע�ᰴť
	private Button btnLogin, btnRegister;
	//��¼Idֵ
	private EditText etLoginID;
	//������ֵ
	private EditText etPasswordOld;
	//������ֵ
	private EditText etPassword;
	//������ȷ��ֵ
	private EditText etPasswordOK;
	//������
	private HttpUtil httpHelper;
	//���̶Ի���
	private ProgressDialog dialog;
	//�����첽����
	private loadAsyncTask loginAsyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updpwd);
		//��ʼ����ͼ
		findview();
		//���ü�����
		setListener();
		//��ʼ��http������
		httpHelper = new HttpUtil();

	}

	/**
	 * ��ʼ����ͼ��ʵ���������ؼ�
	 */
	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("�޸�����");
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etLoginID = (EditText) findViewById(R.id.etLoginID);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
		etPasswordOld = (EditText) findViewById(R.id.etPasswordOld);

	}

	//���ü�����
	private void setListener() {
		btnRegister.setOnClickListener(new btnRegisterOnClickListener());

		btnLogin.setOnClickListener(this);
	}

	//ע�������
	@SuppressWarnings("unchecked")
	private class btnRegisterOnClickListener implements OnClickListener {


		@Override
		public void onClick(View v) {
			//�жϵ�¼iD�ĳ����Ƿ�Ϊ0
			if (etLoginID.getText().length() == 0) {
				toastUtil.show("�������˺�");
				return;
			}
			//�жϵ�¼���������Ƿ�Ϊ0
			if (etPasswordOld.getText().length() == 0) {
				toastUtil.show("�����������");
				return;
			}
			//�жϵ�¼���������Ƿ�Ϊ0
			if (etPassword.getText().length() == 0) {
				toastUtil.show("����������");
				return;
			}
			//�жϵ�¼��ȷ���������Ƿ�Ϊ0
			if (etPasswordOK.getText().length() == 0) {
				toastUtil.show("���ٴ���������");
				return;
			}
			//�ж�������������ȷ��������������Ƿ�һ��
			if (!etPassword.getText().toString().equals(etPasswordOK.getText().toString())) {
				toastUtil.show("������������벻һ��");
				return;
			}
			//���ؼ���
			BaseUtil.HideKeyboard(UpdatePwdActivity.this);
			loginAsyncTask = new loadAsyncTask();
			loginAsyncTask.execute("");

		}
	};

	/**
	 * �첽����������Ҫ�����������Ĳ���
	 */
	@SuppressWarnings("deprecation")
	private class loadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			//ʵ�����������Ի���
			dialog = new ProgressDialog(UpdatePwdActivity.this);
			//���ñ���
			dialog.setTitle("��ʾ");
			//������Ϣ
			dialog.setMessage("������,���Ժ�..");
			//����ȡ����ť
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//����첽����Ϊ�գ�������Ϊ�գ���������Ϣ��������ȡ��
					if (loginAsyncTask != null) {
						loginAsyncTask.cancel(true);
						loginAsyncTask = null;
						toastUtil.show("������ȡ��");
					}
				}
			});
			dialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			//��÷������˵�servlet��ַ
			String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
			//��ֵ���뼯����
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Action", "updatePwd");
			map.put("loginid", etLoginID.getText());
			map.put("passwords", etPasswordOld.getText());
			map.put("passwords_new", etPasswordOK.getText());
			//��ֵ�������������õ����ص�ֵ
			String result = httpHelper.HttpPost(urlString, map);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			BaseUtil.LogII("result " + result);
			//���첽������Ϊ��
			loginAsyncTask = null;
			//��ʾ�Ի�����ʧ
			dialog.dismiss();
			//���resultֵ��Ϊ����ֵΪ1������ʾ�޸ĳɹ�
			if (result != null && result.trim().equals("1")) {
				toastUtil.show("�޸ĳɹ�");
				finish();
			}
			//���result��Ϊ����ֵΪ-1������ʾ���������
			else if (result != null && result.trim().equals("-1")) {
				toastUtil.show("���������");
			}
			//���򵯳��޸�ʧ��
			else {
				toastUtil.show("�޸�ʧ��");
			}
		}
	}

	/**
	 * ����ť�ĵ���¼�
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
