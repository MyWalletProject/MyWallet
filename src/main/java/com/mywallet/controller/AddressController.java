package com.mywallet.controller;

import java.util.HashMap;
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
import com.mywallet.services.AddressService;
import com.mywallet.util.ResponseUtil;

@RestController
public class AddressController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class.getName());

	@Autowired
	private AddressService addressService;
	
	public AddressController(){
		logger.info("AddressController class bean created :");
	}
	

	@PostMapping(path="/address", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> addAddresses(@Valid @RequestBody Address addressData,BindingResult bindingResult ){
		logger.info("inside add address  api :");
		Map<String, Object> map = new HashMap<String, Object>();
		if(bindingResult.hasErrors()){
			map.put("Error Message " , bindingResult.getFieldError().getDefaultMessage());
			return new ResponseEntity<Object>(map,HttpStatus.BAD_REQUEST);
		}
		Address addressObj = addressService.save(addressData);
		if(addressObj == null){
			return ResponseUtil.errorResp("no sddress is added",HttpStatus.NOT_FOUND);
		}
		return ResponseUtil.successResponse("added all address successfully", addressObj,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/address/{addressId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getByAddressId(@PathVariable Integer addressId){

		if(addressId == null ){
			return new ResponseEntity<Object>("addressId Not Null ",HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<>("addressId Not Integer",HttpStatus.BAD_REQUEST);
		}

		Address address = addressService.findByAddressId(addressId);
		if(address == null){
			return new ResponseEntity<>("No address is found",HttpStatus.NOT_FOUND);
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
	
	@RequestMapping(value="/addressDelete/{addressId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
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
