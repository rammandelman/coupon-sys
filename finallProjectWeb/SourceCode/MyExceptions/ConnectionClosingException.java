package MyExceptions;

import java.sql.SQLException;

public class ConnectionClosingException extends SQLException{
	public ConnectionClosingException()
	{
		super("error trying to close connection");
		System.out.println("error trying to close connection");
	}

}
