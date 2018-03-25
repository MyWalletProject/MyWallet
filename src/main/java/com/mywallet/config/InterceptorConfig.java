package com.mywallet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mywallet.security.RoleInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

	
	@Autowired
	private RoleInterceptor roleInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(roleInterceptor);
	}
	
}
