package MyExceptions;

public class NoPurchsedCouponFoundException extends Exception{
	
	public NoPurchsedCouponFoundException() {
		super("no purchased coupon found");
		System.out.println("no purchased coupon found");
	}

}
