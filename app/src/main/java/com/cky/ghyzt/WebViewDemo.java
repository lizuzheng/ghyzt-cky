package com.cky.ghyzt;

import com.cky.ghyzt.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewDemo extends Activity {
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view_demo);

		Bundle extras = getIntent().getExtras();
		String URL = extras.getString("URL");
		System.out.print(URL);
		webview = new WebView(this);
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl(URL);
		// 设置Web视图
		setContentView(webview);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		webview.removeAllViews();
		webview.destroy();
		this.finish();
		return true;
	}

}
