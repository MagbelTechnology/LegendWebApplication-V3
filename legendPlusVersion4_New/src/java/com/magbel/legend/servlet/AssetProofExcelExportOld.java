package com.magbel.legend.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
//import com.magbel.dao.PersistenceServiceDAO;















import javax.servlet.*;
import javax.servlet.http.*;

import magma.AssetRecordsBean;
import magma.net.dao.MagmaDBConnection;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jxl.Workbook;
import jxl.write.Number;
import jxl.write.*;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.bus.Report;
import com.magbel.legend.mail.EmailSmsServiceBus;
import com.magbel.dao.PersistenceServiceDAO;

public class AssetProofExcelExportOld extends HttpServlet {
	private EmailSmsServiceBus mail ;
	private AssetRecordsBean ad;
	private ApprovalRecords records;
//    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException	
{
    	mail= new EmailSmsServiceBus();
    	records = new ApprovalRecords();
    	try {
			ad = new AssetRecordsBean();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PrintWriter out = response.getWriter();
        OutputStream outp = null;
      //  FileOutputStream out;
        //PrintStream out = new PrintStream();
        HttpSession session = request.getSession();
        String branch_Id = request.getParameter("BRANCH_ID");
        String branchCode = request.getParameter("BRANCH_CODE");
        String tranId = request.getParameter("tranId");
        String prooftranId = request.getParameter("prooftranId");
        String mails = request.getParameter("mails1");
        String subject = request.getParameter("subject1");
        String msgText   = request.getParameter("msgText1");
        String otherparam  = request.getParameter("otherparam");
        Report rep = new Report();
//        System.out.println("<<<<<<branch_Id: "+branch_Id+"    prooftranId: "+prooftranId+"  branchCode: "+branchCode);
        String copymails = records.getCodeName("select EMAIL_ADDRESS from am_branch_Manager where BRANCH_CODE = "+branchCode+"");
//        System.out.println("<<<<<<copymails: "+copymails+"   Query: "+"select EMAIL_ADDRESS from am_branch_Manager where BRANCH_CODE = "+branchCode+"");
        String q = "update am_Asset_Proof set exported='YES' where BATCH_ID = '" + prooftranId + "'";
        ad.updateAssetStatusChange(q);

                String delim = ",";
                // CONSTRUCT COLUMNS AND ALLIAS
                String ColQuery = "";
                if(branch_Id!=null){ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BRANCH_ID = '"+branch_Id+"' AND BATCH_ID = '"+prooftranId+"'";}
                else{ColQuery ="SELECT *FROM am_Asset_Proof WHERE PROCESS_STATUS = 'WFA' AND BATCH_ID = '"+prooftranId+"'";}

                System.out.println("Report Query:   " + ColQuery+"   branch_Id: "+branch_Id);
                
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                String query = "";
                String comp = "";
                try {
                	
                	java.util.ArrayList list =rep.getWorkBookSqlRecords(ColQuery);
                	System.out.println("-->size of Array List for Work Book >--> "+list.size());
               if(list.size()!=0){
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    
                    HSSFSheet sheet = workbook.createSheet("workbook");
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell((short) 0).setCellValue("ASSET_ID");
                    rowhead.createCell((short) 1).setCellValue("OLD ASSET ID");
                    rowhead.createCell((short) 2).setCellValue("CATEGORY NAME");
                    rowhead.createCell((short) 3).setCellValue("BRANCH NAME");
                    rowhead.createCell((short) 4).setCellValue("DEPARTMENT NAME");
                    rowhead.createCell((short) 5).setCellValue("SECTION NAME");
                    rowhead.createCell((short) 6).setCellValue("LOCATION");
                    rowhead.createCell((short) 7).setCellValue("STATE");
                    rowhead.createCell((short) 8).setCellValue("DESCRITION");
                    rowhead.createCell((short) 9).setCellValue("BAR CODE");
                    rowhead.createCell((short) 10).setCellValue("SBU CODE");
                    rowhead.createCell((short) 11).setCellValue("SERIAL NO.");
                    rowhead.createCell((short) 12).setCellValue("REGISTRATION NO");
                    rowhead.createCell((short) 13).setCellValue("ENGINE NO");
                    rowhead.createCell((short) 14).setCellValue("ASSET MODEL");
                    rowhead.createCell((short) 15).setCellValue("MAKE");
                    rowhead.createCell((short) 16).setCellValue("LPO");
                    rowhead.createCell((short) 17).setCellValue("VENDOR NAME");
                    rowhead.createCell((short) 18).setCellValue("VENDOR ACCOUNT No.");
                    rowhead.createCell((short) 19).setCellValue("COST PRICE");
                    rowhead.createCell((short) 20).setCellValue("ACCUMULATED DEPRECIATION");
                    rowhead.createCell((short) 21).setCellValue("MONTHLY DEPRECIATION");
                    rowhead.createCell((short) 22).setCellValue("NBV");
                    rowhead.createCell((short) 23).setCellValue("IMPROVE COST PRICE");
                    rowhead.createCell((short) 24).setCellValue("IMPROVE MONTHLY DEPRECIATION");
                    rowhead.createCell((short) 25).setCellValue("IMPROVE ACCUMULATED DEPRECIATION");
                    rowhead.createCell((short) 26).setCellValue("IMPROVE NBV");
                    rowhead.createCell((short) 27).setCellValue("PURCHASE DATE");
                    rowhead.createCell((short) 28).setCellValue("COMPONENT");
                    rowhead.createCell((short) 29).setCellValue("COMPONENT BARCODE");
                    rowhead.createCell((short) 30).setCellValue("SPARE FIELD 1");
                    rowhead.createCell((short) 31).setCellValue("SPARE FIELD 2");
                    rowhead.createCell((short) 32).setCellValue("SPARE FIELD 3");
                    rowhead.createCell((short) 33).setCellValue("SPARE FIELD 4");
                    rowhead.createCell((short) 34).setCellValue("ASSET USER");
                    rowhead.createCell((short) 35).setCellValue("COMMENTS");
                    rowhead.createCell((short) 36).setCellValue("ASSET SIGHTED");
                    rowhead.createCell((short) 37).setCellValue("ASSET FUNCTIONING");
                    rowhead.createCell((short) 38).setCellValue("ASSET CODE");
//                    rowhead.createCell((short) 38).setCellValue("BRANCH ID");
                    rowhead.createCell((short) 39).setCellValue("BATCH ID");
                    int i = 1;
           	     for(int k=0;k<list.size();k++)
        	     {
        	    	 com.magbel.legend.vao.newAssetTransaction  newassettrans = (com.magbel.legend.vao.newAssetTransaction)list.get(k);    	 
        				String assetId =  newassettrans.getAssetId();
        				String oldassetId =  newassettrans.getOldassetId();
        				String barcode =  newassettrans.getBarCode();
        				String Description = newassettrans.getDescription();   
        				String assetuser = newassettrans.getAssetUser();
        				String assetcode = newassettrans.getAssetCode();
        				String branchId = newassettrans.getBranchCode();
        				String branchName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
        				double costprice = newassettrans.getCostPrice();
        				double monthlyDepr = newassettrans.getMonthlyDep();
        				double accumDepr = newassettrans.getAccumDep();
        				double nbv = newassettrans.getNbv();
        				double improvcostPrice = newassettrans.getImprovcostPrice();
        				double improvmonthldepr = newassettrans.getImprovmonthlyDep();
        				double improvaccumDep = newassettrans.getImprovaccumDep();
        				double improvnbv = newassettrans.getImprovnbv();
        				double totalnbv = newassettrans.getTotalnbv();
        				String categoryId = newassettrans.getCategoryCode();
        				String categoryName = records.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_ID = "+branchId+"");
        				String batchId = newassettrans.getIntegrifyId();
        				String sighted = newassettrans.getAssetsighted();
        				String function = newassettrans.getAssetfunction();
        				String comments = newassettrans.getAssetMaintenance();
        				String make = newassettrans.getAssetMake();
        				String lpo = newassettrans.getLpo();
        				String vendoAcct = newassettrans.getVendorAC();
        				String vendorId = newassettrans.getSupplierName();
        				String vendorName = records.getCodeName("select VENDOR_NAME from am_ad_vendor where VENDOR_ID = "+vendorId+"");
        				String model = newassettrans.getAssetModel();
        				String spare1 = newassettrans.getSpare1();
        				String spare2 = newassettrans.getSpare2();
        				String spare3 = newassettrans.getSpare3();
        				String spare4 = newassettrans.getSpare4();
        				String spare5 = newassettrans.getSpare5();
        				String spare6 = newassettrans.getSpare6();        				
        				String registrationNo = newassettrans.getRegistrationNo();
        				String sbucode = newassettrans.getSbuCode();
        				String serialNo = newassettrans.getSerialNo();
        				String engineNo = newassettrans.getEngineNo();
        				String purchaseDate = newassettrans.getDatepurchased();
        				String deptId = newassettrans.getDeptCode();
        				String deptName = records.getCodeName("select DEPT_NAME from am_ad_department where DEPT_ID = "+deptId+"");
        				String sectionId = newassettrans.getSectionCode();
        				String sectionName = records.getCodeName("select SECTION_NAME from am_ad_section where SECTION_ID = "+sectionId+"");
        				String locationId = newassettrans.getLocation();
        				String locationName = records.getCodeName("select LOCATION from AM_GB_LOCATION where LOCATION_ID = "+locationId+"");
        				String stateId = newassettrans.getState();
        				String stateName = records.getCodeName("select STATE_NAME from am_gb_states where STATE_ID = "+stateId+"");
        				
                        HSSFRow row = sheet.createRow((short) i);
                        row.createCell((short) 0).setCellValue(assetId);
                        row.createCell((short) 1).setCellValue(oldassetId);
                        row.createCell((short) 2).setCellValue(categoryName);
                        row.createCell((short) 3).setCellValue(branchName);
                        row.createCell((short) 4).setCellValue(deptName);
                        row.createCell((short) 5).setCellValue(sectionName);
                        row.createCell((short) 6).setCellValue(locationName);
                        row.createCell((short) 7).setCellValue(stateName);                        
                        row.createCell((short) 8).setCellValue(Description);
                        row.createCell((short) 9).setCellValue(barcode);
                        row.createCell((short) 10).setCellValue(sbucode);
                        row.createCell((short) 11).setCellValue(serialNo);
                        row.createCell((short) 12).setCellValue(registrationNo);
                        row.createCell((short) 13).setCellValue(engineNo);
                        row.createCell((short) 14).setCellValue(model);
                        row.createCell((short) 15).setCellValue(make);
                        row.createCell((short) 16).setCellValue(lpo);
                        row.createCell((short) 17).setCellValue(vendorName);
                        row.createCell((short) 18).setCellValue(vendoAcct);
                        row.createCell((short) 19).setCellValue(costprice);
                        row.createCell((short) 20).setCellValue(monthlyDepr);
                        row.createCell((short) 21).setCellValue(accumDepr);
                        row.createCell((short) 22).setCellValue(nbv);
                        row.createCell((short) 23).setCellValue(improvcostPrice);
                        row.createCell((short) 24).setCellValue(improvmonthldepr);
                        row.createCell((short) 25).setCellValue(improvaccumDep);
                        row.createCell((short) 26).setCellValue(improvnbv);
//                        row.createCell((short) 18).setCellValue(totalnbv);
                        row.createCell((short) 27).setCellValue(purchaseDate);
                        row.createCell((short) 28).setCellValue(spare1);
                        row.createCell((short) 29).setCellValue(spare2);
                        row.createCell((short) 30).setCellValue(spare3);
                        row.createCell((short) 31).setCellValue(spare4);
                        row.createCell((short) 32).setCellValue(spare5);
                        row.createCell((short) 33).setCellValue(spare6);                        
                        row.createCell((short) 34).setCellValue(assetuser);
                        row.createCell((short) 35).setCellValue(comments);
                        row.createCell((short) 36).setCellValue(sighted);
                        row.createCell((short) 37).setCellValue(function);
                        row.createCell((short) 38).setCellValue(assetcode);

                        row.createCell((short) 39).setCellValue(batchId);
                        i++;
        	     }
                	
                    String fileName = branch_Id+"AssetProof.xls";
                    String filePath = System.getProperty("user.home")+"\\Downloads";
  //                  String filePath = System.getProperty("user.dir");
                    File file =    new File(filePath+"\\"+fileName);
                    System.out.print("Location Path:>>> "+file);
                    System.out.print("Absolute Path:>>> "+file.getAbsolutePath());
                    String dir = System.getProperty("user.dir");
                    System.out.print("Directory Location Path:>>> "+dir);
                    Path currentWorkingDir = Paths.get("").toAbsolutePath();
                    System.out.println(currentWorkingDir.normalize().toString());
                    
                    FileOutputStream fileOut = new FileOutputStream(file);
                    workbook.write(fileOut);
                    fileOut.close();
                    System.out.print("AssetProof mails: :>>> "+copymails+"   subject: "+subject+"   msgText: "+msgText);
                    mail.sendMailWithAttachment(copymails,subject,msgText,branch_Id,fileName);
                    out.print("<script>alert('Your Asset Proof has been exported to Excel file in your Downloads folder with File Name "+fileName+" ')</script>");
                    out.print("<script>window.location='DocumentHelp.jsp?np=AssetProofByBranchApproval&tranId="+prooftranId+"&transaction_level=1&approval_level_count=0'</script>");
                }
               else{
                   out.print("<script>alert('No Record to export to Excel ')</script>");
                   out.print("<script>window.location='DocumentHelp.jsp?np=AssetProofByBranchApproval&tranId="+prooftranId+"&transaction_level=1&approval_level_count=0'</script>");
               }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

        }
    
    }
//}


