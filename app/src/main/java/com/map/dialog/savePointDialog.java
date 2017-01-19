package com.map.dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.base.ViewState;
import com.cky.ghyzt.R;
import com.cky.ghyzt.AddPhotoActivity;
import com.cky.ghyzt.MainActivity;
import com.cky.model.MapSavePoint;
import com.cky.model.PointInfo;
import com.cky.model.UserModel;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.jztx.utils.FileUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import com.map.utils.BitmapUtils;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.map.utils.PhotoName;
import com.map.utils.PointToString;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 需要传递几个参数： 1、保存的图形的性质（点线面） 2、图形的list信息，全部转化了为了点的属性 3、当前的绘制点的id，用于不保存时的取消该点信息
 * 4、context 上下文 5、activity 性质图片添加的时候会会用
 * 
 */

public class savePointDialog implements OnClickListener {

	private static final int style = R.style.dialog;
	public static ArrayList<String> listPhoto;
	private String xingzhi;
	private ArrayList<Point> pointList;
	private int layId;
	private Activity activity;
	private Context context;
	private GraphicsLayer ghLayer;


	private Dialog d;

	private EditText edit_name;

	private EditText edit_beizhu;
	private CheckBox cb_gongxiang;
	private PointInfo info;
	private String diyu;
	private EditText ed_quyu;
	private EditText ed_anjianbianhao;

	public static TextView dialog_filename;

	public savePointDialog(String xingzhi, ArrayList<Point> pointList,
			int layId, Context context, Activity activity,String diyu) {
		this.xingzhi = xingzhi;
		this.pointList = pointList;
		this.layId = layId;
		this.context = context;
		this.activity = activity;
		this.ghLayer = MainActivity.ghlayer;
		listPhoto = null;
		this.diyu=diyu;
	}

	/**
	 * 保存当前的信息
	 * 
	 * @param xingzhi
	 * @param pointList
	 * @param layId
	 * @param context
	 */
	public void show() {

		View view = View.inflate(context, R.layout.dialog_layer, null);
		edit_name = (EditText) view.findViewById(R.id.edit_name);
		edit_beizhu = (EditText) view.findViewById(R.id.edit_beizhu);
		ed_anjianbianhao=(EditText) view.findViewById(R.id.edit_anjianbianhao);
		ed_quyu=(EditText)view.findViewById(R.id.edit_quyu);
		dialog_filename = (TextView) view.findViewById(R.id.dialog_filename);

		Button btb_camera = (Button) view.findViewById(R.id.btb_camera);
		btb_camera.setOnClickListener(this);

		Button btn_close = (Button) view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(this);

		Button btb_save = (Button) view.findViewById(R.id.btb_save);
		btb_save.setOnClickListener(this);

		cb_gongxiang = (CheckBox) view.findViewById(R.id.dia_cb_gongxiang);

		ed_quyu.setText(diyu);
		
		// 显示
		this.diaShow(view);

	}

