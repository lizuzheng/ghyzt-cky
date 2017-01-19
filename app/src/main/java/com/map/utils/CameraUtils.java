package com.map.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.jztx.utils.FileUtils;

public class CameraUtils {

	public static String pathName;
	public static String cameraOpen(Activity activity) {
		String path = "/sdcard/CkyPoject/PointImage/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		String fileName = getPhotoFileName();
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			File filePath = FileUtils.getFile(path, fileName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
			intent.putExtra("filePath", path + fileName);
			activity.startActivityForResult(intent, 21);

		} catch (Exception e) {
		}
		return pathName = path + fileName;
	}

	/** 照片名 */
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd-HH-mm-ss");
		return dateFormat.format(date) + ".jpg";
	}

}
