package com.map.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cky.base.ViewState;
import com.cky.ghyzt.R;
import com.esri.android.map.MapView;
import com.mapview.click.DrawArea;
import com.mapview.click.DrawAreaMove;
import com.mapview.click.DrawLine;
import com.mapview.click.DrawLineMove;

public class MeasureDialog {
   

	
	public static void openDialog(final Context context,final MapView mMapView)
	{

		View view = View.inflate(context,R.layout.measure_layer, null);
		final Dialog d = new Dialog(context, R.style.dialog);	
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		Button length=(Button) view.findViewById(R.id.measure_juli);
		length.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				mMapView.setOnTouchListener(new DrawLine(context,mMapView));
				MeasureDialog.viewState(context,"测距中");
				
			}
		});
		
		
		Button mianji=(Button) view.findViewById(R.id.measure_mianji);
		mianji.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.dismiss();

				mMapView.setOnTouchListener(new DrawArea(context,mMapView));
				MeasureDialog.viewState(context,"测面积");
				
			}
		});
		
		
		Button length_huadong=(Button) view.findViewById(R.id.measure_juli_huadong);
		length_huadong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				mMapView.setOnTouchListener(new DrawLineMove(context,mMapView));
				MeasureDialog.viewState(context,"测距中");
				
			}
		});
		
		
		Button mianji_haudong=(Button) view.findViewById(R.id.measure_mianji_haudong);
		mianji_haudong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.dismiss();

				mMapView.setOnTouchListener(new DrawAreaMove(context,mMapView));
				MeasureDialog.viewState(context,"测面积");
				
			}
		});
	}
	
	private static void viewState(Context context,String text)
	{
		ViewState.setViewOnclick("测量");
		ViewState.setText("测量", text);
	}
	
}
