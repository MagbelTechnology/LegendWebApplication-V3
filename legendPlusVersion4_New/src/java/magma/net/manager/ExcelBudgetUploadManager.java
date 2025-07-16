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
import magma.net.manager.ExcelBudgetManager;;

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
public class ExcelBudgetUploadManager extends MagmaDBConnection {
    private ArrayList budgetList;
    public ApprovalRecords approve;
    public ExcelBudgetManager allocate;
    String budgetexist = "";
    static String groupId = "";
    String groupIdent = "";
    public ExcelBudgetUploadManager(String groupId) {
    	//System.out.println("<<<<<<<<<<<<<<groupId in ExcelUploadManager: "+groupId);
    	groupIdent = groupId;
    	approve = new ApprovalRecords(); 
    	allocate = new ExcelBudgetManager(); 
    	
        //System.out.println("INFO:Excel ExcelUploadManager instatiated.");
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
        //System.out.println("TOTAL SHEETS FOUND IS:" + sheets.length + " sheets");
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
//System.out.println("<<<<<<<<<<groupIdent in  insertCellContents >>>>>>>: "+groupIdent);
        int size = cell.length;
        String sNo = "";
        String empty = "";
        String branchName = "";
        String branchCode = "";
        String categoryCode = "";
        String subcategoryCode = "";
        String sbuCode = "";
        String total = "";
        String jan = "";
        String feb = "";
        String mar = "";
        String apr = "";
        String may = "";
        String jun = "";
        String jul = "";
        String aug = "";
        String sep = "";
        String oct = "";
        String nov = "";
        String dec = "";
        String firstQuarter = "";
        String secondQuarter = "";
        String thirdQuarter = "";
        String fourthQuarter = "";
        String residue = "";
        String categoryName = "";
        String subcategoryName = "";
        String extrabudget = "";
        boolean status = true;

        try {
        	sNo = cell[0].getContents();
        	//System.out.println("<<<<<<<<<< sNo >>>>>>>: "+sNo+"    groupIdent: "+groupIdent);
            branchCode = cell[1].getContents();
            categoryCode = cell[2].getContents();
            subcategoryCode = cell[3].getContents();
            sbuCode = cell[4].getContents();
            total = cell[5].getContents();
            jan = cell[6].getContents();
            feb = cell[7].getContents();
            mar = cell[8].getContents();
            apr = cell[9].getContents();
            may = cell[10].getContents();
            jun = cell[11].getContents();
            jul = cell[12].getContents();
            aug = cell[13].getContents();
            sep = cell[14].getContents();
            oct = cell[15].getContents();
            nov = cell[16].getContents();
            dec = cell[17].getContents();
            extrabudget = cell[18].getContents();
     //       residue = cell[5].getContents();
/*            firstQuarter = cell[5].getContents();
            secondQuarter = cell[6].getContents();
            thirdQuarter = cell[7].getContents();
            fourthQuarter = cell[8].getContents();*/
            branchName = approve.getCodeName("select BRANCH_NAME from am_ad_branch where BRANCH_CODE = '"+branchCode+"' AND BRANCH_STATUS = 'ACTIVE'");    
            categoryName = approve.getCodeName("select CATEGORY_NAME FROM AM_AD_CATEGORY WHERE CATEGORY_CODE = '"+categoryCode+"' AND CATEGORY_STATUS = 'ACTIVE'"); 
            subcategoryName = approve.getCodeName("select SUB_CATEGORY_NAME FROM AM_AD_SUB_CATEGORY WHERE SUB_CATEGORY_CODE = '"+subcategoryCode+"' AND SUB_CATEGORY_STATUS = 'ACTIVE'");
 //           System.out.println("<<<<sNo: "+sNo+"  branchCode: "+branchCode+"  branchName: "+branchName+"  categoryCode: "+categoryCode+"  sbuCode: "+sbuCode);
 //           System.out.println("<<<<total: "+total+"  firstQuarter: "+firstQuarter+"  secondQuarter: "+secondQuarter+"  thirdQuarter: "+thirdQuarter+"  fourthQuarter: "+fourthQuarter);
            budgetexist = approve.getCodeName("select count(*) from AM_ACQUISITION_BUDGET_TMP where BRANCH_ID = '"+branchCode+"' AND CATEGORY_CODE = '"+categoryCode+"' AND SBU_CODE = '"+sbuCode+"'  AND STATUS IS NULL ");    
            //System.out.println("<<<<budgetexist: "+budgetexist);
 /*           if (size < 11) {
                residue = "0.00";
            } else {
                residue = cell[10].getContents();
            }
*/
        } catch (Exception ex) {
            status = false;
        }

        if (status) {

            if (branchCode.equals(empty)) {
                //Skip
            } else {
                addCellContent(sNo,branchCode, branchName, categoryName, subcategoryName, categoryCode, subcategoryCode, sbuCode, total,
                			   jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec,
                               firstQuarter, secondQuarter, thirdQuarter,
                               fourthQuarter, residue,groupIdent,extrabudget);
            }
        }

    }

