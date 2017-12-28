package com.mywallet.domain.req;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class Req_DocumentMeta {

	
	@NotNull(message="documentName can not be null")
	@NotEmpty(message="documentName can not be empty")
	public String documentName;

	@NotNull(message="description can not be null")
	@NotEmpty(message="description can not be empty")
	public String description;
	
	public boolean isMandatory;

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
	
	
}
