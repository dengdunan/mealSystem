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
 * 选择座位和点餐的份数
 *
 * @author dda
 */
public class PlaceOrderActivity extends BaseActivity {
    //定义一个iD值
    private int id = 0;
    //图片异步加载器
    private AsyncImageLoader asyncImageLoader;
    //服务器地址
    private String serverUrl;
    //有六列座位
    private final int columns = 6;
    //顶端标题栏的左边的按钮和右边的按钮
    private Button btnTopTitleRight, btnTopTitleLeft;
    //线性布局
    private LinearLayout llSeat;
    //HashMap集合
    private HashMap<Integer, Integer> hashMap;
    //份数
    private TextView tvSel;
    //需要的份数
    private EditText etAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        //初始化通用应用设置
        application = (com.mm.mealapp.activity.CommonApplication) getApplicationContext();
        //初始化视图
        findview();
        //初始化异步图片加载器
        asyncImageLoader = new AsyncImageLoader(
                BitmapFactory.decodeResource(getResources(), R.drawable.pc_loading_fali));
        //初始化服务器端地址
        serverUrl = AppConstant.getRootUrl(this);
        //初始化HashMap集合
        hashMap = new HashMap<Integer, Integer>();
        /**
         * 判断传到该页面的Intent是否为null，不为null，则执行异步加载任务
         */
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            new loadAsyncTask().execute(id + "");

        }

    }

    /**
     * 初始化视图
     */
    private void findview() {
        //设置顶端中间文本内容
        ((TextView) findViewById(R.id.tvTopTitleCenter)).setText("选择座位");

        //设置顶端右边按钮文本内容并且让它显示，且设置监听器
        btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
        btnTopTitleRight.setText("提交");
        btnTopTitleRight.setVisibility(View.VISIBLE);
        btnTopTitleRight.setOnClickListener(this);

        //设置顶端左边按钮文本内容并且让它显示，且设置监听器
        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        btnTopTitleLeft.setOnClickListener(this);
        btnTopTitleLeft.setText("返回");
        //初始化llSeat
        llSeat = (LinearLayout) findViewById(R.id.llSeat);
        //初始化tvSel
        tvSel = (TextView) findViewById(R.id.tvSel);
        //初始化etAmount
        etAmount = (EditText) findViewById(R.id.etAmount);
    }

    /**
     * 该异步任务处理需要送餐的座位及其送餐的份数
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //初始化一个进度条对话框，对话框的标题为提示，内容为获取中，请稍后
            dialog = ProgressDialog.show(PlaceOrderActivity.this, "提示", "获取中,请稍后..");
        }

        //后台执行操作，发送一个地址，回传得到服务器的数据
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
            //提示对话框消失
            dialog.dismiss();
            //判断从服务器返回的数据是否长度是否大于0
            if (result.trim().length() > 0) {
                try {
                    //对返回的json格式数据进行解析
                    jsonArray = new JSONArray(result);
                    LinearLayout ll = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //当i的值%6取余为0时，实例化线性布局，并设置线性布局方向和位置
                        if (i % 6 == 0) {
                            ll = new LinearLayout(PlaceOrderActivity.this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setGravity(Gravity.CENTER);
                        }
                        //实例化ImageView(座位)，并设置相关ImageView的状态
                        final ImageView imageView = new ImageView(PlaceOrderActivity.this);
                        imageView.setPadding(5, 5, 5, 5);
                        imageView.setTag(jsonObject.getInt("id") + "," + jsonObject.getInt("state") + ",0");
                        //如果从Json个数解析出的状态值为0，则设置座位可选，否则则座位已被占用
                        if (jsonObject.getInt("state") == 0) {
                            imageView.setImageResource(R.drawable.seat_unavailable);
                        } else {
                            imageView.setImageResource(R.drawable.seat_sel);
                        }

                        //设置图片(座位)监听器
                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //获取状态值，id值和需要的份数
                                String tag = v.getTag().toString();
                                String id = tag.split(",")[0];
                                String state = tag.split(",")[1];// 是否被选择
                                String sel = tag.split(",")[2];// 是否选中
                                //如果状态值是1，则弹出吐司已被选；如果用户选中座位，则进行图片替换等处理；如果sel值为1，则座位可选
                                if ("1".equals(state)) {
                                    toastUtil.show("该座位已被选");
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
                        //将imageView加入ll中
                        ll.addView(imageView);
                        //如果i能被6整除，则将ll加入llSeat布局中
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
     * 设置已经选择的座位
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
        tvSel.setText("已选择:" + sel);
    }

    /**
     * 处理按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //顶端右键按钮进行下单操作
            case R.id.btnTopTitleRight:
                if (TextUtils.isEmpty(tvSel.getText().toString().replace("已选择:", ""))) {
                    toastUtil.show("至少选择一个座位");
                } else {
                    new submitAsyncTask().execute();
                }

                break;
            //顶端左键按钮进行返回操作
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 提交异步处理任务，进行提交订单的操作
     */
    @SuppressWarnings("deprecation")
    private class submitAsyncTask extends AsyncTask<String, Integer, String> {

        //初始化一个进度条对话框，对话框的标题为提示，内容为处理中，请稍后
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PlaceOrderActivity.this, "提示", "处理中,请稍后..");
        }

        //后台提交数据
        @Override
        protected String doInBackground(String... params) {
            //设置Servlet地址
            String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
            //将值加入map集合中
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Action", "createorder");
            map.put("dishesid", id);
            map.put("userid", user.getId());
            map.put("username", user.getName());
            map.put("amount", etAmount.getText());
            map.put("seat", tvSel.getText().toString().replace("已选择:", ""));
            //将数据发送至服务器请求回来的数据
            String result = httpHelper.HttpPost(urlString, map);
            return result;
        }

        //订单提交的状态是否成功
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            BaseUtil.LogII("result  " + result);
            //提示对话框消失
            dialog.dismiss();
            //如果从服务器返回的数据不为空，且为1，则订单提交成功，并跳转界面，否则弹出失败
            if (result != null && result.trim().equals("1")) {
                toastUtil.show("订单提交成功");
                intent = new Intent(PlaceOrderActivity.this, MyOrdersListActivity.class);
                startActivity(intent);
                finish();
            } else {
                toastUtil.show("订单提交失败");
            }
        }
    }

}
