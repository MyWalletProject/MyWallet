package com.mywallet.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class EmailToken {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer emailTokenId; 
	
	@Column(name="token")
	@NotNull(message="metoken can not be null")
	private String emailToken;
	
	
	private Boolean isExpired = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	private Date createdAt;
	
//    private Date expiryDate;
	
	public EmailToken(){
		
	}

	public EmailToken(String emailToken, User user, Date createdAt,Boolean isExpired) {
		
		this.emailToken = emailToken;
		this.user = user;
		this.createdAt = createdAt;
		this.isExpired=isExpired;
	}
	
	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Integer getEmailTokenId() {
		return emailTokenId;
	}

	public void setEmailTokenId(Integer emailTokenId) {
		this.emailTokenId = emailTokenId;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
//	 private Date calculateExpiryDate(int expiryTimeInMinutes) {
//	        Calendar cal = Calendar.getInstance();
//	        cal.setTime(new Timestamp(cal.getTime().getTime()));
//	        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//	        return new Date(cal.getTime().getTime());
//	    }
}
