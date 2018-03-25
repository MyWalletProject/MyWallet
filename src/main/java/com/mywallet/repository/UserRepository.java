package com.mywallet.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.Role;
import com.mywallet.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	
	User findByDefaultAddressId(Integer defaultAddressId);
	User findByRole(Role role);
	User findByUserId(Integer userId);
	User findByUserName(String userName);
	User findByEmail(String email);
	User findByUserNameAndActive(String userName, Boolean isActive);
	User findByEmailAndPassword(String email, String password);
	User findByPassword(String password);
	Boolean deleteUserByUserId(Integer userId);
	Boolean findByActive(Boolean isActive);
	
//	@Query("select w.user from Wallet w where w.user.isKYCVerified=:isKYCVerified")
	Boolean findByIsKYCVerified(Boolean isKYCVerified);	
	
	@Query("select count(userId) from User")
	Integer getTotalUserCount();
	
	@Query("select count(userId) from User u where u.role.roleName=:roleName")
	Integer getTotalMerchantCount(@Param("roleName")String roleName);
	
	@Query("select count(userId) from User u where u.active=true")
	Integer getTotalActiveUserCount();

	
	@Query(value="select year, total, ifnull(verified,0) as verified from (select year(kyc_verification_date) as year,count(*) as total from user where year(kyc_verification_date) between :startYear and :endYear group by year(kyc_verification_date)) as tbl1 left join (select count(*) as verified, year(kyc_verification_date) as year2 from user where iskycverified = true group by year(kyc_verification_date))tbl2 on year = year2",nativeQuery=true)
	List<Object[]> getYearlyKYCStatusGraphData(@Param("startYear")Integer startYear,@Param("endYear") Integer endYear);
	
//	@Query(value="select year(kycVerificationDate) as year,count(*) as total from user where year(kycVerificationDate) between :startYear and :endYear and iskyc_verified=:isKYCVerified group by year(kycVerificationDate) order by year",nativeQuery=true)
//	List<Object[]> getYearlyKYCStatusGraphData(@Param("startYear")Integer startYear,@Param("endYear") Integer endYear,@Param("isKYCVerified") boolean isKYCVerified);

}
