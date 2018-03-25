package com.mywallet.controller;


import java.util.Date;
import java.util.Locale;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mywallet.annotaion.ApiAction;
import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Address;
import com.mywallet.domain.EmailToken;
import com.mywallet.domain.LoginHistory;
import com.mywallet.domain.Role;
import com.mywallet.domain.Token;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_UserLogin;
import com.mywallet.domain.req.Req_signupData;
import com.mywallet.services.AddressService;
import com.mywallet.services.EmailTokenService;
import com.mywallet.services.LoginHistoryService;
import com.mywallet.services.MailService;
import com.mywallet.services.RoleService;
import com.mywallet.services.TokenService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;

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
	
	@Autowired
	private TokenService tService;
	
	@Autowired
	private EmailTokenService emailTokenService;
	
	public UnAuthController(){
		logger.info("UnAuthController class Bean is created : ");
	}

	@Autowired
	private MyWalletConfig myWalletConfig;
	
	@PostConstruct
	public void init(){
	}

	@ApiAction
	@ApiOperation(value = "Api for login", response = ResponseEntity.class)
	//@RequestHeader(value="User-Agent") String userAgent,@RequestHeader(value="Accept-Language") String acceptLanguage,
	@PostMapping(value="/login",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> login( @Valid @RequestBody  Req_UserLogin reqUserLogin,BindingResult result,HttpServletRequest request)  {
		logger.info("Inside login User api :"+reqUserLogin);

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

		Token token = new Token(tService.generateNewToken(),userObj,new Date());
		token=tService.save(token);
		
		if(token==null){
			return ResponseUtil.errorResp("user can't login due to internal server problem : ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Map<String,Object> data=ObjectMap.objectMap(userObj,"userId~email~active~isEmailVerified~isKYCVerified");
		data.put("token", token.getToken().trim());
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
	@ApiOperation(value = "Api for signup by User ", response = ResponseEntity.class)
	@PostMapping(path="/signup",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> signupUser(@Valid @RequestBody Req_signupData signupRequest,BindingResult bindingResult){

		logger.info("Inside signup User api :");

		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		Role roleObj = roleService.findByRoleName(signupRequest.getRoleName());

		if(roleObj == null){
			return ResponseUtil.errorResp("No role is found by this key roleName:",HttpStatus.NOT_FOUND);
		}

		User userObj = userService.findByEmail(signupRequest.getEmail());

		logger.info(" user Object is null ---"+userObj);

		if (userObj != null) {
			return ResponseUtil.errorResp("Oops!  There is already a user registered with the email provided. ",HttpStatus.NOT_FOUND);
		}

		logger.info("getting user Object"+userObj);

		User newUser = new User(signupRequest.getUserName(),signupRequest.getEmail(),signupRequest.getPassword(),true,false,false,new Date(),new Date());
		newUser.setRole(roleObj);
		
		newUser = userService.save(newUser);
		if(newUser== null){
			return ResponseUtil.errorResp("Exception occured in save User object  ",HttpStatus.NOT_FOUND);
	      } 

		logger.info("getting user new Object"+newUser);

		Address addressObj = new Address(signupRequest.getCountry(),newUser);
		
		addressObj = addressService.save(addressObj);
		
		if(addressObj ==null) {
			return ResponseUtil.errorResp("Exception occured in save Address object ",HttpStatus.NOT_FOUND);
		}

		newUser.setDefaultAddressId(addressObj.getAddressId());
		newUser.getAddressArray().add(addressObj);
		userService.save(newUser);

		System.out.println("length is :"+newUser.getAddressArray().size());
		
		
		EmailToken emailTokenObj = new EmailToken(emailTokenService.generateNewToken(), newUser,new Date(), false);
		emailTokenObj = emailTokenService.save(emailTokenObj);
		
		if(emailTokenObj==null){
			return ResponseUtil.errorResp("user can't sigin due to internal server problem : ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String tokenMailBody = "You are successfully registed in mywallet \n"+myWalletConfig.getUrlPath()+"emailVerify?emailToken="+emailTokenService.generateNewToken();
		System.out.println("***********tokenMailBody********"+tokenMailBody);
		mailService.sendMail("hashuv06@gmail.com","harshu@143",signupRequest.getEmail(), "Registration Confirmation.", tokenMailBody);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");

		mailService.userRegisterMail(newUser);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<");

		Map<String,Object> reMap = ObjectMap.objectMap(newUser);
		reMap.put("addressArray", ObjectMap.objectMap(newUser.getAddressArray()));
		reMap.put("role",ObjectMap.objectMap(newUser.getRole()));

		return ResponseUtil.successResponse("User succesfully registed in mywallet",reMap,HttpStatus.OK);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for email Verify By User", response = ResponseEntity.class)
	@RequestMapping(value="/user/emailVerify",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> emailVerifyByUser(@RequestParam("token") String token,HttpServletRequest request){
		logger.info("inside email Verify By User api : "+token);
        
		Locale locale = request.getLocale();
		
		EmailToken emailTokenObj = emailTokenService.findByEmailToken(token);
		if (emailTokenObj == null) {
			return ResponseUtil.errorResp("auth message invalidToken ",HttpStatus.BAD_REQUEST);
	    }
		
		User userObj=emailTokenObj.getUser();
		
		emailTokenObj.setIsExpired(true); 
		emailTokenObj.getUser().setEmailVerified(true);
		emailTokenService.saveRegisteredUser(userObj);
		return ResponseUtil.successResponse("User email Verify successfully by user ","",HttpStatus.OK);
	}

	@ApiAction
	@ApiOperation(value = "Api for password Update By User Id", response = ResponseEntity.class)
	@RequestMapping(value="/password/{userId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> passwordUpdateByUserId(@PathVariable ("userId") Integer id,@RequestBody Map<String, String> requestMap){
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

		return ResponseUtil.successResponse("user new password updated successfully by user ", reMap,HttpStatus.OK);
	}
	
}
