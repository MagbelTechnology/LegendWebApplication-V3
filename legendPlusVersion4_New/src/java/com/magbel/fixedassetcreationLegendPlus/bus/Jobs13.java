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
 
public class Jobs13
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs8.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs13()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {            
	  ArrayList list = comp.getStockSqlRecords();
        String tranType = "Stock Creation Management";
        System.out.println("<<<<<<<< New Stock Creation >> "+list.size());
        for(int i = 0; i < list.size(); i++)
        {
            FleetManatainanceRecord ems = (FleetManatainanceRecord)list.get(i);
            String id = ems.getId();
            String histId = ems.getHistId();
            String branchId = ems.getBranchId();
            String itemType = ems.getType();
            String userId = ems.getUserId(); 
            String superId = ems.getSuperId();
            double costPrice = ems.getCost();
            double vatAmt = ems.getVatAmt(); 
            double whtAmt = ems.getWhtAmt(); 
            String integrifyId = ems.getIntegrifyId();
            String status = ems.getStatus();
            String description = ems.getDescription();
            int assetCode = 0;
            String page1 = "STORE CREATION RAISE ENTRY";
			String flag= "";
			String partPay="";	
            String approvedbyName =comp.findObject(" SELECT full_name from am_gb_user where user_id="+Integer.parseInt(superId)+"");
            String branchName= comp.findObject(" SELECT branch_name from am_ad_branch where branch_Id='"+branchId+"'");
            String subjectT= "N";
            String whT= "N";
            if(vatAmt>0){subjectT= "Y";}
			if(whtAmt>0){whT= "Y";}
			String url = "DocumentHelp.jsp?np=maintenanceRepsRecordDetails&amp;id="+histId+"&pageDirect=Y";
            String result = comp.IsIntegrifyIdExist(integrifyId);
//            System.out.println((new StringBuilder("<<<<<<<<<<<<result: ")).append(result).append("   integrifyId: ").append(integrifyId).append("   histId: ").append(histId).append("  itemType: ").append(itemType).toString());
            if(result == "")
            { 
                comp.setPendingTrans(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "72", assetCode);
                String lastMTID = comp.getCurrentMtid("am_asset_approval");
//                System.out.println((new StringBuilder("<<<<<<<<<<<<lastMTID: ")).append(lastMTID).toString());
                comp.setPendingTransArchive(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "72", Integer.parseInt(lastMTID), assetCode);
 //               System.out.println("<<<<<<<<<<<<>>>>>>: ");
                comp.updateStockPostingRecord(integrifyId, "WFP");
                comp.createIntegrify(histId, integrifyId, tranType);
                comp.insertApprovalx(histId, description, page1, flag, partPay,approvedbyName,branchName,subjectT,whT,url,lastMTID,assetCode);
            } else
            {
//                System.out.println("<<<<<<<<<<<<=About to Update=====>>>>>>: ");
                comp.updateStockErrorMessageRecord(integrifyId, "Already Posted");
            }
        }
        
//        comp.insertFTRecordsFrom_Am_Asset();
}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

