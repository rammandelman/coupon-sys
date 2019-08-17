package MyExceptions;

public class CouponAllreadyExistException extends Exception {
	
	public CouponAllreadyExistException() {
		super("coupon already exist in system!");
		System.out.println("coupon already exist in system!");
	}

}
