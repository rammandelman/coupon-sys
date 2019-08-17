package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import DataBases.Companies;
import DataBases.CouponType;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import DataBases.Customers;
import Logins.Client;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CouponIsOutOfStockException;
import MyExceptions.CustAlreadyExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.CustRemoveException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.NoPurchsedCouponFoundException;
import MyExceptions.UnableToPurchaseCouponException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import MyExceptions.wrongUserOrPassException;
import controler.ConnectionPool;
import interfaces.CustomerDAO;

public class CustomerDBDAO extends Client implements CustomerDAO {
	protected CustomerDBDAO() {

	}

	ConnectionPool pool = ConnectionPool.getConnectionPool();

	@Override
	public Customers createCustomer(String custID, String CUST_NAME, String PASSWORD) 
			throws NoFreeConnectionsException, WrongEntryException, CustNotFoundException, ConnectionClosingException, CustAlreadyExistException {
		Connection connection = null;
		Customers c=null;
		boolean success = false;
		try { // checking if cust already exist
			Customers c1 = getCustomer(custID);
			if(c1 != null) {
				throw new CustAlreadyExistException();
			}
		}catch (CustAlreadyExistException e) {
			// TODO: handle exception
			throw new CustAlreadyExistException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustAlreadyExistException();
		}catch (Exception e) {
			// TODO: handle exception
		}
				
		try {
			connection = pool.returnConnection(connection);
			//inserting new cust data to main customers table
			PreparedStatement ps = connection.prepareStatement("insert into customers VALUES(?, ?, ?)");
			ps.setString(1, custID);
			ps.setString(2, CUST_NAME);
			ps.setString(3, PASSWORD);

			ps.executeUpdate();
			System.out.println("update request send to data base");
			}catch (NoFreeConnectionsException e) {
				// TODO: handle exception
				throw new NoFreeConnectionsException();
			} catch (SQLException e) {
				throw new WrongEntryException();
				//WrongEntryException ee = new WrongEntryException();
				
			} catch (FailedToConnectDb e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
			// inserting  new cust data to join users table 
			PreparedStatement ps2 = connection.prepareStatement("insert into users VALUES(?, ?, 'customers')");
			ps2.setString(1, custID);
			ps2.setString(2, PASSWORD);
			ps2.executeUpdate();
			}catch (NoFreeConnectionsException e) {
				// TODO: handle exception
				throw new NoFreeConnectionsException();
			} catch (SQLException e) {
				throw new WrongEntryException();
				//WrongEntryException ee = new WrongEntryException();
				
			}
			try {
			// checking if customer created 
			PreparedStatement ps3 = connection.prepareStatement("select * from customers where custID =  ? ");
			ps3.setString(1, custID);

			ResultSet rs = ps3.executeQuery();
			if (rs.next()) {
				System.out.println("successfully created customer user ");
				success = true;
				System.out.println("the success status is: "+success);
				c=  getCustomer(custID);
			} else {
				throw new WrongEntryException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
			throw new WrongEntryException();
						
		}
		
		try {
			connection.close();

		} catch (SQLException e) {
			
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		
		return c;
	}

	@Override
	public boolean removeCustomer(String custID) throws NoFreeConnectionsException, WrongEntryException, CustRemoveException, ConnectionClosingException {
		Connection connection = null;
		boolean success = false;

		try { // remove cust from main customers table
			connection = pool.returnConnection(connection);

			// Create sql command for delete one record:
			String sql = String.format("delete from customers where custID=?");
			// Create an object for executing the above command:
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, custID);

			// Execute:
			if(ps.executeUpdate()==1)
			{
				System.out.println("successfully removed customer");
				success = true;
			}else {
				throw new CustRemoveException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
		
			//e.printStackTrace();
			throw new WrongEntryException();
		}catch (CustRemoveException e) {
			throw new CustRemoveException();
		} catch (FailedToConnectDb e) {
			e.printStackTrace();
		}
		try { // remove cust from join users table

			// Create sql command for delete one record:
			String sql2 = String.format("delete from users where user_name=?");
			// Create an object for executing the above command:
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setString(1, custID);

			ps2.executeUpdate();

		} catch (SQLException e) {
		//	System.out.println("could ot remove user from db check ID");
			throw new CustRemoveException();
			
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		
		return success;
	}

	@Override
	public Customers updaetCustomer(String custID, String filedName, String newValue) throws ConnectionClosingException, WrongEntryException, NoFreeConnectionsException, ErrorWritingToJoinTblException, CustNotFoundException, UnableToUpdateDataException {
		Connection connection = null;
		Customers c = null;
		boolean success = false;
		try { // checking if customer even exist
			Customers c1 = getCustomer(custID);
			if(c1 == null) {
				throw new CustNotFoundException();
			}
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (Exception e) {
			// TODO: handle exception
		}
		try { // send update to db
			connection = pool.returnConnection(connection);
			// Create an object for executing the above sql:

			PreparedStatement ps = connection
					.prepareStatement("UPDATE customers SET " + filedName + " = ?  WHERE custID = ? ");
			// ps.setString(1, filedName);
			ps.setString(1, newValue);
			ps.setString(2, custID);

			if(ps.executeUpdate()==1)
			{
				System.out.println("successfully updated customer");
				success = true;
			}else {
				throw new UnableToUpdateDataException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (WrongEntryException e) {
			throw new WrongEntryException();
		} catch (SQLException e) {
		//	System.out.println("unable to update data check values");
			throw new WrongEntryException();
		} catch (UnableToUpdateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnableToUpdateDataException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try { // check the field user requested to update,
			  // some fields needs to be updated in join table too.

			if (filedName.equals("custID")) {

				PreparedStatement ps2 = connection
						.prepareStatement("UPDATE users SET user_name = ?  WHERE user_name = ? ");
				// ps.setString(1, filedName);
				ps2.setString(1, newValue);
				ps2.setString(2, custID);

				ps2.executeUpdate();
			}

		} catch (SQLException e) {
			//System.out.println("unable to update data check values");
			throw new ErrorWritingToJoinTblException();
		}
		try {

			if (filedName.equals("password")) {
				System.out.println("the filed to change is pass");
				PreparedStatement ps3 = connection
						.prepareStatement("UPDATE users SET password = ?  WHERE user_name = ? ");
				// ps.setString(1, filedName);
				ps3.setString(1, newValue);
				ps3.setString(2, custID);

				ps3.executeUpdate();
			}

		} catch (SQLException e) {
			//System.out.println("unable to update data check values");
			throw new ErrorWritingToJoinTblException();
		}
		try {
			if(success == true) // check if code above successfully send to main table, and if so, find the updated record
			{
				c = getCustomer(custID);
			}
			
		}catch (SQLException e) {
			//System.out.println("could not find updated cust id: "+ custID);
			throw new CustNotFoundException();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		
		return c;
	}

	@Override
	public Customers getCustomer(String custID) throws NoFreeConnectionsException, WrongEntryException, ConnectionClosingException, CustNotFoundException {
		Connection connection = null;
		Customers customer = null;
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from customers where custID = ? ");
			ps.setString(1, custID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String theId = rs.getString("custID");
				String CUST_NAME = rs.getString("CUST_NAME");

				String password = rs.getString("password");

				customer = new Customers(theId, CUST_NAME, password);
			}
			if(customer==null) {
				throw new CustNotFoundException();
			}
		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new WrongEntryException();
		} catch (CustNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CustNotFoundException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return customer;
	}

	@Override
	public Set<Customers> getAllCustomers() throws NoFreeConnectionsException, WrongEntryException, ConnectionClosingException {
		Connection connection = null;
		
		Set<Customers> allCustomers = new HashSet<Customers>();
		try { // Querying all customers records from db
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from customers ");

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String theId = rs.getString("custID");
				String COMP_NAME = rs.getString("CUST_NAME");
				String password = rs.getString("password");
				
				allCustomers.add(new Customers(theId, COMP_NAME, password));
				
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new WrongEntryException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return allCustomers;

	}

	@Override
	public Set<Coupons> getCoupons(String custID) throws ConnectionClosingException, NoFreeConnectionsException, WrongEntryException, CouponDoesNotExistException, CustNotFoundException, NoPurchsedCouponFoundException {
		Connection connection = null;
		boolean success=false;
		Set<CustomerVsCoupons> allTheCustomerCoupons =new HashSet<CustomerVsCoupons>();
		Set<Coupons> purchaedCoupons = new HashSet<Coupons>();
		try { // first checking if cust even exist
			Customers c1 = getCustomer(custID);
			if(c1 == null) {
				throw new CustNotFoundException();
			}
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (Exception e) {
			// TODO: handle exception
		}
		try { // getting records from join table
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from mydb.customervscoupons where custID=?");
			ps.setString(1, custID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String cust_id = rs.getString("custID");
				String coupon_id = rs.getString("couponID");
				String purchase_date = rs.getString("purchase_date");
				
				allTheCustomerCoupons.add(new DataBases.CustomerVsCoupons(cust_id, coupon_id,
						purchase_date));
				success=true;
			}
			if(success==false) {
				throw new NoPurchsedCouponFoundException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (WrongEntryException e) {
			// TODO: handle exception
			throw new WrongEntryException();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WrongEntryException();
		} catch (NoPurchsedCouponFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoPurchsedCouponFoundException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
		for(CustomerVsCoupons c :  allTheCustomerCoupons) { // going over all the records from join table and filter using couponid		
		
		PreparedStatement ps2;
			ps2 = connection.prepareStatement("select * from coupons where couponID = ? ");
		
		ps2.setString(1, c.coupon_id);
		// Execute the query and return an object which contains the data:
		ResultSet rs2 = ps2.executeQuery();

		while (rs2.next()) {

			 String couponID = rs2.getString("couponID");
			 String companieID = rs2.getString("companieID");
			String TITLE = rs2.getString("TITLE");
			String START_DATE = rs2.getString("START_DATE");
			String END_DATE = rs2.getString("END_DATE");
			int AMOUNT = rs2.getInt("AMOUNT");
			String MESSAGE = rs2.getString("MESSAGE");
			double PRICE = rs2.getDouble("PRICE");
			String IMAGE = rs2.getString("IMAGE");
			CouponType type =  CouponType.valueOf(rs2.getString("type"));

			 purchaedCoupons.add(new Coupons
					 (couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE,  type));
		}
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new CouponDoesNotExistException();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return purchaedCoupons;
	}

	@Override
	public boolean login(String custID, String pass) throws ConnectionClosingException {
		boolean loginn = false;
		Connection connection = null;
		try {

			try {
				connection = pool.returnConnection(connection);
			} catch (NoFreeConnectionsException e) {
				// TODO: handle exception
			} catch (FailedToConnectDb e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Create a statement object which can read data:
			PreparedStatement ps = connection
					.prepareStatement("select * from customers where custID = ? and password = ?");
			ps.setString(1, custID);
			ps.setString(2, pass);
			ps.executeUpdate();
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
			try {
				if (rs.next()) {
					System.out.println("login succeeded");
					loginn = true;
				} else {
					throw new wrongUserOrPassException();
				}
			} catch (wrongUserOrPassException e) {
				// TODO: handle exception
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			ConnectionClosingException ee = new ConnectionClosingException();
			e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return loginn;
	}

	@Override
	public Coupons purchase(String custID, String couponID) throws NoFreeConnectionsException, UnableToPurchaseCouponException, CouponDoesNotExistException, CouponIsOutOfStockException, ConnectionClosingException, CustNotFoundException {
		Connection connection = null;
		int amontOfCouponInStock = 0;
		Coupons coupon = null;
		Coupons coupon1 = null;
		int amountOfCouponBought = 0;
		try { //  checking if cust already exist
			Customers c1 = getCustomer(custID);
			if(c1 == null) {
				throw new CustNotFoundException();
			}
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (Exception e) {
			// TODO: handle exception
		}
		try { // getting connection:          
			connection = pool.returnConnection(connection);		

			//execute query to return the purchased coupon
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM mydb.coupons where couponID=?");
			
			ps.setString(1, couponID);						
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();		
			
			while (rs.next()) {
				String theCouponID = rs.getString("couponID");
				String companieID = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				String START_DATE = rs.getString("START_DATE");
				String END_DATE = rs.getString("END_DATE");
				int AMOUNT= rs.getInt("AMOUNT");
				String MESSAGE= rs.getString("MESSAGE");
				double PRICE= rs.getDouble("PRICE");
				String IMAGE= rs.getString("IMAGE");
				CouponType type = CouponType.valueOf(rs.getString("type"));			
				
				 coupon1 = new Coupons
						(theCouponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type);
					
			}
			 if(coupon1 == null) // checking if the coupon you want to purchase, is found
			 {
				 throw new CouponDoesNotExistException();
			 }else {
				 System.out.println("coupon found");
				 amontOfCouponInStock = coupon1.AMOUNT;
				
				 //now we check how many times this coupon have been purchased:
				 PreparedStatement ps1 = connection
							.prepareStatement("SELECT * FROM mydb.customervscoupons where couponID = ?");
					
				 ps1.setString(1, couponID);
				 ResultSet rs2 = ps1.executeQuery();
				
				 while(rs2.next())
				 {			
					 String custID1 = rs2.getString("custID");
					 String couponID1 = rs2.getString("couponID");
					String  date = rs2.getString("purchase_date");
					
					amountOfCouponBought++;
				 }
				
			 }
			 if(amountOfCouponBought < amontOfCouponInStock) // now we check if coupon is still in stock
			 {
				 System.out.println("coupon is still in stock");
				// insert data to join table, and buying the coupon
					PreparedStatement ps2 = connection
							.prepareStatement("insert into customervscoupons values(?,?,now())");
					ps2.setString(1, custID);
					ps2.setString(2, couponID);
					
					// Execute the query:
					if (ps2.executeUpdate() == 1)
                      {
						coupon = coupon1;
	                   System.out.println("coupon purchased successfully");
                      }
					
			 }else {
				 throw new CouponIsOutOfStockException();
			 }
							
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} 
		catch (SQLException e) {
			System.out.println
			("unable to purchase coupon. check your cust id and coupon id are valid, you might have already purchased that coupon.");
			throw new UnableToPurchaseCouponException();
		} catch (CouponDoesNotExistException e) {
			// TODO Auto-generated catch block
			throw new CouponDoesNotExistException();
			
		} catch (CouponIsOutOfStockException e) {
			// TODO Auto-generated catch block
			throw new CouponIsOutOfStockException();
			
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return coupon;
	}
}
