package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.CountryDocMapping;


@Repository
public interface CountryDocMappingRepository extends JpaRepository<CountryDocMapping, Integer>{

	CountryDocMapping findByCountryDocMappingId(Integer countryDocMappingId);
}
