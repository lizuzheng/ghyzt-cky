package com.cky.model;

/**
 * 记录登陆者信息
 * @author Administrator
 *
 */
public class UserModel {
	
//	[UserID]
//		      ,[UserNum]
//		      ,[UserPassword]
//		      ,[UserName]
//		      ,[MachineNumber]
//		      ,[IMEI]
//		      ,[UserPhone]
//		      ,[StateNum]
//		      ,[OrgNum]
//		      ,[IsNotVerification]
//		      ,[BeiZhu]
	
	String UserID;
	String UserNum;
	String UserPassword;
	String UserName;
	String MachineNumber;
	String IMEI;
	String UserPhone;
	String StateNum;
	String OrgNum;
	String IsNotVerification;
	String BeiZhu;
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getUserNum() {
		return UserNum;
	}
	public void setUserNum(String userNum) {
		UserNum = userNum;
	}
	public String getUserPassword() {
		return UserPassword;
	}
	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getMachineNumber() {
		return MachineNumber;
	}
	public void setMachineNumber(String machineNumber) {
		MachineNumber = machineNumber;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getUserPhone() {
		return UserPhone;
	}
	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}
	public String getStateNum() {
		return StateNum;
	}
	public void setStateNum(String stateNum) {
		StateNum = stateNum;
	}
	public String getOrgNum() {
		return OrgNum;
	}
	public void setOrgNum(String orgNum) {
		OrgNum = orgNum;
	}
	public String getIsNotVerification() {
		return IsNotVerification;
	}
	public void setIsNotVerification(String isNotVerification) {
		IsNotVerification = isNotVerification;
	}
	public String getBeiZhu() {
		return BeiZhu;
	}
	public void setBeiZhu(String beiZhu) {
		BeiZhu = beiZhu;
	}
	
	
	

}
