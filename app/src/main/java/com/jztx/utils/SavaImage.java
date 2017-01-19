package com.jztx.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

public class SavaImage{
	@SuppressLint("SdCardPath")
	public static Boolean saveImage(Bitmap bitmap, String fileName) {

		FileOutputStream b = null;
		File file = new File("/sdcard/CkyCamera/");
		file.mkdirs();// 创建文件夹
		String filename = "/sdcard/CkyCamera/" + fileName + ".jpg";

		try {
			b = new FileOutputStream(filename);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
