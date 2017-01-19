package com.map.dialog;

import com.cky.ghyzt.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * 显示弹出框的通用方法
 * @author Administrator
 *
 */
public class DialogUtils {

	private static final int style = R.style.dialog;
	private static Dialog d=null;
	
	public static void showDialog(Context context,View view)
	{
		
		d = new Dialog(context, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
	}
	
	public static void showDialog(Context context,Boolean isShut,View view)
	{
		
		d = new Dialog(context, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(isShut);
		d.show();
	}
	
	
	public static void closeDialog()
	{
		d.dismiss();
	}
}
