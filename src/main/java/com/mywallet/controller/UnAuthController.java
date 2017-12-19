package com.mywallet.controller;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
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
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.domain.Address;
import com.mywallet.domain.Role;

import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_UserLogin;
import com.mywallet.services.AddressService;
import com.mywallet.services.MailService;
import com.mywallet.services.RoleService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class UnAuthController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private RoleService roleService;

	public UnAuthController(){}

	@PostConstruct
	public void init(){
	}

	@RequestMapping(value="/user/search",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> login(@RequestBody  @Valid Req_UserLogin reqUserLogin,BindingResult result)  {
		logger.info("Inside signup User api :"+reqUserLogin);

		if(result.hasErrors()){
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
	/*	
		//String email = (String) loginRequest.get("email");

		if(email==null && email.equals("")){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key not null or exist email in request map "),HttpStatus.BAD_REQUEST);
		}

		String password = (String) loginRequest.get("password");

		if(password==null && password.equals("")){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key not exist password in request map "),HttpStatus.BAD_REQUEST);
		}*/

		User user=userService.findByEmailAndPassword(reqUserLogin.getEmail(), reqUserLogin.getPassword());

		if(user==null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("User not exist, invalid login credenstial with password or email incorrect"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>("User successfully login.",HttpStatus.OK);
	}


	@PostMapping(path="/userDB",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> signupUser( @RequestBody Map<String,Object> signupRequestMap){

		logger.info("Inside signup User api :"+signupRequestMap);

		String userName= (String) signupRequestMap.get("userName");

		if(userName == null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key not exist user name in request map "),HttpStatus.BAD_REQUEST);
		}

		String email=(String) signupRequestMap.get("email");
		String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
		regex.matches("email");

		String password=(String) signupRequestMap.get("password");

		if(password == null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key not exist password in request map "),HttpStatus.BAD_REQUEST);
		}

		String roleName=(String) signupRequestMap.get("roleName");

		logger.info("getting roleName User ---- :"+roleName);

		if(roleName == null || roleName.equals("")){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key not exist role name in request map "),HttpStatus.BAD_REQUEST);
		}

		Role roleObj = roleService.findByRoleName(roleName);

		logger.info("getting role Object ;;;;;;..... :"+roleObj);

		if(roleObj == null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("No role is found by this key roleName:"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User userObj = userService.findByEmail(email);
		//		userService.findByRole(roleObj);

		logger.info(" user Object is null ---"+userObj);


		if (userObj != null) {
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("Oops!  There is already a user registered with the email provided. "),HttpStatus.NOT_FOUND);
		}

		logger.info("getting user Object"+userObj);

		User newUser = new User(userName,email,password,true,false);
		newUser.setRole(roleObj);
		newUser = userService.save(newUser);

		logger.info("getting user new Object"+newUser);

		String country=(String) signupRequestMap.get("country");

		logger.info("getting country in address"+country);

		Address addressObj = new Address(country,newUser);
		logger.info("address Object ::::::"+addressObj);

		addressObj = addressService.save(addressObj);

		mailService.sendMail(email, "Successfully registered", "you are successfully registed in mywallet");
	
		newUser.getAddressArray().add(addressObj);

		System.out.println("length is :"+newUser.getAddressArray().size());

		if(addressObj == null) {
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("address object not saved"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		mailService.userRegisterMail(newUser);
		Map<String,Object> reMap = ObjectMap.objectMap(newUser);
		reMap.put("addressArray", ObjectMap.objectMap(newUser.getAddressArray()));
		reMap.put("role",ObjectMap.objectMap(newUser.getRole()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("User succesfully registed in mywallet",reMap),HttpStatus.OK);
	}


	@RequestMapping(value="/updatePassword/{userId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> passwordUpdateByUser(@PathVariable ("userId") Integer id,@RequestBody Map<String, String> requestMap){
		logger.info("inside passwordResetByUser api : "+requestMap);


		String oldPassword =  requestMap.get("oldPassword");
		if(oldPassword==null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key old Password not exist in request map "),HttpStatus.BAD_REQUEST);
		}
		logger.info("getting oldpassword key from requestMap"+oldPassword);


		String newPassword = requestMap.get("newPassword");
		logger.info("getting newpassword key from requestMap"+newPassword);
		if(newPassword==null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("key new password not exist in request map "),HttpStatus.BAD_REQUEST);
		}

		if(oldPassword.equals(newPassword)){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("oop's...old password matches to new password.Please change the new password :"),HttpStatus.OK);
		}

		User userObj =userService.findByUserId(id);

		logger.info("getting user object by id :"+userObj);

		if(userObj == null){
			return new ResponseEntity<Object>(ResponseUtil.errorResponse("user object is null found by user id"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.info("same password -----:"+userObj.getPassword());

		if((userObj.getPassword()).equals(oldPassword)){
			return new ResponseEntity<Object>("both password are same in which user registered : ",HttpStatus.BAD_REQUEST);
		}

		logger.info("userObj.getPassword()).equals(oldPassword)-----:"+userObj.getPassword());

		if (!(oldPassword.equals(newPassword))){
			userObj.setPassword(newPassword);
			userObj =	userService.save(userObj);
			return new ResponseEntity<Object>(ResponseUtil.successResponse("user new password set successfully ",newPassword),HttpStatus.OK);
		}

		Map<String, Object> reMap = ObjectMap.objectMap(userObj);
		reMap.put("addressArray",ObjectMap.objectMap(userObj.getAddressArray()));
		reMap.put("role", ObjectMap.objectMap(userObj.getRole()));

		return new ResponseEntity<Object>(ResponseUtil.successResponse("user new password updated successfully by user : ", reMap),HttpStatus.OK);
	}
}
