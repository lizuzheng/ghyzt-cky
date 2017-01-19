package com.cky.base;

import java.util.HashMap;
import java.util.Map;

import com.cky.model.ViewStateModel;
import com.cky.view.ImageBtn;
import android.view.View;

/**
 * 为了解决按钮的状态问题，需要创建一个容器，用于保存于哪些的状态需要被设置
 * 
 * 1、一个保存view的静态数组 2、添加view的静态方法 3、恢复初始状态的静态方法
 * 
 * 为了很容易调用，需要对每一个view进行命名
 * 
 * @author Administrator
 * 
 */
public class ViewState {

	/**
	 * 保存所有view的容器
	 */
	private static Map<String, ViewStateModel> map = new HashMap<String, ViewStateModel>();

	/**
	 * 向容器添加View
	 * 
	 * @param viewName
	 * @param view
	 */
	public static void addView(String viewName, View view, int onclick,
			int unclick, String text, Boolean isSinger) {
		ViewStateModel model = new ViewStateModel();
		model.setName(viewName);
		model.setView(view);
		model.setState(false);
		model.setOnclick(onclick);
		model.setUnclick(unclick);
		model.setText(text);
		model.setIsSinger(isSinger);
		map.put(viewName, model);

	}
	
	/**
	 * 向容器添加View
	 * 
	 * @param viewName
	 * @param view
	 */
	public static void addView(String viewName, View view, int onclick,
			int unclick, String text) {
		ViewStateModel model = new ViewStateModel();
		model.setName(viewName);
		model.setView(view);
		model.setState(false);
		model.setOnclick(onclick);
		model.setUnclick(unclick);
		model.setText(text);
		model.setIsSinger(false);
		map.put(viewName, model);

	}

	/**
	 * 移除所有按钮的状态
	 * 
	 * @param id
	 */
	public static void removeAllState() {
		for (String s : map.keySet()) {
			ViewStateModel model = map.get(s);
			if (!model.getIsSinger()) {
				model.setState(false);
				View view = model.getView();
				view.setBackgroundResource(model.getUnclick());
				if (model.getText() != null) {
					setText(model.getName(), model.getText());
				}
			}

		}
	}

	/**
	 * 获取一个按钮此时的状态
	 * 
	 * @param viewName
	 * @return
	 */
	public static Boolean getViewState(String viewName) {
		ViewStateModel model = map.get(viewName);
		return model.getState();
	}

	/**
	 * 为一个按钮添加点击状态
	 * 
	 * @param viewName
	 * @param fosId
	 */
	public static void setViewOnclick(String viewName) {
		
		ViewStateModel model = map.get(viewName);
		if(!model.getIsSinger())
		{		
		// 首先将其他的全部回复到初始状态
		removeAllState();
		// 再将这一个按钮设置为点击状态
		}
		
		model.setState(true);
		View view = model.getView();
		view.setBackgroundResource(model.getOnclick());

	}

	public static void setViewOnclick(String viewName, Boolean isSinger) {
		// 再将这一个按钮设置为点击状态
		ViewStateModel model = map.get(viewName);
		model.setState(true);
		View view = model.getView();
		view.setBackgroundResource(model.getOnclick());

	}

	/**
	 * 为一个按钮移除点击状态事件
	 * 
	 * @param viewName
	 * @param unId
	 * @param fosId
	 */
	public static void setViewUn(String viewName) {
		ViewStateModel model = map.get(viewName);
		model.setState(false);
		View view = model.getView();
		view.setBackgroundResource(model.getUnclick());

	}

	public static void setText(String viewName, String text) {
		ImageBtn ib = (ImageBtn) (map.get(viewName).getView());
		ib.setTextViewText(text);
	}

}
