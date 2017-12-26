package com.mywallet.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
	
	public static ResponseEntity<Object> successResponse(String message, Object data,HttpStatus httpStatus){
		Map<String,Object> response = new HashMap<String,Object>();
		
			response.put("data",data);
			response.put("message",message);
			response.put("success",true);
			response.put("error",false);
			
			return new ResponseEntity<Object>(response,httpStatus);
	 }
	
    
    public static ResponseEntity<Object> errorResp(String message,HttpStatus httpStatus){
      	Map<String,Object> responseMap = new HashMap<String,Object>();
    	
      	responseMap.put("data",null);
      	responseMap.put("message",message);
      	responseMap.put("error",true);
      	responseMap.put("success",false);
      	
    	return new ResponseEntity<Object>(responseMap,httpStatus);
    	
    } 
}
