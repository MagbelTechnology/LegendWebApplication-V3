package com.magbel.admin.handlers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.magbel.util.NumberToWordConverter;
import com.magbel.admin.dao.ConnectionClass;
import com.magbel.util.ApplicationHelper;
import com.magbel.admin.dao.MagmaDBConnection;
import com.magbel.admin.objects.ExcelConfirmation;
import com.magbel.admin.objects.VerifyExcelData;
import com.magbel.admin.bus.ExcelUploadConfirmationBus;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import java.math.*;

import com.magbel.util.FileFormat;

public class ExcelUploadManager  extends ConnectionClass {
	BigDecimal big = null;
	  com.magbel.util.DatetimeFormat df;
	private ArrayList assetList;
	private MagmaDBConnection dbConnection;
	private NumberToWordConverter numToWord;
	private ExcelUploadConfirmationBus serviceBus;
	private ApplicationHelper helper;
	private VerifyExcelData accountData = null;
	ApprovalRecords aprecords = null;
	MagmaDBConnection mgDbCon = null;
	String fail="";
	
	public ExcelUploadManager() throws Exception	{
		try {		
	helper = new ApplicationHelper();
	mgDbCon = new MagmaDBConnection();
    aprecords = new ApprovalRecords();
	accountData = new VerifyExcelData();
	serviceBus = new ExcelUploadConfirmationBus();
	numToWord = new NumberToWordConverter();
	big = new BigDecimal("0");
	    } catch (Exception ex) {
	    }
		}

	public ArrayList getFileFromServer(File uploadedFile) 
    {
        acceptExcel(uploadedFile);
        return getAssetList();
    }	
 public void acceptExcel(File file) 
 {

         int rows = 0;
         int cols = 0;
         Sheet sht = null;
         Sheet[] sheets = null;

         this.assetList = new ArrayList();

         try {
             Workbook workbook = Workbook.getWorkbook(file);
             sheets = workbook.getSheets();
             performReading(sheets);
         } catch (Exception io) {
         	io.printStackTrace();
             System.out.println("WARN: Error uploading file ->" + io.toString());
         }
     }

 
 public ArrayList getAssetList()
    {
        return this.assetList;
    }

