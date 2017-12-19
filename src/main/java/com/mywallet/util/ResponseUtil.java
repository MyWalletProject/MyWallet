package com.mywallet.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
	public static Map<String,Object> successResponse(String message, Object data){
		Map<String,Object> response = new HashMap<String,Object>();
		
			response.put("data",data);
			response.put("message",message);
			response.put("error",true);
			return response;
	 }
	
    public static Map<String,Object> errorResponse(String message){
    	Map<String,Object> response = new HashMap<String,Object>();
    	
	    	response.put("data",null);
			response.put("message",message);
			response.put("success",true);
			return response;
	}
    
    
    public static ResponseEntity<Object> errorResp(String message,HttpStatus httpStatus){
    	
      	Map<String,Object> responseMap = new HashMap<String,Object>();
    	
      	responseMap.put("data",null);
      	responseMap.put("message",message);
      	responseMap.put("success",true);

    	
    	return new ResponseEntity<Object>(responseMap,httpStatus);
    	
    } 
}
