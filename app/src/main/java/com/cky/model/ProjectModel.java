package com.cky.model;

import java.util.List;

import android.graphics.Bitmap;

/**
 * 工程信息模型
 * @author 祖正
 *
 */
public class ProjectModel {

	String  name;
	String endTime;
	String pref;
	List<String> bdImageList;
	List<String> pgImageList;
	Bitmap bdBitmap;
	Bitmap pgBitmap;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPref() {
		return pref;
	}
	public void setPref(String pref) {
		this.pref = pref;
	}
	public List<String> getBdImageList() {
		return bdImageList;
	}
	public void setBdImageList(List<String> bdImageList) {
		this.bdImageList = bdImageList;
	}
	public List<String> getPgImageList() {
		return pgImageList;
	}
	public void setPgImageList(List<String> pgImageList) {
		this.pgImageList = pgImageList;
	}
	public Bitmap getBdBitmap() {
		return bdBitmap;
	}
	public void setBdBitmap(Bitmap bdBitmap) {
		this.bdBitmap = bdBitmap;
	}
	public Bitmap getPgBitmap() {
		return pgBitmap;
	}
	public void setPgBitmap(Bitmap pgBitmap) {
		this.pgBitmap = pgBitmap;
	}
	

	
}
