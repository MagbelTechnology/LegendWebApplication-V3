package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.FleetExcelUploadBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class FleetExcelUploadManager extends MagmaDBConnection
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
    public FleetExcelUploadManager(String uid,String transType)
    {
        userid = uid;
        tranType = transType;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        System.out.println("INFO:Excel FleetExcelUploadManager instatiated.");
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
                System.out.println((new StringBuilder("WARN: Error in acceptExcel 2 uploading file ->")).append(io.toString()).toString());
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
        String assetFleetNo = "0";
//        System.out.println("Cell Length: "+size);
        FleetExcelUploadBean asset = null;
        boolean bad = false;
        String empty = "";
        String oldregNo = "";
        String AssetId = "";
        String amtval = "0";
        String newregNo = "";
        String sNo = "";
        String assetCode = "";
        String assetId = "";
        String branchCode = "";
        String barCode = "";
        String make = "";
        String assetUser = "";
        String branchId = ""; 
        String category_id = "";
        String categoryCode = "";
        String errorMessage = "";
        String description = "";
        String effective_Date = "";
        String sbuCode = "";
        String vendorAcct = "";
        String vendorCode = "";
        String narration = "";
        int sheetno = 0;
        boolean status = true;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[20];
        int data[] = new int[10]; 
        String registration_no = "";
        String glAccount = "";
//        String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        try
        {
            sNo = cell[0].getContents();
//            oldregNo = cell[1].getContents();     
            AssetId = cell[1].getContents(); 
            newregNo = cell[2].getContents();
            description = cell[3].getContents();
            barCode = cell[4].getContents();
            sbuCode = cell[5].getContents();
            make = cell[6].getContents();
            amtval = cell[7].getContents(); 
            assetUser = cell[8].getContents();
            effective_Date = cell[9].getContents();
            vendorCode = cell[10].getContents();
             
           System.out.println("<<<<<<sNo: "+sNo+"  oldregNo: "+oldregNo+"  newregNo: "+newregNo+"  amtval: "+amtval+"   effective_Date: "+effective_Date+"  vendorCode: "+vendorCode+"   tranType: "+tranType);
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
//        System.out.println("<<<<<<<status: "+status+"    sNo: "+sNo);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        if(!sNo.equalsIgnoreCase("S/N")){
        status = true;        	
        String error = ""; 
        narration = approve.getCodeName("SELECT DESCRIPTION FROM FT_PROCESSING_TYPE WHERE FT_TYPE_CODE = '"+tranType+"'");
        glAccount = approve.getCodeName("SELECT GL_ACCOUNT FROM FT_PROCESSING_TYPE WHERE FT_TYPE_CODE = '"+tranType+"'");
        if(tranType.equalsIgnoreCase("002") || !tranType.equalsIgnoreCase("I") || !tranType.equalsIgnoreCase("V")){vendorAcct = approve.getCodeName("SELECT ACCOUNT_NUMBER FROM AM_AD_INSURANCE WHERE INSURANCE_CODE = '"+vendorCode+"'");}
        if(!tranType.equalsIgnoreCase("002") || !tranType.equalsIgnoreCase("I") || tranType.equalsIgnoreCase("V")){vendorAcct = approve.getCodeName("SELECT ACCOUNT_NUMBER FROM AM_AD_VENDOR WHERE VENDOR_CODE = '"+vendorCode+"'");}
        String param = "select cast(ASSET_CODE as varchar(50))+'#'+ cast(BRANCH_ID as varchar(50))+'#'+BRANCH_CODE+'#'+CATEGORY_CODE+'#'+DESCRIPTION+'#'+ASSET_ID+'#'+SBU_CODE+'#'+cast(CATEGORY_ID as varchar(50)) from AM_ASSET where registration_No = '"+newregNo+"' AND ASSET_STATUS = 'ACTIVE'";
        if(!param.equalsIgnoreCase("")){registration_no = oldregNo;}
        if(param.equalsIgnoreCase("")){param = "select cast(ASSET_CODE as varchar(50))+'#'+ cast(BRANCH_ID as varchar(50))+'#'+BRANCH_CODE+'#'+CATEGORY_CODE+'#'+DESCRIPTION+'#'+ASSET_ID+'#'+SBU_CODE+'#'+cast(CATEGORY_ID as varchar(50)) from AM_ASSET where registration_No = '"+newregNo+"' AND ASSET_STATUS = 'ACTIVE'";}
        if(!param.equalsIgnoreCase("")){registration_no = newregNo;}
        System.out.println("<<<<<<<param::: "+param); 
        String assetparam = approve.getCodeName(param);
        if(!assetparam.equals("")){
        convertparam = assetparam.split("#");
        System.out.println("<<<<<<<<assetparam==: "+assetparam);
        assetCode = convertparam[0];
        branchId = convertparam[1];
        branchCode = convertparam[2];  
        categoryCode = convertparam[3];
        description = convertparam[4];
        assetId = convertparam[5];
      //  sbuCode = convertparam[6];
        category_id = convertparam[7];
        System.out.println("<<<<<<<<category_id====: "+category_id);
        }
        
        if(tranType.equalsIgnoreCase("I")){narration = "Insurance";
        if(assetId!=null){assetFleetNo = approve.getCodeName("select count(*) from FLEET_SUMINSURED WHERE ASSET_ID = '"+assetId+"' AND TRAN_TYPE = '"+tranType+"' ");}
        }
        if(tranType.equalsIgnoreCase("V")){narration = "VIO";
        if(assetId!=null){assetFleetNo = approve.getCodeName("select count(*) from FLEET_SUMINSURED WHERE ASSET_ID = '"+assetId+"' AND TRAN_TYPE = '"+tranType+"' ");}
        }
        System.out.println("<<<<<<<<assetCode: "+assetCode+"   branchCode: "+branchCode+"   categoryCode: "+categoryCode+"   glAccount: "+glAccount+"  vendorAcct: "+vendorAcct+"  assetId: "+assetId+"  category_id: "+category_id);
        if(assetCode.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false;}
        else{error = "pass; ";status = true;}
        errorMessage = "Record "+sNo+" Asset Id "+error;
        System.out.println("<<<<<<<<status>>>>>>>>: "+status);
        if(!status){
        	 System.out.println("<<<<<<<<status====: "+status);
        boolean rec = insertErrorTransaction(errorMessage,userid,narration);
        }
         }
       
        if(status && !assetCode.equals(""))
        {
        	System.out.println("<<<<<<<assetCode: "+assetCode+"   assetId: "+assetId+"   status: "+status+"    amtval: "+amtval);
            try
            {
                double cost = Double.parseDouble(amtval);
                asset = new FleetExcelUploadBean();
                asset.setRegistration_no(registration_no);
                asset.setCost_price(String.valueOf(cost));
                asset.setAsset_id(assetId);
//                System.out.println("<<<<<<<assetId: "+assetId);
                asset.setAsset_code(assetCode);
                asset.setBar_code(registration_no);
                asset.setCategory_id(category_id);
                System.out.println("<<<<<<<category_id: "+category_id);
                asset.setGid("1");
                asset.setDescription(description);
                asset.setAsset_code(assetCode);
                asset.setBranch_code(branchCode);
                asset.setBranch_id(branchId);
                asset.setCategory_code(categoryCode);
                asset.setFirst_date_obtained(effective_Date);
                asset.setTranType(tranType);
                asset.setDescription(description);
                asset.setUser_id(userid);
                asset.setSbu_code(sbuCode);
                asset.setVendor_account(vendorAcct);
                asset.setVendorId(vendorCode);
                asset.setGlAccount(glAccount);
                addCellContent(asset,assetFleetNo);
               // addCellContent(asset,sheetno);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }

    private void addCellContent(FleetExcelUploadBean asset,String assetFleetNo)
    {
        int staux = 0;
        try
        {
        	System.out.println("<<<<===assetFleetNo: "+assetFleetNo+"  tranType:"+tranType);
        	if((tranType.equalsIgnoreCase("I") || tranType.equalsIgnoreCase("V")) && assetFleetNo.equals("0")){asset.createAssetUpload();}//else{
        		
        		if(!tranType.equalsIgnoreCase("I") && !tranType.equalsIgnoreCase("V")) {
        			System.out.println("<<<<===Not Available"+"      tranType: "+tranType);
        			staux = asset.insertGroupFleetRecordUpload();}
 //           }
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

    public static String getDepEndDate(String vals)
    {
        if(vals != null)
        {
            StringTokenizer st1 = new StringTokenizer(vals, ",");
            if(st1.countTokens() == 2)
            {
                String s1 = st1.nextToken();
                String s2 = st1.nextToken();
                Float rate = Float.valueOf(Float.parseFloat(s1));
                s2 = s2.replaceAll("-", "/");
                StringTokenizer st2 = new StringTokenizer(s2, "/");
                if(st2.countTokens() == 3)
                {
                    String day = st2.nextToken();
                    String month = st2.nextToken();
                    String year = st2.nextToken();
                    if(year.length() == 4 && day.length() > 0 && day.length() < 3 && month.length() > 0 && month.length() < 3)
                    {
                        Calendar c = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                        int months = (int)((100F / rate.floatValue()) * 12F);
                        c.add(2, months);
                        c.add(6, -1);
                        int endDay = c.get(5);
                        int endMonth = c.get(2) + 1;
                        int endYear = c.get(1);
                        return (new StringBuilder(String.valueOf(endDay))).append("/").append(endMonth).append("/").append(endYear).toString();
                    } 
                }
            }
        }
        return "Error";
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
