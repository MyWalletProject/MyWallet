package com.mywallet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Action {
  
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long actionId;
	
	@Pattern(regexp="[a-zA-Z]+",message="Action Id Name only alphabets")
	@Size(min=3,message="Action Id Name must be atleast 3 characters !")
	@NotNull(message="Action Id Name cannot be null")
	@Column(name = "action_id_name")
	private String actionIdName;
	
	
	private String actionIdDesc;
	private boolean active;
	private String handlerMethodName; //Its a method name on which @Action annotation exist...
	
	
	
	
}
