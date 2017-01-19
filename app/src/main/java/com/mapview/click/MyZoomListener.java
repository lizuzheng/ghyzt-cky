package com.mapview.click;

import android.widget.TextView;

import com.cky.ghyzt.MainActivity;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;

public class MyZoomListener implements OnZoomListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1574775721300595703L;
	
	private TextView tv_zoom;	
	private MapView mMapview;
	public  MyZoomListener(TextView tv_zoom,MapView mMapview)
	{
		this.tv_zoom=tv_zoom;
		this.mMapview=mMapview;
	}

	@Override
	public void postAction(float pivotX, float pivotY, double factor) {
		
		MapSize.countBilichi(mMapview, MainActivity.tv_bilichi);
	}

	@Override
	public void preAction(float pivotX, float pivotY, double factor) {
	  
		MapSize.countBilichi(mMapview, MainActivity.tv_bilichi);
	}

}
