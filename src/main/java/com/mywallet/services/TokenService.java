package com.mywallet.services;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.mywallet.domain.Token;
import com.mywallet.domain.User;
import com.mywallet.repository.TokenRepository;

@Service
public class TokenService {

	
	public final static Logger logger=Logger.getLogger(TokenService.class);
	
	@Autowired
	public TokenRepository tokenRepository;
	
	public Token save(Token token){
		logger.info("inside save method of TokenService : "); 
		try {
			return tokenRepository.save(token);
			
		} catch (Exception e) {
			logger.info("token object saved in database : ");
			return  null;
		}
	}
	
	public Token findByTokenId(Integer tokenId){
		logger.info("inside findByTokenId method of TokenService : "); 
		try {
			return tokenRepository.findByTokenId(tokenId);
		} catch (Exception e) {
			logger.info("exception occured in database by find By token id : ");
			return  null;
		}
	}
	
	public User getUserByTokenString(String tokenString)throws JpaSystemException,HibernateException{
		logger.info("inside get UserBy TokenString method of TokenService : "); 
		try {
			return tokenRepository.getUserByTokenString(tokenString);
		} 
		catch(JpaSystemException jpaExp){
			logger.info("JpaSystemExceptionexception occured in getUserByTokenString : "+jpaExp); 
			return  null;
		}
		catch(HibernateException exp){
			logger.info("HibernateException exception occured in getUserByTokenString : "+exp);
			return  null;
		}
		catch (Exception e) {
			logger.info("exception occured in getUserByTokenString from database: "+e);
			return  null;
		}
	}
	
	public Token findByToken(String token){
		logger.info("inside find By Token method of TokenService : "); 
		try {
			return tokenRepository.findByToken(token);
		} catch (Exception e) {
			logger.info("exception occured in database by findBytoken : ");
			return  null;
		}
	}
	
	
	//-1 is when token does not exists and for success 0
		public int logout(String token){
			if(token.equals(""))
				return -1;
			
			Token token1=tokenRepository.findByToken(token);
			if(token==null)
				return -1;
			else{
				tokenRepository.delete(token1);
				return 0;
			}
		}
		
		public String generateNewToken(){
			return UUID.randomUUID().toString();
		}
}
