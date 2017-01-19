package com.map.dialog;



import com.cky.ghyzt.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class LoadDialog {
   
	private static Dialog d=null;
	private static TextView tv_load_txt;
	
	public static void show(final Context context,final Boolean isOutside,final String text)
	{
		
		View view = View.inflate(context, R.layout.loading_layer, null);
		d = new Dialog(context, R.style.dialog);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(isOutside);
		d.setCancelable(isOutside);
		d.show();
		
	    tv_load_txt=(TextView) view.findViewById(R.id.tv_load_txt);
	    tv_load_txt.setText(text);
	    
	}
	public  static void settext(String text)
	{
		tv_load_txt.setText(text);
	}
	
	public static void close()
	{
		d.dismiss();
	}
}
