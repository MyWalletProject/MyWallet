package com.mywallet.domain.req;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.mywallet.domain.Address;

public class Req_ProfileUpdate {

	@Pattern(regexp="[a-zA-Z]+",message="UserName only alphabets")
	@Size(min=3,message="userName must be atleast 3 characters !")
	@NotNull(message="userName cannot be null")
	@NotEmpty(message="userName can not be empty")
	public String userName;
	
	@Pattern(regexp="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$",message="email is not valid format")
	@NotEmpty(message="email can not be empty")
	@NotNull(message="email cannot be null")
	@Email(message="Invalid email address! ")
	public String email;
	
	public boolean isEmailVerified=false;
	
	public List<Address> addressArray = new ArrayList<Address>();

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

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public List<Address> getAddressArray() {
		return addressArray;
	}

	public void setAddressArray(List<Address> addressArray) {
		this.addressArray = addressArray;
	}

}
