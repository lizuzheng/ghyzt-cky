package com.map.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.cky.ghyzt.R;

public class Dialog_CaiJiTongJiFangShi {

	private static final int style = R.style.dialog;
	private static Dialog d=null;

	
	public static void openDialog( final Context context,final resultCaiJiFangShi ifangshi)
	{
	

		View view = View.inflate(context,R.layout.dialog_tongjifangshi, null);
		d = new Dialog(context, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		Button dian=(Button) view.findViewById(R.id.select_time);
		dian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				ifangshi.result("时间");
			}
		});
		
		Button xian=(Button) view.findViewById(R.id.select_quyu);
		xian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				d.dismiss();
				ifangshi.result("区域");
				
			}
		});

		Button mian=(Button) view.findViewById(R.id.select_caijiren);
		mian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.dismiss();
				ifangshi.result("采集人");
				
			}
		});		
			
	}
	
	public interface resultCaiJiFangShi
	 {
	 	void result(String result);
	 }
	
}



