package com.mywallet.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Role;
import com.mywallet.domain.User;
import com.mywallet.repository.UserRepository;


@Service
public class UserService {
	
	private final static Logger logger = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Autowired
	private MyWalletConfig myWalletConfig;

	@Autowired
	private RoleService roleService;

	
	public UserService(){
		logger.info("UserService class Bean is created : ");
	}
	
	@PostConstruct
	public void init(){
		printIPAddress();
		createDefaultAdminUser();
	}

	void printIPAddress(){
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			System.out.println("*************************************IP Address***********************************************");
			logger.info("Current IP address : " + ip.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	void createDefaultAdminUser(){

		Boolean active = myWalletConfig.getActive();
		String email = myWalletConfig.getEmail();
		String password =	myWalletConfig.getPassword();
		Boolean isKYCVerified =	myWalletConfig.getIsKYCVerified();
		String role = myWalletConfig.getRole();
		String username = myWalletConfig.getUsername();

		if(active == null || email == null || password == null || isKYCVerified == null || role == null || username == null)
		{
			logger.error("Default admin user creation fields missing : Deault admin user not created ");
			return;
		}
		User alreadyExist = findByEmail(email.trim());
		if(alreadyExist != null){
			logger.info("Admin user : "+email+" already exist**********8");
			return;
		}

		Role adminRole = roleService.findByRoleName(role.trim());
		if(adminRole == null){
			logger.error("RoleName : "+role+" not found admin user not created");
			return;
		}

		User user = new User(username.trim(),email.trim(),password.trim(),active,true,isKYCVerified,new Date(),new Date());
		user.setRole(adminRole);
		user = save(user);
		if(user == null){
			logger.error("************Default Admin User not created ***************");
		}

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
	
	public Boolean findByIsKYCVerified(Boolean isKYCVerified)throws IncorrectResultSizeDataAccessException,NonUniqueResultException {
		logger.info("inside findIsKYCVerified method OF USER SERVICE :");
		try{
			 userRepository.findByIsKYCVerified(isKYCVerified);
			 return true;
		}
		catch(IncorrectResultSizeDataAccessException e){
			logger.error("IncorrectResultSizeDataAccessException Exception is found by this isKYCVerified key"+e);
			return false;
		}
		catch(NonUniqueResultException except){
			logger.error("NonUniqueResult Exception is found by this isKYCVerified key"+except);
			return false;
		}
		catch(Exception exception){
			logger.error("NO USER object is found by this isKYCVerified key"+exception);
			return false;
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
			logger.error("user not fetch from  database by user id : "+exception);
			return null;
		}
	}
	
	@Transactional
	public User findByDefaultAddressId(Integer defaultAddressId) throws ClassCastException{
		logger.info("inside find By DefaultAddressId method of user servive :");
		try{
		return userRepository.findByDefaultAddressId(defaultAddressId);
		}
		catch(ClassCastException e){
			logger.error("user not fetch from  database by DefaultAddressId : "+e.getMessage());
			return null;
		}
		
		catch(Exception exception){
			logger.error("user not fetch from  database by DefaultAddressId : "+exception.getClass());
			return null;
		}
	}
	
	
	public List<Object[]> getYearlyKYCStatusGraphData(Integer startYear,Integer endYear)throws InvalidDataAccessApiUsageException,InvalidDataAccessResourceUsageException{
		logger.info("inside getYearlyKYCStatusGraphData method of user servive :");
		try {
			return userRepository.getYearlyKYCStatusGraphData(startYear, endYear);
		} 
		catch (IllegalArgumentException ex) {
			throw new InvalidDataAccessApiUsageException(ex.getMessage());
		}
		
		catch (SQLGrammarException sqlGram) {
			throw new InvalidDataAccessApiUsageException(sqlGram.getMessage());
		}
		
		
		catch (Exception e) {
			logger.error("user not fetch YearlyKYCStatusGraphData from  database : "+e);
			return null;
		}
	}
	

	public Map<String,Object> loginUser(){
		return null;
	}
	
	public Map<String,Object> getTotalUsersCount(){

		Map<String,Object> response = new HashMap<String,Object>();
		response.put("totalUsers", getTotalUserCount());
		response.put("activeUsers", getTotalActiveUserCount());
		response.put("merchantUsers", getTotalMerchantCount("merchant"));
		return response;
	}

	Integer getTotalUserCount(){
		try{
			return userRepository.getTotalUserCount();
		}catch (Exception e) {
			logger.error("Exception occur while fetch total users count : ",e);
			return null;
		}
	}
	Integer getTotalActiveUserCount(){
		try{
			return userRepository.getTotalActiveUserCount();
		}catch (Exception e) {
			logger.error("Exception occur while fetch total active users count : ",e);
			return null;
		}
	}
	Integer getTotalMerchantCount(String roleName){
		try{
			return userRepository.getTotalMerchantCount(roleName);
		}catch (Exception e) {
			logger.error("Exception occur while fetch merchant users count : ",e);
			return null;
		}
	}
}
