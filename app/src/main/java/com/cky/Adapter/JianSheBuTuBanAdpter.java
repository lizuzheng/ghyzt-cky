package com.cky.Adapter;

import java.util.List;
import java.util.Map;

import com.cky.ghyzt.R;
import com.cky.model.TuBanJieGuoModel;
import com.esri.core.map.Graphic;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JianSheBuTuBanAdpter extends BaseAdapter {

	private Graphic[] graphic;
	private Map<String, Boolean> isShowMap;
	private Context context;

	public JianSheBuTuBanAdpter(Context context, Graphic[] graphic,
			Map<String, Boolean> isShowMap) {
		this.context = context;
		this.graphic = graphic;
		this.isShowMap = isShowMap;
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return graphic.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View view=View.inflate(context, R.layout.child_chaxun_jianshebutuban, null);
		
		TextView tv_id=(TextView) view.findViewById(R.id.id_jianshebu);
		TextView guihuayongdixing_jianshebu=(TextView) view.findViewById(R.id.guihuayongdixing_jianshebu);
		TextView mianji_jianshebu=(TextView) view.findViewById(R.id.mianji_jianshebu);
		TextView bianhuaqian_jianshebu=(TextView) view.findViewById(R.id.bianhuaqian_jianshebu);
		TextView bianhuahou_jianshebu=(TextView) view.findViewById(R.id.bianhuahou_jianshebu);
		TextView chuliyijian_jianshebu=(TextView) view.findViewById(R.id.chuliyijian_jianshebu);
		TextView beizhu_jianshebu=(TextView) view.findViewById(R.id.beizhu_jianshebu);
		
		
		tv_id.setText(graphic[position].getAttributeValue("Id").toString());
		guihuayongdixing_jianshebu.setText(graphic[position].getAttributeValue("规划用地性").toString());
		mianji_jianshebu.setText(graphic[position].getAttributeValue("面积").toString());
		bianhuaqian_jianshebu.setText(graphic[position].getAttributeValue("变化前用地").toString());
//		bianhuahou_jianshebu.setText(graphic[position].getAttributeValue("变化后用地").toString());
		chuliyijian_jianshebu.setText(graphic[position].getAttributeValue("处理意见").toString());
		beizhu_jianshebu.setText(graphic[position].getAttributeValue("备注").toString());
		
		
		
		return view;
	}

}
