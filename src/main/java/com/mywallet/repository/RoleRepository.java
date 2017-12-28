package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	
	Role findByRoleName(String roleName);
	Role findByRoleId(Integer roleId);
//	Boolean deleteByRoleId(Integer roleId);
}
