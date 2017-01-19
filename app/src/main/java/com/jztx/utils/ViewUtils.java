package com.jztx.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 视图操作类
 * @author Beike
 *
 */
public class ViewUtils {
	
	/**
	 * 将一个布局文件转化成View
	 * @param context
	 * @param id
	 * @return
	 */
	public static View getView(Context context,int id)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(id, null);	
		 view.measure(MeasureSpec.makeMeasureSpec(PixelUtils.dipTopx(context, 60f),
	                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
	                PixelUtils.dipTopx(context, 80f), MeasureSpec.EXACTLY));
	        // 这个方法也非常重要，设置布局的尺寸和位置
	        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		return view;
	}

}
