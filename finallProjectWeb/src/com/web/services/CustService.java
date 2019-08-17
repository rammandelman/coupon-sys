package com.web.services;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.web.model.Income;
import com.web.model.IncomeType;

import DataBases.Companies;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import DataBases.Customers;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouldNotRemoveCompException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CouponIsOutOfStockException;
import MyExceptions.CustAlreadyExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.CustRemoveException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.NoPurchsedCouponFoundException;
import MyExceptions.UnableToPurchaseCouponException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import controler.userType;
import view.CompanyDBDAO;
import view.CouponClinetFacade;
import view.CouponDBDAO;

@Path("/cust")
public class CustService {
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
	
	private String getUserFromReqeust() {
		String user1 =(String) this.request.getSession(false).getAttribute("userID");
		return user1;
	}
	
	@GET
	@Path("purchase")
	@Produces(MediaType.TEXT_PLAIN)
	public String purchase(
			@QueryParam("couponID")
			String couponID	,
			@QueryParam("custID")String custID
			)	
			{
		String Msg=null;
		Coupons c = null;
		System.out.println("this is purchase func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c= facade.adminFacade.customer.purchase(custID, couponID);
				CouponDBDAO couponDBDAO = new CouponDBDAO();
				Coupons c1=  couponDBDAO.getCoupon(couponID);
				double m = c1.PRICE;
				 String user = getUserFromReqeust();
				 System.out.println("the user from http reqeust is: "+user);
				 Income i = new Income();
				 i.setAmount(m);
					i.setDescription(IncomeType.CUSTOMER_PURCHASE);
					i.setName(user);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (UnableToPurchaseCouponException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponIsOutOfStockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}			 
		}
		if(facade.type == userType.customers)
		{
			try {
				c= facade.customerFacade.customer.purchase(custID, couponID);
				CouponDBDAO couponDBDAO = new CouponDBDAO();
				Coupons c1=  couponDBDAO.getCoupon(couponID);
				double m = c1.PRICE;
				 String user = getUserFromReqeust();
				 System.out.println("the user from http reqeust is: "+user);
				 Income i = new Income();
				 i.setAmount(m);
					i.setDescription(IncomeType.CUSTOMER_PURCHASE);
					i.setName(user);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (UnableToPurchaseCouponException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponIsOutOfStockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
				
		System.out.println(c.toString());
		return  new  Gson().toJson(c);		
	}

	@GET
	@Path("createCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCustomer(
			@QueryParam("custID")String custID, @QueryParam("CUST_NAME")
			String CUST_NAME,@QueryParam("PASSWORD")String PASSWORD)	
			{
		String Msg=null;
		Customers c=null;
		System.out.println("this is createCustomer func from admin service");
		System.out.println("this is the cust id:  " +custID);
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				c= facade.adminFacade.customer.createCustomer(custID, CUST_NAME, PASSWORD);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustAlreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}			 
		}
		if(facade.type == userType.customers)
		{
			try {
				c= facade.customerFacade.customer.createCustomer(custID, CUST_NAME, PASSWORD);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustAlreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
			return new  Gson().toJson(c);
		
	}
	
	
	@GET
	@Path("getAllCustomers")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCustomers(
			//@QueryParam("id")
			//String COMP_ID	
			)	
			{
		String Msg=null;
		Set<Customers> custs = null;
		System.out.println("this is show all customers func from service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				custs = facade.adminFacade.customer.getAllCustomers();
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
		}
		if(facade.type == userType.customers)
		{
			try {
				custs = facade.customerFacade.customer.getAllCustomers();
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
		if(Msg==null) {
			System.out.println("an exception might have been thrown: "+Msg);
		}
					 
		return  new  Gson().toJson(custs);		
	}
	

	@GET
	@Path("getCoupons")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCoupons(
			@QueryParam("PurchasedCoupnByCust")
			String custID	
			)	
			{
		String Msg=null;

		Set<Coupons> coupon = null;
		System.out.println("this is show coupons for cust func from cust service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 
			try {
				coupon = facade.adminFacade.customer.getCoupons(custID);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoPurchsedCouponFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		 
		}
		if(facade.type == userType.customers)
		{
			 
				 try {
					coupon = facade.customerFacade.customer.getCoupons(custID);
				} catch (ConnectionClosingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				} catch (NoFreeConnectionsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				} catch (WrongEntryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				} catch (CouponDoesNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				} catch (CustNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				} catch (NoPurchsedCouponFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Msg =  e.getMessage();
					return new  Gson().toJson(Msg);
				}			
		}
				 
		return  new  Gson().toJson(coupon);	
	}
	
	@GET
	@Path("getCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCustomer(
			@QueryParam("custID")
			String custID	
			)	
			{
		String Msg=null;

		Customers cust = null;
		System.out.println("this is show cust func from cust service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				cust = facade.adminFacade.customer.getCustomer(custID);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}		 
		}
		if(facade.type == userType.customers)
		{
			 try {
				cust = facade.customerFacade.customer.getCustomer(custID);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
	
		 
		return  new  Gson().toJson(cust);
		
	}
	@GET
	@Path("removeCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCustomer(
			@QueryParam("custID")
			String custID	
			)	
			{
		String Msg=null;
		boolean b=false;
		//Set<Coupons> Coupons = null;
		System.out.println("this is removeCustomer func frome cust service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				b=facade.adminFacade.customer.removeCustomer(custID);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				new  Gson().toJson(Msg);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				new  Gson().toJson(Msg);
			} catch (CustRemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
		}
		if(facade.type == userType.customers)
		{
			try {
				b= facade.customerFacade.customer.removeCustomer(custID);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				new  Gson().toJson(Msg);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				new  Gson().toJson(Msg);
			} catch (CustRemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
		}
		if(b==true) {
			Msg="complete successfully";
		}
		 
		return new  Gson().toJson(Msg);
			}
	
	@GET
	@Path("updaetCustomer")
	@Produces(MediaType.TEXT_PLAIN)
	public String updaetCustomer(
			@QueryParam("custID")
			String custID,@QueryParam("filedToChange")String filedName,
			@QueryParam("newValue")String newValue
			)	
			{
		String Msg=null;
		Customers c=null;
		System.out.println("from cust service func, this is updaetCustomer func");
		System.out.println("the cust id recied is: " + custID+" the filed to change is: "+filedName+" the new value is: "+newValue);
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				c= facade.adminFacade.customer.updaetCustomer(custID, filedName, newValue);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ErrorWritingToJoinTblException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
			System.out.println("the cust id recied is: " + custID+" the filed to change is: "+filedName+" the new value is: "+newValue);
		}
		if(facade.type == userType.customers)
		{
			try {
				c= facade.customerFacade.customer.updaetCustomer(custID, filedName, newValue);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ErrorWritingToJoinTblException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CustNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			 
			return new  Gson().toJson(c);
			}
}
