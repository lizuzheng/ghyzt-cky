package com.jztx.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

/*
 * 文件操作�?
 * 
 */
public class FileUtils {

	/**
	 * 传入一个路径，生成一个File
	 */
	public static File getFile(String path, String fileName) {

		String fileNamePath = path + fileName;
		File file = new File(fileNamePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 保存一个bitmap
	 * 
	 * @param path
	 * @param fileName
	 * @param bitmap
	 * @return
	 */
	public static File saveBit(String path, String fileName, Bitmap bitmap) {
		if (bitmap == null)
			return null;

		File file1=new File(path);
		
		if(!file1.exists())
		{
			file1.mkdirs();
		}
		
		File file = new File(path, fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			
			return file;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;

	}

}
