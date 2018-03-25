package com.mywallet.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table
public class Wallet {
	    /*
		 * wallet_id is the primary key 
		 */	
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="wallet_id")
		private Integer walletId;
		
	// uuid ko encrypt kr dena
		@Column(name="wallet_address")
		private String address;
		
	// staus will be ACTIVE or INACTIVE
		private String walletStatus = null;

		@OneToOne(cascade=CascadeType.ALL)
		@JoinColumn(name="user_id")  
		private User user;
		
	  // when wallet created then send email to user and chage value of emailSended == true	
		private boolean emailSended = false;

		private Double walletBalance;

		public Wallet(){
			
		}
		
        public Wallet(String address,User user){
        	this.address = address;
        	this.user = user;
		}
		
		public Wallet(String address, String walletStatus, User user, boolean emailSended,Double walletBalance) {
			
			this.address = address;
			this.walletStatus = walletStatus;
			this.user = user;
			this.emailSended = emailSended;
			this.walletBalance = walletBalance;
		}

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

		public String getWalletStatus() {
			return walletStatus;
		}

		public void setWalletStatus(String walletStatus) {
			this.walletStatus = walletStatus;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public boolean isEmailSended() {
			return emailSended;
		}

		public void setEmailSended(boolean emailSended) {
			this.emailSended = emailSended;
		}

		public Double getWalletBalance() {
			return walletBalance;
		}

		public void setWalletBalance(Double walletBalance) {
			this.walletBalance = walletBalance;
		}
		
}
