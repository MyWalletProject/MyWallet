package com.mywallet.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mywallet.annotaion.ApiAction;
import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Address;
import com.mywallet.domain.CountryDocMapping;
import com.mywallet.domain.Document;
import com.mywallet.domain.User;
import com.mywallet.services.AddressService;
import com.mywallet.services.CountryDocMappingService;
import com.mywallet.services.DocumentService;
import com.mywallet.services.UserService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class DocumentController {
	
	public final static Logger logger=Logger.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private MyWalletConfig myWalletConfig; //key UPLOADED_URL_FOLDER
	    
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CountryDocMappingService countryDocMappingService;
	
	@ApiAction
	@ApiOperation(value = "Api for upload User Document url by userId", response = ResponseEntity.class)
	@PostMapping(path="/document/url/{userId}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> uploadUserDocument(@RequestHeader(value="mywallet-token") String mywalletToken,@Valid @PathVariable ("userId") Integer userId, @RequestBody Integer countryDocMappingId, @RequestParam("file") MultipartFile file
		){
		
		User userObj=userService.findByUserId(userId);
		if(userObj==null){
			return ResponseUtil.errorResp("userObj can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
		Address addressObj= addressService.findByAddressId(userObj.getDefaultAddressId());
	
		if(countryDocMappingId==null){
			return ResponseUtil.errorResp("countryDocMappingId can not be found by this id : ", HttpStatus.BAD_REQUEST);
		}
		
		CountryDocMapping docMappingObj = countryDocMappingService.findByCountryDocMappingId(countryDocMappingId);
		
		 Document document;
		
		if(file.isEmpty()){
			return ResponseUtil.errorResp("file is empty : ", HttpStatus.NOT_FOUND);
		}
		try{
			//read and write the file to selected location
			byte[] bytes=file.getBytes();
			logger.info("convert into byte ----------: ");
			Path path = Paths.get(myWalletConfig.getURL() + file.getOriginalFilename());
			logger.info("path of file -------------- : "+path);
            Files.write(path, bytes);
            
            System.out.println("document path set : "+path.toString());
           
            if(!addressObj.getCountry().equals(docMappingObj.getCountry())){
            	return ResponseUtil.errorResp("exception occured in document file path : ", HttpStatus.CONFLICT);
            }
            document= new Document(new Date(),false,"Pending",path.toString(),userObj);
            System.out.println("document path set after it : "+path.toString());
            
            documentService.save(document);
           
		}
		catch(IOException  exception){
			logger.info("exception occured in file path :");
			exception.printStackTrace();
			return ResponseUtil.errorResp("exception occured in document file path : "+exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	     
	     Map<String, Object> map = ObjectMap.objectMap(document);
		
		return ResponseUtil.successResponse("Successfully user documents uploaded : ", map, HttpStatus.CREATED);
	}
	
	@ApiAction
	@ApiOperation(value = "Api for get User Document By user Id", response = ResponseEntity.class)
	@RequestMapping(value="/user/document/{userId}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getUserDocumentById(@RequestHeader(value="mywallet-token") String mywalletToken,@PathVariable ("userId") Integer userId){
		
		logger.info("Inside getUserDocumentById api :"+userId);
		
		User userObj= userService.findByUserId(userId);
		
		if(userObj==null){
			return  ResponseUtil.errorResp("No user object found by this user id : ", HttpStatus.NOT_FOUND);	
		}
		List<Document> document = documentService.getAllDocument();
		Map<String , Object> map =new HashMap<String , Object>();
				map.put("documentArray", ObjectMap.objectMap(document));
		
		return  ResponseUtil.successResponse("We successfully get all user DOCUMENT data : ",map, HttpStatus.OK);
	}
	
}