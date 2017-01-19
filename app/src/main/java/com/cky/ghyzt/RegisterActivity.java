package com.cky.ghyzt;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.model.UserModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.LoadDialog;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {

	private Context context;
	private EditText et_yonghuming;
	private EditText et_mima;
	private EditText et_chongfumima;
	private EditText et_shebeima;
	private String imei;
	private String yonghuming;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// 基本数据初始化
		this.baseInit();

		// 控件初始化
		this.viewInit();

		// 数据初始化
		this.dataInit();

	}

	// 数据初始化
	private void dataInit() {
		imei = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();

		et_shebeima.setText("设备码：" + imei);
	}

	// 控件初始化
	private void viewInit() {
		// 用户名
		et_yonghuming = (EditText) findViewById(R.id.et_yonghuming);
		// 密码
		et_mima = (EditText) findViewById(R.id.et_mima);
		// 重复密码
		et_chongfumima = (EditText) findViewById(R.id.et_chongfumima);
		// 设备码
		et_shebeima = (EditText) findViewById(R.id.et_shebeima);

		// 注册按钮
		Button btn_zhuce = (Button) findViewById(R.id.btn_zhuce);
		btn_zhuce.setOnClickListener(this);
		// 返回按钮
		Button btn_fanhui = (Button) findViewById(R.id.btn_fanhui);
		btn_fanhui.setOnClickListener(this);
	}

	// 基本数据初始化
	private void baseInit() {
		context = this;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_zhuce:

			this.zhuce();

			break;
		case R.id.btn_fanhui:

			this.fanhui();

			break;

		default:
			break;
		}

	}
	//返回

	private void fanhui() {
		this.finish();
		
	}

	// 注册事件
	private void zhuce() {

		// 判断值的合理性
		yonghuming = et_yonghuming.getText().toString().trim();
		String mima = et_mima.getText().toString().trim();
		String chongfumima = et_mima.getText().toString().trim();

		// 判断合理性
		if (yonghuming.equals("") || mima.equals("") || chongfumima.equals("")) {
			Toast.makeText(context, "请将信息填写完整！", Toast.LENGTH_LONG).show();
			return;
		}

		// 判断密码是否相同
		if (!mima.equals(chongfumima)) {
			Toast.makeText(context, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
			return;
		}

		String url = MyAppliaction.AppUrl + "Register.ashx";

		// 执行注册操作
		this.zhuceRequest(url, yonghuming, mima);
	}

	// 注册操作
	private void zhuceRequest(String url, String yonghuming, String mima) {
		LoadDialog.show(context, false, "注册中……");

		RequestParams params = new RequestParams();
		params.addBodyParameter("UserNum", yonghuming);
		params.addBodyParameter("Password", mima);
		params.addBodyParameter("Imei", imei);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				LoadDialog.close();

				Toast.makeText(context, "网络访问失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				zhuceResult(arg0.result);

			}
		});
	}

	// 网络请求结果
	protected void zhuceResult(String result) {
		LoadDialog.close();

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				Intent intent = new Intent();
				intent.putExtra("yonghuming", yonghuming);
				setResult(21, intent);
				finish();

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

	}

}
