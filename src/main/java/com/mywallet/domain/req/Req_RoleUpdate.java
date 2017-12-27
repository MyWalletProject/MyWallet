package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class Req_RoleUpdate {

	@NotNull(message="role Name cannot be null")
	@NotEmpty(message="role Name cannot be empty")
    public String  roleName;
	
	@NotNull(message="roleDescription can not be null")
	@NotEmpty(message="roleDescription can not be Empty ")
	public String  roleDescription;
	
	public Boolean isActive;
	
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
	
}
