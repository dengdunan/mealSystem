package com.mm.mealapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.mealapp.utils.BaseActivity;
import com.mm.mealapp.adapter.DishesAdapter;
import com.mm.mealapp.api.dishes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * ����б�,��ʾȫ������
 *
 * @author dda
 */
public class DishesListActivity extends BaseActivity {
    //���˱���������ߵİ�ť
    private Button btnTopTitleLeft;
    //���˱��������ұߵİ�ť
    private Button btnTopTitleRight;
    //���õ���б�
    private List<dishes> list;
    //���õ���б��������
    private DishesAdapter adapter;
    //�б�
    private ListView listview1;
    //���˱������м���ı�
    private TextView tvTopTitleCenter;
    //�ؼ���
    private String keyword = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����activity_list����
        setContentView(R.layout.activity_list);
        //��ʼ����ͼ
        findview();
        //ʵ����һ�����ص��첽���񣬲�ִ��
        new loadAsyncTask().execute(keyword);
    }

    private void findview() {
        tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
        //���ö����ı�����Ϊ���״�ȫ
        tvTopTitleCenter.setText("���״�ȫ");

        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        //���ö�����߰�ť��ʾ
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        ////���ö�����߰�ť�ļ����¼�
        btnTopTitleLeft.setOnClickListener(this);
        //���ö�����߰�ť���ı�����Ϊ����
        btnTopTitleLeft.setText("����");

        listview1 = (ListView) findViewById(R.id.listview1);

    }

    /**
     * �ü��ص��첽������Ҫ������ײ˵�����ʾ
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {

        //ִ��ǰ�Ĳ���
        @Override
        protected void onPreExecute() {
            //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊ��ȡ��
            dialog = ProgressDialog.show(DishesListActivity.this, "��ʾ", "��ȡ��..");
        }

        //��ִ̨�в���������һ����ַ���ش��õ�������������
        @Override
        protected String doInBackground(String... params) {
            //����һ���ַ���json�������ʼ��Ϊ��
            String json = null;

            //��ȡ�����UrlservletĿ¼�ĵ�ַ
            serverUrl = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getdisheslist&msg="
                    + params[0];

            //�Ե�ǰ�ĵ�ַ�������󣬲�����һ��jsonֵ,json��̨�����ص�����
            json = httpHelper.HttpRequest(serverUrl);

            return json;
        }

        //��̨����ִ����󣬶Ե�ǰ���ײ˵�������и���
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //��ʾ�Ի�����ʧ
            dialog.dismiss();
            //ʵ���������б�
            list = new ArrayList<dishes>();
            //�жϴӷ��������ص������Ƿ�Ϊ���ҳ����Ƿ����0
            if (result != null && result.trim().length() > 0) {
                try {
                    //�Է��ص�json��ʽ���ݽ��н���
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //ʵ����һ��dishesʵ����
                        dishes model = new dishes();
                        //����json�з��ص�ֵ�������ý�ʵ������
                        model.setId(jsonObject.getInt("id"));
                        model.setIntro(jsonObject.getString("intro"));
                        model.setImg_url(jsonObject.getString("img_url"));
                        model.setTitle(jsonObject.getString("title"));
                        model.setPrice(jsonObject.getDouble("price"));
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
            //��ʼ��һ�����׵�������
            adapter = new DishesAdapter(DishesListActivity.this, list);
            //�б���ʾ�������еĴ���������
            listview1.setAdapter(adapter);
        }
    }

    /**
     * ��������activity����ǰ��activity�������ݴ��䴦��
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //���������ͽ���붼Ϊ1����ִ���첽�������
        if (requestCode == 1 && resultCode == 1) {
            new loadAsyncTask().execute(tvTopTitleCenter.getText().toString(), keyword);
        }
    }

    /**
     * ʵ����������
     */
    private void search() {
        final EditText eText = new EditText(this);
        //����һ���Ի��򣬿��ڶԻ������������ݣ������������������ǰ�������Ƿ���ڸ�ֵ
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("����").setIcon(android.R.drawable.ic_dialog_info)
                .setView(eText).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //��ȡ�ؼ��ֵ�ֵ
                        keyword = eText.getText().toString();
                        //ִ�������ؼ��ֵĲ���
                        new loadAsyncTask().execute(keyword);
                    }

                }).setNegativeButton("ȡ��", null).create();
        dialog.show();
    }

    /**
     * ʵ����ѡ��˵��Ĺ���
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //��3��ѡ�������menu��
        menu.add(0, 100, 0, "�ҵĶ���").setIcon(R.drawable.icon_application);
        menu.add(0, 101, 0, "�޸�����").setIcon(R.drawable.icon_application);
        menu.add(0, 102, 0, "�˳�").setIcon(R.drawable.icon_application);
        return true;
    }

    /**
     * �ж���ѡ�����ѡ��˵��ϵ���һ��
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            //������ҵĶ�������ת��MyOrdersListActivity
            case 100:
                intent = new Intent(DishesListActivity.this, MyOrdersListActivity.class);
                startActivity(intent);
                break;
            //������޸����룬��ת��UpdatePwdActivity
            case 101:
                intent = new Intent(DishesListActivity.this, UpdatePwdActivity.class);
                startActivity(intent);
                break;
            //������˳���ֱ���˳�����
            case 102:
                finish();
                System.exit(0);
                break;
        }
        return false;
    }

    /**
     * ����ť�ĵ���¼�
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTopTitleRight:

                break;
            //����������Ͻǵİ�ť������������
            case R.id.btnTopTitleLeft:
                search();
                break;

            default:
                break;
        }

    }

}
