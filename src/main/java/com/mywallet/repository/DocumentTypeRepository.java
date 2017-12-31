package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.DocumentType;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer>{

	
	
//	   @Modifying
//    @Transactional
//    @Query("delete from DocumentType dt where dt.documentTypeID = ?1")
	int deleteByDocumentTypeID(Integer documentTypeID);
	
	DocumentType findByDocumentTypeID(Integer documentTypeID);
}
