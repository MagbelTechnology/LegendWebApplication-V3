package com.magbel.legend.servlet;
import static java.lang.System.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
   
public class SampleUploadAssetVerifyExportServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    			doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	   File file ;
           OutputStream outputStream ;
           FormulaEvaluator formulaEvaluator;
           HSSFWorkbook workbook;
           String home2 ="";
           String home="";
           String excelFilePath;
           FileInputStream stream;
           try{
        	   home = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
               int test = home.length()-4;
               String location = home.substring(0,test);
               String newLocation = location+"\\legendPlus\\sample\\";
               excelFilePath = "ASSET VERIFICATION SAMPLE";
               file = new File(newLocation + excelFilePath + ".xls");
               out.println("The file path + file name is : " +file);
               stream = new FileInputStream(file);
               workbook = new HSSFWorkbook(stream);
               //creating a sheet object to retrieve the object
               HSSFSheet sheet = workbook.getSheetAt(0);
               //evaluating cell type
               formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
               home2 = System.getProperty("user.home");
               //out.println("Your home 2 folder is : " + home2);
               String path = home2 +"/Downloads/"+"ASSET VERIFICATION SAMPLE.xls";
               //System.out.println("Your path is : " +path);
               File file1 = new File(path);
               //out.println("The file is : " +file1);
               response.setContentType("application/vnd.ms-excel");
               response.setHeader("pragma","public");
               response.setHeader("Content-Disposition", String.format("attachment; filename=%s",file1.getName()));
               response.setHeader("Content-Length", String.valueOf(file1));
               outputStream = response.getOutputStream();
               workbook.write(outputStream);
               outputStream.close();
               //out.println("Export File is saved!!!");
               //response.sendRedirect("registrationView.jsp");

           }catch (Exception e){
               e.getCause();
           }

    }
   
}