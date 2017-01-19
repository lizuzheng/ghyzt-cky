package com.map.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.cky.application.MyAppliaction;
import com.cky.application.UserSingle;
import com.cky.model.GPSRrecordModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SaveTextUtils {

	// 在文本中插入数据，临时保存gps数据，登陆的时候再将其上传回服务器
	public static boolean saveUserIfo(GPSRrecordModel model) {

		String path = "/sdcard/CkyPoject/TFXQJCZ/";

		File file1 = new File(path);

		if (!file1.exists()) {
			file1.mkdirs();
		}

		File file = new File(path, "GPS.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}

		String result = model.getGPSRrecordCreatTime() + "#"
				+ model.getGPSRrecordID() + "#" + model.getGPSRrecordLat()
				+ "#" + model.getGPSRrecordLong() + "#"
				+ model.getGPSRrecordOrgNum() + "#"
				+ model.getGPSRrecordUserNum() + "\n";

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;

	}

	
	//导出GPS数据
	public static Boolean daochuGPS(List<GPSRrecordModel> list)
	{
		String path = "/sdcard/CkyPoject/TFXQJCZ/";

		File file1 = new File(path);

		if (!file1.exists()) {
			file1.mkdirs();
		}

		File file = new File(path, "GPS导出-"+UserSingle.getSingle().userModel.getUserName()+".txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}

		String result="创建者  "+"                 轨迹ID                "+"             经度             "+"        纬度                 "+"   创建时间 ";
		result+="\n";
		
		for(int i=0;i<list.size();i++)
		{
			result+=list.get(i).getGPSRrecordUserName()+"      "+list.get(i).getGPSRrecordID()+"     "+list.get(i).getGPSRrecordLong()+"     "+list.get(i).getGPSRrecordLat()+"    "+list.get(i).getGPSRrecordCreatTime();
			result+="\n";
		}
		
		
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, false)));
			out.write(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
	
	
	
	//
	public static void getGPSModdel() {
		File file = new File("/sdcard/CkyPoject/TFXQJCZ/GPS.txt");// Text文件
		List<GPSRrecordModel> listmodel = new ArrayList<GPSRrecordModel>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {
				// 2016-04-02
				// 16:44:33#1793931c-9434-409c-91a1-4abfeba7f4fb#211950.69411403703#228695.45209950255#http://125.70.229.8/TFXQServer/#admin
				GPSRrecordModel model = new GPSRrecordModel();
				model.setGPSRrecordCreatTime(s.split("#")[0]);
				model.setGPSRrecordID(s.split("#")[1]);
				model.setGPSRrecordLat(s.split("#")[2]);
				model.setGPSRrecordLong(s.split("#")[3]);
				model.setGPSRrecordOrgNum(s.split("#")[4]);
				model.setGPSRrecordUserNum(s.split("#")[5]);
				listmodel.add(model);
			}

			br.close();
		} catch (IOException e) {

		}

		// 上传
		if (listmodel != null && listmodel.size() > 0) {
			// 并发会使服务器崩溃
			for (int i = 0; i < listmodel.size(); i++) {
				try {
					Thread.sleep(50);
				} catch (Exception e) {
				}

				shangChuan(listmodel.get(0));

			}

			// 重置文本
			File f = new File("/sdcard/CkyPoject/TFXQJCZ/GPS.txt"); // 输入要删除的文件位置
			if(f.exists())
			 f.delete(); 
		}

	}

	// 上传事件
	private static void shangChuan(GPSRrecordModel model) {
		final GPSRrecordModel model1 = model;
		String Url = MyAppliaction.AppUrl + "addGPS.ashx";
		RequestParams params = new RequestParams();
		params.addBodyParameter("GPSRrecordID", model.getGPSRrecordID());
		params.addBodyParameter("GPSRrecordCreatTime",
				model.getGPSRrecordCreatTime());
		params.addBodyParameter("GPSRrecordLat", model.getGPSRrecordLat());
		params.addBodyParameter("GPSRrecordLong", model.getGPSRrecordLong());
		params.addBodyParameter("GPSRrecordUserNum",
				model.getGPSRrecordUserNum());
		params.addBodyParameter("GPSRrecordOrgNum", model.getGPSRrecordOrgNum());

		HttpUtils http = new HttpUtils();

		http.send(HttpMethod.POST, Url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// 保存到本地
				SaveTextUtils.saveUserIfo(model1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				result(arg0.result, model1);

			}
		});
	}

	    //处理结果
	protected static void result(String result, GPSRrecordModel model) {
		try {
			String code = JsonUtils.getKey(result, "code");

			if (code == null || code.equals("")) {
				// SaveTextUtils.saveUserIfo(model);
				return;
			}
			// 成功
			if (code.equals("Success")) {

			} else {
				// SaveTextUtils.saveUserIfo(model);
			}
		} catch (Exception e) {
			// SaveTextUtils.saveUserIfo(model);
		}
	}
}
