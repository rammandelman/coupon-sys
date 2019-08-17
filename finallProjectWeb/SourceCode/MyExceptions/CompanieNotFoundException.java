package MyExceptions;

public class CompanieNotFoundException extends Exception{
	public CompanieNotFoundException()
	{
		super("error finding  comp check search id");
		System.out.println("error finding  comp check search id");
	}

}
