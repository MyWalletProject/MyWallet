package com.mywallet.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@Pattern(regexp="[a-zA-Z]+",message="UserName only alphabets")
	@Size(min=3,message="userName must be atleast 3 characters !")
	@NotNull(message="userName cannot be null")
	@NotEmpty(message="userName can not be empty")
	@Column(name = "user_name")
	private String userName;
	
	@Pattern(regexp="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$",message="email is not valid format")
	@NotEmpty(message="email can not be empty")
	@NotNull(message="email cannot be null")
	@Email(message="Invalid email address! ")
	@Column(name = "email",unique = true)
	private String email;
	
	@Size(min=8,message="password must be atleast 8 characters !")
	private String password;
	
	private Integer defaultAddressId; 
	
	private boolean active=false;
	
	private String upLoadProfilePic; // path = "D:/mywallet/profilePicUpload/";
	
	private boolean isEmailVerified=false;
	
	private Boolean isKYCVerified=false;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private List<Address> addressArray = new ArrayList<Address>();
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="user")
	private List<LoginHistory> loginHistoryArray = new ArrayList<LoginHistory>();
	
	public User(){
		
	}
	
	public User(String upLoadProfilePic){
		this.upLoadProfilePic=upLoadProfilePic;
	}
	
    public User(String userName,String email,String password,boolean isActive,boolean isEmailVerified,Boolean isKYCVerified){
		this.userName=userName;
		this.email=email;
		this.password=password;
		this.active=isActive;
		this.isEmailVerified=isEmailVerified;
		this.isKYCVerified=isKYCVerified;
	}
    
    public User(String userName,String email,String password,boolean isActive,boolean isEmailVerified,Role role,List<Address> addressArray,List<LoginHistory> loginHistoryArray,Boolean isKYCVerified,Integer defaultAddressId){
		
		this.userName=userName;
		this.email=email;
		this.password=password;
		this.active=isActive;
		this.isEmailVerified=isEmailVerified;
		this.role=role;
		this.addressArray=addressArray;
		this.loginHistoryArray=loginHistoryArray;
		this.isKYCVerified=isKYCVerified;
		this.defaultAddressId=defaultAddressId;

	}
	

	public Integer getDefaultAddressId() {
		return defaultAddressId;
	}

	public void setDefaultAddressId(Integer defaultAddressId) {
		this.defaultAddressId = defaultAddressId;
	}

	public Boolean getIsKYCVerified() {
		return isKYCVerified;
	}

	public void setIsKYCVerified(Boolean isKYCVerified) {
		this.isKYCVerified = isKYCVerified;
	}
    
    public String getUpLoadProfilePic() {
		return upLoadProfilePic;
	}

	public void setUpLoadProfilePic(String upLoadProfilePic) {
		this.upLoadProfilePic = upLoadProfilePic;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<LoginHistory> getLoginHistoryArray() {
		return loginHistoryArray;
	}

	public void setLoginHistoryArray(List<LoginHistory> loginHistoryArray) {
		this.loginHistoryArray = loginHistoryArray;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Address> getAddressArray() {
		return addressArray;
	}

	public void setAddressArray(List<Address> addressArray) {
		this.addressArray = addressArray;
	}
	
	
	public String getlastLogin(){
		LoginHistory loginHistory;
		if (this.loginHistoryArray.size() == 0 )
			loginHistory = null;
		else
			loginHistory = loginHistoryArray.get(loginHistoryArray.size()-1);
		return  loginHistory == null ? null : loginHistory.getLoginTime().toString();
		
	}

//	@Override
//	public String toString() {
//		return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
//				+ ", active=" + active + ", isEmailVerified=" + isEmailVerified + ", role=" + role + ", addressArray="
//				+ addressArray + ", loginHistoryArray=" + loginHistoryArray + "]";
//	}

	
}
