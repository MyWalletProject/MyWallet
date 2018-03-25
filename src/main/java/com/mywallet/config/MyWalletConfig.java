package com.mywallet.config;

import javax.servlet.Filter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mywallet.security.SimpleCORSFilter;
import com.mywallet.security.VerifyTokenFilter;


@Configuration
public class MyWalletConfig {

	
	public final static Logger logger = Logger.getLogger(MyWalletConfig.class);
	
	public MyWalletConfig(){
		logger.info("MyWalletConfig class Bean is created : ");
	}
	
	@Value("${mywallet.uiurl}")
	String urlPath;
	
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
	

	@Value("${mywallet.user.username}") 
	String username;
	@Value("${mywallet.user.email}") 
	String email;
	@Value("${mywallet.user.password}") 
	String password;
	@Value("${mywallet.user.active}") 
	Boolean active;
	@Value("${mywallet.user.isKYCVerified}") 
	Boolean isKYCVerified;
	@Value("${mywallet.user.role}") 
	String role;
		
	public String getRole() {
		return role;
	}
	
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getIsKYCVerified() {
		return isKYCVerified;
	}
	
	public String getUpLoadProfilePic(){
		return this.upLoadProfilePic;
	}
	
	public String getUrlPath(){
		return urlPath;
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
	
	@Bean
	public Filter tokenfilter(){
		logger.debug("Creating global VerifyTokenFilter");
		return new VerifyTokenFilter();
	}
	
}
