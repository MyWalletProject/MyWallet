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
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class DocumentMeta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer documentMetaID; 
	
	@Pattern(regexp="[a-zA-Z]+",message="documentName only alphabets")
	@NotNull(message="documentName can not be null")
	@NotEmpty(message="documentName can not be empty")
	@Column(unique=true)
	private String documentName;

	@NotNull(message="description can not be null")
	@NotEmpty(message="description can not be empty")
	private String description;
	
	private boolean isMandatory=false;
	
	
	@OneToMany(mappedBy="documentMeta",cascade=CascadeType.ALL)
	private List<CountryDocMapping> countryDocMappingArray = new ArrayList<CountryDocMapping>();
	

	public DocumentMeta(){
		
	}
	
	public DocumentMeta( String documentName, String description, boolean isMandatory) {
		
		this.documentName = documentName;
		this.description = description;
		this.isMandatory = isMandatory;
	}
	
	
public DocumentMeta( String documentName, String description, boolean isMandatory,List<CountryDocMapping> countryDocMappingArray) {
		
		this.documentName = documentName;
		this.description = description;
		this.isMandatory = isMandatory;
		this.countryDocMappingArray=countryDocMappingArray;
	}
	

	public List<CountryDocMapping> getCountryDocMappingArray() {
		return countryDocMappingArray;
	}

	public void setCountryDocMappingArray(List<CountryDocMapping> countryDocMappingArray) {
		this.countryDocMappingArray = countryDocMappingArray;
	}

	public Integer getDocumentMetaID() {
		return documentMetaID;
	}

	public void setDocumentMetaID(Integer documentMetaID) {
		this.documentMetaID = documentMetaID;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	@Override
	public String toString() {
		return "DocumentMeta [documentMetaID=" + documentMetaID + ", documentName=" + documentName + ", description="
				+ description + ", isMandatory=" + isMandatory + "]";
	}
	
}
