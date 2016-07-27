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
 * 商品详情界面
 *
 * @author zlus
 */
public class GoodDetailActivity extends BaseActivity {

    //定义一个iD值
    private int id = 0;
    //定义一个图片
    private ImageView imageView1;
    //定义一个异步图片加载器
    private AsyncImageLoader asyncImageLoader;
    //定义一个服务器地址
    private String serverUrl;
    //定义一个介绍信息
    private TextView tvIntro;
    //定义一个顶端右边按钮
    private Button btnTopTitleRight;
    //定义一个顶端左边按钮
    private Button btnTopTitleLeft;
    //定义一个通用应用设置
    private com.mm.mealapp.activity.CommonApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置当前activity布局
        setContentView(R.layout.activity_gooddetail);
        //实例化应用设置
        application = (com.mm.mealapp.activity.CommonApplication) getApplicationContext();
        //初始化视图
        findview();
        //异步加载图片
        asyncImageLoader = new AsyncImageLoader(
                BitmapFactory.decodeResource(getResources(), R.drawable.pc_loading_fali));
        //服务端Url根目录地址
        serverUrl = AppConstant.getRootUrl(this);
        //判断从菜谱界面所传过来的信息是否为空，不为空可获取当前点击的id,实例化一个异步任务，并执行
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            new loadAsyncTask().execute(id + "");
        }

    }

    /**
     * 初始化视图
     */
    private void findview() {

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        tvIntro = (TextView) findViewById(R.id.tvIntro);
        btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
        //设置顶端右边的按钮的文本内容为下单
        btnTopTitleRight.setText("下单");
        //设置顶端右边的按钮显示
        btnTopTitleRight.setVisibility(View.VISIBLE);
        //设置顶端右边的按钮的监听事件
        btnTopTitleRight.setOnClickListener(this);

        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        //设置顶端左边的按钮显示
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        //设置顶端左边的按钮的监听事件
        btnTopTitleLeft.setOnClickListener(this);
        //设置顶端左边的按钮的文本内容为返回
        btnTopTitleLeft.setText("返回");
    }

    /**
     * 该加载的异步任务主要处理菜谱中商品的详情内容
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //初始化一个进度条对话框，对话框的标题为提示，内容为获取中
            dialog = ProgressDialog.show(GoodDetailActivity.this, "提示", "获取中,请稍后..");
        }

        @Override
        protected String doInBackground(String... params) {
            //获取服务端Urlservlet目录的地址
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getOneRow";
            urlString = urlString + "&ID=" + params[0] + "&Table=dishes";
            //获取从服务器返回的信息
            String json = httpHelper.HttpRequest(urlString);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //提示对话框消失
            dialog.dismiss();
            //判断从服务器返回的数据是否长度是否大于0
            if (result.trim().length() > 0) {
                try {
                    //对返回的json格式数据进行解析
                    jsonArray = new JSONArray(result);
                    jsonObject = jsonArray.getJSONObject(0);

                    //设置顶端标题中间的文本内容，且字体为16sp
                    ((TextView) findViewById(R.id.tvTopTitleCenter)).setText(jsonObject.getString("title"));
                    ((TextView) findViewById(R.id.tvTopTitleCenter)).setTextSize(16);
                    //如果图片地址不为空，则设置该图片
                    if (!TextUtils.isEmpty(jsonObject.getString("img_url"))) {
                        asyncImageLoader.loadBitmap(serverUrl + "UploadFile/" + jsonObject.getString("img_url"),
                                imageView1);
                    }
                    //设置当前图片的详情介绍
                    String intro = "单价:￥" + jsonObject.getString("price") + "\n";
                    intro += "数量:" + jsonObject.getString("amount") + "\n";
                    intro += "简介:" + jsonObject.getString("intro");
                    tvIntro.setText(intro);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
            //当点击了按钮顶端右边的按钮，发送当前id到PlaceOrderActivity中
            case R.id.btnTopTitleRight:
                intent = new Intent(GoodDetailActivity.this, PlaceOrderActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 1);
                break;
            //当点击了按钮顶端左边的按钮
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }

    }

}
