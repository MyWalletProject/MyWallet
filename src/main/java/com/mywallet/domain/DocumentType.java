package com.mywallet.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class DocumentType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer documentTypeID;

	@NotNull(message="documentType can not be null")
	@NotEmpty(message="documentType can not be empty")
	@Column(unique=true)
	private String documentType;  //Identity Proof,Residental ,Income Proof,Other

	
	@OneToMany(mappedBy="documentType",cascade=CascadeType.ALL)
	private List<CountryDocMapping> countryDocMappingArray = new ArrayList<CountryDocMapping>();
	
	
	public DocumentType(){
		
	}
	
    public DocumentType(String documentType,List<CountryDocMapping> countryDocMappingArray){
    	this.documentType = documentType;
    	this.countryDocMappingArray = countryDocMappingArray;
 	}
    
	
	public List<CountryDocMapping> getCountryDocMappingArray() {
		return countryDocMappingArray;
	}

	public void setCountryDocMappingArray(List<CountryDocMapping> countryDocMappingArray) {
		this.countryDocMappingArray = countryDocMappingArray;
	}

	public DocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getDocumentTypeID() {
		return documentTypeID;
	}

	public void setDocumentTypeID(Integer documentTypeID) {
		this.documentTypeID = documentTypeID;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	@Override
	public String toString() {
		return "DocumentType [documentTypeID=" + documentTypeID + ", documentType=" + documentType + "]";
	}
	
	
}
