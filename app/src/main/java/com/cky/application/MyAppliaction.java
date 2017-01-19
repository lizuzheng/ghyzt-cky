package com.cky.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cky.model.HouseModel;
import com.cky.model.ShapeModel;
import com.cky.model.UserModel;
import com.esri.android.runtime.ArcGISRuntime;
import com.map.utils.PhotoName;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.content.Context;

public class MyAppliaction extends Application {

	private static Context _context;
	
	//全局保存登陆者的信息
	public  UserModel user;	
	//临时保存的标记图形
	public static Map<Integer,ShapeModel> mapPoint=new HashMap<Integer, ShapeModel>();
	//全局app类型
	public final static String OrgNum="GHYZT";
	//全局ip地址
	public final static String AppUrl="http://125.70.229.8/CKYGhyzt/";
	
	public static String PhotoName="";
	private static int i=0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//bug收集
		CrashReport.initCrashReport(getApplicationContext(), "fae0b5c3ec", false);
		
		ArcGISRuntime.setClientId("jnPjg34BVXUa7rj4");
		
		_context = getApplicationContext();
		
		initImageLoader(getApplicationContext());
		
	}
	
	

	public static String getPhotoName()
	{
		i++;
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		PhotoName=dateFormat.format(date)+i + ".jpg";			
		return PhotoName;
	}
	
	
	public static Context getAppContext() {

		return MyAppliaction._context;
	}

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	
}
