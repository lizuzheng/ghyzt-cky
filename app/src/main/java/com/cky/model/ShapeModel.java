package com.cky.model;

import java.io.Serializable;
import java.util.List;

public class ShapeModel  implements Serializable{



	
	String CGraphicsId;
	String CGraphicsName;
	String CGraphicsType;
	String CGraphicsGeometry;
	String CGraphicsContent;
	String CGraphicsCreatNum;
	String CGraphicsCreatTime;
	String CGraphicsOrg;
	String CGraphicsIsShare;
	List<String> listImage;
	String anjianbianhao;
	String quyu;
	
	//数据导出的时候要用的字段
	boolean isSelect=false;
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public String getAnjianbianhao() {
		return anjianbianhao;
	}
	public void setAnjianbianhao(String anjianbianhao) {
		this.anjianbianhao = anjianbianhao;
	}
	public String getQuyu() {
		return quyu;
	}
	public void setQuyu(String quyu) {
		this.quyu = quyu;
	}
	public String getCGraphicsId() {
		return CGraphicsId;
	}
	public void setCGraphicsId(String cGraphicsId) {
		CGraphicsId = cGraphicsId;
	}
	public String getCGraphicsName() {
		return CGraphicsName;
	}
	public void setCGraphicsName(String cGraphicsName) {
		CGraphicsName = cGraphicsName;
	}
	public String getCGraphicsType() {
		return CGraphicsType;
	}
	public void setCGraphicsType(String cGraphicsType) {
		CGraphicsType = cGraphicsType;
	}
	public String getCGraphicsGeometry() {
		return CGraphicsGeometry;
	}
	public void setCGraphicsGeometry(String cGraphicsGeometry) {
		CGraphicsGeometry = cGraphicsGeometry;
	}
	public String getCGraphicsContent() {
		return CGraphicsContent;
	}
	public void setCGraphicsContent(String cGraphicsContent) {
		CGraphicsContent = cGraphicsContent;
	}
	public String getCGraphicsCreatNum() {
		return CGraphicsCreatNum;
	}
	public void setCGraphicsCreatNum(String cGraphicsCreatNum) {
		CGraphicsCreatNum = cGraphicsCreatNum;
	}
	public String getCGraphicsCreatTime() {
		return CGraphicsCreatTime;
	}
	public void setCGraphicsCreatTime(String cGraphicsCreatTime) {
		CGraphicsCreatTime = cGraphicsCreatTime;
	}
	public String getCGraphicsOrg() {
		return CGraphicsOrg;
	}
	public void setCGraphicsOrg(String cGraphicsOrg) {
		CGraphicsOrg = cGraphicsOrg;
	}
	public String getCGraphicsIsShare() {
		return CGraphicsIsShare;
	}
	public void setCGraphicsIsShare(String cGraphicsIsShare) {
		CGraphicsIsShare = cGraphicsIsShare;
	}
	public List<String> getListImage() {
		return listImage;
	}
	public void setListImage(List<String> listImage) {
		this.listImage = listImage;
	}
	
	
}
