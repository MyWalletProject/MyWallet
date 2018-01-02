package com.mywallet.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.mywallet.util.ObjectHash;


@Entity
@Table(
		uniqueConstraints=
		@UniqueConstraint(columnNames={"countryid", "document_typeid","document_metaid"})
		)
public class CountryDocMapping {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer countryDocMappingId; 

	@ObjectHash
	@ManyToOne
	@JoinColumn(name = "countryid")
	private Country country;

	@ObjectHash
	@ManyToOne
	@JoinColumn(name = "document_typeid")
	private DocumentType documentType;

	@ObjectHash
	@ManyToOne
	@JoinColumn(name = "document_metaid")
	private DocumentMeta documentMeta;

	private Boolean isMandatory = false;

	public CountryDocMapping(){

	}

	public CountryDocMapping(Country country, DocumentType documentType, DocumentMeta documentMeta,
			Boolean isMandatory) {

		this.country = country;
		this.documentType = documentType;
		this.documentMeta = documentMeta;
		this.isMandatory = isMandatory;
	}

	public Integer getCountryDocMappingId() {
		return countryDocMappingId;
	}

	public void setCountryDocMappingId(Integer countryDocMappingId) {
		this.countryDocMappingId = countryDocMappingId;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentMeta getDocumentMeta() {
		return documentMeta;
	}

	public void setDocumentMeta(DocumentMeta documentMeta) {
		this.documentMeta = documentMeta;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

}
