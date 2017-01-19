package com.cky.common;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

import android.os.AsyncTask;

/**
 * 执行地图查询的类
 * 
 * @author Beike
 * 
 */
public  class MyMapQuery extends AsyncTask<String, Integer, FeatureSet> {
	private Geometry geometry;
	private String where;
	private String Url;
	private ReturnResult returnResult;

	// 这三个参数分别是，查询的文本，进度，返回结果

	@SuppressWarnings("deprecation")
	@Override
	protected FeatureSet doInBackground(String... arg0) {

		QueryTask queryTask;
		Query query = new Query();

		query.setMaxFeatures(1000);

		// 设置查询范围
		query.setGeometry(geometry);

		query.setReturnGeometry(true);

		query.setOutFields(new String[] { "*" });

		queryTask = new QueryTask(Url);

		query.setWhere(where);

		FeatureSet fs = null;
		try {
			fs = queryTask.execute(query);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return fs;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(FeatureSet result) {

		this.returnResult.onPostExecute(result);

		super.onPostExecute(result);
	}

	/**
	 * 为返回结果设置一个回调
	 * 
	 * @param returnResult
	 * @return
	 */
	public ReturnResult setReturnResult(ReturnResult returnResult) {
		this.returnResult = returnResult;
		return this.returnResult;
	}

	/**
	 *  抽象接口，方便进行查询结果的回调
	 * @author Beike
	 *
	 */
	public interface ReturnResult {
		void onPostExecute(FeatureSet result);
	}

	/**
	 * 设置查询的范围
	 * 
	 * @param geometry
	 * @return
	 */
	public Geometry setGeometry(Geometry geometry) {
		return this.geometry = geometry;
	}

	/**
	 * 这是查询的内容
	 * 
	 * @param where
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * 设置查询的网址
	 * 
	 * @param Url
	 */
	public void setURL(String Url) {
		this.Url = Url;
	}

}
