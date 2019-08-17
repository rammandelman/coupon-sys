package com.web.servlets;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controler.userType;
import view.CouponClinetFacade;
import view.CouponSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import MyExceptions.ConnectionClosingException;
import MyExceptions.FailedToConnectDb;
import MyExceptions.NoFreeConnectionsException;
import MyExceptions.WrongEntryException;
import MyExceptions.WrongSqlSyntaxException;
import MyExceptions.wrongUserOrPassException;

import java.sql.*;

/**
 * Servlet implementation class LoginServlet1
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouponSystem cs;
	
	@Override
	public void init() {
		System.out.println("init func activated");
		this.cs = cs.getInstance();
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//PrintWriter out = response.getWriter();
		response.setContentType("text/html");  
		try {
			  
		System.out.println("this is the service func from login servlet");
		HttpSession session = request.getSession(false);
		 
	   
	   
		if(session != null)
		{
			session.invalidate();
			session = request.getSession(true);
		}else {
			session = request.getSession(true);
		}
		String user = request.getParameter("user");
		String pwd = request.getParameter("pwd");
		 
	
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			CouponClinetFacade mydfacade;
			/*
			 * calliing the function of login with the paramters of user and pas to get back 
			 * object of facade
			 */
				mydfacade = cs.login(user, pwd, userType.admin);
			/*
			 * checking if the facade is null. if it is the login did not succeed.
			 */
			if(!(mydfacade == null))
			{
				System.out.println("login succes");
				/*
				 * setting the facade in the session
				 */
				session.setAttribute("facede", mydfacade);
				session.setAttribute("userID", user);
				System.out.println("this is facade tostring in servket: " +mydfacade.toString());
				/*
				 * checking the user that connected
				 */
				if(mydfacade.type == userType.admin)
				{
					request.getRequestDispatcher("admin.html").forward(request, response);
					//response.sendRedirect("admin.html");
				}
				else if(mydfacade.type == userType.companies)
				{
					System.out.println("looged as companie attempting redirect");
					System.out.println("thsi is from login servlet, the user name is: "+user);
					request.getRequestDispatcher("company.html").forward(request, response);
					//response.sendRedirect("wellcome companie user.html");
				//	request.getRequestDispatcher("wellcome companie user.html").forward(request, response);
					//response.sendRedirect("wellcome user admin.html");
					//response.sendRedirect("wellcome companie user.html");
				}
				else if(mydfacade.type == userType.customers)
				{
					request.getRequestDispatcher("customer.html").forward(request, response);
					//response.sendRedirect("wellcome customer.html");
				}
				/*
				 * this is the else if login did not succeed
				 */
			}else {
				
				request.getRequestDispatcher("login.html").forward(request, response);
				
			}
		} catch (ConnectionClosingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoFreeConnectionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*
			 * catching ecpetion thrown if user or pass are wrong printing it to the html page
			 */
		} catch (wrongUserOrPassException e) {
			System.out.println("facade is null");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			PrintWriter out= response.getWriter();
			out.println("<font color=red>Either user name or password is wrong.</font>");
			rd.include(request, response);
		}
		}
		
	

	 
}
