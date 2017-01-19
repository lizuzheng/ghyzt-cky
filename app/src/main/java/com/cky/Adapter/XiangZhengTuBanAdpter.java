package com.cky.Adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cky.ghyzt.R;
import com.cky.model.TuBanJieGuoModel;
import com.cky.model.XiangZhengModel;

public class XiangZhengTuBanAdpter extends BaseAdapter {

	private List<XiangZhengModel> caiJiList;
	private Map<String, Boolean> isShowMap;
	private Context context;

	public XiangZhengTuBanAdpter(Context context,
			List<XiangZhengModel> caiJiList, Map<String, Boolean> isShowMap) {
		this.context = context;
		this.caiJiList = caiJiList;
		this.isShowMap = isShowMap;
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

		View view = View.inflate(context, R.layout.child_xiangzheng_tv, null);

		TextView tv_quxian = (TextView) view.findViewById(R.id.tv_quxian);
		TextView tv_caijiliang = (TextView) view
				.findViewById(R.id.tv_caijiliang);

		tv_quxian.setText(caiJiList.get(position).getXiangzheng());
		tv_caijiliang.setText(caiJiList.get(position).getCount());

		tv_quxian.setBackgroundColor(Color.rgb(217, 217, 217));
		tv_caijiliang.setBackgroundColor(Color.rgb(217, 217, 217));

		return view;
	}

}
