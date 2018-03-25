package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_signupData {

	
	@NotNull(message="userName can not be null")
	@NotEmpty(message="userName can not be Empty ")
	public String userName;
	
	@Pattern(regexp="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$",message="incorrect email format")
	@NotNull(message="email can not be null")
	@NotEmpty(message="email can not be Empty ")
	public String email;
	
	@NotNull(message="password can not be null")
	@NotEmpty(message="password can not be Empty ")
	public String password;
	
	@NotNull(message="country can not be null")
	@NotEmpty(message="country can not be Empty ")
	public String country;
	
	@NotNull(message="roleName can not be null")
	@NotEmpty(message="roleName can not be Empty ")
	public String roleName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
