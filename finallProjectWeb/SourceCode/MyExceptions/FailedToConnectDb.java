package MyExceptions;

import java.sql.SQLException;
/**
 * 
 * @author ram
 *this class is an exception thrown when you try to connect with a wrong user or pass to db
 */
public class FailedToConnectDb extends Exception{
	
	public FailedToConnectDb()
	{
		super();
		System.out.println("faild to connect to db ");
	}

}
