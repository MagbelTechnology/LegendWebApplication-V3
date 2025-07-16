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

public class SessionUserAgentBindingFilter implements Filter {
	 @Override
	    public void init(FilterConfig filterConfig) throws ServletException {}

	    @Override
	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	            throws IOException, ServletException {
	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpSession session = httpRequest.getSession(false);

	        if (session != null) {
	            String currentUserAgent = httpRequest.getHeader("User-Agent");
	            String sessionUserAgent = (String) session.getAttribute("sessionUserAgent");

	            // If the User-Agent is different from the one saved in the session, invalidate it
	            if (sessionUserAgent != null && !sessionUserAgent.equals(currentUserAgent)) {
	                session.invalidate();
	                ((HttpServletResponse) response).sendRedirect("sessionTimedOut.jsp");  
	                return;
	            }

	            // Set User-Agent in the session if not already set
	            if (sessionUserAgent == null) {
	                session.setAttribute("sessionUserAgent", currentUserAgent);
	            }
	        }
	        chain.doFilter(request, response);
	    }

	    @Override
	    public void destroy() {}
	}