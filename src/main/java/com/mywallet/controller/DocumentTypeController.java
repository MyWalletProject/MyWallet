package com.mywallet.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mywallet.domain.DocumentType;
import com.mywallet.domain.User;
import com.mywallet.domain.req.Req_DocumentType;
import com.mywallet.services.DocumentTypeService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

@RestController
public class DocumentTypeController {

	
private static final Logger logger = LoggerFactory.getLogger(DocumentTypeController.class);
	
	@Autowired
	private  DocumentTypeService documentTypeService;
	
	
	@RequestMapping(value="/documentTypes",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllDocumentType(){

		logger.info("Inside get All DocumentType api :");
		
		List<DocumentType> documentTypes = documentTypeService.getAllDocumentType();
		
		List<Map<String,Object>> documentTypeArray = new ArrayList<Map<String,Object>>();
		for(DocumentType DocumentTypeObj : documentTypes){
			Map<String,Object> map = ObjectMap.objectMap(DocumentTypeObj);
			documentTypeArray.add(map);
		}
		return ResponseUtil.successResponse("Successfully get all documentTypes : ",documentTypeArray,HttpStatus.OK);
	}
	
	
	@PostMapping(path="/documentType",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createDocumentTypes(@Valid @RequestBody Req_DocumentType req_DocumentType, BindingResult result){

		logger.info("Inside create DocumentTypes api :"+req_DocumentType);
		
		if(result.hasErrors()){
			System.out.println("binding result : "+ result.getFieldError().getDefaultMessage());
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		String documentType = req_DocumentType.getDocumentType();
		if(documentType == null ){
			return ResponseUtil.errorResp("documentType can not be null : ",HttpStatus.BAD_REQUEST);
		} 
		
		
		DocumentType documentTypeObj =new DocumentType(documentType);
		documentTypeService.save(documentTypeObj);
		
		Map<String,Object> map =ObjectMap.objectMap(documentTypeObj);
		
		return ResponseUtil.successResponse("Successfully post documentType : ",map,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/documentType/{documentTypeID}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> documentTypeUpdateById(@PathVariable ("documentTypeID") Integer documentTypeID, @Valid @RequestBody  Req_DocumentType req_DocumentType,BindingResult bindingResult){
		logger.info("inside documentTypeUpdateById  api :");
		
		DocumentType documentTypeObj = documentTypeService.findByDocumentTypeID(documentTypeID);
		if(documentTypeObj == null){
			return ResponseUtil.errorResp("No documentType object is found from database : ", HttpStatus.NOT_FOUND);
		}
        
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		
		logger.info(" DocumentTypeObj setting : ");
		
		documentTypeObj.setDocumentType(req_DocumentType.getDocumentType());
		
		documentTypeService.save(documentTypeObj);

		Map<String, Object> map=ObjectMap.objectMap(documentTypeObj);

		return ResponseUtil.successResponse("Successfully updated DocumentType : ", map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/documentType/{documentTypeId}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> deleteDocumentType(@PathVariable ("documentTypeId") Integer documentTypeId){
		logger.info("Fetching & Deleting documentType with id "+documentTypeId);

		Boolean deleteID = false;
		deleteID = documentTypeService.deleteByDocumentTypeID(documentTypeId);
		System.out.println("kkkk"+deleteID);
		if(!deleteID){
			
			return ResponseUtil.errorResp("No documentType object is deleted from database : "+deleteID, HttpStatus.NOT_FOUND);
		}
	    System.out.println("afttttttttttttttttt"); 
		return ResponseUtil.successResponse("Successfully documentType object deleted from database : ",deleteID,HttpStatus.OK);

	}	
	
	
}
	
