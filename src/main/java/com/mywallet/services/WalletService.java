package com.mywallet.services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mywallet.domain.Wallet;
import com.mywallet.repository.WalletRepository;

@Service
public class WalletService {

	private static final Logger logger = LoggerFactory.getLogger(WalletService.class);
	
	@Autowired
	private WalletRepository walletRepository;

	
	public Wallet save(Wallet userWallet){
		logger.info("inside save method of userWallet service :");
		try {
			return walletRepository.save(userWallet);
		} catch (Exception exception) {
			logger.error("save wallet object in database "+exception);
			return null;
		}
	}

	public Wallet findByWalletId(Integer walletId){
		logger.info("inside findByWalletId method of Document service :");
		try {
			return walletRepository.findByWalletId(walletId);
		} catch (Exception e) {
			logger.error("error occured in find wallet object by Wallet Id from database :"+e);
			return null;
		}
	}
	
	public Wallet findByWalletBalance(Double walletBalance){
		logger.info("inside findByWalletBalance method of Document service :");
		try {
			return walletRepository.findByWalletBalance(walletBalance);
		} catch (Exception e) {
			logger.error("error occured in find wallet object by Wallet Id from database :"+e);
			return null;
		}
	}

		
	public List<Wallet> getAllWallet(){
		logger.debug("returing all wallet list");
		try{
			return walletRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all wallet from database : "+e);
			return null;			
		}
	}
	
	public String generateAddress(){
		return UUID.randomUUID().toString();
	}
}
