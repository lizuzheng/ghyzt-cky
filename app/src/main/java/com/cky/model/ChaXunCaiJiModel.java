package com.cky.model;

import java.io.Serializable;
import java.util.List;

import com.esri.core.map.FeatureSet;

public class ChaXunCaiJiModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3071857949622664730L;

	//是否有保存信息
	Boolean isSave;

	//哪个选项卡
	String xuanxiangka;
	
	//开始时间
	String startTime;
	
	//结束时间
	String endTime;
	
	//搜索条件
	String caijitiaojian;
	
	//采集列表
	List<ShapeModel> listModel_caiji;
	
	//采集列表所有的
	List<ShapeModel> listModel_caiji_all;
	
	//图斑形状点
	FeatureSet result;
	
	public FeatureSet getResult() {
		return result;
	}

	public void setResult(FeatureSet result) {
		this.result = result;
	}

	public List<ShapeModel> getListModel_caiji_all() {
		return listModel_caiji_all;
	}

	public void setListModel_caiji_all(List<ShapeModel> listModel_caiji_all) {
		this.listModel_caiji_all = listModel_caiji_all;
	}

	//区县
	String quxian;
	
	//图斑搜索条件
	String sousuotiaojiantuban;
	
	//图斑列表
	List<TuBanModel> listModel;

	public Boolean getIsSave() {
		return isSave;
	}

	public void setIsSave(Boolean isSave) {
		this.isSave = isSave;
	}

	public String getXuanxiangka() {
		return xuanxiangka;
	}

	public void setXuanxiangka(String xuanxiangka) {
		this.xuanxiangka = xuanxiangka;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCaijitiaojian() {
		return caijitiaojian;
	}

	public void setCaijitiaojian(String caijitiaojian) {
		this.caijitiaojian = caijitiaojian;
	}

	public List<ShapeModel> getListModel_caiji() {
		return listModel_caiji;
	}

	public void setListModel_caiji(List<ShapeModel> listModel_caiji) {
		this.listModel_caiji = listModel_caiji;
	}

	public String getQuxian() {
		return quxian;
	}

	public void setQuxian(String quxian) {
		this.quxian = quxian;
	}

	public String getSousuotiaojiantuban() {
		return sousuotiaojiantuban;
	}

	public void setSousuotiaojiantuban(String sousuotiaojiantuban) {
		this.sousuotiaojiantuban = sousuotiaojiantuban;
	}

	public List<TuBanModel> getListModel() {
		return listModel;
	}

	public void setListModel(List<TuBanModel> listModel) {
		this.listModel = listModel;
	}
	
	

}
