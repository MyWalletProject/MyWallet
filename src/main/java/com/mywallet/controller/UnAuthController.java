package com.mywallet.controller;


import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
import com.mywallet.domain.Address;
import com.mywallet.domain.LoginHistory;
import com.mywallet.domain.Role;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_UserLogin;
import com.mywallet.services.AddressService;
import com.mywallet.services.LoginHistoryService;
import com.mywallet.services.MailService;
import com.mywallet.services.RoleService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class UnAuthController {

	private static final Logger logger = LoggerFactory.getLogger(UnAuthController.class);

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private LoginHistoryService loginHistoryService;

	public UnAuthController(){
		logger.info("UnAuthController class Bean is created : ");
	}

	@PostConstruct
	public void init(){
	}

	@ApiAction
	//@RequestHeader(value="User-Agent") String userAgent,@RequestHeader(value="Accept-Language") String acceptLanguage,
	@PostMapping(value="/login",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> login( @Valid @RequestBody  Req_UserLogin reqUserLogin,BindingResult result,HttpServletRequest request)  {
		logger.info("Inside signup User api :"+reqUserLogin);

		//		 System.out.println("userAgent  : " + userAgent);
		//		 System.out.println("loginIP : "+request.getRemoteAddr());
		//		 System.out.println("loginTime : "+ new Date());

		if(result.hasErrors()){
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}

		User userObj=userService.findByEmailAndPassword(reqUserLogin.getEmail(), reqUserLogin.getPassword());

		if(userObj==null){
			return ResponseUtil.errorResp("User not exist, invalid login credenstial with password or email incorrect",HttpStatus.NOT_FOUND);
		}

		Map<String,Object> data=ObjectMap.objectMap(userObj,"userId~email~active~isEmailVerified~isKYCVerified");
		data.put("lastLogin", userObj.getlastLogin());
		
		data.put("role",ObjectMap.objectMap(userObj.getRole()));


		LoginHistory loginHistory = new LoginHistory( request.getRemoteAddr(),  request.getHeader("User-Agent"), new Date());

		logger.info("error occured in loginHistory : "+loginHistory);

		try{
			loginHistory.setUser(userObj);
			loginHistoryService.save(loginHistory);
		}
		catch(Exception exception){
			logger.error("error occured while saving the loginHistory :"+exception);
			return ResponseUtil.errorResp("error occured while saving the loginHistory :"+exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseUtil.successResponse("User successfully login.",data,HttpStatus.OK);
	}

	@ApiAction
	@PostMapping(path="/signup",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> signupUser( @RequestBody Map<String,Object> signupRequestMap){

		logger.info("Inside signup User api :"+signupRequestMap);

		String userName= (String) signupRequestMap.get("userName");

		if(userName == null){
			return ResponseUtil.errorResp("key not exist user name in request map ",HttpStatus.BAD_REQUEST);
		}

		String email=(String) signupRequestMap.get("email");
		if(email == null){
			return ResponseUtil.errorResp("key not exist email in request map ",HttpStatus.BAD_REQUEST);
		}
		
		String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
		regex.matches("email");

		String password=(String) signupRequestMap.get("password");

		if(password == null){
			return ResponseUtil.errorResp("key not exist password in request map ",HttpStatus.BAD_REQUEST);
		}

		String roleName=(String) signupRequestMap.get("roleName");

		logger.info("getting roleName User ---- :"+roleName);

		if(roleName == null || roleName.equals("")){
			return ResponseUtil.errorResp("key not exist role name in request map ",HttpStatus.BAD_REQUEST);
		}

		Role roleObj = roleService.findByRoleName(roleName);

		logger.info("getting role Object ;;;;;;..... :"+roleObj);

		if(roleObj == null){
			return ResponseUtil.errorResp("No role is found by this key roleName:",HttpStatus.NOT_FOUND);
		}

		User userObj = userService.findByEmail(email);
		//		userService.findByRole(roleObj);

		logger.info(" user Object is null ---"+userObj);

		if (userObj != null) {
			return ResponseUtil.errorResp("Oops!  There is already a user registered with the email provided. ",HttpStatus.NOT_FOUND);
		}

		logger.info("getting user Object"+userObj);

		User newUser = new User(userName,email,password,true,false,false);
		newUser.setRole(roleObj);
		newUser = userService.save(newUser);

		logger.info("getting user new Object"+newUser);

		String country=(String) signupRequestMap.get("country");
		if(country == null){
			return ResponseUtil.errorResp("key not exist country in request map ",HttpStatus.BAD_REQUEST);
		}

		logger.info("getting country in address"+country);

		Address addressObj = new Address(country,newUser);
		logger.info("address Object ::::::"+addressObj);

		addressObj = addressService.save(addressObj);

		mailService.sendMail(email, "Successfully registered", "you are successfully registed in mywallet");

		newUser.getAddressArray().add(addressObj);

		System.out.println("length is :"+newUser.getAddressArray().size());

		if(addressObj == null) {
			return ResponseUtil.errorResp("address object not saved",HttpStatus.NOT_FOUND);
		}

		mailService.userRegisterMail(newUser);
		Map<String,Object> reMap = ObjectMap.objectMap(newUser);
		reMap.put("addressArray", ObjectMap.objectMap(newUser.getAddressArray()));
		reMap.put("role",ObjectMap.objectMap(newUser.getRole()));

		return ResponseUtil.successResponse("User succesfully registed in mywallet",reMap,HttpStatus.OK);
	}

	@ApiAction
	@RequestMapping(value="/password/{userId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> passwordUpdateByUser(@PathVariable ("userId") Integer id,@RequestBody Map<String, String> requestMap){
		logger.info("inside passwordResetByUser api : "+requestMap);

		String oldPassword =  requestMap.get("oldPassword");
		if(oldPassword==null){
			return ResponseUtil.errorResp("key old Password not exist in request map ",HttpStatus.BAD_REQUEST);
		}
		logger.info("getting oldpassword key from requestMap"+oldPassword);


		String newPassword = requestMap.get("newPassword");
		logger.info("getting newpassword key from requestMap"+newPassword);
		if(newPassword==null){
			return ResponseUtil.errorResp("key new password not exist in request map ",HttpStatus.BAD_REQUEST);
		}

		if(oldPassword.equals(newPassword)){
			return ResponseUtil.errorResp("oop's...old password matches to new password.Please change the new password :",HttpStatus.OK);
		}
		
		User userObj =userService.findByUserId(id);

		logger.info("getting user object by id :"+userObj);

		if(userObj == null){
			return ResponseUtil.errorResp("user object is null found by user id",HttpStatus.NOT_FOUND);
		}

		logger.info("same password -----:"+userObj.getPassword());

		if(!((userObj.getPassword()).equals(oldPassword))){
			return  ResponseUtil.errorResp("password are not same as by which user registered : ",HttpStatus.BAD_REQUEST);
		}

		logger.info("userObj.getPassword()).equals(oldPassword)-----:"+userObj.getPassword());

		if (oldPassword.equals(newPassword)){
			return  ResponseUtil.errorResp("password are not same as by which user registered : ",HttpStatus.BAD_REQUEST);
		}

		userObj.setPassword(newPassword);
		userObj =	userService.save(userObj);
		Map<String, Object> reMap = ObjectMap.objectMap(userObj);
		reMap.put("addressArray",ObjectMap.objectMap(userObj.getAddressArray()));
		reMap.put("role", ObjectMap.objectMap(userObj.getRole()));

		return ResponseUtil.successResponse("user new password updated successfully by user : ", reMap,HttpStatus.OK);
	}
}
