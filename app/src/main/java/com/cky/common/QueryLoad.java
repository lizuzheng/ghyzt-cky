package com.cky.common;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import com.cky.common.MyMapQuery.ReturnResult;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.cky.model.QueryViewModel;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.map.dialog.LoadDialog;


public class QueryLoad {
	
	private Activity activity;
	private QueryViewModel model;


	public QueryLoad(Activity activity, QueryViewModel model)
	{
		this.activity=activity;
		this.model=model;
	}
	
	public void queryLoad(Context context,MapView mMapView,String where,Map<Integer,Graphic> mapGh)
	{
		MyReturnResult myResule=new MyReturnResult(context, mMapView,mapGh,activity);	
		MyMapQuery map = new MyMapQuery();
		map.setGeometry(mMapView.getMaxExtent());
		map.setURL("http://125.70.229.64:6080/arcgis/rest/services/road_cd/MapServer/0");
		map.setReturnResult(myResule);
		map.setWhere(where);
		map.execute();
	}

	
	/**
	 * 回调查询结果
	 * @author Administrator
	 *
	 */
	class MyReturnResult implements ReturnResult {

		private Context context;
		private MapView mMapView;
		private Map<Integer, Graphic> mapGh;
		private Activity activity;


		public MyReturnResult(Context context,MapView mMapView,Map<Integer,Graphic> mapGh,Activity activity)
		{
			this.activity=activity;
			this.context=context;
			this.mMapView=mMapView;
			this.mapGh=mapGh;
		}
		
		
		@Override
		public void onPostExecute(FeatureSet result) {
			System.out.println("查询结果"+result.getGraphics().length);
			

						
			//[start] 判断是否为点线面,并且做对应的处理
			for (Graphic graphic : result.getGraphics()) {
				Symbol symbol = null;
				if (graphic.getGeometry().getType() == Type.POINT) {
					symbol = new PictureMarkerSymbol(context.getResources()
							.getDrawable(R.mipmap.showpoint));
				} else if (graphic.getGeometry().getType() == Type.POLYLINE) {
					symbol = new SimpleLineSymbol(Color.rgb(246,104,14),
							5, SimpleLineSymbol.STYLE.SOLID);
				} else if (graphic.getGeometry().getType() == Type.POLYGON) {
					symbol = new SimpleFillSymbol(Color.rgb(255, 0, 0),
							SimpleFillSymbol.STYLE.SOLID);
					((SimpleFillSymbol) symbol).setAlpha(100);
				}

				if (null != symbol) {
					HashMap<String, Object> values = new HashMap<String, Object>(
							graphic.getAttributes());
					Graphic g = new Graphic(graphic.getGeometry(),
							symbol, values, 0);
					//对应保存数据
					int id=MainActivity.ghlayer.addGraphic(g);
					mapGh.put(id, graphic);
				}
			}		
			//mMapView.addLayer(MainActivity.ghlayer);
			//[end]
			
			
			Envelope mapExtend = GraphicHelper.getExtent(result.getGraphics());

			// 计算中心点
			Point point = mapExtend.getCenter();

			//mMapView.setExtent(mapExtend);
			// 设置分辨率
			//mMapView.setScale(46182.92920838987);
			// 地图设置中心点
			mMapView.centerAt(point, true);
			
			//关闭查询框
			LoadDialog.close();
			//activity.finish();
				
			// 切换到地图
			Intent intent = new Intent();
			intent.putExtra("QueryViewModel", model);
			activity.setResult(21, intent);
			activity.finish();

		}
	
	}
}
