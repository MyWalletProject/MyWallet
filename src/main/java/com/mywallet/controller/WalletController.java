package com.mywallet.controller;

import java.util.Base64;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mywallet.annotaion.ApiAction;
import com.mywallet.domain.User;
import com.mywallet.domain.Wallet;
import com.mywallet.domain.req.Req_WalletData;
import com.mywallet.services.TokenService;
import com.mywallet.services.UserService;
import com.mywallet.services.WalletService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;


@Controller
public class WalletController {

	private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
	
	@Autowired
	public WalletService walletService;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	public TokenService tokenService;

	
	@ApiAction
    @ApiOperation(value = "Api for generate address of wallet", response = ResponseEntity.class)
	@PostMapping(value="/addwalletaddress",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> addAddressInWallet(@RequestHeader(value="mywallet-token") String mywalletToken){
		logger.info("Inside addAddressInWallet api :");
		
        User userObj =tokenService.getUserByTokenString(mywalletToken);
		
		if(userObj==null){
			return ResponseUtil.errorResp("user Object can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
		Wallet walletObj = new Wallet(walletService.generateAddress(),userObj);
		walletObj=walletService.save(walletObj);
		System.out.println("*****^^^^^^walletObj^^^^^*******"+walletObj);

		if(walletObj==null){
			return ResponseUtil.errorResp("wallet object can not be found: ", HttpStatus.NOT_FOUND);
		}
		
         Map<String, Object> map = ObjectMap.objectMap(walletObj);
         
		return ResponseUtil.successResponse("Successfully Add Address In Wallet.",map,HttpStatus.OK);
	}

	
	@ApiAction
	@ApiOperation(value = "Api for get user wallet balance", response = ResponseEntity.class)
	@RequestMapping(value="/walletbalance",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> getWalletBalance(@RequestHeader(value="mywallet-token") String mywalletToken){
		logger.info("Inside getWalletBalance api :");
		
		User userObj =tokenService.getUserByTokenString(mywalletToken);
		
		if(userObj==null){
			return ResponseUtil.errorResp("userObj can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
		System.out.println("******userobject^^^^^^^ : "+userObj);
		
    	Wallet walletObj = userObj.getWallet();    	
    	if(walletObj==null){
			return ResponseUtil.errorResp("userWalletObj can not be found by this wallet id : ", HttpStatus.NOT_FOUND);
		}
    	
    	Map<String , Object> map = ObjectMap.objectMap(walletObj);
//		map.put("walletBalance",ObjectMap.objectMap(walletObj.getWalletBalance()));
		
		return ResponseUtil.successResponse("Successfully get Wallet Balance.",map,HttpStatus.OK);
	}
	
	//admin ko balance add krne ka write addAmountInWallet
	
	@ApiAction
	@ApiOperation(value = "Api for add balance in user wallet", response = ResponseEntity.class)
	@PostMapping(value="/addbalance",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> addAmountInWallet(@RequestHeader(value="mywallet-token") String mywalletToken,Double walletBalance){
		
		logger.info("Inside addAmountInWallet api :");
		
		if(walletBalance == null || walletBalance.equals("")){
			return new ResponseEntity<Object>("wallet Balance can Not be Null or Empty",HttpStatus.BAD_REQUEST);
		}
		
        User userObj =tokenService.getUserByTokenString(mywalletToken);
		
		if(userObj==null){
			return ResponseUtil.errorResp("user Object can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
//		Wallet walletObj = new Wallet(walletService.generateAddress(),userObj);
//		walletObj=walletService.save(walletObj);
//		System.out.println("*****^^^^^^walletObj^^^^^*******"+walletObj);
//
//		if(walletObj==null){
//			return ResponseUtil.errorResp("wallet object can not be found: ", HttpStatus.NOT_FOUND);
//		}
		
		Wallet walletObj = userObj.getWallet();
		System.out.println("****** walletObj : "+walletObj);
		
		walletObj =	walletService.findByWalletId(walletObj.getWalletId());
	    
		if(walletObj==null){
			return ResponseUtil.errorResp("wallet object can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		System.out.println("*****''''''walletObj'''''*******"+walletObj);
		
		
		Boolean iskycverified  = userService.findByIsKYCVerified(walletObj.getUser().getIsKYCVerified());
		
		System.out.println("******** iskycverified "+iskycverified);
		
		if(!iskycverified){
			walletObj.setWalletStatus(new String("INACTIVE"));
	        logger.info("WalletStatus IS INACTIVE");
			return  ResponseUtil.errorResp("User kyc is not verified.You can't perform this action",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
         walletObj.setWalletStatus(new String("ACTIVE"));
         
         Double walletAmount = walletObj.getWalletBalance() + walletBalance;
         
         walletObj.setWalletBalance(walletAmount);
         walletService.save(walletObj);
         
 		System.out.println("******** walletObj "+ObjectMap.objectMap(walletObj));
        logger.info("Successfully save walletAmount in database"+ObjectMap.objectMap(walletObj));
         
         Map<String, Object> map = ObjectMap.objectMap(walletObj);
         
		return ResponseUtil.successResponse("Successfully Add Amount In Wallet.",map,HttpStatus.OK);
	}
	
	
	@ApiAction
	@ApiOperation(value = "Api for transfer balance from one wallet to another", response = ResponseEntity.class)
	@PostMapping(path="/transferbalance",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> transferMoneyInWallet(@RequestHeader(value="mywallet-token") String mywalletToken,@Valid @RequestBody Req_WalletData walletData,BindingResult bindingResult){
		
		logger.info("Inside transferMoneyInWallet api :");
		
		if(bindingResult.hasErrors()){
			return ResponseUtil.errorResp("FieldError"+bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
		}
		
		User userObj =tokenService.getUserByTokenString(mywalletToken);
		
		if(userObj==null){
			return ResponseUtil.errorResp("userObj can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
		Wallet walletObj = userObj.getWallet();
		walletObj= walletService.findByWalletId(walletObj.getWalletId());
		
		if(walletObj==null){
			return ResponseUtil.errorResp("walletObj can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
		
		Boolean iskycverified  = userService.findByIsKYCVerified(userObj.getIsKYCVerified());
		System.out.println("******** iskycverified "+iskycverified);
		
		if(!iskycverified){
	        walletObj.setWalletStatus(new String("INACTIVE"));
	        logger.info("WalletStatus IS INACTIVE");
			return  ResponseUtil.errorResp("User kyc is not verified.You can't perform this action",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
         walletObj.setWalletStatus(new String("ACTIVE"));
         
         Double walletAmount = walletObj.getWalletBalance();
         
         if(walletAmount == null || walletAmount.equals("")){
 			return new ResponseEntity<Object>("wallet Balance can not be Null or Empty",HttpStatus.NOT_FOUND);
 		}
         
        Wallet walletObj2 = walletService.findByWalletId(walletData.getWalletId());
        
        if(walletObj2==null){
			return ResponseUtil.errorResp("walletObj2 can not be found by this id : ", HttpStatus.NOT_FOUND);
		}
         
        String address = walletData.getAddress(); 
        address= Base64.getEncoder().encodeToString(walletService.generateAddress().toString().getBytes());
  	    System.out.println("walletAddress :"+address);
        
        Double walletMoney = walletData.getWalletBalance();
          
        if(walletAmount < walletMoney)
        {
        	System.out.println("Insufficient funds!!");
			return ResponseUtil.errorResp("Insufficient amount in wallet, please add some amount in your wallet.", HttpStatus.NOT_FOUND);
        }
        else
        {
            walletAmount= walletAmount - walletMoney;
            System.out.println(walletAmount);

//            walletObj.setWalletBalance(walletAmount);
//            walletService.save(walletObj);
            
            walletMoney= walletObj2.getWalletBalance() + walletMoney;
            System.out.println(walletMoney);
            
//            walletObj2.setWalletBalance(walletMoney);
//            walletService.save(walletObj2);
        }
        walletObj.setWalletBalance(walletAmount);
        walletService.save(walletObj);
 
        walletObj2.setWalletBalance(walletMoney);
        walletService.save(walletObj2); 
        
        Map<String, Object> map = ObjectMap.objectMap(walletObj);
     	return ResponseUtil.successResponse("Successfully transfer Money In Wallet.",map,HttpStatus.OK);
	}
	
}
