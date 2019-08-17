package com.web.services;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.web.model.Income;

public class BusinessDelegate {

	private static final String Url = "http://localhost:8888/api/income";
	private static final String Url2 = "http://localhost:8888/api/allincomes";
	private static final String Url3 = "http://localhost:8888/api/allincomes_customer";
	private static final String Url4 = "http://localhost:8888/api/allincomes_companie";
	private Client client = ClientBuilder.newClient();
	//post income 
	
	public synchronized Response storeIncome(Income i) {
		System.out.println("from BusinessDelegate , the income description  recived is: "+ i.getDescription());
		 return client
			      .target(Url)
			      .request(MediaType.APPLICATION_XML)
			      .post(Entity.entity(i, MediaType.APPLICATION_XML));
	}
	
	 public synchronized Response getAllIncomes() {
	          Response r =	client.target(Url2)
				      .request(MediaType.APPLICATION_JSON).get();
					// String output = r.readEntity(String.class);
				 
					 return  r;
	    }
	 public synchronized Response getAllIncomesByCustName(String CUST_NAME) {
		 Response r = client
	          .target(Url3)
	          .path(CUST_NAME)
	          .request(MediaType.APPLICATION_JSON)
	          .get();
	         
	         return r;
	    }
	 public synchronized Response getAllIncomesByCompName(String COMP_NAME) {
		 Response r = client
		          .target(Url4)
		          .queryParam("COMP_NAME", COMP_NAME)
		          .request(MediaType.APPLICATION_JSON)
		          .get();
//		 String output = r.readEntity(String.class);
//		 System.out.println(output);
	         return r;
	    }
	
	
}
