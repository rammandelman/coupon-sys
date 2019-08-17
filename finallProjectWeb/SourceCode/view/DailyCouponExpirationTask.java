package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import DataBases.CouponType;
import DataBases.Coupons;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponCouldNotBeRemovedException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import controler.ConnectionPool;

/**
 * 
 * @author ram
 *this class is the thread that delete expired coupons
 */
public class DailyCouponExpirationTask  implements Runnable {
	ConnectionPool pool = ConnectionPool.getConnectionPool();
	CouponDBDAO coupon = new CouponDBDAO();
	boolean quit = false;
	
	public DailyCouponExpirationTask()
	{
	
	}
	
	
	/**
	 * this run func will check for expired coupons and delete them
	 */
@Override
public void run() {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	Date theCurrenDate = new Date();
	dateFormat2.format(theCurrenDate);
	Connection connection = null;
	Set<Coupons> expiredCoupons = new HashSet<Coupons>();
	try {
		connection = pool.returnConnection(connection);

		while(this.quit == false) {
			System.out.println("starting daily expired coupons checking");		
		// Create a statement object which can read data:
		PreparedStatement ps = connection.prepareStatement("select * from coupons");

		// Execute the query and return an object which contains the data:
		ResultSet rs = ps.executeQuery();
		SimpleDateFormat START_DATE = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat END_DATE = new SimpleDateFormat("yyyy-MM-dd");

		while (rs.next()) {
			//System.out.println("finding new coupon...");
			String couponID = rs.getString("couponID");

			String companieID = rs.getString("companieID");
			String TITLE = rs.getString("TITLE");

			Date date = START_DATE.parse(rs.getString("START_DATE"));

			START_DATE.format(date);
			Date date2 = END_DATE.parse(rs.getString("END_DATE"));
			END_DATE.format(date2);
			int AMOUNT = rs.getInt("AMOUNT");
			String MESSAGE = rs.getString("MESSAGE");
			double PRICE = rs.getDouble("PRICE");
			String IMAGE = rs.getString("IMAGE");
			CouponType type =  CouponType.valueOf(rs.getString("type")) ;

			 Coupons coupon = new Coupons(couponID, companieID, TITLE, date,
					 date2, AMOUNT, MESSAGE, PRICE, IMAGE, type);

			// allCoupons.add(coupon);

			//System.out.println(START_DATE.format(date) + " " + couponID);
			//System.out.println(END_DATE.format(date2) + " " + couponID);
			//System.out.println(type);
			if (date2.after(theCurrenDate)) {
				//System.out.println("coupon is still valid");
			} else {
				//System.out.println("coupon is expierd");
				expiredCoupons.add(coupon);
			}
			/**
			 * this for each loop will go over the expired coupons set and delete each one at a time cy calling the right func
			 */
			for(Coupons c : expiredCoupons)
			{
				this.coupon.removeCoupon(c.couponID, connection);
			}
			if(this.quit == true) {
				break;
			}
			
		}
		System.out.println("finished the daily coupon expiration checking");
		try {
			Thread.sleep(86400000);
		} catch (InterruptedException e1) {
			System.out.println("could not sleep");
			e1.printStackTrace();
		}
		
		}
		
	} catch (NoFreeConnectionsException ee) {

	}

	catch (SQLException e) {
		System.out.println("could not find requested coupon");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (CouponCouldNotBeRemovedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FailedToConnectDb e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		connection.close();

	} catch (SQLException e) {
		ConnectionClosingException ee = new ConnectionClosingException();
		//e.printStackTrace();
	}
	pool.connectionsCountDecrement();
	
	
}	
public void stopTask()
{
	quit = true;
}
	
}
