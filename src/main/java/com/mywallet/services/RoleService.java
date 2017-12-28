package com.mywallet.services;

import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Role;
import com.mywallet.repository.RoleRepository;

@Service
public class RoleService {

	private final static Logger logger = Logger.getLogger(RoleService.class.getName());
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MyWalletConfig myWalletConfig;
	
	
	public RoleService(){
		logger.info("RoleService class Bean is created : ");
	}
	
	 @PostConstruct
	 public void init(){
		 initalizationData(myWalletConfig.getRoleName());
	 }
	 
	 public void initalizationData(String[] roleNames){
		 for(String roleName : roleNames){
		 System.out.println("get role name from roleName array :"+roleName);
		 Role role = new Role(roleName,"this is a role",true);
	     this.save(role);
		 }
	 }
	 
	public Role save(Role role){
		logger.info("inside save method of role :");
		try{
		return roleRepository.save(role);
		}
		catch(Exception exception){
			logger.error("Exception occur while save role in database :"+exception);
			return null;
		}
	}
	
	public List<Role> getAllRolefromDB(){
		logger.info("inside getAllRolefromDB method :");
		try{
			return roleRepository.findAll();	
		}catch (Exception e) {
			logger.error("Exception occur while fetching all roles from database : "+e);
			return null;
		}
	}
	
	public Role persistRole(Role role){
		logger.info("inside persistData method of role :");
		try{
		return roleRepository.saveAndFlush(role);
		}
		catch(Exception exception){
			logger.error("exception occured  in persistRole in database :"+exception);
			return null;
		}
	}
	
	public Role findByRoleName(String roleName){
		logger.info("inside findByRoleName method of role :");
		try{
		return roleRepository.findByRoleName(roleName);
		}
		catch(Exception exception){
			logger.error("exception occured in find By RoleId from database :"+exception);
			return null;
		}
	}
	
	public Role findByRoleId(Integer roleId){
		logger.info("inside find By RoleId method of role :");
		try{
		return roleRepository.findByRoleId(roleId);
		}
		catch(Exception exception){
			logger.error("exception occured in find By RoleName from database :"+exception);
			return null;
		}
	}
	


}
