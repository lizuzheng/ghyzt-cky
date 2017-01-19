package com.jztx.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BitmapUtils {
	
	
	public static Bitmap comp(Activity activity, Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		}
		ByteArrayInputStream isbm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newopts = new BitmapFactory.Options();
		newopts.inJustDecodeBounds = true;
		Bitmap bt = BitmapFactory.decodeStream(isbm, null, newopts);
		newopts.inJustDecodeBounds = false;
		int w = newopts.outWidth;
		int h = newopts.outHeight;
		
		WindowManager wm = activity.getWindowManager();
		Display ld = wm.getDefaultDisplay();

		float ww = (int) (ld.getWidth() * 0.8);
		float hh = (int) (ld.getHeight() * 0.5);
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newopts.outWidth / ww);
		} else {
			be = (int) (newopts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newopts.inSampleSize = be;
		isbm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap= BitmapFactory.decodeStream(isbm, null, newopts);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		int ys = 100;
		while (out.toByteArray().length / 1024 > 100) {
			out.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, ys, out);
			ys -= 5;
		}
		ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());		
		return BitmapFactory.decodeStream(input);
	}
	

	public static Bitmap compPath(Activity activity,String filePath)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, null);
		
		return comp(activity, bitmap);
	}
	
	
	public static Bitmap viewToBit(View view)
	{
		 Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
	                Bitmap.Config.ARGB_8888);
		 Canvas canvas = new Canvas(bitmap);
	        view.draw(canvas);
	   return bitmap;
	}
	
	
	/**
	* 把View绘制到Bitmap上
	* @param view 需要绘制的View
	* @param width 该View的宽度
	* @param height 该View的高度
	* @return 返回Bitmap对象
	* add by csj 13-11-6
	*/
	public static Bitmap getViewBitmap(View v) {
		Bitmap bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
		Canvas ca=new Canvas(bitmap);
		v.draw(ca);
			
		return bitmap;
	}
	
}
