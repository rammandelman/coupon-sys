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
import DataBases.CouponType;
import DataBases.Coupons;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponAllreadyExistException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.NoPurchsedCouponFoundException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import controler.userType;
import view.CompanyDBDAO;
import view.CouponClinetFacade;

@Path("/coupons")
public class CouponsServices {
	
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
	@Path("createCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCoupon(			
			@QueryParam("couponID")String couponID,
			@QueryParam("companieID")String companieID,
			@QueryParam("TITLE")String TITLE,
			@QueryParam("START_DATE")String START_DATE,
			@QueryParam("END_DATE")String END_DATE,
			@QueryParam("AMOUNT")int AMOUNT,
			@QueryParam("MESSAGE")String MESSAGE,
			@QueryParam("PRICE")double PRICE,
			@QueryParam("IMAGE")String IMAGE, 
			@QueryParam("type")CouponType type
			)	
			{
		String Msg=null;
		Coupons c=null;
		System.out.println("this is createCoupon func from service");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				 c=facade.adminFacade.coupon.createCoupon
				 (couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type);
				 CompanyDBDAO companyDBDAO = new CompanyDBDAO();
				 Companies comp = companyDBDAO.getCompany(companieID);
				 Income i = new Income();
				 i.setAmount(100);
					i.setDescription(IncomeType.COMPANY_NEW_COUPON);
					i.setName(comp.COMP_NAME);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
					
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ErrorWritingToJoinTblException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}		 
		}
		if(facade.type == userType.companies)
		{
			 try {
				c=facade.companyFacade.coupon.createCoupon
				 (couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type);
				 CompanyDBDAO companyDBDAO = new CompanyDBDAO();
				 Companies comp = companyDBDAO.getCompany(companieID);
				 Income i = new Income();
				 i.setAmount(100);
					i.setDescription(IncomeType.COMPANY_NEW_COUPON);
					i.setName(comp.COMP_NAME);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ErrorWritingToJoinTblException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}	
		}
		
		//System.out.println(comp.theId + comp.COMP_NAME);
		
		 
		
		return new  Gson().toJson(c);
		
	}
	@GET
	@Path("updateCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCoupon(
			@QueryParam("couponID")
			String couponID,@QueryParam("filedToChange")String filedName,
			@QueryParam("newValue")String newValue
			)	
			{
		String Msg=null;
		Coupons c=null;
		System.out.println("from coupoon service func, this is updateCoupon func");
		System.out.println("the couponid is: "+couponID +" the new val recied is: "+newValue+" the filed o change is: "+filedName);
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				c= facade.adminFacade.coupon.updateCoupon(couponID, filedName, newValue);
	
				 String user = getUserFromReqeust();
				 System.out.println("the user from http reqeust is: "+user);				 
				 Income i = new Income();
				 i.setAmount(10);
					i.setDescription(IncomeType.COMPANY_UPDATE_COUPON);
					i.setName(user);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
					
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
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
			System.out.println("the coupon id recied is: " + c.couponID);
		}
		if(facade.type == userType.companies)
		{
			try {
				c= facade.companyFacade.coupon.updateCoupon(couponID, filedName, newValue);
				 String user = getUserFromReqeust();
				 System.out.println("the user from http reqeust is: "+user);				 
				 Income i = new Income();
				 i.setAmount(10);
					i.setDescription(IncomeType.COMPANY_UPDATE_COUPON);
					i.setName(user);
					Response r =  this.bd.storeIncome(i);
					System.out.println("r : " + r);
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
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
		System.out.println("the coupon id recied is: " + c.couponID);
			return new  Gson().toJson(c);
			}


	@GET
	@Path("getCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCoupon(
			@QueryParam("couponID")
			String couponID	
			)	
			{
		String Msg=null;

		Coupons c = null;
		System.out.println("this is show coupon by id func from coupon service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c = facade.adminFacade.coupon.getCoupon(couponID);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}		 
		}
		if(facade.type == userType.companies)
		{
			 try {
				c = facade.companyFacade.coupon.getCoupon(couponID);
			} catch (NoFreeConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
	
		//System.out.println(comp.theId + comp.COMP_NAME);
		//System.out.println(c.toString());
		 
		return  new  Gson().toJson(c);
		
	}
	@GET
	@Path("getAllCoupons")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCoupons(
			//@QueryParam("id")
			//String COMP_ID	
			)	
			{
		String Msg=null;
		Set<Coupons> c= null;
		System.out.println("this is show all coupons func from coupons service calss");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c = facade.adminFacade.coupon.getAllCoupons();
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}	 
		}
		if(facade.type == userType.companies)
		{
			try {
				c = facade.companyFacade.coupon.getAllCoupons();
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}if(facade.type == userType.customers)
		{
			try {
				c = facade.customerFacade.coupon.getAllCoupons();
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
		 
		return  new  Gson().toJson(c);
		
	}
	
	@GET
	@Path("getCouponByType")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCouponByType(
			@QueryParam("type")
			String couponType	
			)	
			{
		String Msg=null;
		Set<Coupons> c= null;
		System.out.println("this is show all coupons by type func from coupons service calss");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c = facade.adminFacade.coupon.getCouponByType(couponType);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}	 
		}
		if(facade.type == userType.companies)
		{
			try {
				c = facade.companyFacade.coupon.getCouponByType(couponType);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
		 
		return  new  Gson().toJson(c);
		
	}
	
	@GET
	@Path("getPurchasedCouponByType")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPurchasedCouponByType(
			@QueryParam("custID")
			String custID,
			@QueryParam("couponType")
			String couponType
			)	
			{
		String Msg=null;
		Set<Coupons> c= null;
		System.out.println("this is show all purchased coupons by type func from coupons service calss");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c = facade.adminFacade.coupon.getPurchasedCouponByType(couponType, custID);
				System.out.println("this is show all purchased coupons by type func from coupons service calss,"
						+ " the Msg is: "+Msg);
				if(c==null) {
					System.out.println("the c is null");
				}else {
					System.out.println("the c returned with objects");
				}
				for(Coupons c1 : c) {
					System.out.println("the c id is: "+c1.couponID);
					System.out.println("the c1 to string is: "+c1.toString());
				}
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
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
				c = facade.customerFacade.coupon.getPurchasedCouponByType(couponType, custID);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
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
		
		return  new  Gson().toJson(c);		
	}
	
	
	@GET
	@Path("getPurchasedCouponByPrice")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPurchasedCouponByPrice(
			@QueryParam("custID")
			String custID,
			@QueryParam("PRICE")
			double PRICE
			)	
			{
		String Msg=null;
		Set<Coupons> c= null;
		System.out.println("this is show all purchased coupons by price func from coupons service calss");
		System.out.println("the id recid is: "+custID);
		System.out.println("the price recived is: "+PRICE);
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				c = facade.adminFacade.coupon.getPurchasedCouponByPrice(PRICE, custID);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoPurchsedCouponFoundException e) {
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
				c = facade.customerFacade.coupon.getPurchasedCouponByPrice(PRICE, custID);
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
			} catch (CouponDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongSqlSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (WrongEntryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (NoPurchsedCouponFoundException e) {
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
 
		return  new  Gson().toJson(c);		
	}
}
