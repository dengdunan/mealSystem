package com.mm.mealapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miebo.utils.AsyncImageLoader;
import com.mm.mealapp.activity.AppConstant;
import com.mm.mealapp.activity.GoodDetailActivity;
import com.mm.mealapp.activity.R;
import com.mm.mealapp.api.dishes;

import java.util.List;

/**
 * 点餐商品的适配器
 */
public class DishesAdapter extends BaseAdapter {
	//点餐商品列表
	private List<dishes> list = null;
	//上下文
	private final Context context;
	//布局加载器
	private LayoutInflater infater = null;
	//异步图片加载器
	private final AsyncImageLoader asyncImageLoader;
	//服务器地址
	private final String serverUrl;

	/**
	 * DishesAdapter构造函数，初始化属性
	 * @param context
	 * @param list
	 */
	public DishesAdapter(Context context, List<dishes> list) {
		this.infater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		asyncImageLoader = new AsyncImageLoader(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.pc_loading_fali));
		serverUrl = AppConstant.getRootUrl(context);
	}

	//总共的点餐商品集合大小
	@Override
	public int getCount() {

		return list.size();
	}


	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	/**
	 * 复写视图
	 * @param position
	 * @param convertview
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			//实例化属性，即图片，标题等
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			holder.imageView1 = (ImageView) convertview.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);
			/**
			 * 设置当前视图的监听器，跳转界面
			 */
			convertview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, GoodDetailActivity.class);
					intent.putExtra("id", list.get(position).getId());
					context.startActivity(intent);

				}
			});
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		//设置标题的内容
		holder.tvTitle.setText(list.get(position).getTitle());
		//设置点餐商品的价格
		holder.tvPrice.setText("单价:￥" + list.get(position).getPrice());
		//介绍信息判断成都大小，大于40则使用。。。
		if (list.get(position).getIntro().length() > 40) {
			holder.tvIntro.setText("\u3000" + list.get(position).getIntro().substring(0, 39) + "...");
		} else {
			holder.tvIntro.setText("\u3000" + list.get(position).getIntro());
		}
		//异步图片加载器,加载商品的图片(从服务器端获取)
		asyncImageLoader.loadBitmap(serverUrl + "UploadFile/" + list.get(position).getImg_url(), holder.imageView1);
		return convertview;
	}

	class ViewHolder {
		private ImageView imageView1;
		private TextView tvTitle;
		private TextView tvPrice;
		private TextView tvIntro;

	}

}
