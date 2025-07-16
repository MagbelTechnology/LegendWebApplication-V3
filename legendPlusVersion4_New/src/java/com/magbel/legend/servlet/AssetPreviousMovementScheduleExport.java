package com.magbel.legend.servlet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import magma.AssetRecordsBean;
   
public class AssetPreviousMovementScheduleExport extends HttpServlet
{
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
   public void doPost(HttpServletRequest request, 
    HttpServletResponse response)
      throws ServletException, IOException
     
   {
	 String userClass = (String) request.getSession().getAttribute("UserClass");
	 
	 if (!userClass.equals("NULL") || userClass!=null){
//	   PrintWriter out = response.getWriter();
//    OutputStream out = null; 
	mail= new EmailSmsServiceBus();
	records = new ApprovalRecords();
//    String branch_Code = request.getParameter("initiatorSOLID");
 //   String branch_Id = request.getParameter("branch");
    //String branchCode = request.getParameter("BRANCH_CODE");
	 String userId =(String) request.getSession().getAttribute("CurrentUser");
    String branch_Id =(String) request.getSession().getAttribute("UserCenter");
    String userName = records.getCodeName("select user_name from am_gb_user where user_id = ? ",userId);
    String branch_Code = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    System.out.println("<<<<<<branch_Id: "+branch_Id);
    String FromDate = request.getParameter("startDate");
    String ToDate = request.getParameter("endDate");
    System.out.println("======>FromDate: "+FromDate);
  
    String branchCode = "";
   
    if(!branch_Id.equals("0")){
    	branchCode = records.getCodeName("select BRANCH_CODE from am_ad_branch where BRANCH_ID = ? ",branch_Id);
    }
    System.out.println("<<<<<<branch_Code: "+branch_Code);
//    String userName = request.getParameter("userName");
    String fileName = branch_Code+"By"+userName+"AssetMovementScheduleExportReport.xlsx";    	
    String filePath = System.getProperty("user.home")+"\\Downloads";
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
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", 
   "attachment; filename="+fileName+"");
    try
    {
    	ad = new AssetRecordsBean();
 
//    if(exists==false){   	

        String categoryCode = request.getParameter("category");
     Report rep = new Report();
   System.out.println("<<<<<<branch_Id: "+branch_Id+"    categoryCode: "+categoryCode+"  branchCode: "+branchCode);
   String ColQuery = "";
     if(FromDate.equals("")  && ToDate.equals("")){
    	 System.out.println("======>>>>>>>FromDate and ToDate Not Selected: ");
	     ColQuery ="select '1' AS SERIAL,'BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive "
		     		+ "    pivot(sum(cost_open_bal) for class_name "
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(cost_additions) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(cost_disposal) for class_name"
		     		+ "    in ([COMPUTER EQUIPMENT],[FURNITURE FITTINGS & EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "--ACCUMULATED DEPRECIATION:"
		     		+ "select '6' AS SERIAL,'BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, dep_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive "
		     		+ "    pivot(sum(dep_open_bal) for class_name "
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD]  "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '7' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(dep_charge) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '8' AS SERIAL,'TRANSFER' AS NARRATION, * from (select class_name,dep_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(dep_disposal) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '9' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(dep_disposal) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '9.1' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(nbv_closing_bal) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable "
		     		+ "UNION "
		     		+ "select '9.2' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
		     		+ "    pivot(sum(nbv_open_bal) for class_name"
		     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
		     		+ "    [MOTOR VEHICLES],"
		     		+ "    [LAND & LEASEHOLD] "
		     		+ "    ) ) as pivottable";
		}      
	      
     if(!FromDate.equals("")  && !ToDate.equals("")){
    	 System.out.println("======>>>>>>>FromDate and ToDate Selected: ");
	     ColQuery ="select '1' AS SERIAL,'BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, cost_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive "
	     		+ "    pivot(sum(cost_open_bal) for class_name "
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '2' AS SERIAL,'ADDITIONS' AS NARRATION, * from (select class_name,cost_additions from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(cost_additions) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '3' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,cost_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(cost_disposal) for class_name"
	     		+ "    in ([COMPUTER EQUIPMENT],[FURNITURE FITTINGS & EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '6' AS SERIAL,'BALANCES AS AT BEGINING OF YEAR' AS NARRATION, * from (select class_name, dep_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive "
	     		+ "    pivot(sum(dep_open_bal) for class_name "
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD]  "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '7' AS SERIAL,'CHARGE FOR THE YEAR' AS NARRATION, * from (select class_name,dep_charge from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(dep_charge) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '8' AS SERIAL,'TRANSFER' AS NARRATION, * from (select class_name,dep_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(dep_disposal) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '9' AS SERIAL,'DISPOSAL' AS NARRATION,* from (select class_name,dep_disposal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(dep_disposal) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.1' AS SERIAL,'NBV AS YEAR END' AS NARRATION,* from (select class_name,nbv_closing_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(nbv_closing_bal) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable "
	     		+ "UNION "
	     		+ "select '9.2' AS SERIAL,'NBV OPENING BALANCE' AS NARRATION,* from (select class_name,nbv_open_bal from fixed_asset_schedule_archive where month_val = ? and year_val = ?) fixed_asset_schedule_archive"
	     		+ "    pivot(sum(nbv_open_bal) for class_name"
	     		+ "    in ([FURNITURE FITTINGS & EQUIPMENT],[COMPUTER EQUIPMENT],[MACHINE & EQUIPMENT],[INTANGIBLE ASSETS],"
	     		+ "    [MOTOR VEHICLES],"
	     		+ "    [LAND & LEASEHOLD] "
	     		+ "    ) ) as pivottable";
	}      

     
//   System.out.println("======>>>>>>>ColQuery: "+ColQuery);
     java.util.ArrayList list =rep.getFixedAssetMovementScheduleRecords(ColQuery,FromDate,ToDate);
     if(list.size()!=0){
	 
    	 SXSSFWorkbook workbook = new SXSSFWorkbook();
         Sheet sheet = workbook.createSheet("Demo");
         Row rowhead = sheet.createRow((int) 0);
         
         rowhead.createCell((short) 0).setCellValue("NARRATION");
         rowhead.createCell((short) 1).setCellValue("FURNITURE FITTINGS & EQUIPMENT");
         rowhead.createCell((short) 2).setCellValue("COMPUTER EQUIPMENT");
         rowhead.createCell((short) 3).setCellValue("MACHINE & EQUIPMENT");
         rowhead.createCell((short) 4).setCellValue("INTANGIBLE ASSETS");
         rowhead.createCell((short) 5).setCellValue("MOTOR VEHICLES");
         rowhead.createCell((short) 6).setCellValue("LAND & LEASEHOLD");
     int i = 1;
//     System.out.println("<<<<<<list.size(): "+list.size());
	 for(int k=0;k<list.size();k++)
     {
    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
			String furniture_fittings =  newassettrans.getAssetMaintenance();//furniture_fittings;
			String computer_equipment =  newassettrans.getAssetType();//computer_equipment;
			String barCode =  newassettrans.getBarCode();//narration;
			String machine_equipment =  newassettrans.getDescription();//machine_equipment
			String intangible_assets = newassettrans.getCategoryName();  //intangible_assets; 
			String motor_vehicles = newassettrans.getEngineNo();//motor_vehicles
			String land_leasehold = newassettrans.getLocation();
			String spare1 = newassettrans.getSpare1();
			branchCode = newassettrans.getBranchCode();
//			String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
			Row row = sheet.createRow((int) i);

            row.createCell((short) 0).setCellValue(barCode);
            row.createCell((short) 1).setCellValue(furniture_fittings);
            row.createCell((short) 2).setCellValue(computer_equipment);
            row.createCell((short) 3).setCellValue(machine_equipment);
            row.createCell((short) 4).setCellValue(intangible_assets);
            row.createCell((short) 5).setCellValue(motor_vehicles);
            row.createCell((short) 6).setCellValue(land_leasehold);
            i++;
     }
	   OutputStream stream = response.getOutputStream();
//         new MailSender().AttachmentMail("ayomidematanmi4@gmail.com",file);
         workbook.write(stream);
         stream.close();
         System.out.println("Data is saved in excel file.");
    /* w.write();
     w.close(); */

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
}