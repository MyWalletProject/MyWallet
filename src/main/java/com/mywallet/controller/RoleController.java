package com.mywallet.controller;


import java.util.ArrayList;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mywallet.annotaion.ApiAction;
import com.mywallet.domain.Action;
import com.mywallet.domain.Role;
import com.mywallet.domain.req.Req_AssignActionToUser;
import com.mywallet.domain.req.Req_RoleUpdate;
import com.mywallet.services.ActionService;
import com.mywallet.services.RoleService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ActionService actionService;
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class); 
	
	public RoleController() {
		logger.info("RoleController class bean created :");
	}
	
	
	@RequestMapping(value="/assignAction",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> assignActionToRole(@Valid @RequestBody Req_AssignActionToUser reqAssignActionToUser,BindingResult result){
		logger.info("inside assignActionToRole api :");
		if(result.hasErrors()){
			System.out.println("binding result : "+ result.getFieldError().getDefaultMessage());
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		Integer roleId =reqAssignActionToUser.getRoleId();
		Integer[] actionIdArray = reqAssignActionToUser.getActivityIdArray();
		
		Role roleObj = roleService.findByRoleId(roleId);
		logger.info("error occured in ROLE OBJECT : "+roleObj);
		
		if(roleObj == null){
			return ResponseUtil.errorResp("No roleobject  is found by this roleId : ",HttpStatus.NOT_FOUND);
		}
		
		
		ArrayList<Action> actionArray = new ArrayList<Action>(); 
		for(Integer actionId : actionIdArray){
			Action actionObj = actionService.findByActionId(Long.parseLong(actionId+""));
			if(actionObj == null){
				return ResponseUtil.errorResp("No action is found : "+actionId,HttpStatus.NOT_FOUND);
			}
			actionArray.add(actionObj);
		}
		roleObj.setActionArray(actionArray);
		
		roleObj = roleService.save(roleObj);
		if(roleObj==null){
			return ResponseUtil.errorResp("No role object  is saved in database", HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> reMap = ObjectMap.objectMap(roleObj);
		reMap.put("actionArray :", ObjectMap.objectMap(roleObj.getActionArray()));
		
		return ResponseUtil.successResponse("successfully assign Action To Role :", reMap, HttpStatus.OK);
	}
	
	
	@ApiAction
	@PostMapping(path="/role", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createRoles(@Valid @RequestBody Role roleDta,BindingResult bindingResult ){
		logger.info("inside create role  api :");
		
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		Role roleObj = roleService.save(roleDta);
		if(roleObj == null){
			return ResponseUtil.errorResp("no role is added",HttpStatus.NOT_FOUND);
		}
		return ResponseUtil.successResponse("added all role successfully", roleObj,HttpStatus.CREATED);
	}
	
	
	@ApiAction
	@RequestMapping(value="/roles/{roleName}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByRoleName(@PathVariable String roleName){

		if(roleName == null || roleName.length() == 0){
			return  ResponseUtil.errorResp("role name Not Null or Empty",HttpStatus.BAD_REQUEST);
		}

		logger.info("RolenName send by UI : "+roleName);
		
		Role role = roleService.findByRoleName(roleName);
		
		if(role == null){
			
			return ResponseUtil.errorResp("Role Not Exist",HttpStatus.NOT_FOUND);
		}

		return ResponseUtil.successResponse("Role Fetched Successfully", role,HttpStatus.OK);
	}
	
	@RequestMapping(value="/roles",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllRoles(){

		
		List<Role> rolesObj = roleService.getAllRolefromDB();
		
		if(rolesObj == null){
			return ResponseUtil.errorResp("No Roles object is found from database : ", HttpStatus.NOT_FOUND);
		}
		
		ArrayList<Role> roleArray = new ArrayList<Role>();
		
		for(Role roleObj : rolesObj){
			
			 roleArray.add(roleObj);
		}
		
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("roleArray",ObjectMap.objectMap(roleArray));
      
		return ResponseUtil.successResponse("Successfully all roles are get : ", map, HttpStatus.OK);
	}

	
	

	@ApiAction
	@RequestMapping(value="/role/{roleId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> roleUpdateById(@PathVariable ("roleId") Integer roleId, @Valid @RequestBody Req_RoleUpdate role,BindingResult bindingResult){
		logger.info("inside roleUpdateById  api :");
		
		Role roleObj = roleService.findByRoleId(roleId);	
		if(roleObj == null){
			return ResponseUtil.errorResp("No Roles object is found from database : ", HttpStatus.NOT_FOUND);
		}
		
        
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		logger.info(" get role name :");
		
		String roleName = role.getRoleName();
		if(roleName==null || roleName.equals("") ){
			return ResponseUtil.errorResp("Role name can not be null or empty : ", HttpStatus.NOT_FOUND);
		}
		
		logger.info(" get role description :");
		String roleDescription = role.getRoleDescription();
		if(roleDescription==null){
			return ResponseUtil.errorResp("Role Description can not be null : ", HttpStatus.NOT_FOUND);
		}
	
		roleObj.setRoleName(roleName);
		roleObj.setRoleDescription(roleDescription); 
		roleObj.setIsActive(role.getIsActive());
		roleService.save(roleObj);

		Map<String, Object> map=ObjectMap.objectMap(roleObj);

		return ResponseUtil.successResponse("Successfully updated role : ", map, HttpStatus.OK);
	}
	
	
	
}
