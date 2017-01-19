package com.map.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.application.MyAppliaction;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.cky.ghyzt.PhotoActivity;
import com.cky.model.PointInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.utils.BitmapUtils;

import com.map.utils.ImageOnTouch;
import com.map.utils.JsonUtils;
import com.mapview.click.DrawAllPoint;

public class ShowPototDialog {

	private static final int style = R.style.dialog;
	private static Bitmap bitmap;
	protected static Dialog d;
	private String id;
	private String pathName;
	private Context context;
	private int layId;

	/**
	 * 将元素的属性传入
	 * 
	 * @param name
	 * @param beizhu
	 * @param pathName
	 */
	public void showDialog(final Activity activity, final Context context,
			final PointInfo info, final int layid) {

		id = info.getId();

		this.context = context;
		this.layId = layid;
		String name = info.getName();
		String beizhu = info.getBeizhu();

		if (info.getListPath() != null && info.getListPath().size() > 0) {
			pathName = info.getListPath().get(0);
		}

		LoadDialog.show(context, false, "读取中……");

		LayoutInflater inflater1 = activity.getLayoutInflater();
		final View view1 = inflater1.inflate(R.layout.pointshuxing_layer, null);
		EditText shuxing_name = (EditText) view1
				.findViewById(R.id.shuxing_name);
		shuxing_name.setText(name);
		EditText shuxing_beizhu = (EditText) view1
				.findViewById(R.id.shuxing_beizhu);
		shuxing_beizhu.setText(beizhu);
		
		EditText anjianbianhao = (EditText) view1
				.findViewById(R.id.edit_anjianbianhao);
		anjianbianhao.setText(info.getAnjianbianhao());
		
		EditText quyu = (EditText) view1
				.findViewById(R.id.edit_quyu);
		quyu.setText(info.getQuyu());
		
		
		final ImageView image = (ImageView) view1
				.findViewById(R.id.shuxing_image);

		Button btn_morephoto = (Button) view1.findViewById(R.id.btn_morephoto);
		btn_morephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent photo = new Intent(context, PhotoActivity.class);
				photo.putExtra("title", "标记");

				ArrayList<String> listPath = new ArrayList<String>();
				if (info.getListPath() != null && info.getListPath().size() > 0) {
					String path = "file://";
					for (String s : info.getListPath()) {
						listPath.add(path + s);
					}
				}

				photo.putStringArrayListExtra("urlMaxList", info.getListPath());
				photo.putStringArrayListExtra("urlMinList", listPath);
				context.startActivity(photo);

			}
		});

		Button delete = (Button) view1.findViewById(R.id.delete_point);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoadDialog.show(context, false, "删除中……");

				deleteGraphics();
			}
		});

		LayoutInflater inflater2 = activity.getLayoutInflater();
		final View view2 = inflater2.inflate(R.layout.imageview_layer, null);
		final ImageView imageview = (ImageView) view2
				.findViewById(R.id.imageview);
		imageview.setOnTouchListener(new ImageOnTouch(imageview));
		final Dialog imagedialog = new Dialog(context, style);
		imagedialog.setCanceledOnTouchOutside(true);
		imagedialog.setContentView(view2);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					imageview.setImageBitmap(bitmap);
				} catch (Exception e) {

					e.printStackTrace();
				}
				imagedialog.show();

			}
		});

		final Handler h1 = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				LoadDialog.close();

				d = new Dialog(context, style);
				d.setCanceledOnTouchOutside(true);
				d.setContentView(view1);
				d.show();

			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					if (pathName != null && !pathName.equals("")) {

						Bitmap bit = BitmapUtils.getSmallBitmap(pathName);
						if (bit != null) {
							image.setImageBitmap(bit);
						}
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

				Message msg = new Message();
				Bundle bundel = new Bundle();
				msg.setData(bundel);
				h1.sendMessage(msg);
			}
		}).start();

	}

	final Handler hd = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			LoadDialog.close();
			d.dismiss();
			super.handleMessage(msg);
		}

	};

	// 执行删除
	protected void deleteGraphics() {
		String Url = MyAppliaction.AppUrl + "delGraphic.ashx";
		RequestParams params = new RequestParams();

		HttpUtils http = new HttpUtils();
		params.addBodyParameter("CGraphicsId", id);
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(context, "删除失败", Toast.LENGTH_LONG).show();

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LoadDialog.close();
				deleteResult(arg0.result);

			}

		});

	}

	protected void deleteResult(String result) {
		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {
				MainActivity.ghlayer.removeGraphic(layId);
				d.dismiss();
			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}
	}

}
