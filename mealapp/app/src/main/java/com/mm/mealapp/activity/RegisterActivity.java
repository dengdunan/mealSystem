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
 * ע�����
 *
 * @author dda
 */
public class RegisterActivity extends BaseActivity {

    //��¼��ť��ע�ᰴť
    private Button btnLogin, btnRegister;
    //��¼Id�ı��������ı���ȷ��������ȷ�ı��������ı���
    private EditText etLoginID, etPassword, etPasswordOK, etName;
    //http������
    private HttpUtil httpHelper;
    //�������Ի���
    private ProgressDialog dialog;
    //�첽��������
    private loadAsyncTask loginAsyncTask;
    //idֵ
    private int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //��ʼ����ͼ
        findview();
        //���ü�����
        setListener();
        //ʵ����http����
        httpHelper = new HttpUtil();
        //����û�����Ϊ�գ��������ص�ע�����
        if (user != null) {
            id = user.getId();
            ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("�޸���Ϣ");
            etLoginID.setText(user.getLoginid());
            etName.setText(user.getName());
            btnRegister.setText("�޸�");
            btnLogin.setVisibility(View.GONE);
        }

    }

    //��ʼ����ͼ��ʵ���������ؼ�
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("ע��");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
        etName = (EditText) findViewById(R.id.etName);

    }

    //���ü�����������ע��͵�¼�ļ�����
    private void setListener() {
        btnRegister.setOnClickListener(new btnRegisterOnClickListener());

        btnLogin.setOnClickListener(this);
    }

    /**
     * ע�ᰴť������
     */
    @SuppressWarnings("unchecked")
    private class btnRegisterOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            //���ѧ�ŵĳ���Ϊ0���򵯳���ʾ
            if (etLoginID.getText().length() == 0) {
                toastUtil.show("������ѧ��");
                return;
            }
            //��������ĳ���Ϊ0���򵯳���ʾ
            if (etName.getText().length() == 0) {
                toastUtil.show("����������");
                return;
            }
            //�������ĳ���Ϊ0���򵯳���ʾ
            if (etPassword.getText().length() == 0) {
                toastUtil.show("����������");
                return;
            }
            //����ٴ���������ĳ���Ϊ0���򵯳���ʾ
            if (etPasswordOK.getText().length() == 0) {
                toastUtil.show("���ٴ���������");
                return;
            }
            //�������������ٴ���������Ĳ�һ�£��򵯳���ʾ
            if (!etPassword.getText().toString().equals(etPasswordOK.getText().toString())) {
                toastUtil.show("������������벻һ��");
                return;
            }
            //�����Դ�����
            BaseUtil.HideKeyboard(RegisterActivity.this);
            //ִ���첽�����������
            loginAsyncTask = new loadAsyncTask();
            loginAsyncTask.execute("");

        }
    }

    ;

    /**
     * �첽���������ύ�û�ע�����Ϣ����������
     */
    @SuppressWarnings("deprecation")
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //�����������Ի���
            dialog = new ProgressDialog(RegisterActivity.this);
            //���ñ���
            dialog.setTitle("��ʾ");
            //������Ϣ
            dialog.setMessage("������,���Ժ�..");
            //����ȡ����ť
            dialog.setCancelable(true);
            dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //����첽��������Ϊ�գ���ʹ�첽��������Ϊ�գ���������˾
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
            //��÷������˵�Servlet��ַ
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
            //��ֵ����map������
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Action", "register");
            map.put("id", id);
            map.put("loginid", etLoginID.getText());
            map.put("password", etPassword.getText());
            map.put("name", etName.getText());
            //��map��ֵ������������õ����������ص�ֵ
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
            //���ֵ��Ϊ����ֵΪ1�����Ϊ�գ��򵯳�ע��ʧ�ܵ���Ϣ
            if (result != null && result.trim().equals("1")) {
                //���idΪ0���򵯳�ע��ɹ�����˾
                if (id == 0) {
                    toastUtil.show("ע��ɹ�");

                }
                //���򵯳��޸ĳɹ�����˾
                else {
                    toastUtil.show("�޸ĳɹ�");

                }
                finish();
            } else {
                toastUtil.show("ע��ʧ��");
            }
        }
    }

    /**
     * ��ť����¼�
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //��¼��ť�ļ�����
            case R.id.btnLogin:
                finish();
                break;

            default:
                break;
        }
    }

}
