package com.alteca.phenix_challenge;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.ExplicitCamelContextNameStrategy;
import org.apache.camel.main.Main;


public class MyMain extends Main {

	private MyMain() {
		super();
	}
	
	@Override
	protected CamelContext createContext() {
		LOG.info("createContext() with NameStrategy");
		CamelContext ret = super.createContext();
		ret.setNameStrategy(new ExplicitCamelContextNameStrategy("camel-phenix"));
		return ret;
	}
	
	public static Main getInstance() {
        if (instance == null) {
        	synchronized (MyMain.class) {
        		if (instance == null) {
        			instance = new MyMain();
        		}
			}
        }
        return instance;
    }

}
