package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetUpdateBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import com.magbel.util.HtmlUtility;
//import com.magbel.legend.bus.ApprovalRecords;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class AssetExcelUpdateManager extends MagmaDBConnection
{

    private ArrayList assetList;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    String userid;
    SimpleDateFormat sdf;
    HtmlUtility htmlUtil = null;
//    ApprovalRecords approve = null;
    private MagmaDBConnection dbConnection;
    public AssetExcelUpdateManager(String uid)
    {
        userid = uid;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        System.out.println("INFO:Excel AssetExcelUpdateManager instatiated.");
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
            for(int x = 1; x < rows; x++)
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
        ExcelAssetUpdateBean asset = null;
        boolean bad = false;
        String empty = "";
        String assetid = "";
        String branch_code = "";
        String department_code = "";
        String category_code = "";
        String depreciation_start_date = null;
        String depreciation_end_date = null;
        String description = "";
        String vendor_account = "";
        String vatable_cost = "0";
        String vat_amount = "0";
        String serial_number = "";
        String engine_no = "";
        String model = "";
        String make = "";
        String subject_to_vat = "N";
        String date_of_purchase = null;
        String registration_no = "";
        String require_depreciation = "";
        String section_code = "";
        String user_id = userid;
        String residualValue = "";
        String wh_tax_cb = "N";
        String wh_tax_amount = "0";
        String wh_tax = "S";
        String serial_no = "";
        String invoice_No = "";
        String lpo_no = "";
        String bar_code = "";
        String Vendor_Account ="";
        String sbu_code = "";
        String asset_user = "";
        String purchase_reason = "";
        String spare1 = "";
        String spare2 = "";
        int i = 0;
        boolean status = true;
        try
        {
            assetid = cell[0].getContents();
            description = cell[1].getContents();
            registration_no = cell[2].getContents();            
            bar_code = cell[3].getContents();
            serial_no = cell[7].getContents();
            model = cell[8].getContents();
            make = cell[9].getContents();
            engine_no = cell[10].getContents();
            asset_user = cell[11].getContents();
            purchase_reason = cell[12].getContents();
            spare1 = cell[14].getContents();
            spare2 = cell[15].getContents();
            
            
 //           lpo_no = cell[4].getContents();
            
            
 //           System.out.println("assetId: "+assetid);
 /*           if(size > 11)
            {
                registration_no = cell[11].getContents();
            } else
            {
                registration_no = " ";
            }
            if(size > 12)
            {
                serial_number = cell[12].getContents();
            } else
            {
                serial_number = " ";
            }*/
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
//        System.out.println("status: "+status+" assetId: "+assetid);
        if(status && !assetid.equals(empty))
        {
 /*       	System.out.println("Inside insertCellContents method ");
            System.out.println("registrationNo: "+registration_no); 
            System.out.println("description: "+description);
            System.out.println("AssetSerialNo: "+serial_no);
            System.out.println("AssetEngineNo: "+engine_no);
            System.out.println("barCode: "+bar_code);
            System.out.println("assetUser: "+asset_user);
            System.out.println("make: "+make);
            System.out.println("model: "+model);
            System.out.println("purchaseReason: "+purchase_reason);
            System.out.println("spare1: "+spare1);
            System.out.println("spare2: "+spare2);
            System.out.println("assetId: "+assetid); */
            try
            {
                System.out.println((new StringBuilder("the Asset id is ::")).append(assetid).toString());
                asset = new ExcelAssetUpdateBean();
                asset.setAsset_id(assetid);  
//                asset.setCategory_id(cateId);
//                asset.setVatable_cost(vatable_cost);
//                asset.setDate_of_purchase(date_of_purchase);
//                asset.setDepartment_id(deptId);
//                asset.setDepreciation_end_date(depreciation_end_date);
//                asset.setDepreciation_rate(depreciation_rate);
//                asset.setDepreciation_start_date(depreciation_start_date);
                asset.setDescription(description);
                asset.setRegistration_no(registration_no);
//                asset.setRequire_depreciation(require_depreciation);
//                asset.setSection_id(sectionId);
//                asset.setSerial_number(serial_number);
//                asset.setSubject_to_vat(subject_to_vat);
//                asset.setUser_id(user_id);
//                asset.setVat_amount(vat_amount);
//                asset.setVatable_cost(vatable_cost);
//                asset.setWh_tax_amount(wh_tax_amount);
 //               asset.setWh_tax_cb(wh_tax_cb);
//                asset.setResidual_value(residualValue);
//                asset.setCost_price(String.valueOf(cost));
//                asset.setGid("1");
//                asset.setBranch_code(branch_code);
//                asset.setDepartment_code(department_code);
//                asset.setCategory_code(category_code);
//                asset.setSection_code(section_code);
                asset.setSerial_no(serial_no);
//                asset.setInvoice_No(invoice_No);
//                asset.setLpo_no(lpo_no);
                asset.setBar_code(bar_code);
                asset.setEngine_number(engine_no);
                asset.setModel(model);
                asset.setMake(make);
                asset.setUser(asset_user);
                asset.setReason(purchase_reason);
 /*               String Sparefld = spare1;
                System.out.println("Spare 1 Before Update: "+Sparefld);
                if(Sparefld.equalsIgnoreCase(" ")){ 
                System.out.println("asset id Inside if: "+assetid.trim());
                String query = "SELECT ASSET_CODE FROM AM_ASSET  WHERE asset_id = '"+assetid.trim()+"'";
                System.out.println("query Inside if: "+query);
                String spare =  htmlUtil.findObject(query);
                System.out.println("Spare 1 Inside if: "+spare);
                Sparefld = spare;
                }
                System.out.println("Spare 1 After Update: "+Sparefld);   */             
                asset.setSpare_1(spare1);
                asset.setSpare_2(spare2);
   //             i=i+1;                
                addCellContent(asset);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    //    System.out.println("Total Number of Records Processed: "+i);
    }

    private void addCellContent(ExcelAssetUpdateBean asset)
    {
        int staux = 0;
        boolean statuxa = false;
        try        
        {//System.out.println("Inside addCellContent");
        String asset_id = asset.getAsset_id();
        //System.out.println("Asset Id Inside addCellContent: "+asset_id);
        	statuxa = asset.updateBulkAsset(asset);
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
    public boolean updateBulkAsset(ArrayList list) {

        ArrayList newList = null;
        Asset asset = null;
        Connection con = null;
        PreparedStatement ps = null;
        htmlUtil = new HtmlUtility();
         
        int[] d =null;
/*        String query= "UPDATE AM_ASSET SET REGISTRATION_NO=?," +
                " DESCRIPTION=?,ASSET_USER=?,ASSET_MAINTENANCE=?,Vendor_AC=?,Asset_Model=?," +
                "Asset_Serial_No=?,Asset_Engine_No=?,Supplier_Name=?,Authorized_By=?,Purchase_Reason=?," +
                "SBU_CODE=?,Spare_1=?,Spare_2=?,BAR_CODE=?, DEPT_ID=?, DEPT_CODE=? WHERE ASSET_ID=?" ;*/
        String query= "UPDATE AM_ASSET SET DESCRIPTION=?," +
        "Asset_Serial_No=?,Asset_Engine_No=?, BAR_CODE=? WHERE ASSET_ID=?" ;

        try {
             con = dbConnection.getConnection("legendPlus");

             for (int i = 0; i < list.size(); ++i) {
        asset = (Asset)list.get(i);
                String assetId = asset.getId();
//                String registrationNo = asset.getRegistrationNo();
                String description = asset.getDescription();
//                String vendorAC = asset.getVendorAc();
//                String assetModel = asset.getAssetMake();
                String AssetSerialNo = asset.getSerialNo();
                String AssetEngineNo = asset.getEngineNo();
                String barCode = asset.getBarCode();
//                String lpo_no = asset.getLPO();
//                int SupplierName = asset.getSupplierName();
//                String assetUser = asset.getAssetUser();
//                int assetMaintenance = asset.getAssetMaintain();
//                String authorizedBy = asset.getAuthorizeBy();
//                String purchaseReason = asset.getPurchaseReason();
//                String sbuCode = asset.getSbuCode();
//                String spare1 = asset.getSpare1();
//                String spare2 = asset.getSpare2(); 
//                String deptId = asset.getDepartmentId();
//                String deptQuery = "SELECT dept_code FROM   am_ad_department WHERE dept_id = '"+deptId+"'";
//                String dept_code = htmlUtil.findObject(deptQuery);
                
//                String barCode = asset.getBarCode();
                 System.out.println("the asset id in Excel update manager is >>>>>>>>>>> " + assetId);
                asset=null;
                 ps = con.prepareStatement(query);

//             ps.setString(1,registrationNo);
             ps.setString(2,description);
//             ps.setString(3,assetUser);
//             ps.setInt(4,assetMaintenance);
//             ps.setString(5,vendorAC);
//             ps.setString(6,assetModel);
             ps.setString(3,AssetSerialNo);
             ps.setString(4,AssetEngineNo);
//             ps.setInt(9,SupplierName);
//             ps.setString(10,authorizedBy);
//             ps.setString(11,purchaseReason);
//             ps.setString(12,sbuCode);
//             ps.setString(13,spare1);
//             ps.setString(14,spare2);             
//             ps.setString(5,lpo_no);
             ps.setString(6,barCode);
//             ps.setString(16,deptId);
//             ps.setString(17,dept_code);
             ps.setString(7,assetId);

            // ps.addBatch();
            ps.execute();
             }
            d=ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("AssetExcelUpdateManager: AssetExcelUpdate()>>>>>" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }
            return (d.length > 0);
    }

    
}
