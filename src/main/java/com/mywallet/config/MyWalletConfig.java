package com.mywallet.config;

import javax.servlet.Filter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mywallet.security.SimpleCORSFilter;


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
	
	@Value("${UPLOADED_URL_FOLDER}") 
	String url; 
	
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
	
	public String getURL(){
		return this.url;
	}
	
	public String[] getControllerPackages(){
		return (controllerPackages!=null) ? controllerPackages : new String[0];
	}
	/**
	 * This Bean is used to register Global CORS filter 
	 * in Spring Context. Order of this filter is highest
	 * in kyc.
	 */
	@Bean
	public Filter corsfilter(){
		logger.debug("Creating global SimpleCORSFilter");
		return new SimpleCORSFilter();
	}
	
}
