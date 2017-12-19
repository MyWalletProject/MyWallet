package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class Req_UserLogin {

	@Pattern(regexp="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$",message="incorrect email format")
	@NotNull(message="email can not be null")
	@NotEmpty(message="email can not be Empty ")
	private String email;
	
	@NotNull(message="password can not be null")
	@NotEmpty(message="password can not be Empty ")
	private String password;

	
	public Req_UserLogin(){
		
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
