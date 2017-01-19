package com.cky.model;

import java.util.ArrayList;

public class PointInfo {

	private  String name;
	private  String beizhu;
	private String id;
	private String anjianbianhao;
	public String getAnjianbianhao() {
		return anjianbianhao;
	}
	public void setAnjianbianhao(String anjianbianhao) {
		this.anjianbianhao = anjianbianhao;
	}
	private String quyu;
	
	
	public String getQuyu() {
		return quyu;
	}
	public void setQuyu(String quyu) {
		this.quyu = quyu;
	}
	private ArrayList<String> listPath;
	
	
	
	public ArrayList<String> getListPath() {
		return listPath;
	}
	public void setListPath(ArrayList<String> listPath) {
		this.listPath = listPath;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	
	
	
	
}
