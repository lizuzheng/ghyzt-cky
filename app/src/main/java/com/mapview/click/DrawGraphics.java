package com.mapview.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.cky.common.GraphicHelper;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.map.dialog.DialogShowTuBan;
import com.map.dialog.LoadDialog;

public class DrawGraphics extends MapOnTouchListener {

	private Context context;
	private MapView mMapview;
	private Graphic[] Graphic;
	private GraphicsLayer ghlayer;
	private HashMap<Integer, Graphic> mapId;

	public DrawGraphics(Context context, MapView view, Graphic[] graphic) {

		super(context, view);
		this.context = context;
		this.mMapview = view;
		this.Graphic = graphic;
		this.ghlayer = MainActivity.ghlayer;

		ghlayer.removeAll();
		// 绘制
		this.drawGraphic(graphic);

	}

	// 绘制图形
	private void drawGraphic(Graphic[] graphic1) {
		
	    mapId=new HashMap<>();
		// [start] 判断是否为点线面,并且做对应的处理
		for (Graphic graphic : graphic1) {
			Symbol symbol = null;
			if (graphic.getGeometry().getType() == Type.POINT) {
				symbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(R.mipmap.showpoint));
			} else if (graphic.getGeometry().getType() == Type.POLYLINE) {
				symbol = new SimpleLineSymbol(Color.rgb(246, 104, 14), 5,
						SimpleLineSymbol.STYLE.SOLID);
			} else if (graphic.getGeometry().getType() == Type.POLYGON) {
				symbol = new SimpleFillSymbol(Color.rgb(255, 0, 0),
						SimpleFillSymbol.STYLE.SOLID);
				((SimpleFillSymbol) symbol).setAlpha(100);
			}

			if (null != symbol) {
		
				Graphic g = new Graphic(graphic.getGeometry(), symbol, graphic.getAttributes(),
						0);
				// 对应保存数据
				int id = ghlayer.addGraphic(g);
	
				mapId.put(id, g);
				
			}

		}

	}

	@Override
	public boolean onSingleTap(MotionEvent e) {
		int[] ids = ghlayer.getGraphicIDs(e.getX(), e.getY(), 10);

		if (ids.length > 0) {
			LoadDialog.show(context, true, "等待加载……");
			for (int i = 0; i < ids.length; i++) {

				//需要展示的数据
				String[] ziduan={"编号","区市县","乡镇","位置","变化年份","图斑面积","备注"};
				
				
				try {
					//整理数据
					List<Map<String, String>> listMap=new ArrayList<Map<String,String>>();
					
					//循环数据
					for(String s:ziduan)
					{
						Map<String, String> map=new HashMap<String, String>();
						map.put(s, mapId.get(ids[i]).getAttributeValue(s).toString());
						listMap.add(map);
					}
					
					Point centerPoint =GraphicHelper.getExtent( mapId.get(ids[i])).getCenter();
					
									
					//显示弹窗
					View view=DialogShowTuBan.openDialogView(context, listMap,mapId.get(ids[i]).getAttributeValue("区市县").toString());
					
					LoadDialog.close();
					final Callout callout = mMapview.getCallout();
					callout.setStyle(R.xml.callout_style);// 设置显示样式
					callout.setContent(view);// callout的显示内容
					callout.setCoordinates(centerPoint); // 设置显示的位置为单击的位置
					callout.show();// 显示callout
					
					break;
				} catch (Exception e2) {
					continue;
				}

			}

		}else
		{
			mMapview.getCallout().hide();
		}
		
		
		return super.onSingleTap(e);
	}

}
