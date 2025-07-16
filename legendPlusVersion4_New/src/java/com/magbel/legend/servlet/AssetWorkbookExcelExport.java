package com.magbel.legend.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.magbel.legend.bus.Report;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AssetWorkbookExcelExport extends HttpServlet {

//    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException	
{

    	PrintWriter out = response.getWriter();
        OutputStream outp = null;
      //  FileOutputStream out;
        //PrintStream out = new PrintStream();
        HttpSession session = request.getSession();
        String branch_Id = request.getParameter("BRANCH_ID");
        Report rep = new Report();

    	String userClass = (String) request.getSession().getAttribute("UserClass");
    	if (!userClass.equals("NULL") || userClass!=null){
    		
                String delim = ",";
                // CONSTRUCT COLUMNS AND ALLIAS
                String ColQuery = "";
                if(branch_Id!=null){ColQuery ="SELECT *FROM am_gb_workbookupdate WHERE PROCESS_STATUS != 'Y' AND BRANCH_ID = '"+branch_Id+"'";}
                else{ColQuery ="SELECT *FROM am_gb_workbookupdate WHERE PROCESS_STATUS != 'Y'";}

 //               System.out.println("Report Query:   " + ColQuery+"   branch_Id: "+branch_Id);
                
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                String query = "";
                String comp = "";
                try {
                	java.util.ArrayList list =rep.getWorkBookSqlRecords(ColQuery);
 //               	System.out.println("-->size of Array List for Work Book >--> "+list.size());
               if(list.size()!=0){
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    
                    HSSFSheet sheet = workbook.createSheet("workbook");
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell((short) 0).setCellValue("ASSET_ID");
                    rowhead.createCell((short) 1).setCellValue("DESCRITION");
                    rowhead.createCell((short) 2).setCellValue("BAR CODE");
                    rowhead.createCell((short) 3).setCellValue("ASSET USER");
                    rowhead.createCell((short) 4).setCellValue("COMMENTS");
                    rowhead.createCell((short) 5).setCellValue("ASSET SIGHTED");
                    rowhead.createCell((short) 6).setCellValue("ASSET FUNCTIONING");
                    rowhead.createCell((short) 7).setCellValue("ASSET CODE");
                    rowhead.createCell((short) 8).setCellValue("BRANCH ID");
                    rowhead.createCell((short) 9).setCellValue("CATEGORY ID");
                    rowhead.createCell((short) 10).setCellValue("BATCH ID");
                    int i = 1;
           	     for(int k=0;k<list.size();k++)
        	     {
        	    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
        				String assetId =  newassettrans.getAssetId();
        				String barcode =  newassettrans.getBarCode();
        				String Description = newassettrans.getDescription();   
        				String assetuser = newassettrans.getAssetUser();
        				String assetcode = newassettrans.getAssetCode();
        				String branchId = newassettrans.getBranchCode();
        				String categoryId = newassettrans.getCategoryCode();
        				String batchId = newassettrans.getIntegrifyId();
        				String sighted = newassettrans.getAssetMake();
        				String function = newassettrans.getAssetModel();
        				String comments = newassettrans.getAssetMaintenance();
        				
                        HSSFRow row = sheet.createRow((short) i);
                        row.createCell((short) 0).setCellValue(assetId);
                        row.createCell((short) 1).setCellValue(Description);
                        row.createCell((short) 2).setCellValue(barcode);
                        row.createCell((short) 3).setCellValue(assetuser);
                        row.createCell((short) 4).setCellValue(comments);
                        row.createCell((short) 5).setCellValue(sighted);
                        row.createCell((short) 6).setCellValue(function);
                        row.createCell((short) 7).setCellValue(assetcode);
                        row.createCell((short) 8).setCellValue(branchId);
                        row.createCell((short) 9).setCellValue(categoryId);
                        row.createCell((short) 10).setCellValue(batchId);
                        i++;
        	     }

                    String location = "F:/ACCA/test.xls";
                    FileOutputStream fileOut = new FileOutputStream(location);
                    workbook.write(fileOut);
                    fileOut.close();
                    out.print("<script>alert('Asset Work Book has been exported to Excel file in Drive C with File Name workbook.xls ')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=AssetWorkbook'</script>");
                }
               else{
                   out.print("<script>alert('No Record to export to Excel ')</script>");
                   out.print("<script>window.location='DocumentHelp.jsp?np=AssetWorkbook'</script>");
               }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
}
        }
    
    }
//}


