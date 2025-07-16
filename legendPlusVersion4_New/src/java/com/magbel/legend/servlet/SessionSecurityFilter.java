package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionSecurityFilter implements Filter {
	   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {

	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpSession session = httpRequest.getSession(false);
	        
	        if (session != null) {
	            // Retrieve user session data
	            String storedIP = (String) session.getAttribute("ip");
	            String storedUserAgent = (String) session.getAttribute("userAgent");
	            
	            String currentIP = httpRequest.getRemoteAddr();
	            String currentUserAgent = httpRequest.getHeader("User-Agent");

	            // Check if the IP or User-Agent has changed
	            if (!currentIP.equals(storedIP) || !currentUserAgent.equals(storedUserAgent)) {
	                // Invalid session, redirect to login
	                session.invalidate();
	                ((HttpServletResponse) response).sendRedirect("sessionTimedOut.jsp");
	                return;
	            }
	        }

	        chain.doFilter(request, response); // Continue filter chain
	    }

	    public void init(FilterConfig filterConfig) throws ServletException {
	    }

	    public void destroy() {
	    }

}
