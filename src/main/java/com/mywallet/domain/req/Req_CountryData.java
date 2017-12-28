package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_CountryData {

	@NotNull(message="countryName can not be null")
	@NotEmpty(message="countryName can not be empty")
	public String countryName;

	public String countryCode;

	public String countryDialCode;

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
	
}
