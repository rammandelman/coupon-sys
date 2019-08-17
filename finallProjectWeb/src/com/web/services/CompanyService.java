package com.web.services;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DataBases.Companies;
import DataBases.Coupons;
import MyExceptions.CompAllreadyExistException;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouldNotRemoveCompException;
import MyExceptions.CouponCouldNotBeRemovedException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import controler.userType;

import view.CouponClinetFacade;

@Path("/comp")
public class CompanyService {

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
	
	@GET
	@Path("createCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCompany(
			@QueryParam("companieID")String companieID, @QueryParam("COMP_NAME")
			String COMP_NAME,@QueryParam("EMAIL")String EMAIL,
			@QueryParam("Password")String Password
			) throws WrongEntryException	
			{
		
		String Msg=null;
		Companies comp= null;
		System.out.println("this is createCompany func from admin service");
		System.out.println(companieID + " " + COMP_NAME + " " +
				EMAIL + " " + Password
				);
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
										
				comp= facade.adminFacade.company.createCompany(companieID, COMP_NAME, EMAIL, Password);
			}catch (WrongEntryException e) {
				
				Msg =  e.getMessage();
				System.out.println("this is from excption handler in comapny service class, the exception is:  "+Msg);
				
				return new  Gson().toJson(Msg);  
				
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
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
		if(facade.type == userType.companies)
		{
			try {
				comp= facade.companyFacade.company.createCompany(companieID, COMP_NAME, EMAIL, Password);
			}catch (WrongEntryException e) {
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
				
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
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
		
		 
		return  new  Gson().toJson(comp);
		
	}
	
	@GET
	@Path("getCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCompany(
			@QueryParam("id")
			String COMP_ID	
			)	
			{
		String Msg=null;

		Companies comp = null;
		System.out.println("this is show comp func from comp service class");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				comp = facade.adminFacade.company.getCompany(COMP_ID);
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
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				System.out.println("the Msg is : "+Msg);
				return new  Gson().toJson(Msg);
			}		 
		}
		if(facade.type == userType.companies)
		{
			 try {
				comp = facade.companyFacade.company.getCompany(COMP_ID);
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
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
	
		//System.out.println(comp.theId + comp.COMP_NAME);		
		 
		return  new  Gson().toJson(comp);
		
	}
	
	@GET
	@Path("getAllCompanies")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCompanies(
			//@QueryParam("id")
			//String COMP_ID	
			)	
			{
		String Msg=null;
		Set<Companies> comps = null;
		System.out.println("this is show all comp func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			 try {
				comps = facade.adminFacade.company.getAllCompanies();
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
			}	 
		}
		if(facade.type == userType.companies)
		{
			try {
				comps = facade.companyFacade.company.getAllCompanies();
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
			}
		}
			
		 
		return  new  Gson().toJson(comps);
		
	}
	
	@GET
	@Path("getCoupons")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCoupons(
			@QueryParam("companieID")
			String COMP_ID	
			)	
			{
		String Msg=null;
		System.out.println("result : " +COMP_ID);
		Set<Coupons> coupons = null;
		System.out.println("this is getCoupons func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				coupons = facade.adminFacade.company.getCoupons(COMP_ID);
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
			} catch (CompanieNotFoundException e) {
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
				coupons = facade.companyFacade.company.getCoupons(COMP_ID);
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
			} catch (CompanieNotFoundException e) {
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
			
		 System.out.println(coupons.toString());
		return  new  Gson().toJson(coupons);
		
	}
	@GET
	@Path("removeCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCompany(
			@QueryParam("compID")
			String compID	
			)	
			{
		String Msg=null;
		
		boolean b=false;
		System.out.println("this is removeCompany func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				b =facade.adminFacade.company.removeCompany(compID);
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
			} catch (CouldNotRemoveCompException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
		}
		if(facade.type == userType.companies)
		{
			try {
				 b= facade.companyFacade.company.removeCompany(compID);
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
			} catch (CouldNotRemoveCompException e) {
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
	@Path("removeCoupon")
	@Produces(MediaType.TEXT_PLAIN)
	public String removeCoupon(
			@QueryParam("couponID")
			String couponID	
			)	
			{
		String Msg=null;
		
		boolean b=false;
		System.out.println("this is removeCoupon func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				b =facade.adminFacade.coupon.removeCoupon(couponID);
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
				System.out.println("thsi is catch cluase the msg is: "+Msg);
				return new  Gson().toJson(Msg);
			} catch (CouponCouldNotBeRemovedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
		}
		if(facade.type == userType.companies)
		{
			try {
				 b= facade.companyFacade.coupon.removeCoupon(couponID);
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
			} catch (CouponCouldNotBeRemovedException e) {
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
	@Path("updateCompany")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCompany(
			@QueryParam("compID")
			String compID,@QueryParam("filedToChange")String filedName,
			@QueryParam("newValue")String newValue
			)	
			{
		String Msg=null;
		Companies c=null;
		System.out.println("from comp service func, this is updateCompany func");
		CouponClinetFacade facade = this.getFacade();
		if(facade.type == userType.admin)
		{
			try {
				c=facade.adminFacade.company.updateCompany(compID, filedName, newValue);
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
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} 
			System.out.println("the comp id recied is: " + compID+" the filed to change is: "+filedName+" the new value is: "+newValue);
		}
		if(facade.type == userType.companies)
		{
			try {
				c=facade.companyFacade.company.updateCompany(compID, filedName, newValue);
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
			} catch (UnableToUpdateDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompanieNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			} catch (CompAllreadyExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Msg =  e.getMessage();
				return new  Gson().toJson(Msg);
			}
		}
			
		 
			return new  Gson().toJson(c);
			}
	@GET
	@Path("getAllincomes")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllincomes(
			//@QueryParam("id")
			//String COMP_ID	
			)	
			{
		System.out.println("this is getAllIncomes func from comp class");
		//CouponClinetFacade facade = this.getFacade();
		Response r =  this.bd.getAllIncomes();
		System.out.println(r);
		return new  Gson().toJson(r);

			}
}
