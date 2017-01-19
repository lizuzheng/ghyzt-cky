package com.map.utils;

import android.os.AsyncTask;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.map.utils.LayerIdentTaskUtil.Iresult;


/**
 * 对图层查询的一个类，这是一个在原有基础上重写的方法，添加了一个接口，为了执行完成后及时的调用
 * 在上一个方法中，没有回调代码还是正常执行，不知道为什么
 * @author Administrator
 *
 */
public class LayerIdentTaskUtil {

	
	/**
	 * 创建一个接口，用于执行完成后回调该函数
	 * @author Administrator
	 *
	 */
	public interface Iresult
	{
		public void  result(IdentifyResult[] results);
	}
	
	/**
	 * 查询方法，用于构建参数
	 * @param url 地址
	 * @param mMapView 地图
	 * @param geometry
	 * @param wucha 误差
	 * @param layerId 图层id
	 * @param iresult 回调接口
	 */
	public static void queryTask(String url, MapView mMapView, Geometry geometry,int wucha,int[] layerId,Iresult iresult) {
		final IdentifyParameters params = new IdentifyParameters();
		params.setTolerance(wucha);
		params.setDPI(98);
		params.setLayers(layerId);
		params.setLayerMode(IdentifyParameters.ALL_LAYERS);
	
		params.setGeometry(geometry);
		params.setSpatialReference(mMapView.getSpatialReference());
		params.setMapHeight(mMapView.getHeight());
		params.setMapWidth(mMapView.getWidth());

		Envelope env = new Envelope();
		mMapView.getExtent().queryEnvelope(env);
		params.setMapExtent(env);

		// 我们自己扩展的异步类
		MyLayerask mTask = new MyLayerask(url,iresult);
		mTask.execute(params);
	
	}
	
}

/**
 * 异步查询类，用于查询图层的数据
 * @author Administrator
 *
 */
class MyLayerask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

	String url;
	IdentifyTask mIdentifyTask;
	private Iresult iresult;
	MyLayerask( String url,Iresult result) {		
		this.url = url;
		this.iresult=result;
	}
	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		IdentifyResult[] mResult = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				mResult = mIdentifyTask.execute(mParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mResult;
	}
	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		iresult.result(results);
	}
	@Override
	protected void onPreExecute() {
		mIdentifyTask = new IdentifyTask(url);
	}
}