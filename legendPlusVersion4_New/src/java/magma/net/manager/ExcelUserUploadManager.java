package magma.net.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import com.magbel.util.CryptManager;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.Cryptomanager; 
import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ExcelUserUploadManager extends MagmaDBConnection
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
    public ExcelUserUploadManager(String uid)
    {
        userid = uid;
 //       tranType = transType;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        System.out.println("INFO:Excel ExcelUserUploadManager instatiated.");
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

    private void performReading(Sheet sheets[]) throws IOException
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

    private void insertCellContents(Cell cell[]) throws IOException
    {
        int size = cell.length;
        int count = 0;
        legend.admin.handlers.SecurityHandler security = new legend.admin.handlers.SecurityHandler();
        System.out.println("Cell Length: "+size);
        Properties prop = new Properties();
      	File file = new File("C:\\Property\\LegendPlus.properties");
      	FileInputStream input = new FileInputStream(file);
      	prop.load(input);
        String passwordValidation = prop.getProperty("PasswordValidation");
        UserExcelUploadBean user = null;
        boolean bad = false;
        String sNo = "";
        String empty = "";
        String userName = "";
        String fullName = "";
        String securityClass = "";
        String legacySystemId = "";
        String branchCode = "";
        String password = "";
        String phoneNo = "";
        String isSupervisor = "";
        String userStatus = "";
        String createDate = "";
        String fleetAdmin = "";
        String sbuCode = "";
        String branchRestriction = "";
        String tokenRequired = "";
        String deptCode = "";
        String regionCode = "";
        String zoneCode = "";
        String regionRestriction = "";
        String zoneRestriction = "";
        String facilityAdmin = "";
        String storeAdmin = "";
        String userMail = "";
        String errorMessage = "";
        String narration = "User Upload";
        int sheetno = 0;
        boolean result = false;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[20];
        int data[] = new int[10]; 
        String loginStatus = "0";
        String passwordMust = passwordValidation.equals("Y") ? "Y" : "N";
        String isStorekeeper = "";
       // Cryptomanager cm = new Cryptomanager();
        CryptManager cm = new CryptManager();
        
        System.out.println("<<<<<<<passwordMust: "+passwordMust);
        try
        {
            sNo = cell[0].getContents();
            userName = cell[1].getContents();      
            fullName = cell[2].getContents();
            legacySystemId = cell[3].getContents();
            securityClass = cell[4].getContents(); 
            branchCode = cell[5].getContents();
            password = cell[6].getContents();
            phoneNo = cell[7].getContents();
            isSupervisor = cell[8].getContents();
            userStatus = cell[9].getContents();
            createDate = cell[10].getContents();
            fleetAdmin = cell[11].getContents();
            userMail = cell[12].getContents();
            branchRestriction = cell[13].getContents();
            deptCode = cell[14].getContents();
            tokenRequired = cell[15].getContents();
            regionCode = cell[16].getContents();
            zoneCode = cell[17].getContents();
            regionRestriction = cell[18].getContents();
            zoneRestriction = cell[19].getContents();
            facilityAdmin = cell[20].getContents();
            storeAdmin = cell[21].getContents();
            isStorekeeper = cell[22].getContents();
            
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
        String existuser = approve.getCodeName("SELECT count(*) FROM am_gb_User WHERE USER_NAME = '"+userName+"'");
        if(!sNo.equalsIgnoreCase("S/No")){
       // 	result = true;        	
        String error = "";
        
        boolean valid = EmailValidator.getInstance().isValid(userMail);
//        System.out.println("<<<<<<<result: "+result+"    valid: "+valid+"    userMail: "+userMail);
        
        if(!valid){error = "Invalid Mail Address "+"fail; ";
        result = false;}
        else{error = "pass; ";result = true;}
        errorMessage = "Record "+sNo+" User Mail Address "+error;
        if(!existuser.equals("0")){error = error+" User Already Exist "+"fail; ";
        result = false;}
        else{error = "pass; ";result = true;}
        errorMessage = errorMessage+"Record "+sNo+" User Already Exist "+error;
        System.out.println("<<<<<<< result:  "+result+"  error: "+error+"   errorMessage: "+errorMessage+"  existuser: "+existuser);
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
//                     System.out.println("password ============================ "+password);
//            	System.out.println("<<<<<<<userName: "+userName+"   password Before Encryption: "+password);
		    	 password = cm.encrypt(security.Name(userName.toLowerCase(),password));
		    	 String userId = (new ApplicationHelper()).getGeneratedId("AM_GB_USER");
//		    	 System.out.println("<<<<<<<userName: "+userName+"   password  After Encryption: "+password);
            	String userClass = approve.getCodeName("select class_id from  am_gb_class where class_desc like '%"+securityClass+"%'");
    			String branch = approve.getCodeName("select BRANCH_ID from am_ad_branch where BRANCH_CODE = '"+branchCode+"' ");
//    			System.out.println("<<<<<<<branch: "+branch+"   branchCode: "+branchCode+"   userClass: "+userClass);
                user = new UserExcelUploadBean();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySystemId);
                user.setUserClass(userClass);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
        		user.setPhoneNo(phoneNo);
        		user.setIsSupervisor(isSupervisor);
        		user.setFleetAdmin(fleetAdmin);
        		user.setMustChangePwd(passwordMust);
 //       		user.setPwdExpiry(expiry);
        		user.setLoginStatus(loginStatus);
        		user.setUserStatus(userStatus);
        		user.setCreatedBy(userid);
        		user.setEmail(userMail);
                user.setBranchRestrict(branchRestriction);
                user.setTokenRequire(tokenRequired);
//                user.setExpiryDays(expiryDays);
//                user.setExpiryDate(expiryDate);
        		user.setDeptCode(deptCode);
 //       		user.setApproveLevel(approveLevel);
        		user.setIsStorekeeper(isStorekeeper);
 //               user.setIsStockAdministrator(isStockAdministrator);
//                user.setDeptRestrict(buRestrict);
//                user.setUnderTaker(undertaker);
                user.setRegionCode(regionCode);
                user.setZoneCode(zoneCode);
                user.setRegionRestrict(regionRestriction);
                user.setZoneRestrict(zoneRestriction);
                user.setIsFacilityAdministrator(facilityAdmin);
                user.setIsStoreAdministrator(storeAdmin);
                addCellContent(user);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }
    }

    private void addCellContent(UserExcelUploadBean user)
    {
        int staux = 0;
        try
        {
            staux = user.insertUserRecordUpload();
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
            userList.add(user);
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
