package com.mm.mealapp.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miebo.utils.AsyncImageLoader;
import com.mm.mealapp.activity.AppConstant;
import com.mm.mealapp.activity.R;
import com.mm.mealapp.api.orders;

import java.util.List;

/**
 * 订单的适配器
 */
public class OrdersAdapter extends BaseAdapter {
	//订单商品列表
	private List<orders> list = null;
	//上下文
	private final Context context;
	//布局加载器
	private LayoutInflater infater = null;
	//异步图片加载器
	private final AsyncImageLoader asyncImageLoader;
	//服务器端地址
	private final String serverUrl;

	/**
	 * OrdersAdapter的构造函数，初始化属性
	 * @param context
	 * @param list
	 */
	public OrdersAdapter(Context context, List<orders> list) {
		this.infater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		asyncImageLoader = new AsyncImageLoader(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.pc_loading_fali));
		serverUrl = AppConstant.getRootUrl(context);
	}

	//总共的订单集合大小
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
	 * 复写视图，获取当前item的视图
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
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			//实例化属性，即图片，标题等
			holder.imageView1 = (ImageView) convertview.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);

			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		//设置标题的内容
		holder.tvTitle.setText(list.get(position).getUsername());
		//设置价格
		holder.tvPrice.setText("数量:" + list.get(position).getAmount() + " 单价:" + list.get(position).getPrice());
		//设置座位号和下单时间
		String intro = "座位号:" + list.get(position).getSeat() + "\n下单时间" + list.get(position).getCreatetime();
		//判断当前的订单状态
		if (1 == list.get(position).getStatus()) {
			intro += "\n订单状态:已经完成";
		} else if (0 == list.get(position).getStatus()) {
			intro += "\n订单状态:进行中";
		} else {
			intro += "\n订单状态:已取消";
		}
		//设置介绍信息
		holder.tvIntro.setText(intro);
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
