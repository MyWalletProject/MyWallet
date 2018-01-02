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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mywallet.domain.Country;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_CountryData;
import com.mywallet.services.CountryService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class CountryController {

	private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
	
	@Autowired
	private  CountryService countryService;
	
	
	
	@RequestMapping(value="/countries",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllCountries(){

		logger.info("Inside get All Countries api :");
		
		List<Country> countries = countryService.getAllCountry();
		
		List<Map<String,Object>> countriesArray = new ArrayList<Map<String,Object>>();
		for(Country countryObj : countries){
			Map<String,Object> map = ObjectMap.objectMap(countryObj);
			countriesArray.add(map);
		}
		return ResponseUtil.successResponse("Successfully get all countries : ",countriesArray,HttpStatus.OK);
	}
	
	
	@PostMapping(path="/country",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createCountries(@Valid @RequestBody Req_CountryData req_CountryData,BindingResult result){

		logger.info("Inside createCountries api :"+req_CountryData);
		
		if(result.hasErrors()){
			System.out.println("binding result : "+ result.getFieldError().getDefaultMessage());
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		String countryName = req_CountryData.getCountryName();
		
		String countryCode = req_CountryData.getCountryCode();
		
		String countryDialCode = req_CountryData.getCountryDialCode();
		
		Country country =new Country(countryName, countryCode, countryDialCode);
		countryService.save(country);
		
		Map<String,Object> map =ObjectMap.objectMap(country);
		
		return ResponseUtil.successResponse("Successfully post countries : ",map,HttpStatus.OK);
	}
}
