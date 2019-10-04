package com.alteca.phenix_challenge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.alteca.phenix_challenge.model.RefProduit;
import com.alteca.phenix_challenge.model.Transaction;

public class DataStore {

	private static DataStore instance;	
	public static DataStore getInstance() {
        if (instance == null) {
        	synchronized (DataStore.class) {
        		if (instance == null) {
        			instance = new DataStore();
        		}
			}
        }
        return instance;
    }
	
	//Référentiel Produit jour,magasin,produit->prix 
	static Map<String,Map<String,Map<Integer,Double>>> refProduitMap= new ConcurrentHashMap<String,Map<String,Map<Integer,Double>>>();
		
	//Ventes Produit jour,magasin,produit->cumul 
	static Map<String,Map<String,Map<Integer,Integer>>> ventesProduitMap= new ConcurrentHashMap<String,Map<String,Map<Integer,Integer>>>();
	
	public static void addRef(String jour,String magasin,List<RefProduit> refProduitList){		
		Map<Integer, Double> map = refProduitList.stream()
			      .collect(Collectors.toMap(RefProduit::getProduit, RefProduit::getPrix));
		
		refProduitMap.computeIfAbsent(jour, k -> new HashMap<>())
		.computeIfAbsent(magasin, k -> new HashMap<>())
		.putAll(map);		
	}
	
	public static void addSale(String jour,List<Transaction> transactionList){		
		Map<String,Map<Integer,Integer>> map=transactionList.stream().collect(
				Collectors.groupingBy(
					Transaction::getMagasin,Collectors.groupingBy(
								Transaction::getProduit,Collectors.summingInt(Transaction::getQte)
							)
					)
				);		
					
		ventesProduitMap.computeIfAbsent(jour, k -> new HashMap<>())
		.putAll(map);		
	}
	
	public static void computeResults(){
		
		
		//ventesProduitMap.entrySet().stream().;
		System.out.println("Hello");
		
	}

	public static Map<String, Map<String, Map<Integer, Double>>> getRefProduitMap() {
		return refProduitMap;
	}

	public static Map<String, Map<String, Map<Integer, Integer>>> getVentesProduitMap() {
		return ventesProduitMap;
	}
	
	
	
	
}
