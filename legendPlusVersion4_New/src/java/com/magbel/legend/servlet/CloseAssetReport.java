package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import magma.AssetRecordsBean;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
   
public class CloseAssetReport extends HttpServlet
{
		private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
   
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 String userId = (String) request.getSession().getAttribute("CurrentUser");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();


    String branch_Id = request.getParameter("branch");
    String FromDate = request.getParameter("FromDate");
    String ToDate = request.getParameter("ToDate");
    if(!FromDate.equals("")){
	String yyyy = FromDate.substring(6, 10);
//	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
//	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
//    System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	String Tyyyy = ToDate.substring(6, 10);
//	System.out.println("======>Tyyyy: "+Tyyyy);
	String Tmm = ToDate.substring(3, 5);
//	System.out.println("======>Tmm: "+Tmm);
	String Tdd = ToDate.substring(0, 2);
	ToDate = Tyyyy+"-"+Tmm+"-"+Tdd;
    }
 //   System.out.println("======>ToDate: "+ToDate);
    String dept_Code = request.getParameter("deptCode");
    String asset_Id = request.getParameter("assetId");
    String report = request.getParameter("report");
    String branchIdNo = records.getCodeName("select BRANCH from am_gb_User where USER_ID = ? ",userId);
    //String branchCode = request.getParameter("BRANCH_CODE");
    String userName = records.getCodeName("select USER_NAME from am_gb_User where USER_ID = ? ",userId);
//    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String branchCode = "";
    if(!branchIdNo.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branchIdNo);
    }
//    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = "";
    if(report.equalsIgnoreCase("rptMenuCloseAsset")){fileName = branchCode+"By"+userName+"CloseAssetReport.xlsx";}
//    System.out.println("<<<<<<fileName: "+fileName);
    String filePath = System.getProperty("user.home")+"\\Downloads";
//    System.out.println("<<<<<<filePath: "+filePath);
	File tmpDir = new File(filePath);
	boolean exists = tmpDir.exists();	
/*	if(exists==true){
		File f1 = new File(filePath);
		if (f1.delete()) {
		    System.out.println("File " + f1.getName() + " is deleted.");
		} else {
		    System.out.println("File " + f1.getName() + " not found.");
		}
	}*/
	response.setIntHeader("Content-Length", -1);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
 //    System.out.println("=====FromDate Length: "+FromDate.length());
 //    System.out.println("=====ToDate Length: "+ToDate.length());
