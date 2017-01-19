package com.mapview.click;

import com.esri.android.map.MapView;
import com.map.utils.MathUtil;

import android.content.Context;
import android.widget.TextView;

/**
 * 地图的范围大小控制组件
 * @author doudou
 *
 */
public class MapSize {
	
	private Context context;
	private  MapView mMapView;
	
	public MapSize(Context context,MapView mMapView)
	{
		this.context=context;
		this.mMapView=mMapView;
	}
	
	/**
	 * 放大
	 * @param context
	 * @param mMapView
	 */
	public static void setZoomOut(Context context,MapView mMapView)
	{
		mMapView.zoomout(true);
	}
	/**
	 * 缩小
	 * @param context
	 * @param mMapView
	 */
	public static void setZoomMin(Context context,MapView mMapView)
	{
		mMapView.zoomin(true);
	}
	
	/**
	 * 显示全图
	 * @param mMapView
	 */
	public static void allMap(MapView mMapView)
	{
		
		
	}
	
	
	/**
	 * 比例尺的计算
	 */
	public static  void countBilichi(MapView mMapview,TextView tv_bilichi) {
		double scale = mMapview.getScale();
		scale = scale / 100;
		String text = scale > 1000 ? MathUtil.round(scale / 1000, 2) + "公里"
				: MathUtil.round(scale, 2) + "米";
		tv_bilichi.setText(text);
	}
}
































