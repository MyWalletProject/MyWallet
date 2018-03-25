package com.mywallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.CountryDocMapping;


@Repository
public interface CountryDocMappingRepository extends JpaRepository<CountryDocMapping, Integer>{

	CountryDocMapping findByCountryDocMappingId(@Param("countryID") Integer countryID);
//	select   year,   total,ifnull(verified,0) as verified from   (     select       year(default_country_kycdate) as year,       count(*) as total     from       user     where       year(default_country_kycdate) between 2011       and 2018     group by       year(default_country_kycdate)   ) as tbl1   left join (     select       count(*) as verified,       year(default_country_kycdate) as year2     from       user     where       iskyc_verified = true     group by       year(default_country_kycdate)   ) tbl2 on year = year2
	@Query("from CountryDocMapping as cdm where cdm.country.countryID=:countryID")
	List<CountryDocMapping> getAllCountryDocMappingByCountryID(@Param("countryID") Integer countryID);

	@Query("from CountryDocMapping as cdm where cdm.country.countryName=:countryName")
	List<CountryDocMapping> getAllCountryDocMappingByCountryName(@Param("countryName") String countryName);
	
}
