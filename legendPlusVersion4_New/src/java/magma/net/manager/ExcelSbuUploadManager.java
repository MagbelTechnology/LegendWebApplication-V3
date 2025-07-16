package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import org.apache.commons.validator.routines.EmailValidator;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.SbuExcelUploadBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ExcelSbuUploadManager extends MagmaDBConnection
{

    private ArrayList assetList;
    private ArrayList convertvaluelist;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    public ApprovalRecords approve;
    private MagmaDBConnection dbConnection;
    String userid;
    String tranType;
    SimpleDateFormat sdf;
    public ExcelSbuUploadManager(String uid)
    {
        userid = uid;
 //       tranType = transType;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        System.out.println("INFO:Excel ExcelSbuUploadManager instatiated.");
    }

    public ArrayList getAssetList()
    {
        return assetList;
    }

    public void acceptExcel(String fileObj)
    {
        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet sheets[] = (Sheet[])null;
        assetList = new ArrayList();
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
        assetList = new ArrayList();
        if(assetList.isEmpty())
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
        
        System.out.println("Cell Length: "+size);
        SbuExcelUploadBean asset = null;
        boolean bad = false;
        String sNo = "";
        String empty = "";
        String sbuName = "";
        String sbuCode = "";
        String sbuContact = "";
        String status = "ACTIVE";
        String sbuMail = "";
        String errorMessage = "";
        String narration = "SBU Upload";
        int sheetno = 0;
        boolean result = false;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[20];
        int data[] = new int[10]; 
        String registration_no = "";
        String glAccount = "";
//        String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        try
        {
            sNo = cell[0].getContents();
            sbuCode = cell[1].getContents();      
            sbuName = cell[2].getContents();
            sbuContact = cell[3].getContents(); 
            sbuMail = cell[4].getContents();
            
           System.out.println("<<<<<<sNo: "+sNo+"  sbuCode: "+sbuCode+"  sbuName: "+sbuName+"  sbuContact: "+sbuContact+"   sbuMail: "+sbuMail);
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
        System.out.println("<<<<<<<result: "+result+"    sNo: "+sNo);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        if(!sNo.equalsIgnoreCase("S/No")){
       // 	result = true;        	
        String error = "";
        
        boolean valid = EmailValidator.getInstance().isValid(sbuMail);
        System.out.println("<<<<<<<result: "+result+"    valid: "+valid);
        if(!valid){error = "Invalid Mail Address "+"fail; ";
        result = false;}
        else{error = "pass; ";result = true;}
        errorMessage = "Record "+sNo+" SBU Upload "+error;
        if(!result){
        boolean rec = insertErrorTransaction(errorMessage,userid,narration);
        }
    //     }
     //   System.out.println("<<<<<<< result:  "+result);
        if(result)
        {
        	//System.out.println("<<<<<<<sbuCode: "+sbuCode+"   status: "+status);
            try
            {
                asset = new SbuExcelUploadBean();
                asset.setRegistration_no(sbuContact);
                asset.setBar_code(sbuMail);
                asset.setGid("1");
                asset.setDescription(sbuName);
                asset.setTranType(status);
                asset.setUser_id(userid);
                asset.setSbu_code(sbuCode);
                addCellContent(asset);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }
    }

    private void addCellContent(SbuExcelUploadBean asset)
    {
        int staux = 0;
        try
        {
            staux = asset.insertSBURecordUpload();
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
            assetList.add(asset);
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
