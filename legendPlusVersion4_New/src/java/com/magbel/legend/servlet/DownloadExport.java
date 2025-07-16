package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.util.DataConnect;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;

public class DownloadExport extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  doGet(request,response);
	}
	 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
         String home = System.getProperty("user.home");
         String excelFilePath = "Finacle Excel Download";
         File file = new File(home + "/Downloads/" + excelFilePath + ".xls");
         Connection conn=null;
         Statement statement = null;
         ResultSet result = null;
  	   
 		Properties prop = new Properties();
 		File propfile = new File("C:\\Property\\LegendPlus.properties");
 		FileInputStream input = new FileInputStream(propfile);
 		prop.load(input);

 		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");         
//         String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
         System.out.println("ThirdPartyLabel is : " + ThirdPartyLabel);
         String userClass = (String) request.getSession().getAttribute("UserClass");
         try {
        if (!userClass.equals("NULL") || userClass!=null){
         String sql = "select BRANCH_CODE, SBU_CODE, DR_ACCT, CR_ACCT, AMOUNT, NARRATION, NARRATION2 FROM FINACLE_EXT";
         System.out.println("The sql is : " + sql);
         conn = getConnection("legendPlus");
          
         statement =conn.createStatement();

         result = statement.executeQuery(sql);
         String label1 = "";
         System.out.println("The ThirdPartyLabel is : " + ThirdPartyLabel);
         if(ThirdPartyLabel.equals("ZENITH")){label1 = "BRANCH CODE";}else{label1 = "SBU Code";}
         HSSFWorkbook workbook = new HSSFWorkbook();
         HSSFSheet sheet = workbook.createSheet("Download Excel Export");

         System.out.println("The file name is : " + file);

         HSSFRow rowhead = sheet.createRow((short) 0);

         rowhead.createCell((short) 0).setCellValue(label1);
         rowhead.createCell((short) 1).setCellValue("Debt Acct");
         rowhead.createCell((short) 2).setCellValue("Credit_Acct");
         rowhead.createCell((short) 3).setCellValue("Amount");
         rowhead.createCell((short) 4).setCellValue("Narration");
         rowhead.createCell((short) 5).setCellValue("Narration2");
         int rowCount = 1;

         while (result.next()) {
             String Sbu_Code = result.getString("SBU_CODE");
             String branchCode = result.getString("BRANCH_CODE");
             String DebitAccount = result.getString("DR_ACCT");
             String CreditAccount = result.getString("CR_ACCT");
             String Amount = result.getString("AMOUNT"); 
             String Narration = result.getString("NARRATION");
             String Narration2 = result.getString("NARRATION2");
             
             Double amount = Double.parseDouble(Amount);

             String labelCode = "";
             if(label1.equals("BRANCH CODE")){labelCode = branchCode;}else {labelCode = Sbu_Code;}
             HSSFRow row = sheet.createRow((short) rowCount);

             row.createCell((short) 0).setCellValue(labelCode);
             row.createCell((short) 1).setCellValue(DebitAccount);
             row.createCell((short) 2).setCellValue(CreditAccount);
             row.createCell((short) 3).setCellValue(amount);
             row.createCell((short) 4).setCellValue(Narration);
             row.createCell((short) 5).setCellValue(Narration2);

             rowCount++;
         }
         response.setContentType("application/vnd.ms-excel");
         response.setHeader("Content-Disposition", String.format("attachment; filename=%s",file.getName()));
         response.setHeader("pragma","public");
         response.setHeader("Content-Length", String.valueOf(file));
       //  OutputStream stream = response.getOutputStream();
//         new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
         try (OutputStream outputStream = response.getOutputStream()) {
             workbook.write(outputStream); // Write Excel data to the response output stream
         } finally {
         	workbook.close();
         }
//         workbook.write(stream);
//         stream.close();
         System.out.println("Data is saved in excel file.");
         //new MailUtil().sendResetPasswordMail("ayomidematanmi1@gmail.com","Fabregas4");
         //new AttachmentMail().mail("ayomidematanmi4@gmail.com",file);
         response.sendRedirect("registrationView.jsp");
         }
     } catch (SQLException e) {
         e.getMessage();
     }finally{
    	 closeOpenConnection(result,statement,conn);
     }
	}
	
     public Connection getConnection(String jndiName) {
         Connection con = null;
         try {
             con = new DataConnect(jndiName).getConnection();
         } catch (Exception conError) {
             System.out.println("WARNING:Error getting connection - >" +
                                conError);
         }
         return con;
     }

     public void closeOpenConnection(ResultSet rs, Statement ps,
             Connection con) {
try {
if (rs != null) {
rs.close();
}
if (ps != null) {
ps.close();
}
if (con != null) {
con.close();
}

} catch (Exception error) {
System.out.println("WARNING:Error closing connection->" +
        error.getMessage());
}
}
   
}