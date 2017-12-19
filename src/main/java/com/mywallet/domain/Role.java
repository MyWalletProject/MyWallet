package com.mywallet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role",uniqueConstraints = @UniqueConstraint(
		columnNames = { "roleName"}))
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	
	@Pattern(regexp="[a-zA-Z]+",message="Role Name only alphabets")
	@Size(min=3,message="role Name must be atleast 3 characters !")
	@NotNull(message="role Name cannot be null")
	@Column(unique = true)
	private String  roleName;
	
	private String  roleDescription;
	
	private Boolean isActive;
	
	
	public Role(){
		
	}

	public Role(Integer roleId, String roleName, String roleDescription, Boolean isActive) {
		
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDescription = roleDescription;
		this.isActive = isActive;
	}


	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", roleDescription=" + roleDescription
				+ ", isActive=" + isActive + "]";
	}
	
	
}
