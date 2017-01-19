//package com.map.utils;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.apache.http.HttpResponse;
//
//public class StreamTools {
//	/**
//	 * @param is 输入流
//	 * @return String 返回的字符串
//	 * @throws IOException
//	 */
//	public static String readFromStream(InputStream is){
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while((len = is.read(buffer))!=-1){
//				baos.write(buffer, 0, len);
//			}
//			is.close();
//			String result = baos.toString();
//			baos.close();
//			return result;
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//
//	/**
//	 * 将HttpResponse转化成一个输入流
//	 *
//	 * @return 输入流
//	 *
//	 */
//	public static InputStream resInputStream(HttpResponse hp)
//	{
//		try {
//			InputStream is=hp.getEntity().getContent();
//			return is;
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
