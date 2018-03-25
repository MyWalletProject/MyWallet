package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_WalletData {

	public Integer walletId;
	
	@NotNull(message="address cannot be null")
	@NotEmpty(message="address can not be empty")
	public String address;
	
	@NotNull(message="walletBalance cannot be null")
	@NotEmpty(message="walletBalance can not be empty")
  //"T(-{0,1}(?!0)\\d+)"
	@Pattern(regexp="[1-9][0-9]*",message="no. should not be negative or zero")
	public Double walletBalance;
	
	
	
	public Integer getWalletId() {
		return walletId;
	}


	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Double getWalletBalance() {
		return walletBalance;
	}


	public void setWalletBalance(Double walletBalance) {
		this.walletBalance = walletBalance;
	}
	
	


	
}
