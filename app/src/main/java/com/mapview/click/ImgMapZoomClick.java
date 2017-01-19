package com.mapview.click;

import android.content.Context;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;

public class ImgMapZoomClick implements OnZoomListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436422995331418583L;
	private Context context;
	private MapView map1;
	private MapView map2;


	public ImgMapZoomClick(Context context,MapView map1,MapView map2)
	{
		this.context=context;
		this.map1=map1;
		this.map2=map2;
	}
	
	
	@Override
	public void postAction(float pivotX, float pivotY, double factor) {
		double scale=map1.getScale();
		map2.setScale(scale,true);
		
	}

	@Override
	public void preAction(float pivotX, float pivotY, double factor) {
		double scale=map1.getScale();
		map2.setScale(scale,true);
	}

}
