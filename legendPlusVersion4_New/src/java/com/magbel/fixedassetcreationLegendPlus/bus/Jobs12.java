package com.magbel.fixedassetcreationLegendPlus.bus;
 
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import legend.admin.handlers.CompanyHandler;
 
import magma.net.vao.FleetManatainanceRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*; 

import com.magbel.legend.vao.newAssetTransaction;
 
public class Jobs12
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs12.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs12()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {            
	  ArrayList malilist = comp.getExpensesSqlRecords();
        String tranType = "Project Expenses";
        System.out.println((new StringBuilder("<<<<<<<< Project Expenses >> ")).append(malilist.size()).toString());
        for(int i = 0; i < malilist.size(); i++)
        {
//        	System.out.println("Loop Index:  "+i);
            FleetManatainanceRecord ems = (FleetManatainanceRecord)malilist.get(i);
            String id = ems.getId();
            String histId = ems.getHistId();
            String branchId = ems.getBranchId();
            String itemType = ems.getType();
            String userId = ems.getUserId();
            String superId = ems.getSuperId();
            double VatableCost = ems.getCost();
            double vatRate = ems.getVatRate(); 
            double whtRate = ems.getWhtRate(); 
//            System.out.println("vatRate:  "+vatRate+"   whtRate: "+whtRate);
            String integrifyId = ems.getIntegrifyId();
            String status = ems.getStatus();
            String subjectTVat= ems.getSubjectTOVat();
            String whT= ems.getSubjectToWhTax();
            int assetCode = 0;
            String description = ems.getDescription();
            String page1 = "EXPENSES CREATION RAISE ENTRY";
			String flag= "";
			String partPay="";	
			 double vatrate = 0.0;
			 double whtrate = 0.0;
			 double costPrice = 0.00;
			 double whtaxamount = 0.00;
//			 System.out.println("superId:  "+superId+"     branchId: "+branchId);
//			 String test = " SELECT full_name from am_gb_user where user_id="+Integer.parseInt(superId)+"";
//			 System.out.println("Test:  "+test);
            String approvedbyName =comp.findObject(" SELECT full_name from am_gb_user where user_id="+Integer.parseInt(superId)+"");
            String branchName= comp.findObject(" SELECT branch_name from am_ad_branch where branch_Id='"+branchId+"'");
//            if(vatAmt>0){subjectTVat= "Y";}
//			if(whtAmt>0){whT= "Y";}
//           System.out.println("subjectTVat:  "+subjectTVat+"     whT: "+whT);
			 if((!subjectTVat.equalsIgnoreCase(null)) && (!subjectTVat.equalsIgnoreCase("")) && (subjectTVat.equalsIgnoreCase("Y"))){				
				 vatrate =  vatRate/100;
				 costPrice = VatableCost*vatrate;
				 costPrice = VatableCost + (VatableCost*vatrate);
//				 System.out.println("costPrice:  "+costPrice);
			 }					 
//			 System.out.println("vatrate:  "+vatrate);
			 if((!whT.equalsIgnoreCase(null)) && (!whT.equalsIgnoreCase(""))  && (!whT.equalsIgnoreCase("N"))){
				 whtrate = whtRate/100;
				 whtaxamount =  VatableCost*(whtRate/100);
//				 System.out.println("whtaxamount:  "+whtaxamount);
			 }	
//			 System.out.println("whtrate:  "+whtrate);
			String url = "DocumentHelp.jsp?np=projectExpensesDetails&amp;id="+histId+"&pageDirect=Y";
            String result = comp.IsIntegrifyIdExist(integrifyId);
//            System.out.println((new StringBuilder("<<<<<<<<<<<<result in job12: ")).append(result).append("   integrifyId: ").append(integrifyId).append("   histId: ").append(histId).append("  itemType: ").append(itemType).toString());
            if(result == "")
            {
                comp.setPendingTrans(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", assetCode);
                String lastMTID = comp.getCurrentMtid("am_asset_approval");
//                System.out.println((new StringBuilder("<<<<<<<<<<<<lastMTID in Job12: ")).append(lastMTID).toString());
                comp.setPendingTransArchive(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", Integer.parseInt(lastMTID), assetCode);
//                System.out.println("<<<<<<<<<<<<>>>>>>: ");
                comp.updateExpensesPostingRecord(integrifyId, "WFP", "Waiting For Posting");
                comp.createIntegrify(histId, integrifyId, tranType);
                comp.insertApprovalx(histId, description, page1, flag, partPay,approvedbyName,branchName,subjectTVat,whT,url,lastMTID,assetCode);
            } else
            {
//                System.out.println("<<<<<<<<<<<<=About to Update=====>>>>>>: ");
                comp.updateExpensesMessageRecord(integrifyId, "Already Posted");
            }
        }
}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

