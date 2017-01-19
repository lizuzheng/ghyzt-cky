package com.map.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
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
import com.cky.ghyzt.PhotoActivity;
import com.cky.model.ShapeModel;
import com.cky.model.UserModel;
import com.esri.android.map.MapView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.utils.ImageOnTouch;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.mapview.click.DrawAllPoint;

public class ShowShapeDialog {

	private static final int style = R.style.dialog;
	private static Bitmap bitmap;
	protected String id;
	private MapView mapview;
	private Context context;
	private Dialog dia;
	
	public ShowShapeDialog(Context context, MapView view)
	{
		this.context=context;
		this.mapview=view;
	}
	

	public  void show(final Context context,  ShapeModel info,
			final Bitmap bitmap,final ArrayList<String> minImageList,
			final ArrayList<String> MaxImageList) {
		
		id=info.getCGraphicsId();
		
		String name = info.getCGraphicsName();
		String beizhu = info.getCGraphicsContent();

		final View view1 = View.inflate(context, R.layout.pointshuxing_layer,
				null);
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
		
		
		final ImageView image = (ImageView) view1.findViewById(R.id.shuxing_image);
		Button btn_morephoto=(Button) view1.findViewById(R.id.btn_morephoto);
		btn_morephoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent photo=new Intent(context,PhotoActivity.class);
				photo.putExtra("title", "标记");
				photo.putStringArrayListExtra("urlMaxList",MaxImageList);						
				photo.putStringArrayListExtra("urlMinList", minImageList);
				
				context.startActivity(photo);
				
			}
		});
		
		Button delete=(Button) view1.findViewById(R.id.delete_point);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoadDialog.show(context, false, "删除中……");
				deleteGraphics();
				
			}
		});
		
		image.setImageBitmap(bitmap);

		dia = new Dialog(context, style);
		dia.setCanceledOnTouchOutside(true);
		dia.setCancelable(true);
		dia.setContentView(view1);
		dia.show();

		final View view2 = View
				.inflate(context, R.layout.imageview_layer, null);
		
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

	}

	//执行删除
	protected void deleteGraphics() {
		String Url=MyAppliaction.AppUrl+"delGraphic.ashx";
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

			
				dia.dismiss();
				mapview.setOnTouchListener(new DrawAllPoint(context,mapview));
				

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}
	}


}
