package com.cky.ghyzt;

import java.util.ArrayList;
import java.util.List;

import com.cky.Adapter.ListSelectImgLayer;
import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.ghyzt.R;
import com.cky.model.LayerImageModel;
import com.cky.model.LayerModel;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.DialogUtils;
import com.map.dialog.LoadDialog;
import com.map.utils.JsonUtils;
import com.map.utils.MapJsonUtils;
import com.mapview.click.ImgMapTouchClick;
import com.mapview.click.ImgMapZoomClick;
import com.mapview.click.InitMapClick;
import com.mapview.click.MapLoad;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 这里个有已知的几处不妥的地方 1、当要设置底图的时候，不能很好的在配置文件中表达出来
 * 2、有些图层缩小大一定级别的时候就不显示了，暂不知道原因，目前只是将地图设置了最小显示比例解决该问题
 * 
 * @author Administrator
 * 
 */
public class ImgContrastActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private Context context;
	private MapView img_map_left;
	private MapView img_map_right;
	private List<LayerImageModel> listImage;
	private List<LayerImageModel> listImage_jiben;
	private List<LayerImageModel> listImage_ditu;

	private Button img_btnselect1;
	private Button img_btnselect2;
	
	private int left=0;
	private int right=0;

	private double minSc = 1616895;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_contrast);

		// 初始化全局数据
		this.init();

		// 初始化控件
		this.viewInit();

		// 数据初始化
		this.dataInit();

	}

	// 初始化基本数据
	private void init() {
		context = this;

	}

	// 数据初始化
	private void dataInit() {

		LoadDialog.show(context, false, "地图加载中……");


		// 去服务端获取数据
		initLayer();

	}

	// 读取个人图层
	private void initLayer() {

		String url = MyAppliaction.AppUrl + "getImageLayer.ashx";

		RequestParams params = new RequestParams();
		params.addBodyParameter("userNum", UserSingle.getSingle().userModel.getUserNum());
		params.addBodyParameter("orgNum", MyAppliaction.OrgNum);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				layerResult(arg0.result);

			}

		});

	}

	// 处理获取到的结果
	private void layerResult(String result) {

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
				listImage = MapJsonUtils.getLayersImage(data);


				//添加到地图
				this.addToMapView();
				
				

			} else {
				String msg = JsonUtils.getKey(result, "msg");
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "返回数据异常！", Toast.LENGTH_LONG).show();
		}

		LoadDialog.close();

	}

	//设置当前的选择的图层
	public void setLayer(int position,String str)
	{

		
		if(str.equals("Left"))
		{
			//基本图全部隐藏
			for(int i=0;i<listImage_jiben.size();i++)
			{
				listImage_jiben.get(i).getLayer().setVisible(false);
			}
			//显示当前的
			listImage_jiben.get(position).getLayer().setVisible(true);
			img_btnselect1.setText(listImage_jiben.get(position).getLayer().getName());
		}
		else
		{
			//基本图全部隐藏
			for(int i=0;i<listImage_jiben.size();i++)
			{
				listImage_jiben.get(i).getLayerRight().setVisible(false);
			}
			//显示当前的
			listImage_jiben.get(position).getLayerRight().setVisible(true);
			img_btnselect2.setText(listImage_jiben.get(position).getLayerRight().getName());
		}
	}
	
	//将数据添加到地图上
	private void addToMapView() {
		
		//设置一个正常的底图，将其隐藏，获取到当前地图的最小比例，目前是最快速的解决方案
		Layer layer=new ArcGISTiledMapServiceLayer("http://125.70.229.64:6080/arcgis/rest/services/WP_2012_CD2012/MapServer");
		img_map_left.addLayer(layer);
		
		System.out.println(img_map_left.getMinScale()+" 获取最小的比例尺");
		layer.setVisible(false);

		//分离基本图层和底图
		
		 listImage_jiben=new ArrayList<LayerImageModel>();
		 listImage_ditu=new ArrayList<LayerImageModel>();
		
		
		
		for(int i=0;i<listImage.size();i++)
		{
			//如果是底图
			if(listImage.get(i).getIsBaise())
			{
				listImage_ditu.add(listImage.get(i));
			}
			else
			{
				listImage_jiben.add(listImage.get(i));
			}
			
			
		
			
		}
		
		
		
		
	}

	// 控件初始化
	private void viewInit() {

		// 返回地图
		Button btn_return = (Button) findViewById(R.id.btn_img_return);
		btn_return.setOnClickListener(this);

		// 地图1
		img_map_left = (MapView) findViewById(R.id.img_map_left);
		 img_map_left.setOnStatusChangedListener(new MyMapLoadLeft());

		// 地图2
		img_map_right = (MapView) findViewById(R.id.img_map_right);

		// 地图改变移动事件
		img_map_left.setOnTouchListener(new ImgMapTouchClick(context,
				img_map_left, img_map_right));

		img_map_right.setOnTouchListener(new ImgMapTouchClick(context,
				img_map_right, img_map_left));

		// 比例尺变化事件
		img_map_left.setOnZoomListener(new ImgMapZoomClick(context,
				img_map_left, img_map_right));
		img_map_right.setOnZoomListener(new ImgMapZoomClick(context,
				img_map_right, img_map_left));

		// 放大按钮
		Button btn_fangda = (Button) findViewById(R.id.fangda);
		btn_fangda.setOnClickListener(this);

		// 缩小按钮
		Button btn_suoxiao = (Button) findViewById(R.id.suoxiao);
		btn_suoxiao.setOnClickListener(this);

		// 放大按钮，右边
		Button btn_fangfa_right = (Button) findViewById(R.id.fangda_right);
		btn_fangfa_right.setOnClickListener(this);

		// 缩小按钮，右边
		Button btn_suoxiao_right = (Button) findViewById(R.id.suoxiao_right);
		btn_suoxiao_right.setOnClickListener(this);

		// 影像选择
		img_btnselect1 = (Button) findViewById(R.id.img_btnselect1);
		img_btnselect1.setOnClickListener(this);

		img_btnselect2 = (Button) findViewById(R.id.img_btnselect2);
		img_btnselect2.setOnClickListener(this);
	}

	//加载图层
	private void loadLayer()
	{
		//将基本图按照顺序添加到地图上
		
				for (int i = 0; i < listImage_jiben.size(); i++) {
					
					//添加之前要将地图的比例尺修改到正常值，这是因为本身的bug
					listImage_jiben.get(i).getLayer().setMinScale(minSc);
					listImage_jiben.get(i).getLayerRight().setMinScale(minSc);
					
					img_map_left.addLayer(listImage_jiben.get(i).getLayer());
					img_map_right.addLayer(listImage_jiben.get(i).getLayerRight());
					
					//判断默认显示的图层
					if(listImage_jiben.get(i).getIsShow().equals("left"))
					{
						left=i;
					}
					if(listImage_jiben.get(i).getIsShow().equals("right"))
					{
						right=i;
					}
				}
				
				
				
				//设置默认值
				if(listImage_jiben.size()>=2)
				{
					setLayer(left,"Left");
					setLayer(right,"Right");
				}
				
				//将底图按照顺序添加到地图上
				for (int i = 0; i < listImage_ditu.size(); i++) {
					//设置最小比例尺
					listImage_ditu.get(i).getLayer().setMinScale(minSc);
					listImage_ditu.get(i).getLayerRight().setMinScale(minSc);
					
					
					
					
					img_map_left.addLayer(listImage_ditu.get(i).getLayer());
					img_map_right.addLayer(listImage_ditu.get(i).getLayerRight());
					
					//设置显示
					listImage_ditu.get(i).getLayer().setVisible(true);
					listImage_ditu.get(i).getLayerRight().setVisible(true);
				}
	}
	
	
	// 创建一个值，记录当前点击的是哪个按钮
	String state = null;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 返回地图
		case R.id.btn_img_return:
			this.finish();
			break;

		case R.id.fangda:
			img_map_left.zoomout();
			break;

		case R.id.suoxiao:
			img_map_left.zoomin();
			break;

		case R.id.fangda_right:
			img_map_right.zoomout();
			break;
		case R.id.suoxiao_right:
			img_map_right.zoomin();
			break;

		case R.id.img_btnselect1:
			state = LEFT;
			showSelectImgLayer();
			break;

		case R.id.img_btnselect2:
			state = RIGHT;
			showSelectImgLayer();
			break;

		default:
			break;
		}
	}

	// 打开图层选择弹出框
	private void showSelectImgLayer() {

		// [start] 加载弹出框需要的view

		View view = View.inflate(context, R.layout.selectimglayer_layer, null);
		ListView listview = (ListView) view.findViewById(R.id.list_selectimg);
		listview.setAdapter(new ListSelectImgLayer(context, listImage_jiben));
		listview.setOnItemClickListener(this);

		// [end]

		DialogUtils.showDialog(context, view);

	}

	//设置选择的图层
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("点击了我"+position);
		if (state == null)
			return;
		if (state.equals(LEFT)) {
			setLayer(position,"Left");
		} else {
			setLayer(position,"Right");
		}
		DialogUtils.closeDialog();

	}

	// 左边地图加载完成执行
	class MyMapLoadLeft implements OnStatusChangedListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 691013098610703559L;

		@Override
		public void onStatusChanged(Object source, STATUS status) {
			if (status == STATUS.INITIALIZED) {
				minSc=img_map_left.getMinScale();
				loadLayer();
				img_map_right.setOnStatusChangedListener(new MyMapLoadRight());

			} else if (status == STATUS.LAYER_LOADED) {
			} else if ((status == STATUS.INITIALIZATION_FAILED)) {
			} else if ((status == STATUS.LAYER_LOADING_FAILED)) {
			}
		}
	}

	// 右边地图加载完成执行
	class MyMapLoadRight implements OnStatusChangedListener {

		private static final long serialVersionUID = 691013098610703559L;

		@Override
		public void onStatusChanged(Object source, STATUS status) {
			if (status == STATUS.INITIALIZED) {

				img_map_right.centerAt(img_map_left.getCenter(), false);
				img_map_right.setScale(img_map_left.getMinScale());
				LoadDialog.close();

			} else if (status == STATUS.LAYER_LOADED) {
			} else if ((status == STATUS.INITIALIZATION_FAILED)) {
			} else if ((status == STATUS.LAYER_LOADING_FAILED)) {
			}
		}
	}

}
