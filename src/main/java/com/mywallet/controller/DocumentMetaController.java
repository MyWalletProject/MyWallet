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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mywallet.annotaion.ApiAction;
import com.mywallet.domain.DocumentMeta;
import com.mywallet.domain.req.Req_DocumentMeta;
import com.mywallet.services.DocumentMetaService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;

@Transactional
@RestController 
public class DocumentMetaController {

	
private static final Logger logger = LoggerFactory.getLogger(DocumentMetaController.class);
	
	@Autowired
	private  DocumentMetaService documentMetaService;
	
	@ApiAction
	@ApiOperation(value = "Api for get All Document Meta", response = ResponseEntity.class)
	@RequestMapping(value="/documentMetas",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getAllDocumentMeta(){

		logger.info("Inside get All documentMeta api :");
		
		List<DocumentMeta> documentMetas = documentMetaService.getAllDocumentMeta();
		
		List<Map<String,Object>> documentMetaArray = new ArrayList<Map<String,Object>>();
		for(DocumentMeta documentMetaObj : documentMetas){
			Map<String,Object> map = ObjectMap.objectMap(documentMetaObj);
			documentMetaArray.add(map);
		}
		return ResponseUtil.successResponse("Successfully get all documentMetaArray : ",documentMetaArray,HttpStatus.OK);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for create DocumentMeta", response = ResponseEntity.class)
	@PostMapping(path="/documentMeta",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createDocumentMeta(@RequestHeader(value="mywallet-token") String mywalletToken,@Valid @RequestBody Req_DocumentMeta req_DocumentMeta,BindingResult result){

		logger.info("Inside createDocumentMeta api :"+req_DocumentMeta);
		
		if(result.hasErrors()){
			System.out.println("binding result : "+ result.getFieldError().getDefaultMessage());
			return ResponseUtil.errorResp(result.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}

		boolean isMandatory = req_DocumentMeta.isMandatory();
		
		DocumentMeta documentMeta =new DocumentMeta(req_DocumentMeta.getDocumentName(), req_DocumentMeta.getDescription(), isMandatory);
		documentMetaService.save(documentMeta);
		
		Map<String,Object> map =ObjectMap.objectMap(documentMeta);
		
		return ResponseUtil.successResponse("Successfully post documentMeta  ",map,HttpStatus.CREATED);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for documentMeta Update By documentMeta Id", response = ResponseEntity.class)
	@RequestMapping(value="/documentMeta/{documentMetaID}",method=RequestMethod.PATCH,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> documentMetaUpdateById(@RequestHeader(value="mywallet-token") String mywalletToken,@PathVariable ("documentMetaID") Integer documentMetaID, @Valid @RequestBody Req_DocumentMeta req_documentMeta,BindingResult bindingResult){
		logger.info("inside documentMetaUpdateById  api :");
		
		DocumentMeta documentMetaObj = documentMetaService.findByDocumentMetaID(documentMetaID);
		if(documentMetaObj == null){
			return ResponseUtil.errorResp("No documentMeta object is found from database : ", HttpStatus.NOT_FOUND);
		}
        
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
		}
		logger.info(" documentMetaObj setting :");
		
		documentMetaObj.setDocumentName(req_documentMeta.documentName);
		documentMetaObj.setDescription(req_documentMeta.description);
		documentMetaObj.setMandatory(req_documentMeta.isMandatory);;
		documentMetaService.save(documentMetaObj);

		Map<String, Object> map=ObjectMap.objectMap(documentMetaObj);

		return ResponseUtil.successResponse("Successfully updated DocumentMeta ", map, HttpStatus.OK);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for delete DocumentMeta by documentMetaId", response = ResponseEntity.class)
	@RequestMapping(value="/documentMeta/{documentMetaId}",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> deleteDocumentMeta(@RequestHeader(value="mywallet-token") String mywalletToken,@PathVariable ("documentMetaId") Integer documentMetaId){
		logger.info("Fetching & Deleting document meta with id "+documentMetaId);
		
		Boolean deleteID = false;
		deleteID = documentMetaService.deleteByDocumentMetaID(documentMetaId);
	
		if(!deleteID){
			
			return ResponseUtil.errorResp("No document meta object is deleted from database : "+deleteID, HttpStatus.NOT_FOUND);
		}
	    System.out.println("tatatatatatatatata"); 
		

		return ResponseUtil.successResponse("document meta deleted Successfully",deleteID,HttpStatus.NO_CONTENT);

	}	
}
	

