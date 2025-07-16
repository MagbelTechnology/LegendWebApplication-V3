package magma.net.manager;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import jxl.*;
import legend.admin.handlers.AdminHandler;
import legend.admin.objects.*;
import magma.ExcelAssetTransferBean;

import java.text.SimpleDateFormat;

import com.magbel.legend.bus.ApprovalRecords;

import magma.net.dao.MagmaDBConnection;

// Referenced classes of package magma.net.manager:
//            FleetTransactManager

public class AssetTransferExcelUploadManager extends MagmaDBConnection
{

    private ArrayList assetList;
    private AdminHandler admin;
    private FleetTransactManager ftm;
    private ApprovalRecords approve;
    String userid;
    SimpleDateFormat sdf;
    
    public AssetTransferExcelUploadManager(String uid)
    {
        userid = uid;
        admin = new AdminHandler();
        approve = new ApprovalRecords(); 
        ftm = new FleetTransactManager();
        
        System.out.println("INFO:Excel AssetTransferExcelUploadManager instatiated.");
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
            System.out.println((new StringBuilder("WARN: Error uploading Asset Transfer file ->")).append(io.getMessage()).toString());
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
                System.out.println((new StringBuilder("WARN: Error uploading Asset Transfer file ->")).append(io.toString()).toString());
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
        
        System.out.println("Cell Length: "+size);
        ExcelAssetTransferBean asset = null;
        boolean bad = false;
        String empty = "";
        String oldasset_id = "";
        String description = "";
        String oldbranch_code = "";
        String olddepartment_code = "";
        String oldsbu_code = "";
        String oldsection_code = "";
        String oldasset_user = "";
        String newbranch_code = "";
        String newdepartment_code = "";
        String newsbu_code = "";
        String newsection_code = "";
        String newasset_user = "";
        String category_code = "";
        int sheetno = 0;
        boolean status = true;
        try
        {
            oldasset_id = cell[0].getContents();
            System.out.println("====oldasset_id===>>>>>? "+oldasset_id);
            description = cell[1].getContents();
           System.out.println("====description===>>>>>? "+description);  
            oldbranch_code = cell[2].getContents();
           System.out.println("====oldbranch_code===>>>>>? "+oldbranch_code);            
            olddepartment_code = cell[3].getContents();
            System.out.println("====olddepartment_code===>>>>>? "+olddepartment_code);
            oldsbu_code = cell[4].getContents();
            System.out.println("====oldsbu_code===>>>>>? "+oldsbu_code);            
            oldsection_code = cell[5].getContents();
            System.out.println("====oldsection_code===>>>>>? "+oldsection_code);
            oldasset_user = cell[6].getContents();
            System.out.println("====oldasset_user===>>>>>? "+oldasset_user);
            newbranch_code = cell[7].getContents();
           System.out.println("====newbranch_code===>>>>>? "+newbranch_code);            
            newdepartment_code = cell[8].getContents();
            System.out.println("====newdepartment_code===>>>>>? "+newdepartment_code);
            newsbu_code = cell[9].getContents();
            System.out.println("====newsbu_code===>>>>>? "+newsbu_code);            
            newsection_code = cell[10].getContents();
            System.out.println("====newsection_code===>>>>>? "+newsection_code);
            newasset_user = cell[11].getContents();
            System.out.println("====newasset_user===>>>>>? "+newasset_user);   
            category_code = cell[12].getContents();
            System.out.println("====category_code===>>>>>? "+category_code);                 
            sheetno = sheetno + 1;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            status = false;
        }
        if(status && !oldasset_id.equals(empty))
        {
            try
            {
                String oldbranchId = "0";
                String olddeptId = "0";
                String oldsectionId = "0";
                String newbranchId = "0";
                String newdeptId = "0";
                String newsectionId = "0"; 
                String cateId = "0";
             //   String depreciation_rate = "0";
                Branch oldbranch = admin.getBranchByBranchCode(oldbranch_code);                
                if(oldbranch != null)
                {
                    oldbranchId = oldbranch.getBranchId();
                } else
                {
                    bad = true;
                }
                Department olddept = admin.getDeptByDeptCode(olddepartment_code);
                if(olddept != null)
                {
                    olddeptId = olddept.getDept_id();
                } else
                if(olddepartment_code != "0")
                {
                    bad = true;
                }
                Section oldsect = admin.getSectionByCode(oldsection_code);
                if(oldsect != null)
                {
                    oldsectionId = oldsect.getSection_id();
                } else
                if(oldsection_code != "0")
                {
                    bad = true;
                }

                Branch newbranch = admin.getBranchByBranchCode(newbranch_code);                
                if(newbranch != null)
                {
                    newbranchId = newbranch.getBranchId();
                } else
                {
                    bad = true;
                }
                Department newdept = admin.getDeptByDeptCode(newdepartment_code);
                if(newdept != null)
                {
                    newdeptId = newdept.getDept_id();
                } else
                if(newdepartment_code != "0")
                {
                    bad = true;
                }
                Section newsect = admin.getSectionByCode(newsection_code);
                if(newsect != null)
                {
                    newsectionId = newsect.getSection_id();
                } else
                if(newsection_code != "0")
                {
                    bad = true;
                }
               Category cat = admin.getCategoryByCode(category_code);
                if(cat != null)
                {
                    cateId = cat.getCategoryId();
                } else
                {
                    bad = true;
                }                
                asset = new ExcelAssetTransferBean();
                asset.setOldasset_id(oldasset_id);
                asset.setAssetuser(oldasset_user);
                asset.setBranch_id(oldbranchId);  
                asset.setCategory_id(cateId);
                asset.setDepartment_id(olddeptId);
                asset.setDescription(description);
                asset.setSection_id(oldsectionId);
                asset.setSbu_code(oldsbu_code);
                asset.setCategory_code(category_code);
               // asset.setGid("1");
                asset.setNewassetuser(newasset_user);
                asset.setNewbranch_id(newbranchId);
                asset.setNewdepartment_id(newdeptId);
                asset.setNewsection_id(newsectionId);
                asset.setNewsbu_code(newsbu_code);
                asset.setUser_id(userid);
                
                addCellContent(asset);
               // addCellContent(asset,sheetno);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addCellContent(ExcelAssetTransferBean asset)
    {
        int staux = 0;
        try
        {
        	String gid = approve.getCodeName("select MAX(group_id) from am_group_asset_main");
        	System.out.println("Group Id Before insertGroupAssetRecordUpload: "+gid);
            staux = asset.insertGroupAssetRecordUpload(gid);
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

}
