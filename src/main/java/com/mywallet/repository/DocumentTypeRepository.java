package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.DocumentType;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer>{

	DocumentType findByDocumentTypeID(Integer documentTypeID);
}
