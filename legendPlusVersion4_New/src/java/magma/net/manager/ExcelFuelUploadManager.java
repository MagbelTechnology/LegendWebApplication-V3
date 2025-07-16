package magma.net.manager;

import jxl.*;

import java.util.Date;
import java.io.File;
import java.sql.*;
import java.util.*;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.FileFormat;

import magma.net.vao.BranchAllocation;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetBudgetManager;;

/**
 * <p>Title: ExcelUploadManager.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class ExcelFuelUploadManager extends MagmaDBConnection {
    private ArrayList budgetList;
    public ApprovalRecords approve;
    public FleetBudgetManager allocate;
    String budgetexist = "";
    static String groupId = "";
    String groupIdent = "";
    public ExcelFuelUploadManager(String groupId) {
    	System.out.println("<<<<<<<<<<<<<<groupId in ExcelFuelUploadManager: "+groupId);
    	groupIdent = groupId;
    	approve = new ApprovalRecords(); 
    	allocate = new FleetBudgetManager(); 
    	
 //       System.out.println("INFO:Excel ExcelFuelUploadManager instatiated.");
    }

    public ArrayList getBudgetList() {
        return this.budgetList;
    }

    public void acceptExcel(String fileObj) {

        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet[] sheets = null;

        this.budgetList = new ArrayList();

        try {
            Workbook workbook = Workbook.getWorkbook(new File(fileObj));
            sheets = workbook.getSheets();
            performReading(sheets);
        } catch (Exception io) {
            System.out.println("WARN: Error uploading file ->" + io.getMessage());
        }
    }

    public void acceptExcel(File file) {

        int rows = 0;
        int cols = 0;
        Sheet sht = null;
        Sheet[] sheets = null;

        this.budgetList = new ArrayList();

        try {
            Workbook workbook = Workbook.getWorkbook(file);
            sheets = workbook.getSheets();
            performReading(sheets);
        } catch (Exception io) {
            System.out.println("WARN: Error uploading file ->" + io.toString());
        }
    }

    public ArrayList getFileFromServer(File uploadedFile) {
    	String groupIdent = groupId;
        acceptExcel(uploadedFile);
        return getBudgetList();
    }


    private void performReading(Sheet[] sheets) {
        System.out.println("TOTAL SHEETS FOUND IS:" + sheets.length + " sheets");
        boolean sucessful = true;
        for (int index = 0; index < sheets.length; index++) {
            System.out.print("Sheet:" + index);
            int rows = sheets[index].getRows();
            int cols = sheets[index].getColumns();
            for (int x = 1; x < rows; x++) {
                Cell[] cell = sheets[index].getRow(x);

                String cellTest = cell[0].getContents();
                if (cellTest == null || cellTest.equalsIgnoreCase("")) {
                    //Skip reading for empty cell
                } else {
                    insertCellContents(cell,groupIdent);
                }
            }
        }

    }

    private void insertCellContents(Cell[] cell,String groupIdent) {
		System.out.println("<<<<<<<<<<groupIdent in  insertCellContents >>>>>>>: "+groupIdent);
        int size = cell.length;
        String sNo = "";
        String empty = "";
        String branchName = "";
        String branchCode = "";
        String categoryCode = "";
        String sbuCode = "";
        String total = "";
        String firstQuarter = "";
        String secondQuarter = "";
        String thirdQuarter = "";
        String fourthQuarter = "";
        String residue = "0";
        String categoryName = "";
       
        boolean status = true;

        try {
        	sNo = cell[0].getContents();
        	System.out.println("<<<<<<<<<< sNo >>>>>>>: "+sNo+"    groupIdent: "+groupIdent);
            branchCode = cell[1].getContents();
            categoryCode = cell[2].getContents();
            sbuCode = cell[3].getContents();
            total = cell[4].getContents();
            firstQuarter = cell[5].getContents();
            secondQuarter = cell[6].getContents();
            thirdQuarter = cell[7].getContents(); 
            fourthQuarter = cell[8].getContents();
            branchName = approve.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branchCode+"' AND BRANCH_STATUS = 'ACTIVE'");    
            categoryName = approve.getCodeName("select CATEGORY_NAME FROM AM_AD_CATEGORY WHERE CATEGORY_CODE = '"+categoryCode+"' AND CATEGORY_STATUS = 'ACTIVE'"); 
            System.out.println("<<<<sNo: "+sNo+"  branchCode: "+branchCode+"  branchName: "+branchName+"  categoryCode: "+categoryCode+"  sbuCode: "+sbuCode);
            System.out.println("<<<<sNo: "+sNo+"  branchCode: "+branchCode+"  branchName: "+branchName+"  categoryCode: "+categoryCode);
            System.out.println("<<<<total: "+total+"  firstQuarter: "+firstQuarter+"  secondQuarter: "+secondQuarter+"  thirdQuarter: "+thirdQuarter+"  fourthQuarter: "+fourthQuarter);
//            budgetexist = approve.getCodeName("select count(*) from AM_ACQUISITION_BUDGET_TMP where BRANCH_ID = '"+branchCode+"' AND CATEGORY_CODE = '"+categoryCode+"' AND SBU_CODE = '"+sbuCode+"'  AND STATUS IS NULL ");    
            budgetexist = approve.getCodeName("select count(*) from AM_FUEL_BUDGET_TMP where BRANCH_ID = '"+branchCode+"' AND CATEGORY_CODE = '"+categoryCode+"' AND STATUS IS NULL ");    

            System.out.println("<<<<budgetexist: "+budgetexist);

        } catch (Exception ex) {
            status = false;
        }

        if (status) {

            if (branchCode.equals(empty)) {
                //Skip
            } else {
                addCellContent(sNo,branchCode, branchName, categoryName, categoryCode,sbuCode, total,
                               firstQuarter, secondQuarter, thirdQuarter,
                               fourthQuarter, residue,groupIdent);
            }
        }

    }

    private void addCellContent(String sNo,String branchCode,String branchName,String categoryName,
                                String categoryCode,String sbuCode, String total,
                                String firstQuarter, String secondQuarter,
                                String thirdQuarter, String fourthQuarter,
                                String residue,String groupIdent) {
        String id = "";
        double firstQauterAmount = Double.parseDouble(firstQuarter);
        double secondQuaterAmount = Double.parseDouble(secondQuarter);
        System.out.println("<<<<firstQauterAmount in addCellContent: "+firstQauterAmount+"   secondQuaterAmount: "+secondQuaterAmount);
        double thirdQauterAmount = Double.parseDouble(thirdQuarter);
        double fourthQuaterAmount = Double.parseDouble(fourthQuarter);
        System.out.println("<<<<thirdQauterAmount in addCellContent: "+thirdQauterAmount+"   fourthQuaterAmount: "+fourthQuaterAmount);
        double totalAmount = Double.parseDouble(total);
        System.out.println("<<<<totalAmount in addCellContent: "+totalAmount);
        double balanceAmount = Double.parseDouble(residue);
        String type = "P";
        
        BranchAllocation ba = new BranchAllocation(id, branchCode,categoryCode,sbuCode, branchName,categoryName,
                firstQauterAmount, secondQuaterAmount, thirdQauterAmount,
                fourthQuaterAmount, totalAmount, balanceAmount, type);
        ba.setGroupId(groupIdent);
        this.budgetList.add(ba);
        System.out.println("<<<<budgetexist in addCellContent: "+budgetexist);
        if(budgetexist.equals("0")){
	        allocate.allocateFuelBudget(branchCode, branchName,
	        categoryName,categoryCode,sbuCode, firstQauterAmount,secondQuaterAmount,
	        thirdQauterAmount,fourthQuaterAmount,totalAmount,balanceAmount, type, groupIdent);
        }
        if(budgetexist!="0"){
        		allocate.extralFuelBudgetary(branchCode, branchName,categoryName,
        		categoryCode,sbuCode,firstQauterAmount,secondQuaterAmount,thirdQauterAmount,fourthQuaterAmount);
        }
    }

    public static void main(String[] args) {
        ExcelUploadManager excelManager = new ExcelUploadManager(groupId);
        FileFormat fm = new FileFormat();
        String file = "d:\\BookBudget.xls";
        excelManager.acceptExcel(file);

        System.out.println("The content of file[" + file + "] is :\n");
        System.out.println("======\t\t============================\t\t=======");
        ArrayList list = excelManager.getBudgetList();

        for (int x = 0; x < list.size(); x++) {
            BranchAllocation ba = (BranchAllocation) list.get(x);
            System.out.println(ba.getBranchCode() + "\t|\t" +
                               fm.formatText(25, ba.getBranchName()) + "\t|" +
                               fm.formatTextRightJustified(18,
                    Double.toString(ba.getTotalAmount())));
        }
    }
}
