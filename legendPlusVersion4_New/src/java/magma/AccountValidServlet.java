/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.magbel.util.DataConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import ng.com.magbel.token.ZenithTokenClass;

import org.json.JSONObject;

import legend.admin.handlers.VendorHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
/**
 *
 * @author Olabo
 */
public class AccountValidServlet extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }
      VendorHandler vh = new VendorHandler();

        
 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	 
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");
//		System.out.println("ThirdPartyLabel: " + ThirdPartyLabel);
		String AccountValidationUrl = prop.getProperty("AccountValidationUrl");
//		System.out.println("AccountValidationUrl: " + AccountValidationUrl);
		
		String oldAccountNo = request.getParameter("accountNo");
		
		 String responseCode = "";
		  String responseDescription = "";
		  String customer_No = "";
		  String customer_Name = "";
		
		   String batchNo ="";
		   String result = "";
		   if(!AccountValidationUrl.equals("")){
		   try{
		   String status = ZenithTokenClass.accountDetailValidation(oldAccountNo);
		   JSONObject json = new JSONObject(status);
		  responseCode = json.getString("responseCode");
		  responseDescription = json.getString("responseDescription");
		  customer_No = json.getString("customer_No");
		  customer_Name = json.getString("customer_Name1");
		  result = responseDescription;
		  if(responseDescription.equalsIgnoreCase("SUCCESSFUL")){result = customer_No;}
		   }catch(Exception e){
			   e.getMessage();
	   		}
	   }
	
		   if(AccountValidationUrl.equals("")){result = vh.accountValid(oldAccountNo);}
      processUserTransaction(request, response, result);
   }
 private void processUserTransaction(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String result) throws
             IOException, ServletException {
         response.setContentType("text/xml");
         response.setHeader("Cache-Control", "no-cache");
         response.getWriter().write(result);

     }
}
