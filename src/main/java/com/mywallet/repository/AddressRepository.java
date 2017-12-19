package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{


	Address findByAddressId(Integer addressId);
	Boolean deleteAddressByAddressId(Integer addressId);
}
