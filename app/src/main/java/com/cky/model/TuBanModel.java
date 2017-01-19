package com.cky.model;

import java.io.Serializable;

public class TuBanModel implements Serializable{

	// "OBJECTID_1": "1",
	// "OBJECTID": "0",
	// "Id": "0",
	// "备注": "玉堂01",
	// "Shape_Leng": "0.00000000",
	// "bz": "YUTANG01",
	// "编号": "CDDCTB0001-201603/33-DJY15/01-YUTANG01",
	// "区市县": "都江堰市",
	// "乡镇": "玉堂镇",
	// "位置": "都汶高速以北，彩虹大道以南，岷江河以西，黑石河以东",
	// "变化年份": "2014年-2015年",
	// "图斑面积": "74054.00000000",
	// "Shape": "1"

	String oBJECTID_1;
	String oBJECTID;
	String Id;
	String beizhu;
	String bz;
	String bianhao;
	String qushixian;
	String xiangzheng;
	String weizhi;
	String bianhuanianfen;
	String tubanmianji;

	public String getoBJECTID_1() {
		return oBJECTID_1;
	}

	public void setoBJECTID_1(String oBJECTID_1) {
		this.oBJECTID_1 = oBJECTID_1;
	}

	public String getoBJECTID() {
		return oBJECTID;
	}

	public void setoBJECTID(String oBJECTID) {
		this.oBJECTID = oBJECTID;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getBianhao() {
		return bianhao;
	}

	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}

	public String getQushixian() {
		return qushixian;
	}

	public void setQushixian(String qushixian) {
		this.qushixian = qushixian;
	}

	public String getXiangzheng() {
		return xiangzheng;
	}

	public void setXiangzheng(String xiangzheng) {
		this.xiangzheng = xiangzheng;
	}

	public String getWeizhi() {
		return weizhi;
	}

	public void setWeizhi(String weizhi) {
		this.weizhi = weizhi;
	}

	public String getBianhuanianfen() {
		return bianhuanianfen;
	}

	public void setBianhuanianfen(String bianhuanianfen) {
		this.bianhuanianfen = bianhuanianfen;
	}

	public String getTubanmianji() {
		return tubanmianji;
	}

	public void setTubanmianji(String tubanmianji) {
		this.tubanmianji = tubanmianji;
	}

}
