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
 * 点餐列表,显示全部菜谱
 *
 * @author dda
 */
public class DishesListActivity extends BaseActivity {
    //顶端标题栏的左边的按钮
    private Button btnTopTitleLeft;
    //顶端标题栏的右边的按钮
    private Button btnTopTitleRight;
    //设置点餐列表
    private List<dishes> list;
    //设置点餐列表的适配器
    private DishesAdapter adapter;
    //列表
    private ListView listview1;
    //顶端标题栏中间的文本
    private TextView tvTopTitleCenter;
    //关键字
    private String keyword = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载activity_list布局
        setContentView(R.layout.activity_list);
        //初始化视图
        findview();
        //实例化一个加载的异步任务，并执行
        new loadAsyncTask().execute(keyword);
    }

    private void findview() {
        tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
        //设置顶端文本内容为菜谱大全
        tvTopTitleCenter.setText("菜谱大全");

        btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
        //设置顶端左边按钮显示
        btnTopTitleLeft.setVisibility(View.VISIBLE);
        ////设置顶端左边按钮的监听事件
        btnTopTitleLeft.setOnClickListener(this);
        //设置顶端左边按钮的文本内容为搜索
        btnTopTitleLeft.setText("搜索");

        listview1 = (ListView) findViewById(R.id.listview1);

    }

    /**
     * 该加载的异步任务主要处理菜谱菜单的显示
     */
    private class loadAsyncTask extends AsyncTask<String, Integer, String> {

        //执行前的操作
        @Override
        protected void onPreExecute() {
            //初始化一个进度条对话框，对话框的标题为提示，内容为获取中
            dialog = ProgressDialog.show(DishesListActivity.this, "提示", "获取中..");
        }

        //后台执行操作，发送一个地址，回传得到服务器的数据
        @Override
        protected String doInBackground(String... params) {
            //定义一个字符串json，将其初始化为空
            String json = null;

            //获取服务端Urlservlet目录的地址
            serverUrl = AppConstant.getUrl(getApplicationContext()) + "ServletService?Action=getdisheslist&msg="
                    + params[0];

            //对当前的地址进行请求，并返回一个json值,json后台所返回的数据
            json = httpHelper.HttpRequest(serverUrl);

            return json;
        }

        //后台操作执行完后，对当前菜谱菜单界面进行更新
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //提示对话框消失
            dialog.dismiss();
            //实例化菜谱列表
            list = new ArrayList<dishes>();
            //判断从服务器返回的数据是否为空且长度是否大于0
            if (result != null && result.trim().length() > 0) {
                try {
                    //对返回的json格式数据进行解析
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //实例化一个dishes实体类
                        dishes model = new dishes();
                        //将从json中返回的值依次设置进实体类中
                        model.setId(jsonObject.getInt("id"));
                        model.setIntro(jsonObject.getString("intro"));
                        model.setImg_url(jsonObject.getString("img_url"));
                        model.setTitle(jsonObject.getString("title"));
                        model.setPrice(jsonObject.getDouble("price"));
                        //将实体类对象的数据加入到菜谱列表中
                        list.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //如果没有返回数据，则弹出一个吐司
                toastUtil.show("没有数据");
            }
            //初始化一个菜谱的适配器
            adapter = new DishesAdapter(DishesListActivity.this, list);
            //列表显示适配器中的处理后的内容
            listview1.setAdapter(adapter);
        }
    }

    /**
     * 处理后面的activity往当前的activity进行数据传输处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果请求码和结果码都为1，则执行异步任务操作
        if (requestCode == 1 && resultCode == 1) {
            new loadAsyncTask().execute(tvTopTitleCenter.getText().toString(), keyword);
        }
    }

    /**
     * 实现搜索功能
     */
    private void search() {
        final EditText eText = new EditText(this);
        //弹出一个对话框，可在对话框中输入数据，向服务器请求搜索当前的数据是否存在该值
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("搜索").setIcon(android.R.drawable.ic_dialog_info)
                .setView(eText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取关键字的值
                        keyword = eText.getText().toString();
                        //执行搜索关键字的操作
                        new loadAsyncTask().execute(keyword);
                    }

                }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    /**
     * 实现了选项菜单的功能
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //将3个选项加载入menu中
        menu.add(0, 100, 0, "我的订单").setIcon(R.drawable.icon_application);
        menu.add(0, 101, 0, "修改密码").setIcon(R.drawable.icon_application);
        menu.add(0, 102, 0, "退出").setIcon(R.drawable.icon_application);
        return true;
    }

    /**
     * 判断你选择的是选项菜单上的哪一项
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            //点击了我的订单，跳转到MyOrdersListActivity
            case 100:
                intent = new Intent(DishesListActivity.this, MyOrdersListActivity.class);
                startActivity(intent);
                break;
            //点击了修改密码，跳转到UpdatePwdActivity
            case 101:
                intent = new Intent(DishesListActivity.this, UpdatePwdActivity.class);
                startActivity(intent);
                break;
            //点击了退出，直接退出程序
            case 102:
                finish();
                System.exit(0);
                break;
        }
        return false;
    }

    /**
     * 处理按钮的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTopTitleRight:

                break;
            //点击顶端左上角的按钮进行搜索操作
            case R.id.btnTopTitleLeft:
                search();
                break;

            default:
                break;
        }

    }

}
