package com.alteca.phenix_challenge.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\|")
public class RefProduit {
	//produit|prix
	@DataField(pos = 1)
	Integer produit;
	@DataField(pos = 2)
	Double prix;
	
	@Override
	public String toString() {
		return "RefProduit [produit=" + produit + ", prix=" + prix + "]";
	}

	public Integer getProduit() {
		return produit;
	}

	public void setProduit(Integer produit) {
		this.produit = produit;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}
	
	
	
}
