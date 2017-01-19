package com.cky.model;

import java.util.List;


/**
 * 房屋数据模型
 * @author lzz
 *
 */
public class HouseModel {
	
	String  xiangmu;
	String quyu;
	String niandai;
	String menpaihao;
	String jianzhumingcheng;
	String dixiashi;
	String zongcengshu;
	String state;
	String beizhu;
	
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZongcengshu() {
		return zongcengshu;
	}
	public void setZongcengshu(String zongcengshu) {
		this.zongcengshu = zongcengshu;
	}
	List<String> cengList;
	public List<String> getCengList() {
		return cengList;
	}
	public void setCengList(List<String> cengList) {
		this.cengList = cengList;
	}
	public String getXiangmu() {
		return xiangmu;
	}
	public void setXiangmu(String xiangmu) {
		this.xiangmu = xiangmu;
	}
	public String getQuyu() {
		return quyu;
	}
	public void setQuyu(String quyu) {
		this.quyu = quyu;
	}
	public String getNiandai() {
		return niandai;
	}
	public void setNiandai(String niandai) {
		this.niandai = niandai;
	}
	public String getMenpaihao() {
		return menpaihao;
	}
	public void setMenpaihao(String menpaihao) {
		this.menpaihao = menpaihao;
	}
	public String getJianzhumingcheng() {
		return jianzhumingcheng;
	}
	public void setJianzhumingcheng(String jianzhumingcheng) {
		this.jianzhumingcheng = jianzhumingcheng;
	}
	public String getDixiashi() {
		return dixiashi;
	}
	public void setDixiashi(String dixiashi) {
		this.dixiashi = dixiashi;
	}

}
