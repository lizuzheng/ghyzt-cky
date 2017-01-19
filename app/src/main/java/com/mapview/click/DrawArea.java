package com.mapview.click;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class DrawArea extends MapOnTouchListener {

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Polygon tempPolygon = null;// 记录绘制过程中的多边形
	private Point startPoint = null;
	private int layFillId = -1;
	private int layLineId = -1;
	private int textId = -1;
	private int count = 1;

	public DrawArea(Context context, MapView view) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		graphicsLayer = MainActivity.ghlayer;

	}

	@Override
	public boolean onSingleTap(MotionEvent e) {

		// 获取地图上的点
		Point point = mMapView.toMapPoint(new Point(e.getX(), e.getY()));

		if (startPoint == null) {
			tempPolygon = new Polygon();
			startPoint = point;
			tempPolygon.startPath(point);
		} else {
			if (layFillId != -1) {
				graphicsLayer.removeGraphic(layFillId);
				graphicsLayer.removeGraphic(layLineId);
				graphicsLayer.removeGraphic(textId);
			}

			tempPolygon.lineTo(point);

			Graphic gpFill = new Graphic(tempPolygon, new SimpleFillSymbol(
					Color.BLUE).setAlpha(15));
			layFillId = graphicsLayer.addGraphic(gpFill);

			Graphic gpLine = new Graphic(tempPolygon, new SimpleLineSymbol(
					Color.RED, 2));
			layLineId = graphicsLayer.addGraphic(gpLine);

		}

		Graphic graphic = new Graphic(point, new SimpleMarkerSymbol(Color.BLUE,
				10, STYLE.CIRCLE));
		graphicsLayer.addGraphic(graphic);

		Envelope env = new Envelope();
		tempPolygon.queryEnvelope(env);
		Point center = env.getCenter();

	
		this.drawText(	center , tempPolygon);
		count++;

		return super.onSingleTap(e);
	}

	private void drawText(Point point, Polygon tempPolygon) {
		if (count > 2) {
			
			Polygon tempPolygon1=new Polygon();
			tempPolygon1.startPath(tempPolygon.getPoint(0).getX(),tempPolygon.getPoint(0).getY());
			
			for(int i=1;i<tempPolygon.getPointCount();i++)
			{
				tempPolygon1.lineTo(tempPolygon.getPoint(i).getX(), tempPolygon.getPoint(i).getY());
			}
			
			
			// 绘制面积字体
			String sArea = getAreaString(tempPolygon1.calculateArea2D());
			

			View view = View.inflate(context, R.layout.showmaptext, null);
			TextView tv_show_text = (TextView) view
					.findViewById(R.id.tv_show_text);
			tv_show_text.setText(sArea);
			tv_show_text.setWidth(400);

			Bitmap bit = getViewBitmap(view,400,100);

			Drawable drawable = new BitmapDrawable(bit);
			PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
			pic.setOffsetX(50);
			pic.setOffsetY(20);

			Graphic gText = new Graphic(point, pic);
			textId = graphicsLayer.addGraphic(gText);

		}
	}

	/**
	* 把View绘制到Bitmap上
	* @param view 需要绘制的View
	* @param width 该View的宽度
	* @param height 该View的高度
	* @return 返回Bitmap对象
	* add by csj 13-11-6
	*/
	public Bitmap getViewBitmap(View comBitmap, int width, int height) {
		
		Bitmap bitmap = null;
		if (comBitmap != null) {
			comBitmap.clearFocus();
			comBitmap.setPressed(false);


			boolean willNotCache = comBitmap.willNotCacheDrawing();
			comBitmap.setWillNotCacheDrawing(false);


			// Reset the drawing cache background color to fully transparent
			// for the duration of this operation
			int color = comBitmap.getDrawingCacheBackgroundColor();
			comBitmap.setDrawingCacheBackgroundColor(0);
			float alpha = comBitmap.getAlpha();
			comBitmap.setAlpha(1.0f);


			if (color != 0) {
				comBitmap.destroyDrawingCache();
			}
			
			int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
			int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
			comBitmap.measure(widthSpec, heightSpec);
			comBitmap.layout(0, 0, width, height);


			comBitmap.buildDrawingCache();
			Bitmap cacheBitmap = comBitmap.getDrawingCache();
			if (cacheBitmap == null) {
				Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")", 
						new RuntimeException());
				return null;
			}
			bitmap = Bitmap.createBitmap(cacheBitmap);
			// Restore the view
			comBitmap.setAlpha(alpha);
			comBitmap.destroyDrawingCache();
			comBitmap.setWillNotCacheDrawing(willNotCache);
			comBitmap.setDrawingCacheBackgroundColor(color);
		}
		return bitmap;
	}



	private String getAreaString(double dValue) {
		long area = Math.abs(Math.round(dValue));
		String sArea = "";
		// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
		if (area >= 1000000) {
			double dArea = area / 1000000.0;
			sArea = Double.toString(dArea) + " 平方公里";
		} else
			sArea = Double.toString(area) + " 平方米";

		return sArea;
	}

}