	/**
	 * 显示dialog
	 * 
	 * @param view
	 */
	private void diaShow(View view) {

		d = new Dialog(context, style);
		d.setContentView(view);
		d.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				ghLayer.removeGraphic(layId);

			}
		});
		d.setCanceledOnTouchOutside(false);
		d.show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btb_camera:
			Intent intent = new Intent(context, AddPhotoActivity.class);
			activity.startActivityForResult(intent, 110);
			break;

		case R.id.btn_close:
			ghLayer.removeGraphic(layId);
			d.dismiss();
			ViewState.removeAllState();
			break;

		case R.id.btb_save:
			this.cheakParm();

			break;

		default:
			break;
		}

	}

	private void cheakParm() {

		String title = edit_name.getText().toString();
		String content = edit_beizhu.getText().toString();
		String id = UUID.randomUUID().toString();// 用来生成数据库的主键id非常不错
		String type = xingzhi;
		String anjianbianhao=ed_anjianbianhao.getText().toString();
		String quyu=ed_quyu.getText().toString();

		// 标题为空
		if (title.equals("")) {
			Toast.makeText(context, "标题不能为空！", Toast.LENGTH_LONG).show();
			return;
		}

		// 登陆id
		String person = UserSingle.getSingle().userModel.getUserID();

		// 将点转化成坐标
		String geometry = PointToString.pointToString(pointList);

		// 是否共享
		String iscommon = cb_gongxiang.isChecked() ? "Y" : "F";

		// 执行提交参数
		this.saveParm(person, title, content, type, id, geometry, iscommon,anjianbianhao,quyu);

	}

	private void saveParm(String person, String title, String beizhu,
			String type, final String id, String geometry, String iscommon,String anjianbianhao,String quyu) {
		// 写在这里
		String url = MyAppliaction.AppUrl + "addGraphic.ashx";

		// 获取当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 构建参数
		RequestParams parms = new RequestParams();
		parms.addBodyParameter("CGraphicsId", id);	
		parms.addBodyParameter("CGraphicsName", title);
		parms.addBodyParameter("CGraphicsType", type);
		parms.addBodyParameter("CGraphicsGeometry", geometry);
		parms.addBodyParameter("CGraphicsContent", beizhu);
		parms.addBodyParameter("CGraphicsCreatNum",UserSingle.getSingle().userModel.getUserNum());	
		parms.addBodyParameter("CGraphicsCreatTime", df.format(new Date()));
		parms.addBodyParameter("CGraphicsOrg", MyAppliaction.OrgNum);
		parms.addBodyParameter("CGraphicsQuYu", quyu);
		parms.addBodyParameter("CGraphicsIsShare", iscommon);

		if (listPhoto != null && listPhoto.size() > 0) {
			for (String path : listPhoto) {
				Bitmap bit = BitmapUtils.getSmallBitmap(path);
				FileUtils.saveBit("/sdcard/CkyPoject/Min/",
						 PhotoName.getInstance().getPhotoName(), bit);
				File file = new File("/sdcard/CkyPoject/Min/",
						 PhotoName.getInstance().photoName);
				parms.addBodyParameter( PhotoName.getInstance().getPhotoName(), file);
			}
		}
		// 为临时存储区保存一份
		info = new PointInfo();
		info.setBeizhu(beizhu);
		info.setName(title);
		info.setId(id);
		info.setQuyu(quyu);

		LoadDialog.show(context, false, "提交中……");
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, parms, new RequestCallBack<String>() {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {

				LoadDialog.settext("提交中……" + current + "/" + total);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				System.out.println(responseInfo.result);
				graphicsResult(responseInfo.result);

			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(context, "连接服务端失败，请重试！", Toast.LENGTH_LONG)
						.show();

				LoadDialog.close();
			}
		});

	}

	// 对结果进行处理
	protected void graphicsResult(String result) {

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {
				
				
				//需要在临时保存区保存一份
	        	Map<Integer,PointInfo> mapPoint=MapSavePoint.mapPoint;	
	        	info.setId(JsonUtils.getKey(result, "msg"));
	        	info.setListPath(listPhoto);
	        	mapPoint.put(layId, info);
				
				LoadDialog.close();
				Toast.makeText(context, "添加成功！", Toast.LENGTH_LONG).show();
				d.dismiss();
				
				//显示上报成功
				ViewState.setViewOnclick("上报");
				ViewState.setText("上报", "上报成功，点击查看");

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

	}


	public static void setListPhoto(ArrayList<String> listPhoto) {
		if (listPhoto != null && listPhoto.size() != 0) {
			savePointDialog.listPhoto = listPhoto;
			dialog_filename.setText("添加了" + listPhoto.size() + "张图片");
		} else {
			savePointDialog.listPhoto = null;
			dialog_filename.setText("未添加任何图片");
		}
	}

}
