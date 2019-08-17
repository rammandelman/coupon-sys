package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import MyExceptions.ConnectionClosingException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import MyExceptions.wrongUserOrPassException;
import controler.ConnectionPool;
import controler.userType;


public class CouponSystem {
	ConnectionPool pool = ConnectionPool.getConnectionPool();
	private static CouponSystem cs;	
	private DailyCouponExpirationTask couponsDestroyer = null;
	private Thread thread;	
/**
 * this is the singleton constructor that also start the thread for deleting expired coupons
 */
	private CouponSystem() {
		
		//new Thread(couponsDestroyer).start();
		thread = new Thread(couponsDestroyer);
		this.couponsDestroyer = new DailyCouponExpirationTask();
		thread.start();
	}
	
	public static CouponSystem getInstance() {
		if (cs == null) {
			cs = new CouponSystem();
		}
		return cs;
	}

	/**
	 * 
	 * @param user_name
	 * @param pass
	 * @param type
	 * @return
	 * this is main login func
	 * @throws ConnectionClosingException 
	 * @throws NoFreeConnectionsException 
	 * @throws wrongUserOrPassException 
	 * @throws WrongEntryException 
	 */
	public CouponClinetFacade login(String user_name, String pass, userType type) throws ConnectionClosingException, NoFreeConnectionsException, wrongUserOrPassException, WrongEntryException
	{
		CouponClinetFacade yourFacade = null;
			Connection connection = null;
			 boolean loginn = false;
			 String typee = null;
			try {

				try { // getting connection to db to check user and pass
					connection = pool.returnConnection(connection);
				}catch (NoFreeConnectionsException e) {
					// TODO: handle exception
					throw new NoFreeConnectionsException();
				} catch (FailedToConnectDb e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PreparedStatement ps = connection
						.prepareStatement("select * from mydb.users where user_name = ? and password = ? ");
				ps.setString(1, user_name);
				ps.setString(2, pass);
				
				ResultSet rs = ps.executeQuery();
				// this loop check the result and if user and pass correct it will create and refer the correct facade
						while(rs.next())
						{
							loginn = true;
						//System.out.println("starting while loop");
							String userName = rs.getString("user_name");
							String password = rs.getString("password");
							String yourType = rs.getString("user_type");
							typee = yourType ;
							
							//System.out.println(userName +" " + password);
							if(user_name.equals(userName) && pass.equals(password))
							{
								System.out.println("checking type");
							if (yourType.equals("companies"))
							{
								System.out.println("logged as company");
								CompanyFacade companyFacade = new CompanyFacade();
								System.out.println("companie facade created");
								
								yourFacade = new CouponClinetFacade(companyFacade);
								yourFacade.type = userType.companies;
								
							}
							if (yourType.equals("customers"))
							{
								System.out.println("logged as customer");
								CustomerFacade customerFacade = new CustomerFacade();
								
								yourFacade = new CouponClinetFacade(customerFacade);
								yourFacade.type = userType.customers;
							}
							if (yourType.equals("admin"))
							{
								System.out.println("logged as admin");
								AdminFacade adminFacade = new AdminFacade();
								System.out.println("admin facade created");
								
								yourFacade = new CouponClinetFacade(adminFacade);
								yourFacade.type = userType.admin;
							}
							
							}
			
						}
						if(loginn == false)
						{
							throw new wrongUserOrPassException();
						}
				
					} catch (NoFreeConnectionsException e) {
						throw new NoFreeConnectionsException();
				// TODO: handle exception
			}
			 catch (wrongUserOrPassException e) {
				// TODO Auto-generated catch block
				 throw new wrongUserOrPassException();
				
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new WrongEntryException();
			}
			try {
				connection.close();

			} catch (SQLException e) {
				//ConnectionClosingException ee = new ConnectionClosingException();
				throw new ConnectionClosingException();
			}
			pool.connectionsCountDecrement();
			
		return yourFacade;
	
	}
	
	/**
	 * this func closes all active connection to data base and stop the daily copuon destroyer
	 */
	public void shutDown() {
		couponsDestroyer.stopTask();
		thread.interrupt();
		try {
			pool.closeAllConnections();
		} catch (WrongSqlSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x = pool.getNumberOfVaildConnections();
		System.out.println("the number of valid connections after shutdown func is: "+x);
		
	}

}
