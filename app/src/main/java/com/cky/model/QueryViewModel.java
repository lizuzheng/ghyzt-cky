package com.cky.model;

import java.io.Serializable;
import java.util.List;

public class QueryViewModel implements Serializable {
	
	String text;
	String itemText;
	List<AutoTextModel> listAutoTextModel;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getItemText() {
		return itemText;
	}
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}
	public List<AutoTextModel> getListAutoTextModel() {
		return listAutoTextModel;
	}
	public void setListAutoTextModel(List<AutoTextModel> listAutoTextModel) {
		this.listAutoTextModel = listAutoTextModel;
	}

}
