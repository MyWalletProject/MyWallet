package com.mywallet.services;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mywallet.domain.Address;
import com.mywallet.repository.AddressRepository;

@Service
public class AddressService {
	
	private final static Logger logger = Logger.getLogger(AddressService.class.getName());
	
	@Autowired
	private AddressRepository addressRepository;
	
	public AddressService(){
		logger.info("AddressService class Bean is created : ");
	}
	
	public Address save(Address address)throws DataIntegrityViolationException,ConstraintViolationException{
		logger.info("inside save method of address :");
		try{
		return addressRepository.save(address);
		}
		catch(DataIntegrityViolationException dataIntegrity){
			logger.error("error msg of DataIntegrityViolationException :"+dataIntegrity);
			throw dataIntegrity;
		}
		catch(ConstraintViolationException constraint){
			logger.error("error msg of ConstraintViolationException :"+constraint);
			throw constraint;
		}
		catch(Exception exception){
			logger.error("error msg to save address in database :"+exception);
			return null;
		}
	}
	
	public List<Address> getAllAddressfromDB(){
		logger.info("inside getAllAddressfromDB method :");
		try{
			return addressRepository.findAll();	
		}catch (Exception e) {
			logger.error("Exception occur while fetching all Addresses : "+e);
			return null;
		}
	}
	
	public Address persistAddress(Address address){
		logger.info("inside persistData method of Address :");
		try{
		return addressRepository.saveAndFlush(address);
		}
		catch(Exception exception){
			logger.error("save address in database :");
			return null;
		}
	}
	
	public Address findByAddressId(Integer addressId){
		logger.info("inside findByAddressId method of Address :");
		try{
		return addressRepository.findByAddressId(addressId);
		}
		catch(Exception exception){
			logger.error("fetch address by address id from database :");
			return null;
		}
	}
	
	@Transactional
	public Boolean deleteAddressByAddressId(Integer addressId){
		logger.info("inside delete Address By Address Id method :");
		try{
		 addressRepository.deleteAddressByAddressId(addressId);
		 return true;
		}
		catch(Exception exception){
		logger.error("delete Address by Address id in database :");
			return false;
		}
	}

}
