package com.web.services;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.web.model.Income;
import com.web.model.IncomeType;

import DataBases.Companies;
import MyExceptions.WrongSqlSyntaxException;
import controler.ConnectionPool;
import view.AdminFacade;
import view.CouponClinetFacade;
import view.CouponSystem;

@Path("/admin")
public class AdminService {
	
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	
	private BusinessDelegate bd = new BusinessDelegate();
	
	public CouponClinetFacade getFacade() {
		System.out.println("this is get facade func");
		CouponClinetFacade facade =(CouponClinetFacade) this.request.getSession(false).getAttribute("facede");
		
		return facade;
	}
	/**
	 * this function uses the single instance of coupon system too call shut down function
	 * which stop the daily exparitoin task and closes all connection
	 */
	@GET
	@Path("/shutDown")
	@Produces(MediaType.TEXT_PLAIN)
	public String shutDown() {
		System.out.println("this is shut down func from admin class");
		String Msg = null;
		/**
		 * getting the instance  CouponSystem
		 */
		CouponSystem c = CouponSystem.getInstance();
		c.shutDown();
		CouponClinetFacade facade = this.getFacade();
		ConnectionPool pool = facade.getPool();
		int x = 0;
		try {
			x = pool.checkConnections();
		} catch (WrongSqlSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Msg =  e.getMessage();
			return new  Gson().toJson(Msg);
		}
		System.out.println("from admin service calss the numver of checkConnections resualt is: "+x);
		
		return new Gson().toJson(x);
				
	}
	
	/**
	 *  this function uses the single instance of connection pool to check the number of open connections to db.
	 */
	@GET
	@Path("/checkConnections")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnections() {
		System.out.println("this is checkConnections func from admin class");
		CouponClinetFacade facade = this.getFacade();
		String Msg = null;
		/**
		 * getting the instance  ConnectionPool
		 */
		ConnectionPool pool = facade.getPool();
		int x =0;
		try {
			x = pool.checkConnections();
		} catch (WrongSqlSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Msg =  e.getMessage();
			return new  Gson().toJson(Msg);
		}
		System.out.println("from admin service calss the numver of checkConnections resualt is: "+x);
		
		
		return new  Gson().toJson(x);
				
	}
	@GET
	@Path("/getAllIncomes")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllIncomes() {
		System.out.println("this is getAllIncomes func from admin class");
		//CouponClinetFacade facade = this.getFacade();
		Response r =  this.bd.getAllIncomes();
		System.out.println(r);
		String output = r.readEntity(String.class);
		System.out.println(output);
		return output;
				
	}
	
	@GET
	@Path("/getAllIncomesByCustName")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllIncomesByCustName(@QueryParam("custName")String CUST_NAME) {
		System.out.println("this is getAllIncomes func from admin class");
		String Msg ="no incomes were found.";
		Response r =  this.bd.getAllIncomesByCustName(CUST_NAME);
		System.out.println(r);
		String output = r.readEntity(String.class);
		if(output.length()<3) {
			return new  Gson().toJson(Msg);
		}
		System.out.println(output);
		return output;
				
	}
	@GET
	@Path("/getAllIncomesByCompName")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllIncomesByCompName(@QueryParam("compName")String COMP_NAME) {
		System.out.println("this is getAllIncomesByCompName func from admin class");
		String Msg ="no incomes were found.";
		Response r =  this.bd.getAllIncomesByCompName(COMP_NAME);
		System.out.println(r);
		String output = r.readEntity(String.class);
		if(output.length()<3) {
			return new  Gson().toJson(Msg);
		}
		System.out.println(output);
		return output;
				
	}
	
}
