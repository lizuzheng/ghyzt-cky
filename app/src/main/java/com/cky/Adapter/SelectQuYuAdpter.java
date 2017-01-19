package com.cky.Adapter;

import com.cky.ghyzt.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectQuYuAdpter extends BaseAdapter {

	private String[] quyu;
	private Context context;

	public SelectQuYuAdpter(Context context,String[] quyu)
	{
		this.quyu=quyu;
		this.context=context;
	}
	
	
	@Override
	public int getCount() {
		
		return quyu.length;
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
		
		View view=View.inflate(context, R.layout.child_textview	, null);
		
		TextView tv=(TextView) view.findViewById(R.id.tv_quyuchild);
		tv.setText(quyu[position]);
		
		return view;
	}
	
	

}
