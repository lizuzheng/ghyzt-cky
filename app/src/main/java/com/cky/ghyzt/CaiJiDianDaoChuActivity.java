package com.cky.ghyzt;

import java.util.ArrayList;
import java.util.List;

import com.cky.Adapter.CaiJiDaoChuAdpter;
import com.cky.Adapter.CaiJiDaoChuAdpter.QuanXuanState;
import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.model.CaiJiDianDaoChuStateModel;
import com.cky.model.ShapeModel;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.LoadDialog;
import com.map.utils.CaiJiDianDaoChuUtils;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.map.utils.ZipUtils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CaiJiDianDaoChuActivity extends Activity implements
		OnClickListener {

	private Context context;
	private ListView lv_caiji_jieguo;
	private List<ShapeModel> listModel;
	private CheckBox cb_quanxuan;
	private CaiJiDianDaoChuStateModel caiJiDianDaoChuStateModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cai_ji_dian_dao_chu);

		// 基本数据初始化
		this.baseInit();
		// 控件初始化
		this.viewInit();
		// 数据初始化
		this.dataInit();
	}

	// 基本数据初始化
	private void baseInit() {

		context = this;

		//ShapefileFeatureTable  table=new ShapefileFeatureTable("");
	}

	// 控件初始化
	private void viewInit() {

		// 初始化标题栏
		Button fanhui = (Button) findViewById(R.id.title_bar_btn_left);
		fanhui.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_title);
		title.setText("采集点导出");
		Button queding = (Button) findViewById(R.id.title_btn_right);

		queding.setText("导出");
		queding.setOnClickListener(this);

		// 采集结果
		lv_caiji_jieguo = (ListView) findViewById(R.id.lv_caiji_jieguo);

		// 全选按钮
		cb_quanxuan = (CheckBox) findViewById(R.id.cb_xuanze);
		cb_quanxuan.setOnClickListener(this);

	}

	// 数据初始化
	private void dataInit() {

		// 判断是否有从前方传输过来的值

		CaiJiDianDaoChuStateModel sMap = (CaiJiDianDaoChuStateModel) getIntent()
				.getExtras().getSerializable("value");
		if (sMap != null) {

			this.bindListview(sMap.getListModelAll());

			// 判断全选,这样子效率很低
			boolean isState = true;
			for (ShapeModel model : listModel) {
				if (!model.isSelect()) {
					isState = false;
				}
			}
			cb_quanxuan.setChecked(isState);

		} else {

			LoadDialog.show(context, true, "查询中……");
			// 查询全部的值
			this.queryCaijidian("");
		}

	}

	// 根据条件查询采集点
	private void queryCaijidian(String value) {

		String Url = MyAppliaction.AppUrl + "getGraphicForValue.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("CGraphicsCreatNum",
				UserSingle.getSingle().userModel.getUserNum());
		params.addBodyParameter("CGraphicsOrg", MyAppliaction.OrgNum);
		params.addBodyParameter("Value", value);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				resultShape(arg0.result);
			}

		});
	}

	// 对查询到的所有的结果进行解析
	protected void resultShape(String result) {

		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
				return;
			}
			// 成功
			if (code.equals("Success")) {

				String data = JsonUtils.getKey(result, "data");
				// 获取到所有的保存图层
				List<ShapeModel> listModel = MapJsonUtils.getAllShape(data);
				// 绑定到列表
				this.bindListview(listModel);
			} else {
				LoadDialog.close();
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			LoadDialog.close();
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

	}

	// 绑定到列表
	private void bindListview(List<ShapeModel> listModel1) {

		this.listModel = listModel1;
		lv_caiji_jieguo.setAdapter(new CaiJiDaoChuAdpter(context, listModel,
				state));

		lv_caiji_jieguo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("点击事件");

				List<ShapeModel> listModelresult = new ArrayList<ShapeModel>();
				listModelresult.add(listModel.get(position));

				// 保存当前的状态
				saveState(listModelresult);

				// 传递给上一个页面
				// LoadDialog.show(context, true, "绘制中……");
				Intent intent = new Intent();
				intent.putExtra("caijidaochu", caiJiDianDaoChuStateModel);
				setResult(302, intent);
				finish();

			}
		});

		System.out.println("执行绑定");

		LoadDialog.close();

	}

	// 保存状态
	protected void saveState(List<ShapeModel> listModelresult) {
		caiJiDianDaoChuStateModel = new CaiJiDianDaoChuStateModel();
		caiJiDianDaoChuStateModel.setListModelAll(listModel);
		caiJiDianDaoChuStateModel.setListModelSelect(listModelresult);

	}

	// 执行当前全选状态
	QuanXuanState state = new QuanXuanState() {

		@Override
		public void zhixing(boolean state) {
			System.out.println("执行全选" + state);
			cb_quanxuan.setChecked(state);

		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cb_xuanze:
			this.selectChange();
			System.out.println("点击");
			break;

		case R.id.title_btn_right:
			this.daochu111();
			break;

		case R.id.title_bar_btn_left:
			this.finish();
			break;

		default:
			break;
		}

	}

	private  void daochu111()
	{
		try {

			ZipUtils.zipFolder("/sdcard/CkyPoject/CkyGHYZT/采集点导出/20170116113641","/sdcard/CkyPoject/CkyGHYZT/采集点导出/20170116113641.zip");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	// 导出
	private void daochu() {

		if(listModel==null||listModel.size()==0)
		{
			Toast.makeText(getApplicationContext(),"没有查询到数据",Toast.LENGTH_LONG).show();
			return;
		}


		List<ShapeModel> isListModel = new ArrayList<>();

		// 找出选中的采集点
		for (ShapeModel model : listModel) {
			if (model.isSelect()) {

				// 执行保存操作
				isListModel.add(model);

			}
		}

		if(isListModel==null||isListModel.size()==0)
		{
			Toast.makeText(getApplicationContext(),"请选择数据后再导出！",Toast.LENGTH_LONG).show();
			return;
		}

		//执行保存操作
		LoadDialog.show(context, true, "导出中……请等待!");
		CaiJiDianDaoChuUtils daochu=new CaiJiDianDaoChuUtils(context,isListModel);
		daochu.save();

	}

	// 全选事件改变
	private void selectChange() {

		LoadDialog.show(context, true, "加载中……");

		for (int i = 0; i < listModel.size(); i++) {

			listModel.get(i).setSelect(cb_quanxuan.isChecked());

		}

		bindListview(listModel);
	}

}
