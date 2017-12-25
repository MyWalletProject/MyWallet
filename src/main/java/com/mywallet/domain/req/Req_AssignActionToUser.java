package com.mywallet.domain.req;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;
public class Req_AssignActionToUser {

//	@Pattern(regexp="[0-9]+",message="roleId should be an Integer")
	@NotNull(message="RoleId can not be null")
	public Integer RoleId;
	
	@NotNull(message="ActionIdArray can not be null")
    public Integer[] activityIdArray;;

	public Integer getRoleId() {
		return RoleId;
	}

	public void setRoleId(Integer roleId) {
		RoleId = roleId;
	}

	public Integer[] getActivityIdArray() {
		return activityIdArray;
	}

	public void setActivityIdArray(Integer[] activityIdArray) {
		this.activityIdArray = activityIdArray;
	}

}
