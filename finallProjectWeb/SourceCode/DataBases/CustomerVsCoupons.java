package DataBases;

/**
 * 
 * @author ram
 *this class id used to hold db objects from the CustomerVsCoupons table
 */
public class CustomerVsCoupons {
public CustomerVsCoupons( String cust_id, String coupon_id, String purchase_date)
{
	this.cust_id= cust_id;
	this.coupon_id = coupon_id;
	this.purchase_date = purchase_date;
	
}

	
	public String cust_id;
	public String coupon_id;
	public String purchase_date;
	
	/**
	 * overriding to string 
	 */
	@Override
	public String toString() {
		return "CustomerVsCoupons [cust_id=" + cust_id + ", coupon_id=" + coupon_id + ", purchase_date=" + purchase_date
				+ "]";
	}
	
}
