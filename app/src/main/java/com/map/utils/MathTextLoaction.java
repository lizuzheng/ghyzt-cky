package com.map.utils;

import java.math.BigDecimal;

import com.esri.core.geometry.Point;

public class MathTextLoaction {

	/**
	 * @param args
	 */
	public static Point mathTextLoaction(Point ptCurrent,Point ptPrevious)
	{
		
		Point point=new Point();
		BigDecimal x1 = new BigDecimal(Double.toString(ptCurrent.getX()));
		BigDecimal x2 = new BigDecimal(Double.toString(ptPrevious.getX()));
		BigDecimal y1 = new BigDecimal(Double.toString(ptCurrent.getY()));
		BigDecimal y2 = new BigDecimal(Double.toString(ptPrevious.getY()));
		point.setX(x1.add(x2).doubleValue()/2);
		point.setY(y1.add(y2).doubleValue()/2);	
		return point;		
	}

}
