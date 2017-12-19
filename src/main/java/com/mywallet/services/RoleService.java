package com.mywallet.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.domain.Role;
import com.mywallet.repository.RoleRepository;

@Service
public class RoleService {

	private final static Logger logger = Logger.getLogger(RoleService.class.getName());
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Role save(Role role){
		logger.info("inside save method of role :");
		try{
		return roleRepository.save(role);
		}
		catch(Exception exception){
			logger.error("save role in database :");
			return null;
		}
	}
	
	public List<Role> getAllRolefromDB(){
		logger.info("inside getAllRolefromDB method :");
		try{
			return roleRepository.findAll();	
		}catch (Exception e) {
			logger.error("Exception occur while fetching all roles : "+e);
			return null;
		}
	}
	
	public Role persistRole(Role role){
		logger.info("inside persistData method of role :");
		try{
		return roleRepository.saveAndFlush(role);
		}
		catch(Exception exception){
			logger.error("save role in database :");
			return null;
		}
	}
	
	public Role findByRoleName(String roleName){
		logger.info("inside findByRoleName method of role :");
		try{
		return roleRepository.findByRoleName(roleName);
		}
		catch(Exception exception){
			logger.error("save role in database :");
			return null;
		}
	}
	
	public Role findByRoleId(Integer roleId){
		logger.info("inside find By RoleId method of role :");
		try{
		return roleRepository.findByRoleId(roleId);
		}
		catch(Exception exception){
			logger.error("save role in database :");
			return null;
		}
	}
	
	public Boolean deleteRoleByRoleId(Integer roleId){
		logger.info("inside delete Role By RoleId method :");
		try{
		return roleRepository.deleteRoleByRoleId(roleId);
		}
		catch(Exception exception){
		logger.error("delete role by role id in database :");
			return null;
		}
	}
}
