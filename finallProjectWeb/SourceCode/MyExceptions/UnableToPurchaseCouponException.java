package MyExceptions;

public class UnableToPurchaseCouponException extends Exception{
	
	public UnableToPurchaseCouponException() {
		super("unable to purchase coupon. check your cust id and coupon id are valid, you might have already purchased that coupon.");
		System.out.println("unable to purchase coupon. check your cust id and coupon id are valid, you might have already purchased that coupon.");
	}

}
