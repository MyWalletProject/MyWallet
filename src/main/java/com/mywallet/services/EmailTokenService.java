package com.mywallet.services;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.domain.EmailToken;
import com.mywallet.domain.User;
import com.mywallet.repository.EmailTokenRepository;

@Service
public class EmailTokenService {

	
public final static Logger logger=Logger.getLogger(EmailTokenService.class);
	
	@Autowired
	public EmailTokenRepository emailTokenRepository;
	
	public EmailToken save(EmailToken token){
		logger.info("inside save method of EmailTokenService : "); 
		try {
			return emailTokenRepository.save(token);
			
		} catch (Exception e) {
			logger.info("EmailToken object saved in database : ");
			return  null;
		}
	}
	
	public User saveRegisteredUser(User user){
		logger.info("inside save RegisteredUser method of EmailTokenService : "); 
		try {
			return emailTokenRepository.saveRegisteredUser(user);
			
		} catch (Exception e) {
			logger.info("EmailToken object saved in database : ");
			return  null;
		}
	}
	
	
	public EmailToken findByEmailTokenId(Integer emailTokenId){
		logger.info("inside findByEmailTokenId method of TokenService : "); 
		try {
			return emailTokenRepository.findByEmailTokenId(emailTokenId);
		} catch (Exception e) {
			logger.info("token object saved in database by email token id : ");
			return  null;
		}
	}
	
	public User getUserByEmailToken(String emailToken){
		logger.info("inside get UserBy TokenString method of TokenService : "); 
		try {
			return emailTokenRepository.getUserByEmailToken(emailToken);
		} catch (Exception e) {
			logger.info("token object saved in database by token id : ");
			return  null;
		}
	}
	
	public EmailToken findByEmailToken(String emailToken){
		logger.info("inside find By Token method of TokenService : "); 
		try {
			return emailTokenRepository.findByEmailToken(emailToken);
		} catch (Exception e) {
			logger.info("token object saved in database by token : ");
			return  null;
		}
	}
	
	
	//-1 is when token does not exists and for success 0
		public int logout(String emailToken){
			if(emailToken.equals(""))
				return -1;
			
			EmailToken token=emailTokenRepository.findByEmailToken(emailToken);
			if(token==null)
				return -1;
			else{
				emailTokenRepository.delete(token);
				return 0;
			}
		}
		
		public String generateNewToken(){
			return UUID.randomUUID().toString();
		}
	
}
