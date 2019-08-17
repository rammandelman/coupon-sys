package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import DataBases.Companies;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import DataBases.Customers;
import Logins.Client;
import MyExceptions.ConnectionClosingException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import controler.ConnectionPool;
import interfaces.CompanyDAO;
import interfaces.CouponDAO;
import interfaces.CustomerDAO;
/**
 * 
 * @author ram
 *this is the admin facade
 */
public class AdminFacade

{
	public String facadeType = "admin";
	ConnectionPool pool = 	null;
	public CompanyDBDAO company = null;
	public CustomerDBDAO customer = null;
	public CouponDBDAO coupon = null;
	
	protected AdminFacade() // class constructor initializing the class parameters
	{
		this.pool = ConnectionPool.getConnectionPool();
		this.company = new CompanyDBDAO();
		this.customer = new CustomerDBDAO();
		this.coupon = new CouponDBDAO();
	}
	/**
	 * 
	 * @return a set of companie objects 
	 * @throws FailedToConnectDb 
	 * @throws ConnectionClosingException 
	 * @throws WrongSqlSyntaxException 
	 */
	public Set<Companies> getAllCompanies() throws FailedToConnectDb, ConnectionClosingException, WrongSqlSyntaxException { 
		Connection connection = null;
		Set<Companies> allTheCompanies = new HashSet<Companies>();
		try {
			connection = pool.returnConnection(connection); // getting a connection to the db
			PreparedStatement ps = connection.prepareStatement("select * from companies"); // Create a statement object which can read data:
			
			ResultSet rs = ps.executeQuery(); // Execute the query and return an object which contains the data:

			while (rs.next()) {

				String theId = rs.getString("companieID");
				String COMP_NAME = rs.getString("COMP_NAME");
				String EMAIL = rs.getString("EMAIL");
				String password = rs.getString("Password");
				
				 // adding companie the set every time a companie is found
				allTheCompanies.add(new Companies(theId, COMP_NAME, EMAIL, password));				
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new WrongSqlSyntaxException();
		} catch (FailedToConnectDb e) {			
			e.printStackTrace();
			throw new FailedToConnectDb();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("faild to close connection "+connection.toString());
			
		}
		pool.connectionsCountDecrement();

		return allTheCompanies;
	}
	
	public Set<Customers> getAllCustomers() {
		Connection connection = null;
		Set<Customers> allTheCustomers = new HashSet<Customers>();
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from customers");
			

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String custID = rs.getString("custID");
				String CUST_NAME = rs.getString("CUST_NAME");			
				String PASSWORD = rs.getString("PASSWORD");

				allTheCustomers.add(new Customers(custID, CUST_NAME, PASSWORD));		
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
			
			
		}
		pool.connectionsCountDecrement();

		return allTheCustomers;
	}
	
}
