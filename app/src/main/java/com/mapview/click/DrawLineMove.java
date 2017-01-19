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

import com.cky.base.ViewState;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.map.utils.VersionCommon;

/**
 * 测量距离所使用的类，这个是用于滑动测量， 不适用于点击
 * 
 * @author Administrator
 * 
 */

public class DrawLineMove extends MapOnTouchListener {

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private boolean isFirst = true;
	private Point startPoint;
	private Polyline poly;
	private int ghlayerId = -1;

	public DrawLineMove(Context context, MapView view) {
		super(context, view);
		this.context = context;
		this.mMapView = view;
		this.graphicsLayer = MainActivity.ghlayer;

	}

	@Override
	public boolean onSingleTap(MotionEvent point) {

		isFirst = true;
		return super.onSingleTap(point);
	}

	@Override
	public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

		Point mapPt = mMapView.toMapPoint(to.getX(), to.getY());
		if (isFirst) {

			if (startPoint == null) {
				poly = new Polyline();

				startPoint = mMapView.toMapPoint(from.getX(), from.getY());
				poly.startPath(startPoint);

				Graphic ep = new Graphic(startPoint, new SimpleMarkerSymbol(
						Color.RED, 10, STYLE.CIRCLE));
				graphicsLayer.addGraphic(ep);

			} else {

				poly.lineTo(mapPt);
				if (ghlayerId != -1) {
					graphicsLayer.removeGraphic(ghlayerId);
				}
				ghlayerId = graphicsLayer.addGraphic(new Graphic(poly,
						new SimpleLineSymbol(Color.RED, 2)));

			}

		} else {
			return super.onDragPointerMove(from, to);
		}

		return true;

	}

	@Override
	public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

		if (isFirst) {
			Point endPoint = mMapView
					.toMapPoint(new Point(to.getX(), to.getY()));
			Graphic ep = new Graphic(endPoint, new SimpleMarkerSymbol(
					Color.RED, 10, STYLE.CIRCLE));
			startPoint = null;
			ghlayerId=-1;
			graphicsLayer.addGraphic(ep);
			isFirst = false;

			this.showText(poly,endPoint);
			
			
			// 将按钮复原

			ViewState.removeAllState();
			ViewState.setText("测量", "测量");
			
		}

		return super.onDragPointerUp(from, to);
	}

	private void showText(MultiPath poly2, Point endPoint) {
		
		
		
		String length = Double
				.toString(Math.round(poly.calculateLength2D()))+"米";
		TextSymbol allLengthText=null;
		
		
		//绘制字体
		View view = View.inflate(context, R.layout.showmaptext, null);
		TextView tv_show_text = (TextView) view
				.findViewById(R.id.tv_show_text);
		tv_show_text.setText(length);
		tv_show_text.setWidth(400);

		Bitmap bit = getViewBitmap(view,400,100);

		Drawable drawable = new BitmapDrawable(bit);
		PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
		pic.setOffsetX(50);
		pic.setOffsetY(20);
		
		Envelope env = new Envelope();
		poly2.queryEnvelope(env);
		Point center = env.getCenter();	

		Graphic gText = new Graphic(center, pic);
		graphicsLayer.addGraphic(gText);
		
		
//		
//		if(VersionCommon.getReleade()!=5)
//		{
//		allLengthText = new TextSymbol(16, length + "米",
//					Color.BLUE);
//		  allLengthText.setFontFamily("DroidSansFallback.ttf");
//		}
//		else
//		{
//			allLengthText = new TextSymbol(16, length + " Rice",
//					Color.BLUE);
//		}
//		allLengthText.setOffsetX(-10);
//		allLengthText.setOffsetY(-20);
//		Graphic gallLength = new Graphic(endPoint, allLengthText);
//		graphicsLayer.addGraphic(gallLength);
		

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

}
