package com.cky.model;

import android.view.View;


/**
 * 这是一个极佳的解决方案，为了解决按钮点击的状态问题
 * @author Administrator
 *
 */
public class ViewStateModel {
	
	String Name;
	View view;
	Boolean state;
	Boolean isSinger;
	public Boolean getIsSinger() {
		return isSinger;
	}
	public void setIsSinger(Boolean isSinger) {
		this.isSinger = isSinger;
	}
	int onclick;
	int unclick;
	
	String text;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getOnclick() {
		return onclick;
	}
	public void setOnclick(int onclick) {
		this.onclick = onclick;
	}
	public int getUnclick() {
		return unclick;
	}
	public void setUnclick(int unclick) {
		this.unclick = unclick;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}

	
	
	
}
