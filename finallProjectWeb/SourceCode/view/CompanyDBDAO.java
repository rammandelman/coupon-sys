package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import DataBases.Companies;
import DataBases.CouponType;
import DataBases.Coupons;
import Logins.Client;
import MyExceptions.CompAllreadyExistException;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouldNotRemoveCompException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import MyExceptions.wrongUserOrPassException;
import controler.ConnectionPool;
import interfaces.CompanyDAO;
import interfaces.CrudDB;

public class CompanyDBDAO extends Client implements CompanyDAO {

	
	public CompanyDBDAO() {
		
	}
	/*
	 * this object get the instance of connection pool
	 */
	private ConnectionPool pool = ConnectionPool.getConnectionPool();
	
	@Override
	public Companies createCompany(String companieID, String COMP_NAME, String EMAIL, String Password) throws NoFreeConnectionsException, WrongEntryException, CompanieNotFoundException,ConnectionClosingException, CompAllreadyExistException
			{
		Connection connection = null;
		Companies comp = null;
		boolean success=false;
		//this try will check if companie already exist before creating it
		try {
			Companies c=  getCompany(companieID);
			if(c != null) {
				throw new CompAllreadyExistException();
			}
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			
		}catch (ConnectionClosingException  e) {
			// TODO: handle exception
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (WrongEntryException e) {
			// TODO: handle exception
		}catch (CompanieNotFoundException e) {
			// companie not found, good to processed
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}
		try {
			connection = pool.returnConnection(connection);
			PreparedStatement ps = connection.prepareStatement("insert into companies VALUES(?, ?, ?, ?)");
			ps.setString(1, companieID);
			ps.setString(2, COMP_NAME);
			ps.setString(3, EMAIL);
			ps.setString(4, Password);
			ps.executeUpdate();

			PreparedStatement ps2 = connection.prepareStatement("insert into users VALUES(?, ?, 'companies')");
			ps2.setString(1, companieID);
			ps2.setString(2, Password);
			ps2.executeUpdate();

			PreparedStatement ps3 = connection.prepareStatement("select * from companies where companieID =  ? ");
			ps3.setString(1, companieID);

			ResultSet rs = ps3.executeQuery(); //testing to see if object created successfully  
			if (rs.next()) {
				System.out.println("successfully created companie user ");
				success = true;
			} else {
				throw new WrongEntryException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
			e.printStackTrace();

			throw new WrongEntryException();
			
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(success == true) {			
		
		try {
	
			comp = getCompany(companieID);
		} catch (SQLException e) {

			//System.out.println("error finding ceated comp id: " + companieID);
			e.printStackTrace();
			throw new CompanieNotFoundException();
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

		return comp;
	}

	@Override
	public boolean removeCompany(String compID) throws ConnectionClosingException,NoFreeConnectionsException,CouldNotRemoveCompException {
		Connection connection = null;
		boolean success = false;
		try {
			connection = pool.returnConnection(connection);
			String sql = String.format("delete from companies where companieID=?");

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, compID);

			if(ps.executeUpdate() == 1)
			{
				System.out.println("companie removed successfully");
				success = true;
			}else {
				throw new CouldNotRemoveCompException();
			}
		}catch (NoFreeConnectionsException e) {
			
			throw new NoFreeConnectionsException();
		}catch (SQLException e) {
			//System.out.println("could not remove company from companies check yur comp ID");
			success = false;
			throw new CouldNotRemoveCompException();
		} catch (CouldNotRemoveCompException e) {
			
			e.printStackTrace();
			throw new CouldNotRemoveCompException();
		} catch (FailedToConnectDb e) {
			
			e.printStackTrace();
		}

		try {
			String sql2 = String.format("delete from users where user_name=?");

			PreparedStatement ps3 = connection.prepareStatement(sql2);
			ps3.setString(1, compID);
			ps3.executeUpdate();
		} catch (SQLException e1) {
			//System.out.println("could not remove company from companies check yur comp ID");
			throw new CouldNotRemoveCompException();
		}

		try {
			connection.close();

		}  catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("failed to close connection "+connection.toString());
		}
		pool.connectionsCountDecrement();		
		
		return success;
	}

	@Override
	public Companies updateCompany(String compID, String filedName, String newValue)throws ConnectionClosingException,
		NoFreeConnectionsException,WrongEntryException,UnableToUpdateDataException,CompanieNotFoundException, CompAllreadyExistException
		{
		Connection connection = null;
		Companies comp= null;
		boolean success= false;
		try {
			Companies c= getCompany(compID);
			if(c==null) {
				throw new CompanieNotFoundException();
			}
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}
		catch (ConnectionClosingException  e) {
			// TODO: handle exception
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (WrongEntryException e) {
			// TODO: handle exception
		}catch (CompanieNotFoundException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}
		/*
		 * the code below update the data in the companies table
		 */
		try {

			connection = pool.returnConnection(connection);

			// Create an object for executing the above sql:

			PreparedStatement ps = connection
					.prepareStatement("UPDATE companies SET " + filedName + " = ?  WHERE companieID = ? ");
			// ps.setString(1, filedName);
			ps.setString(1, newValue);
			ps.setString(2, compID);

			if (ps.executeUpdate()==1)
			{
				System.out.println("companie updated successfully");
				success = true;
				
			}else {
				throw new UnableToUpdateDataException();
			}

		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new  NoFreeConnectionsException();
		} catch (WrongEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnableToUpdateDataException();
		} catch (SQLException e) {
			//System.out.println("unable to update data check values");
			throw new UnableToUpdateDataException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * the code below check if the filed to change is the companie ID then it makes the same change to users
		 * join table
		 */
		try {

			if (filedName.equals("companieID")) {

				PreparedStatement ps2 = connection
						.prepareStatement("UPDATE users SET user_name = ?  WHERE user_name = ? ");
				// ps.setString(1, filedName);
				ps2.setString(1, newValue);
				ps2.setString(2, compID);

				ps2.executeUpdate();
			}

		} catch (SQLException e) {
			//System.out.println("unable to update data check values");
			throw new UnableToUpdateDataException();
		}
		/*
		 * the code below check if the filed to change is the companie password then it makes the same change to users
		 * join table
		 */
		try {

			if (filedName.equals("password")) {

				PreparedStatement ps3 = connection
						.prepareStatement("UPDATE users SET password = ?  WHERE user_name = ? ");
				// ps.setString(1, filedName);
				ps3.setString(1, newValue);
				ps3.setString(2, compID);

				ps3.executeUpdate();
			}

		} catch (SQLException e) {
			//System.out.println("unable to update data check values");
			throw new UnableToUpdateDataException();
		}
		/**
		 * the code below finds the new updated object in companies table and return it to user
		 */
		if(success == true) {		
		try {
				
		comp = getCompany(compID);
				
		}catch (SQLException e) {
			// TODO: handle exception
			//System.out.println("could not fund the companie you updated. comp id: " + compID);
			throw new CompanieNotFoundException();
		}
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
		//	e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return comp;
	}

	@Override
	public boolean login(String companieID, String pass) {
		Connection connection = null;
		boolean loginn = false;
		try {

			try {
				connection = pool.returnConnection(connection);
			} catch (NoFreeConnectionsException e) {
				// TODO: handle exception
			} catch (FailedToConnectDb e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PreparedStatement ps = connection
					.prepareStatement("select * from companies where companieID = ? and password = ?");
			ps.setString(1, companieID);
			ps.setString(2, pass);
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
			System.out.println("failed to close connection "+connection.toString());
			e.printStackTrace();
		}
		pool.connectionsCountDecrement();

		return loginn;
	}

	@Override
	public Companies getCompany(String id)throws NoFreeConnectionsException,WrongEntryException,CompanieNotFoundException,ConnectionClosingException {
		Companies companie = null;
		Connection connection = null;
		System.out.println("this id recived to func is: "+id);
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from mydb.companies where companieID = ? ");
			ps.setString(1, id);
			System.out.println("this is befor query");
			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();
			System.out.println("this is after query");
			while (rs.next()) {
				System.out.println("this is inside while loop");
				String theId = rs.getString("companieID");
				String COMP_NAME = rs.getString("COMP_NAME");
				String EMAIL = rs.getString("EMAIL");
				String password = rs.getString("password");

				companie = new Companies(theId, COMP_NAME, EMAIL, password);
			}
			if(companie == null) {
				throw new CompanieNotFoundException();
			}
			System.out.println("this is after while loop");
		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new NoFreeConnectionsException();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("could not find companie");
			throw new CompanieNotFoundException();
			
		}catch (CompanieNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new CompanieNotFoundException();
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		try {
			connection.close();
			System.out.println("this is connection closing");
		} catch (SQLException e) {
			// ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return companie;
	}

	@Override
	public Set<Companies> getAllCompanies() throws ConnectionClosingException, NoFreeConnectionsException,WrongEntryException {
		Connection connection = null;
		Set<Companies> allCompanies = new HashSet<Companies>();
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from companies ");

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("starting while loop...");
				String theId = rs.getString("companieID");
				String COMP_NAME = rs.getString("COMP_NAME");
				String EMAIL = rs.getString("EMAIL");
				String password = rs.getString("password");

				allCompanies.add(new Companies(theId, COMP_NAME, EMAIL, password));
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
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return allCompanies;

	}

	@Override
	public Set<Coupons> getCoupons(String compID)throws NoFreeConnectionsException, WrongEntryException,WrongEntryException, CompanieNotFoundException, ConnectionClosingException, CouponDoesNotExistException {
		Connection connection = null;
		boolean success=false;
		Set<Coupons> allThecompaniesCoupons = new HashSet<Coupons>();
		try { // first check if such comp even exist
			Companies c=  getCompany(compID);
			if(c==null) {
				throw new CompanieNotFoundException();
			}
			
		}catch (NullPointerException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}catch (ConnectionClosingException  e) {
			// TODO: handle exception
		}catch (NoFreeConnectionsException e) {
			// TODO: handle exception
		}catch (WrongEntryException e) {
			// TODO: handle exception
		}catch (CompanieNotFoundException e) {
			// TODO: handle exception
			throw new CompanieNotFoundException();
		}
		try {
			connection = pool.returnConnection(connection);

			// Create a statement object which can read data:
			PreparedStatement ps = connection.prepareStatement("select * from mydb.coupons where companieID=?");
			ps.setString(1, compID);

			// Execute the query and return an object which contains the data:
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String couponID = rs.getString("couponID");
				String companieID1 = rs.getString("companieID");
				String TITLE = rs.getString("TITLE");
				Date START_DATE = rs.getDate("START_DATE");
				Date END_DATE = rs.getDate("END_DATE");
				int AMOUNT = rs.getInt("AMOUNT");
				String MESSAGE = rs.getString("MESSAGE");
				double PRICE = rs.getDouble("PRICE");
				String IMAGE = rs.getString("IMAGE");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				
				allThecompaniesCoupons.add(new Coupons(couponID, companieID1, TITLE, START_DATE, END_DATE, AMOUNT, MESSAGE, PRICE,
						IMAGE, type));
				success=true;
			}
			if(success==false) {
				throw new CouponDoesNotExistException();
			}
			
		} catch (NoFreeConnectionsException e) {
			// TODO: handle exception
			throw new NoFreeConnectionsException();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new WrongEntryException();
		} catch (CouponDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CouponDoesNotExistException();
		} catch (FailedToConnectDb e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (Coupons c : allThecompaniesCoupons) {
			System.out.println(c.couponID);
		}
		try {
			connection.close();

		} catch (SQLException e) {
			//ConnectionClosingException ee = new ConnectionClosingException();
			//e.printStackTrace();
			throw new ConnectionClosingException();
		}
		pool.connectionsCountDecrement();

		return allThecompaniesCoupons;
	}

}
