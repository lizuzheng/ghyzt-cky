package com.mapview.click;

import java.util.ArrayList;
import java.util.List;
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
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.identify.IdentifyResult;
import com.map.dialog.LoadDialog;
import com.map.dialog.ShowPototDialog;
import com.map.dialog.savePointDialog;
import com.map.utils.LayerIdentTaskUtil;
import com.map.utils.LayerIdentTaskUtil.Iresult;

public class DrawLinePoint extends MapOnTouchListener {

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Point startPoint = null;
	private MultiPath poly;
	private boolean isFirst = true;
	private List<Integer> pointId = new ArrayList<Integer>();
	private int[] pointIdClick;
	private ArrayList<Point> drawPoint = new ArrayList<Point>();
	private Activity activity;
	private int ghlayerId=-1;

	public DrawLinePoint(Context context, MapView view,Activity activity) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		this.activity=activity;
		
		
		graphicsLayer = MainActivity.ghlayer;
		poly = new Polyline();
	}

	@Override
	public boolean onSingleTap(MotionEvent point) {

		pointIdClick = graphicsLayer.getGraphicIDs(point.getX(), point.getY(),
				10);
		if (pointId != null && pointIdClick != null) {
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

		if (isFirst) {
			Point mapPt = mMapView.toMapPoint(to.getX(), to.getY());
			if (startPoint == null) {
				startPoint = mMapView.toMapPoint(from.getX(), from.getY());

				

				poly.startPath(startPoint);

				Graphic graphic = new Graphic(startPoint, new SimpleLineSymbol(
						Color.RED, 5));
				int id = graphicsLayer.addGraphic(graphic);
				pointId.add(id);
			} else {
				poly.lineTo(mapPt);

				//清除上一个
				if(ghlayerId!=-1)
				{
					graphicsLayer.removeGraphic(ghlayerId);
				}
				
				int id=ghlayerId= graphicsLayer.addGraphic(new Graphic(poly,
						new SimpleLineSymbol(Color.RED, 2)));
				pointId.add(id);

				String length = Double.toString(Math.round(poly
						.calculateLength2D())) + " 米";

			}
			drawPoint.add(mapPt);

		} else {
			return super.onDragPointerMove(from, to);
		}

		return true;
	}

	@Override
	public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

		
		startPoint = null;
		if(isFirst)
		{
//			savePointDialog save=new savePointDialog("Polyline", drawPoint, ghlayerId, context, activity,"");
//			save.show();
			this.query(drawPoint.get(0));
		}
		isFirst = false;
		Log.i("点的总个数：", drawPoint.size()+"");
		
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
				savePointDialog save = new savePointDialog("Polyline", drawPoint,
						ghlayerId, context, activity,diyu);
				save.show();

			}

		};

}
