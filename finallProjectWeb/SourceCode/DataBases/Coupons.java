package DataBases;

import java.util.Date;
/**
 * 
 * @author ram
 *this class id used to hold db objects from the coupons table
 */
public class Coupons {
	/**
	 * overriding to string 
	 */
	@Override
	public String toString() {
		return "Coupons [couponID=" + couponID + ", companieID=" + companieID + ", TITLE=" + TITLE + ", START_DATE="
				+ START_DATE + ", END_DATE=" + END_DATE + ", AMOUNT=" + AMOUNT + ", MESSAGE=" + MESSAGE + ", PRICE="
				+ PRICE + ", IMAGE=" + IMAGE + ", type=" + type + ", START_DATED=" + START_DATED + ", END_DATED="
				+ END_DATED + "]";
	}

	/**
	 * this constructor is for holding coupon object with dates formats
	 * @param couponID
	 * @param companieID
	 * @param TITLE
	 * @param START_DATE
	 * @param END_DATE
	 * @param AMOUNT
	 * @param MESSAGE
	 * @param PRICE
	 * @param IMAGE
	 * @param type
	 */
	public Coupons(String couponID, String companieID, String TITLE, Date START_DATE, Date END_DATE, int AMOUNT,
			String MESSAGE, double PRICE, String IMAGE,CouponType type) {
		this.couponID = couponID;
		this.companieID = companieID;
		this.TITLE = TITLE;
		this.START_DATED = START_DATE;
		this.END_DATED = END_DATE;
		this.AMOUNT = AMOUNT;
		this.MESSAGE = MESSAGE;
		this.PRICE = PRICE;
		this.IMAGE = IMAGE;
		this.type = type;

	}
	
	public Coupons(String couponID, String companieID, String TITLE, String START_DATE, String END_DATE, int AMOUNT,
			String MESSAGE, double PRICE, String IMAGE, CouponType type) {
		this.couponID = couponID;
		this.companieID = companieID;
		this.TITLE = TITLE;
		this.START_DATE = START_DATE;
		this.END_DATE = END_DATE;
		this.AMOUNT = AMOUNT;
		this.MESSAGE = MESSAGE;
		this.PRICE = PRICE;
		this.IMAGE = IMAGE;
		this.type = type;

	}


	public String couponID;
	public String companieID;
	public String TITLE;
	public String START_DATE ;
	public String END_DATE;
	public int AMOUNT;
	public String MESSAGE;
	public double PRICE;
	public String IMAGE;
	public CouponType type;
	
	
	public Date START_DATED;
	public Date END_DATED;
	
	
	

}
