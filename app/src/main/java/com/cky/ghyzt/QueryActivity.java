package com.cky.ghyzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cky.application.MyAppliaction;
import com.cky.common.GraphicHelper;
import com.cky.common.QueryLoad;
import com.cky.common.SerializableChaXunZhuanTi;
import com.cky.common.SerializableMap;
import com.cky.ghyzt.R;
import com.cky.model.AutoTextModel;
import com.cky.model.QueryViewModel;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.jztx.utils.PixelUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.DialogUtils;
import com.map.dialog.LoadDialog;
import com.map.utils.MapJsonUtils;
import com.mapview.click.QueryClickPoi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class QueryActivity extends Activity implements OnClickListener {

	private Context context;
	private Activity activity;
	private Spinner sp;
	private ListView list_auto;
	private MapView mMapView;
	private GraphicsLayer ghlayer;
	private EditText ed_sousuo;
	private List<AutoTextModel> listAutoTextModel;
	private Boolean isFirst = true;
	private List<AutoTextModel> listAutoTextModelAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);

		// 初始化必需数据
		this.init();
		// 控件初始化
		this.viewInit();
		// 数据初始化
		this.dataInit();

	}

	// 初始化必需数据
	private void init() {
		context = this;
		activity = this;
		ghlayer = MainActivity.ghlayer;
		mMapView = MainActivity.mMapview;
	}

	// 数据初始化
	private void dataInit() {

		// 获取原来的值
		QueryViewModel sMap = (QueryViewModel) getIntent().getExtras()
				.getSerializable("value");

		if (sMap != null) {
			ed_sousuo.setText(sMap.getText());
			
			listAutoTextModelAll=listAutoTextModel=sMap.getListAutoTextModel();
			// 绑定到listvew
			list_auto.setAdapter(new ListAdadter(listAutoTextModel));
			// listView的点击事件
			list_auto.setOnItemClickListener(new MyOnItemLinstener());
			String xuanxiang = sMap.getItemText();
			int a = 0;
			switch (xuanxiang) {
			case "智能":
				a = 0;
				break;
			case "兴趣点":
				a = 1;
				break;
			case "道路":
				a = 2;
				break;
			case "门址":
				a = 3;
				break;

			default:
				break;
			}

			sp.setSelection(a);

		}

		isFirst = false;

	}

	// 控件初始化
	private void viewInit() {
		Button fanhui = (Button) findViewById(R.id.sousuo_btn_fanhui);
		fanhui.setOnClickListener(this);
		ed_sousuo = (EditText) findViewById(R.id.sousuo_ed_sousuo);
		ed_sousuo.addTextChangedListener(new MyEdChange());
		sp = (Spinner) findViewById(R.id.sousuo_sp);
		Button btn_sousuo = (Button) findViewById(R.id.sousuo_btn_sousuo);
		btn_sousuo.setOnClickListener(this);
		list_auto = (ListView) findViewById(R.id.sousuo_list);

	}

	// 文本框改变事件
	class MyEdChange implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (!isFirst) {
				// 获取查询字段
				String value = s.toString();
				// 执行查询
				query(value, "");
			}

		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	}

	// 执行线程查询
	private void query(String value, final String laiyuan) {

		String type = "";
		if (value != null && !value.equals("")) {
			// 获取查询条件
			String getCategory = sp.getSelectedItem().toString();
			type = getCategory(getCategory);

		} else {
			// 获取查询条件
			String getCategory = sp.getSelectedItem().toString();
			type = getCategory(getCategory);

		}

		String ClientRequest = "AutoComplete";
		if (laiyuan.equals("Btn")) {
			ClientRequest = "WildSearch";
		}

		String url = "http://125.70.229.8/AutoTextServer/Handler/LocationSearch.ashx";

		RequestParams params = new RequestParams();
		params.addBodyParameter("ClientRequest", ClientRequest);
		params.addBodyParameter("data", value);
		params.addBodyParameter("category", type);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				queryResult(arg0.result, laiyuan);

			}

		});

	}

	// 处理查询的结果
	protected void queryResult(String result, String laiyuan) {
		System.out.println(result);

		// 解析得到查询到的值数组
		listAutoTextModel = MapJsonUtils.getAutoText(result);
		listAutoTextModelAll=listAutoTextModel;
		if (laiyuan.equals("Btn")) {
			drawGraphics(listAutoTextModel);
		}

		// 绑定到listvew
		list_auto.setAdapter(new ListAdadter(listAutoTextModel));

		// listView的点击事件
		list_auto.setOnItemClickListener(new MyOnItemLinstener());

	}

	// 获取到查询的类型
	private String getCategory(String type) {
		String category = "";
		if (type.equals("智能")) {
			category = "POI,STREET,ROAD";
		} else if (type.equals("兴趣点")) {
			category = "POI";
		} else if (type.equals("道路")) {
			category = "ROAD";
		} else if (type.equals("门址")) {
			category = "STREET";
		}

		return category;
	}

	// 按钮的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sousuo_btn_fanhui:
			this.finish();
			break;
		case R.id.sousuo_btn_sousuo:
			LoadDialog.show(context, true, "绘制中……");
			query(ed_sousuo.getText().toString(), "Btn");
			break;

		default:
			break;
		}

	}

	// 将获取到的结果绑定的listview
	class ListAdadter extends BaseAdapter {
		List<AutoTextModel> list;

		public ListAdadter(List<AutoTextModel> listAutoTextModel) {
			this.list = listAutoTextModel;
		}

		@Override
		public int getCount() {
			if (list == null || list.size() == 0) {
				return 0;
			} else {
				return list.size();
			}
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

			View view = View.inflate(context, R.layout.child_showqueryresult,
					null);

			// 名字
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			// 性质
			TextView tv_type = (TextView) view.findViewById(R.id.tv_type);

			tv_name.setText(list.get(position).getLabel());
			tv_type.setText(list.get(position).getType());

			return view;
		}

	}

	// item的点击事件
	class MyOnItemLinstener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AutoTextModel autoTextModel = listAutoTextModel.get(position);
			List<AutoTextModel> listautoTextModel = new ArrayList<AutoTextModel>();
			listautoTextModel.add(autoTextModel);
			// 绘制搜索点
			drawGraphics(listautoTextModel);

		}

	}

	// 判断是否是点
	private void drawGraphics(List<AutoTextModel> listautoTextModel) {

		if (listautoTextModel != null) {

			String where = "";
			// 清空已经绘制的图形
			ghlayer.removeAll();

			// 关闭之前保存一遍数据
			QueryViewModel model = new QueryViewModel();
			model.setItemText(sp.getSelectedItem().toString());
			model.setListAutoTextModel(listAutoTextModelAll);
			model.setText(ed_sousuo.getText().toString());

			// 创建一个map记录绘制点的id
			Map<Integer, AutoTextModel> mapId = new HashMap<Integer, AutoTextModel>();
			Map<Integer, Graphic> mapGh = new HashMap<Integer, Graphic>();
			// 循环绘制
			for (int i = 0; i < listautoTextModel.size(); i++) {
				AutoTextModel autoTextModel = listautoTextModel.get(i);
				// 如果是点，就直接绘制
				if (autoTextModel.getCategory().equals("POI")
						|| autoTextModel.getCategory().equals("STREET")) {
					// 绘制图形
					Drawable drawable = getDrawable(i, context);
					PictureMarkerSymbol symbol = new PictureMarkerSymbol(
							drawable);
					Graphic graphic = new Graphic(autoTextModel.getPoint(),
							symbol, null, 0);
					int id = ghlayer.addGraphic(graphic);

					Envelope mapExtend = GraphicHelper.getExtent(graphic);

					// 计算中心点
					Point point = mapExtend.getCenter();
					// 设置分辨率
					mMapView.setScale(46182.92920838987);
					// 地图设置中心点
					mMapView.centerAt(point, true);

					// 将值添加到对应的Map
					mapId.put(id, autoTextModel);
				}
				// 处理是道路的情况
				if (autoTextModel.getCategory().equals("ROAD")) {
					if (where.equals("")) {
						where += "ROADNAME ='" + autoTextModel.getLabel() + "'";
					} else {
						where += " or ROADNAME='" + autoTextModel.getLabel()
								+ "'";
					}
				}

			}

			if (!where.equals("")) {
				QueryLoad queryLoad = new QueryLoad(this, model);
				queryLoad.queryLoad(context, mMapView, where, mapGh);

				// 将地图的点击事件转向到新的点击事件
				mMapView.setOnTouchListener(new QueryClickPoi(context,
						mMapView, ghlayer, mapId, mapGh));
			} else {

				// 将地图的点击事件转向到新的点击事件
				mMapView.setOnTouchListener(new QueryClickPoi(context,
						mMapView, ghlayer, mapId, mapGh));

				// 切换到地图
				Intent intent = new Intent();

				intent.putExtra("QueryViewModel", model);
				setResult(21, intent);
				this.finish();

			}

		}
	}

	// 返回一个Drawable
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(int i, Context context) {
		View view = View.inflate(context, R.layout.number_layer, null);
		// 获得布局文件中的TextView
		TextView tv = (TextView) view.findViewById(R.id.number_tv);
		if (i + 1 >= 10) {
			tv.setText("");
		} else {
			tv.setText("");
		}

		// 启用绘图缓存
		view.setDrawingCacheEnabled(true);
		// 调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
		view.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));
		// 这个方法也非常重要，设置布局的尺寸和位置
		int r = PixelUtils.dipTopx(context, 30);
		int h = PixelUtils.dipTopx(context, 60);

		view.layout(0, 0, r, h);
		// 获得绘图缓存中的Bitmap
		view.buildDrawingCache();

		Bitmap bit = view.getDrawingCache();
		return new BitmapDrawable(bit);

	}

}
