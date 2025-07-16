package com.magbel.legend.servlet;
import java.io.IOException;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import magma.AssetRecordsBean;
   
public class AssetFleetRenewalExcelExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
	SimpleDateFormat sdf;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   {
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null;
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
	sdf = new SimpleDateFormat("dd-MM-yyyy");
	String userClass = (String) request.getSession().getAttribute("UserClass");
    try
    {
    if (!userClass.equals("NULL") || userClass!=null){
    	ad = new AssetRecordsBean();
     response.setContentType("application/vnd.ms-excel");
 
     response.setHeader("Content-Disposition", 
    "attachment; filename=AssetFleetRenewal.xls");
     String branch_Id = request.getParameter("BRANCH_ID");
 //    String prooftranId = request.getParameter("prooftranId");
     String userId = request.getParameter("user_id");
     Report rep = new Report();
     String tableName = "Temp"+userId;
//     String q0 = "delete from am_asset_proof where BATCH_ID in (select BATCH_ID from "+tableName+") ";
//  	ad.updateAssetStatusChange(q0); 
     String ColQuery = "SELECT *FROM "+tableName+"";
     java.util.ArrayList list =rep.getFleetRenewalSqlRecords(ColQuery);
     if(list.size()!=0){
     WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
     WritableSheet s = w.createSheet("Demo", 0);

     s.addCell(new Label(0, 0, "S/N"));
     s.addCell(new Label(1, 0, "ASSET_ID"));
     s.addCell(new Label(2, 0, "REGISTRATION NO"));
     s.addCell(new Label(3, 0, "DESCRITION"));
     s.addCell(new Label(4, 0, "BAR CODE"));
     s.addCell(new Label(5, 0, "SBU CODE"));
     s.addCell(new Label(6, 0, "MAKE"));
     s.addCell(new Label(7, 0, "SUM INSURED"));
     s.addCell(new Label(8, 0, "ASSET USER"));
     s.addCell(new Label(9, 0, "EFFECTIVE DATE"));
     s.addCell(new Label(10, 0, "VENDOR_CODE"));
     s.addCell(new Label(11, 0, "COMMENTS"));
     int i = 1;
 //    System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
//		 System.out.println("<<<<<<I: "+i+"    K: "+k);
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String assetId =  newassettrans.getAssetId();
			String barcode =  newassettrans.getBarCode();
			String Description = newassettrans.getDescription();   
			String assetuser = newassettrans.getAssetUser();
			String comments = newassettrans.getAssetMaintenance();
			String make = newassettrans.getAssetMake();    				
			String registrationNo = newassettrans.getRegistrationNo();
			String sbucode = newassettrans.getSbuCode();	
			String vendorCode = newassettrans.getVendorCode();
			String sumInsured = "0";
			String effectiveDate = sdf.format(new java.util.Date());
//			String sumInsured = records.getCodeName("select SUM_INSURED from FT_SUMINSURED where ASSET_ID = '"+assetId+"' and TRAN_TYPE = 'I'");
//			String effectiveDate = records.getCodeName("select EFFECTIVE_DATE from FLEET_SUMINSURED where ASSET_ID = '"+assetId+"' and TRAN_TYPE = 'I'");
 //           HSSFRow row = sheet.createRow((short) i);
		   s.addCell(new Label(0, i, String.valueOf(k)));
           s.addCell(new Label(1, i, assetId));
           s.addCell(new Label(2, i, registrationNo));
           s.addCell(new Label(3, i, Description));
           s.addCell(new Label(4, i, barcode));
           s.addCell(new Label(5, i, sbucode));
           s.addCell(new Label(6, i, make));
           s.addCell(new Label(7, i, sumInsured));
           s.addCell(new Label(8, i, assetuser));
           s.addCell(new Label(9, i, effectiveDate));
           s.addCell(new Label(10, i, vendorCode));
           s.addCell(new Label(11, i, comments));
            i++;
     }
     String q1 = "IF EXISTS(select * from "+tableName+" where ASSET_ID is not null) drop table "+tableName+" ";
     ad.updateAssetStatusChange(q1);
     w.write();
     w.close();
     }
    }
    } catch (Exception e)
    {
     throw new ServletException("Exception in Excel Sample Servlet", e);
    } finally
    {
//     if (out != null)
//      out.close();
    }
   }
   
   
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
   
}