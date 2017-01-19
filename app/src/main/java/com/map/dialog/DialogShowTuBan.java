package com.map.dialog;

import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cky.Adapter.GyydListAdpter;
import com.cky.ghyzt.R;

public class DialogShowTuBan {

	private static final int style = R.style.dialog;
	private static Dialog d = null;

	public static void openDialog(final Context context,
			List<Map<String, String>> listMap,String title) {

		View view = View.inflate(context, R.layout.dialog_gyyd, null);
		ListView list = (ListView) view.findViewById(R.id.listgyyd);

		if (listMap.size() < 7) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			list.setLayoutParams(params);
		}

		list.setAdapter(new GyydListAdpter(listMap, context));
		TextView show_tv_title = (TextView) view
				.findViewById(R.id.show_tv_title);
		show_tv_title.setText(title);

		d = new Dialog(context, style);
		d.setContentView(view);
		d.setCanceledOnTouchOutside(true);
		d.show();
	}
	
	
	public static View openDialogView(final Context context,
			List<Map<String, String>> listMap,String title) {

		View view = View.inflate(context, R.layout.dialog_gyyd, null);
		ListView list = (ListView) view.findViewById(R.id.listgyyd);

		if (listMap.size() < 7) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			list.setLayoutParams(params);
		}

		list.setAdapter(new GyydListAdpter(listMap, context));
		TextView show_tv_title = (TextView) view
				.findViewById(R.id.show_tv_title);
		show_tv_title.setText(title);

		return view;
	}

}
