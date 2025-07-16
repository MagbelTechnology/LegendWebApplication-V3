package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetBidBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.ApplicationHelper;

import magma.net.dao.MagmaDBConnection;

import com.magbel.legend.bus.ApprovalRecords;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class AssetBidExcelUploadManager extends MagmaDBConnection
{

    private ArrayList assetList;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    public ApprovalRecords approve;
    private MagmaDBConnection dbConnection;
    String userid;
    SimpleDateFormat sdf;
    public AssetBidExcelUploadManager(String uid)
    {
        userid = uid;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        approve = new ApprovalRecords(); 
        dbConnection = new MagmaDBConnection();
        System.out.println("INFO:Excel AssetExcelUploadManager instatiated.");
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
            System.out.println((new StringBuilder("WARN: Error uploading file ->")).append(io.getMessage()).toString());
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
                System.out.println((new StringBuilder("WARN: Error uploading file ->")).append(io.toString()).toString());
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
                    insertCellContents(cell);
                }
            }

        }

    }

    private void insertCellContents(Cell cell[])
    {
        int size = cell.length;
        int count = 0;
        int[] statusList = new int[10];
        System.out.println("Cell Length: "+size);
        ExcelAssetBidBean asset = null;
        boolean bad = false;
        String empty = "";
        String branch_code = "";
        String department_code = "";
        String category_code = "";
        String sub_category_code = "";
        String depreciation_start_date = null;
        String depreciation_end_date = null;
        String description = "";
        String vendor_account = "";
        String vatable_cost = "0";
        String vat_amount = "0";
        String wh_tax_amount = "0";
        String vatvalue = "0";
        double vatrate = 0.0;
        double whtrate = 0.0;
        String serial_number = "";
        String subject_to_vat = "N";
        String date_of_purchase = null;
        String registration_no = "";
        String require_depreciation = "";
        String section_code = "";
        String user_id = userid;
        String residualValue = "";
        String wh_tax_cb = "N";
        String wh_taxPercent = "0";
        String vatPercent = "0";
        String wh_tax = "S";
        String serial_no = "";
        String invoice_No = "";
        String lpo_no = "";
        String bar_code = "";
        String Vendor_Account ="";
        String sbu_code = "";
        String asset_user = "";
        String spare1 = "";
        String spare2 = "";
        String spare3 = "";
        String spare4 = "";
        String spare5 = "";
        String spare6 = "";
        String model = "";
        String make = "";
        String vendor = "";
        String assetMaintainBy = "";
        String purchaseReason = "";
        String location = "";   
        String projectCode = "";
        String errorMessage = "";
        String sNo = "";
        String regionCode = "";
        String zoneCode = "";
        int sheetno = 0;
        boolean status = true;
        boolean status0 = true;
        boolean status1 = true;
        
//        String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        try
        {
            sNo = cell[0].getContents();
            if(!sNo.equalsIgnoreCase("S/No")){
            branch_code = cell[1].getContents();
            department_code = cell[2].getContents();
 //           section_code = cell[3].getContents();
            category_code = cell[3].getContents();
//            sub_category_code = cell[5].getContents();
            sbu_code = cell[4].getContents();
            description = cell[5].getContents();
            vatable_cost = cell[6].getContents();                
            date_of_purchase = cell[7].getContents();        
            location = cell[8].getContents();
            }
//           System.out.println("<<<<<<model: "+model+"  make: "+make+"  vendor: "+vendor+"  assetMaintainBy: "+assetMaintainBy+"  purchaseReason: "+purchaseReason+"  location: "+location+"   projectCode: "+projectCode);
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
        String threshold = approve.getCodeName("select Cost_Threshold from am_gb_company");
//        System.out.println("<<<<<<<status: "+status);
     //   String deleterecord = approve.getCodeName("DELETE FROM am_uploadCheckErrorLog WHERE TRANSACTION_TYPE = 'Asset Creation'");
        if(!sNo.equalsIgnoreCase("S/No")){
        status = true;        	
        String error = "";
        String branchcodeValid = approve.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branch_code+"'");
        System.out.println("=====>branchcodeValid: "+branchcodeValid);
        if(branchcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[0] = '0';}
        else{error = "pass; ";status = true; statusList[0] = '1';}
        
        System.out.println(">>>>>>status: "+status+"   branchcodeValid: "+branchcodeValid);
        errorMessage = "Record "+sNo+" branch_code "+error;
        String deptcodeValid = approve.getCodeName("select DEPT_NAME from am_ad_department where DEPT_CODE = '"+department_code+"'");
        System.out.println("=====>deptcodeValid: "+deptcodeValid);
        if(deptcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[1] = '0';}
        else{error = "pass; ";status = true; statusList[1] = '1';}
        errorMessage = errorMessage+" department_code "+error;
//        String sectcodeValid = approve.getCodeName("select SECTION_NAME from am_ad_section where SECTION_CODE = '"+section_code+"'");
//        System.out.println("=====>sectcodeValid: "+sectcodeValid);
//        if(sectcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
//        status = false; statusList[2] = '0';}
//        else{error = "pass; ";status = true; statusList[2] = '1';}
//        errorMessage = errorMessage+" section_code "+error;
        String catcodeValid = approve.getCodeName("select CATEGORY_NAME from am_ad_category where CATEGORY_CODE = '"+category_code+"'");
        System.out.println("=====>catcodeValid: "+catcodeValid);
        if(catcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[3] = '0';}
        else{error = "pass; ";status = true; statusList[3] = '1';}
        errorMessage = errorMessage+" category_code "+error;
        String sbucodeValid = approve.getCodeName("select SBU_NAME from Sbu_SetUp where SBU_CODE = '"+sbu_code+"'");
        System.out.println("=====>sbucodeValid: "+sbucodeValid);
        if(sbucodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[4] = '0';}
        else{error = "pass; ";status = true; statusList[4] = '1';}
        errorMessage = errorMessage+" sbu_code "+error;
/*        String projectcodeValid = approve.getCodeName("select DESCRIPTION from ST_GL_PROJECT where CODE = '"+projectCode+"'");
        if(projectcodeValid.equalsIgnoreCase("")){error = " "+"fail; ";
        status = false; statusList[5] = '0';}
        else{error = "pass; ";status = true; statusList[5] = '1';}
        errorMessage = errorMessage+" projectCode "+error;  */
        String locationcodeValid = approve.getCodeName("select LOCATION from AM_GB_LOCATION where LOCATION_CODE = '"+location+"'");
        if(locationcodeValid.equalsIgnoreCase("")){error = " "+"fail; "; 
        status = false; statusList[5] = '0';}
        else{error = "pass; ";status = true; statusList[5] = '1';}
        errorMessage = errorMessage+" Location "+error;
   //     vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);
        double totalcost = Double.parseDouble(vatable_cost);
        for (int i = 0; i < 10; ++i) {
        	if(statusList[i] == '0'){status0 = false;}else{status1 = true;}        	
        }
        if(!status0 || !status1){status = false;}
        if(status0 && status1){status = true;}
        System.out.println("<<<<status0: "+status0+"  status1: "+status1);
/*        if(totalcost<Double.parseDouble(threshold)){error = " "+"fail; "; 
        status = false;}  
        else{error = "pass; ";status = true;}        
        errorMessage = errorMessage+" Cost Price Less ThresHold "+error;    */    
        System.out.println("<<<<errorMessage: "+errorMessage+"  status: "+status);
        if(!status){
        boolean rec = insertErrorTransaction(errorMessage,userid);
        }
         }
        if(status && !branch_code.equals(empty))
        {
            try
            {
                String branchId = "0";
                String deptId = "0";
                String sectionId = "0";
                String cateId = "0";
                String subcateId = "0";
                String depreciation_rate = "0";
                Branch branch = admin.getBranchByBranchCode(branch_code);                
                if(branch != null)
                {
                    branchId = branch.getBranchId();
                } else
                {
                    bad = true;
                }
                Department dept = admin.getDeptByDeptCode(department_code);
                if(dept != null)
                {
                    deptId = dept.getDept_id();
                } else
                if(department_code != "0")
                {
                    bad = true;
                }
//                Section sect = admin.getSectionByCode(section_code);
//                if(sect != null)
//                {
//                    sectionId = sect.getSection_id();
//                } else
//                if(section_code != "0")
//                {
//                    bad = true;
//                }
                Category cat = admin.getCategoryByCode(category_code);
                if(cat != null)
                {
                    cateId = cat.getCategoryId();
                    depreciation_rate = cat.getDepRate();
                    residualValue = cat.getResidualValue();
                } else
                {
                    bad = true; 
                }
                if(subject_to_vat.equalsIgnoreCase("Y"))
                {
					 vatrate =  Double.parseDouble(vatPercent)/100;
					 vat_amount = String.valueOf(Double.parseDouble(vatable_cost)*vatrate);
                   // vat_amount = String.valueOf(ftm.getVatAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    vat_amount = "0.00";
                }
                if(wh_tax.equalsIgnoreCase("Y"))
                {
                	whtrate =  Double.parseDouble(wh_taxPercent)/100;
                	wh_tax_amount = String.valueOf(Double.parseDouble(vatable_cost)*whtrate);
//                    wh_tax_amount = String.valueOf(ftm.getWhtAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    wh_tax_amount = "0.00";
                }

                double cost = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
                asset = new ExcelAssetBidBean();
                asset.setBranch_id(branchId);  
                asset.setCategory_id(cateId);
                asset.setVatable_cost(vatable_cost);
                asset.setDate_of_purchase(date_of_purchase);
                asset.setDepartment_id(deptId);
                asset.setDescription(description);
                asset.setSection_id(sectionId);
                asset.setUser_id(user_id);
                asset.setVat_amount(vat_amount);
                asset.setVatable_cost(vatable_cost);
                asset.setCost_price(String.valueOf(cost));
                asset.setGid("1");
                asset.setBranch_code(branch_code);
                asset.setDepartment_code(department_code);
                asset.setCategory_code(category_code);
//                asset.setSection_code(section_code);
                String locationId = approve.getCodeName("select location_id from AM_GB_LOCATION where LOCATION_CODE = '"+location+"'");
                System.out.println("=====locationId: "+locationId+"    location: "+location);
                asset.setLocation(locationId);
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

    private void addCellContent(ExcelAssetBidBean asset)
    {
        int staux = 0;
        try
        {
            staux = asset.insertGroupAssetRecordUpload();
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
	  String transtype = "Asset Creation";
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
