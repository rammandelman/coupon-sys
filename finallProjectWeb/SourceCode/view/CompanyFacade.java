package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DataBases.Coupons;
import MyExceptions.ConnectionClosingException;
import MyExceptions.WrongEntryException;
import controler.ConnectionPool;

public class CompanyFacade {
	public String facadeType = "companies";

	public CompanyDBDAO company = null;

	public CouponDBDAO coupon =null;

	protected CompanyFacade() {
		this.coupon =  new CouponDBDAO();
		this.company = new CompanyDBDAO();
	}
	
}
