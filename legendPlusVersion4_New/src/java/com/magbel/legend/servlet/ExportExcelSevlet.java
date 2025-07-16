package com.magbel.legend.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.Report;
import com.magbel.util.DataConnect;

public class ExportExcelSevlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//PrintWriter st = response.getWriter();
    	Connection c = null;
    	ResultSet rs = null;
    	Statement s = null; 
    	
        try {

            String username = request.getParameter("userName");
            System.out.println("Username is : " + username);
//            LocalDate myObj = LocalDate.now();
//            String formattedDate = myObj.toString();
            String home = System.getProperty("user.home");
            String excelFilePath = username + "_" + "ExportFile";
            File file = new File(home + "/Downloads/" + excelFilePath + ".xls");

   

            String sql = "SELECT group_id,  Description, Cost_Price, Posting_Date, AMOUNT_REM FROM am_group_stock_main  WHERE PART_PAY='Y' OR RAISE_ENTRY='Y'  ORDER BY DATE_PURCHASED DESC";

            System.out.println("The sql is : " + sql);

			Report rep = new Report();
//            Statement statement = ConnectionUrl.initializeDatabase().createStatement();

            java.util.ArrayList list =rep.getexportexcelSqlRecords(sql);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Reviews");

            System.out.println("The file name is : " + file);

            HSSFRow rowhead = sheet.createRow((short) 0);

            rowhead.createCell((short) 0).setCellValue("GROUP ID");
            rowhead.createCell((short) 1).setCellValue("DESCRIPTION");
            rowhead.createCell((short) 2).setCellValue("COST PRICE");
            rowhead.createCell((short) 3).setCellValue("BALANCE");
            rowhead.createCell((short) 4).setCellValue("POSTING DATE");
            int rowCount = 1;

       	 for(int k=0;k<list.size();k++)
         {
//    		 System.out.println("<<<<<<I: "+i+"    K: "+k);
        	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
	 			String groupId =  newassettrans.getAssetId();
	 			String description = newassettrans.getDescription();   
	 			String costPrice = String.valueOf(newassettrans.getCostPrice());
	 			String postingDate = newassettrans.getPostingDate();
	 			String amountRemaining = String.valueOf(newassettrans.getVatValue());    				

                HSSFRow row = sheet.createRow((short) rowCount);

                row.createCell((short) 0).setCellValue(groupId);
                row.createCell((short) 1).setCellValue(description);
                row.createCell((short) 2).setCellValue(costPrice);
                row.createCell((short) 3).setCellValue(amountRemaining);
                row.createCell((short) 4).setCellValue(postingDate);

  //              rowCount++;
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s",file.getName()));
            response.setHeader("pragma","public");
            response.setHeader("Content-Length", String.valueOf(file));
            OutputStream stream = response.getOutputStream();
            workbook.write(stream);
            stream.close();
    
            //System.out.println("Data is saved in excel file.");
	//		 st.println("<script>alert('The user password is '"+password+"')</script>");
             //st.print("<script>window.location='groupstockPaymentList.jsp'</script>");
   //         response.sendRedirect("groupstockPaymentList.jsp");
            
        } catch (Exception e)
        {
         throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally
        {
//         if (out != null)
//          out.close();
        
        } 
    }


}
