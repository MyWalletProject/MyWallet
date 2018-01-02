package com.mywallet.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Country {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer countryID;

	@NotNull(message="countryName can not be null")
	@NotEmpty(message="countryName can not be empty")
	@Column(unique=true)
	private String countryName;

	private String countryCode;
	
	private String countryDialCode;
	
	
	@OneToMany(mappedBy="country",cascade=CascadeType.ALL)
	private List<CountryDocMapping> countryDocMappingArray = new ArrayList<CountryDocMapping>();
	
	public Country(){
		
	}

	public Country( String countryName, String countryCode, String countryDialCode) {
		
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.countryDialCode = countryDialCode;
	}
	
	

	public Country(String countryName, String countryCode, String countryDialCode, List<CountryDocMapping> countryDocMappingArray) {
		
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.countryDialCode = countryDialCode;
		this.countryDocMappingArray = countryDocMappingArray;
	}
	

	public List<CountryDocMapping> getCountryDocMappingArray() {
		return countryDocMappingArray;
	}

	public void setCountryDocMappingArray(List<CountryDocMapping> countryDocMappingArray) {
		this.countryDocMappingArray = countryDocMappingArray;
	}

	public Integer getCountryID() {
		return countryID;
	}

	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryDialCode() {
		return countryDialCode;
	}

	public void setCountryDialCode(String countryDialCode) {
		this.countryDialCode = countryDialCode;
	}

	@Override
	public String toString() {
		return "Country [countryID=" + countryID + ", countryName=" + countryName + ", countryCode=" + countryCode
				+ ", countryDialCode=" + countryDialCode + "]";
	}
	
	
}
