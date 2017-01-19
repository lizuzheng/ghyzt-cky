package com.cky.Adapter;

import java.util.List;

import com.cky.ghyzt.R;
import com.cky.model.TuBanModel;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TuBanChaXunAdpter extends BaseAdapter{

	private Context context;
	private List<TuBanModel> listModel;

	public TuBanChaXunAdpter(Context context,List<TuBanModel> listModel)
	{
		this.context=context;
		this.listModel=listModel;
	}
	
	
	@Override
	public int getCount() {
		
		if(listModel==null)
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
	
		View view=View.inflate(context, R.layout.child_chaxun_tuban, null);
		
		//控件
		TextView beizhu_tuban=(TextView) view.findViewById(R.id.beizhu_tuban);	
		TextView bianhao_tuban=(TextView) view.findViewById(R.id.bianhao_tuban);
		TextView qushixian_tuban=(TextView) view.findViewById(R.id.qushixian_tuban);
		TextView xiangzheng_tuban=(TextView) view.findViewById(R.id.xiangzheng_tuban);
		TextView tubanmianji=(TextView) view.findViewById(R.id.tubanmianji);
		TextView weizhi_tuban=(TextView) view.findViewById(R.id.weizhi_tuban);
		TextView bianhuanianfen_tuban=(TextView) view.findViewById(R.id.bianhuanianfen_tuban);

		//设置值
		beizhu_tuban.setText(listModel.get(position).getBeizhu());
		bianhao_tuban.setText(listModel.get(position).getBianhao());
		qushixian_tuban.setText(listModel.get(position).getQushixian());
		xiangzheng_tuban.setText(listModel.get(position).getXiangzheng());
		tubanmianji.setText(listModel.get(position).getTubanmianji());
		weizhi_tuban.setText(listModel.get(position).getWeizhi());
		bianhuanianfen_tuban.setText(listModel.get(position).getBianhuanianfen());
		
		
		
		
		return view;
	}

}
