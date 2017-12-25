package com.mywallet.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyWalletConfig {

	
	public final static Logger logger = Logger.getLogger(MyWalletConfig.class);
	
	@Value("${mywallet.controllers}")
	String[] controllerPackages;
	
	@Value("${mywallet.roleName}")
	String[] roleName; 
	
	@Value("${mywallet.roleDescription}")
	String[] roleDescription;
	
	@Value("${UPLOADED_FOLDER}") 
	String upLoadProfilePic; 

	public MyWalletConfig(){
		logger.info("MyWalletConfig class Bean is created : ");
	}
	
	public String getUpLoadProfilePic(){
		return this.upLoadProfilePic;
	}
	
	public String[] getRoleName(){
		return this.roleName;
	}
	
	public String[] getRoleDescription(){
		return this.roleDescription;
	}
	
	public String[] getControllerPackages(){
		return (controllerPackages!=null) ? controllerPackages : new String[0];
	}
	
}
