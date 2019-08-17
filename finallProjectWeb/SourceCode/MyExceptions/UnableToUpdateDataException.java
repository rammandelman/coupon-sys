package MyExceptions;

public class UnableToUpdateDataException extends Exception{
	
	public UnableToUpdateDataException()
	{
		super("unable to update data check values");
		System.out.println("unable to update data check values");
	}

}
