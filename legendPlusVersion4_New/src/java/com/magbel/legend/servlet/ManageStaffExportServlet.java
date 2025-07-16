package com.magbel.legend.servlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import static java.lang.System.out;

public class ManageStaffExportServlet extends HttpServlet {
  
	
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    			doGet(request,response);
    }
    /* (non-Javadoc)
     * @see jakarta.servlet.http.HttpServlet#doGet(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	  FileInputStream stream;
          File file ;
          HSSFWorkbook workbook;
          FormulaEvaluator formulaEvaluator;
          OutputStream outputStream ;
          String home="";
          String home2 ="";
          try{
//              home = System.getProperty("user.home");
//              out.println("Your home name is : " +home);
              home = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
              int test = home.length()-4;
              String location = home.substring(0,test);
              String newLocation = location+"\\legendPlus\\sample\\";
             String excelFilePath = "Sample Staff Creation Upload";
//              out.println("Your excel file name is : " +excelFilePath);
              file = new File(newLocation+excelFilePath+".xls");
              out.println("The file path + file name is : " +file);
              stream = new FileInputStream(file);
              //creating workbook instance that refers to .xlsx file
              workbook = new HSSFWorkbook(stream);
              //creating a sheet object to retrieve the object
              HSSFSheet sheet = workbook.getSheetAt(0);
              //evaluating cell type
              formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
              for(Row row: sheet)     //iteration over row using for each loop
              {
                  for(Cell cell: row)    //iteration over cell using for each loop
                  {
                      switch(formulaEvaluator.evaluateInCell(cell).getCellType())
                      {
                          case NUMERIC:   //field that represents numeric cell type
                              //getting the value of the cell as a number
                              //System.out.print(cell.getNumericCellValue()+ "\t\t");
                              break;
                          case STRING:    //field that represents string cell type
                              // getting the value of the cell as a string
                             // System.out.print(cell.getStringCellValue()+ "\t\t");
                              break;
                      }
                  }
                  System.out.println();

              }
              home2 = System.getProperty("user.home");
              //out.println("Your home 2 folder is : " + home2);
              String path = home2 +"/Downloads/"+"SBU UPLOAD FOR NEW UPGRADE.xls";
              //System.out.println("Your path is : " +path);
              File file1 = new File(path);
              //out.println("The file is : " +file1);
              response.setContentType("application/vnd.ms-excel");
              response.setHeader("pragma","public");
              response.setHeader("Content-Disposition", String.format("attachment; filename=%s",file.getName()));
              response.setHeader("Content-Length", String.valueOf(file));
              outputStream = response.getOutputStream();
              workbook.write(outputStream);
              outputStream.close();
              //out.println("Export File is saved!!!");
             // response.sendRedirect("registrationView.jsp");

          }catch (Exception e){
              e.getCause();
          }

    }
}
