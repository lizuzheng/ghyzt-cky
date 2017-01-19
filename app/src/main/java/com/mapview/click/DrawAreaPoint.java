package com.mapview.click;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.cky.common.DiYuHelpler;
import com.cky.ghyzt.MainActivity;
import com.cky.model.MapSavePoint;
import com.cky.model.PointInfo;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.identify.IdentifyResult;
import com.map.dialog.LoadDialog;
import com.map.dialog.ShowPototDialog;
import com.map.dialog.savePointDialog;
import com.map.utils.LayerIdentTaskUtil;
import com.map.utils.LayerIdentTaskUtil.Iresult;

public class DrawAreaPoint extends MapOnTouchListener {

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Point startPoint = null;
	private MultiPath poly;
	private ArrayList<Point> pointList;

	private boolean isFirst = true;
	private Activity activity;
	private int mapId=-1;
	
	private int ghlayerId=-1;

	public DrawAreaPoint(Context context, MapView view,Activity activity) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		this.activity=activity;
		graphicsLayer = MainActivity.ghlayer;
		poly = new Polygon();
		
	}
	
	@Override
	public boolean onSingleTap(MotionEvent point) {
		
		Point p=mMapView.toMapPoint(point.getX(), point.getY());
		if(p!=null&&pointList!=null)
		{
			int[] ids=graphicsLayer.getGraphicIDs(point.getX(), point.getY(), 10);
			if(ids.length>0)
			{
				try {
					Map<Integer,PointInfo> map=MapSavePoint.mapPoint;	
					ShowPototDialog dia=new ShowPototDialog();
					dia.showDialog(activity, context, map.get(ids[0]),ids[0]);
					
									
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}		
				
		return super.onSingleTap(point);
	}

	@Override
	public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

		Point mapPt = mMapView.toMapPoint(to.getX(), to.getY());
		if(isFirst)
		{
			if (startPoint == null) {
				removeAll();
				pointList=new ArrayList<Point>();
				startPoint = mMapView.toMapPoint(from.getX(), from.getY());
				
				pointList.add(startPoint);
				
				poly.startPath(startPoint);
				Graphic graphic = new Graphic(startPoint, new SimpleLineSymbol(
						Color.RED, 5));
				graphicsLayer.addGraphic(graphic);
			} else {				
				pointList.add(mapPt);		
				
				poly.lineTo(mapPt);
				
				//清除上一个
				if(ghlayerId!=-1)
				{
					graphicsLayer.removeGraphic(ghlayerId);
				}
					
				ghlayerId=graphicsLayer.addGraphic(new Graphic(poly,
						new SimpleFillSymbol(Color.RED).setAlpha(20)));
											
				String length = Double.toString(Math.round(poly
						.calculateLength2D())) + " 米";
				Log.i("长度：", length);
			}			
		}
		else
		{
			return super.onDragPointerMove(from, to);
		}
		return true;
	}

	@Override
	public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

		Log.i("list长度:", pointList.size()+"");
		
		startPoint = null;
		if(isFirst)
		{
//			savePointDialog save=new savePointDialog("Polygon", pointList, ghlayerId, context, activity);
//			save.show();
			
			this.query(pointList.get(0));
		}
		isFirst=false;		
		return super.onDragPointerUp(from, to);
	}

	
	
	// 查询点的位置
	private void query(Point point) {
		int[] a = { 0 };
		LoadDialog.show(context, true, "查询地域中……");

		LayerIdentTaskUtil
				.queryTask(
						"http://125.70.229.64:6080/arcgis/rest/services/CD_DISTRICT/MapServer",
						mMapView, point, 20, a, Jiekou);

	}

	// 回调接口
	Iresult Jiekou = new Iresult() {

		@Override
		public void result(IdentifyResult[] results) {

			LoadDialog.close();
			String diyu="成都市";

			if (results != null && results.length > 0) {
				IdentifyResult Ide = results[0];
				Map<String, Object> map = Ide.getAttributes();

				diyu=DiYuHelpler.getPointDiYu(map.get("ENTINAME")
						.toString());
			}

			// 执行保存操作,版本更新，需要作出回调调整
			savePointDialog save = new savePointDialog("Polygon", pointList,
					ghlayerId, context, activity,diyu);
			save.show();

		}

	};
	
	
	
	
	
	
	
	
	/**
	 * 清除图层
	 */
	public void removeAll() {
		graphicsLayer.removeAll();
		poly = new Polygon();

	}
	
	
	
}


