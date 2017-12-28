package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_DocumentType {

	@NotNull(message="documentType can not be null")
	@NotEmpty(message="documentType can not be empty")
	public String documentType;                        //Identity Proof,Residental ,Income Proof,Other

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
}
