package com.mapview.click;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.Callout;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

public class MapNullCick extends MapOnTouchListener{

	private MapView mMapview;
	private Point point;

	public MapNullCick(Context context, MapView view) {
		super(context, view);
	
		this.mMapview=view;
		
	}

	@Override
	public boolean onSingleTap(MotionEvent e) {
		final Callout callout = mMapview.getCallout();
		callout.hide();
		
		point = mMapview.toMapPoint(e.getX(), e.getY());
		
		System.out.println(point.getX()+"   "+point.getY());
		
		return super.onSingleTap(e);
		
		
	}
	
	
}
