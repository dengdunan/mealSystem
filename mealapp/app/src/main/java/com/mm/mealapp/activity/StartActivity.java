package com.mm.mealapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mm.mealapp.utils.BaseActivity;
import com.miebo.utils.SPUtil;

/*
 * ��ʼ�࣬����ip��ַ�࣬�״β�����Ҫ�Է���˵ĵ�ַ����˿ڽ��в��ԣ��ڶ��ν�����ֱ����ת����½���档
 */

public class StartActivity extends BaseActivity {
    //ȷ�ϰ�ť
    private Button btnOK;
    //����IP��ַ�ı���
    private EditText etIP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences��װ��SPUtils����һ�ζԶ˿ڽ������ã��ڶ���ֱ����ת
        if (SPUtil.get(this, "IP", "").length() > 0) {
            intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_start);
            findview();
        }

    }

    /**
     * ��ʼ����ͼ��ʵ���������ؼ�
     */
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("����IP��ַ");
        btnOK = (Button) findViewById(R.id.btnOK);
        etIP = (EditText) findViewById(R.id.etIP);
        btnOK.setOnClickListener(this);
    }

    /**
     * ����¼�
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			//�������ȷ�ϰ�ť,�ж������ı����Ƿ�Ϊ��,��IP��ʽ�Ƿ���ȷ����ȷ������ת����
            case R.id.btnOK:
                if (etIP.getText().length() == 0) {
                    toastUtil.show("������IP��ַ");
                    return;
                }
                if (etIP.getText().length() < 12) {
                    toastUtil.show("IP��ַ��ʽ����");
                    return;
                }

                SPUtil.set(StartActivity.this, "IP", etIP.getText().toString());
                intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }

    }
}
