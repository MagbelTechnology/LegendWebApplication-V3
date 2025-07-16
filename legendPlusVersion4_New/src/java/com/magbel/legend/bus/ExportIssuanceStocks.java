/* 
 * Copyright 2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package com.magbel.legend.bus;

/**
 * <p>
 * This is just a simple job that says "Hello" to the world.
 * </p>
 * 
 * @author Bill Kratzer
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import magma.net.dao.MagmaDBConnection;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class ExportIssuanceStocks extends MagmaDBConnection
{
	public ExportIssuanceStocks() throws SQLException{
		new ExportIssuanceStocks().export();
		new MagmaDBConnection();
	}
	
	 public void export() throws SQLException {
		 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select User_id,User_Name, Full_Name,Class,Branch from am_gb_User";
         con = getConnection("legendPlus");
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         
	        String excelFilePath = "E:\\Export Files\\Reviews-export.xls";
            
	        try{
	            HSSFWorkbook  workbook = new HSSFWorkbook();
	            HSSFSheet sheet = workbook.createSheet("Reviews");
	 
	            writeHeaderLine(sheet);
	 
	            writeDataLines(rs, workbook, sheet);
	 
	            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
	            workbook.write(outputStream);
	            workbook.close();
	 
	            ps.close();
	            
	            System.out.println("Generated File Successfully!!");
	 
	        } catch (SQLException e) {
	            System.out.println("Datababse error:");
	            e.printStackTrace();
	        } catch (IOException e) {
	            System.out.println("File IO error:");
	            e.printStackTrace();
	        }
	    }
	 
	    private void writeHeaderLine(HSSFSheet sheet) {
	 
	        Row headerRow = sheet.createRow(0);
	 
	        Cell headerCell = headerRow.createCell(0);
	        headerCell.setCellValue("User_id");
	 
	        headerCell = headerRow.createCell(1);
	        headerCell.setCellValue("User_Name");
	 
	        headerCell = headerRow.createCell(2);
	        headerCell.setCellValue("Full_Name");
	 
	        headerCell = headerRow.createCell(3);
	        headerCell.setCellValue("Class");
	 
	        headerCell = headerRow.createCell(4);
	        headerCell.setCellValue("Branch");
	    }

	    
	    private void writeDataLines(ResultSet result, HSSFWorkbook workbook,
	    		HSSFSheet sheet) throws SQLException {
	        int rowCount = 1;
	 
	        while (result.next()) {
	            String User_id = result.getString("User_id");
	            String User_Name = result.getString("User_Name");
	            String Full_Name = result.getString("Full_Name");
	            String Class = result.getString("Class");
	            String Branch = result.getString("Branch");
	 
	            Row row = sheet.createRow(rowCount++);
	 
	            int columnCount = 0;
	            Cell cell = row.createCell(columnCount++);
	            cell.setCellValue(User_id);
	 
	            cell = row.createCell(columnCount++);
	            cell.setCellValue(User_Name);
	 
	            cell = row.createCell(columnCount++);
	            cell.setCellValue(Full_Name);
	 
	            cell = row.createCell(columnCount++);
	            cell.setCellValue(Class);
	 
	            cell = row.createCell(columnCount);
	            cell.setCellValue(Branch);
	 
	        }
	    }
}
