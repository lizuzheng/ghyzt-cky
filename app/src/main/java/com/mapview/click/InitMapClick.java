package com.mapview.click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.Adapter.GyydListAdpter;
import com.cky.ghyzt.R;
import com.cky.ghyzt.MainActivity;
import com.cky.ghyzt.WebViewDemo;
import com.cky.model.LayerModel;
import com.esri.android.map.Callout;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.identify.IdentifyResult;
import com.map.dialog.DialogUtils;
import com.map.dialog.LoadDialog;
import com.map.utils.IdentifyTaskUtil;
import com.map.utils.LayerIdentTaskUtil;
import com.map.utils.LayerIdentTaskUtil.Iresult;

/**
 * 查询图层功能，针对一些固定的数据，暂时只能显示一些基本字段
 * 
 * @author 祖正
 * 
 */
public class InitMapClick extends MapOnTouchListener {

	private MapView mMapView;
	private Context context;
	private List<LayerModel> listLayers;

	private int nowCount;
	private Point point;

	public InitMapClick(Context context, MapView view) {
		super(context, view);
		this.mMapView = view;
		this.context = context;
		this.listLayers = MainActivity.listLayers;

	}

	@Override
	public boolean onSingleTap(MotionEvent e) {

		
		point = mMapView.toMapPoint(e.getX(), e.getY());

		System.out.print("当前点坐标:"+point.getY()+"  "+point.getX());

		LoadDialog.show(context, true, "查询中……");
		nowCount = listLayers.size() - 1;
		this.queryLayer(point);

		return super.onSingleTap(e);
	}

	// 查询图层
	private void queryLayer(Point point) {

		
		
		if (listLayers.get(nowCount).getIsQuery()
				&& listLayers.get(nowCount).getLayer().isVisible()) {
			// 表示这个图层有查询功能

			String url = listLayers.get(nowCount).getQueryUrl();
			
			
			LayerIdentTaskUtil.queryTask(url, mMapView, point, 20, listLayers
					.get(nowCount).getLayerId(), Gyyd);

		} else {
			if ((nowCount - 1) >= 0) {
				nowCount--;
				
				queryLayer(point);
			}
			else
			{
				mMapView.getCallout().animatedHide();
				LoadDialog.close();
			}
		}
	}

	/**
	 * 查询工业用地回调接口
	 */
	Iresult Gyyd = new Iresult() {

		@Override
		public void result(IdentifyResult[] results) {

			if (results != null && results.length > 0) {
				IdentifyResult Ide = results[0];
				Map<String, Object> map = Ide.getAttributes();

			
				
				// 实例化保存数据的list
				List<Map<String, String>> listGyyd = new ArrayList<Map<String, String>>();

				// 保存单个结果的map
				Map<String, String> mapResult;

				// 开始循环数据
				for (int i = 0; i < listLayers.get(nowCount).getQuertFiled().length; i++) {
					
					System.out.println("查询字段");
					System.out.println(listLayers.get(nowCount).getShowFiled()[i].trim());
					
					//首先判断是不是全景点，这个由显示的字段做标记，openQuanJing
					if(listLayers.get(nowCount).getShowFiled()[i].trim().equals("openQuanJing"))
					{
						//打开网址
						String URL=listLayers.get(nowCount).getQuertFiled()[i].trim();
						URL=map.get(URL).toString();
						Intent intent = new Intent(context, WebViewDemo.class);
						intent.putExtra("URL", URL);
						context.startActivity(intent);
						
						LoadDialog.close();
						//返回
						return;
					}
					
					//首先判断是不是全景点，这个由显示的字段做标记，openQuanJing
					if(listLayers.get(nowCount).getShowFiled()[i].trim().equals("openJieJing"))
					{
						//打开网址
						String GID=listLayers.get(nowCount).getQuertFiled()[i].trim();
						String URL="http://118.123.172.157:8070/newStreet/index.html?id=" + map.get(GID).toString();
						System.out.print(map.get(GID).toString());
						
						Intent intent = new Intent(context, WebViewDemo.class);
						intent.putExtra("URL", URL);
						context.startActivity(intent);
						
						LoadDialog.close();
						//返回
						return;
					}
					
					
					mapResult = new HashMap<String, String>();

					//判断查询字段是否需要连续   多字段格式，a+"米"&b+"亩"
					String s="";
					try {
					
					String queryStr=listLayers.get(nowCount).getQuertFiled()[i].trim();
					//s = map.get(queryStr).toString();
					//1、判断是不是连续字段
					if(queryStr.split("&").length>1)
					{
						//说明有连续字段
						//分离出多个连续情况
						for(int a=0;i<queryStr.split("&").length;a++)
						{
							if(queryStr.split("&")[a].split("#").length>1)
							{
								//单个有分割字符
								s+=queryStr.split("&")[a].split("#")[0];
								s+=map.get(queryStr.split("&")[a].split("#")[1]).toString();
								s+=queryStr.split("&")[a].split("#")[2];
								if(a!=queryStr.split("&").length-1)
								{
									s+=",";
								}
							}
							else
							{
								//没有单位
								s = map.get(queryStr.split("&")[a].split("#")).toString();
							
							}
						}
					}
					else
					{
						//s = map.get(queryStr).toString();
						//没有连续字段
						//2、再次判定是否有单位
						if(queryStr.split("#").length>1)
						{
							//单个有分割字符
							s=queryStr.split("#")[0];
							s+=map.get(queryStr.split("#")[1]).toString();
							s+=queryStr.split("#")[2];
						}
						else
						{
							//没有单位
							s = map.get(queryStr).toString();
						
						}
					}
						
					} catch (Exception e) {}
					
					
					mapResult.put(
							listLayers.get(nowCount).getShowFiled()[i].trim(),
							s);

					listGyyd.add(mapResult);

				}
				
				View view = View.inflate(context, R.layout.dialog_gyyd,
						null);
				ListView list = (ListView) view.findViewById(R.id.listgyyd);
				
				
				if(listLayers.get(nowCount).getQuertFiled().length<7)
				{
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					list.setLayoutParams(params);
				}
				
				list.setAdapter(new GyydListAdpter(listGyyd, context));
				TextView show_tv_title = (TextView) view
						.findViewById(R.id.show_tv_title);
				show_tv_title.setText(map.get(
						listLayers.get(nowCount).getQuertFiled()[0].trim())
						.toString());

				if (listLayers.get(nowCount).getStyle().equals("table")) {
					mMapView.getCallout().animatedHide();
					LoadDialog.close();
					DialogUtils.showDialog(context, view);
				}
				else
				{
					LoadDialog.close();
					final Callout callout = mMapView.getCallout();
					callout.setStyle(R.xml.callout_style);// 设置显示样式
					callout.setContent(view);// callout的显示内容
					callout.setCoordinates(point); // 设置显示的位置为单击的位置
					callout.show();// 显示callout
				}
				
			} else {
				if ((nowCount - 1) >= 0) {
					nowCount--;
					
					//mMapView.getCallout().animatedHide();
					queryLayer(point);
				}
				else
				{
					mMapView.getCallout().animatedHide();
					LoadDialog.close();
				}
			}

		}
	};

}
