package interfaces;

import java.util.Collection;
import java.util.Set;

import DataBases.Companies;
import DataBases.Coupons;
import MyExceptions.CompAllreadyExistException;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouldNotRemoveCompException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongEntryException;


public interface CompanyDAO {

	public Companies createCompany(String companieID, String COMP_NAME, String EMAIL, String Password)throws NoFreeConnectionsException, WrongEntryException, CompanieNotFoundException,ConnectionClosingException, CompAllreadyExistException ;
	
	public boolean removeCompany(String compID) throws ConnectionClosingException,NoFreeConnectionsException,CouldNotRemoveCompException ;
	
	public Companies updateCompany(String compID, String filedName, String newValue)throws ConnectionClosingException,
	NoFreeConnectionsException,WrongEntryException,MyExceptions.UnableToUpdateDataException,CompanieNotFoundException, CompAllreadyExistException;
	
	public boolean login(String companieID, String pass);
	 
	public Companies getCompany(String id)throws NoFreeConnectionsException,WrongEntryException,CompanieNotFoundException,ConnectionClosingException ;
	
	public Set<Companies> getAllCompanies() throws ConnectionClosingException, NoFreeConnectionsException,WrongEntryException;
	
	public Set<Coupons> getCoupons(String compID)throws NoFreeConnectionsException, WrongEntryException,WrongEntryException, CompanieNotFoundException, ConnectionClosingException, CouponDoesNotExistException ;
	
	
	
}
