package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetReconcileBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ReconcileExcelUploadManager_22_02_2026 extends MagmaDBConnection
{

    private ArrayList assetList;
    private ArrayList convertvaluelist;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    public ApprovalRecords approve;
    private MagmaDBConnection dbConnection;
    String userid;
    SimpleDateFormat sdf;
    public ReconcileExcelUploadManager_22_02_2026(String uid)
    {
        userid = uid;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        //System.out.println("INFO:Excel AssetExcelUploadManager instatiated.");
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
        //System.out.println((new StringBuilder("TOTAL SHEETS FOUND IS:")).append(sheets.length).append(" sheets").toString());
        boolean sucessful = true;
        for(int index = 0; index < sheets.length; index++)
        {
            System.out.print((new StringBuilder("Sheet: ")).append(index).toString());
            int rows = sheets[index].getRows();
            int cols = sheets[index].getColumns();
            int counter = 0;
            //System.out.println("Column: "+cols);
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
        
        //System.out.println("Cell Length: "+size);
        ExcelAssetReconcileBean asset = null;
        boolean bad = false;
        String empty = "";
        String assetId = "";
        String user_id = userid;
        String barCode = "";
        String errorMessage = "";
        String sNo = "";
        String description = ""; 
        String branchCode = "";
        String assetCode = "";
        String existbranchCode = "";
        String branchId = "";
        String subcatId = "";
        String subcategory_code = "";
        String departId = "";
        String deptCode = "";
        String section = "";
        String sectionCode = "";
        String location = "";
        String state = "";
        String sbuCode = "";
        String serialNo = "";
        String regNo = "";
        String engNo = "";
        String model = "";
        String make = "";
        String vendorCode = "";
        String vendorAccount = "";
        String maintainedby = "";
        String lpo = "";
        String purchaseDate = "";
        String assetuser = "";
        String spare1 = "";
        String spare2 = "";
        String spare3 = "";
        String spare4 = "";
        String spare5 = "";
        String costprice = "";
        String spare6 = "";        
        int sheetno = 0;
        String initiator = "";
        String initiatorBranchId = "";
        boolean status = true;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[10];
        int data[] = new int[10]; 
        String convertvalue = "";
//        String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        try
        {
            sNo = cell[0].getContents();   
            assetId = cell[1].getContents();      
            description = cell[2].getContents();    
            barCode = cell[3].getContents(); 
            sbuCode = cell[4].getContents(); 
            subcatId = cell[5].getContents();  
            departId = cell[6].getContents();  
            section = cell[7].getContents();  
            location = cell[8].getContents();  
            state = cell[9].getContents();  
            serialNo = cell[10].getContents();  
            regNo = cell[11].getContents();  
            engNo = cell[12].getContents();  
            model = cell[13].getContents();  
            make = cell[14].getContents();  
            vendorCode = cell[15].getContents();  
            vendorAccount = cell[16].getContents();  
            maintainedby = cell[17].getContents();  
            costprice = cell[18].getContents(); 
            purchaseDate = cell[19].getContents(); 
            lpo = cell[20].getContents();  
            spare1 = cell[21].getContents();  
            spare2 = cell[22].getContents(); 
            spare3 = cell[23].getContents(); 
            spare4 = cell[24].getContents(); 
            spare5 = cell[25].getContents(); 
            spare6 = cell[26].getContents();   
            branchId = cell[27].getContents(); 
            initiator = cell[28].getContents(); 
            assetuser = cell[29].getContents();   
            initiatorBranchId = cell[30].getContents();  
            if(!sNo.equalsIgnoreCase("S/No")){
            if(subcatId.equals("")){subcatId = "0";}
            if(departId.equals("")){departId = "0";}
//            System.out.println("<<<<<<sNo>>>>>>>>: "+sNo+"  assetId: "+assetId+"  description: "+description+"  branchId: "+branchId+"  subcatId: "+subcatId+"  departId: "+departId+"  section: "+section+"  location: "+location+"  state: "+state+"  barCode: "+barCode+"   sbuCode: "+sbuCode+"  serialNo: "+serialNo+"  regNo: "+regNo+"  engNo: "+engNo+"  model: "+model+""+make+"  initiator: "+initiator);
//            initiatorBranchId = approve.getCodeName("SELECT BRANCH FROM am_gb_User WHERE USER_ID = "+initiator+" ");
            branchCode = approve.getCodeName("SELECT BRANCH_CODE FROM am_ad_branch WHERE BRANCH_ID = "+initiatorBranchId+" ");
            subcatId = approve.getCodeName("SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_ID = "+subcatId+" ");
//            String tester = "SELECT sub_category_Id FROM am_ad_sub_category WHERE sub_category_ID = "+subcatId+" ";
//            System.out.println("<<<<tester: "+tester);
            subcategory_code = approve.getCodeName("SELECT sub_category_code FROM am_ad_sub_category WHERE sub_category_ID = '"+subcatId+"' ");
            deptCode = approve.getCodeName("SELECT Dept_code FROM am_ad_department WHERE dept_Code = '"+departId+"' ");
            departId = approve.getCodeName("SELECT Dept_Id FROM am_ad_department WHERE dept_Code = '"+departId+"' ");
            sectionCode = approve.getCodeName("SELECT Section_Code FROM am_ad_section WHERE Section_Code = '"+section+"' ");
            section = approve.getCodeName("SELECT Section_Id FROM am_ad_section WHERE Section_Code = '"+section+"' ");
            state = approve.getCodeName("SELECT state_code FROM am_gb_states WHERE state_Code = '"+state+"' ");
            location = approve.getCodeName("SELECT location_id FROM AM_GB_LOCATION WHERE location_Code = '"+location+"' ");
            assetCode = approve.getCodeName("SELECT ASSET_CODE FROM AM_ASSET_PROOF WHERE ASSET_ID = '"+assetId+"' ");
//           System.out.println("<<<<<<sNo======: "+sNo+"  assetId: "+assetId+"  barCode: "+barCode+"  branchCode: "+branchCode+"  description: "+description+"  branchId: "+branchId+"  subcatId: "+subcatId+"  departId: "+departId+"  section: "+section+"  location: "+location+"  state: "+state+"  barCode: "+barCode+"   sbuCode: "+sbuCode+"  serialNo: "+serialNo+"  regNo: "+regNo+"  engNo: "+engNo+"  model: "+model+""+make);
            sheetno = sheetno + 1;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
//        System.out.println("<<<<<<<status: "+status+"    sNo: "+sNo);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        if(!sNo.equalsIgnoreCase("S/No")){
        status = true;        	
        String error = "";
        String param = "select cast(ASSET_CODE as varchar(50))+'#'+BRANCH_CODE+'#'+ASSET_ID+'#'+DESCRIPTION+'#'+SBU_CODE+'#'+cast((coalesce(SUB_CATEGORY_ID,0)) as varchar(5))+'#'+cast((coalesce(Dept_ID,0)) as varchar(5))+'#'+cast((coalesce(Section_id,0)) as varchar(5))+'#'+cast((coalesce(Location,0)) as varchar(5))+'#'+cast((coalesce(State,0)) as varchar(5)) from AM_ASSET where ASSET_CODE = '"+assetCode+"' ";
//        String param = "select cast(ASSET_CODE as varchar(50))+'#'+BRANCH_CODE from AM_ASSET where ASSET_ID = '"+assetId+"' ";
 //       String param = "select cast(ASSET_CODE as varchar(50))+'#'+BRANCH_CODE from AM_ASSET where BAR_CODE = '"+barCode+"' ";
//        System.out.println("<<<<<<<param: "+param); 
        String assetparam = approve.getCodeName(param);
        if(!assetparam.equals("")){
        convertparam = assetparam.split("#");
//        System.out.println("<<<<<<<<assetparam: "+assetparam);
        assetCode = convertparam[0];
        existbranchCode = convertparam[1];
        assetId = convertparam[2];
 //       System.out.println("<<<<<<<<existbranchCode: "+existbranchCode+"  branchId: "+branchId+"   assetCode: "+assetCode);
//        description = convertparam[3];
//        sbuCode = convertparam[4];
//        subcatId = convertparam[5];
//        departId = convertparam[6];
//        section = convertparam[7];
//        location = convertparam[8];
//        state = convertparam[9];
        }
        if(assetCode.equals("")){assetCode = "0";}
 //       System.out.println("<<<<<<<<state: "+state);
//        System.out.println("<<<<<<<<assetCode: "+assetCode+"   branchCode: "+branchCode+"   existbranchCode: "+existbranchCode);
/*
        if(assetCode.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false;}
        else{error = "pass; ";status = true;}
        errorMessage = "Record "+sNo+" Asset Id "+error;
*/
        //String convertvalue = approve.getCodeName("SELECT cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(IMPROV_NBV as varchar(50))+'#'+cast(IMPROV_ACCUMDEP as varchar(50))+'#'+cast(IMPROV_COST as varchar(50))+'#'+cast(IMPROV_VATABLECOST as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'");
  //      String test = "SELECT DESCRIPTION+'#'+cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(coalesce(IMPROV_NBV,0) as varchar(50))+'#'+cast(coalesce(IMPROV_ACCUMDEP,0) as varchar(50))+'#'+cast(coalesce(IMPROV_COST,0) as varchar(50))+'#'+cast(coalesce(IMPROV_VATABLECOST,0) as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'";
   //     convertvalue = approve.getCodeName(test);
  //      convertvaluelist = convertvalue.split("#");
 //       int No = convertvaluelist.length;
 //       System.out.println("<<<<<<<No: "+No+"   convertvalue: "+convertvalue);
 //       System.out.println("<<<<<<<test: "+test); 
//        System.out.println("<<<<errorMessage: "+errorMessage+"  status: "+status);
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
         }
        if(status && !assetCode.equals(""))
        {
//        	System.out.println("<<<<<<<assetCode: "+assetCode+"   status: "+status);
            try
            {
                asset = new ExcelAssetReconcileBean();
                asset.setUser_id(user_id);
                asset.setGid("1");
                asset.setBar_code(barCode); 
                asset.setDescription(description);
                asset.setAsset_id(assetId);
//                System.out.println("<<<<<<<<description: "+description);
                asset.setAsset_code(assetCode);
                asset.setBranch_code(branchCode);
                asset.setExistBranchCode(existbranchCode);
                asset.setAssetMaintainBy(maintainedby);
                asset.setSubCategory_id(subcatId);
                asset.setSubCategory_code(subcategory_code);
                asset.setSbu_code(sbuCode);
                asset.setDepartment_id(departId);
                asset.setDepartment_code(deptCode);
                asset.setSection_id(section);
                asset.setSection_code(sectionCode);
                asset.setLocation(location);
                asset.setState(state);
                asset.setAssetMaintainBy(maintainedby);
                asset.setSerial_no(serialNo);
                asset.setRegistration_no(regNo);
                asset.setEngine_number(engNo);
                asset.setModel(model);
                asset.setMake(make);
                asset.setSupplied_by(vendorCode);
                asset.setVendor_account(vendorAccount);
                asset.setLpo_no(lpo);
                asset.setSpare1(spare1);
                asset.setSpare2(spare2);
                asset.setSpare3(spare3);
                asset.setSpare4(spare4);
                asset.setSpare5(spare5);
                asset.setSpare6(spare6);
                
                addCellContent(asset);
               // addCellContent(asset,sheetno);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }

    private void addCellContent(ExcelAssetReconcileBean asset)
    {
        int staux = 0;
        try
        {
            staux = asset.insertGroupAssetReconcileRecordUpload();
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

public boolean insertErrorTransaction(String errorMessge,String userId) {
	  boolean done=true;

		   Connection con = null; 
	  PreparedStatement ps = null;
	  String transtype = "Reconciliation Upload";
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
