package com.alteca.phenix_challenge.model;

import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\|")
public class Transaction {
	//txId|datetime|magasin|produit|qte
	
	@DataField(pos = 1)
	Integer txId;
	@DataField(pos = 2,pattern="yyyyMMdd'T'HHmmssZZZZZ")
	Date datetime;
	@DataField(pos = 3)
	String magasin;
	@DataField(pos = 4)
	Integer produit;
	@DataField(pos = 5)
	Integer qte;
	
	@Override
	public String toString() {
		return "Transaction [txId=" + txId + ", datetime=" + datetime + ", magasin=" + magasin + ", produit=" + produit
				+ ", qte=" + qte + "]";
	}

	public Integer getTxId() {
		return txId;
	}

	public void setTxId(Integer txId) {
		this.txId = txId;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getMagasin() {
		return magasin;
	}

	public void setMagasin(String magasin) {
		this.magasin = magasin;
	}

	public Integer getProduit() {
		return produit;
	}

	public void setProduit(Integer produit) {
		this.produit = produit;
	}

	public Integer getQte() {
		return qte;
	}

	public void setQte(Integer qte) {
		this.qte = qte;
	}
	
	
	
	
}
