package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;


public class Req_CountryDocMapping {

	@NotNull(message="countryID can not be null")
	public Integer countryID;
	
	@NotNull(message="documentTypeID can not be null")
	public Integer documentTypeID;
	
	@NotNull(message="documentMetaID can not be null")
	public  Integer documentMetaID;

	public Boolean isMandatory;

	public Integer getCountryID() {
		return countryID;
	}

	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}

	public Integer getDocumentTypeID() {
		return documentTypeID;
	}

	public void setDocumentTypeID(Integer documentTypeID) {
		this.documentTypeID = documentTypeID;
	}

	public Integer getDocumentMetaID() {
		return documentMetaID;
	}

	public void setDocumentMetaID(Integer documentMetaID) {
		this.documentMetaID = documentMetaID;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

}