    private void addCellContent(String sNo,String branchCode,String branchName,String categoryName,String subcategoryName,
                                String categoryCode,String subcategoryCode, String sbuCode, String total,
                                String jan, String feb, String mar, String apr, String may, String jun, String jul,
                                String aug, String sep, String oct, String nov, String dec,
                                String firstQuarter, String secondQuarter,
                                String thirdQuarter, String fourthQuarter,
                                String residue,String groupIdent,String extrabudget) {
        String id = "";
        double janAmount = Double.parseDouble(jan);
        double febAmount = Double.parseDouble(feb);
        double marAmount = Double.parseDouble(mar);
        double aprAmount = Double.parseDouble(apr);
        double mayAmount = Double.parseDouble(may);
        double junAmount = Double.parseDouble(jun);
        double julAmount = Double.parseDouble(jul);
        double augAmount = Double.parseDouble(aug);
        double sepAmount = Double.parseDouble(sep);
        double octAmount = Double.parseDouble(oct);
        double novAmount = Double.parseDouble(nov);
        double decAmount = Double.parseDouble(dec);
        double firstQauterAmount = janAmount+febAmount+marAmount;
        double secondQuaterAmount = aprAmount+mayAmount+junAmount;
        double thirdQauterAmount = julAmount+augAmount+sepAmount;
        double fourthQuaterAmount = octAmount+novAmount+decAmount;
        double totalSum = firstQauterAmount+secondQuaterAmount+thirdQauterAmount+fourthQuaterAmount;
//        double totalAmount = Double.parseDouble(total);
//        double balanceAmount = Double.parseDouble(residue);        
        double totalAmount = totalSum;
        double balanceAmount = totalSum;
        String type = "P";
        
        BranchAllocation ba = new BranchAllocation(id, branchCode,categoryCode,subcategoryCode, sbuCode, branchName,categoryName,subcategoryName,
        		janAmount,febAmount,marAmount,aprAmount,mayAmount,junAmount,julAmount,augAmount,sepAmount,
      	        octAmount,novAmount,decAmount,
                firstQauterAmount, secondQuaterAmount, thirdQauterAmount,
                fourthQuaterAmount, totalAmount, balanceAmount, type,extrabudget);
        ba.setGroupId(groupIdent);
        this.budgetList.add(ba);
 //       System.out.println("<<<<budgetexist in addCellContent: "+budgetexist);
        if(extrabudget.equals("N")){
	        allocate.allocateBudget(branchCode, branchName,
	        categoryName,subcategoryName,sbuCode,categoryCode,subcategoryCode, 
	        janAmount,febAmount,marAmount,aprAmount,mayAmount,junAmount,julAmount,augAmount,sepAmount,
	        octAmount,novAmount,decAmount,
	        firstQauterAmount,secondQuaterAmount,
	        thirdQauterAmount,fourthQuaterAmount,totalAmount,balanceAmount, type, groupIdent,extrabudget);
        }
        
        if(extrabudget.equals("Y")){
	        allocate.allocateBudget(branchCode, branchName,
	        categoryName,subcategoryName,sbuCode,categoryCode,subcategoryCode, 
	        janAmount,febAmount,marAmount,aprAmount,mayAmount,junAmount,julAmount,augAmount,sepAmount,
	        octAmount,novAmount,decAmount,
	        firstQauterAmount,secondQuaterAmount,
	        thirdQauterAmount,fourthQuaterAmount,totalAmount,balanceAmount, type, groupIdent,extrabudget);

/*        		allocate.extralBudgetary(branchCode, branchName,categoryName,subcategoryName,sbuCode,
        		categoryCode,subcategoryCode,
    	        janAmount,febAmount,marAmount,aprAmount,mayAmount,junAmount,julAmount,augAmount,sepAmount,
    	        octAmount,novAmount,decAmount,
        		firstQauterAmount,secondQuaterAmount,thirdQauterAmount,fourthQuaterAmount); */
        }
    }

    public static void main(String[] args) {
        ExcelBudgetUploadManager excelManager = new ExcelBudgetUploadManager(groupId);
        FileFormat fm = new FileFormat();
        String file = "d:\\BookBudget.xls";
        excelManager.acceptExcel(file);

        //System.out.println("The content of file[" + file + "] is :\n");
        //System.out.println("======\t\t============================\t\t=======");
        ArrayList list = excelManager.getBudgetList();

        for (int x = 0; x < list.size(); x++) {
            BranchAllocation ba = (BranchAllocation) list.get(x);
//            System.out.println(ba.getBranchCode() + "\t|\t" +
//                               fm.formatText(25, ba.getBranchName()) + "\t|" +
//                               fm.formatTextRightJustified(18,
//                    Double.toString(ba.getTotalAmount())));
        }
    }
}
