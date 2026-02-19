package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.*;
import magma.ExcelAssetImproveBean;
import magma.asset.manager.AssetManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class ImproveExcelUploadManager extends MagmaDBConnection
{

    private ArrayList assetList;
    private ArrayList convertvaluelist;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    public ApprovalRecords approve;
    private MagmaDBConnection dbConnection;
    private AssetManager assetMan;
    private CompanyHandler comp;
    String userid;
    String excelType;
    String groupID;
    SimpleDateFormat sdf;
    public ImproveExcelUploadManager(String uid,String groupAssetByAsset,String groupId)
    {
        userid = uid;
        groupID = groupId;
        excelType = groupAssetByAsset;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        assetMan = new AssetManager();
        comp = new CompanyHandler();
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
        int[] statusList = new int[15];
        //System.out.println("Cell Length: "+size);
        ExcelAssetImproveBean asset = null;
        boolean bad = false;
        String empty = "";
        String assetId = "";
        String group_Id = groupID;
        String vatable_cost = "0";
        String wh_tax_amount = "0";
        String wh_tax_cb = "N";
        String wh_tax = "S";
        String subjectToVat = "";
        String lpoNo = "";
        String vat_amount = "0";
        String serial_number = "";
        String subject_to_vat = "N";
        String user_id = userid;
        String invoice_No = "";
        String lpo_no = "";
        String bar_code = "";
        String Vendor_Account ="";
        String sbu_code = "";
        String vendor = "";
        String location = "";   
        String projectCode = "";
        String errorMessage = "";
        String sNo = "";
        String regionCode = "";
        String zoneCode = "";
        String revalueReason = "";
        String oldcost_price = "";
        String oldvatable_cost = ""; 
        String oldvat_amount = "";
        String oldwht_amount = "";
        String oldnbv = ""; 
        String oldaccum_dep = "";
        String oldIMPROV_NBV = "";
        String oldIMPROV_ACCUMDEP = "";
        String oldIMPROV_COST = ""; 
        String oldIMPROV_VATABLECOST = "";
        String description = ""; 
        String assetCode = "";
        String branchCode = "";
        String categoryCode = "";
        String usefulLife = "";
        String vatRate = "0";
        int sheetno = 0;
        boolean status = true;
        boolean status0 = true;
        boolean status1 = true;
        String convertvaluelist[] = new String[20];
        String convertparam[] = new String[5];
        int data[] = new int[10]; 
        String convertvalue = "";
        String vendorId = "";
//        String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        try
        {
        	if(excelType.equalsIgnoreCase("Y")){
            sNo = cell[0].getContents();
            assetId = cell[1].getContents();      
            sbu_code = cell[2].getContents();
            vatable_cost = cell[3].getContents();        
            wh_tax_amount = cell[4].getContents();                
            wh_tax = cell[5].getContents();          
            subject_to_vat = cell[6].getContents();  
            vatRate = cell[7].getContents();  
            lpo_no = cell[8].getContents();         
            invoice_No = cell[9].getContents();
            Vendor_Account = cell[10].getContents();
            vendor = cell[11].getContents();
            location = cell[12].getContents();
            projectCode = cell[13].getContents();
            revalueReason = cell[14].getContents();
            usefulLife = cell[15].getContents();
            
           //System.out.println("<<<<<<sNo: "+sNo+"  assetId: "+assetId+"  sbu_code: "+sbu_code+"  vatable_cost: "+vatable_cost+"  wh_tax_amount: "+wh_tax_amount+"  location: "+location+"   projectCode: "+projectCode);
            sheetno = sheetno + 1;
        	  }
        	if(excelType.equalsIgnoreCase("N")){
                sNo = cell[0].getContents();
                assetId = cell[1].getContents();      
                vatable_cost = cell[2].getContents();        
                lpo_no = cell[3].getContents();         
                invoice_No = cell[4].getContents();
                Vendor_Account = cell[5].getContents();
//                vendor = cell[6].getContents();
                revalueReason = cell[6].getContents();
                usefulLife = cell[7].getContents();
                
               //System.out.println("<<<<<<sNo: "+sNo+"  assetId: "+assetId+"  sbu_code: "+sbu_code+"  vatable_cost: "+vatable_cost+"  wh_tax_amount: "+wh_tax_amount+"  location: "+location+"   projectCode: "+projectCode);
                sheetno = sheetno + 1;
            	  }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
        //System.out.println("<<<<<<<status: "+status+"    sNo: "+sNo);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        if(!sNo.equalsIgnoreCase("S/No")){
        	if(excelType.equalsIgnoreCase("Y")){
        status = true;        	
        String error = "";
        String param = "select cast(ASSET_CODE as varchar(50))+'#'+BRANCH_CODE+'#'+CATEGORY_CODE from AM_ASSET where ASSET_ID = '"+assetId+"' AND ASSET_STATUS = 'ACTIVE'";
        //System.out.println("<<<<<<<param: "+param); 
        String assetparam = approve.getCodeName(param);
        //System.out.println("<<<<<<<assetparam: "+assetparam); 
        if(assetparam.equalsIgnoreCase("")){error = " Not Found "+"fail; ";
        status = false; statusList[0] = '0';}
        else{error = "pass; ";status = true; statusList[0] = '1';}
        errorMessage = errorMessage+" Record "+sNo+" Asset Id "+error;
        
        if(!assetparam.equals("")){
        convertparam = assetparam.split("#");
        //System.out.println("<<<<<<<<assetparam: "+assetparam);
        assetCode = convertparam[0];
        branchCode = convertparam[1];
        categoryCode = convertparam[2];
        }
        //System.out.println("<<<<<<<<assetCode: "+assetCode+"   branchCode: "+branchCode+"   categoryCode: "+categoryCode);
       
        if(assetCode.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[1] = '0';}
        else{error = "pass; ";status = true; statusList[1] = '1';}
        errorMessage = errorMessage+" Record "+sNo+" Asset Id "+error;
        sbu_code = approve.getCodeName("select SBU_CODE from SBU_SETUP WHERE SBU_CODE = '"+sbu_code+"' AND STATUS = 'ACTIVE'");
        if(sbu_code.equalsIgnoreCase("")){error = "SBU_CODE not Valid "+"fail; ";
        status = false; statusList[2] = '0';}
        else{error = "pass; ";status = true; statusList[2] = '1';}
        errorMessage = errorMessage+sNo+" SBU CODE "+error;
        
        String availableForTrans = comp.checkAssetAvalability(assetId);
        if((availableForTrans != "")&& (!assetparam.equals(""))){error = " Asset is still pending for approval. Cannot be initiated for Improvement "+"fail; ";
        status = false; statusList[3] = '0';}
        else{error = "pass; ";status = true; statusList[3] = '1';}
        errorMessage = errorMessage+" Pending Transaction "+error;
        vendor = approve.getCodeName("select Vendor_Code from am_ad_vendor where account_number = '"+Vendor_Account+"'");
        vendorId = approve.getCodeName("select Vendor_Id from am_ad_vendor where account_number = '"+Vendor_Account+"'");
        String vendorAcctNo = approve.getCodeName("select account_number from am_ad_vendor where account_number = '"+Vendor_Account+"'");
        if(vendorAcctNo.equalsIgnoreCase("")){error = "account_number "+"fail; ";
        status = false; statusList[4] = '0';}
        else{error = "pass; ";status = true; statusList[4] = '1';}
        errorMessage = errorMessage+" Vendor Account NO "+error;
        String vendorCode = approve.getCodeName("select Vendor_Id from am_ad_vendor where Vendor_Code = '"+vendor+"'");
        if(vendorCode.equalsIgnoreCase("")){error = "Vendor_Code "+"fail; ";
        status = false; statusList[5] = '0';}
        else{error = "pass; ";status = true; statusList[5] = '1';}
        errorMessage = errorMessage+" Vendor Code "+error;
        System.out.println("<<<<<<<invoice_No: "+invoice_No);
        String invnumb = vendorId+'-'+invoice_No;
        System.out.println("<<<<<<<invnumb: "+invnumb);
        String invoiceNumValid = approve.getCodeName("select INVOICE_NO from AM_INVOICE_NO where INVOICE_NO = '"+invnumb+"'");
		 if(!invoiceNumValid.equalsIgnoreCase("")){error = " "+"fail; "; 
	        status = false; statusList[14] = '0';}
		  else{error = "pass; ";status = true; statusList[14] = '1';}        
	        errorMessage = errorMessage+" Invoice "+error;
        
        //String convertvalue = approve.getCodeName("SELECT cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(IMPROV_NBV as varchar(50))+'#'+cast(IMPROV_ACCUMDEP as varchar(50))+'#'+cast(IMPROV_COST as varchar(50))+'#'+cast(IMPROV_VATABLECOST as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'");
        String test = "SELECT DESCRIPTION+'#'+cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(coalesce(IMPROV_NBV,0) as varchar(50))+'#'+cast(coalesce(IMPROV_ACCUMDEP,0) as varchar(50))+'#'+cast(coalesce(IMPROV_COST,0) as varchar(50))+'#'+cast(coalesce(IMPROV_VATABLECOST,0) as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'";
        convertvalue = approve.getCodeName(test);
        convertvaluelist = convertvalue.split("#");
        int No = convertvaluelist.length;
        //System.out.println("<<<<<<<No: "+No+"   convertvalue: "+convertvalue);
        //System.out.println("<<<<<<<test: "+test); 
        //System.out.println("<<<<errorMessage: "+errorMessage+"  status: "+status);
   /*     if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }*/
        for (int i = 0; i < 10; ++i) {
        	if(statusList[i] == '0'){status0 = false;}else{status1 = true;}        	
        }
        if(!status0 || !status1){status = false;}
        if(status0 && status1){status = true;}
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
        	 }
        	if(excelType.equalsIgnoreCase("N")){
        status = true;        	
        String error = "";
        String param = "select cast(ASSET_CODE as varchar(50))+'#'+BRANCH_CODE+'#'+CATEGORY_CODE from AM_ASSET where ASSET_ID = '"+assetId+"' AND ASSET_STATUS = 'ACTIVE'";
        //System.out.println("<<<<<<<param: "+param); 
        String assetparam = approve.getCodeName(param);
        //System.out.println("<<<<<<<assetparam: "+assetparam); 
        if(assetparam.equalsIgnoreCase("")){error = " Not Found "+"fail; ";
        status = false; statusList[0] = '0';}
        else{error = "pass; ";status = true; statusList[0] = '1';}
        errorMessage = errorMessage+" Record "+sNo+" Asset Id "+error;
        
        if(!assetparam.equals("")){
        convertparam = assetparam.split("#");
        //System.out.println("<<<<<<<<assetparam: "+assetparam);
        assetCode = convertparam[0];
        branchCode = convertparam[1];
        categoryCode = convertparam[2];
        }
        //System.out.println("<<<<<<<<assetCode: "+assetCode+"   branchCode: "+branchCode+"   categoryCode: "+categoryCode);
        if(assetCode.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[1] = '0';}
        else{error = "pass; ";status = true; statusList[1] = '1';}
        errorMessage = errorMessage+" Record "+sNo+" Asset Id "+error;
        String availableForTrans = comp.checkAssetAvalability(assetId);
        if((availableForTrans != "")&& (!assetparam.equals(""))){error = " Asset is still pending for approval. Cannot be initiated for Improvement "+"fail; ";
        status = false; statusList[2] = '0';}
        else{error = "pass; ";status = true; statusList[2] = '1';}
        errorMessage = errorMessage+" Pending Transaction "+error;
        vendor = approve.getCodeName("select Vendor_Code from am_ad_vendor where account_number = '"+Vendor_Account+"'");
        vendorId = approve.getCodeName("select Vendor_Id from am_ad_vendor where account_number = '"+Vendor_Account+"'");
//        System.out.println("<<<<<<< vendor: "+vendor + " vendorId: " + vendorId); 
        String vendorAcctNo = approve.getCodeName("select account_number from am_ad_vendor where account_number = '"+Vendor_Account+"'");
//        System.out.println("<<<<<<<vendorAcctNo: "+vendorAcctNo); 
        if(vendorAcctNo.equalsIgnoreCase("")){error = "account_number "+"fail; ";
        status = false; statusList[3] = '0';}
        else{error = "pass; ";status = true; statusList[3] = '1';}
        errorMessage = errorMessage+" Vendor Account NO "+error;
        String vendorCode = approve.getCodeName("select Vendor_Id from am_ad_vendor where Vendor_Code = '"+vendor+"'");
        if(vendorCode.equalsIgnoreCase("")){error = "Vendor_Code "+"fail; ";
        status = false; statusList[4] = '0';}
        else{error = "pass; ";status = true; statusList[4] = '1';}
        errorMessage = errorMessage+" Vendor Code "+error;
        
        
        //String convertvalue = approve.getCodeName("SELECT cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(IMPROV_NBV as varchar(50))+'#'+cast(IMPROV_ACCUMDEP as varchar(50))+'#'+cast(IMPROV_COST as varchar(50))+'#'+cast(IMPROV_VATABLECOST as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'");
        String test = "SELECT DESCRIPTION+'#'+cast(cost_price as varchar(50))+'#'+cast(vatable_cost as varchar(50))+'#'+cast(vat as varchar(50))+'#'+cast(Wh_Tax_Amount as varchar(50))+'#'+cast(nbv as varchar(50))+'#'+cast(accum_dep as varchar(50))+'#'+cast(coalesce(IMPROV_NBV,0) as varchar(50))+'#'+cast(coalesce(IMPROV_ACCUMDEP,0) as varchar(50))+'#'+cast(coalesce(IMPROV_COST,0) as varchar(50))+'#'+cast(coalesce(IMPROV_VATABLECOST,0) as varchar(50)) FROM am_asset WHERE ASSET_ID = '"+assetId+"'";
        convertvalue = approve.getCodeName(test);
        convertvaluelist = convertvalue.split("#");
        int No = convertvaluelist.length;
        for (int i = 0; i < 10; ++i) {
        	if(statusList[i] == '0'){status0 = false;}else{status1 = true;}        	
        }
        if(!status0 || !status1){status = false;}
        if(status0 && status1){status = true;}
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
        	 }
         }
        if(status && !assetCode.equals(""))
        {
        	//System.out.println("<<<<<<<assetCode: "+assetCode+"   status: "+status);
            try
            {
            	if(!convertvalue.equals("")){
            	description = convertvaluelist[0];
                oldcost_price = convertvaluelist[1];
                oldvatable_cost = convertvaluelist[2]; 
                oldvat_amount = convertvaluelist[3];
                oldwht_amount = convertvaluelist[4];
                oldnbv = convertvaluelist[5]; 
                oldaccum_dep = convertvaluelist[6];
                oldIMPROV_NBV = convertvaluelist[7];
                oldIMPROV_ACCUMDEP = convertvaluelist[8];
                oldIMPROV_COST = convertvaluelist[9];
                oldIMPROV_VATABLECOST = convertvaluelist[10];        
            	}
                String branchId = "0";
                String deptId = "0";
                String sectionId = "0";
                String cateId = "0";
                String subcateId = "0";
                String depreciation_rate = "0";
                double cost = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
                asset = new ExcelAssetImproveBean();
                asset.setBranch_id(branchId);  
                asset.setCategory_id(cateId);
                asset.setSubCategory_id(subcateId);
                asset.setVatable_cost(vatable_cost);
                asset.setUser_id(user_id);
                asset.setVat_amount(vatRate);
                asset.setVatable_cost(vatable_cost);
                asset.setWh_tax_amount(wh_tax_amount);
                asset.setWh_tax_cb(wh_tax_cb);
                asset.setCost_price(String.valueOf(cost));
                asset.setGid("1");
                asset.setInvoice_No(invoice_No);
                asset.setLpo_no(lpo_no);
                asset.setBar_code(bar_code); 
                asset.setWh_tax(wh_tax);
                asset.setVendor_account(Vendor_Account);
                asset.setSbu_code(sbu_code);
                asset.setSupplied_by(vendorId);
                asset.setImproveReason(revalueReason);
                asset.setProjectCode(projectCode);
                asset.setRegionCode(regionCode);
                asset.setZoneCode(zoneCode);
                asset.setDescription(description);
                asset.setAsset_id(assetId);
                //System.out.println("<<<<<<<<description: "+description);
                asset.setLocation(location);
                asset.setAsset_code(assetCode);
                asset.setBranch_code(branchCode);
                asset.setCategory_code(categoryCode);
                asset.setOldcost_price(oldcost_price);
                asset.setOldvatable_cost(oldvatable_cost);
                asset.setOldvat_amount(oldvat_amount);
                asset.setOldwht_amount(oldwht_amount);
                asset.setOldnbv(oldnbv);
                asset.setOldaccum_dep(oldaccum_dep);
                asset.setOldIMPROV_NBV(oldIMPROV_NBV);
                asset.setOldIMPROV_COST(oldIMPROV_COST);
                asset.setOldIMPROV_ACCUMDEP(oldIMPROV_ACCUMDEP);
                asset.setOldIMPROV_VATABLECOST(oldIMPROV_VATABLECOST);
                asset.setUsefullife(usefulLife);
                addCellContent(asset,group_Id);
               // addCellContent(asset,sheetno);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else{}
    }

    private void addCellContent(ExcelAssetImproveBean asset,String groupId)
    {
        int staux = 0;
        try
        {
            staux = asset.insertGroupAssetImproveRecordUpload(groupId);
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
	  String transtype = "Asset Improvement";
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
