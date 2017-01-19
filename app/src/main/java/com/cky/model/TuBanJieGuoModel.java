package com.cky.model;

import java.util.List;

public class TuBanJieGuoModel {
	
	String qushixian;
	String shuju;
	List<XiangZhengModel> model;
	Boolean isQuery;
	
	Boolean isShow=false;
	
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO 自动生成的方法存根
		return super.clone();
	}
	@Override
	public boolean equals(Object o) {
		// TODO 自动生成的方法存根
		return super.equals(o);
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO 自动生成的方法存根
		super.finalize();
	}
	@Override
	public int hashCode() {
		// TODO 自动生成的方法存根
		return super.hashCode();
	}
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return super.toString();
	}
	public Boolean getIsQuery() {
		return isQuery;
	}
	public void setIsQuery(Boolean isQuery) {
		this.isQuery = isQuery;
	}
	public List<XiangZhengModel> getModel() {
		return model;
	}
	public void setModel(List<XiangZhengModel> model) {
		this.model = model;
	}
	public String getQushixian() {
		return qushixian;
	}
	public void setQushixian(String qushixian) {
		this.qushixian = qushixian;
	}
	public String getShuju() {
		return shuju;
	}
	public void setShuju(String shuju) {
		this.shuju = shuju;
	}

}
