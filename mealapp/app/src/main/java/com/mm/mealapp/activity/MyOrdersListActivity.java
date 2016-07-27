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
 * 我的订单界面
 *
 * @author dda
 */
public class MyOrdersListActivity extends BaseActivity {
    //顶端左边按钮
    private Button btnTopTitleLeft;
    //订单
    private List<orders> list;
    //订单适配器
    private OrdersAdapter adapter;
    //列表
    private ListView listview1;
    //顶端中间的文本
    private TextView tvTopTitleCenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //初始化视图
        findview();
        //异步加载任务
        new loadAsyncTask().execute();
    }

    /**
     * 初始化视图
     */
    private void findview() {
        //设置顶端中间的文本的内容
        tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
        tvTopTitleCenter.setText("我的订单");

        //设置顶端左边按钮显示并设置监听器且设置文本内容
        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        btnTopTitleLeft.setOnClickListener(this);
        btnTopTitleLeft.setText("返回");

        //设置列表的监听器
        listview1 = (ListView) findViewById(R.id.listview1);
        listview1.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //显示订单内容的对话框
                showContactDialog(position);
                return true;
            }

        });

    }

    /**
     * 该加载的异步任务主要处理我的订单的内容显示
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //初始化一个进度条对话框，对话框的标题为提示，内容为获取中
            dialog = ProgressDialog.show(MyOrdersListActivity.this, "提示", "获取中..");
        }

        //后台执行操作，发送一个地址，回传得到服务器的数据
        @Override
        protected String doInBackground(String... params) {
            String json = null;

            serverUrl = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getmyorderslist&userid="
                    + user.getId();
            json = httpHelper.HttpRequest(serverUrl);
            return json;
        }

        //后台操作执行完后，回传数据后，对当前我的订单界面进行更新
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //提示对话框消失
            dialog.dismiss();
            //实例化订单
            list = new ArrayList<orders>();
            //判断从服务器返回的数据是否为空且长度是否大于0
            if (result != null && result.trim().length() > 0) {
                try {
                    //对返回的json格式数据进行解析
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //实例化一个orders实体类
                        orders model = new orders();
                        //将从json中返回的值依次设置进实体类中
                        model.setId(jsonObject.getInt("id"));
                        model.setAmount(jsonObject.getDouble("amount"));
                        model.setCreatetime(jsonObject.getString("createtime"));
                        model.setSeat(jsonObject.getString("seat"));
                        model.setPrice(jsonObject.getDouble("price"));
                        model.setUsername(jsonObject.getString("title"));
                        model.setImg_url(jsonObject.getString("img_url"));
                        model.setStatus(jsonObject.getInt("status"));
                        //将实体类对象的数据加入到订单列表中
                        list.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //如果没有返回数据，则弹出一个吐司
                toastUtil.show("没有数据");
            }
            //初始化一个订单的适配器
            adapter = new OrdersAdapter(MyOrdersListActivity.this, list);
            //列表显示适配器中的处理后的内容
            listview1.setAdapter(adapter);
        }
    }

    // 弹出上下文菜单，弹出对话框是否需要取消订单
    private void showContactDialog(final int position) {
        String[] arg = new String[]{"取消订单"};
        new AlertDialog.Builder(this).setTitle("选择操作").setItems(arg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:// 取消
                        new deleteAsyncTask().execute(list.get(position).getId() + "");
                }
            }
        }).show();
    }

    /**
     * 删除订单的异步处理任务
     */
    private class deleteAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //初始化一个进度条对话框，对话框的标题为提示，内容为取消中，请稍后..
            dialog = ProgressDialog.show(MyOrdersListActivity.this, "提示", "取消中,请稍后..");
        }

        //后台执行操作，发送一个地址，回传得到服务器的数据
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
            //提示对话框消失
            dialog.dismiss();
            //判断从服务器返回的数据是否为空且长度是否大于0，进行相应的处理
            if (result != null && result.trim().length() > 0) {
                toastUtil.show("取消成功");
                new loadAsyncTask().execute();
            } else {
                toastUtil.show("取消失败");

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
            // //点击顶端左上角的按钮进行返回操作
            case R.id.btnTopTitleLeft:
                finish();
                break;
            default:
                break;
        }
    }
}
