package com.mywallet.controller;


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
import com.mywallet.domain.Address;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_AddressData;
import com.mywallet.services.AddressService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class AddressController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class.getName());

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private UserService userService;
	
	
	public AddressController(){
		logger.info("AddressController class bean created :");
	}
	

	@PostMapping(path="/address/{userId}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> addAddresses(@Valid @PathVariable ("userId") Integer userId, @RequestBody Req_AddressData addressData,BindingResult bindingResult ){
		logger.info("inside add address  api :");
		
		User userObj = userService.findByUserId(userId);
		if(userObj == null){
			return ResponseUtil.errorResp("No user object is found by this userId : ",HttpStatus.NOT_FOUND);
		}
		
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		Address addressObj = new Address(); 
		
		addressObj.setCountry(addressData.getCountry());
		addressObj.setCity(addressData.getCity());
		addressObj.setAddressLine(addressData.getAddressLine());
		addressObj.setState(addressData.getState());
		addressObj.setStreet(addressData.getStreet());
		addressObj.setPincode(addressData.getPincode());
		addressObj.setContactNo(addressData.getContactNo());
		
		addressObj.setUser(userObj);
		addressObj = addressService.save(addressObj);
		
		Map<String , Object>map= ObjectMap.objectMap(addressObj);
			
		
		
		return ResponseUtil.successResponse("added all address successfully", map,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/address/{addressId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByAddressId(@PathVariable Integer addressId){

		if(addressId == null ){
			return ResponseUtil.errorResp("addressId Not Null ",HttpStatus.BAD_REQUEST);
		}

		logger.info("addressId send by UI : "+addressId);
		
		Address addressObj = addressService.findByAddressId(addressId);
		
		if(addressObj == null){
			
			return ResponseUtil.errorResp("address Not Exist",HttpStatus.NOT_FOUND);
		}

		return ResponseUtil.successResponse("address Fetched Successfully", addressObj,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/addressUpdate/{addressId}", method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> modifyObjByAddressId(@PathVariable("addressId") String id,@RequestBody Address addressrData){

		logger.info("Inside MODIFY Address BY USING Address ID :");
		
		Integer addressId =null;
		try{
			addressId = Integer.parseInt(id);
		}
		catch(Exception e){
			return ResponseUtil.errorResp("addressId Not Integer Exception occure "+e,HttpStatus.BAD_REQUEST);
		}

		Address address = addressService.findByAddressId(addressId);
		if(address == null){
			return ResponseUtil.errorResp("No address is found",HttpStatus.NOT_FOUND);
		}

		address.setCountry(addressrData.getCountry());
		address.setCity(addressrData.getCity());
		address.setState(addressrData.getState());
		address.setStreet(addressrData.getStreet());
		address = addressService.save(address);

		if(address == null){	
			return ResponseUtil.errorResp("address Not modified",HttpStatus.NOT_MODIFIED);
		}
		return ResponseUtil.successResponse("ADDRESS Updated Successfully", address,HttpStatus.OK);
	}
	
	@RequestMapping(value="/addressDelete/{addressId}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> addressDeleteByAddressId(@PathVariable Integer addressId){
		
		Address address = addressService.findByAddressId(addressId);
		logger.info("Fetching address with id {}", address);
		
         if(address == null){
			
			return ResponseUtil.errorResp("No addresss is found",HttpStatus.NOT_FOUND);
		}
		
         boolean deleteAddress = false;
         deleteAddress= addressService.deleteAddressByAddressId(addressId);
		
         if(!deleteAddress){
 			
 			return ResponseUtil.errorResp("No address is deleted",HttpStatus.NOT_FOUND);
 		}

		return ResponseUtil.successResponse("Address deleted Successfully", address,HttpStatus.NO_CONTENT);

	}
	
}
