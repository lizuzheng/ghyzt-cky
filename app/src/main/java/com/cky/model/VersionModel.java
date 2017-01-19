package com.cky.model;

/**
 * 版本更新时返回的对象，里面记录了当前最新版本的相关信息
 * @author Administrator
 *
 */
public class VersionModel {
	
	String VersionId;
	String VersionNum;
	String VersionCode;
	String VersionTime;
	String VersionContent;
	public String getVersionId() {
		return VersionId;
	}
	public void setVersionId(String versionId) {
		VersionId = versionId;
	}
	public String getVersionNum() {
		return VersionNum;
	}
	public void setVersionNum(String versionNum) {
		VersionNum = versionNum;
	}
	public String getVersionCode() {
		return VersionCode;
	}
	public void setVersionCode(String versionCode) {
		VersionCode = versionCode;
	}
	public String getVersionTime() {
		return VersionTime;
	}
	public void setVersionTime(String versionTime) {
		VersionTime = versionTime;
	}
	public String getVersionContent() {
		return VersionContent;
	}
	public void setVersionContent(String versionContent) {
		VersionContent = versionContent;
	}
	
	

}
