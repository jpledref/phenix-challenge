package com.alteca.phenix_challenge;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

public class MyRoutes  extends RouteBuilder{

	@Override
	public void configure() throws Exception {         
         DataFormat bindy = new BindyCsvDataFormat(com.alteca.phenix_challenge.model.RefProduit.class);
         DataFormat bindy2 = new BindyCsvDataFormat(com.alteca.phenix_challenge.model.Transaction.class);
		
		from("file://data?include=.*data$&sendEmptyMessageWhenIdle=true")
		.choice()
			.when().simple("${in.body} == null")
				//.to("Calculation")
				.bean(App.class, "shutdown()")
			.when(header("CamelFileName").startsWith("reference_prod-"))
				.unmarshal(bindy).to("direct:saveRef")
			.when(header("CamelFileName").startsWith("transactions_"))
				.unmarshal(bindy2).to("direct:saveTx")
			.otherwise()
				.to("log:output?showExchangePattern=false&showBodyType=true")
			.end()		
		;
		
		from("direct:saveRef")
		.process(new Processor(){
	       public void process(Exchange exchange) throws Exception{
	    	   String[] arrVal =exchange.getIn().getHeader("CamelFileName").toString()
	    			   .replaceAll("reference_prod\\-|\\.data", "").split("_");
	    	   String magasin="XXXX",jour="19700101";
	    	   if(arrVal.length==2){
	    		   magasin=arrVal[0];
	    		   jour=arrVal[1];
	    	   }	    	   
	    	   exchange.getIn().setHeader("magasin", magasin);
	    	   exchange.getIn().setHeader("jour", jour);	    	   
	       }
		})
		.to("log:output?showExchangePattern=false&showBodyType=true")
		.bean(DataStore.class,"addRef(\"${headers.jour}\",\"${headers.magasin}\",${in.body})")
		;
		
		from("direct:saveTx")
		.process(new Processor(){
		       public void process(Exchange exchange) throws Exception {
		    	   String arrVal =exchange.getIn().getHeader("CamelFileName").toString()
		    			   .replaceAll("transactions_|\\.data", "");
		    	   String jour="19700101";
		    	   if(arrVal!=null){
		    		   jour=arrVal;
		    	   }	
		    	   exchange.getIn().setHeader("jour", jour);	    	   
		       }
		})
		.to("log:output?showExchangePattern=false&showBodyType=true")
		.bean(DataStore.class,"addSale(\"${headers.jour}\",${in.body})")
		;
		
		from("direct://save2File")
		.to("file://results/");		
		
	}

}
