package com.map.dialog;

import java.util.Calendar;
import java.util.List;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.ghyzt.R;
import com.cky.ghyzt.WebViewDemo;
import com.cky.model.GPSRrecordModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.map.utils.SaveTextUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class MoreDilog {

	public static void openDialog(final Activity activity, final Context context) {
		View meun = View.inflate(context, R.layout.my_meun_dialog, null);

		Button shiyongshouce=(Button) meun.findViewById(R.id.shiyongshouce);
		shiyongshouce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(context, WebViewDemo.class);
				intent.putExtra("URL", "file:///android_asset/html.html");
				context.startActivity(intent);

			}
		});


		Button changePwd = (Button) meun.findViewById(R.id.changePwd);
		changePwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Dialog_updatePass.openDialog(context);
			}
		});

		Button existBtn = (Button) meun.findViewById(R.id.existBtn);
		existBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				activity.finish();
				System.exit(0);

			}
		});

		//gps数据的导出
		Button gpsup = (Button) meun.findViewById(R.id.gpsup);
		gpsup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Toast.makeText(context, "后台开始导出中，请继续操作！", Toast.LENGTH_SHORT).show();
				daochu(context);
				DialogUtils.closeDialog();

			}
		});

		//gps数据的删除
		Button gpsDelete=(Button) meun.findViewById(R.id.gpsdelete);
		gpsDelete.setOnClickListener(new OnClickListener() {
			Calendar c = Calendar.getInstance();
			@Override
			public void onClick(View v) {
				
				// 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
				new DoubleDatePickerDialog(context, 0, new DoubleDatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
							int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
							int endDayOfMonth) {
				
						//执行删除操作
						String startTime=startYear+"-"+(startMonthOfYear + 1)+"-"+startDayOfMonth;
						String endTTime=endYear+"-"+( endMonthOfYear + 1)+"-"+endDayOfMonth;
						
					
						//执行请求
						gpsDelete(context, startTime, endTTime);
						
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
			}
			
		});
		
		
		
		DialogUtils.showDialog(context, meun);
	}

	private static void gpsDelete(final Context context,String startTime,String endTime)
	{
		String Url = MyAppliaction.AppUrl + "deleteGPS.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("GPSUserNum",UserSingle.getSingle().userModel.getUserID());
		params.addBodyParameter("GPSStartTime",startTime);
		params.addBodyParameter("GPSEndTime",endTime);
		
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				Toast.makeText(context, "删除成功，网络连接不成功！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LoadDialog.close();
				deleteResult(arg0.result, context);

			}
		});
	}
	
	//删除gps数据处理方法
	protected static void deleteResult(String result, Context context) {
		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}
		
	}

	protected static void daochu(final Context context) {

		String Url = MyAppliaction.AppUrl + "getAllGPS.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("CGraphicsCreatNum",
				UserSingle.getSingle().userModel.getUserID());
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				Toast.makeText(context, "导出失败，网络连接不成功！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LoadDialog.close();
				result(arg0.result, context);

			}
		});

	}

	protected static void result(String result, Context context) {

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				String data = JsonUtils.getKey(result, "data");
				List<GPSRrecordModel> list = MapJsonUtils.getGPSModel(data);
				Boolean success=SaveTextUtils.daochuGPS(list);
				if(success)
				{
					Toast.makeText(context, "导出成功！路径:/sdcard/CkyPoject/TFXQJCZ/GPS导出-"+UserSingle.getSingle().userModel.getUserName()+".txt", Toast.LENGTH_LONG).show();
				}

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, "导出失败："+msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

	}


}
