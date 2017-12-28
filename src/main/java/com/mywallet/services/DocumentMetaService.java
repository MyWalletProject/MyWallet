package com.mywallet.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.domain.DocumentMeta;
import com.mywallet.repository.DocumentMetaRepository;

@Service
public class DocumentMetaService {

private final static Logger logger = Logger.getLogger(DocumentMetaService.class);
	

    @Autowired
	private DocumentMetaRepository documentMetaRepository;


	public DocumentMeta save(DocumentMeta documentMeta){
		logger.info("inside save method of DocumentMeta service :");
		try {
			return documentMetaRepository.save(documentMeta);
		} catch (Exception exception) {
			logger.error("save DocumentMeta object in database "+exception);
			return null;
		}
	}

	public DocumentMeta findByDocumentMetaID(Integer documentMetaID){
		logger.info("inside findByDocumentMetaID method of DocumentMeta service :");
		try {
			return documentMetaRepository.findByDocumentMetaID(documentMetaID);
		} catch (Exception e) {
			logger.error("fetching DocumentMeta object by find By DocumentMetaID from database :"+e);
			return null;
		}

	}

	
	public List<DocumentMeta> getAllDocumentMeta(){
		logger.debug("returing all DocumentMeta list");
		try{
			return documentMetaRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all DocumentMeta from database : "+e);
			return null;			
		}
	}

}
