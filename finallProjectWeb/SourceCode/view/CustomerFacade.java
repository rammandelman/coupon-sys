package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import MyExceptions.ConnectionClosingException;
import MyExceptions.WrongSqlSyntaxException;
import controler.ConnectionPool;

public class CustomerFacade {
	
	
	public String facadeType = "customers";
	public CustomerDBDAO customer = null;	
	public CouponDBDAO coupon = null;
	protected ConnectionPool pool = ConnectionPool.getConnectionPool();
	protected CustomerFacade()
	{
		this.customer = new  CustomerDBDAO();
		this.coupon = new CouponDBDAO();		
	}
	


}
