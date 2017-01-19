package com.map.utils;

import java.util.List;

import android.util.Log;

import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;

/**
 * 线、面转化成String方便保存
 * 
 * @author Beike
 * 
 */
public class PointToString {

	/**
	 * 临时纪录线的String
	 */
	private static String line;

	/**
	 * 将线转化成String
	 * 
	 * @param point
	 * @return
	 */
	public static String pointToString(List<Point> point) {
		StringBuilder sb = new StringBuilder();

		for (Point p : point) {
			sb.append(p.getX());
			sb.append(",");
			sb.append(p.getY());
			sb.append(";");
		}
		Log.i("线转化成String:", sb.toString());
		line = sb.toString();
		return line;
	}


	/**
	 * 讲String转化成点
	 * 
	 * @param point
	 * @return
	 */
	public static Point stringToPoint(String line) {
		Point point = null;
		String[] linePoint = line.split(";");
		Log.i("逆向总长度：", linePoint.length + "");
		Log.i("输出第一个：", linePoint[0]);

		for (String s : linePoint) {
			String[] pointXY = s.split(",");
			Double x = Double.valueOf(pointXY[0]);
			Double y = Double.valueOf(pointXY[1]);
			point = new Point(x, y);

		}
		return point;
	}

	/**
	 * 将String转化成线
	 * 
	 * @param line
	 * @return
	 */
	public static Polyline StringToLine(String line) {
		Polyline poly = new Polyline();
		Boolean isFirst = true;

		String[] linePoint = line.split(";");
		Log.i("逆向总长度：", linePoint.length + "");
		Log.i("输出第一个：", linePoint[0]);
		for (String s : linePoint) {
			String[] pointXY = s.split(",");
			Double x = Double.valueOf(pointXY[0]);
			Double y = Double.valueOf(pointXY[1]);
			Point point = new Point(x, y);

			if (isFirst) {
				poly.startPath(point);
				isFirst = false;
			} else {
				poly.lineTo(point);
			}
		}

		return poly;
	}

	/**
	 * 将String转化成面
	 * 
	 * @param line
	 * @return
	 */
	public static Polygon stringToArea(String area) {
		Polygon tempPolygon = new Polygon();
		Point startPoint = null;
		Point mapPt = null;

		String[] linePoint = area.split(";");

		for (String s : linePoint) {
			String[] pointXY = s.split(",");
			Double x = Double.valueOf(pointXY[0]);
			Double y = Double.valueOf(pointXY[1]);
			Point point = new Point(x, y);
			Line line = new Line();
			mapPt = point;

			if (startPoint == null) {
				startPoint = point;
				line.setStart(startPoint);
			} else {
				line.setStart(startPoint);
				line.setEnd(mapPt);
				startPoint = mapPt;
				tempPolygon.addSegment(line, false);
				
			}			
		}

		return tempPolygon;
	}
	
	
	/*
	 * 上面的方法存入数据格式其实更加方便易懂，但是数据库保存的方式不是这个，所以有了下面这几个方法
	 *  
	 */
	
	/**
	 * 将点数组转化为字符串
	 * @param point
	 * @return
	 */
	public static String pointToValue(List<Point> point) {
		StringBuilder sb = new StringBuilder();

		for (int i=0;i<point.size();i++) {
			Point p=point.get(i);
			
			sb.append(p.getX());
			sb.append(",");
			sb.append(p.getY());
			
			if(i!=point.size()-1)
			{
			  sb.append(",");
			}
			
		}
		line = sb.toString();
		return line;
	}

	
	
	
	/**
	 *  将字符串转化为点
	 * @param line
	 * @return
	 */
	public static Point valueToPoint(String point)
	{
		Point p=new Point();
		String[] b=point.split(",");
		for(int i=0;i<b.length;i++)
		{
			if(i%2==0)
			{		
				try {
					Double x = Double.valueOf(b[i]);
					Double y = Double.valueOf(b[i+1]);
					p.setXY(x, y);
				} catch (NumberFormatException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}	
		return p;	
	}
	

	/**
	 * 将字符串转化为线段
	 * @param line
	 * @return
	 */
	public static Polyline valueToPolyline(String line)
	{
		Polyline poly = new Polyline();
		Boolean isFirst = true;
		String[] b=line.split(",");
		for(int i=0;i<b.length;i++)
		{
			if(i%2==0)
			{		
				try {
					Double x = Double.valueOf(b[i]);
					Double y = Double.valueOf(b[i+1]);
					Point point = new Point(x, y);
					if (isFirst) {
						poly.startPath(point);
						isFirst = false;
					} else {
						poly.lineTo(point);
					}
				} catch (NumberFormatException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}		
		return poly;	
	}
	
	/**
	 * 将字符串转化为面
	 * @param polygon
	 * @return
	 */
	public static Polygon valueToPolygon(String polygon)
	{
		
		Polygon tempPolygon = new Polygon();
		Point startPoint = null;
		Point mapPt = null;
		String[] b=polygon.split(",");
		for(int i=0;i<b.length;i++)
		{
			if(i%2==0)
			{		
				try {
					Double x = Double.valueOf(b[i]);
					Double y = Double.valueOf(b[i+1]);
					Point point = new Point(x, y);
					Line line = new Line();
					mapPt = point;
					
					if (startPoint == null) {
						startPoint = point;
						line.setStart(startPoint);
					} else {
						line.setStart(startPoint);
						line.setEnd(mapPt);
						startPoint = mapPt;
						tempPolygon.addSegment(line, false);
					}
				} catch (NumberFormatException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}	
			}
		}	
		return tempPolygon;
	}
	
}
