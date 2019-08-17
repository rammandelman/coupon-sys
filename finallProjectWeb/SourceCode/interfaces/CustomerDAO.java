package interfaces;

import java.util.Collection;
import java.util.Set;

import DataBases.Companies;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import DataBases.Customers;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CouponIsOutOfStockException;
import MyExceptions.CustAlreadyExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.CustRemoveException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.NoPurchsedCouponFoundException;
import MyExceptions.UnableToPurchaseCouponException;
import MyExceptions.UnableToUpdateDataException;
import MyExceptions.WrongEntryException;

public interface CustomerDAO {

	public Customers createCustomer(String custID , String CUST_NAME , String PASSWORD)
			throws NoFreeConnectionsException,WrongEntryException, WrongEntryException, CustNotFoundException, ConnectionClosingException, CustAlreadyExistException ;
	
	public boolean removeCustomer(String custID) throws NoFreeConnectionsException, WrongEntryException, CustRemoveException, ConnectionClosingException;
	
	public Customers updaetCustomer(String custID, String filedName, String newValue) throws ConnectionClosingException, WrongEntryException, NoFreeConnectionsException, ErrorWritingToJoinTblException, CustNotFoundException, UnableToUpdateDataException;
	
	public Customers getCustomer(String custID) throws NoFreeConnectionsException, WrongEntryException, ConnectionClosingException, CustNotFoundException;
	
	public Set<Customers> getAllCustomers() throws NoFreeConnectionsException, WrongEntryException, ConnectionClosingException;
	
	public Set<Coupons> getCoupons(String custID) throws ConnectionClosingException, NoFreeConnectionsException, WrongEntryException, CouponDoesNotExistException, CustNotFoundException, NoPurchsedCouponFoundException;
	
	public boolean login(String custID, String pass) throws ConnectionClosingException;
	
	public Coupons purchase(String couponId, String custID) throws NoFreeConnectionsException, UnableToPurchaseCouponException, CouponDoesNotExistException, CouponIsOutOfStockException, ConnectionClosingException, CustNotFoundException;
	
}
