package com.mywallet.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Token {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer tokenId; 
	
	@Column(name="token")
	@NotNull(message="metoken can not be null")
	private String token;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	private Date createdAt;
	
	public Token(){
		
	}
	
	public Token(String token, User user,Date createdAt) {
		this.token = token;
		this.user = user;
		this.createdAt=createdAt;
	}

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String generateNewToken() {
		return UUID.randomUUID().toString();
	}
	
}
