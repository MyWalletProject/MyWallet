package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer>{

	
	Wallet findByWalletBalance(Double walletBalance);
	Wallet findByWalletId(Integer walletId);
}
