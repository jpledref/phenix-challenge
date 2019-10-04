package com.alteca.phenix_challenge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.google.common.collect.MinMaxPriorityQueue;

public class App {
	final static Logger LOG = LoggerFactory.getLogger(App.class);
	static Main main;
	static long start;
	static long finish;
	
    public static void main( String[] args ) throws Exception{
    	main = MyMain.getInstance();  
    	main.addRouteBuilder(new MyRoutes());    	

		System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
		start = System.currentTimeMillis();
		main.run();
    }
        
    public static void shutdown(){
    	if(main!=null)
			try {
				ProducerTemplate pt=main.getCamelContexts().get(0).createProducerTemplate();
				Comparator<Entry<Integer, Integer>> comparator = Entry.comparingByValue(Comparator.reverseOrder());
				Comparator<Entry<Integer, Double>> comparatorD = Entry.comparingByValue(Comparator.reverseOrder());
				
				//top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data				
				DataStore.getVentesProduitMap().entrySet()
				.stream()
				.forEach(jm->{	
					String jour=jm.getKey();					
					jm.getValue().entrySet().stream().forEach(mp->{
						String magasin=mp.getKey();	
						String filename="top_100_ventes_"+magasin+"_"+jour+".data";	
						
						//GUAVA: Using it seems to be less effective
						/*MinMaxPriorityQueue<Entry<Integer,Integer>> tops = MinMaxPriorityQueue.orderedBy(comparator)
						        .maximumSize(100)
						        .create(mp.getValue().entrySet());						
						Map<Integer,Integer> map=(Map<Integer, Integer>) tops.stream()
								.sorted(comparator)
								.collect(Collectors.toMap(Map.Entry::getKey, 
					                Map.Entry::getValue,
					                (m1, m2) -> m1,
					                LinkedHashMap::new));*/
						
						// Map des ventes
						Map<Integer, Integer> input=mp.getValue();
						
						// Sort descending O(n*log(n)) 
						List<Map.Entry<Integer, Integer>> list = new ArrayList<>(input.entrySet());
						list.sort(comparator);

						// Select top entries
						Map<Integer, Integer> map = new LinkedHashMap<>();
						list.subList(0, Math.min(100, list.size()))
						    .forEach(e -> map.put(e.getKey(), e.getValue()));
												
						pt.sendBodyAndHeader("direct:save2File", map.toString(), Exchange.FILE_NAME, filename);
					});
				});   
				
				//top_100_ca_<ID_MAGASIN>_YYYYMMDD.data
				DataStore.getVentesProduitMap().entrySet()
				.stream()
				.forEach(jm->{	
					String jour=jm.getKey();		
					jm.getValue().entrySet().stream().forEach(mp->{
						String magasin=mp.getKey();	
						String filename="top_100_ca_"+magasin+"_"+jour+".data";	
												
						// Map de CA
						Map<Integer, Double> input=mp.getValue().entrySet().stream().collect(Collectors.toMap(
								k -> {
									Integer res=(Integer)((Entry)k).getKey();								
								   return res;
								},
								v -> {									
									Double prix=DataStore.getRefProduitMap().get(jour).get(magasin).get(v.getKey());
									if(prix==null)prix=0.0;									
									Double ca=v.getValue()*prix;									
									return ca;
								}
								));												
						
						// Sort descending
						List<Map.Entry<Integer, Double>> list = new ArrayList<>(input.entrySet());
						list.sort(comparatorD);

						// Select top entries
						Map<Integer, Double> map = new LinkedHashMap<>();
						list.subList(0, Math.min(100, list.size()))
						    .forEach(e -> map.put(e.getKey(), e.getValue()));
						
						pt.sendBodyAndHeader("direct:save2File", map.toString(), Exchange.FILE_NAME, filename);
					});
					
				}); 				
				
				finish = System.currentTimeMillis();
				long timeElapsed = finish - start;				
				LOG.info("==> Time elapsed:{}ms",timeElapsed);
				main.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }    
    
    
  
    
    
}
