package controler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import MyExceptions.ConnectionClosingException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongSqlSyntaxException;

import java.sql.*;

/**
 * 
 * @author ram
 *this is the connection controler class
 */
public class ConnectionPool {
	

	// this variable will count the amount of connection created to the data base
	private int connectionsCount = 0;

	// this variable will count the amount of connection valid and used
	private int numberOfVaildConnections = 0;

	// this method returns the number of connections that are currently valid and
	// used
	public int getNumberOfVaildConnections() {
		return numberOfVaildConnections;
	}

	// this method returns the current connection count

	public int getConnectionsCount() {
		return connectionsCount;
	}

	// this set will hold all the connections to the db
	private Set<Connection> allConnections = new HashSet<Connection>();

	public Set<Connection> getConnectionSet() {
		return this.allConnections;
	}

	// this is the instance of the class
	private static ConnectionPool cp;

	// private constructor to maintain singleton class
	private ConnectionPool() {

	}

	/**
	 * 
	 * @return
	 * this is the func that returns the instance 
	 */
	// this method will return the referenced variable of the singleton class.
	public static ConnectionPool getConnectionPool() {
		if (cp == null) {
			cp = new ConnectionPool();
		}
		return cp;
	}

	/**
	 * this method check all the connections if are open
	 * @throws WrongSqlSyntaxException 
	 */
	// this method check all the connections
	public int checkConnections() throws WrongSqlSyntaxException  {
		numberOfVaildConnections = 0 ;
		//System.out.println("this is befor check connections method, all connections count: " + connectionsCount);
		for (Connection c : allConnections) { // go through the array of connections
			try {
				if (!(c.isClosed())) {
					numberOfVaildConnections++;
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new WrongSqlSyntaxException();
			}
		}
		connectionsCount = numberOfVaildConnections;
		return connectionsCount;
	//	System.out.println("this is after  all connections count, number of valid connection " + numberOfVaildConnections);
	}
/**
 * 
 * @param connection
 * @return
 * @throws NoFreeConnectionsException
 * htis is the main func that return connections to data base
 * @throws FailedToConnectDb 
 */
	// the method is synchronized due to accessibility from multiple threads.
	//this methos return a valid open connection to the data base if connection can be made.
	public synchronized Connection returnConnection(Connection connection) throws NoFreeConnectionsException, FailedToConnectDb {
		synchronized (allConnections) {
		System.out.println("connections count befor creating connection : " + connectionsCount);
		connection = null;
		if (connectionsCount < 20) { // checking if connection count has not reached the preset amount
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "1234");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new FailedToConnectDb();								
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				throw new FailedToConnectDb();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				throw new FailedToConnectDb();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new FailedToConnectDb();
			}
			allConnections.add(connection);
			connectionsCount++;
			System.out.println("connections count after creating connection : " + connectionsCount);
		} else { // if connections count grater than 20
			throw new NoFreeConnectionsException();
		}
		}
		return connection;
	}

	/**
	 * this func closes all connections to db
	 * @throws WrongSqlSyntaxException 
	 * @throws ConnectionClosingException 
	 */
	public void closeAllConnections() throws WrongSqlSyntaxException {
		for (Connection c : this.allConnections) {
			try {
				c.close();
			} catch (ConnectionClosingException e) {
				// TODO Auto-generated catch block
				System.out.println("unable to close the connection : "  +  c.toString());
				e.printStackTrace();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//ConnectionClosingException ee = new ConnectionClosingException();
				e.printStackTrace();
				//throw new ConnectionClosingException();
			}
	
		}
		checkConnections();
		System.out.println("done all connections closed");
	}
	
	public synchronized void connectionsCountIncrement() //increment the connection count to the db
	{
		//System.out.println("connections count befor connection increment : " + connectionsCount);
		synchronized (allConnections) {
			connectionsCount++;
			
		}
		//System.out.println("connections count after connection increment : " + connectionsCount);	
		
	}
	
	public synchronized void connectionsCountDecrement()  //decrement the connection count to the db
	{
		//System.out.println("connections count befor connection decrement : " + connectionsCount);
		synchronized (allConnections) {
			connectionsCount--;
			
		}
	//	System.out.println("connections count befor connection decrement : " + connectionsCount);
	
	}

}
