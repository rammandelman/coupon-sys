package MyExceptions;

import java.sql.SQLException;

/**
 * 
 * @author ram
 *this class is the exception thrown when you try enter invalid data to db table
 */
public class WrongEntryException extends SQLException{
	
	public WrongEntryException()
	{
		super("the deteials could not be recivied in data base check that your values match the required entries.");
		System.out.println("the deteials could not be recivied in data base check that your values match the required entries.");
	}
	
	  
}
