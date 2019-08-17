package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import DataBases.Companies;
import DataBases.CouponType;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import DataBases.Customers;
import MyExceptions.CompAllreadyExistException;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponAllreadyExistException;
import MyExceptions.CouponCouldNotBeRemovedException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.NoPurchsedCouponFoundException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import controler.ConnectionPool;
import interfaces.CompanyDAO;
import interfaces.CouponDAO;

public class CouponDBDAO implements CouponDAO{
	
	CompanyDBDAO companyDBDAO = null;
	public CouponDBDAO() {	
		this.companyDBDAO = new CompanyDBDAO();
	}	
	
	ConnectionPool pool = ConnectionPool.getConnectionPool();
	@Override
	public Coupons createCoupon(
			String couponID,String companieID,String TITLE,String START_DATE,String END_DATE,
			int AMOUNT,String MESSAGE,double PRICE,String IMAGE, CouponType type) 
					throws CouponDoesNotExistException,NoFreeConnectionsException,WrongEntryException,
					ErrorWritingToJoinTblException,ConnectionClosingException, CouponAllreadyExistException, CompanieNotFoundException
	{
		try {		// checking if coupon already exist	
			Coupons c=  getCoupon(couponID);
			if(c!=null) {
				throw new CouponAllreadyExistException();
			}
		}catch (CouponDoesNotExistException e) {
			// TODO: handle exception
			System.out.println("this is catch clues in create coupon func");
		}
		catch ( NoFreeConnectionsException e) {
			// TODO: handle exception

		}catch (CouponAllreadyExistException e) {
			// TODO: handle exception
			throw new CouponAllreadyExistException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CouponAllreadyExistException();
		}
		catch (ConnectionClosingException e) {
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
		}
		try {		// checking if companie entered exist	
			Companies c2=  this.companyDBDAO.getCompany(companieID);
			if(c2 == null) {
				throw new CompanieNotFoundException();
			}
		}catch ( NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (ConnectionClosingException e) {
			// TODO: handle exception
		}catch (CompanieNotFoundException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}
		Coupons c=null;
		boolean success = false;
		Connection connection = null;
		try {  // writing coupon data to db			
			connection = pool.returnConnection(connection);
			PreparedStatement ps = connection.prepareStatement("insert into coupons VALUES(?, ?, ?, ?,?,?,?,?,?,?)");
			ps.setString(1, couponID);
			ps.setString(2, companieID);
			ps.setString(3, TITLE);
			ps.setString(4, START_DATE);
			ps.setString(5, END_DATE);
			ps.setInt(6, AMOUNT);
			ps.setString(7, MESSAGE);
			ps.setDouble(8, PRICE);
			ps.setString(9, IMAGE);
			ps.setString(10, (type.name()));
		   if(ps.executeUpdate() == 1)
		   {
			   System.out.println("coupon created successfully");
			   success = true;
			   
		   }else {
			   throw new WrongEntryException();
		   }
					
		}catch (NoFreeConnectionsException e) {			
			throw new NoFreeConnectionsException();			
		} 
		catch (WrongEntryException e) {		
			e.printStackTrace();			
			throw new WrongEntryException();		
		} catch (SQLException ee) {
			ee.printStackTrace();
			//System.out.println("the deteials could not be recivied in data base check for duplications");
			throw new WrongEntryException();
			
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * the code below check if the coupon was successfully created 
		 */
		if(success == true) {			
		try {				
			c = getCoupon(couponID);
			
		}catch (SQLException eee) {
			//System.out.println("could not find the created coupon id: " + couponID);
			throw new CouponDoesNotExistException();
		}
	}
		/**
		 * the code below insert the coupon to the join table
		 */
		try {
			PreparedStatement ps2 = connection.prepareStatement("insert into company_coupon VALUES(?, ?)");
			ps2.setString(1, couponID);
			ps2.setString(2, companieID);
			
			ps2.executeUpdate();
		} catch (SQLException e) {
			//System.out.println("error writing to join table, check for duplicates");
			throw new ErrorWritingToJoinTblException();
		}
		try { // 	checking if coupon data also written to join table
			PreparedStatement ps4 = connection.prepareStatement("select * from company_coupon where couponID =  ? ");
			ps4.setString(1, couponID);

			ResultSet rs2 = ps4.executeQuery();
			if (rs2.next()) {
	System.out.println("found coupon with id: " + couponID );
			}else {
				throw new WrongEntryException();
			}
			}catch (SQLException eee) {
				//System.out.println("could not find the created coupon in join table");
				throw new CouponDoesNotExistException();
			}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		
		pool.connectionsCountDecrement();
		return c;
		
	}
/**
 * the remove func has method overload so that when the daily copuon destroyer need to delete multiple copuons it can do it
 * with just a single connection
 * @throws NoFreeConnectionsException 
 */
	@Override
	public boolean removeCoupon(String couponID) throws NoFreeConnectionsException, ConnectionClosingException,
	WrongEntryException,CouponCouldNotBeRemovedException
	{
		Connection connection = null;
		boolean success=false;
		try {
			connection = pool.returnConnection(connection);

			// Create sql command for delete one record from main coupons table
			String sql = String.format("delete from coupons where couponID=?");
			// Create an object for executing the above command:
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, couponID);

			// Execute:
			if(ps.executeUpdate() ==1)
			{
				System.out.println();
				success = true;
				String sql2 = String.format("delete from company_coupon where couponID=?");
				// Create an object for executing the above command and delete from join table
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ps2.setString(1, couponID);
				ps2.executeUpdate();
			}else {
			throw new CouponCouldNotBeRemovedException();
			}
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} 
		catch (WrongEntryException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new WrongEntryException();
		}catch (CouponCouldNotBeRemovedException e) {
			// TODO: handle exception
			throw new CouponCouldNotBeRemovedException();
		}
		catch (SQLException e) {
			//System.out.println("coupon could not be removed. check coupon id");
			//e.printStackTrace();
			throw new CouponCouldNotBeRemovedException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public boolean removeCoupon(String couponID, Connection connection)
			throws NoFreeConnectionsException, ConnectionClosingException,
			WrongEntryException,CouponCouldNotBeRemovedException{
		
		boolean success = false;
		try {
			
			// Create sql command for delete one record from main coupons table
			String sql = String.format("delete from coupons where couponID=?");
			// Create an object for executing the above command:
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, couponID);

			// Execute:
			if(ps.executeUpdate()==1)
			{
				System.out.println("successfully removed coupon");
				success = true;
			}
			// Create an object for executing the above command and delete from join table
			String sql2 = String.format("delete from company_coupon where couponID=?");
			
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setString(1, couponID);
			ps2.executeUpdate();

		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} 
		catch (WrongEntryException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			throw new WrongEntryException();
		} catch (SQLException e) {
			//System.out.println("coupon could not be removed. check coupon id");
			//e.printStackTrace();
			throw new CouponCouldNotBeRemovedException();
		}
		

		return success;
	}

	@Override
	public Coupons updateCoupon(String couponID, String filedName, String newValue) throws 
	ConnectionClosingException, NoFreeConnectionsException, WrongEntryException, CouponDoesNotExistException, UnableToUpdateDataException {
		Connection connection = null;
		Coupons c = null;
		boolean success = false;
		System.out.println("this is update coupon from couponDBDAO");
		try { // first checking if coupon even exist
			Coupons c1=  getCoupon(couponID);
			if(c1 == null) {
				throw new CouponDoesNotExistException();
			}
		}catch ( NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (CouponDoesNotExistException e) {
			// TODO: handle exception
			throw new CouponDoesNotExistException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CouponDoesNotExistException();
		}
		catch (ConnectionClosingException e) {
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
		}
		try {
			connection = pool.returnConnection(connection);

			// Create an object for executing the above sql:
			PreparedStatement ps = connection.prepareStatement("UPDATE mydb.coupons SET " + filedName + " = ?  WHERE couponID = ? ");
			
			ps.setString(1, newValue);
			ps.setString(2, couponID);

			if(ps.executeUpdate()==1)
			{
				System.out.println("successfully updated coupon");
				success = true;
			}else {
				System.out.println("UnableToUpdateDataException has been thrown");
				throw new UnableToUpdateDataException();
			}

		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (UnableToUpdateDataException e) {
			// TODO: handle exception
			throw new UnableToUpdateDataException();
		}
//		catch (WrongEntryException e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//			throw new WrongEntryException();
//		}
		catch (SQLException e) {
			//System.out.println("the deteials could not be recivied in data base check for duplications");
			e.printStackTrace();
			throw new WrongEntryException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(success==true) { // checking if update was a success, if so find coupon and return it	
		try {
					
		c= getCoupon(couponID);
					
		}catch (SQLException e) {
			//System.out.println("could not find updated coupon id: " + couponID);
			e.printStackTrace();
			throw new CouponDoesNotExistException();
		}
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
	
			return c;
	}

	@Override
	public Coupons getCoupon(String couponID) 
			throws NoFreeConnectionsException, CouponDoesNotExistException, ConnectionClosingException {
		Connection connection = null;
		Coupons coupon = null;
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from coupons where couponID = ? ");
			ps.setString(1, couponID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				 String couponID1 = rs.getString("couponID");
				 String companieID = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				String START_DATE = rs.getString("START_DATE");
				String END_DATE = rs.getString("END_DATE");
				int AMOUNT = rs.getInt("AMOUNT");
				String MESSAGE = rs.getString("MESSAGE");
				double PRICE = rs.getDouble("PRICE");
				String IMAGE = rs.getString("IMAGE");
				CouponType type =  CouponType.valueOf(rs.getString("type"));

				 coupon = new Coupons(couponID1, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE,  type);
			}
			if(coupon==null) { // checking if coupon was found
				throw new CouponDoesNotExistException();
			}
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		}catch (CouponDoesNotExistException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CouponDoesNotExistException();
		}
		catch (SQLException e) {
			//System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return coupon;
	}
	

	@Override
	public Set<Coupons> getAllCoupons() 
			throws NoFreeConnectionsException, CouponDoesNotExistException, ConnectionClosingException {
		Connection connection = null;
		Set<Coupons> allCoupons = new HashSet<Coupons>();
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from coupons ");

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String couponID = rs.getString("couponID");
				String companieID = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				String START_DATE = rs.getString("START_DATE");
				String END_DATE = rs.getString("END_DATE");
				int AMOUNT = rs.getInt("AMOUNT");
				String MESSAGE = rs.getString("MESSAGE");
				double PRICE = rs.getDouble("PRICE");
				String IMAGE = rs.getString("IMAGE");
				CouponType type =  CouponType.valueOf(rs.getString("type")) ;
				
				allCoupons.add(new Coupons
						(couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type));

			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		}
		catch (SQLException e) {
			//System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();

		} catch (SQLException e) {
		//	ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCoupons;
	}

	@Override
	public Set<Coupons> getCouponByType(String couponType)
			throws WrongSqlSyntaxException, NoFreeConnectionsException, CouponDoesNotExistException, ConnectionClosingException, WrongEntryException {
		Connection connection = null;
		boolean success=false;
		Set<Coupons> allCoupons = new HashSet<Coupons>();
		try {
				
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data for coupons with required type
			PreparedStatement ps = connection.prepareStatement("select * from coupons where type= ? ");
			ps.setString(1, couponType);
		
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
		
			while (rs.next()) {

				String couponID = rs.getString("couponID");
				String companieID = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				String START_DATE = rs.getString("START_DATE");
				String END_DATE = rs.getString("END_DATE");
				int AMOUNT = rs.getInt("AMOUNT");
				String MESSAGE = rs.getString("MESSAGE");
				double PRICE = rs.getDouble("PRICE");
				String IMAGE = rs.getString("IMAGE");
				CouponType type =  CouponType.valueOf(rs.getString("type")) ;
				
				allCoupons.add(new Coupons
						(couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type));
				success=true;
			}
			if(success==false) {
				throw new WrongEntryException();
			}
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		}catch (WrongEntryException e) {
			// TODO: handle exception
			throw new WrongEntryException();
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CouponDoesNotExistException();
		}
		catch (Exception e) {
			//System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		}
	
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCoupons;
	}
	
	public Set<Coupons> getPurchasedCouponByType(String couponType,String custID) 
			throws WrongSqlSyntaxException, ConnectionClosingException, NoFreeConnectionsException, CouponDoesNotExistException, CustNotFoundException, NoPurchsedCouponFoundException {
		Connection connection = null;
		boolean success=false;
		Set<CustomerVsCoupons> allCustomerCoupons = new HashSet<CustomerVsCoupons>();
		Set<Coupons> allCustomerCouponsByType = new HashSet<Coupons>();
		System.out.println("this  is getPurchasedCouponByType from coupondbdao the type recied is : "+couponType);
		System.out.println("this  is getPurchasedCouponByType from coupondbdao the custid recied is : "+custID);
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		try { // first checking if customer exist
			Customers c = customerDBDAO.getCustomer(custID);
			if(c==null) {
		throw new CustNotFoundException();
			}
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}
		catch(Exception e){
			
		}
			
		try { // get coupons from join table by cust id				
			connection = pool.returnConnection(connection);
			System.out.println("the cust id recibed is: "+custID);
			System.out.println("the type recied is: "+couponType);
			PreparedStatement ps = connection.prepareStatement("select * from mydb.customervscoupons where custID = ? ");
			ps.setString(1, custID);
			System.out.println("this is befor executeQuery");
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
			System.out.println("this is after executeQuery");
			while (rs.next()) {
				System.out.println("this is starting while loop of rs");
				String theCustID = rs.getString("custID");
				String couponID = rs.getString("couponID");
				String purchase_date = rs.getString("purchase_date");
			
				System.out.println("test, " +couponID);				
				allCustomerCoupons.add(new CustomerVsCoupons(theCustID, couponID, purchase_date));
				success=true;
			}
			if(success==false) {
				throw new NoPurchsedCouponFoundException();
			}
			System.out.println("this is after while loop");
			
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		}catch (NoPurchsedCouponFoundException e) {
			// TODO: handle exception
			throw new NoPurchsedCouponFoundException();
		}
		catch (SQLException e) {
			// TODO: handle exception
			throw new NoPurchsedCouponFoundException();
		}
		catch (Exception e) {
			//System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		}
		try { // going through the set of allCustomerCoupons and find correct coupons by type and id
			System.out.println("this is starting for each");
				for(CustomerVsCoupons c :allCustomerCoupons )	
				{
					System.out.println("this is befor ps2");
			// Create a statement object which can read data:
			PreparedStatement ps2 = connection.prepareStatement
					("select * from mydb.coupons where couponID= ? and type = ?");
			ps2.setString(1, c.coupon_id);
			ps2.setString(2, couponType);
			System.out.println("this is befor rs2 excecute query");
			// Execute the query and return an object which contains the data:
			ResultSet rs2 = ps2.executeQuery();
			System.out.println("this is befor while loop 2");
			while (rs2.next()) {
				System.out.println("this is starting while loop 2");
				String couponID = rs2.getString("couponID");
				String companieID = rs2.getString("companieID");
				String TITLE = rs2.getString("TITLE");
				String START_DATE = rs2.getString("START_DATE");
				String END_DATE = rs2.getString("END_DATE");
				int AMOUNT = rs2.getInt("AMOUNT");
				String MESSAGE = rs2.getString("MESSAGE");
				double PRICE = rs2.getDouble("PRICE");
				String IMAGE = rs2.getString("IMAGE");
				CouponType type =  CouponType.valueOf(rs2.getString("type")) ;
											
				allCustomerCouponsByType.add(new Coupons
						(couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type));
				System.out.println("test2, "+couponID);
			}
			if(allCustomerCouponsByType.isEmpty()) { // checking if any records were found
				throw new NoPurchsedCouponFoundException();
			}
				}
						
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		}catch (NoPurchsedCouponFoundException e) {
			// TODO: handle exception
			throw new NoPurchsedCouponFoundException();
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new NoPurchsedCouponFoundException();
		}
		catch (Exception e) {
			//System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		}

		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
		//	e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCustomerCouponsByType;
	}
	
	public Set<Coupons> getPurchasedCouponByPrice(double PRICE,String custID) 
			throws WrongSqlSyntaxException, NoFreeConnectionsException, CouponDoesNotExistException, ConnectionClosingException, NoPurchsedCouponFoundException, WrongEntryException, CustNotFoundException {
		Connection connection = null;
		boolean success=false;
		Set<CustomerVsCoupons> allCustomerCoupons = new HashSet<CustomerVsCoupons>();
		Set<Coupons> allCustomerCouponsByPrice = new HashSet<Coupons>();
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		try { // first checking if customer exist
			Customers c = customerDBDAO.getCustomer(custID);
			if(c==null) {
		throw new CustNotFoundException();
			}
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}
		catch(Exception e){
			
		}
		try {  // get coupons from join table by cust id				
			connection = pool.returnConnection(connection);
			
			PreparedStatement ps = connection.prepareStatement("select * from customervscoupons where custID= ? ");
			ps.setString(1, custID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {

				String theCustID = rs.getString("custID");
				String couponID = rs.getString("couponID");
				String purchase_date = rs.getString("purchase_date");
				
				allCustomerCoupons.add(new CustomerVsCoupons(theCustID, couponID, purchase_date));
				success = true;
			}
			if(success == false) { // check if any records were found
				throw new NoPurchsedCouponFoundException();
			}				
			
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (NoPurchsedCouponFoundException e) {
			//System.out.println("could not find requested coupon");
			throw new NoPurchsedCouponFoundException();
		}catch(SQLException e) {
			e.printStackTrace();
			throw new WrongEntryException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(success==true) {		
		
		try {
			success=false;
			for(CustomerVsCoupons c :allCustomerCoupons )	
			{
		// Create a statement object which can read data:
		PreparedStatement ps2 = connection.prepareStatement
				("select * from coupons where couponID= ? and PRICE = ?");
		ps2.setString(1, c.coupon_id);
		ps2.setDouble(2, PRICE);
	
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
			double thePRICE = rs2.getDouble("PRICE");
			String IMAGE = rs2.getString("IMAGE");
			CouponType type =  CouponType.valueOf(rs2.getString("type")) ;
					
			success=true;
			allCustomerCouponsByPrice.add(new Coupons
					(couponID, companieID, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, thePRICE, IMAGE, type));
		}if(success == false) {
			throw new NoPurchsedCouponFoundException();
		}
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CouponDoesNotExistException();
		}catch (NoPurchsedCouponFoundException e) {
			// TODO: handle exception
			throw new NoPurchsedCouponFoundException();
		}
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCustomerCouponsByPrice;
	}
	
	public Set<CustomerVsCoupons> getPurchasedCouponByCustId(String custID) 
			throws WrongSqlSyntaxException, NoFreeConnectionsException, CouponDoesNotExistException, ConnectionClosingException, CustNotFoundException {
		Connection connection = null;
		Set<CustomerVsCoupons> allCustomerCoupons = new HashSet<CustomerVsCoupons>();
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		try { // first checking if customer exist
			Customers c = customerDBDAO.getCustomer(custID);
			if(c==null) {
		throw new CustNotFoundException();
			}
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}catch (CustNotFoundException e) {
			// TODO: handle exception
			throw new CustNotFoundException();
		}
		catch(Exception e){
			
		}
		
		try {
				
			connection = pool.returnConnection(connection);
			
			PreparedStatement ps = connection.prepareStatement("select * from customervscoupons where custID= ? ");
			ps.setString(1, custID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {

				String theCustID = rs.getString("custID");
				String couponID = rs.getString("couponID");
				String purchase_date = rs.getString("purchase_date");
				
				allCustomerCoupons.add(new CustomerVsCoupons(theCustID, couponID, purchase_date));
			}
					
			
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} 
		catch (Exception e) {
		//	System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		}

		try {
			connection.close();

		} catch (SQLException e) {
		//	ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCustomerCoupons;
	}
	
	public Set<Coupons> getCouponByCompId(String companieID)
			throws WrongSqlSyntaxException, ConnectionClosingException, NoFreeConnectionsException, CouponDoesNotExistException, CompanieNotFoundException {
		Connection connection = null;
		Set<Coupons> allCoupons = new HashSet<Coupons>();
		try {
			Companies c=  this.companyDBDAO.getCompany(companieID);
			if(c == null) {
				throw new CompanieNotFoundException();
			}
			
		}catch (CompanieNotFoundException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}catch (ConnectionClosingException  e) {
			// TODO: handle exception
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (WrongEntryException e) {
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
		}
		try {
					
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from coupons where companieID= ? ");
			ps.setString(1, companieID);
		
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
		
			while (rs.next()) {

				String couponID = rs.getString("couponID");
				String companieID1 = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				String START_DATE = rs.getString("START_DATE");
				String END_DATE = rs.getString("END_DATE");
				int AMOUNT = rs.getInt("AMOUNT");
				String MESSAGE = rs.getString("MESSAGE");
				double PRICE = rs.getDouble("PRICE");
				String IMAGE = rs.getString("IMAGE");
				CouponType type =  CouponType.valueOf(rs.getString("type")) ;
			
				allCoupons.add(new Coupons
						(couponID, companieID1, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE, IMAGE, type));

			}
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} 
		catch (Exception e) {
		//	System.out.println("could not find requested coupon");
			throw new CouponDoesNotExistException();
		}

		try {
			connection.close();

		} catch (SQLException e) {
//			ConnectionClosingException ee = new ConnectionClosingException();
//			e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();
		return allCoupons;
	}
	
}
