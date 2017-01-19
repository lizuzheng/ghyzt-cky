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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cky.ghyzt.R;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener,
		Runnable {

	private SharedPreferences sp;
	private EditText tv_user;
	private EditText tv_pwd;
	private CheckBox savepwd;
	private Button btn_login;
	private Context context;
	private String imei;
	private RelativeLayout rl;
	private String loginUrl;
	private TextView tv_zhuce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		this.init();

		this.viewInit();

		this.dataInit();

	}

	private void init() {
		context = this;
		imei = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE))
				.getDeviceId();

		System.out.println(imei);
		loginUrl = MyAppliaction.AppUrl + "Login.ashx";

		sp = getSharedPreferences("GHYZT", MODE_PRIVATE);

		rl = (RelativeLayout) findViewById(R.id.log_beijing);

	}

	private void viewInit() {
		tv_user = (EditText) findViewById(R.id.tv_user);
		tv_pwd = (EditText) findViewById(R.id.tv_pwd);
		savepwd = (CheckBox) findViewById(R.id.savepwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_user.setText(sp.getString("user", ""));
		tv_pwd.setText(sp.getString("pwd", ""));
		if (!sp.getString("pwd", "").equals("")) {
			savepwd.setChecked(true);
		}

		tv_zhuce = (TextView) findViewById(R.id.tv_zhuce);
		tv_zhuce.setText(Html.fromHtml("<u>" + "注册账号" + "</u>"));
		tv_zhuce.setOnClickListener(this);

	}

	private void dataInit() {

		this.Confinuration();

		btn_login.setOnClickListener(this);

		Thread t1 = new Thread(this);
		t1.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			LoginClick();
			break;
		case R.id.tv_zhuce:
			zhuce();
			break;

		default:
			break;
		}

	}

	private void zhuce() {

		Intent query = new Intent(context, RegisterActivity.class);
		startActivityForResult(query, 20);

	}

	private void LoginClick() {

		LoadDialog.show(context, false, "登录中……");

		if (tv_user.getText().toString().equals("")
				|| tv_pwd.getText().toString().equals("")) {

			LoadDialog.close();

			Toast.makeText(LoginActivity.this, "请完整填写信息！", Toast.LENGTH_LONG)
					.show();
		} else {
			RequestParams params = new RequestParams();
			params.addBodyParameter("UserID", tv_user.getText().toString());
			params.addBodyParameter("Password", tv_pwd.getText().toString());
			params.addBodyParameter("Imei", imei);
			params.addBodyParameter("type", MyAppliaction.OrgNum);

			HttpUtils http = new HttpUtils();
			http.send(HttpMethod.POST, loginUrl, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {

							LoadDialog.close();

							Toast.makeText(context, "服务器连接失败！！",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							LoadDialog.close();
							result(arg0.result);

						}
					});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 20) {
			if (resultCode == 21) {

				Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
				String yonghuming = data.getStringExtra("yonghuming");
				tv_user.setText(yonghuming);
				tv_pwd.setText("");
			}

		}

	}

	private void result(String result) {

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据错误！", Toast.LENGTH_LONG).show();
				return;
			}

			if (code.equals("Success")) {

				String data = JsonUtils.getKey(result, "data");

				UserModel model = MapJsonUtils.getUser(data);
				UserSingle.getSingle().userModel = model;
				System.out.println(model.getUserName()+"  dsadasdsa");

				savePass();

				Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();

				this.startMapActicity();

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "解析数据出现异常", Toast.LENGTH_LONG).show();
		}

	}

	private void Confinuration() {

		int state = getResources().getConfiguration().orientation;
		System.out.println(state);
		if (state == 2) {
			rl.setBackground(getResources().getDrawable(R.mipmap.loginheng));

		} else {

			rl.setBackground(getResources().getDrawable(R.mipmap.loginshu));
		}
	}

	public void savePass() {

		String user = "";
		String pwd = "";
		user = tv_user.getText().toString();
		if (savepwd.isChecked()) {
			pwd = tv_pwd.getText().toString();
		}

		Editor edit = sp.edit();

		edit.putString("user", user);
		edit.putString("pwd", pwd);

		edit.commit();
	}

	protected void startMapActicity() {

		LoadDialog.close();

		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);

		finish();

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rl.setBackground(getResources().getDrawable(R.mipmap.loginheng));

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

			rl.setBackground(getResources().getDrawable(R.mipmap.loginshu));

		}

	}

	@Override
	public void run() {

	}

}
