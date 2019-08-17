package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataBases.Companies;
import DataBases.CouponType;
import DataBases.Users;
import MyExceptions.NoFreeConnectionsException;
import controler.ConnectionPool;
import controler.userType;

/**
 * 
 * @author ram
 *this class is the general facade that holds the correct facade for each user
 */
public class CouponClinetFacade {
	
	public String facadeType = "this is just generic type";
	
    protected ConnectionPool pool = ConnectionPool.getConnectionPool();
	
	public ConnectionPool getPool() {
		return pool;
	}

	public userType type = null;
	/**
	 * this 3 objects are each initialized in the corresponding constractor depending on the user type. 
	 */
	public AdminFacade adminFacade;
	public CustomerFacade customerFacade;
	public CompanyFacade companyFacade;
	
	/**
	 * this contractor is called when the user trying to connect is an admin;
	 * and initialize the adminfacade object.
	 * @param adminFacade
	 */
	protected CouponClinetFacade(AdminFacade adminFacade)
	{
		this.adminFacade = adminFacade;
	}
	/**
	 * this contractor is called when the user trying to connect is a customer;
	 * and initialize the customerfacade object.
	 * @param customerFacade
	 */
	protected CouponClinetFacade(CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}
	/**
	 *  this contractor is called when the user trying to connect is a companie;
	 * and initialize the companiefacade object.
	 * @param companyFacade
	 */
	protected CouponClinetFacade(CompanyFacade companyFacade)
	{
		this.companyFacade = companyFacade;
	}
	/**
	 * this function check the type of facade initialized
	 * @return
	 */
	protected userType checkTheUserType() {
		if(this.adminFacade != null) {
			return userType.admin;
		}
		if(this.companyFacade != null) {
			return userType.companies;
		}
		if(this.customerFacade != null) {
			return userType.customers;
		}
		return null;
		
		}
	
	}
