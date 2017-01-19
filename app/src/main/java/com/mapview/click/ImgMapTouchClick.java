package com.mapview.click;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

public class ImgMapTouchClick extends MapOnTouchListener{

	private MapView map1;
	private MapView map2;

	public ImgMapTouchClick(Context context, MapView view) {
		super(context, view);
		
	}

	public ImgMapTouchClick(Context context, MapView map1,MapView map2)
	{
		
		super(context, map1);
		this.map1=map1;
		this.map2=map2;
	}

	@Override
	public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
		
		
		Point point=map1.getCenter();
		map2.centerAt(point, true);
		
		return super.onDragPointerMove(from, to);
	}
	
	
}
