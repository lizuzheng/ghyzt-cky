package com.cky.pointQuery;

import com.cky.common.MyMapQuery;
import com.cky.common.MyMapQuery.ReturnResult;
import com.esri.android.map.MapView;

import android.content.Context;

//查询图斑事件
public class QueryTuBan {

	private Context context;
	private MapView mMapview;

	public QueryTuBan(Context context, MapView mMapView) {
		this.context = context;
		this.mMapview = mMapView;
	}

	public void startQuery(String OBJECTID_1, ReturnResult myResule) {
		MyMapQuery map = new MyMapQuery();
		map.setGeometry(mMapview.getMaxExtent());
		map.setURL("http://125.70.229.64:6080/arcgis/rest/services/BHTB_ALL/MapServer/0");
		map.setReturnResult(myResule);
		map.setWhere("OBJECTID_1="+OBJECTID_1);
		map.execute();
	}

}
