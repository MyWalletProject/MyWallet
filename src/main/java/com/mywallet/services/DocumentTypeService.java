package com.mywallet.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mywallet.domain.DocumentType;
import com.mywallet.repository.DocumentTypeRepository;

@Service
public class DocumentTypeService {
  
	
private final static Logger logger = Logger.getLogger(DocumentTypeService.class);
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	public DocumentType save(DocumentType documentType){
		logger.info("inside save method of DocumentType service :");
		try {
			return documentTypeRepository.save(documentType);
		} catch (Exception exception) {
			logger.error("save DocumentType object in database "+exception);
			return null;
		}
	}

	public DocumentType findByDocumentTypeID(Integer documentTypeID){
		logger.info("inside find By Document Type ID method of DocumentType service :");
		try {
			return documentTypeRepository.findByDocumentTypeID(documentTypeID);
		} catch (Exception e) {
			logger.error("fetching DocumentType object by DocumentTypeID from database :"+e);
			return null;
		}

	}

	public List<DocumentType> getAllDocumentType(){
		logger.debug("returing all documentType list");
		try{
			return documentTypeRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all DocumentType from database : "+e);
			return null;			
		}
	}

}
