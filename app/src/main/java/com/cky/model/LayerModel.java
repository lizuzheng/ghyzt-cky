package com.cky.model;

import com.esri.android.map.Layer;

/**
 * 修改2015-10-13 
 * @author Administrator
 *
 */
public class LayerModel
{
	Layer layer;
	Boolean isQuery;
	String queryUrl;
	String[] quertFiled;
	String[] showFiled;
	
	int[] layerId;
	
	String style;
	
	public int[] getLayerId() {
		return layerId;
	}
	public void setLayerId(int[] layerId) {
		this.layerId = layerId;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Layer getLayer() {
		return layer;
	}
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
	public Boolean getIsQuery() {
		return isQuery;
	}
	public void setIsQuery(Boolean isQuery) {
		this.isQuery = isQuery;
	}
	public String getQueryUrl() {
		return queryUrl;
	}
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	public String[] getQuertFiled() {
		return quertFiled;
	}
	public void setQuertFiled(String[] quertFiled) {
		this.quertFiled = quertFiled;
	}
	public String[] getShowFiled() {
		return showFiled;
	}
	public void setShowFiled(String[] showFiled) {
		this.showFiled = showFiled;
	}
	
	
	
		
}