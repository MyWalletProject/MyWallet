package com.mywallet.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;
	
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String street;
	
	private String addressLine;
	
	private String pincode;
	
	private String contactNo;
	
	
	@ManyToOne
	private User user;
	
	public Address(){
		
	}
	
    public Address(String country,User user){
    	this.country = country;
    	this.user = user;
	}

	public Address(String country, String state, String city, String street, String addressLine,String pincode, String contactNo){
		this.country = country;
		this.state = state;
		this.city = city;
		this.street = street;
		this.addressLine = addressLine;
		this.pincode = pincode;
		this.contactNo = contactNo;	
	}
	
	public Address(String country, String state, String city, String street, String addressLine,String pincode, String contactNo, User user) {
			
		this.country = country;
		this.state = state;
		this.city = city;
		this.street = street;
		this.addressLine = addressLine;
		this.pincode = pincode;
		this.contactNo = contactNo;
		this.user = user;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
