package MyExceptions;

public class CouldNotRemoveCompException extends Exception {
	
	public CouldNotRemoveCompException() {
		super("could not remove company from companies check yur comp ID");
		System.out.println("could not remove company from companies check yur comp ID");
	}

}
