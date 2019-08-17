package DataBases;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author ram
 *this class id used to hold db objects from the companies table
 */
public class Companies {
	
/**
 * overriding to toString 
 */
	@Override
	public String toString() {
		return "Companies [companyCoupons=" + companyCoupons + ", theId=" + theId + ", COMP_NAME=" + COMP_NAME
				+ ", EMAIL=" + EMAIL + ", password=" + password + "]";
	}

	Set<Coupons> companyCoupons = new HashSet<Coupons>();

	public Companies(String theId, String COMP_NAME, String EMAIL, String password) {
		this.theId = theId;
		this.COMP_NAME = COMP_NAME;
		this.EMAIL = EMAIL;
		this.password = password;
		}

	
public 	String theId;
public String COMP_NAME;
public String EMAIL;
public String password;

}
