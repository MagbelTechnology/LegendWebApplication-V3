package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.validator.routines.EmailValidator;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.UserExcelUploadBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.Cryptomanager; 
import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ExcelAssetDescriptionUploadManager extends MagmaDBConnection
{

    private ArrayList userList;
    private ArrayList convertvaluelist;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    public ApprovalRecords approve;
    private MagmaDBConnection dbConnection;
    String userid;
    String tranType;
    SimpleDateFormat sdf;
    String sNo = "";
    String id = "";
    String subCategory_Code = "";
    String description = "";
    String status = "";
    String createdBy = "";
    String createdDate = "";
    public ExcelAssetDescriptionUploadManager(String uid)
    {
        userid = uid;
 //       tranType = transType;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        System.out.println("INFO:Excel ExcelStaffUploadManager instatiated.");
    }

    public ArrayList getAssetList()
    {
        return userList;
    }

    public void acceptExcel(String fileObj)
    {
        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet sheets[] = (Sheet[])null;
        userList = new ArrayList();
        try
        {
            Workbook workbook = Workbook.getWorkbook(new File(fileObj));
            sheets = workbook.getSheets();
            performReading(sheets);
        }
        catch(Exception io)
        {
            io.printStackTrace();
            System.out.println((new StringBuilder("WARN: Error in acceptExcel 1 uploading file ->")).append(io.getMessage()).toString());
        }
    }

    public void acceptExcel(File file)
    {
        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet sheets[] = (Sheet[])null;
        userList = new ArrayList();
        if(userList.isEmpty())
        {
            try
            { 
                Workbook workbook = Workbook.getWorkbook(file);
                sheets = workbook.getSheets();
                performReading(sheets);
            }
            catch(Exception io)
            {
                io.printStackTrace();
                System.out.println((new StringBuilder("WARN: Error in sbuExcel for uploading file ->")).append(io.toString()).toString());
            }
        }
    }

    public ArrayList getFileFromServer(File uploadedFile)
    {
        acceptExcel(uploadedFile);
        return getAssetList();
    }

    private void performReading(Sheet sheets[])
    {  
        System.out.println((new StringBuilder("TOTAL SHEETS FOUND IS:")).append(sheets.length).append(" sheets").toString());
        boolean sucessful = true;
        for(int index = 0; index < sheets.length; index++)
        {
            System.out.print((new StringBuilder("Sheet: ")).append(index).toString());
            int rows = sheets[index].getRows();
            int cols = sheets[index].getColumns();
            int counter = 0;
            System.out.println("Column: "+cols);
            //for(int x = 0; x < rows-1; x++)
            for(int x = 0; x < rows; x++)            	
            {
                Cell cell[] = sheets[index].getRow(x);
                
                
                
                
                
                String cellTest = cell[0].getContents();
                if(cellTest != null && !cellTest.equalsIgnoreCase(""))
                {
                	if(rows>1){
                    insertCellContents(cell);
                	}
                }
            }

        }

    }

    private void insertCellContents(Cell cell[])
    {
        int size = cell.length;
        int count = 0;
        legend.admin.handlers.SecurityHandler_07_11_2024 security = new legend.admin.handlers.SecurityHandler_07_11_2024();
        System.out.println("Cell Length: "+size);
        AssetDescription assetDescription = null;
        boolean bad = false;
      
        String errorMessage = "";
        String narration = "Asset Description Upload";
        int sheetno = 0;
        boolean result = false;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[20];
        int data[] = new int[10];
        boolean error_status = true;
       
        try
        {
            sNo = cell[0].getContents(); 
            subCategory_Code = cell[1].getContents();
            description = cell[2].getContents();
            status = cell[3].getContents();
            createdBy =  cell[4].getContents();
            createdDate = cell[5].getContents();
            
//           System.out.println("<<<<<<sNo: "+sNo+"  userName: "+userName+"  fullName: "+fullName+"  securityClass: "+securityClass+"   password: "+password+"   userMail: "+userMail);
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
        System.out.println("<<<<<<<id: "+id+"    sNo: "+sNo);
   
        if(!sNo.equalsIgnoreCase("S/No")){
       // 	result = true;        	
        String error = "";
        
       // boolean valid = EmailValidator.getInstance().isValid(userMail);
//        System.out.println("<<<<<<<result: "+result+"    valid: "+valid+"    userMail: "+userMail);
        
       // if(!valid){error = "Invalid Mail Address "+"fail; ";
      //  result = false;}
        //else{error = "pass; ";result = true;}
//        errorMessage = "Record "+sNo+" User Mail Address "+error;
//        if(!existuser.equals("0")){error = error+" User Already Exist "+"fail; ";
//        result = false;}
//        else{error = "pass; ";result = true;}
//        errorMessage = errorMessage+"Record "+sNo+" User Already Exist "+error;
        
        String sub_Category_Code = approve.getCodeName("select sub_category_name from am_ad_sub_category where sub_category_code = '"+subCategory_Code+"'");
        //  System.out.println("<<<<<<< sub Category Code:  "+sub_Category_Code);
          if(sub_Category_Code.equalsIgnoreCase("")){error = " "+"fail;";
          result = false; error_status = false;}
          else{error = "pass; ";result = true; error_status = true;}
          errorMessage = "Record "+sNo+" Sub Category Code "+error;
          

          String description_Status = approve.getCodeName("select asset_status from am_gb_asset_status where asset_status = '"+status+"'");
        //  System.out.println("<<<<<<< deptcodeValid:  "+deptcodeValid);
          if(!description_Status.equalsIgnoreCase("ACTIVE")){error = " "+"fail; ";
          error_status = false;}
          else{error = "pass; ";error_status = true;}
          errorMessage = errorMessage+" description_Status: "+error;
          
          String created_by = approve.getCodeName("select Full_Name from am_gb_User where USER_ID = '"+createdBy+"'");
          //  System.out.println("<<<<<<< deptcodeValid:  "+deptcodeValid);
            if(created_by.equalsIgnoreCase("")){error = " "+"fail; ";
            error_status = false;}
            else{error = "pass; ";error_status = true;}
            errorMessage = errorMessage+" created_by: "+error;
       
        System.out.println("<<<<<<< result:  "+result+"  error: "+error+"   errorMessage: "+errorMessage);
        if(!result){
        boolean rec = insertErrorTransaction(errorMessage,userid,narration);
        }
    //     }
        System.out.println("<<<<<<< result:  "+result);
        if(result)
        {
        	//System.out.println("<<<<<<<sbuCode: "+sbuCode+"   status: "+status);
            try
            {

            	assetDescription = new AssetDescription();
            	assetDescription.setSubCategoryCode(subCategory_Code);
            	assetDescription.setDescription(description);
            	assetDescription.setDescriptionStatus(status);
            	assetDescription.setUserId(createdBy);
            	assetDescription.setCreateDate(createdDate);
               
                addCellContent(assetDescription);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }
    }

    private void addCellContent(AssetDescription assetDescription)
    {
        int staux = 0;
        try
        {
            staux = assetDescription.insertDescriptionRecordUpload();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
        if(staux == 1 || staux == 2)
        {
            userList.add(assetDescription);
        }
    }
 
public boolean insertErrorTransaction(String errorMessge,String userId,String narration) {
	  boolean done=true;

		   Connection con = null; 
	  PreparedStatement ps = null;
	  String transtype = narration;
	  String query2 = "INSERT INTO [am_uploadCheckErrorLog](USER_ID,ERRORDESCRIPTION,TRANSACTION_TYPE,ERRORDATE)" +
              " VALUES('"+userId+"','"+errorMessge+"','"+transtype+"','"+dbConnection.getDateTime(new java.util.Date())+"')";
//	  System.out.println("<<<<<query: "+query2);
	  try {
	      con = dbConnection.getConnection("legendPlus");
	      ps = con.prepareStatement(query2);      
	      ps.execute();

	  }
	  catch (Exception ex)
	  {
		   done = false;
	      System.out.println("WARNING:cannot insert error_transaction table in insertErrorTransaction->" );
	      ex.printStackTrace();
	  } finally {
	      closeConnect(con, ps);
	  }
	  return done;
	}
public void closeConnect(Connection con, PreparedStatement ps) {
    try {
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    } catch (Exception e) {
        System.out.println("WARNING:Error closing Connection ->" +
                           e.getMessage());
    }

}



  private Connection getConnection() {
		Connection con = null;
		try {
//        	if(con==null){
                Context initContext = new InitialContext();
                String dsJndi = "java:/legendPlus";
                DataSource ds = (DataSource) initContext.lookup(
                		dsJndi);
                con = ds.getConnection();
//        	}
		} catch (Exception e) {
			System.out.println("WARNING: Error 1 getting connection ->"
					+ e.getMessage());
		}
		
		return con;
    }

}
