package com.mywallet.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mywallet.domain.Role;
import com.mywallet.services.RoleService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@Controller
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class.getName());
	
	@PostMapping(path="/role", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createRoles(@Valid @RequestBody Role roleDta,BindingResult bindingResult ){
		logger.info("inside create role  api :");
		Map<String, Object> map = new HashMap<String, Object>();
		if(bindingResult.hasErrors()){
			map.put("Error Message " , bindingResult.getFieldError().getDefaultMessage());
			return new ResponseEntity<Object>(map,HttpStatus.BAD_REQUEST);
		}
		Role roleObj = roleService.save(roleDta);
		if(roleObj == null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("no role is added"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(ResponseUtil.successResponse("added all role successfully", roleObj),HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/roles/{roleName}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByRoleName(@PathVariable String roleName){

		if(roleName == null || roleName.length() == 0){
			return new ResponseEntity<Object>("role name Not Null or Empty",HttpStatus.BAD_REQUEST);
		}

		logger.info("RolenName send by UI : "+roleName);
		
		Role role = roleService.findByRoleName(roleName);
		
		if(role == null){
			
			return new ResponseEntity<>(ResponseUtil.errorResponse("Role Not Exist"),HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Object>(ResponseUtil.successResponse("Role Fetched Successfully", role),HttpStatus.OK);
	}
	

	@RequestMapping(value="/roleDelete/{roleId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> rolesDeleteByRoleId(@PathVariable Integer roleId){
		
		Role role = roleService.findByRoleId(roleId);
		logger.info("Fetching Roloe with id {}", role);
		
         if(role == null){
			
			return new ResponseEntity<>(ResponseUtil.errorResponse("No role is found"),HttpStatus.NOT_FOUND);
		}
		
         boolean deleteRole = false;
         deleteRole = roleService.deleteRoleByRoleId(roleId);
		
         if(!deleteRole){
 			
 			return new ResponseEntity<>(ResponseUtil.errorResponse("No role is deleted"),HttpStatus.NOT_FOUND);
 		}

		return new ResponseEntity<Object>(ResponseUtil.successResponse("Role deleted Successfully", role),HttpStatus.NO_CONTENT);

	}
	
	@RequestMapping(value="/roles",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllRoles(){

		List<Role> roles = roleService.getAllRolefromDB();
		Map<String, Object> map = new HashMap<String, Object>();
		for(Role role : roles){
			 map =ObjectMap.objectMap(role);
		}

		return new ResponseEntity<Object>(map,HttpStatus.OK);
	}
	

}
