package MyExceptions;

import java.sql.SQLException;
/**
 * 
 * @author ram
 *this exception thrown when you have exceeded the allowed number of connections 
 */

public class NoFreeConnectionsException extends SQLException{
	
	public NoFreeConnectionsException() {
		super("no available connections at this time.");
		System.out.println("no available connections at this time.");
	}
	
	

}
