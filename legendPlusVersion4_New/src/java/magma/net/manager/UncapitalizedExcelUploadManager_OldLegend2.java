package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetBean;

import java.text.SimpleDateFormat;

import magma.net.dao.MagmaDBConnection;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class UncapitalizedExcelUploadManager_OldLegend2 extends MagmaDBConnection
{
	SimpleDateFormat sdf;
    private ArrayList assetList;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    String userid;

    public UncapitalizedExcelUploadManager_OldLegend2(String uid)
    {
        userid = uid;
        admin = new AdminHandler();
        ftm = new FleetTransactManager();
        System.out.println("INFO:Excel UncapitalizedExcelUploadManager instatiated.");
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
            System.out.print((new StringBuilder("Sheet:")).append(index).toString());
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
        ExcelAssetBean asset = null;
        boolean bad = false;
        String empty = "";
        String branch_code = "";
        String department_code = "";
        String category_code = "";
        String subcategory_code = "";
        String depreciation_start_date = null;
        String depreciation_end_date = null;
        String description = "";
        String vendor_account = "";
        String vatable_cost = "0";
        String vat_amount = "0";
        String serial_number = "";
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

        String wh_taxPercent = "0";
        String vatPercent = "0";
        double vatrate = 0.0;
        double whtrate = 0.0;
        
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
        boolean status = true;
        try
        {
            branch_code = cell[0].getContents();
            department_code = cell[1].getContents();
            section_code = cell[2].getContents();
            category_code = cell[3].getContents();
            subcategory_code = cell[4].getContents();
            sbu_code = cell[5].getContents();
            description = cell[6].getContents();
            vatable_cost = cell[7].getContents();
            wh_tax = cell[8].getContents();    
            wh_taxPercent = cell[9].getContents();  
            subject_to_vat = cell[10].getContents();  
            vatPercent = cell[11].getContents();  

            date_of_purchase = cell[12].getContents();
            depreciation_start_date = cell[13].getContents();          
            require_depreciation = cell[14].getContents();
            registration_no = cell[15].getContents();
            serial_no = cell[16].getContents();
            lpo_no = cell[17].getContents();
            bar_code = cell[18].getContents();
            invoice_No = cell[19].getContents();
            Vendor_Account = cell[20].getContents();
            asset_user = cell[21].getContents();
            spare1 = cell[22].getContents();
            spare2 = cell[23].getContents();
            spare3 = cell[24].getContents();
            spare4 = cell[25].getContents();
            spare5 = cell[26].getContents();
            spare6 = cell[27].getContents();
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
                Section sect = admin.getSectionByCode(section_code);
                if(sect != null)
                {
                    sectionId = sect.getSection_id();
                } else
                if(section_code != "0")
                {
                    bad = true;
                }
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
                SubCategory subcat = admin.getSubCategoryByCode(subcategory_code);
                if(subcat != null)
                {
                    subcateId = subcat.getAssetSubCategoryId();
                } else
                {
                    bad = true;
                }                
 /*               if(subject_to_vat.equalsIgnoreCase("Y"))
                {
                    vat_amount = String.valueOf(ftm.getVatAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    vat_amount = "0.00";
                }
                if(wh_tax_cb.equalsIgnoreCase("Y"))
                {
                    wh_tax_amount = String.valueOf(ftm.getWhtAmount(Double.parseDouble(vatable_cost)));
                } else
                {
                    wh_tax_amount = "0.00";
                }*/
         /*       if(!depreciation_rate.equals("0"))
                {
                    depreciation_end_date = getDepEndDate((new StringBuilder(String.valueOf(depreciation_rate))).append(",").append(depreciation_start_date).toString());
                }
                if(depreciation_rate.equals("0"))
                {                
                	depreciation_end_date = "4/1/2008";
                }  */        
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

               // double cost = Double.parseDouble(wh_tax_amount) + Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
                double cost = Double.parseDouble(vat_amount) + Double.parseDouble(vatable_cost);
                System.out.println((new StringBuilder("the branch id is ::")).append(branchId).toString());
                System.out.println((new StringBuilder("the category id is :::")).append(cateId).toString());
                System.out.println((new StringBuilder("the department id is :::")).append(deptId).toString());
                System.out.println((new StringBuilder("the depreciation_end_date is ::: ")).append(depreciation_end_date).toString());
                asset = new ExcelAssetBean();
                asset.setBranch_id(branchId);
                asset.setCategory_id(cateId);
                asset.setSubCategory_id(subcateId);
                asset.setVatable_cost(vatable_cost);
                asset.setDate_of_purchase(date_of_purchase);
                asset.setDepartment_id(deptId);
                asset.setDepreciation_end_date(depreciation_end_date);
              //  asset.setDepreciation_rate(depreciation_rate);
                asset.setDepreciation_start_date(depreciation_start_date);
                asset.setDescription(description);
                asset.setRegistration_no(registration_no);
                asset.setRequire_depreciation(require_depreciation);
                asset.setSection_id(sectionId);
                asset.setSerial_number(serial_number);
                asset.setSubject_to_vat(subject_to_vat);
                asset.setUser_id(user_id);
                asset.setVat_amount(vat_amount);
                asset.setVatable_cost(vatable_cost);
                asset.setWh_tax_amount(wh_tax_amount);
                asset.setWh_tax_cb(wh_tax_cb);
                asset.setResidual_value(residualValue);
                asset.setCost_price(String.valueOf(cost));
                asset.setGid("1");
                asset.setBranch_code(branch_code);
                asset.setDepartment_code(department_code);
                asset.setCategory_code(category_code);
                asset.setSubCategory_code(subcategory_code);
                asset.setSection_code(section_code);
                asset.setSerial_no(serial_no);
                asset.setInvoice_No(invoice_No);
                asset.setLpo_no(lpo_no);
                asset.setBar_code(bar_code); 
                asset.setWh_tax(wh_tax);
                asset.setVendor_account(Vendor_Account);
                asset.setSbu_code(sbu_code);
                asset.setAssetuser(asset_user);
                asset.setSpare_1(spare1);
                asset.setSpare_2(spare2);
                asset.setSpare_3(spare3);
                asset.setSpare_4(spare4);
                asset.setSpare_5(spare5);
                asset.setSpare_6(spare6);
                addCellContent(asset);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addCellContent(ExcelAssetBean asset)
    {
        int staux = 0;
        try
        {
            staux = asset.insertGroupUncapitalizedAssetRecordUpload();
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
}
