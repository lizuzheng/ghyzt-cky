package com.cky.pointQuery;

import android.content.Context;

import com.cky.common.MyMapQuery;
import com.cky.common.MyMapQuery.ReturnResult;
import com.esri.android.map.MapView;

public class QueryJianSheBu {

	private Context context;
	private MapView mMapview;

	public QueryJianSheBu(Context context, MapView mMapView) {
		this.context = context;
		this.mMapview = mMapView;
	}

	public void startQuery(String tiaojian, ReturnResult myResule) {
		
		//组合条件查询语句

		
		String sql=" 备注 like '%"+tiaojian+"%' or "+
		           " 规划用地性 like '%"+tiaojian+"%' or "+
		           " 变化前用地 like '%"+tiaojian+"%' or "+
		           " 处理意见 like '%"+tiaojian+"%' or "+
		           " 是否符合 like '%"+tiaojian+"%' or "+
		           " 备注 like '%"+tiaojian+"%'";
		
		MyMapQuery map = new MyMapQuery();
		map.setGeometry(mMapview.getMaxExtent());
		map.setURL("http://125.70.229.64:6080/arcgis/rest/services/JSBTB_2016_CD2012/MapServer/0");
		map.setReturnResult(myResule);
		map.setWhere(sql);
		map.execute();
	}
}
