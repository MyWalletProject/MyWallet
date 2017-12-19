package com.mywallet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.domain.Address;
import com.mywallet.domain.User;
import com.mywallet.services.AddressService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;



@RestController
@RequestMapping("/api")
public class UserController{
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());
	
	@Autowired
	private AddressService addressService;
	

	@PostMapping(path="/user",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user,BindingResult bindingResult){
		logger.info("Inside createUser api :");
		Map<String ,Object> map = new HashMap<String ,Object>();
		if(bindingResult.hasErrors()){
			System.out.println("I found errors");
			System.out.println("Error map "+ObjectMap.objectMap(bindingResult.getFieldError()));
			map.put("Error Message " , bindingResult.getFieldError().getDefaultMessage());
			return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
		}
		User	userObj = null;
		try{
			userObj = userService.save(user);
		}
		catch(DataIntegrityViolationException dataIntigrity){

			return new ResponseEntity<Object>(ResponseUtil.errorResponse("user Already registered with email"),HttpStatus.CONFLICT);
		}
		if(userObj == null){

			return new ResponseEntity<Object>(ResponseUtil.errorResponse("user not created"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>(ResponseUtil.successResponse("user created successfully", userObj),HttpStatus.CREATED);
	}

	@RequestMapping(value="/users",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllUsers(){

		List<User> users = userService.getAllUserfromDB();
		List<Map<String,Object>> userArray = new ArrayList<Map<String,Object>>();
		for(User usr : users){
			Map<String,Object> reMap = ObjectMap.objectMap(usr);
			usr.getAddressArray();
			reMap.put("addressArray", ObjectMap.objectMap(usr.getAddressArray()));
			userArray.add(reMap);
		}

		return new ResponseEntity<Object>(userArray,HttpStatus.OK);
	}


	@RequestMapping(value="/userUpdate/{userName}", method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> modifyObjByUserName(@Valid @PathVariable("userName") String userName,@RequestBody User userData,BindingResult bindingResult){

		logger.info("Inside MODIFY ROLE BY USING ROLE NAME :");

		Map<String, Object> map = new HashMap<String, Object>();
		if(bindingResult.hasErrors()){
			logger.info("I found errors");
			map.put("ErrorMessage", ((DefaultMessageSourceResolvable) bindingResult.getFieldErrors()).getDefaultMessage());
			return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserName(userName);
		if(user == null){
			return new ResponseEntity<>("No user is found",HttpStatus.NOT_FOUND);
		}

		user.setUserName(userData.getUserName());
		user.setEmail(userData.getEmail());
		user.setPassword(userData.getPassword());
		user.setActive(userData.isActive());
		user = userService.save(user);

		if(user == null){	
			return new ResponseEntity<>(ResponseUtil.errorResponse("userName Not modified"),HttpStatus.NOT_MODIFIED);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return new ResponseEntity<>(ResponseUtil.successResponse("User Updated Successfully", reMap),HttpStatus.OK);
	}

	@RequestMapping(value="/users/{userName}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByUserName(@PathVariable String userName){

		if(userName == null || userName.length() == 0){
			return new ResponseEntity<Object>("roleNotNullEmpty",HttpStatus.BAD_REQUEST);
		}

		logger.info("User Name send by UI : "+userName);
		User user = userService.findByUserName(userName);

		if(user == null){

			return new ResponseEntity<>(ResponseUtil.errorResponse("User Not Exist"),HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("User Fetched Successfully", reMap),HttpStatus.OK);
	}


	@RequestMapping(value="/users/search",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByUserNameAndActive(@RequestParam String userName,@RequestParam Boolean active){

		if(userName == null || userName.length() == 0){
			return new ResponseEntity<Object>("User Name can Not be Null or Empty",HttpStatus.BAD_REQUEST);
		}
		if(active == null){
			return new ResponseEntity<Object>("Active can Not be Null",HttpStatus.BAD_REQUEST);
		}

		logger.info("User Name send by UI : "+userName);
		logger.info("active send by UI : "+active);

		User user = userService.findByUserNameAndIsActive(userName,active);

		if(user == null){

			return new ResponseEntity<>(ResponseUtil.errorResponse("Role Not Exist"),HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("User Fetched Successfully", reMap),HttpStatus.OK);
	}

	@RequestMapping(value="/userEmail/search",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByEmail(@RequestParam(defaultValue="",required=true) String email){

		if( email == null || email.equals("")){
			return new ResponseEntity<Object>("User email can Not be Null",HttpStatus.BAD_REQUEST);
		}

		logger.info("User email send by UI : "+email);

		User user = userService.findByEmail(email.trim());

		if(user == null){
			return new ResponseEntity<>(ResponseUtil.errorResponse("User Not Found"),HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("User Fetched Email Successfully", reMap),HttpStatus.OK);
	}

	@RequestMapping(value="/userDelete/{id}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> deleteRole(@PathVariable String id){
		logger.info("Fetching & Deleting User with id {}", id);

		Integer userId =null;

		try{
			userId = Integer.parseInt(id);
		}
		catch(Exception e){
			return new ResponseEntity<>("userIdNotInteger",HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserId(userId);
		logger.info("Fetching User with id {}", user);


		if(user == null){
			return new ResponseEntity<>(ResponseUtil.errorResponse("No user is found"),HttpStatus.NOT_FOUND);
		}


		boolean deleteUser = false;
		deleteUser = userService.deleteUserByUserId(userId);

		if(!deleteUser){
			return new ResponseEntity<>(ResponseUtil.errorResponse("No user is id found :"),HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Object>(ResponseUtil.successResponse("Role deleted Successfully",user),HttpStatus.NO_CONTENT);

	}	

	@RequestMapping(value="/assignAddressToUser/{userId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> assignAddressToUser(@PathVariable ("userId") String id,@RequestBody Map<String, Object> requestMap){
		logger.debug("inside assignAddressToUser api request is :"+requestMap);
		Integer userId =null;

		try{
			userId = Integer.parseInt(id);
		}
		catch(Exception e){
			return new ResponseEntity<Object>("userIdNotInteger",HttpStatus.BAD_REQUEST);
		}
		List<Integer> addressIdArray =(List<Integer>) requestMap.get("addressIdArray");
		System.out.println("getting from addressIdArray :");
		
		if(addressIdArray == null){
			return new ResponseEntity<Object>("addressIdArray key not exist in request map ",HttpStatus.BAD_REQUEST);
		}

		logger.info("Address ID Array from request : "+addressIdArray);
		List<Address> addressArray = new ArrayList<Address>();


		User user = userService.findByUserId(userId);
		if(user == null){  
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("No user is found"),HttpStatus.NOT_FOUND);
		}		

		for(Integer addressId : addressIdArray){
			Address address = addressService.findByAddressId(addressId);
			if(address == null){
				return new ResponseEntity<Object>(ResponseUtil.errorResponse("Address Id : "+addressId+" not exist so please send valid Id"),HttpStatus.NOT_FOUND);
			}
			address.setUser(user);
			address = addressService.save(address);
		}

		Map<String,Object> reMap = ObjectMap.objectMap(user);
		
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("Address Successfully assign to user", reMap),HttpStatus.OK);
	}


	
}
