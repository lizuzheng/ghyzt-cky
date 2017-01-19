package com.map.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoName {
	
	
	public  String photoName;
	private  int i=0;
	private static PhotoName single=null;  
	public  String getPhotoName()
	{
		i++;
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		photoName=dateFormat.format(date)+i + ".jpg";			
		return photoName;
	}
	

	    //静态工厂方法   
	    public static PhotoName getInstance() {  
	         if (single == null) {    
	            single = new PhotoName();  
	         }    
	        return single;  
	    }  


}
