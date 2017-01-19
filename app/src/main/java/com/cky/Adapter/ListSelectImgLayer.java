package com.cky.Adapter;

import java.util.List;

import com.cky.ghyzt.R;
import com.cky.model.LayerImageModel;
import com.esri.android.map.Layer;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListSelectImgLayer extends BaseAdapter{

	private Context context;
	private List<LayerImageModel> list;

	public ListSelectImgLayer(Context context,List<LayerImageModel> list)
	{
		this.context=context;
		this.list=list;
	}
	
	
	@Override
	public int getCount() {

		//减1是因为有一个底图需要一直显示
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	
		View view=View.inflate(context, R.layout.textview_layer, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_textviews);
		
          tv.setText(list.get(position).getLayer().getName());
	
		
		return view;
		
	}
}















