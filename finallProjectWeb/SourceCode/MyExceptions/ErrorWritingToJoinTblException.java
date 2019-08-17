package MyExceptions;

public class ErrorWritingToJoinTblException extends Exception {
	
	public ErrorWritingToJoinTblException() {
		super("error writing requested data to join table");
		System.out.println("error writing requested data to join table");
	}
	

}
