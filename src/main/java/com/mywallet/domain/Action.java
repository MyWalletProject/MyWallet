package com.mywallet.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "action")
public class Action {
  
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="action_id")
	private Long actionId;
	
	@Pattern(regexp="[a-zA-Z]+",message="Action Id Name only alphabets")
	@Size(min=3,message="Action Id Name must be atleast 3 characters !")
	@NotNull(message="Action Id Name cannot be null")
	@Column(name = "action_name")
	private String actionName;
	
	
	private String actionDesc;
	private boolean active;
	private String handlerMethodName; //Its a method name on which @Action annotation exist...
	
	@ManyToMany(mappedBy="actionArray")
	private List<Role> roleArray = new ArrayList<Role>();
	
	public Action(){
		
	}
	
	public Action( String actionName, String actionDesc, boolean active, String handlerMethodName) {
		
		this.actionName = actionName;
		this.actionDesc = actionDesc;
		this.active = active;
		this.handlerMethodName = handlerMethodName;
	}

	public Action(String actionIdName, String actionDesc, boolean active, String handlerMethodName,List<Role> roleArray) {
		
		this.actionName = actionIdName;
		this.actionDesc = actionDesc;
		this.active = active;
		this.handlerMethodName = handlerMethodName;
		this.roleArray = roleArray;
	}

	public Long getActionId() {
		return actionId;
	}

	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionIdName) {
		this.actionName = actionIdName;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getHandlerMethodName() {
		return handlerMethodName;
	}

	public void setHandlerMethodName(String handlerMethodName) {
		this.handlerMethodName = handlerMethodName;
	}

	public List<Role> getRoleArray() {
		return roleArray;
	}

	public void setRoleArray(List<Role> roleArray) {
		this.roleArray = roleArray;
	}

//	@Override
//	public String toString() {
//		return "Action [actionId=" + actionId + ", actionIdName=" + actionName + ", actionIdDesc=" + actionDesc
//				+ ", active=" + active + ", handlerMethodName=" + handlerMethodName + ", roleArray=" + roleArray + "]";
//	}
	
	
}
