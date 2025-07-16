package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

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
import com.magbel.util.HtmlUtility;
import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ExcelMobileRegistrationUploadManager extends MagmaDBConnection
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
    public ExcelMobileRegistrationUploadManager(String uid)
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
                System.out.println((new StringBuilder("WARN: Error in mobile registration for uploading file ->")).append(io.toString()).toString());
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
        legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
        HtmlUtility htmlUtil = new HtmlUtility();
        System.out.println("Cell Length: "+size);
        Mobiles mobile = null;
        boolean bad = false;
        String sNo = "";
        String userName = "";
        String macAddress = "";
        String userStatus = "";
        String errorMessage = "";
        String narration = "Mobile Registration Upload";
        int sheetno = 0;
        boolean result = false;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[20];
        int data[] = new int[10];
        int[] statusList = new int[15];
        boolean status = true;
        
        try
        {
            sNo = cell[0].getContents();
            userName = cell[1].getContents();      
            macAddress = cell[2].getContents();
            userStatus = cell[3].getContents();
            
//           System.out.println("<<<<<<sNo: "+sNo+"  userName: "+userName+"  fullName: "+fullName+"  securityClass: "+securityClass+"   password: "+password+"   userMail: "+userMail);
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
//        System.out.println("<<<<<<<result: "+result+"    sNo: "+sNo);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        String existuser = htmlUtil.findObject("SELECT count(*) FROM AM_GB_USER WHERE USER_NAME = ? ",userName);
        if(!sNo.equalsIgnoreCase("S/No")){
       // 	result = true;        	
        String error = "";
        
        if(existuser.equals("0")){error = " "+"fail; ";result = false;}
        else{error = "pass; ";  result = true;}
        errorMessage = "Record "+sNo+" User already exist in Legend: "+error;
        
        String userNameValid = htmlUtil.findObject("select count(*) from am_gb_regmac where USER_NAME = ? ",userName);
        //  System.out.println("<<<<<<< branchcodeValid:  "+branchcodeValid);
        if(!userNameValid.equals("0")){error = " "+"fail; ";result = false;}
          else{error = "pass; ";status = true;}
          errorMessage = errorMessage+"User does not exist in mobile registration:"+error;
        
       
        
        
       // System.out.println("<<<<<<< result:  "+result+"  error: "+error+"   errorMessage: "+errorMessage+"  existuser: "+existuser);
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
            	 Date date = new Date();  
          	   	sdf = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            	 String currentDate = sdf.format(date);
                 String DD = currentDate.substring(0,2);
                 String MM = currentDate.substring(3,5);
                 String YYYY = currentDate.substring(6,10);           
                 currentDate = YYYY+"-"+MM+"-"+DD;
            	
            	
//                     System.out.println("password ============================ "+password);
//            	System.out.println("<<<<<<<userName: "+userName+"   password Before Encryption: "+password);
//		    	 password = cm.encrypt(security.Name(userName.toLowerCase(),password));
//		    	 String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
//		    	 System.out.println("<<<<<<<userName: "+userName+"   password  After Encryption: "+password);
//            	String userClass = approve.getCodeName("select class_id from  am_gb_class where class_desc like '%"+securityClass+"%'");
    			 userStatus = approve.getCodeName("select User_Status from am_gb_user where User_Name = '"+userName+"' ");
    			 if(userStatus.equals("")) {userStatus = "";}
//    			System.out.println("<<<<<<<branch: "+branch+"   branchCode: "+branchCode+"   userClass: "+userClass);
            	mobile = new Mobiles();
            	mobile.setUserName(userName);
            	mobile.setMacAddress(macAddress);
            	mobile.setMobileStatus(userStatus);
                mobile.setCreateDate(currentDate);
               mobile.setUserId(userid);
               
                addCellContent(mobile);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }
    }

    private void addCellContent(Mobiles mobile)
    {
        int staux = 0;
        try
        {
            staux = mobile.insertUserRecordUpload();
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
            userList.add(mobile);
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
}
