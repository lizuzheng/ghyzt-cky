package com.map.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cky.Adapter.SelectQuYuAdpter;
import com.cky.common.DiYuHelpler;
import com.cky.ghyzt.R;

public class Dialog_SelectQuYu {
	
	private static final int style = R.style.dialog;
	private static Dialog d=null;

	
	public static void openDialog( final Context context, final EditText edit_caijitiaojian_quyu)
	{
	
		View view = View.inflate(context,R.layout.dialog_selectquyu, null);
		d = new Dialog(context, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		
		//listview控件
		ListView lv_quyu=(ListView) view.findViewById(R.id.lv_quyu);
		lv_quyu.setAdapter(new SelectQuYuAdpter(context,DiYuHelpler.getAllDiYU()));
		
		//创建事件
		lv_quyu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				edit_caijitiaojian_quyu.setText(DiYuHelpler.getAllDiYU()[position]);
				d.dismiss();
			}
		});
		
	}
	
	public static void openDialog( final Context context, final Button edit_caijitiaojian_quyu)
	{
	
		View view = View.inflate(context,R.layout.dialog_selectquyu, null);
		d = new Dialog(context, style);		
		d.setContentView(view);	
		d.setCanceledOnTouchOutside(true);
		d.show();
		
		
		//listview控件
		ListView lv_quyu=(ListView) view.findViewById(R.id.lv_quyu);
		lv_quyu.setAdapter(new SelectQuYuAdpter(context,DiYuHelpler.getAllDiYU2()));
		
		//创建事件
		lv_quyu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				edit_caijitiaojian_quyu.setText(DiYuHelpler.getAllDiYU2()[position]);
				d.dismiss();
			}
		});
		
	}

}
