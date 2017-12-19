package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {

//	Boolean deleteLoginHistory(LoginHistory loginHistory);
	LoginHistory findByLoginHistoryId(Integer loginHistoryId);
	
}
