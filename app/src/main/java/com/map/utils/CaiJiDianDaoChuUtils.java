package com.map.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.cky.application.MyAppliaction;
import com.cky.model.ShapeModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.map.dialog.LoadDialog;

/**
 * 
 * 
 * @author Administrator
 * 
 *         这个类是用来导出采集点的工具类
 * 
 *         1、保存文本 2、保存图片 3、主控制器
 * 
 * 
 */
public class CaiJiDianDaoChuUtils {

	private List<ShapeModel> isListModel;
	private String savaBasePath;
	private String time;
	private Context context;
	private File txtFile;

	public CaiJiDianDaoChuUtils(Context context, List<ShapeModel> isListModel) {
		this.context = context;
		this.isListModel = isListModel;
	}

	// 主控制器入口
	public void save() {

		// 获取当前的时间
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		time = dateFormat.format(date);

		// 基本保存路径
		savaBasePath = "/sdcard/CkyPoject/CkyGHYZT/采集点导出/";

		// 1、保存图片
		this.saveImage(isListModel, 0);

		// 2、保存坐标信息
		this.savaText(isListModel, 0);

	}

	private void savaText(List<ShapeModel> listModel, int index) {

		String result = "";
		// String result="名称#"+"内容#"+"创建人#"+"创建时间#"+"经纬度数据";
		// result+="\r\n";

		for (int i = 0; i < listModel.size(); i++) {
			String value = listModel.get(i).getCGraphicsGeometry();
			if (listModel.get(i).getCGraphicsType().equals("Polygon")) {
				String[] str = listModel.get(i).getCGraphicsGeometry()
						.split(";");

				String statr = str[0];
				value += statr + ";";

			}

			result += listModel.get(i).getCGraphicsName() + "#"
					+ listModel.get(i).getCGraphicsContent() + "#"
					+ listModel.get(i).getCGraphicsCreatNum() + "#"
					+ listModel.get(i).getCGraphicsCreatTime() + "#"
					+ listModel.get(i).getCGraphicsType() + "#" + value;
			result += "\r\n";
		}

		this.getShapePath(result);

	}

	//获取shape路径
	private void getShapePath(String result) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("infomation", result);
		params.addBodyParameter("f", "json");

		String url = "http://125.70.229.26:6080/arcgis/rest/services/2D_DL_CD2012/MapServer/exts/ConvertToShpSOE/ConvertToShpOperation";
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				LoadDialog.close();

				Toast.makeText(context, "服务器连接失败！！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				downShape(arg0.result);

			}
		});

	}

	//下载shape文件
	protected void downShape(String result) {
		
		//解析json
		String code=JsonUtils.getKey(result, "success");
		
		if(!code.equals("True"))
		{
			Toast.makeText(context, "文件下载失败！", Toast.LENGTH_LONG).show();
			return ;
		}
		
		//获取文件名
		String name=JsonUtils.getKey(result, "filename");
		System.out.println("文件名："+name);
		
//		String[] fileName={name+"Point.dbf",name+"Polygon.dbf",name+"Polyline.dbf",
//				           name+"Point.sbn", name+"Polygon.sbn",name+"Polyline.sbn",
//				           name+"Point.sbx",name+"Polygon.sbx",name+"Polyline.sbx",
//				           name+"Point.shp",name+"Polygon.shp",name+"Polyline.shp",
//				           name+"Point.shx",name+"Polygon.shx",name+"Polyline.shx"};
		
		List<String > listName=new ArrayList<>();
		boolean isPoint=false;
		boolean isXian=false;
		boolean isMian=false;

		for(ShapeModel model:isListModel)
		{
			
			if(model.getCGraphicsType().equals("Point")&&!isPoint)
			{
				isPoint=true;
				listName.add(name+"Point.dbf");
				listName.add(name+"Point.sbn");
				listName.add(name+"Point.sbx");
				listName.add(name+"Point.shp");
				listName.add(name+"Point.shx");
			}
			
			if(model.getCGraphicsType().equals("Polyline")&&!isXian)
			{
				isXian=true;
				listName.add(name+"Polyline.dbf");
				listName.add(name+"Polyline.sbn");
				listName.add(name+"Polyline.sbx");
				listName.add(name+"Polyline.shp");
				listName.add(name+"Polyline.shx");
			}
			
			if(model.getCGraphicsType().equals("Polygon")&&!isMian)
			{
				isMian=true;
				listName.add(name+"Polygon.dbf");
				listName.add(name+"Polygon.sbn");
				listName.add(name+"Polygon.sbx");
				listName.add(name+"Polygon.shp");
				listName.add(name+"Polygon.shx");
			}
			
		}
		
		
		for(String s:listName)
		{
			this.downFile(name,s);
			System.out.println(s);
		}
		
	}

	//开始下载文件
	private void downFile(String Name, String fileName) {
		
		String url="http://125.70.229.26/downShape/downShape.ashx?pathName="+Name+"&fileName="+fileName;
		String savePath=savaBasePath+time+"/Shape/"+fileName;
		
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
//		params.addBodyParameter("pathName",Name );
//		params.addBodyParameter("fileName", fileName);
		http.download(HttpMethod.GET, url, savePath, params,
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						super.onLoading(total, current, isUploading);
						

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						System.out.println(arg0.toString() + "  " + arg1);

						// 下载失败
						Toast.makeText(context, "下载失败", Toast.LENGTH_LONG)
								.show();

					}
				});
		
	}

	// 保存图片
	private void saveImage(List<ShapeModel> listModel, int index) {

		// 判断循环是否完毕
		if (listModel.size() - 1 < index) {
			LoadDialog.close();
			Toast.makeText(context, "导出成功，文件夹：" + savaBasePath + time,
					Toast.LENGTH_LONG).show();
			return;
		}

		// 执行保存图片操作
		ShapeModel model = listModel.get(index);

		// 保存路径
		if (model.getListImage() != null && model.getListImage().size() > 0) {
			for (int i = 0; i < model.getListImage().size(); i++) {
				String url = MyAppliaction.AppUrl
						+ "getGraphicImage.ashx?photoname="
						+ model.getListImage().get(i);

				String fileName = this
						.getImageName(model.getListImage().get(i));
				String shapeName = model.getCGraphicsName();

				System.out.println(fileName);

				// 组合文件保存在何处
				String savaPath = savaBasePath + time + "/" + shapeName + "/"
						+ fileName;

				// 执行下载操作
				this.downImage(url, savaPath, index);
			}
		} else {
			saveImage(isListModel, index + 1);
		}

	}

	// 下载图片
	private void downImage(final String url, String savaPath, final int index) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		http.download(HttpMethod.GET, url, savaPath, params,
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						super.onLoading(total, current, isUploading);

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {

						System.out.println("下载成功： " + url);
						saveImage(isListModel, index + 1);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						System.out.println(arg0.toString() + "  " + arg1);

					}
				});

	}

	// 获取图片名称
	private String getImageName(String str) {

		return str.split("//")[1];
	}

}
