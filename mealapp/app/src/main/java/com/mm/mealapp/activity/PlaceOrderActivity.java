package com.mm.mealapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miebo.utils.AsyncImageLoader;
import com.mm.mealapp.utils.BaseActivity;
import com.miebo.utils.BaseUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ѡ����λ�͵�͵ķ���
 *
 * @author dda
 */
public class PlaceOrderActivity extends BaseActivity {
    //����һ��iDֵ
    private int id = 0;
    //ͼƬ�첽������
    private AsyncImageLoader asyncImageLoader;
    //��������ַ
    private String serverUrl;
    //��������λ
    private final int columns = 6;
    //���˱���������ߵİ�ť���ұߵİ�ť
    private Button btnTopTitleRight, btnTopTitleLeft;
    //���Բ���
    private LinearLayout llSeat;
    //HashMap����
    private HashMap<Integer, Integer> hashMap;
    //����
    private TextView tvSel;
    //��Ҫ�ķ���
    private EditText etAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        //��ʼ��ͨ��Ӧ������
        application = (com.mm.mealapp.activity.CommonApplication) getApplicationContext();
        //��ʼ����ͼ
        findview();
        //��ʼ���첽ͼƬ������
        asyncImageLoader = new AsyncImageLoader(
                BitmapFactory.decodeResource(getResources(), R.drawable.pc_loading_fali));
        //��ʼ���������˵�ַ
        serverUrl = AppConstant.getRootUrl(this);
        //��ʼ��HashMap����
        hashMap = new HashMap<Integer, Integer>();
        /**
         * �жϴ�����ҳ���Intent�Ƿ�Ϊnull����Ϊnull����ִ���첽��������
         */
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            new loadAsyncTask().execute(id + "");

        }

    }

    /**
     * ��ʼ����ͼ
     */
    private void findview() {
        //���ö����м��ı�����
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("ѡ����λ");

        //���ö����ұ߰�ť�ı����ݲ���������ʾ�������ü�����
        btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
        btnTopTitleRight.setText("�ύ");
        btnTopTitleRight.setVisibility(View.VISIBLE);
        btnTopTitleRight.setOnClickListener(this);

        //���ö�����߰�ť�ı����ݲ���������ʾ�������ü�����
        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        btnTopTitleLeft.setOnClickListener(this);
        btnTopTitleLeft.setText("����");
        //��ʼ��llSeat
        llSeat = (LinearLayout) findViewById(R.id.llSeat);
        //��ʼ��tvSel
        tvSel = (TextView) findViewById(R.id.tvSel);
        //��ʼ��etAmount
        etAmount = (EditText) findViewById(R.id.etAmount);
    }

    /**
     * ���첽��������Ҫ�Ͳ͵���λ�����Ͳ͵ķ���
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊ��ȡ�У����Ժ�
            dialog = ProgressDialog.show(PlaceOrderActivity.this, "��ʾ", "��ȡ��,���Ժ�..");
        }

        //��ִ̨�в���������һ����ַ���ش��õ�������������
        @Override
        protected String doInBackground(String... params) {
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getOneRow";
            urlString = urlString + "&Table=seats";
            String json = httpHelper.HttpRequest(urlString);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //�жϴӷ��������ص������Ƿ񳤶��Ƿ����0
            if (result.trim().length() > 0) {
                try {
                    //�Է��ص�json��ʽ���ݽ��н���
                    jsonArray = new JSONArray(result);
                    LinearLayout ll = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //��i��ֵ%6ȡ��Ϊ0ʱ��ʵ�������Բ��֣����������Բ��ַ����λ��
                        if (i % 6 == 0) {
                            ll = new LinearLayout(PlaceOrderActivity.this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setGravity(Gravity.CENTER);
                        }
                        //ʵ����ImageView(��λ)�����������ImageView��״̬
                        final ImageView imageView = new ImageView(PlaceOrderActivity.this);
                        imageView.setPadding(5, 5, 5, 5);
                        imageView.setTag(jsonObject.getInt("id") + "," + jsonObject.getInt("state") + ",0");
                        //�����Json������������״ֵ̬Ϊ0����������λ��ѡ����������λ�ѱ�ռ��
                        if (jsonObject.getInt("state") == 0) {
                            imageView.setImageResource(R.drawable.seat_unavailable);
                        } else {
                            imageView.setImageResource(R.drawable.seat_sel);
                        }

                        //����ͼƬ(��λ)������
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //��ȡ״ֵ̬��idֵ����Ҫ�ķ���
                                String tag = v.getTag().toString();
                                String id = tag.split(",")[0];
                                String state = tag.split(",")[1];// �Ƿ�ѡ��
                                String sel = tag.split(",")[2];// �Ƿ�ѡ��
                                //���״ֵ̬��1���򵯳���˾�ѱ�ѡ������û�ѡ����λ�������ͼƬ�滻�ȴ������selֵΪ1������λ��ѡ
                                if ("1".equals(state)) {
                                    toastUtil.show("����λ�ѱ�ѡ");
                                } else {
                                    if ("1".equals(sel)) {
                                        imageView.setImageResource(R.drawable.seat_unavailable);
                                        imageView.setTag(id + "," + state + "," + "0");
                                        hashMap.remove(Integer.valueOf(id));
                                        setSel();
                                    } else {
                                        imageView.setImageResource(R.drawable.seat_selected);
                                        imageView.setTag(id + "," + state + "," + "1");
                                        hashMap.put(Integer.valueOf(id), Integer.valueOf(id));
                                        setSel();
                                    }
                                }
                            }
                        });
                        //��imageView����ll��
                        ll.addView(imageView);
                        //���i�ܱ�6��������ll����llSeat������
                        if (i % 6 == 0) {
                            llSeat.addView(ll);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /*
     *
     * �����Ѿ�ѡ�����λ
     */
    private void setSel() {
        String sel = "";
        Iterator iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            sel += hashMap.get(iterator.next()) + ",";
        }
        if (sel.length() > 0) {
            sel = sel.substring(0, sel.length() - 1);
        }
        tvSel.setText("��ѡ��:" + sel);
    }

    /**
     * ����ť�ĵ���¼�
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //�����Ҽ���ť�����µ�����
            case R.id.btnTopTitleRight:
                if (TextUtils.isEmpty(tvSel.getText().toString().replace("��ѡ��:", ""))) {
                    toastUtil.show("����ѡ��һ����λ");
                } else {
                    new submitAsyncTask().execute();
                }

                break;
            //���������ť���з��ز���
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * �ύ�첽�������񣬽����ύ�����Ĳ���
     */
    @SuppressWarnings("deprecation")
    private class submitAsyncTask extends AsyncTask<String, Integer, String> {

        //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊ�����У����Ժ�
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PlaceOrderActivity.this, "��ʾ", "������,���Ժ�..");
        }

        //��̨�ύ����
        @Override
        protected String doInBackground(String... params) {
            //����Servlet��ַ
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
            //��ֵ����map������
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Action", "createorder");
            map.put("dishesid", id);
            map.put("userid", user.getId());
            map.put("username", user.getName());
            map.put("amount", etAmount.getText());
            map.put("seat", tvSel.getText().toString().replace("��ѡ��:", ""));
            //�����ݷ������������������������
            String result = httpHelper.HttpPost(urlString, map);
            return result;
        }

        //�����ύ��״̬�Ƿ�ɹ�
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            BaseUtil.LogII("result  " + result);
            //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //����ӷ��������ص����ݲ�Ϊ�գ���Ϊ1���򶩵��ύ�ɹ�������ת���棬���򵯳�ʧ��
            if (result != null && result.trim().equals("1")) {
                toastUtil.show("�����ύ�ɹ�");
                intent = new Intent(PlaceOrderActivity.this, MyOrdersListActivity.class);
                startActivity(intent);
                finish();
            } else {
                toastUtil.show("�����ύʧ��");
            }
        }
    }

}
