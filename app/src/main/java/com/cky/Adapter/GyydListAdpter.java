package com.cky.Adapter;

import java.util.List;
import java.util.Map;

import com.cky.ghyzt.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GyydListAdpter extends BaseAdapter {

	private List<Map<String, String>> list;
	private Context context;

	public GyydListAdpter(List<Map<String, String>> list,Context context)
	{
		this.list=list;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
	
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		View view=View.inflate(context, R.layout.dialog_gyydtext, null);
		TextView tv_gyydtitle=(TextView) view.findViewById(R.id.tv_gyydtitle);
		TextView tv_gyydneirong=(TextView) view.findViewById(R.id.tv_gyydneirong);
		
		Map<String, String> map=list.get(arg0);
		
		for(String s:map.keySet())
		{
			tv_gyydtitle.setText(s);
			tv_gyydneirong.setText(map.get(s));
		}
		
		
		return view;
	}
}
