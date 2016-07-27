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
 * �����Ʒ��������
 */
public class DishesAdapter extends BaseAdapter {
	//�����Ʒ�б�
	private List<dishes> list = null;
	//������
	private final Context context;
	//���ּ�����
	private LayoutInflater infater = null;
	//�첽ͼƬ������
	private final AsyncImageLoader asyncImageLoader;
	//��������ַ
	private final String serverUrl;

	/**
	 * DishesAdapter���캯������ʼ������
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

	//�ܹ��ĵ����Ʒ���ϴ�С
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
	 * ��д��ͼ
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
			//ʵ�������ԣ���ͼƬ�������
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			holder.imageView1 = (ImageView) convertview.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);
			/**
			 * ���õ�ǰ��ͼ�ļ���������ת����
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
		//���ñ��������
		holder.tvTitle.setText(list.get(position).getTitle());
		//���õ����Ʒ�ļ۸�
		holder.tvPrice.setText("����:��" + list.get(position).getPrice());
		//������Ϣ�жϳɶ���С������40��ʹ�á�����
		if (list.get(position).getIntro().length() > 40) {
			holder.tvIntro.setText("\u3000" + list.get(position).getIntro().substring(0, 39) + "...");
		} else {
			holder.tvIntro.setText("\u3000" + list.get(position).getIntro());
		}
		//�첽ͼƬ������,������Ʒ��ͼƬ(�ӷ������˻�ȡ)
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
