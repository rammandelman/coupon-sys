package MyExceptions;

public class CustRemoveException extends Exception{
	
	public CustRemoveException() {
		super("could not remove customer from database");
		System.out.println("could not remove customer from database");
	}

}
