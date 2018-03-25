package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Token;
import com.mywallet.domain.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{

	
	@Query("select t.user from Token t where t.token=:tokenString")
	User getUserByTokenString(@Param ("tokenString") String tokenString);
	
	Token findByToken(String token);
	Token findByTokenId(Integer tokenId);
}
