package MyExceptions;

public class CustNotFoundException extends Exception{

	public CustNotFoundException() {
		super("could not find requested customer");
		System.out.println("could not find requested customer");
	}

}
