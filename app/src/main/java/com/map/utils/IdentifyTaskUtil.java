package com.map.utils;

import android.os.AsyncTask;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

/**
 * 对点击点查询的类
 * 
 * @author Beike
 * 
 */
public class IdentifyTaskUtil {

	public static IdentifyResult[] queryTask(String url, MapView mMapView, Geometry geometry,int wucha,int[] layerId) {
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
		MyIdentifyTask mTask = new MyIdentifyTask( url);
		AsyncTask<IdentifyParameters, Void, IdentifyResult[]> a = mTask.execute(params);
		try {
			IdentifyResult[] results = a.get();
				return results;
		} catch (Exception e) {}
		
		return null;
	}
}

/**
 * 异步查询类
 * 
 * @author Beike
 * 
 */
class MyIdentifyTask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

	String url;
	IdentifyTask mIdentifyTask;
	MyIdentifyTask( String url) {
		
		this.url = url;
	}
	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		IdentifyResult[] mResult = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				// 获取要素数据
				mResult = mIdentifyTask.execute(mParams);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return mResult;
	}
	@Override
	protected void onPostExecute(IdentifyResult[] results) {

	}
	@Override
	protected void onPreExecute() {
		// 实例化一个要素识别类对象
		mIdentifyTask = new IdentifyTask(url);
	}

}