    private void performReading(Sheet[] sheets)
    {
          System.out.println("TOTAL SHEETS FOUND IS: " + sheets.length + " sheets");
           boolean sucessful = true;
           for (int index = 0; index < sheets.length; index++) {
        	   System.out.print("Matanmi: ");
              System.out.print("Sheet: " + index);
               int rows = sheets[index].getRows();
               System.out.print("rows: " + rows);
               int cols = sheets[index].getColumns();
               System.out.print("cols: " + cols);
               int counter = 0; 
               for (int x = 1 ; x < rows; x++) 
               {
                   Cell[] cell = sheets[index].getRow(x);

                    
                   String cellTest = cell[0].getContents();
    //               System.out.print("cellTest: " + cellTest);
/*                   
                   if (cellTest == null || cellTest.equalsIgnoreCase("")) 
                   
                   {
                       //Skip reading for empty cell
                   } 
                   else 
                   { */
                       insertCellContents(cell);
                 //  }
                  
              }
           }

       }
    private void insertCellContents(Cell[] cell) 
    {

            int size = cell.length;
            ExcelConfirmation asset = null;
            
            String id="";
            String IssueSubject=""; 
        	String IssueDescription="";
        	String CreationDate="";
        	String EmptyColumn = "";
        	String Assignee="";
        	String AssigneeCode="";
        	String StatusCode="";
            String amountToWords="";
        	String amountInWords="";
        	String beneficiary="";
//        	String branch="";
        	String status="";
//        	String userId="";
        	String authourizedUser="";
        	String confirmedUser=""; 
        	String confirmedBranch=""; 
        	String mandate="";
        	String deleteReason="";
        	  try {// System.out.println("About to Insert in insertCellContents Methodm ");
        	  EmptyColumn = cell[0].getContents();
        		  id = cell[1].getContents();
     //       	accountName = serviceBus.getCodeName("SELECT  ACCT_NAME  FROM CHK.FINACLE_CLEARING_VIEW WHERE FORACID='"+accountNo+"'");
    
                 IssueSubject= cell[2].getContents();
                 IssueDescription= cell[3].getContents();             
                 CreationDate = cell[4].getContents();
                 Assignee= cell[5].getContents();
                 String AssigneeQuery = "SELECT  User_id   FROM   AM_GB_USER where Full_Name = '"+Assignee+"' ";
                 AssigneeCode = aprecords.getCodeName(AssigneeQuery);                 
                 status= cell[6].getContents();
                 String SlaQuery = "SELECT  STATUS_CODE   FROM   HD_STATUS where STATUS_DESCRIPTION = '"+status.toUpperCase()+"' ";
                 StatusCode = aprecords.getCodeName(SlaQuery);        
       
          /*  	amount = Double.parseDouble(cell[3].getContents().replaceAll(",", ""));
            	amountToWords = cell[4].getContents();       
            	mandate  = cell[5].getContents();
            	beneficiary = cell[6].getContents();   
            	confirmedBranch = cell[7].getContents(); 
            	amountInWords = numToWord.wordValue(new BigDecimal(amountToWords))+" only";
            	
            	*/
        	    
    				} 
                  catch (Exception e)
                    {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                  try {       
                  asset = new ExcelConfirmation();
                  asset.setId(id);
                  asset.setIssueSubject(IssueSubject);
                  asset.setIssueDescription(IssueDescription);                                  
                  asset.setCreationDate(CreationDate);
                  asset.setAssignee(AssigneeCode);
                  asset.setStatusCode(StatusCode);            
/*                  asset.setAmountInWords(amountInWords);          
                  asset.setMandate(mandate);
                  asset.setBeneficiary(beneficiary);
                  asset.setStatus("A");*/
      //            asset.setUserId(userId);
     //             asset.setBranch(branch);
                  addCellContent(asset);
                  } 
                  catch (Exception e)
                    {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        }





private void addCellContent(ExcelConfirmation asset) 
{
        int staux=0;
        try 
        {
 /*       	
        	System.out.println("Complaint Id "+asset.getId());
        	System.out.println("Issue Subject "+asset.getIssueSubject());
        	System.out.println("date "+asset.getCreationDate());
        	System.out.println("Issue Description "+asset.getIssueDescription());
  //      	System.out.println("amt "+asset.getAmount());
 //       	System.out.println("word "+asset.getAmountInWords());
        	System.out.println("Assignee "+asset.getAssignee());
        	System.out.println("bene "+asset.getBeneficiary());
        	System.out.println("status "+asset.getStatus());
        	System.out.println("user id "+asset.getUserId());
        	System.out.println("branch "+asset.getBranch());
  */      	
        /*	
        	if (serviceBus.isPostDatedCheque(asset.getChequeDate()))
        		{
        		System.out.println(ErrorMessage.POST_DATED_CHEQUE);	
        		
        		}
        	if (!serviceBus.isStaleCheque(asset.getChequeDate()))
        		{
        		System.out.println(ErrorMessage.STALE_CHEQUE);
        		
        		}
        	if (!serviceBus.doesAccountExist(asset.getAccountNo()))
    			{
        		System.out.println(ErrorMessage.INVALID_ACCT_NUMBER+asset.getAccountNo()+ErrorMessage.INVALID_ACCT_NUMBER2);
        		fail=ErrorMessage.INVALID_ACCT_NUMBER+asset.getAccountNo()+ErrorMessage.INVALID_ACCT_NUMBER2;
        		createConfirmationUploadFail(asset,fail,timeId);
        		
    			}
        	else
    		{*/
        		accountData = serviceBus.findAccountByNo(asset.getId());
        	//	System.out.println("location x1");
        	//	System.out.println("ID After findAccountByNo "+asset.getId());
   			 	//String acctStatus = serviceBus.getCodeName("SELECT  CAM_ACCT_STATUS  FROM CHK.FINACLE_CLEARING_VIEW WHERE FORACID='"+asset.getAccountNo()+"'");
   	            String SlaQuery = "SELECT  complaint_id   FROM   HD_COMPLAINT where complaint_id = '"+asset.getId()+"'";
   	            String acctStatus = aprecords.getCodeName(SlaQuery);   	
   	        //    System.out.println("====acctStatus==> "+acctStatus); 			 	
   			 	if(!acctStatus.equalsIgnoreCase("")) 
	    		{  
	    			//AcctErrorMessage acctErrorMsg = new AcctErrorMessage();
	    		//	acctErrorMsg = serviceBus.findAcctStatusErrorMessageByCode(acctStatus);
	    		//	System.out.println(acctErrorMsg.getErrorDescription());
	    		//	fail=acctErrorMsg.getErrorDescription();
	    			createConfirmationUploadFail(asset,fail);
	    		} 
   			 	else{ System.out.println("Status not set for this Account");}
   			 		
   			 	
        } 
   			 	catch (Throwable e) 
		{
			
			e.printStackTrace();
		}
		if(staux == 1 || staux == 2 )
		{
			
		  	this.assetList.add(asset);
    	}
		

    }

public void createConfirmationUploadFail(ExcelConfirmation asset,String fail) {
//	System.out.println("in fail for = "+asset.getId());

	String CREATE_QUERY = "UPDATE HD_COMPLAINT set request_Subject=?, "
			+ "request_Description=?,"
			+ "technician=?,status=?  where complaint_id = '" + asset.getId()+"' ";
 
	Connection con = null;
	PreparedStatement ps = null;
//	String id = helper.getGeneratedId("CP_CONFIRMATION_FAILED_UPLOAD");
	  int i = 0;
	try {
 
		con = getConnection();
		ps = con.prepareStatement(CREATE_QUERY); 
  
		ps.setString(1, asset.getIssueSubject().trim());
		//ps.setDate(2, df.dateConvert(asset.getCreationDate()));
		ps.setString(2, asset.getIssueDescription().trim());
		ps.setInt(3, Integer.parseInt(asset.getAssignee()));
		ps.setString(4, asset.getStatusCode());
		ps.executeUpdate(); 

	} catch (Exception er) {
		System.out.println("WARN:Error creating createConfirmationUploadFail ->"
				+ er.getMessage());
		
	} finally {
		 try {
             if (con != null) {
                 con.close();
             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
	//	dbConnection.closeConnection(con, ps);				
	}

}



}
