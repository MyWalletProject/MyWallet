package com.mywallet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.domain.Action;
import com.mywallet.services.ActionService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class ActionController {

	@Autowired
	private ActionService actionService;
	
	@RequestMapping(value="/actions",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllAction(){
	
		List<Action> actionList = actionService.getAllAction();
		
		if(actionList == null){
			return ResponseUtil.errorResp("No action object is found in database : ", HttpStatus.NOT_FOUND);
		}
		
		ArrayList<Action> actionArray = new ArrayList<Action>();
		for(Action action : actionList){
			actionArray.add(action);
		}
		
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("actionArray", ObjectMap.objectMap(actionArray));
		
		return ResponseUtil.successResponse("Successfully get all action : ", map, HttpStatus.OK);
	}
}
