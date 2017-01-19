package com.mapview.click;

import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cky.ghyzt.R;
import com.cky.model.AutoTextModel;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;

public class QueryClickPoi extends MapOnTouchListener {

	private GraphicsLayer graphicsLayer;
	private Context context;
	private MapView mMapview;
	private Map<Integer, AutoTextModel> map;
	private Map<Integer, Graphic> mapGh;

	public QueryClickPoi(Context context, MapView view,
			GraphicsLayer graphicsLayer, Map<Integer, AutoTextModel> map,
			Map<Integer, Graphic> mapGh) {
		super(context, view);
		this.context = context;
		this.mMapview = view;
		this.graphicsLayer = graphicsLayer;
		this.map = map;
		this.mapGh = mapGh;

	}

	@Override
	public boolean onSingleTap(MotionEvent e) {

		Point point = mMapview.toMapPoint(new Point(e.getX(), e.getY()));
		int[] ids = graphicsLayer.getGraphicIDs(e.getX(), e.getY(), 10);
		if (ids.length > 0) {

			System.out.println(ids[0]);

			if (map.get(ids[0]) != null && !map.get(ids[0]).equals("")) {
				System.out.println(map.get(ids[0]).getLabel());
				this.showPointInfo(point, map.get(ids[0]));

			} else if (mapGh!=null&&mapGh.get(ids[0]) != null
					&& !mapGh.get(ids[0]).equals("")) {
				Graphic g = mapGh.get(ids[0]);
				this.showLoadInfo(point,g);
				
			}

		} else {
			final Callout callout = mMapview.getCallout();
			callout.hide();
		}

		return super.onSingleTap(e);
	}

	/**
	 * 显示道路的信息
	 * @param point
	 * @param g
	 */
	private void showLoadInfo(Point point, Graphic g) {
		
		//点击时添加不同的样式，以便区分
		
		Symbol symbol  = new SimpleFillSymbol(Color.rgb(248,67,52),SimpleFillSymbol.STYLE.DIAGONAL_CROSS);
		
		Graphic gl = new Graphic(g.getGeometry().copy(),symbol);
		graphicsLayer.addGraphic(gl);
		
	     if(g.getGeometry()!=null)
	     {
	    	 System.out.println("不是空图形");
	     }
		
		

		//ENTIID  GRADE  要素分类
		View view = View.inflate(context, R.layout.showloadinfo_layer, null);

		TextView show_tv_title = (TextView) view.findViewById(R.id.showload_tv_title);
		show_tv_title.setText(g.getAttributeValue("ROADNAME").toString());
		TextView showload_entiid = (TextView) view.findViewById(R.id.showload_entiid);
		showload_entiid.setText("ENTIID:"+g.getAttributeValue("ENTIID").toString());
		TextView showload_grade = (TextView) view.findViewById(R.id.showload_grade);
		showload_grade.setText("GRADE:"+g.getAttributeValue("GRADE").toString());
		TextView showload_yaosu = (TextView) view.findViewById(R.id.showload_yaosu);
		showload_yaosu.setText("要素分类:"+g.getAttributeValue("要素分类").toString());
		

		final Callout callout = mMapview.getCallout();
		callout.setStyle(R.xml.callout_style);// 设置显示样式
		callout.setContent(view);// callout的显示内容
		callout.setCoordinates(point); // 设置显示的位置为单击的位置
		callout.show();// 显示callout
		
	}

	/**
	 * 显示点的信息
	 * @param point
	 * @param autoTextModel
	 */
	private void showPointInfo(Point point, AutoTextModel autoTextModel) {
		View view = View.inflate(context, R.layout.showpointinfo_layer, null);

		TextView show_tv_title = (TextView) view
				.findViewById(R.id.show_tv_title);
		show_tv_title.setText(autoTextModel.getLabel());
		TextView show_laber = (TextView) view.findViewById(R.id.show_laber);
		show_laber.setText(autoTextModel.getLabel());
		TextView show_jigou = (TextView) view.findViewById(R.id.show_jigou);
		show_jigou.setText(autoTextModel.getType());

		final Callout callout = mMapview.getCallout();
		callout.setStyle(R.xml.callout_style);// 设置显示样式
		callout.setContent(view);// callout的显示内容
		callout.setCoordinates(point); // 设置显示的位置为单击的位置
		callout.show();// 显示callout

	}

}
