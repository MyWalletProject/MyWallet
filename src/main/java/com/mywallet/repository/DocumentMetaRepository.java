package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.DocumentMeta;

@Repository
public interface DocumentMetaRepository extends JpaRepository<DocumentMeta, Integer>{

	 int deleteByDocumentMetaID(Integer documentMetaID);
	 DocumentMeta findByDocumentMetaID(Integer documentMetaID);
}
