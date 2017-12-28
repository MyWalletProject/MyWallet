package com.mywallet.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.domain.Country;
import com.mywallet.repository.CountryRepository;

@Service
public class CountryService {
	
	private final static Logger logger = Logger.getLogger(CountryService.class);
	
	@Autowired
	private CountryRepository countryRepository;
	
	public Country save(Country country){
		logger.info("inside save method of country service :");
		try {
			return countryRepository.save(country);
		} catch (Exception exception) {
			logger.error("save country object in database "+exception);
			return null;
		}
	}

	public Country findByCountryID(Integer countryID){
		logger.info("inside findByCountryID method of country service :");
		try {
			return countryRepository.findByCountryID(countryID);
		} catch (Exception e) {
			logger.error("fetching country object by findByCountryID from database :"+e);
			return null;
		}

	}

	public Country findByCountryNameAndCountryCodeAndCountryDialCode(String countryName,String countryCode,String countryDialCode){
		logger.info("inside find By CountryName And CountryCode And CountryDialCode method of country service :");
		try {
			return countryRepository.findByCountryNameAndCountryCodeAndCountryDialCode(countryName,countryCode,countryDialCode);
		} catch (Exception e) {
			logger.error("fetching country object by CountryName And CountryCode And CountryDialCode from database :"+e);
			return null;
		}

	}

	public List<Country> getAllCountry(){
		logger.debug("returing all countries list");
		try{
			return countryRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all Countries from database : "+e);
			return null;			
		}
	}

}
