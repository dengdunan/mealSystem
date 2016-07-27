package com.mm.mealapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miebo.utils.AsyncImageLoader;
import com.mm.mealapp.utils.BaseActivity;;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * ��Ʒ�������
 *
 * @author zlus
 */
public class GoodDetailActivity extends BaseActivity {

    //����һ��iDֵ
    private int id = 0;
    //����һ��ͼƬ
    private ImageView imageView1;
    //����һ���첽ͼƬ������
    private AsyncImageLoader asyncImageLoader;
    //����һ����������ַ
    private String serverUrl;
    //����һ��������Ϣ
    private TextView tvIntro;
    //����һ�������ұ߰�ť
    private Button btnTopTitleRight;
    //����һ��������߰�ť
    private Button btnTopTitleLeft;
    //����һ��ͨ��Ӧ������
    private com.mm.mealapp.activity.CommonApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���õ�ǰactivity����
        setContentView(R.layout.activity_gooddetail);
        //ʵ����Ӧ������
        application = (com.mm.mealapp.activity.CommonApplication) getApplicationContext();
        //��ʼ����ͼ
        findview();
        //�첽����ͼƬ
        asyncImageLoader = new AsyncImageLoader(
                BitmapFactory.decodeResource(getResources(), R.drawable.pc_loading_fali));
        //�����Url��Ŀ¼��ַ
        serverUrl = AppConstant.getRootUrl(this);
        //�жϴӲ��׽���������������Ϣ�Ƿ�Ϊ�գ���Ϊ�տɻ�ȡ��ǰ�����id,ʵ����һ���첽���񣬲�ִ��
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            new loadAsyncTask().execute(id + "");
        }

    }

    /**
     * ��ʼ����ͼ
     */
    private void findview() {

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        tvIntro = (TextView) findViewById(R.id.tvIntro);
        btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
        //���ö����ұߵİ�ť���ı�����Ϊ�µ�
        btnTopTitleRight.setText("�µ�");
        //���ö����ұߵİ�ť��ʾ
        btnTopTitleRight.setVisibility(View.VISIBLE);
        //���ö����ұߵİ�ť�ļ����¼�
        btnTopTitleRight.setOnClickListener(this);

        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        //���ö�����ߵİ�ť��ʾ
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        //���ö�����ߵİ�ť�ļ����¼�
        btnTopTitleLeft.setOnClickListener(this);
        //���ö�����ߵİ�ť���ı�����Ϊ����
        btnTopTitleLeft.setText("����");
    }

    /**
     * �ü��ص��첽������Ҫ�����������Ʒ����������
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //��ʼ��һ���������Ի��򣬶Ի���ı���Ϊ��ʾ������Ϊ��ȡ��
            dialog = ProgressDialog.show(GoodDetailActivity.this, "��ʾ", "��ȡ��,���Ժ�..");
        }

        @Override
        protected String doInBackground(String... params) {
            //��ȡ�����UrlservletĿ¼�ĵ�ַ
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getOneRow";
            urlString = urlString + "&ID=" + params[0] + "&Table=dishes";
            //��ȡ�ӷ��������ص���Ϣ
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
                    jsonObject = jsonArray.getJSONObject(0);

                    //���ö��˱����м���ı����ݣ�������Ϊ16sp
                    ((TextView) findViewById(R.id.tvTopTitleCenter)).setText(jsonObject.getString("title"));
                    ((TextView) findViewById(R.id.tvTopTitleCenter)).setTextSize(16);
                    //���ͼƬ��ַ��Ϊ�գ������ø�ͼƬ
                    if (!TextUtils.isEmpty(jsonObject.getString("img_url"))) {
                        asyncImageLoader.loadBitmap(serverUrl + "UploadFile/" + jsonObject.getString("img_url"),
                                imageView1);
                    }
                    //���õ�ǰͼƬ���������
                    String intro = "����:��" + jsonObject.getString("price") + "\n";
                    intro += "����:" + jsonObject.getString("amount") + "\n";
                    intro += "���:" + jsonObject.getString("intro");
                    tvIntro.setText(intro);

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
            //������˰�ť�����ұߵİ�ť�����͵�ǰid��PlaceOrderActivity��
            case R.id.btnTopTitleRight:
                intent = new Intent(GoodDetailActivity.this, PlaceOrderActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 1);
                break;
            //������˰�ť������ߵİ�ť
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }

    }

}
