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
 * 开始类，设置ip地址类，首次测试需要对服务端的地址及其端口进行测试；第二次进入则直接跳转至登陆界面。
 */

public class StartActivity extends BaseActivity {
    //确认按钮
    private Button btnOK;
    //输入IP地址文本框
    private EditText etIP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences封装类SPUtils，第一次对端口进行设置，第二次直接跳转
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
     * 初始化视图，实例化各个控件
     */
    private void findview() {
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("配置IP地址");
        btnOK = (Button) findViewById(R.id.btnOK);
        etIP = (EditText) findViewById(R.id.etIP);
        btnOK.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			//当点击了确认按钮,判断输入文本框是否为空,且IP格式是否正确，正确即可跳转界面
            case R.id.btnOK:
                if (etIP.getText().length() == 0) {
                    toastUtil.show("请输入IP地址");
                    return;
                }
                if (etIP.getText().length() < 12) {
                    toastUtil.show("IP地址格式错误");
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
