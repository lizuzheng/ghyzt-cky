package com.cky.pointQuery;

import android.os.Handler;

public class MyTextChange  {

	private String value;
	private String where;
	
	public MyTextChange(String value, String where, Handler hd) {
		this.value = value;
		this.where = where;
	
	}

	
	

	private String getCategory(String where) {
		String category="";
		if(where.equals("智能"))
		{
			category="POI,STREET,ROAD";
		}
		else if(where.equals("兴趣点"))
		{
			category="POI";
		}
		else if(where.equals("道路"))
		{
			category="ROAD";
		}
		else if(where.equals("门址"))
		{
			category="STREET";
		}
		
		
		return category;
	}

}
