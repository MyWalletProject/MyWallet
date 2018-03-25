package com.mywallet.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mywallet.domain.LoginHistory;
import com.mywallet.domain.User;
import com.mywallet.repository.LoginHistoryRepository;

@Service
public class LoginHistoryService {
	
	private final static Logger logger = Logger.getLogger(LoginHistoryService.class);
	
	@Autowired
	private LoginHistoryRepository logHistoryRepository;
	
	public LoginHistoryService(){
		logger.info("LoginHistoryService class Bean is created : ");
	}
	
	public LoginHistory save(LoginHistory loginHistory){
		logger.info("inside save method of LoginHistory :");
		try{
			return  logHistoryRepository.save(loginHistory);
		}
		catch(Exception exception){
			logger.error("error occured in save loginHistory object in database :"+exception);
			return null;
		}
	}
	
	public List<LoginHistory> findAllLoginHistory(){
		logger.info("inside findAll LoginHistory method of LoginHistory :");
		try{
			return logHistoryRepository.findAll();
		}
		catch(Exception exception){
			logger.error("error occured in fetching loginHistory object from database :"+exception);
			return null;
		}
	}
	
	public LoginHistory findByLoginHistoryId(Integer loginHistoryId){
		logger.info("inside find by Login History id method of LoginHistory :");
		try {
			return logHistoryRepository.findByLoginHistoryId(loginHistoryId);
		} catch (Exception e) {
			logger.error("error occured in fetching login History object by id from database :"+e);
			return null;
		}
	}

	public List<LoginHistory> getUserLoginHistory(User user,int limit){
		logger.info("inside getUserLoginHistory by user email :");
		try{
			logger.debug("fetching loginHistory for user with limit : ");
		return logHistoryRepository.userLoginHistory(user.getEmail(),new PageRequest(0, limit));
		}catch(Exception e){
			logger.error("Exception in fetching LoginHistory : ", e);
		    return null;
		}
	}

}
