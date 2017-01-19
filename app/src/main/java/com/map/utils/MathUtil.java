package com.map.utils;

import java.math.BigDecimal;

public class MathUtil
{
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
}