//   System.out.println("<<<<<<bran ch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
//   System.out.println("<<<<<<branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  branchCode: "+branchCode);
     String ColQuery = "";
    
     if(asset_Id.equals("")  && FromDate.equals("")  && ToDate.equals("")){
  	   System.out.println("======>>>>>>>No Selection: "+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"   asset_Id: "+asset_Id);
  	    ColQuery ="select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'CAPITALISED ASSET' AS AssetType, \n"
  	    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
  	    		+ "from am_asset a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g where a.Asset_id = p.asset_id \n"
  	    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id and p.tran_type = 'CloseAsset' and p.process_status = 'A'\n"
  	    		+ "UNION\n"
  	    		+ "select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'UNCCAPITALISED ASSET' AS AssetType,\n"
  	    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
  	    		+ "from AM_ASSET_UNCAPITALIZED a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g  where a.Asset_id = p.asset_id \n"
  	    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id  and p.tran_type = 'Uncapitalise Close Asset' and p.process_status = 'A' \n"
  	    		+ "";    
  	}   
	 
	 if(!asset_Id.equals("")  && FromDate.equals("")  && ToDate.equals("")){	   
	   System.out.println("======>>>>>>>Asset Id Selected: "+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"   asset_Id: "+asset_Id);
	    ColQuery ="select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'CAPITALISED ASSET' AS AssetType, \n"
	    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
	    		+ "from am_asset a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g where a.Asset_id = p.asset_id \n"
	    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id and p.tran_type = 'CloseAsset' and p.process_status = 'A'and a.Asset_id=?\n"
	    		+ "UNION\n"
	    		+ "select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'UNCCAPITALISED ASSET' AS AssetType,\n"
	    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
	    		+ "from AM_ASSET_UNCAPITALIZED a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g  where a.Asset_id = p.asset_id \n"
	    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id  and p.tran_type = 'Uncapitalise Close Asset' and p.process_status = 'A' and a.Asset_id=?";
	    }
  
   if(!FromDate.equals("")  && !ToDate.equals("") && asset_Id.equals("")){
	System.out.println("======>>>>>>> Date selected: "+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"   asset_Id: "+asset_Id);
    ColQuery ="select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'CAPITALISED ASSET' AS AssetType, \n"
    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
    		+ "from am_asset a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g where a.Asset_id = p.asset_id \n"
    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id and p.tran_type = 'CloseAsset' and p.process_status = 'A' and a.Date_Closed between ? and ?\n"
    		+ "UNION\n"
    		+ "select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'UNCCAPITALISED ASSET' AS AssetType,\n"
    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
    		+ "from AM_ASSET_UNCAPITALIZED a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g  where a.Asset_id = p.asset_id \n"
    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id  and p.tran_type = 'Uncapitalise Close Asset' and p.process_status = 'A' and a.Date_Closed between ? and ?";		     
	} 
   if(!asset_Id.equals("") && !FromDate.equals("")  && !ToDate.equals("")){
	System.out.println("======>>>>>>>Asset Id and Date Selection: "+"    FromDate: "+FromDate+"   ToDate: "+ToDate+"  dept_Code: "+dept_Code+"   asset_Id: "+asset_Id);
    ColQuery ="select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'CAPITALISED ASSET' AS AssetType, \n"
    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
    		+ "from am_asset a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g where a.Asset_id = p.asset_id \n"
    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id and p.tran_type = 'CloseAsset' and p.process_status = 'A'and a.Asset_id= ? and a.Date_Closed between ? and ?\n"
    		+ "UNION\n"
    		+ "select distinct a.BRANCH_CODE,a.CATEGORY_CODE, a.Asset_id,a.Description,Cost_Price,Date_Closed,a.Posting_Date,'UNCCAPITALISED ASSET' AS AssetType,\n"
    		+ "u.User_Name AS CLOSEDBY, g.User_Name AS APPROVEDBY\n"
    		+ "from AM_ASSET_UNCAPITALIZED a,am_asset_approval p,am_ad_branch b, am_ad_category c,am_gb_user u,am_gb_user g  where a.Asset_id = p.asset_id \n"
    		+ "and a.BRANCH_CODE = b.BRANCH_CODE and a.CATEGORY_CODE = c.category_code and p.user_id = u.User_id and p.super_id = g.User_id  and p.tran_type = 'Uncapitalise Close Asset' and p.process_status = 'A' and a.Asset_id= ? and a.Date_Closed between ? and ?";	    
	}    

   
   //System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getCloseAssetRecords(ColQuery,asset_Id,FromDate,ToDate);
     System.out.println("======>>>>>>>list size: "+list.size()+"        =====report: "+report);
     if(list.size()!=0){
   	 if(report.equalsIgnoreCase("rptMenuCloseAsset")){
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell( 0).setCellValue("S/No.");
         rowhead.createCell( 1).setCellValue("Asset ID");
         rowhead.createCell( 2).setCellValue("Branch Code");
         rowhead.createCell( 3).setCellValue("Category Code");
         rowhead.createCell( 4).setCellValue("Description");
         rowhead.createCell( 5).setCellValue("Cost Price");
         rowhead.createCell( 6).setCellValue("Date Closed");
         rowhead.createCell( 7).setCellValue("Posting Date");
         rowhead.createCell( 8).setCellValue("Asset Type");
         rowhead.createCell( 9).setCellValue("Closed By");
         rowhead.createCell( 10).setCellValue("Approved By");
         
     int i = 1;
     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
    	 String assetId = newassettrans.getAssetId();
         String category_Code = newassettrans.getCategoryCode();
         String branch_Code =   newassettrans.getBranchCode();
         String description =  newassettrans.getDescription();
         double costPrice =  newassettrans.getCostPrice();
         String dateClosed =   newassettrans.getEffectiveDate() != null ? getDate(newassettrans.getEffectiveDate()) : "";
         String postingDate = newassettrans.getPostingDate() != null ? getDate(newassettrans.getPostingDate()) : "";
         String assetType =  newassettrans.getAssetType();
         String closedBy = newassettrans.getAction();
         String approvedBy = newassettrans.getApprovalStatus();
		
	

			Row row = sheet.createRow((int) i);
			 
			row.createCell((short) 0).setCellValue(i);
			row.createCell((short) 1).setCellValue(assetId);
            row.createCell((short) 2).setCellValue(category_Code);
            row.createCell((short) 3).setCellValue(branch_Code);
            row.createCell((short) 4).setCellValue(description);
            row.createCell((short) 5).setCellValue(costPrice);
            row.createCell((short) 6).setCellValue(dateClosed);
            row.createCell((short) 7).setCellValue(postingDate);
            row.createCell((short) 8).setCellValue(assetType);
            row.createCell((short) 9).setCellValue(closedBy);
            row.createCell((short) 10).setCellValue(approvedBy);
            
            //	System.out.println("======>oldAssetId====: "+oldAssetId+"  Index: "+i);

            i++;
//            System.out.println("we are here");
     }
	 System.out.println("we are here 2");
	   OutputStream stream = response.getOutputStream();
         workbook.write(stream);
         stream.close();
         workbook.close();  
         System.out.println("Data is saved in excel file.");
         
    /* w.write();
     w.close(); */
     }
 }
   	    
    } catch (Exception e)
    {
     e.getMessage();
    } 
    //finally
   // {
//     if (out != null)
//      out.close();
   // }
   }
   }
   public void doGet(HttpServletRequest request, 
		    HttpServletResponse response)
		      throws ServletException, IOException
		   {
	   doPost(request, response);
		   }
   
   public String getDate(String date) {
		   //System.out.println("<<<<<<<<<<<< Date: " + date);
		   String yyyy = date.substring(0, 4);
			String mm = date.substring(5, 7);
			String dd = date.substring(8, 10);
			date = dd+"/"+mm+"/"+yyyy;
		   
			
		   return date;
	   }
}