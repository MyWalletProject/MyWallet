package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{

	Country findByCountryID(Integer countryID);
	Country findByCountryNameAndCountryCodeAndCountryDialCode(String countryName,String countryCode,String countryDialCode);
}
