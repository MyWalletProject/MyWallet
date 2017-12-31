package com.mywallet.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mywallet.config.MyWalletConfig;
import com.mywallet.domain.Document;
import com.mywallet.repository.DocumentRepository;

@Service
public class DocumentService {

private final static Logger logger = Logger.getLogger(DocumentService.class);
	

    @Autowired
	private DocumentRepository documentRepository;


	public Document save(Document document){
		logger.info("inside save method of Document service :");
		try {
			return documentRepository.save(document);
		} catch (Exception exception) {
			logger.error("save Document object in database "+exception);
			return null;
		}
	}

	public Document findByDocumentId(Integer documentId){
		logger.info("inside findByDocumentID method of Document service :");
		try {
			return documentRepository.findByDocumentId(documentId);
		} catch (Exception e) {
			logger.error("fetching Document object by find By DocumentID from database :"+e);
			return null;
		}

	}

	
	public List<Document> getAllDocument(){
		logger.debug("returing all Document list");
		try{
			return documentRepository.findAll();
		}catch(Exception e){
			logger.debug("Exception occure while fetch all Document from database : "+e);
			return null;			
		}
	}

	public Boolean deleteByDocumentId(Integer documentId){
		logger.debug("delete by document id : ");
		int deletedRow =0;
		try{
			deletedRow = documentRepository.deleteByDocumentId(documentId);
       System.out.println("deleted row"+ deletedRow);
			if(deletedRow > 0)
			   return true;
		}catch(Exception e){
			logger.debug("Exception occure while deleting Document by id from database : "+e);
			return false;	
		}
		return false;
	}
	
}
