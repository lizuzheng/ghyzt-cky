package com.cky.Adapter;

import java.util.List;
import java.util.Map;


import com.cky.model.CaiJiShuJuTongJiModel;
import com.cky.ghyzt.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CaiJiShuJuAdpter extends BaseAdapter {

	private List<CaiJiShuJuTongJiModel> caiJiList;
	private Map<String, Boolean> isShowMap;
	private Context context;

	public  CaiJiShuJuAdpter(Context context,List<CaiJiShuJuTongJiModel> caiJiList,Map<String,Boolean> isShowMap)
	{
		this.context=context;
		this.caiJiList=caiJiList;
		this.isShowMap=isShowMap;
	}
	
	@Override
	public int getCount() {
		
		return caiJiList.size();
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
		
		View view=View.inflate(context,R.layout.child_caijishuju_tv, null);
		
		TextView tv_quxian=(TextView) view.findViewById(R.id.tv_quxian);
		TextView tv_caijiliang=(TextView) view.findViewById(R.id.tv_caijiliang);
		
		tv_quxian.setText(caiJiList.get(position).getQuyu());
		tv_caijiliang.setText(caiJiList.get(position).getShuliang());
		
		if(position==0)
		{
			tv_quxian.setBackgroundColor(Color.rgb(217, 217, 217));
			tv_caijiliang.setBackgroundColor(Color.rgb(217, 217, 217));
		}
		
		
		return view;
	}

}
