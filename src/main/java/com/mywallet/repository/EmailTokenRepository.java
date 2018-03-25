package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.EmailToken;
import com.mywallet.domain.User;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Integer> {

	@Query("select t.user from EmailToken t where t.emailToken=:emailToken")
	User getUserByEmailToken(@Param ("emailToken") String emailToken);
	
	
	@Query("select t.user from EmailToken t where t.user=:user")
	User saveRegisteredUser(@Param ("user") User user);
	
	EmailToken findByEmailTokenId(Integer emailTokenId);
	EmailToken findByEmailToken(String emailToken);
}
