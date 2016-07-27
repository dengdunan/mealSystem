package com.mm.mealapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.mealapp.utils.BaseActivity;
import com.mm.mealapp.adapter.OrdersAdapter;
import com.mm.mealapp.api.orders;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * �ҵĶ�������
 *
 * @author dda
 */
public class MyOrdersListActivity extends BaseActivity {
    //������߰�ť
    private Button btnTopTitleLeft;
    //����
    private List<orders> list;
    //����������
    private OrdersAdapter adapter;
    //�б�
    private ListView listview1;
    //�����м���ı�
    private TextView tvTopTitleCenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //��ʼ����ͼ
        findview();
        //�첽��������
        new loadAsyncTask().execute();
    }

    /**
     * ��ʼ����ͼ
     */
    private void findview() {
        //���ö����м���ı�������
        tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
        tvTopTitleCenter.setText("�ҵĶ���");

        //���ö�����߰�ť��ʾ�����ü������������ı�����
        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        btnTopTitleLeft.setOnClickListener(this);
        btnTopTitleLeft.setText("����");

        //�����б�ļ�����
        listview1 = (ListView) findViewById(R.id.listview1);
        listview1.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //��ʾ�������ݵĶԻ���
                showContactDialog(position);
                return true;
            }

        });

    }

    /**
     * �ü��ص��첽������Ҫ�����ҵĶ�����������ʾ
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊ��ȡ��
            dialog = ProgressDialog.show(MyOrdersListActivity.this, "��ʾ", "��ȡ��..");
        }

        //��ִ̨�в���������һ����ַ���ش��õ�������������
        @Override
        protected String doInBackground(String... params) {
            String json = null;

            serverUrl = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getmyorderslist&userid="
                    + user.getId();
            json = httpHelper.HttpRequest(serverUrl);
            return json;
        }

        //��̨����ִ����󣬻ش����ݺ󣬶Ե�ǰ�ҵĶ���������и���
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //ʵ��������
            list = new ArrayList<orders>();
            //�жϴӷ��������ص������Ƿ�Ϊ���ҳ����Ƿ����0
            if (result != null && result.trim().length() > 0) {
                try {
                    //�Է��ص�json��ʽ���ݽ��н���
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //ʵ����һ��ordersʵ����
                        orders model = new orders();
                        //����json�з��ص�ֵ�������ý�ʵ������
                        model.setId(jsonObject.getInt("id"));
                        model.setAmount(jsonObject.getDouble("amount"));
                        model.setCreatetime(jsonObject.getString("createtime"));
                        model.setSeat(jsonObject.getString("seat"));
                        model.setPrice(jsonObject.getDouble("price"));
                        model.setUsername(jsonObject.getString("title"));
                        model.setImg_url(jsonObject.getString("img_url"));
                        model.setStatus(jsonObject.getInt("status"));
                        //��ʵ�����������ݼ��뵽�����б���
                        list.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //���û�з������ݣ��򵯳�һ����˾
                toastUtil.show("û������");
            }
            //��ʼ��һ��������������
            adapter = new OrdersAdapter(MyOrdersListActivity.this, list);
            //�б���ʾ�������еĴ���������
            listview1.setAdapter(adapter);
        }
    }

    // ���������Ĳ˵��������Ի����Ƿ���Ҫȡ������
    private void showContactDialog(final int position) {
        String[] arg = new String[]{"ȡ������"};
        new AlertDialog.Builder(this).setTitle("ѡ�����").setItems(arg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:// ȡ��
                        new deleteAsyncTask().execute(list.get(position).getId() + "");
                }
            }
        }).show();
    }

    /**
     * ɾ���������첽��������
     */
    private class deleteAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊȡ���У����Ժ�..
            dialog = ProgressDialog.show(MyOrdersListActivity.this, "��ʾ", "ȡ����,���Ժ�..");
        }

        //��ִ̨�в���������һ����ַ���ش��õ�������������
        @Override
        protected String doInBackground(String... params) {
            serverUrl = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=cancelOrders&ID="
                    + params[0];
            String json = httpHelper.HttpRequest(serverUrl);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //�жϴӷ��������ص������Ƿ�Ϊ���ҳ����Ƿ����0��������Ӧ�Ĵ���
            if (result != null && result.trim().length() > 0) {
                toastUtil.show("ȡ���ɹ�");
                new loadAsyncTask().execute();
            } else {
                toastUtil.show("ȡ��ʧ��");

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
            // //����������Ͻǵİ�ť���з��ز���
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }
    }
}
