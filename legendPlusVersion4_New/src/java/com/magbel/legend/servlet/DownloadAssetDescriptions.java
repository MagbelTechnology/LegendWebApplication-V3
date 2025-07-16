package com.magbel.legend.servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.util.DataConnect;
   
public class DownloadAssetDescriptions extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  doGet(request,response);
	}
	 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String home = System.getProperty("user.home");
       String excelFilePath = "Asset Descriptions Download";
       File file = new File(home + "/Downloads/" + excelFilePath + ".xls");
       Connection conn=null;
       Statement statement = null;
       ResultSet result = null;
	   
		Properties prop = new Properties();
		File propfile = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(propfile);
		prop.load(input);

		String ThirdPartyLabel = prop.getProperty("ThirdPartyLabel");         
//       String ThirdPartyLabel = request.getParameter("ThirdPartyLabel");
       System.out.println("ThirdPartyLabel is : " + ThirdPartyLabel);
       String userClass = (String) request.getSession().getAttribute("UserClass");
       try {
      if (!userClass.equals("NULL") || userClass!=null){
       String sql = "SELECT ID, SUB_CATEGORY_CODE, DESCRIPTION, STATUS, USER_ID, CREATE_DATE FROM AM_ASSET_DESCRIPTION "
       		+ "ORDER BY DESCRIPTION ";
     //  System.out.println("The sql is : " + sql);
       conn = getConnection("legendPlus");
        
       statement =conn.createStatement();

       result = statement.executeQuery(sql);
       String label1 = "";
       System.out.println("The ThirdPartyLabel is : " + ThirdPartyLabel);
     //  if(ThirdPartyLabel.equals("ZENITH")){label1 = "BRANCH CODE";}else{label1 = "SBU Code";}
       HSSFWorkbook workbook = new HSSFWorkbook();
       HSSFSheet sheet = workbook.createSheet("Download Excel Export");

       System.out.println("The file name is : " + file);

       HSSFRow rowhead = sheet.createRow((short) 0);

       rowhead.createCell((short) 0).setCellValue("S/N");
       rowhead.createCell((short) 1).setCellValue("Id");
       rowhead.createCell((short) 2).setCellValue("Subcategory Code");
       rowhead.createCell((short) 3).setCellValue("Description");
       rowhead.createCell((short) 4).setCellValue("Status");
       rowhead.createCell((short) 5).setCellValue("User Id");
       rowhead.createCell((short) 6).setCellValue("Create Date");
       int rowCount = 1;

       while (result.next()) {
           String id = result.getString("ID");
           String subcategoryCode = result.getString("SUB_CATEGORY_CODE");
           String description = result.getString("DESCRIPTION");
           String status = result.getString("STATUS");
           String userId = result.getString("USER_ID"); 
           String createDate = result.getString("CREATE_DATE");
           
           
         

           String labelCode = "";
        //   if(label1.equals("BRANCH CODE")){labelCode = branchCode;}else {labelCode = Sbu_Code;}
           HSSFRow row = sheet.createRow((short) rowCount);

           row.createCell((short) 0).setCellValue(String.valueOf(rowCount));
           row.createCell((short) 1).setCellValue(id);
           row.createCell((short) 2).setCellValue(subcategoryCode);
           row.createCell((short) 3).setCellValue(description);
           row.createCell((short) 4).setCellValue(status);
           row.createCell((short) 5).setCellValue(userId);
           row.createCell((short) 6).setCellValue(createDate);

           rowCount++;
       }
       response.setContentType("application/vnd.ms-excel");
       response.setHeader("Content-Disposition", String.format("attachment; filename=%s",file.getName()));
       response.setHeader("pragma","public");
       response.setHeader("Content-Length", String.valueOf(file));
     //  OutputStream stream = response.getOutputStream();
//       new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
       try (OutputStream outputStream = response.getOutputStream()) {
           workbook.write(outputStream); // Write Excel data to the response output stream
       } finally {
       	workbook.close();
       }
//       workbook.write(stream);
//       stream.close();
       System.out.println("Data is saved in excel file.");
       //new MailUtil().sendResetPasswordMail("ayomidematanmi1@gmail.com","Fabregas4");
       //new AttachmentMail().mail("ayomidematanmi4@gmail.com",file);
      // response.sendRedirect("registrationView.jsp");
       }
   } catch (SQLException e) {
       e.getMessage();
   }finally{
  	 closeOpenConnection(result,statement,conn);
   }
	}
   
   public String getDate(String date) {
		   //System.out.println("<<<<<<<<<<<< Date: " + date);
		   String yyyy = date.substring(0, 4);
			String mm = date.substring(5, 7);
			String dd = date.substring(8, 10);
			date = dd+"/"+mm+"/"+yyyy;
		   
			
		   return date;
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