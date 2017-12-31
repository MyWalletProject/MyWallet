package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mywallet.domain.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer>{

	int deleteByDocumentId(Integer documentId);
	Document findByDocumentId(Integer documentId);
}
