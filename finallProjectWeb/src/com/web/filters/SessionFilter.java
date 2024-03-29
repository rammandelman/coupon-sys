package com.web.filters;



import MyExceptions.ConnectionClosingException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongSqlSyntaxException;

import java.io.IOException;
import java.sql.*;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controler.userType;
import view.CouponClinetFacade;
import view.CouponSystem;

/**
 * Servlet Filter implementation class loginFilter
 */
@WebFilter("/SessionFilter")
public class SessionFilter implements Filter {
	//private CouponSystem cs;
    /**
     * Default constructor. 
     */
	
	
    public SessionFilter() {
    	 System.out.println("this is the login filter constructor");
    	
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpSession session = ((HttpServletRequest) request).getSession(false);
		
		if(session == null) {
			((HttpServletResponse) response).sendRedirect("login.html");
		}
		else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
