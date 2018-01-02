package com.mywallet.services;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.mywallet.domain.CountryDocMapping;
import com.mywallet.repository.CountryDocMappingRepository;


@Service
public class CountryDocMappingService {

private final static Logger logger = Logger.getLogger(CountryDocMappingService.class);
	

    @Autowired
	private CountryDocMappingRepository countryDocMappingRepository;

    
    @Transactional
	public CountryDocMapping save(CountryDocMapping countryDocMapping) throws DataIntegrityViolationException{
		logger.info("inside save method of CountryDocMapping service :");
		try {
			return countryDocMappingRepository.save(countryDocMapping);
		} catch(DataIntegrityViolationException d){
			throw d;
		}
		
		catch (Exception exception) {
			logger.error("save CountryDocMapping object in database "+exception);
			return null;
		}
	}

	public CountryDocMapping findByCountryDocMappingId(Integer countryDocMappingId){
		logger.info("inside findByCountryDocMappingID method of CountryDocMapping service :");
		try {
			return countryDocMappingRepository.findByCountryDocMappingId(countryDocMappingId);
		} catch (Exception e) {
			logger.error("fetching CountryDocMapping object by find By CountryDocMappingID from database :"+e);
			return null;
		}

	}

	
	public List<CountryDocMapping> getAllCountryDocMapping(){
		logger.debug("returing all CountryDocMapping list");
		try{
			return countryDocMappingRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all CountryDocMapping from database : "+e);
			return null;			
		}
	}


	
}
