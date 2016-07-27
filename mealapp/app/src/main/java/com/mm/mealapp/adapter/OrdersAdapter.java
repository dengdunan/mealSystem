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
 * ������������
 */
public class OrdersAdapter extends BaseAdapter {
	//������Ʒ�б�
	private List<orders> list = null;
	//������
	private final Context context;
	//���ּ�����
	private LayoutInflater infater = null;
	//�첽ͼƬ������
	private final AsyncImageLoader asyncImageLoader;
	//�������˵�ַ
	private final String serverUrl;

	/**
	 * OrdersAdapter�Ĺ��캯������ʼ������
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

	//�ܹ��Ķ������ϴ�С
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
	 * ��д��ͼ����ȡ��ǰitem����ͼ
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
			//ʵ�������ԣ���ͼƬ�������
			holder.imageView1 = (ImageView) convertview.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);

			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		//���ñ��������
		holder.tvTitle.setText(list.get(position).getUsername());
		//���ü۸�
		holder.tvPrice.setText("����:" + list.get(position).getAmount() + " ����:" + list.get(position).getPrice());
		//������λ�ź��µ�ʱ��
		String intro = "��λ��:" + list.get(position).getSeat() + "\n�µ�ʱ��" + list.get(position).getCreatetime();
		//�жϵ�ǰ�Ķ���״̬
		if (1 == list.get(position).getStatus()) {
			intro += "\n����״̬:�Ѿ����";
		} else if (0 == list.get(position).getStatus()) {
			intro += "\n����״̬:������";
		} else {
			intro += "\n����״̬:��ȡ��";
		}
		//���ý�����Ϣ
		holder.tvIntro.setText(intro);
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
