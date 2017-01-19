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
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol;
import com.map.utils.MathTextLoaction;


/**
 * 测量距离所使用的类，这个是用于点击测量， 不适用于滑动测量
 * @author Administrator
 *
 */
public class DrawLine  extends MapOnTouchListener{

	private Context context;
	private MapView mMapView;
	private GraphicsLayer graphicsLayer;
	private Point startPoint = null;
	private int allId=-1;
    private double allLength=0;
	
	public DrawLine(Context context, MapView view) {
		super(context, view);
		this.context=context;
		this.mMapView=view;
		graphicsLayer=MainActivity.ghlayer;
	}

	@Override
	public boolean onSingleTap(MotionEvent e) {
		
		Point mapPt=mMapView.toMapPoint(new Point(e.getX(), e.getY()));
		//Point mapPt=new Point(e.getX(), e.getY());
	
		
		
		if(startPoint == null)
		{
			startPoint=mapPt;
			
			Graphic graphic = new Graphic(startPoint, 
					new SimpleMarkerSymbol(Color.RED,10, STYLE.CIRCLE));
			graphicsLayer.addGraphic(graphic);
							
		}
		else
		{
			// 生成当前线段（由当前点和上一个点构成）
			Line line = new Line();
			line.setStart(startPoint);
			line.setEnd(mapPt);
			
						
			Polyline polyline = new Polyline();
			polyline.addSegment(line, true);
						
			Graphic graphic = new Graphic(mapPt,
					new SimpleMarkerSymbol(Color.RED,10, STYLE.CIRCLE));
			graphicsLayer.addGraphic(graphic);
			
			graphicsLayer.addGraphic(new Graphic(polyline,
					new SimpleLineSymbol(Color.RED, 2,
							SimpleLineSymbol.STYLE.DASH)));
			
			
			
			Polyline pl=new Polyline();
			Line line1 = new Line();
			Point startPoint1=new Point(startPoint.getX(), startPoint.getY());
			line1.setStart(startPoint1);
			
			Point mapPt1=new Point(mapPt.getX(), mapPt.getY());
			line1.setEnd(mapPt1);

			pl.addSegment(line1, true);
			
			
			String length = Double.toString(Math.round(pl
					.calculateLength2D() )) + " 米";
			
			allLength+=Math.round(pl
					.calculateLength2D() );

			Log.i("长度：", length);
			
			
			//计算字体显示的位置
			Point textLocation=MathTextLoaction.mathTextLoaction(startPoint, mapPt);	
//			TextSymbol ts = new TextSymbol(12, length, Color.BLUE);
//			ts.setFontFamily("DroidSansFallback.ttf");  
//			ts.setOffsetX(0);
//			ts.setOffsetY(0);	
			
			
			View view1 = View.inflate(context, R.layout.showmaptext, null);
			TextView tv_show_text1 = (TextView) view1
					.findViewById(R.id.tv_show_text);
			tv_show_text1.setText(length);

			
			Bitmap bit1 = getViewBitmap(view1,300,100);
			Drawable drawable1 = new BitmapDrawable(bit1);
			PictureMarkerSymbol pic1 = new PictureMarkerSymbol(drawable1);
			pic1.setOffsetX(0);
			pic1.setOffsetY(0);
			
			
			Graphic gText = new Graphic(textLocation,pic1);
			graphicsLayer.addGraphic(gText);
			
			if(allId!=-1)
			{
			  graphicsLayer.removeGraphic(allId);
			}
			
//			TextSymbol allLengthText = new TextSymbol(16, "总长:"+allLength+"米", Color.BLUE);
//			allLengthText.setFontFamily("DroidSansFallback.ttf");  
//			allLengthText.setOffsetX(-10);
//			allLengthText.setOffsetY(-20);				
			
			
			View view = View.inflate(context, R.layout.showmaptext, null);
			TextView tv_show_text = (TextView) view
					.findViewById(R.id.tv_show_text);
			tv_show_text.setText("总长:"+allLength+"米");

			
			Bitmap bit = getViewBitmap(view,300,100);
			Drawable drawable = new BitmapDrawable(bit);
			PictureMarkerSymbol pic = new PictureMarkerSymbol(drawable);
			pic.setOffsetX(5);
			pic.setOffsetY(5);
			
			
			
			
			Graphic gallLength= new Graphic(mapPt,pic);
		    allId=graphicsLayer.addGraphic(gallLength);
						
			startPoint=mapPt;
			
		}
				
		
		return super.onSingleTap(e);
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
