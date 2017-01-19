package com.cky.model;

import java.io.Serializable;
import java.util.List;

public class CaiJiDianDaoChuStateModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7543750014433626395L;
	private List<ShapeModel> listModelAll;
	private List<ShapeModel> listModelSelect;

	public List<ShapeModel> getListModelAll() {
		return listModelAll;
	}

	public void setListModelAll(List<ShapeModel> listModelAll) {
		this.listModelAll = listModelAll;
	}

	public List<ShapeModel> getListModelSelect() {
		return listModelSelect;
	}

	public void setListModelSelect(List<ShapeModel> listModelSelect) {
		this.listModelSelect = listModelSelect;
	}

}
