package DataBases;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * 
 * @author ram
 *this class id used to hold db objects from the customers table
 */
public class Customers {
	/**
	 * overriding to string 
	 */
	@Override
	public String toString() {
		return "Customers [custID=" + custID + ", CUST_NAME=" + CUST_NAME + ", passwrod=" + passwrod + "]";
	}
	public Customers( String custID, String CUST_NAME, String passwrod)
	{
		this.custID = custID;
		this.CUST_NAME = CUST_NAME;
		this.passwrod = passwrod;
	}

	public String custID;
	public String CUST_NAME;
	public String passwrod;

}
