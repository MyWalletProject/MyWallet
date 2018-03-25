package com.mywallet.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {

//	Boolean deleteByLoginHistoryId(Integer loginHistoryId);
	LoginHistory findByLoginHistoryId(Integer loginHistoryId);
	
	
	//@Query(value = "select * from login_history where userid =:userid ORDER BY login_time DESC limit 5", nativeQuery = true)
			@Query("select lh from LoginHistory lh where lh.user.email=:email ORDER BY lh.loginTime DESC")
			List<LoginHistory> userLoginHistory(@Param("email") String email,Pageable pageSize);

}
