package MyExceptions;

public class CompAllreadyExistException extends Exception{
	
	public CompAllreadyExistException()
	{
		super("an equel id already exist in the system");
		System.out.println("an equel id already exist in the system");
	}
	

}
