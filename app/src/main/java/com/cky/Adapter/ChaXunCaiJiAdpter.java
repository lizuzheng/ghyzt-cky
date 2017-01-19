package com.cky.Adapter;

import java.util.List;

import com.cky.ghyzt.R;
import com.cky.model.ShapeModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChaXunCaiJiAdpter extends BaseAdapter {

	private Context context;
	private List<ShapeModel> listModel;

	public ChaXunCaiJiAdpter(Context context, List<ShapeModel> listModel) {
		this.context = context;
		this.listModel = listModel;
	}

	@Override
	public int getCount() {
		if (listModel == null)
			return 0;

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

		View view = View.inflate(context, R.layout.child_chacun_caiji, null);


		TextView tv_tuxingmingcheng = (TextView) view
				.findViewById(R.id.tv_tuxingmingcheng);
		TextView caijizhe = (TextView) view.findViewById(R.id.caijizhe);
		TextView caijishijian = (TextView) view.findViewById(R.id.caijishijian);
		TextView anjianbianhao = (TextView) view
				.findViewById(R.id.anjianbianhao);
		TextView quxian = (TextView) view.findViewById(R.id.quxian);
		TextView beizhu = (TextView) view.findViewById(R.id.beizhu);

		tv_tuxingmingcheng.setText(listModel.get(position).getCGraphicsName());
		caijizhe.setText(listModel.get(position).getCGraphicsCreatNum());
		caijishijian.setText(listModel.get(position).getCGraphicsCreatTime());
		anjianbianhao.setText(listModel.get(position).getAnjianbianhao());
		quxian.setText(listModel.get(position).getQuyu());
		beizhu.setText(listModel.get(position).getCGraphicsContent());

		return view;
	}

}
