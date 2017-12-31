package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_AddressData {

	
	@NotNull(message="country cannot be null")
	@NotEmpty(message="country can not be empty")
	public String country;
	
	@NotNull(message="state cannot be null")
	@NotEmpty(message="state can not be empty")
	public String state;
	
	@NotNull(message="city cannot be null")
	@NotEmpty(message="city can not be empty")
	public String city;
	
	public String street;
	
	@NotNull(message="addressLine cannot be null")
	@NotEmpty(message="addressLine can not be empty")
	public String addressLine;
	
	@NotNull(message="pincode cannot be null")
	@NotEmpty(message="pincode can not be empty")
	public String pincode;
	
	@Pattern(regexp="^(0|[1-9][0-9]*)$",message="contactNo is not valid format")
	@Size(min=9,message="contactNo must be atleast 9 characters !")
	@NotNull(message="contactNo cannot be null")
	@NotEmpty(message="contactNo can not be empty")
	public String contactNo;

	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	
}
