package MyExceptions;

public class CustAlreadyExistException extends Exception {
	
	public CustAlreadyExistException() {
		super("customer already exist !");
		System.out.println("customer already exist !");
	}

}
