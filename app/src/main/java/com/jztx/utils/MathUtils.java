package com.jztx.utils;

import java.math.BigDecimal;
import java.util.List;

import com.esri.core.geometry.Point;

/**
 * 数学算法相关类
 * @author Beike
 *
 */
public class MathUtils {

	/**
	 * 判断一个数是否属于数组
	 * @param group
	 * @param num
	 * @return
	 */
	public static boolean isGroup(int[] group,int num)
	{
		for(int i:group)
		{
			if(num==i)
				return true;
		}		
		return false;
	}
	/**
	 * 判断一个数组的元素是否有和另一个数组相同的
	 * @param group
	 * @param num
	 * @return
	 */
	public static boolean isGroup(List<Integer> group,int[] num)
	{
		for(int i:group)
		{
			for(int j:num)
			{
				if(i==j)
	               return true;
			}
		}		
		return false;
	}
	/**
	 * 将双精度浮点值按指定的小数位数舍入
	 * @param value 要舍入的双精度浮点数
	 * @param digits 返回值中的小数数字
	 * @return
	 */
	public static double round(double value, int digits)
	{
		BigDecimal bigDecimal = new BigDecimal(value);
		double result = bigDecimal.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();		
		return result;
	}
	
	/**
	 * 判断一个点是否在一个多边形内
	 * @param list
	 * @param p
	 * @return
	 */
	public static boolean Contains(List<Point> list, Point p ) 
	{
		Point[] points=new Point[list.size()];
		for(int i=0;i<list.size();i++)
		{
			points[i]=list.get(i);
		}	
	   Boolean result = false;
	   for( int i = 0; i < points.length - 1; i++ )
	   {
	      if( ( ( ( points[ i + 1 ].getY()<= p.getY() ) && ( p.getY() < points[ i ].getY() ) ) || ( ( points[ i ].getY() <= p.getY() ) && ( p.getY() < points[ i + 1 ].getY() ) ) ) && ( p.getX() < ( points[ i ].getX() - points[ i + 1 ].getX() ) * ( p.getY() - points[ i + 1 ].getY() ) / ( points[ i ].getY() - points[ i + 1 ].getY() ) + points[ i + 1 ].getX() ) )
	      {
	         result = !result;
	      }
	   }
	   return result;
	}
}




