package com.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
	  public static <K, V extends Comparable<? super V>> Map<K, V> sortMap(final Map<K, V> mapToSort) {
			List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(mapToSort.size());
	 
			entries.addAll(mapToSort.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
				@Override
				public int compare(final Map.Entry<K, V> entry2, final Map.Entry<K, V> entry1) {
					int i=entry1.getValue().compareTo(entry2.getValue());
					return i;
				}
			});
	 
			Map<K, V> sorted = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : entries) {
				sorted.put(entry.getKey(), entry.getValue());
			}
	 
			return sorted;
		}	
}
