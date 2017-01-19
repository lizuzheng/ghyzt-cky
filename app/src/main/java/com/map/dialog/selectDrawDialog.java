package com.map.dialog;

import com.cky.base.ViewState;
import com.cky.ghyzt.R;
import com.esri.android.map.MapView;
import com.mapview.click.DrawAllPoint;
import com.mapview.click.DrawAreaPoint;
import com.mapview.click.DrawLinePoint;
import com.mapview.click.DrawPointSave;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * 显示选择绘制图形弹出框
 * @author doudou
 *
 */
public class selectDrawDialog {
	
	private static final int style = R.style.dialog;
	private static Dialog d=null;
	private static Context context;
	
	public static void openDialog(final Activity activity1, final Context context1,final MapView mMapview)
	{

		context=context1;		
		LayoutInflater inflater = activity1.getLayoutInflater();
		View view = inflater.inflate(R.layout.seletedraw_layer, null);
		d = new Dialog(context1, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		Button dian=(Button) view.findViewById(R.id.select_draw_dian);
		dian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				mMapview.setOnTouchListener(new DrawPointSave(context, mMapview,activity1));
				viewState(context1,"上报（绘制点）");
			}
		});
		
		Button xian=(Button) view.findViewById(R.id.select_draw_xian);
		xian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				mMapview.setOnTouchListener(new DrawLinePoint(context, mMapview,activity1));
				viewState(context1,"上报（绘制线）");
			}
		});

		Button mian=(Button) view.findViewById(R.id.select_draw_mian);
		mian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.dismiss();
				mMapview.setOnTouchListener(new DrawAreaPoint(context, mMapview,activity1));
				viewState(context1,"上报（绘制面）");
			}
		});		
		
		CheckBox select_all=(CheckBox) view.findViewById(R.id.select_draw_showall);
		select_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				d.dismiss();
							
				ViewState.removeAllState();
				mMapview.setOnTouchListener(new DrawAllPoint(context1, mMapview));
				viewState(context1,"上报（查看保存点信息）");
			}
		});
		
	}
	
	
	
	private static void viewState(Context context,String text)
	{
		ViewState.setViewOnclick("上报");
		ViewState.setText("上报", text);
	}
	
	
}

	





















