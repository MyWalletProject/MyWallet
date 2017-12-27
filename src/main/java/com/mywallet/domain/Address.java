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
	
	@ManyToOne
	private User user;
	
	public Address(){
		
	}
	
    public Address(String country,User user){
		this.country = country;
		this.user=user;
	}
    

    public Address(String country){
		this.country = country;
	}
	
	public Address(Integer addressId, String country, String state, String city, String street, String addressLine) {
		
		this.addressId = addressId;
		this.country = country;
		this.state = state;
		this.city = city;
		this.street = street;
		this.addressLine = addressLine;
	}
	
   public Address(Integer addressId, String country, String state, String city, String street, String addressLine,User user) {
		
		this.addressId = addressId;
		this.country = country;
		this.state = state;
		this.city = city;
		this.street = street;
		this.addressLine = addressLine;
		this.user=user;
	}

	public User getUser() {
	return user;
	}
	
	public void setUser(User user) {
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

//	@Override
//	public String toString() {
//		return "Address [addressId=" + addressId + ", country=" + country + ", state=" + state + ", city=" + city
//				+ ", street=" + street + ", addressLine=" + addressLine + ", user=" + user + "]";
//	}

	

}
