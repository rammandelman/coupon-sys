package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.spi.CalendarNameProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.*;

import com.web.model.Income;
import com.web.model.IncomeType;
import com.web.model.Incomes;
import com.web.services.BusinessDelegate;
import com.web.services.CompanyService;

import DataBases.Companies;
import DataBases.CouponType;
import DataBases.Coupons;
import DataBases.CustomerVsCoupons;
import MyExceptions.CompanieNotFoundException;
import MyExceptions.ConnectionClosingException;
import MyExceptions.CouponDoesNotExistException;
import MyExceptions.CustNotFoundException;
import MyExceptions.ErrorWritingToJoinTblException;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import MyExceptions.wrongUserOrPassException;
import controler.ConnectionPool;

import controler.userType;
import interfaces.CompanyDAO;
import view.AdminFacade;
import view.CompanyDBDAO;
import view.CouponClinetFacade;
import view.CouponSystem;
import view.DailyCouponExpirationTask;

/**
 * 
 * @author ram this is the main class for coupon system
 *
 */
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BusinessDelegate businessDelegate = new BusinessDelegate();
		 Response r2= businessDelegate.getAllIncomesByCompName("zohar");
		 
		 String output = r2.readEntity(String.class);
		 System.out.println(output.length());
		 System.out.println("the out is: "+output);
		

	     
	}
	
}
