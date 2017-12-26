package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Role;
import com.mywallet.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	
//	@Query(value="select COUNT(roleId)as total,COUNT(case when user.roleId=:adminId then 1 end) as admin,COUNT(case when user.roleid=:userid then 1 end) as users,COUNT(case when user.roleid=:merchantid then 1 end) as merchant,COUNT(case when user.is_active=1 then 1 end) as active_user from user",nativeQuery=true)
//	String getUserMeta(@Param("adminid") long adminid,@Param("merchantid")long merchantid,@Param("userid")long userid);
	
	@Query(value="select ",nativeQuery=true)
	
	User findByRole(Role role);
	User findByUserId(Integer userId);
	User findByUserName(String userName);
	User findByEmail(String email);
	User findByUserNameAndActive(String userName, Boolean isActive);
	User findByEmailAndPassword(String email, String password);
	User findByPassword(String password);
	Boolean deleteUserByUserId(Integer userId);
	Boolean findByActive(Boolean isActive);
}
