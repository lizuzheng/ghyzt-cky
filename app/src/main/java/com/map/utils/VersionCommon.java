package com.map.utils;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;



import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionCommon {
	public static final String SERVER_IP="http://192.168.1.105/";
	public static final String SERVER_ADDRESS=SERVER_IP+"try_downloadFile_progress_server/index.php";//软件更新包地址
	public static final String UPDATESOFTADDRESS=SERVER_IP+"try_downloadFile_progress_server/update_pakage/baidu.apk";//软件更新包地址

	
	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context,String pageName) {
        int verCode = -1;
        try {
        	//注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
            		pageName, 0).versionCode;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verCode;
    }
   /**
    * 获取版本名称
    * @param context
    * @return
    */
    public static String getVerName(Context context,String pageName) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
            		pageName, 0).versionName;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verName;   
        
       
}
    /**
     * 获取系统版本号，只返回大版本，5 or 4……
     * @return
     */
	public static int getReleade()
	{
		int a=0;
		String releade=android.os.Build.VERSION.RELEASE;
		System.out.println("系统版本号:"+releade);
		String banben=releade.substring(0, 1);
		a=Integer.parseInt(banben);
		return a;
	}
	
	
	
	
	
}
