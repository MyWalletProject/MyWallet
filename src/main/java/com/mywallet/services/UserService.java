package com.mywallet.services;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Role;
import com.mywallet.domain.User;
import com.mywallet.repository.UserRepository;
import com.mywallet.util.ResponseUtil;

@Transactional
@Service
public class UserService {
	
	private final static Logger logger = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private UserRepository userRepository;
	
	public UserService(){
		logger.info("UserService class Bean is created : ");
	}
	
	public User persistUser(User user){
		System.out.println("user.getAddressArray()"+user.getAddressArray().size());
		logger.info("inside persistUser UserDB method :");
		try{user.setEmailVerified(true);
			
			return userRepository.saveAndFlush(user);
		}
		catch(Exception exception){
			logger.error("Save  persistUser in database : "+exception);
			return null;
		}
	}
	
	public User findByRole(Role role){
		logger.info("inside findByRole method :");
		try{
			return userRepository.findByRole(role);
		}
		catch(Exception exception){
			logger.error("no role object is found"+exception);
			return null;
		}
	}
	
	
	public User save(User user)throws DataIntegrityViolationException{
		logger.info("inside create UserDB method :");
		try{
			return userRepository.save(user);
			
		}catch(DataIntegrityViolationException de){
			logger.error("user already exist with email in database : "+de);
			throw de;
		//	return null;
		}
		catch(Exception exception){
			logger.error("Save  users in database : "+exception.getMessage());
			return null;
		}

	}
	
	public List<User> getAllUserfromDB(){
		logger.info("inside getAllUserfromDB method :");
		try{
			return userRepository.findAll();	
		}catch (Exception e) {
			logger.error("Exception occur while fetching all users : "+e);
			return null;
		}
	}
	
	public User findByUserName(String userName){
		logger.info("Enter the user name in the database  :");
		try {
			return this.userRepository.findByUserName(userName);
		} catch (Exception exception) {
			logger.error("No user name in the database",exception);
			return null;
		}
		
	}
	
	public User findByEmail(String email){
		logger.info("Enter the find by email user email from the database  :");
		try{
			return  userRepository.findByEmail(email);
		}
		catch(Exception exception){
			logger.error("No email found from database :"+exception);
			return null;
		}
	}
	
	
	public User findByEmailAndPassword(String email, String password) {
		logger.info("inside findByRoleNameAndIsActive method :");
		try{
			return	userRepository.findByEmailAndPassword(email,password);
		}
		catch(Exception exception){
			logger.error("user not fetch from  database by email and password : "+exception);
			return null;
		}
	}
	
	public Boolean findIsActive(Boolean isActive){
		logger.info("inside isActive method :");
		try{
		 	 userRepository.findByActive(isActive); 
		 	return true; 
		}
		catch(Exception exception){
			
			logger.error("NO Object isActive from database :"+exception);
			return false;
		}
		
	}
	
	
	public User findByPassword(String password){
		logger.info("Enter the user password in the database  :");
		try {
			return this.userRepository.findByPassword(password);
		} catch (Exception exception) {
			logger.error("No user password in the database",exception);
			return null;
		}
		
	}
	
	@Transactional
	public Boolean deleteUserByUserId(Integer userId){
		logger.info("inside deleteUserByUserId method :");
		try{
		 	 userRepository.deleteUserByUserId(userId); 
		 	return true; 
		}
		catch(Exception exception){
			
			logger.error("NO Object Deleted from database :"+exception);
			return false;
		}
		
	}

	public User findByUserNameAndIsActive(String userName, Boolean isActive) {
		logger.info("inside findByRoleNameAndActive method :");
		try{
			return	userRepository.findByUserNameAndActive(userName,isActive);
		}
		catch(Exception exception){
			logger.error("user not fetch from  database by usereName and active : "+exception);
			return null;
		}
	}

	public User findByUserId(Integer userId) {
		logger.info("inside findByUserId method :");
		try{
			return	userRepository.findByUserId(userId);
		}
		catch(Exception exception){
			logger.error("user not fetch from  database by usereName and active : "+exception);
			return null;
		}
	}

	public Map<String,Object> loginUser(){
		return null;
	}
	
}
