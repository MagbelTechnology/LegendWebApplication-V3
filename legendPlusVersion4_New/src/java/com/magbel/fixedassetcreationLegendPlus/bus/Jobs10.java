package com.magbel.fixedassetcreationLegendPlus.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;
 








import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
 
public class Jobs10
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs10.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs10()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        { 
	  
	     java.util.ArrayList vendortrans =comp.getVendorTransactionSqlRecords();
//	     System.out.println("-->size of Vendor Transactions in Capitalized>--> "+vendortrans.size());
	      //================================
	     for(int i=0;i<vendortrans.size();i++)
	     {
	     com.magbel.legend.vao.newAssetTransaction  trans = (com.magbel.legend.vao.newAssetTransaction)vendortrans.get(i);    	 
			String projectCode =  trans.getProjectCode();
			projectCode = projectCode+"#"+"000";
//			System.out.println("-->size of Vendor projectCode--> "+projectCode);
			String []projectCodelist = projectCode.split("#");
			int No = projectCodelist.length;
//			System.out.println("-->size of Vendor projectCodelist[0]--> "+projectCodelist[0]+"    projectCodelist[1]: "+projectCodelist[1]);
			if(!projectCodelist[0].equals("")){
			double amount = trans.getCostPrice();
			String qw = "UPDATE ST_GL_PROJECT SET PROJECT_BALANCE = COST - "+amount+", AMOUNT_UTILIZED = "+amount+" where CODE = '"+projectCodelist[0]+"'";
//			System.out.println("-->size of Vendor qw--> "+qw);
			comp.updateAssetStatusChange(qw);
			}
			if(!projectCodelist[1].equals("")){
			double amount = trans.getCostPrice();
			String qw2 = "UPDATE ST_PROJECT_SUB_CATEGORY SET SUBCATEGORY_BALANCE = SUBCATEGORYCOST - "+amount+", SUBCATEGORYAMTUTILIZED = "+amount+" where PROJECTCODE = '"+projectCodelist[0]+"' AND SUBCATEGORYCODE = '"+projectCodelist[1]+"' ";
//			System.out.println("-->size of Vendor qw2--> "+qw2);
			comp.updateAssetStatusChange(qw2);
			}			
	     }   
	     //======================================
     }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

