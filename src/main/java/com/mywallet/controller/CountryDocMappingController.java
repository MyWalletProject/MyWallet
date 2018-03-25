package com.mywallet.controller;


import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.annotaion.ApiAction;
import com.mywallet.domain.Address;
import com.mywallet.domain.Country;
import com.mywallet.domain.CountryDocMapping;
import com.mywallet.domain.DocumentMeta;
import com.mywallet.domain.DocumentType;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_CountryDocMapping;
import com.mywallet.services.AddressService;
import com.mywallet.services.CountryDocMappingService;
import com.mywallet.services.CountryService;
import com.mywallet.services.DocumentMetaService;
import com.mywallet.services.DocumentTypeService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="CountryDocMapping_Controller",description="Operation related to country document mapping")

@RestController
public class CountryDocMappingController {

	private static final Logger logger = LoggerFactory.getLogger(CountryDocMappingController.class);
	
	@Autowired
	private CountryDocMappingService countryDocMappingService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@Autowired
	private DocumentMetaService documentMetaService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@ApiAction
	@ApiOperation(value = "Api for add CountryDocMapping", response = ResponseEntity.class)
	@PostMapping(path="/countrydocmapping",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createCountryDocMapping(@RequestHeader(value="mywallet-token") String mywalletToken,@Valid @RequestBody Req_CountryDocMapping countryDocMapping,BindingResult bindingResult){
		
		logger.info("inside create CountryDocMapping api");
		
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp("error occured in bindingResult : "+bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		Country country = countryService.findByCountryID(countryDocMapping.getCountryID());
		if(country == null ){
			return ResponseUtil.errorResp("country object can not be null : ",HttpStatus.BAD_REQUEST);
		} 
		
		DocumentType documentType = documentTypeService.findByDocumentTypeID(countryDocMapping.getDocumentTypeID());
		if(documentType == null ){
			return ResponseUtil.errorResp("documentType object can not be null : ",HttpStatus.BAD_REQUEST);
		} 
		
		DocumentMeta documentMeta= documentMetaService.findByDocumentMetaID(countryDocMapping.getDocumentMetaID());
		if(documentMeta == null ){
			return ResponseUtil.errorResp("documentMeta object can not be null : ",HttpStatus.BAD_REQUEST);
		} 
		
		CountryDocMapping countryDocMappingObj = new CountryDocMapping(country, documentType, documentMeta, countryDocMapping.getIsMandatory());
		
		try{
		countryDocMappingService.save(countryDocMappingObj);
		}
		catch(DataIntegrityViolationException dataIntegrity){
			logger.error("DataIntegrityViolationException exception occured : "+dataIntegrity.getClass());
			return ResponseUtil.errorResp("document mapping is already exists ",HttpStatus.CONFLICT);
		}
		catch (Exception e) {
			System.out.println("kkkkkkkkkkkkkkkkk");
			logger.error("Exception occured on duplicate data "+e.getClass());
			
			return ResponseUtil.errorResp("No countryDocMapping Obj data is saved ",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		Map<String, Object>  map = ObjectMap.objectMap(countryDocMappingObj);
		map.put("country", ObjectMap.objectMap(countryDocMappingObj.getCountry()));
		map.put("documentType", ObjectMap.objectMap(countryDocMappingObj.getDocumentType()));
		map.put("documentMeta", ObjectMap.objectMap(countryDocMappingObj.getDocumentMeta()));
		return ResponseUtil.successResponse("Successfully CountryDocMapping object is created", map, HttpStatus.CREATED);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for get all CountryDocMapping Object", response = ResponseEntity.class)
	@RequestMapping(value="/countrydocmappings",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllCountrydocmapping(){

		logger.info("Inside get All Countrydocmapping  api :");
		
		List<CountryDocMapping> countryDocMappings = countryDocMappingService.getAllCountryDocMapping();
		
		ArrayList<CountryDocMapping> countryDocMappingArray = new ArrayList<CountryDocMapping>();
		for(CountryDocMapping countryDocMappingObj : countryDocMappings){
			countryDocMappingArray.add(countryDocMappingObj);
		}
		
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put("countryDocMappingArray", ObjectMap.objectMap(countryDocMappingArray));
		
		return ResponseUtil.successResponse("Successfully get all countries : ",map,HttpStatus.OK);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for get CountryDocMapping by user Id", response = ResponseEntity.class)
	@RequestMapping(value="/countrymapping/{userId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public ResponseEntity<Object> getCountrydocmappingById(@RequestHeader(value="mywallet-token") String mywalletToken,@PathVariable (value ="userId") Integer userId){
		logger.info("Inside get All Countrydocmapping by id api :");
		
		if(userId==null){
			return ResponseUtil.errorResp("user id is not exist ", HttpStatus.BAD_REQUEST);
		}
		
		User userObj=userService.findByUserId(userId);
		
		if(userObj==null){
			return ResponseUtil.errorResp("user object can not found by this Id : ", HttpStatus.NOT_FOUND);
		}
		
		Integer defaultAddressId = userObj.getDefaultAddressId();
		logger.info("********************************* :"+userObj.getDefaultAddressId());
		
		if(defaultAddressId == null){
			return ResponseUtil.errorResp("Default AddressId can not found by this Id : ", HttpStatus.NOT_FOUND);
		}
		
		Address address = addressService.findByAddressId(defaultAddressId);
		logger.info("++++++++++++++++++++++++ :");
		//CountryDocMapping countryDocMapObj = countryDocMappingService.findByCountryDocMappingId(defaultAddressId);
		if(address==null){
			return ResponseUtil.errorResp("User Address not found : ", HttpStatus.NOT_FOUND);
		}
		
		List countryDocMappingArray  = ObjectMap.objectMap(countryDocMappingService.getAllCountryDocMappingByCountryName(address.getCountry().trim()));
		
//		Map<String, Object>  map = new HashMap<String, Object>();
//		map.put("countryDocMappingArray", ObjectMap.objectMap(countryDocMappingArray));

		return ResponseUtil.successResponse("Successfully get country document mapping  ", countryDocMappingArray, HttpStatus.OK);
		
	}
	
}
