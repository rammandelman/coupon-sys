package MyExceptions;

public class CouponCouldNotBeRemovedException extends Exception{
	
	public CouponCouldNotBeRemovedException()
	{
		super("oupon could not be removed. check coupon id");
		System.out.println("oupon could not be removed. check coupon id");
	}


}
