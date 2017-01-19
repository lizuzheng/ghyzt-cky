package com.mapview.click;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.Toast;

import com.cky.common.DiYuHelpler;
import com.cky.ghyzt.MainActivity;
import com.cky.model.MapSavePoint;
import com.cky.model.PointInfo;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.identify.IdentifyResult;
import com.jztx.utils.MathUtils;
import com.map.dialog.LoadDialog;
import com.map.dialog.ShowPototDialog;
import com.map.dialog.savePointDialog;
import com.map.utils.LayerIdentTaskUtil;
import com.map.utils.LayerIdentTaskUtil.Iresult;

public class DrawPointSave extends MapOnTouchListener {

	private Context context;
	private Activity activity;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Boolean isFirst = true;
	private int pointId = 0;
	private ArrayList<Point> pointList;

	public DrawPointSave(Context context, MapView view, Activity activity) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		this.activity = activity;
		graphicsLayer = MainActivity.ghlayer;
	}

	@Override
	public boolean onSingleTap(MotionEvent e) {
		Point point = new Point(e.getX(), e.getY());
		Point mapPoint = mMapView.toMapPoint(point);
		if (isFirst) {
			Graphic graphic = new Graphic(mapPoint, new SimpleMarkerSymbol(
					Color.RED, 10, STYLE.CIRCLE));

			pointId = graphicsLayer.addGraphic(graphic);

			isFirst = false;

			pointList = new ArrayList<Point>();
			pointList.add(mapPoint);

			// 查询当前点的位置
			this.query(mapPoint);

		} else {

			// 表示已经保存，在做回显点击操作
			int[] ids = graphicsLayer.getGraphicIDs(e.getX(), e.getY(), 10);
			if (ids.length > 0) {
				try {
					Map<Integer, PointInfo> map = MapSavePoint.mapPoint;
					ShowPototDialog dia = new ShowPototDialog();
					dia.showDialog(activity, context, map.get(ids[0]), ids[0]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		return super.onSingleTap(e);
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
			savePointDialog save = new savePointDialog("Point", pointList,
					pointId, context, activity,diyu);
			save.show();

		}

	};

}
