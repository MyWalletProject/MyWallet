package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Role;
import com.mywallet.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

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
