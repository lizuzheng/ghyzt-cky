package com.cky.ghyzt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cky.Adapter.CaiJiShuJuAdpter;
import com.cky.Adapter.TuBanJieGuoAdpter;
import com.cky.Adapter.XiangZhengTuBanAdpter;
import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.ghyzt.R;
import com.cky.model.CaiJiShuJuTongJiModel;
import com.cky.model.TuBanJieGuoModel;
import com.cky.model.XiangZhengModel;
import com.cky.view.MyListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.Dialog_CaiJiTongJiFangShi;
import com.map.dialog.Dialog_CaiJiTongJiFangShi.resultCaiJiFangShi;
import com.map.dialog.Dialog_SelectQuYu;
import com.map.dialog.DoubleDatePickerDialog;
import com.map.dialog.LoadDialog;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;

import android.app.Activity;
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

public class ZhuanTiTongJiActivity extends Activity implements OnClickListener {

	private ZhuanTiTongJiActivity context;
	private ZhuanTiTongJiActivity activity;
	private Button btn_caishujutongji;
	private Button btn_tubantongji;
	private Button btn_tongji;
	private ListView lv_tongjijieguo;
	private List<CaiJiShuJuTongJiModel> CaiJiList;
	private LinearLayout ll_caijishuju;
	private EditText edit_startTime;
	private EditText edit_endTime;
	private String startTime;
	private String endTTime;
	private LinearLayout ll_include_time;
	private LinearLayout ll_include_qita;
	private TextView tv_qitatiaojian;
	private EditText edit_qita;
	private Button tv_tongjifangshi;
	private TextView tv_tongjifangshiwenzi;
	private LinearLayout ll_include_quyu;
	private EditText edit_caijitiaojian_quyu;
	// 采集状态标记 1、时间 2、采集人 3、区域
	private int caijiState = 1;
	private LinearLayout ll_tubantongji;
	private Button btn_tubantongji_tongji;
	private TextView tv_tongjifangshiwenzi_tuban;
	private ListView lv_tongjijieguo_tuban;
	private ArrayList<TuBanJieGuoModel> tuBanJieGuoModelList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuan_ti_tong_ji);

		this.baseInit();
		this.viewInit();
		this.dataInit();

	}

	// 数据初始化
	private void dataInit() {
		// 绑定时间
		this.bindTime();

	}

	// 绑定时间
	private void bindTime() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		endTTime = format.format(date);
		edit_endTime.setText(endTTime);
		startTime = edit_startTime.getText().toString();
	}

	// 控件初始化
	private void viewInit() {
		// 采集数据统计
		btn_caishujutongji = (Button) findViewById(R.id.btn_caijishujutognji);
		btn_caishujutongji.setOnClickListener(this);

		// 图斑统计
		btn_tubantongji = (Button) findViewById(R.id.btn_tubantongji);
		btn_tubantongji.setOnClickListener(this);

		// 采集数据统计按钮
		btn_tongji = (Button) findViewById(R.id.btn_tongji);
		btn_tongji.setOnClickListener(this);

		// 采集数据统计结果显示
		lv_tongjijieguo = (ListView) findViewById(R.id.lv_tongjijieguo);

		// 采集数据子控件
		ll_caijishuju = (LinearLayout) findViewById(R.id.include_caijishuju);
		ll_tubantongji = (LinearLayout) findViewById(R.id.include_tubantongji);

		// 采集数据开始时间
		edit_startTime = (EditText) findViewById(R.id.edit_startTime);
		edit_startTime.setOnClickListener(this);

		// 采集数据结束时间
		edit_endTime = (EditText) findViewById(R.id.edit_endTime);
		edit_endTime.setOnClickListener(this);

		// 采集数据条件模块
		ll_include_time = (LinearLayout) findViewById(R.id.ll_include_time);

		// 采集数据采集人模块
		ll_include_qita = (LinearLayout) findViewById(R.id.ll_include_qitatiaojian);
		edit_qita = (EditText) findViewById(R.id.edit_caijitiaojian_neirong);

		// 区域模块
		ll_include_quyu = (LinearLayout) findViewById(R.id.ll_include_quyu);
		edit_caijitiaojian_quyu = (EditText) findViewById(R.id.edit_caijitiaojian_quyu);
		edit_caijitiaojian_quyu.setOnClickListener(this);

		// 统计方式文字
		tv_tongjifangshiwenzi = (TextView) findViewById(R.id.tv_tongjifangshiwenzi);

		// 统计方式
		tv_tongjifangshi = (Button) findViewById(R.id.tv_tongjifangshi);
		tv_tongjifangshi.setOnClickListener(this);

		// 图斑统计按钮
		btn_tubantongji_tongji = (Button) findViewById(R.id.btn_tongji_tuban);
		btn_tubantongji_tongji.setOnClickListener(this);
		tv_tongjifangshiwenzi_tuban = (TextView) findViewById(R.id.tv_tongjifangshi_tuban);
		tv_tongjifangshiwenzi_tuban.setOnClickListener(this);
		lv_tongjijieguo_tuban = (ListView) findViewById(R.id.lv_tongjijieguo_tuban);
	}

	// 基本数据初始化
	private void baseInit() {

		this.context = this;
		this.activity = this;

		// 初始化标题栏
		Button fanhui = (Button) findViewById(R.id.title_bar_btn_left);
		fanhui.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_title);
		title.setText("专题统计");
		Button queding = (Button) findViewById(R.id.title_btn_right);

		queding.setText("确定");
		queding.setVisibility(View.GONE);
		queding.setOnClickListener(this);

	}

	// 按钮的点击事件
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_bar_btn_left:
			this.finish();

			break;

		case R.id.btn_caijishujutognji:
			this.caijishujutongji();

			break;
		case R.id.btn_tubantongji:
			this.tubantongji();
			break;

		case R.id.btn_tongji:
			this.tongji();
			break;

		case R.id.edit_startTime:
			this.selectTime(edit_startTime);
			break;

		case R.id.edit_endTime:
			this.selectTime(edit_endTime);
			break;

		case R.id.tv_tongjifangshi:
			// this.tongjifangshi();
			break;

		case R.id.edit_caijitiaojian_quyu:
			// 选择区域
			this.selectQuYu();
			break;

		case R.id.btn_tongji_tuban:
			// 统计图斑按钮
			this.tubantongjiclick();
			break;

		case R.id.tv_tongjifangshi_tuban:
			// 统计方式

			break;

		default:
			break;
		}

	}

	// 点击统计图斑
	private void tubantongjiclick() {
		LoadDialog.show(context, true, "统计中……");

		String Url = MyAppliaction.AppUrl + "/getTuBanCount.ashx";
		RequestParams params = new RequestParams();

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				tubantongjiejieguo(arg0.result);
			}

		});

	}

	// 图斑统计结果
	protected void tubantongjiejieguo(String result) {

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
				Map<String, String> allMap = MapJsonUtils.TongjiTuBan(data);
				// 整体数据
				this.bindDataTuBan(allMap);

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

	// 图斑统计结果的显示
	private void bindDataTuBan(Map<String, String> allMap) {
		tuBanJieGuoModelList = new ArrayList<TuBanJieGuoModel>();

		TuBanJieGuoModel model = new TuBanJieGuoModel();
		model.setQushixian("区市县");
		model.setShuju("变化图斑数量");
		model.setIsQuery(false);
		tuBanJieGuoModelList.add(model);

		for (String s : allMap.keySet()) {
			TuBanJieGuoModel model1 = new TuBanJieGuoModel();
			model1.setQushixian(s);
			model1.setShuju(allMap.get(s));
			model1.setIsQuery(false);
			tuBanJieGuoModelList.add(model1);

		}

		LoadDialog.close();

		// 绑定数据
		lv_tongjijieguo_tuban.setAdapter(new TuBanJieGuoAdpter(context,
				tuBanJieGuoModelList, null));
		lv_tongjijieguo_tuban.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position != 0) {

					MyListView listview = (MyListView) view
							.findViewById(R.id.lv_xiangzheng);

					// 判断是不是隐藏操作
					if (tuBanJieGuoModelList.get(position).getIsShow()) {
						listview.setVisibility(View.GONE);
						tuBanJieGuoModelList.get(position).setIsShow(false);
						return;
					}

					// 判断有没有读取过数据库
					if (tuBanJieGuoModelList.get(position).getIsQuery()) {

						// 已经读取过了,显示就行了
						listview.setVisibility(View.VISIBLE);

					} else {
						// 还没有读取
						queryXiangZheng(tuBanJieGuoModelList.get(position),
								listview);
					}
					tuBanJieGuoModelList.get(position).setIsShow(true);
				}

			}
		});
	}

	// 查询乡镇
	protected void queryXiangZheng(final TuBanJieGuoModel tuBanJieGuoModel,
			final MyListView listview) {

		LoadDialog.show(context, true, "查询中……");

		String Url = MyAppliaction.AppUrl + "/getTuBanTongJiXiangZheng.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("quxian", tuBanJieGuoModel.getQushixian());

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LoadDialog.close();
				Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				tubanxiangzhengjieguo(arg0.result, tuBanJieGuoModel, listview);
			}

		});
	}

	// 图斑象征结果
	protected void tubanxiangzhengjieguo(String result,
			TuBanJieGuoModel tuBanJieGuoModel, MyListView listview) {
		System.out.println(result);
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
				Map<String, String> allMap = MapJsonUtils
						.TongjiTuBanXiangZhen(data);
				// 整体数据
				this.bindDataTuBanXiangZheng(allMap, tuBanJieGuoModel, listview);

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

	// 绑定图斑乡镇
	private void bindDataTuBanXiangZheng(Map<String, String> allMap,
			TuBanJieGuoModel tuBanJieGuoModel, final MyListView listview) {
		List<XiangZhengModel> listModel = new ArrayList<XiangZhengModel>();

		for (String s : allMap.keySet()) {
			XiangZhengModel model = new XiangZhengModel();
			model.setXiangzheng(s);
			model.setCount(allMap.get(s));
			listModel.add(model);
		}
		
		tuBanJieGuoModel.setModel(listModel);
		listview.setAdapter(new XiangZhengTuBanAdpter(context,listModel,null));

		listview.setVisibility(View.VISIBLE);
		LoadDialog.close();

	}

	// 选择区域
	private void selectQuYu() {

		Dialog_SelectQuYu.openDialog(context, edit_caijitiaojian_quyu);
	}

	// 统计方式按钮
	private void tongjifangshi() {

		Dialog_CaiJiTongJiFangShi.openDialog(context, fangshi);

	}

	// 选择方式回调接口
	resultCaiJiFangShi fangshi = new resultCaiJiFangShi() {

		@Override
		public void result(String result) {

			switch (result) {
			case "区域":

				quyutongji();
				break;
			case "采集人":

				caijirentongji();
				break;
			case "时间":

				shijiantongji();
				break;

			default:
				break;
			}
		}
	};

	// 选择查询的前后时间
	private void selectTime(EditText edit_startTime2) {
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

						edit_startTime.setText(startTime);
						edit_endTime.setText(endTTime);

					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DATE), true).show();

	}

	// 区域统计
	protected void quyutongji() {
		tv_tongjifangshi.setText("按区域统计");

		caijiState = 3;

		// 隐藏时间控件组
		ll_include_quyu.setVisibility(View.VISIBLE);

		// 显示其他控件组
		ll_include_qita.setVisibility(View.GONE);
		ll_include_time.setVisibility(View.GONE);

	}

	// 采集人统计
	protected void caijirentongji() {
		tv_tongjifangshi.setText("按采集人统计");
		caijiState = 2;

		// 隐藏时间控件组
		ll_include_qita.setVisibility(View.VISIBLE);

		// 显示其他控件组
		ll_include_quyu.setVisibility(View.GONE);
		ll_include_time.setVisibility(View.GONE);

	}

	// 时间统计
	private void shijiantongji() {
		tv_tongjifangshi.setText("按时间统计");
		caijiState = 1;

		// 显示时间控件组
		ll_include_time.setVisibility(View.VISIBLE);

		// 隐藏其他控件组
		ll_include_qita.setVisibility(View.GONE);
		ll_include_quyu.setVisibility(View.GONE);

	}

	// 统计
	private void tongji() {
		// 判断各个条件是否满足
		switch (caijiState) {
		case 1:

			// 判断时间
			if (startTime == null || startTime.equals("") || endTTime == null
					|| endTTime.equals("")) {
				Toast.makeText(context, "请正确填写时间", Toast.LENGTH_LONG).show();
				return;
			}

			getAllShape("", "", startTime, endTTime);
			break;
		case 2:
			// 判断采集人
			if (edit_qita.getText().toString().trim().equals("")) {
				Toast.makeText(context, "请正确填写采集人", Toast.LENGTH_LONG).show();
				return;
			}
			getAllShape(edit_qita.getText().toString(), "", "", "");
			break;
		case 3:
			// 判断区域
			if (edit_caijitiaojian_quyu.getText().toString().trim().equals("")) {
				Toast.makeText(context, "请正确选择区域", Toast.LENGTH_LONG).show();
				return;
			}

			getAllShape("", edit_caijitiaojian_quyu.getText().toString(), "",
					"");
			break;

		default:
			break;
		}

	}

	// 获取保存的所有点
	private void getAllShape(String caijiren, String quyu, String startTime1,
			String endTTime1) {
		LoadDialog.show(context, true, "统计中……");

		String Url = MyAppliaction.AppUrl + "/getGraphicForTime.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId",
				UserSingle.getSingle().userModel.getUserID());
		params.addBodyParameter("startTime", startTime1);
		params.addBodyParameter("endTime", endTTime1);
		params.addBodyParameter("caijiren", caijiren);
		params.addBodyParameter("quyu", quyu);

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
				Map<String, Integer> allMap = MapJsonUtils.TongjiBiaoJi(data);
				// 整体数据
				this.bindData(allMap);

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

	// 绑定列表数据
	private void bindData(Map<String, Integer> allMap) {

		// 整理数据
		CaiJiList = new ArrayList<CaiJiShuJuTongJiModel>();

		// 加入首数据
		CaiJiShuJuTongJiModel model = new CaiJiShuJuTongJiModel();
		model.setQuyu("区县");
		model.setShuliang("采集数据量");
		CaiJiList.add(model);

		// 加入有效数据
		for (String s : allMap.keySet()) {

			System.out.println(s + "  " + allMap.get(s));

			if (allMap.get(s) > 0) {
				CaiJiShuJuTongJiModel model1 = new CaiJiShuJuTongJiModel();
				model1.setQuyu(s);
				model1.setShuliang(allMap.get(s) + "");
				CaiJiList.add(model1);
			}

		}

		// 将数据绑定到列表
		this.bindListview();

	}

	// 将数据绑定到列表
	private void bindListview() {
		LoadDialog.close();
		System.out.println("长度：" + CaiJiList.size());
		lv_tongjijieguo.setAdapter(new CaiJiShuJuAdpter(context, CaiJiList,
				null));

	}

	// 图斑统计按钮
	private void tubantongji() {

		btn_tubantongji.setBackgroundColor(Color.rgb(217, 217, 217));
		btn_caishujutongji.setBackgroundColor(Color.rgb(245, 245, 245));

		// 隐藏采集数据模块
		ll_caijishuju.setVisibility(View.GONE);

		// 显示图斑统计模板
		ll_tubantongji.setVisibility(View.VISIBLE);
	}

	// 采集数据按钮
	private void caijishujutongji() {
		btn_tubantongji.setBackgroundColor(Color.rgb(245, 245, 245));
		btn_caishujutongji.setBackgroundColor(Color.rgb(217, 217, 217));

		// 显示采集数据
		ll_caijishuju.setVisibility(View.VISIBLE);

		// 隐藏图斑统计
		ll_tubantongji.setVisibility(View.GONE);
	}

}
