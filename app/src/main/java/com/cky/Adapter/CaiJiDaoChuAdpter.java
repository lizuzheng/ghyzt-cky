package com.cky.Adapter;

import java.util.List;

import com.cky.ghyzt.R;
import com.cky.model.ShapeModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CaiJiDaoChuAdpter extends BaseAdapter {

	private Context context;
	private List<ShapeModel> listModel;
	private QuanXuanState state;

	public CaiJiDaoChuAdpter(Context context, List<ShapeModel> listModel,
			QuanXuanState state) {
		this.context = context;
		this.listModel = listModel;
		this.state = state;
	}

	@Override
	public int getCount() {
		if (listModel == null) {
			return 0;
		}
		return listModel.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int position1 = position;

		View view = View.inflate(context, R.layout.child_caiji_daochu, null);

		CheckBox cb = (CheckBox) view.findViewById(R.id.cb_xuanze);
		TextView tv_tuxingmingcheng = (TextView) view
				.findViewById(R.id.tv_tuxingmingcheng);
		TextView caijizhe = (TextView) view.findViewById(R.id.caijizhe);
		TextView caijishijian = (TextView) view.findViewById(R.id.caijishijian);
		TextView tubianzhangshu = (TextView) view
				.findViewById(R.id.tubianzhangshu);

		cb.setText("选择");
		cb.setChecked(listModel.get(position).isSelect());
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				listModel.get(position1).setSelect(isChecked);

				// 判断当前的状态
				if (!isChecked) {
					state.zhixing(false);
				} else {
					boolean isQuanxuan = true;
					for (ShapeModel model : listModel) {
						if (!model.isSelect()) {
							isQuanxuan = false;
							break;
						}
					}
					state.zhixing(isQuanxuan);
				}

			}
		});

		tv_tuxingmingcheng.setText(listModel.get(position).getCGraphicsName());
		caijizhe.setText(listModel.get(position).getCGraphicsCreatNum());
		caijishijian.setText(listModel.get(position).getCGraphicsCreatTime());
		
		int count=listModel.get(position).getListImage()==null?0:listModel.get(position).getListImage().size();
		tubianzhangshu.setText(count+ "张");

		return view;
	}

	public interface QuanXuanState {
		void zhixing(boolean state);
	}
}
