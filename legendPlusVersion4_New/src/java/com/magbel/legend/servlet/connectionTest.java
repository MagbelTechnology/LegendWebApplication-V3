package com.magbel.legend.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.corba.se.pept.transport.Connection;

public class connectionTest {
//	@WebServlet("/")
	public class SimpleServlet extends HttpServlet {
	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        // Set response content type
	        resp.setContentType("text/html");
	        PrintWriter out = resp.getWriter();
	        out.println("<h1>" + "Welcome to the servlet!" + "</h1>");
	        try {
	            String server = "localhost";
	            String database = "sa";
	            String password = "magbel";

	            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	            String connectionUrl = "jdbc:sqlserver://"+server+":1433;databaseName="+database+";user=sa;password="+password+";";
	            Connection con = (Connection) DriverManager.getConnection(connectionUrl);
	        } catch (ClassNotFoundException e) {
	            out.println("<h2>" + e.getClass().getSimpleName() + "_" + e.getMessage() + "</h2>");
	        } catch (SQLException e){
	            out.println("<h2>" + e.getClass().getSimpleName() + "_" + e.getMessage() + "</h2>");
	        } finally {
	            out.println("<h1>" + "That's the end of the servlet!" + "</h1>");
	        }
	    }
	}	

}
