package MyExceptions;

import java.sql.SQLException;

/**
 * 
 * @author ram
 *this excpetion class is thrown when the sql syntax is wrong
 */
public class WrongSqlSyntaxException extends Exception {
	

	public WrongSqlSyntaxException()
	{
		super("the syntax you have enterd is wrong");
		System.out.println("the syntax you have enterd is wrong");
	}

}
