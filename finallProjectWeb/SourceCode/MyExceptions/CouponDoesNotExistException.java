package MyExceptions;

public class CouponDoesNotExistException extends Exception {
	
	public CouponDoesNotExistException() {
		super("the specified coupon is not found");
		System.out.println("the specified coupon is not found");
	}
	

}
