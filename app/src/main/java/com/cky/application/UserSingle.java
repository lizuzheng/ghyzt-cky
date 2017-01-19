package com.cky.application;

import com.cky.model.UserModel;

public class UserSingle {

	//全局保存登陆者的信息
    public UserModel userModel;	
    private static UserSingle userSingle;
    
    public static UserSingle getSingle()
    {
    	if(userSingle==null)
    	{
    		userSingle=new UserSingle();
    	}
		return userSingle;
    	
    }
	
}
