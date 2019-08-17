package MyExceptions;
/**
 * 
 * @author ram
 *this exception is thrown when you try to connect to a facade with a wrong user or pass
 */
public class wrongUserOrPassException extends Exception {
	
	public  wrongUserOrPassException()
	{
		super("wrong user or password");
		System.out.println("wrong user or password");
	}

}
