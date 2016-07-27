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
 * ��¼����
 *
 * @author dda
 */
public class LoginActivity extends BaseActivity {

    //��������ע�ᰴť����½��ť
    private Button btnLogin, btnRegister;
    //���������û�ID�ı���������ı���
    private EditText etLoginID, etPassword;
    //�����ѡ���Ƿ��ס����
    private CheckBox ckbSavePwd;
    //http����
    private HttpUtil httpHelper;
    //���ȶԻ���
    private ProgressDialog dialog;
    //�첽���ص�¼����
    private loadAsyncTask loginAsyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //��ʼ������
        findview();
        //���ü�����
        setListener();
        //ʵ����http���ߣ���Ԥ���û�������
        httpHelper = new HttpUtil();
        etLoginID.setText("test00");
        etPassword.setText("111111");
    }

    //����IP��ַ
    public void configIP(View source) {
        //ʹ��SPUtil��������IP
        SPUtil.set(this, "IP", "");
        //��ת������StartActivity
        intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.setLoginUser(null);
    }

    //��ʼ�����棬ʵ���������ؼ������¼��ע�ᣬ����ȿؼ�
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("��¼");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ckbSavePwd = (CheckBox) findViewById(R.id.ckbSavePwd);

    }

    //���õ�¼��������ע��������������û��Ŀ�������ı��������
    private void setListener() {
        btnLogin.setOnClickListener(new btnLoginOnClickListener());
        etLoginID.setText(SPUtil.get(LoginActivity.this, "loginid", ""));
        etPassword.setText(SPUtil.get(LoginActivity.this, "password", ""));
        btnRegister.setOnClickListener(this);
    }

    //��¼������
    @SuppressWarnings("unchecked")
    private class btnLoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            //�û�����Ϊ0�������������ʾ
            if (etLoginID.getText().length() == 0) {
                toastUtil.show("�������˺�");
                return;
            }
            //���볤��Ϊ0�������������ʾ
            if (etPassword.getText().length() == 0) {
                toastUtil.show("����������");
                return;
            }
            //���ؼ���
            BaseUtil.HideKeyboard(LoginActivity.this);
            //ʵ�����첽��¼����
            loginAsyncTask = new loadAsyncTask();
            loginAsyncTask.execute("");

        }
    }

    ;

    //�첽���ص�¼����
    @SuppressWarnings("deprecation")
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //ʵ�����������Ի���
            dialog = new ProgressDialog(LoginActivity.this);
            //���ñ���
            dialog.setTitle("��ʾ");
            //������Ϣ
            dialog.setMessage("��¼��,���Ժ�..");
            //����ȡ������Ϊtrue
            dialog.setCancelable(true);
            //���ð�ť
            dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //���loginAsyncTask��Ϊ�գ�������Ϊ�գ���������˾
                    if (loginAsyncTask != null) {
                        loginAsyncTask.cancel(true);
                        loginAsyncTask = null;
                        toastUtil.show("��¼��ȡ��");
                    }
                }
            });
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // //��ȡ�����UrlservletĿ¼�ĵ�ַ
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=login";
            urlString += "&loginid=" + etLoginID.getText() + "&passwords=" + etPassword.getText();
            //��ȡ�ӷ��������ص���Ϣ
            String json = httpHelper.HttpRequest(urlString);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loginAsyncTask = null;
            // //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //�ж��Ƿ��¼�ɹ����������ֵ����Ϊ0���¼ʧ�ܣ�������ʾ��¼�ɹ����������û���¼��Ϣ
            if (result.trim().length() == 0) {
                toastUtil.show("��¼ʧ��");
                return;
            } else {
                //�����¼��
                SPUtil.set(LoginActivity.this, "loginid", etLoginID.getText().toString());
                //����������밴ť��ѡ�У��򱣴����룻���򱣴������
                if (ckbSavePwd.isChecked()) {
                    SPUtil.set(LoginActivity.this, "password", etPassword.getText().toString());
                } else {
                    SPUtil.set(LoginActivity.this, "password", "");
                }
                try {
                    //����json����
                    jsonArray = new JSONArray(result);
                    jsonObject = jsonArray.getJSONObject(0);
                    // �����¼�û���Ϣ
                    CommonApplication application = (CommonApplication) getApplicationContext();
                    OnLineUser model = new OnLineUser();
                    model.setId(jsonObject.getInt("id"));
                    model.setLoginid(etLoginID.getText().toString());
                    model.setName(jsonObject.getString("name"));
                    application.setLoginUser(model);
                    //������˾����¼�ɹ�
                    toastUtil.show(model.getName() + ",��¼�ɹ�");
                    //��ת������DishesListActivity
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
     * ����ť�ĵ���¼�
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //�����ע�ᰴť����ת��ע��ҳ��
            case R.id.btnRegister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

}
