package com.cky.model;

/**
 * 用地性质数据模型
 * @author lzz
 *
 */
public class LandUseModel {
	
	public LandUseModel(String code,String parentCode,String value)
	{
		this.code=code;
		this.parentCode=parentCode;
		this.value=value;
	}

	String code;
	String parentCode;
	String value;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
