package com.cky.ghyzt;

import java.io.File;
import com.jztx.utils.GPSLocation;
import com.jztx.utils.NetUtils;
import com.jztx.utils.SDCardUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.map.utils.VersionCommon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.application.MyAppliaction;
import com.cky.model.VersionModel;
import com.cky.ghyzt.R;

public class LoadActivity extends Activity {

	TextView tv_loadmsg;
	private LoadActivity context;
	private String pageName;
	private ProgressDialog m_progressDlg;
	private RelativeLayout rl;
	private String url;
	private String newAppName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		this.init();
		this.viewInit();
		this.dataInit();
		this.startClick();

	}

	// 开始执行事件
	private void startClick() {

		// 检测网络
		if (!NetUtils.isConnected(context)) {
			this.showErr("网络异常", "请检查网络连接后再试！");
			return;
		}

		// 检查内存卡
		if (!SDCardUtils.isSDCardEnable()) {
			this.showErr("没有内存卡", "检测到手机没有内存卡");
			return;
		}

		// 检查gps
		if (!GPSLocation.isOPen(context)) {
			this.showErr("GPS未打开", "请打开GPS后再试");
			return;
		}

		// 检测版本
		this.isNewVersion(url, pageName, MyAppliaction.OrgNum);

	}

	// 初始化必需数据
	private void init() {
		context = this;
		// 版本检测Url
		url = MyAppliaction.AppUrl + "getVersion.ashx";
		pageName = "com.cky.ghyzt";
		newAppName = "CkyGHYZT.apk";

	}

	// 数据初始化
	private void dataInit() {

		// 手机设置检测线程
		tv_loadmsg.setText("检查手机设置中……");
		// 下载进度条
		m_progressDlg = new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		m_progressDlg.setIndeterminate(false);
		m_progressDlg.setTitle("下载进度");
		m_progressDlg.setCanceledOnTouchOutside(false);
		m_progressDlg.setCancelable(false);

		// 判断横竖屏
		this.Confinuration();

	}

	// 首次进入横竖屏切换
	private void Confinuration() {

		int state = getResources().getConfiguration().orientation;
		if (state == 2) {
			rl.setBackground(getResources().getDrawable(R.mipmap.logocky));

		} else {

			rl.setBackground(getResources().getDrawable(R.mipmap.logocky_shu));

		}
	}

	// 控件初始化
	private void viewInit() {
		tv_loadmsg = (TextView) findViewById(R.id.load_txt);
		rl = (RelativeLayout) findViewById(R.id.load_beijing);
	}

	// 没有网络
	protected void showErr(String title, String Message) {

		Builder builder = new Builder(LoadActivity.this);
		builder.setTitle(title);
		builder.setMessage(Message);
		builder.setNegativeButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				finish();
			}
		});
		builder.show();
	}

	// 初始化登陆界面
	private void startLoginActivity() {
		Log.i("进入主界面", "主界面");
		Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	// 检测是不是最新版
	private void isNewVersion(String url, String pageName, String orgNum) {

		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("Appstr", orgNum);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				versionResult(arg0.result);

			}
		});
	}

	// 对查询到的版本结构进行处理
	protected void versionResult(String result) {

		System.out.println(result);
		try {
			String code = JsonUtils.getKey(result, "code");
			System.out.println(code);
			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！" + code, Toast.LENGTH_LONG)
						.show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				String data = JsonUtils.getKey(result, "data");

				VersionModel versionModel = MapJsonUtils.getVersion(data);

				System.out.println(versionModel.getVersionCode());

				// 判断是否是大于或者等于服务器版本
				double dangqian=Double.parseDouble(VersionCommon.getVerName(context, pageName));
				double fuquqi=Double.parseDouble(versionModel.getVersionCode());
				
				if (fuquqi>dangqian) {
					this.versionDiff(versionModel);
				} else {
					startLoginActivity();
				}

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据解析失败！" + e, Toast.LENGTH_LONG).show();
			System.out.println(e);
		}

	}

	// 检测到不是最新版的app，弹出更新提示
	private void versionDiff(final VersionModel model) {

		// 组合更新内容
		String content = model.getVersionContent() + "; ;";
		String[] neirong = content.split(";");

		View view = View.inflate(context, R.layout.dialog_upadatelist, null);
		ListView list = (ListView) view.findViewById(R.id.ls_update);
		list.setAdapter(new MyListAdapter(neirong));

		System.out.println("执行中");

		Builder builder = new Builder(context);
		builder.setTitle("发现新版本 " + model.getVersionCode()).setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 下载进度提示
				m_progressDlg.show();

				downFile(model);

			}
		});
		builder.setNegativeButton("暂不更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startLoginActivity();

			}
		});
		builder.create().show();

		System.out.println("执行");

	}

	// 下载新版本
	protected void downFile(VersionModel model) {

		final String fileUrl = Environment.getExternalStorageDirectory() + "/"
				+ newAppName;
		String newurl = MyAppliaction.AppUrl + "getAPK.ashx";

		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("orgNum", MyAppliaction.OrgNum);
		params.addBodyParameter("code", model.getVersionCode());
		http.download(HttpMethod.GET, newurl, fileUrl, params,
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						super.onLoading(total, current, isUploading);
						m_progressDlg.setMax((int) total);
						m_progressDlg.setProgress((int) current);

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						update(fileUrl);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						System.out.println(arg0.toString() + "  " + arg1);

						// 下载失败
						Toast.makeText(context, "下载失败", Toast.LENGTH_LONG)
								.show();

						startLoginActivity();

					}
				});

	}

	// 安装
	private void update(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(url)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	// 更新内容列表
	class MyListAdapter extends BaseAdapter {

		private String[] neirong;

		public MyListAdapter(String[] neirong) {
			this.neirong = neirong;
		}

		@Override
		public int getCount() {

			return neirong.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(context, R.layout.dialog_listtv, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_update_list);
			tv.setText(neirong[position]);

			return view;
		}

	}

	// 横竖屏切换事件
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rl.setBackground(getResources().getDrawable(R.mipmap.logocky));

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

			rl.setBackground(getResources().getDrawable(R.mipmap.logocky_shu));

		}

	}

}
