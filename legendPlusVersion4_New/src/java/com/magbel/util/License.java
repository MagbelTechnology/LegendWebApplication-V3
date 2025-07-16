package com.magbel.util;

import java.io.File;  
import java.io.IOException;  
import java.util.List;
import org.apache.commons.io.FileUtils; 


public class License 
{ 
	public boolean modifyLicense(String licCode,String authorCode,String file)
	{  
		boolean result=true;
		String xx[]={}; 
		try{  
			  File in =  new File(file);             
			  List<String> contents = FileUtils.readLines(in);   
				for(int x=0;x<contents.size();x++)
				 {
					String test=(String)contents.get(x);
					xx = test.split("=");
					if(xx[0].equals("lic-code"))contents.set(x, xx[0]+"="+licCode);
					if(xx[0].equals("author.code"))contents.set(x, xx[0]+"="+authorCode);
				 } 
			  FileUtils.writeLines(in, contents);  
			 }  
		catch (IOException e) 
			          
			  { 
			  result=false;   
			  e.printStackTrace(); 
			   System.out.println("File cannot be located");       
			  }   
		 return result;
	 }  
 
}
