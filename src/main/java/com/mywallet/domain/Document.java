package com.mywallet.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.mywallet.util.ObjectHash;

@Entity
public class Document {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer documentId;
	
	private Date uploadedAt;
	
	private Boolean isVerfied = false;
	
	@NotNull(message="url cannot be null")
	@NotEmpty(message="url can not be empty")
	private String url;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	private String status;
	

	public Document(){
		
	}
	
	public Document(Date uploadedAt,  String url, User user) {
		this.uploadedAt = uploadedAt;
		this.url = url;
		this.user=user;
	}
	
	public Document(Date uploadedAt, Boolean isVerfied,String status, String url,User user) {
		this.uploadedAt = uploadedAt;
		this.isVerfied = isVerfied;
		this.status=status;
		this.url = url;
		this.user=user;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public Boolean getIsVerfied() {
		return isVerfied;
	}

	public void setIsVerfied(Boolean isVerfied) {
		this.isVerfied = isVerfied;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ObjectHash
	public String uploadedAt(){
		return this.uploadedAt.toString();
	}
	
}
