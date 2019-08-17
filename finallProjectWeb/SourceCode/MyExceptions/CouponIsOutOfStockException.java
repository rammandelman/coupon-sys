package MyExceptions;

public class CouponIsOutOfStockException extends Exception {
	
	public CouponIsOutOfStockException() {
		super("this coupon is out of stock");
		System.out.println("this coupon is out of stock");
	}

}
