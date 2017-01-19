package com.cky.model;

import java.io.Serializable;

import com.esri.core.geometry.Point;

/**
 * 查询的时候自动匹配返回的结果模型
 * @author Administrator
 *
 */
public class AutoTextModel implements Serializable{
	
//	{"category":"POI","keyword":"天府新区成都片区管委会TIANFUXINQUCHENGDUPIANQUGUANWEIHUITFXQCDPQGWH",
//		"label":"天府新区成都片区管委会","objid":"114487","pinyin":"TIANFUXINQUCHENGDUPIANQUGUANWEIHUI",
//		"shortpinyin":"TFXQCDPQGWH","type":"县级政府","x":"219542.82490000000","y":"203832.53430000000"}
	

	String category;
	String keyword;
	String label;
	String objid;
	String pinyin;
	String shortpinyin;
	String type;
    Point point;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getObjid() {
		return objid;
	}
	public void setObjid(String objid) {
		this.objid = objid;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getShortpinyin() {
		return shortpinyin;
	}
	public void setShortpinyin(String shortpinyin) {
		this.shortpinyin = shortpinyin;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}

	
	
	
}
