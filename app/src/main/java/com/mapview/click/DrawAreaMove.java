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
import com.esri.core.symbol.TextSymbol;

public class DrawAreaMove extends MapOnTouchListener {

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Point startPoint = null;
	private Polygon poly;
	private boolean state = false;

	private int ghId = -1;
	private int textId;
	private int lineId;

	public DrawAreaMove(Context context, MapView view) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		this.ghlayer();
	}

	private void ghlayer() {
		graphicsLayer = MainActivity.ghlayer; 
	}

	@Override
	public boolean onSingleTap(MotionEvent e) {
		state = false;
		graphicsLayer.removeAll();
		return super.onSingleTap(e);
	}

	@Override
	public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

		Point point = mMapView.toMapPoint(to.getX(), to.getY());

		if (!state) {
			if (startPoint == null) {

				poly = new Polygon();
				Point p = mMapView.toMapPoint(from.getX(), from.getY());
				startPoint = p;
				poly.startPath(p);

			} else {
				
				if (ghId != -1)
				{
					graphicsLayer.removeGraphic(ghId);
					graphicsLayer.removeGraphic(lineId);
				}
				poly.lineTo(point);
				Graphic graphic = new Graphic(poly, new SimpleFillSymbol(
						Color.BLUE).setAlpha(30));
				ghId = graphicsLayer.addGraphic(graphic);
				
				Graphic gRadius = new Graphic(poly, new SimpleLineSymbol(Color.RED, 2));
				lineId=graphicsLayer.addGraphic(gRadius);

			}
			return false;
		} else {
			return super.onDragPointerMove(from, to);
		}

	}

	@Override
	public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

		Point point = mMapView.toMapPoint(to.getX(), to.getY());

		if (!state) {
			this.countArea(poly, point);
			startPoint = null;
			state = true;
			
			
//			// 将按钮复原
//			ViewState.removeAllState();
//			mMapView.setOnTouchListener(new QueryHuOnclick(context, mMapView));
//			ViewState.setText("测量", "测量");
			
			
			return false;
			
		} else {
			return super.onDragPointerUp(from, to);
		}
	}

	private void countArea(Polygon poly, Point point) {

		String sArea = "总面积:"+getAreaString(poly.calculateArea2D());
//		TextSymbol Area = new TextSymbol(15, "总面积:" + sArea, Color.BLUE);
//		Area.setFontFamily("DroidSansFallback.ttf");
//		Area.setOffsetX(0);
//		Area.setOffsetY(0);
//		Graphic gText = new Graphic(point, Area);
//		textId = graphicsLayer.addGraphic(gText);
		
		
		
		//String sArea = getAreaString(tempPolygon1.calculateArea2D());
		

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

		Envelope env = new Envelope();
		poly.queryEnvelope(env);
		Point center = env.getCenter();		
		
		Graphic gText = new Graphic(center, pic);
		textId = graphicsLayer.addGraphic(gText);
		

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
