package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {

//	Boolean deleteByLoginHistoryId(Integer loginHistoryId);
	LoginHistory findByLoginHistoryId(Integer loginHistoryId);
	
}
