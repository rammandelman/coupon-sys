package interfaces;

import java.util.Collection;
import java.util.Set;

import DataBases.CouponType;
import DataBases.Coupons;
import DataBases.Customers;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponAllreadyExistException;
import MyExceptions.CouponCouldNotBeRemovedException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;

public interface CouponDAO {
	
	public Coupons createCoupon
	(String couponID,String COMPANY_ID,String TITLE,String START_DATE,String END_DATE,int AMOUNT,String MESSAGE,double PRICE,String IMAGE, CouponType type)
			throws CouponDoesNotExistException,NoFreeConnectionsException,WrongEntryException,
			ErrorWritingToJoinTblException,ConnectionClosingException, CouponAllreadyExistException, CompanieNotFoundException;
	
	public boolean removeCoupon(String couponID)throws NoFreeConnectionsException, ConnectionClosingException,
	WrongEntryException,CouponCouldNotBeRemovedException;
	
	public Coupons updateCoupon(String couponID, String filedName, String newValue)throws ConnectionClosingException,
	NoFreeConnectionsException,WrongEntryException,CouponDoesNotExistException, UnableToUpdateDataException;
	
	public Coupons getCoupon(String couponId)throws NoFreeConnectionsException,
	CouponDoesNotExistException,ConnectionClosingException;
	
	public Set<Coupons> getAllCoupons() 
			throws NoFreeConnectionsException,CouponDoesNotExistException,ConnectionClosingException;
	
	public Set<Coupons> getCouponByType(String couponType) 
			throws WrongSqlSyntaxException, NoFreeConnectionsException,CouponDoesNotExistException,ConnectionClosingException, WrongEntryException;
	
	

}
