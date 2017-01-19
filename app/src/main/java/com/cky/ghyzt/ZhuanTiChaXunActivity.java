package com.cky.ghyzt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cky.Adapter.ChaXunCaiJiAdpter;
import com.cky.Adapter.JianSheBuTuBanAdpter;
import com.cky.Adapter.TuBanChaXunAdpter;
import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.common.SerializableChaXunZhuanTi;
import com.cky.common.MyMapQuery.ReturnResult;
import com.cky.ghyzt.R;
import com.cky.model.ChaXunCaiJiModel;
import com.cky.model.ShapeModel;
import com.cky.model.TuBanModel;
import com.cky.pointQuery.QueryJianSheBu;
import com.cky.pointQuery.QueryTuBan;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.Dialog_SelectQuYu;
import com.map.dialog.DoubleDatePickerDialog;
import com.map.dialog.LoadDialog;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ZhuanTiChaXunActivity extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private Button btn_caijishujuchaxun;
	private Button btn_tubanchaxun;
	private LinearLayout ll_chacun_caiji;
	private LinearLayout ll_chacun_tuban;
	private ListView lv_leibiao_caijis;
	private EditText edit_startTime_chacun;
	private EditText edit_endTime_chaxun;
	private EditText edit_tiaojian_caiji;
	private String startTime;
	private String endTTime;
	private LinearLayout ll_chacun_jieguo;
	private EditText edit_sousuotiaojian_tuban;
	private Button btn_quxian_tuban;
	private ListView lv_jieguo_tuban;
	private LinearLayout include_tuban_title;
	private List<ShapeModel> listModel_caiji;
	private String xuanxiangka = "采集";

	// 保存当前状态
	private ChaXunCaiJiModel chaxuncaijiModel;
	private List<TuBanModel> listModel_tuban;
	private Button btn_jianshebutubanchaxun;
	private LinearLayout include_jianshebu;
	private LinearLayout include_jianshebu_title;
	private LinearLayout ll_include_jianshebu;
	private LinearLayout ll_include_jianshebu_title;
	private EditText edit_sousuotiaojian_jianshebu;
	private ListView lv_jieguo_jianshebu;
	private ListView lv_caiji_jieguo;
	private LinearLayout include_caijidiantitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuan_ti_cha_xun);

		this.baseInit();
		this.viewInit();
		this.dataInit();

		System.out.println("创建");

	}

	// 数据初始化
	private void dataInit() {

		// 获取原本有的值

		SerializableChaXunZhuanTi sMap = (SerializableChaXunZhuanTi) getIntent()
				.getExtras().getSerializable("value");
		if (sMap != null) {

			ChaXunCaiJiModel model = sMap.getModel();

			// 赋值
			if (model.getIsSave()) {
				// 开始时间
				startTime = model.getStartTime();
				edit_startTime_chacun.setText(model.getStartTime());

				// 结束时间
				endTTime = model.getEndTime();
				edit_endTime_chaxun.setText(model.getEndTime());

				// 搜索条件
				edit_tiaojian_caiji.setText(model.getCaijitiaojian());

				// 采集查询列表
				listModel_caiji = model.getListModel_caiji_all();
				if (listModel_caiji != null) {
					this.bindListView(listModel_caiji);
				}

				// 区市县
				btn_quxian_tuban.setText(model.getQuxian());

				// 图斑搜索字段
				edit_sousuotiaojian_tuban.setText(model
						.getSousuotiaojiantuban());

				// 图斑列表
				if (model.getListModel() != null) {
					listModel_tuban = model.getListModel();
					this.bindListViewTuBan(listModel_tuban);
				}

				// 判断选项卡
				if (model.getXuanxiangka().equals("图斑")) {
					tubanchacunqiehuan();
				}

				return;
			}
		}

		// 绑定时间
		this.bindTime();
	}

	// 绑定时间
	private void bindTime() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		endTTime = format.format(date);
		edit_endTime_chaxun.setText(endTTime);
		startTime = edit_startTime_chacun.getText().toString();
	}

	// 控件初始化
	private void viewInit() {

		// 采集数据查询
		btn_caijishujuchaxun = (Button) findViewById(R.id.btn_caijishujuchaxun);
		btn_caijishujuchaxun.setOnClickListener(this);

		// 图斑查询
		btn_tubanchaxun = (Button) findViewById(R.id.btn_tubanchaxun);
		btn_tubanchaxun.setOnClickListener(this);

		// 建设部图斑
		btn_jianshebutubanchaxun = (Button) findViewById(R.id.btn_jianshebutubanchaxun);
		btn_jianshebutubanchaxun.setOnClickListener(this);

		// 采集数据框架
		ll_chacun_caiji = (LinearLayout) findViewById(R.id.include_chacun_caiji);

		// 开始时间
		edit_startTime_chacun = (EditText) findViewById(R.id.edit_startTime_chacun);
		edit_startTime_chacun.setOnClickListener(this);

		// 结束时间
		edit_endTime_chaxun = (EditText) findViewById(R.id.edit_endTime_chaxun);
		edit_endTime_chaxun.setOnClickListener(this);

		// 搜索条件
		edit_tiaojian_caiji = (EditText) findViewById(R.id.edit_tiaojian_caiji);

		// 搜索按钮
		Button btn_sousuo_caiji = (Button) findViewById(R.id.btn_sousuo_caiji);
		btn_sousuo_caiji.setOnClickListener(this);

		// 地图上显示
		Button btn_xianshi_caiji = (Button) findViewById(R.id.btn_xianshi_caiji);
		btn_xianshi_caiji.setOnClickListener(this);

		// 结果
		ll_chacun_jieguo = (LinearLayout) findViewById(R.id.include_jieguo);

		// 列表
		lv_leibiao_caijis = (ListView) findViewById(R.id.lv_leibiao_caijis);

		// 图斑框架
		ll_chacun_tuban = (LinearLayout) findViewById(R.id.include_chacun_tuban);

		// 区县选择按钮
		btn_quxian_tuban = (Button) findViewById(R.id.btn_quxian_tuban);
		btn_quxian_tuban.setOnClickListener(this);

		// 搜索条件图斑
		edit_sousuotiaojian_tuban = (EditText) findViewById(R.id.edit_sousuotiaojian_tuban);

		// 搜索图斑
		Button btn_sousuo_tuban = (Button) findViewById(R.id.btn_sousuo_tuban);
		btn_sousuo_tuban.setOnClickListener(this);

		include_tuban_title = (LinearLayout) findViewById(R.id.include_tuban_title);

		// 图斑结果列表
		lv_jieguo_tuban = (ListView) findViewById(R.id.lv_jieguo_tuban);

		// 建设部图斑
		ll_include_jianshebu = (LinearLayout) findViewById(R.id.include_chacun_jianshebu);
		// 建设部图斑表头
		ll_include_jianshebu_title = (LinearLayout) findViewById(R.id.include_jianshebu_title);
		// 搜索条件建设部
		edit_sousuotiaojian_jianshebu = (EditText) findViewById(R.id.edit_sousuotiaojian_jianshebu);

		// 搜索图斑
		Button btn_sousuo_jianshebu = (Button) findViewById(R.id.btn_sousuo_jianshebu);
		btn_sousuo_jianshebu.setOnClickListener(this);

		// 图斑结果列表
		lv_jieguo_jianshebu = (ListView) findViewById(R.id.lv_jieguo_jianshebu);
	}

	// 基本数据初始化
	private void baseInit() {
		this.context = this;
		this.activity = this;

		// 初始化标题栏
		Button fanhui = (Button) findViewById(R.id.title_bar_btn_left);
		fanhui.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_title);
		title.setText("专题查询");
		Button queding = (Button) findViewById(R.id.title_btn_right);

		queding.setText("确定");
		queding.setVisibility(View.GONE);
		queding.setOnClickListener(this);

		

	}

	// 按钮事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.title_bar_btn_left:
			this.finish();
			break;

		case R.id.btn_caijishujuchaxun:

			this.caijishujuchacunqiehuan();
			break;

		case R.id.btn_tubanchaxun:
			this.tubanchacunqiehuan();
			break;

		case R.id.edit_startTime_chacun:

		case R.id.edit_endTime_chaxun:
			this.selectTime();
			break;

		case R.id.btn_sousuo_caiji:
			this.sousuoclick();
			break;

		case R.id.btn_quxian_tuban:
			this.selectQuXian();
			break;

		case R.id.btn_sousuo_tuban:
			this.sousuoTuBan();
			break;

		case R.id.btn_xianshi_caiji:
			this.mapShow();
			break;

		// 建设部图斑
		case R.id.btn_jianshebutubanchaxun:
			this.jianshebutubanqiehuan();
			break;

		case R.id.btn_sousuo_jianshebu:
			this.sousuojianshetubananniu();
			break;

		default:
			break;
		}

	}

	// 按钮点击搜索建设图斑
	private void sousuojianshetubananniu() {

		// 条件判断
		String tiaojian = edit_sousuotiaojian_jianshebu.getText().toString();

		// 执行搜索
		this.sousuojianshebutubanonclick(tiaojian);

	}

	// 执行建设部图斑形状查询
	private void sousuojianshebutubanonclick(String tiaojian) {
		MyReturnResultJianSheBu result = new MyReturnResultJianSheBu();
		// 执行查询
		QueryJianSheBu queryJianSheBu = new QueryJianSheBu(context,
				MainActivity.mMapview);
		queryJianSheBu.startQuery(tiaojian, result);

	}

	// 创建一个接口处理回调
	class MyReturnResultJianSheBu implements ReturnResult {

		@Override
		public void onPostExecute(FeatureSet result) {

			if (result == null) {
				Toast.makeText(context, "未查询到数据！", Toast.LENGTH_LONG).show();
				return;
			}

			// 整理数据
			Jianshebuchaxunshuju(result.getGraphics());
		}

	}

	// 处理建设部图斑查询数据
	public void Jianshebuchaxunshuju(Graphic[] graphics) {

		ll_include_jianshebu_title.setVisibility(View.VISIBLE);
		lv_jieguo_jianshebu.setAdapter(new JianSheBuTuBanAdpter(context,
				graphics, null));

	}

	// 在地图上显示
	private void mapShow() {

		// 判断数据的可用性
		if (listModel_caiji == null || listModel_caiji.size() == 0) {
			Toast.makeText(context, "未查询到数据！", Toast.LENGTH_LONG).show();
			return;
		}

		// 保存当前的状态
		this.saveState(listModel_caiji);

		SerializableChaXunZhuanTi sMap = new SerializableChaXunZhuanTi();
		sMap.setModel(chaxuncaijiModel);

		// 返回到主地图
		LoadDialog.show(context, true, "绘制中……");
		Intent intent = new Intent();
		intent.putExtra("chaxunjieguo", sMap);
		setResult(202, intent);
		finish();

	}

	// 保存当前的状态
	private void saveState(List<ShapeModel> listModelresult) {

		// 新建一个
		chaxuncaijiModel = new ChaXunCaiJiModel();

		// 是否保存
		chaxuncaijiModel.setIsSave(true);

		// 哪个选项卡
		chaxuncaijiModel.setXuanxiangka(xuanxiangka);

		// 开始时间
		chaxuncaijiModel.setStartTime(startTime);

		// 结束时间
		chaxuncaijiModel.setEndTime(endTTime);

		// 搜索条件
		chaxuncaijiModel.setCaijitiaojian(edit_tiaojian_caiji.getText()
				.toString());

		// 采集列表
		chaxuncaijiModel.setListModel_caiji(listModelresult);

		// 所有采集的列表
		chaxuncaijiModel.setListModel_caiji_all(listModel_caiji);

		// 区县
		chaxuncaijiModel.setQuxian(btn_quxian_tuban.getText().toString());

		// 图斑搜索条件
		chaxuncaijiModel.setSousuotiaojiantuban(edit_sousuotiaojian_tuban
				.getText().toString());

		// 图斑列表
		chaxuncaijiModel.setListModel(listModel_tuban);

	}

	// 搜索图斑事件
	private void sousuoTuBan() {

		String quxian = btn_quxian_tuban.getText().toString();
		String tiaojian = edit_sousuotiaojian_tuban.getText().toString();

		// 执行搜索事件
		this.sousuoTuBanClick(quxian, tiaojian);

	}

	// 搜索图斑
	private void sousuoTuBanClick(String quxian, String tiaojian) {

		LoadDialog.show(context, true, "统计中……");

		String Url = MyAppliaction.AppUrl + "/getTuBanChaXun.ashx";
		RequestParams params = new RequestParams();

		params.addBodyParameter("quxian", quxian);
		params.addBodyParameter("neirong", tiaojian);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				resultTuBan(arg0.result);
			}

		});

	}

	// 数据库图斑结果的回调
	protected void resultTuBan(String result) {

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
				listModel_tuban = MapJsonUtils.getTubanChaXun(data);

				// 绑定数据
				this.bindListViewTuBan(listModel_tuban);

			} else {
				LoadDialog.close();
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				// 绑定数据
				this.bindListViewTuBan(null);
			}
		} catch (Exception e) {
			LoadDialog.close();
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

		LoadDialog.close();
	}

	// 绑定图斑处理结果
	private void bindListViewTuBan(final List<TuBanModel> listModel) {

		// 设置显示
		include_tuban_title.setVisibility(View.VISIBLE);

		// 绑定
		lv_jieguo_tuban.setAdapter(new TuBanChaXunAdpter(context, listModel));

		// 设置点击事件
		lv_jieguo_tuban.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TuBanXingZhuang(listModel.get(position).getoBJECTID_1());
			}
		});
	}

	// 查询形状
	private void TuBanXingZhuang(String id) {
		MyReturnResult result = new MyReturnResult();
		// 执行查询
		QueryTuBan tuban = new QueryTuBan(context, MainActivity.mMapview);
		tuban.startQuery(id, result);

	}

	// 创建一个接口处理回调
	class MyReturnResult implements ReturnResult {

		@Override
		public void onPostExecute(FeatureSet result) {
			System.out.println("结果回调");

			// 未查询到结果
			if (result == null) {
				Toast.makeText(context, "未查询到该点", Toast.LENGTH_LONG).show();
				return;
			}

			// 执行后续操作
			System.out.println(result.getGraphics());
			// 保存当前的状态
			saveState(listModel_caiji);

			// 设置结果值
			chaxuncaijiModel.setResult(result);

			// 保存值
			SerializableChaXunZhuanTi sMap = new SerializableChaXunZhuanTi();
			sMap.setModel(chaxuncaijiModel);

			// 返回到主地图
			LoadDialog.show(context, true, "绘制中……");
			Intent intent = new Intent();
			intent.putExtra("chaxunjieguo", sMap);
			setResult(202, intent);
			finish();

		}

	}

	// 区县选择
	private void selectQuXian() {

		Dialog_SelectQuYu.openDialog(context, btn_quxian_tuban);
	}

	// 搜索事件
	private void sousuoclick() {

		// 判断值的有效性，只需要判断时间
		if (startTime.equals("") || endTTime.equals("")) {
			Toast.makeText(context, "请正确输入时间！", Toast.LENGTH_LONG).show();
			return;
		}

		// 执行网络请求
		this.getAllShape();
	}

	// 获取保存的所有点
	private void getAllShape() {
		LoadDialog.show(context, true, "统计中……");

		String Url = MyAppliaction.AppUrl + "/getGraphicForQita.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId",
				UserSingle.getSingle().userModel.getUserID());
		params.addBodyParameter("startTime", startTime);
		params.addBodyParameter("endTime", endTTime);
		params.addBodyParameter("neirong", edit_tiaojian_caiji.getText()
				.toString().trim());

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

	// 执行结果处理
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
				listModel_caiji = MapJsonUtils.getAllShape(data);

				System.out.println("获取的点的个数：" + listModel_caiji.size());

				this.bindListView(listModel_caiji);

			} else {
				LoadDialog.close();
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

				this.bindListView(null);
			}
		} catch (Exception e) {
			LoadDialog.close();
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

		LoadDialog.close();

	}

	// 绑定列表_采集数据
	private void bindListView(final List<ShapeModel> listModel) {

		// 显示表头
		ll_chacun_jieguo.setVisibility(View.VISIBLE);
		// 绑定数据
		lv_leibiao_caijis.setAdapter(new ChaXunCaiJiAdpter(context, listModel));
		// 设置事件
		lv_leibiao_caijis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				List<ShapeModel> listModelresult = new ArrayList<ShapeModel>();
				listModelresult.add(listModel.get(position));

				// 保存当前的状态
				saveState(listModelresult);

				// 保存值
				SerializableChaXunZhuanTi sMap = new SerializableChaXunZhuanTi();
				sMap.setModel(chaxuncaijiModel);

				// 返回到主地图
				LoadDialog.show(context, true, "绘制中……");
				Intent intent = new Intent();
				intent.putExtra("chaxunjieguo", sMap);
				setResult(202, intent);
				finish();

			}
		});

	}

	// 选择查询的前后时间
	private void selectTime() {
		Calendar c = Calendar.getInstance();

		// 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
		new DoubleDatePickerDialog(context, 0,
				new DoubleDatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker startDatePicker,
							int startYear, int startMonthOfYear,
							int startDayOfMonth, DatePicker endDatePicker,
							int endYear, int endMonthOfYear, int endDayOfMonth) {

						// 执行删除操作
						startTime = startYear + "-" + (startMonthOfYear + 1)
								+ "-" + startDayOfMonth + " 00:00";
						endTTime = endYear + "-" + (endMonthOfYear + 1) + "-"
								+ endDayOfMonth + " 00:00";

						edit_startTime_chacun.setText(startTime);
						edit_endTime_chaxun.setText(endTTime);

					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DATE), true).show();

	}

	// 图斑查询切换
	private void tubanchacunqiehuan() {

		// 记录状态
		xuanxiangka = "图斑";

		ll_chacun_tuban.setVisibility(View.VISIBLE);
		ll_chacun_caiji.setVisibility(View.GONE);
		ll_include_jianshebu.setVisibility(View.GONE);

		btn_tubanchaxun.setBackgroundColor(Color.rgb(217, 217, 217));
		btn_caijishujuchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
		btn_jianshebutubanchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
	}

	// 采集数据按钮切换
	private void caijishujuchacunqiehuan() {

		// 记录状态
		xuanxiangka = "采集";

		ll_chacun_caiji.setVisibility(View.VISIBLE);
		ll_chacun_tuban.setVisibility(View.GONE);
		ll_include_jianshebu.setVisibility(View.GONE);

		btn_caijishujuchaxun.setBackgroundColor(Color.rgb(217, 217, 217));
		btn_tubanchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
		btn_jianshebutubanchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
	}

	// 建设部图斑切换
	private void jianshebutubanqiehuan() {
		// 记录状态
		xuanxiangka = "建设部";

		ll_chacun_caiji.setVisibility(View.GONE);
		ll_chacun_tuban.setVisibility(View.GONE);
		ll_include_jianshebu.setVisibility(View.VISIBLE);

		btn_caijishujuchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
		btn_tubanchaxun.setBackgroundColor(Color.rgb(245, 245, 245));
		btn_jianshebutubanchaxun.setBackgroundColor(Color.rgb(217, 217, 217));
	}

	@Override
	protected void onDestroy() {
		LoadDialog.close();
		super.onDestroy();
	}

}
