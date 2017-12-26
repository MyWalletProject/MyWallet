package com.mywallet.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LoginHistory implements Comparable<LoginHistory>{

	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Integer loginHistoryId;
	    
		private String loginIP;
		private String userAgent;
		private Date loginTime;
		
		@ManyToOne
		@JoinColumn(name = "user_id", nullable = true)
	    private User user;
	    
	    public LoginHistory(){
	    	
	    }
	    
	    public LoginHistory(Integer loginHistoryId,String loginIP,String userAgent,Date loginTime){
	    	this.loginHistoryId = loginHistoryId;
			this.loginIP = loginIP;
			this.userAgent = userAgent;
			this.loginTime = loginTime;
	    }
	    
	    public LoginHistory(String loginIP,String userAgent,Date loginTime){
			this.loginIP = loginIP;
			this.userAgent = userAgent;
			this.loginTime = loginTime;
	    }
	    
		public LoginHistory(Integer loginHistoryId, String loginIP, String userAgent, Date loginTime, User user) {
			
			this.loginHistoryId = loginHistoryId;
			this.loginIP = loginIP;
			this.userAgent = userAgent;
			this.loginTime = loginTime;
			this.user = user;
		}

		public Integer getLoginHistoryId() {
			return loginHistoryId;
		}
		public void setLoginHistoryId(Integer loginHistoryId) {
			this.loginHistoryId = loginHistoryId;
		}
		public String getLoginIP() {
			return loginIP;
		}
		public void setLoginIP(String loginIP) {
			this.loginIP = loginIP;
		}
		public String getUserAgent() {
			return userAgent;
		}
		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}
		public Date getLoginTime() {
			return loginTime;
		}
		public void setLoginTime(Date loginTime) {
			this.loginTime = loginTime;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		
		@Override
		  public int compareTo(LoginHistory loginHistory) {
		    if (getLoginTime() == null || loginHistory.getLoginTime() == null)
		      return 0;
		    return getLoginTime().compareTo(loginHistory.getLoginTime());
		  }
		
		
		@Override
		public String toString() {
			return "LoginHistory [loginHistoryId=" + loginHistoryId + ", loginIP=" + loginIP + ", userAgent="
					+ userAgent + ", loginTime=" + loginTime + ", user=" + user + "]";
		}
	    
	    
	    
}
