package com.mywallet.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.multipart.MultipartFile;

import com.mywallet.annotaion.ApiAction;
import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Address;
import com.mywallet.domain.LoginHistory;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_ProfileUpdate;
import com.mywallet.services.AddressService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;


@RestController
public class UserController{
	
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MyWalletConfig myWalletConfig;
	
	public UserController(){
		logger.info("UserController class bean is created : ");
	}
	
//	private static String UPLOADED_FOLDER = "D://mywallet//profilePicUpload//";
	
	@ApiAction
	@PostMapping(path="/user/uploadprofilepic/{userId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> upLoadProfilePicOfUser(@Valid @PathVariable ("userId") Integer userId,@RequestParam("file") MultipartFile file){
		logger.info("Inside upLoadProfilePicOfUser api :");
		System.out.println(file.getOriginalFilename());
		
		User userObj= userService.findByUserId(userId);
		if(userObj==null){
			return ResponseUtil.errorResp("user object not found by this userId : ", HttpStatus.NOT_FOUND);
		}
		
		if(file.isEmpty()){
			return ResponseUtil.errorResp("file is empty : ", HttpStatus.NOT_FOUND);
		}
		try{
			//read and write the file to selected location
			byte[] bytes=file.getBytes();
			logger.info("convert into byte ----------: ");
			Path path = Paths.get(myWalletConfig.getUpLoadProfilePic() + file.getOriginalFilename());
			logger.info("path of file -------------- : ");
            Files.write(path, bytes);
            userObj.setUpLoadProfilePic(path.toString());
            userService.save(userObj);
		}
		catch(IOException  exception){
			logger.info("exception occured in file path :");
			exception.printStackTrace();
			return ResponseUtil.errorResp("exception occured in file path : "+exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Map<String, Object> reMap = ObjectMap.objectMap(userObj);
		//reMap.put("addressArray",ObjectMap.objectMap(userObj.getAddressArray()));
		//reMap.put("role", ObjectMap.objectMap(userObj.getRole()));
		
		System.out.println("GET Login History Array ******** : "+ObjectMap.objectMap(userObj.getLoginHistoryArray()));
		
		//reMap.put("loginHistoryArray",ObjectMap.objectMap(userObj.getLoginHistoryArray()));
		
		return ResponseUtil.successResponse("SUCCESSFULLY USER UPLOAD THIER DATA : ", reMap, HttpStatus.OK);
	}

	
	
	@RequestMapping(value="/user/profile/{userId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getUsersProfileById(@PathVariable ("userId") Integer userId){
		
		logger.info("Inside getUsersProfileById api :");
		
		User userObj= userService.findByUserId(userId);
		if(userObj==null){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		
		Map<String , Object> map = ObjectMap.objectMap(userObj,"userId~email~userName~isEmailVerified~isKYCVerified~upLoadProfilePic");
		map.put("addressArray", ObjectMap.objectMap(userObj.getAddressArray()));
		Collections.sort(userObj.getLoginHistoryArray(), new Comparator<LoginHistory>() {
			  public int compare(LoginHistory o1, LoginHistory o2) {
			      return o2.getLoginTime().compareTo(o1.getLoginTime());
			  }
			});
		map.put("loginHistoryArray",ObjectMap.objectMap(userObj.getLoginHistoryArray()));
		
		return  ResponseUtil.successResponse("We successfully get all users profile data : ",map, HttpStatus.OK);
	}

	
	@PostMapping(path="/user",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user,BindingResult bindingResult){
		logger.info("Inside createUser api :");
		
		
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		User	userObj = null;
		try{
			userObj = userService.save(user);
		}
		catch(DataIntegrityViolationException dataIntigrity){

			return ResponseUtil.errorResp("user Already registered with email",HttpStatus.CONFLICT);
		}
		
		if(userObj == null){

			return ResponseUtil.errorResp("user not created",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseUtil.successResponse("user created successfully", userObj,HttpStatus.CREATED);
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

		return ResponseUtil.successResponse("Successfully get all users : ",userArray,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/user/profile/{userId}", method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> modifyObjByUserId(@PathVariable("userId") Integer userId,@Valid @RequestBody Req_ProfileUpdate userProfileUpdate,BindingResult bindingResult){

		logger.info("Inside MODIFY ROLE BY USING ROLE ID :");

		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		User userObj =userService.findByUserId(userId);
		
		if(userObj==null){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		
		Address addressObj =  addressService.findByAddressId(userProfileUpdate.getAddressId());
		if(addressObj==null){
			return  ResponseUtil.errorResp("No address object found by this address id : ", HttpStatus.NOT_FOUND);	
		}
		
		userObj=addressObj.getUser();
		if(!(userId.equals(userObj.getUserId()))){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		
		userObj.setUserName(userProfileUpdate.getUserName());
		addressObj.setCountry(userProfileUpdate.getCountry());
		addressObj.setCity(userProfileUpdate.getCity());
		addressObj.setAddressLine(userProfileUpdate.getAddressLine());
		addressObj.setState(userProfileUpdate.getState());
		addressObj.setStreet(userProfileUpdate.getStreet());
		addressObj.setPincode(userProfileUpdate.getPincode());
		addressObj.setContactNo(userProfileUpdate.getContactNo());
		
		addressService.save(addressObj);
		userService.save(userObj);
		
		Map<String , Object> map = ObjectMap.objectMap(userObj,"userId~email~userName~isEmailVerified~isKYCVerified");
		map.put("addressArray", ObjectMap.objectMap(userObj.getAddressArray()));
		
		return  ResponseUtil.successResponse("successfully modified user profile data : ",map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/roleName/{userId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getUserByRoleName(@PathVariable ("userId") Integer userId){
		logger.info("Inside getUsersProfileById api :");
		
		User userObj= userService.findByUserId(userId);
		if(userObj==null){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		
		return ResponseUtil.successResponse("successfully get user by there role name : ", "", HttpStatus.OK);
	}
	

	@RequestMapping(value="/user/{userName}", method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> modifyObjByUserName(@Valid @PathVariable("userName") String userName,@RequestBody User userData,BindingResult bindingResult){

		logger.info("Inside MODIFY ROLE BY USING ROLE NAME :");

		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		} 
		
		User user = userService.findByUserName(userName);
		if(user == null){
			return ResponseUtil.errorResp("No user object is found",HttpStatus.NOT_FOUND);
		}

		user.setUserName(userData.getUserName());
		user.setEmail(userData.getEmail());
		user.setPassword(userData.getPassword());
		user.setActive(userData.isActive());
		user = userService.save(user);

		if(user == null){	
			return ResponseUtil.errorResp("userName Not modified",HttpStatus.NOT_MODIFIED);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return ResponseUtil.successResponse("User Updated Successfully", reMap,HttpStatus.OK);
	}

	@RequestMapping(value="/users/{userName}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByUserName(@PathVariable String userName){

		if(userName == null || userName.length() == 0){
			return new ResponseEntity<Object>("roleNotNullEmpty",HttpStatus.BAD_REQUEST);
		}

		logger.info("User Name send by UI : "+userName);
		User user = userService.findByUserName(userName);

		if(user == null){

			return ResponseUtil.errorResp("User Not Exist",HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return ResponseUtil.successResponse("User Fetched Successfully", reMap,HttpStatus.OK);
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

			return ResponseUtil.errorResp("Role Not Exist",HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return ResponseUtil.successResponse("User Fetched Successfully", reMap,HttpStatus.OK);
	}

	@RequestMapping(value="/userEmail/search",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByEmail(@RequestParam(defaultValue="",required=true) String email){

		if( email == null || email.equals("")){
			return new ResponseEntity<Object>("User email can Not be Null",HttpStatus.BAD_REQUEST);
		}

		logger.info("User email send by UI : "+email);

		User user = userService.findByEmail(email.trim());

		if(user == null){
			return ResponseUtil.errorResp("User Not Found",HttpStatus.NOT_FOUND);
		}
		Map<String,Object> reMap = ObjectMap.objectMap(user);
		user.getAddressArray();
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return ResponseUtil.successResponse("User Fetched Email Successfully", reMap,HttpStatus.OK);
	}

	@RequestMapping(value="/userDelete/{id}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> deleteRole(@PathVariable String id){
		logger.info("Fetching & Deleting User with id {}", id);

		Integer userId =null;

		try{
			userId = Integer.parseInt(id);
		}
		catch(Exception e){
			return ResponseUtil.errorResp("userIdNotInteger"+e,HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserId(userId);
		logger.info("Fetching User with id {}", user);


		if(user == null){
			return ResponseUtil.errorResp("No user is found",HttpStatus.NOT_FOUND);
		}


		boolean deleteUser = false;
		deleteUser = userService.deleteUserByUserId(userId);

		if(!deleteUser){
			return ResponseUtil.errorResp("No user is id found :",HttpStatus.NOT_FOUND);
		}

		return ResponseUtil.successResponse("Role deleted Successfully",user,HttpStatus.NO_CONTENT);

	}	

	@RequestMapping(value="/assignAddressToUser/{userId}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> assignAddressToUser(@PathVariable ("userId") String id,@RequestBody Map<String, Object> requestMap){
		logger.debug("inside assignAddressToUser api request is :"+requestMap);
		Integer userId =null;

		try{
			userId = Integer.parseInt(id);
		}
		catch(Exception e){
			return ResponseUtil.errorResp("userIdNotInteger"+e,HttpStatus.BAD_REQUEST);
		}
		List<Integer> addressIdArray =(List<Integer>) requestMap.get("addressIdArray");
		System.out.println("getting from addressIdArray :");
		
		if(addressIdArray == null){
			return new ResponseEntity<Object>("addressIdArray key not exist in request map ",HttpStatus.BAD_REQUEST);
		}

		logger.info("Address ID Array from request : "+addressIdArray);

		User user = userService.findByUserId(userId);
		if(user == null){  
			return ResponseUtil.errorResp("No user is found",HttpStatus.NOT_FOUND);
		}		
//       ArrayList<Address> l = new ArrayList<>();
		for(Integer addressId : addressIdArray){
			Address address = addressService.findByAddressId(addressId);
			if(address == null){
				return ResponseUtil.errorResp("Address Id : "+addressId+" not exist so please send valid Id",HttpStatus.NOT_FOUND);
			}
			address.setUser(user);
			address = addressService.save(address);
//			l.add(address);
	   }
//		user.setAddressArray(l);
//		userService.save(user);

		Map<String,Object> reMap = ObjectMap.objectMap(user);
		
		reMap.put("addressArray", ObjectMap.objectMap(user.getAddressArray()));

		return ResponseUtil.successResponse("Address Successfully assign to user", reMap,HttpStatus.OK);
	}

	@ApiAction
	@RequestMapping(value="/user/iskycverified/{userId}", method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> modifyIsKYCVerified(@PathVariable("userId") Integer userId,@Valid @RequestBody Map<String , Object> requestMap,BindingResult bindingResult){

		logger.info("Inside MODIFY iskycverified KEY BY USING ROLE ID :");

		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		User userObj =userService.findByUserId(userId);
		
		if(userObj==null){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		Boolean iskycverified =false;
		iskycverified=(Boolean) requestMap.get("iskycverified");
		
		
		if(!iskycverified){
			return  ResponseUtil.errorResp(" iskycverified return is not verfied : "+iskycverified,HttpStatus.BAD_REQUEST);
		}

		userObj.setIsKYCVerified(iskycverified);
		userService.save(userObj);
		 
		Map<String , Object> map = new HashMap<>();
		map.put("iskycverified",ObjectMap.objectMap(userObj.getIsKYCVerified()));
		return ResponseUtil.successResponse("Successfully modified iskycverified : ", map,HttpStatus.OK);
	}

}
