package com.magbel.fixedassetcreationLegendPlusFIXFAC.bus;
 
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
 
public class Jobs7
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs7.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs7()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {            
	  ArrayList malilist = comp.getMaintenanceEnvironmentSqlRecords();
        String tranType = "Environmental Social Management";
        System.out.println((new StringBuilder("<<<<<<<< Environmental Social Management >> ")).append(malilist.size()).toString());
        for(int i = 0; i < malilist.size(); i++)
        {
            FleetManatainanceRecord ems = (FleetManatainanceRecord)malilist.get(i);
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
            int assetCode = 0;
            String description = ems.getDescription();
            String page1 = "ESMS CREATION RAISE ENTRY";
			String flag= "";
			String partPay="";	
            String approvedbyName =comp.findObject(" SELECT full_name from am_gb_user where user_id="+Integer.parseInt(superId)+"");
            String branchName= comp.findObject(" SELECT branch_name from am_ad_branch where branch_Id='"+branchId+"'");
            String subjectT= "N";
            String whT= "N";
            if(vatAmt>0){subjectT= "Y";}
			if(whtAmt>0){whT= "Y";}
			String url = "DocumentHelp.jsp?np=maintenanceEnvRecordDetails&amp;id="+histId+"&pageDirect=Y";
            String result = comp.IsIntegrifyIdExist(integrifyId);
            System.out.println((new StringBuilder("<<<<<<<<<<<<result in job7: ")).append(result).append("   integrifyId: ").append(integrifyId).append("   histId: ").append(histId).append("  itemType: ").append(itemType).toString());
            if(result == "")
            {
                comp.setPendingTrans(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", assetCode);
                String lastMTID = comp.getCurrentMtid("am_asset_approval");
                System.out.println((new StringBuilder("<<<<<<<<<<<<lastMTID in Job7: ")).append(lastMTID).toString());
                comp.setPendingTransArchive(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", Integer.parseInt(lastMTID), assetCode);
                System.out.println("<<<<<<<<<<<<>>>>>>: ");
                comp.updateESMSPostingRecord(integrifyId, "WFP", "Waiting For Posting");
                comp.createIntegrify(histId, integrifyId, tranType);
                comp.insertApprovalx(histId, description, page1, flag, partPay,approvedbyName,branchName,subjectT,whT,url,lastMTID,assetCode);
            } else
            {
                System.out.println("<<<<<<<<<<<<=About to Update=====>>>>>>: ");
                comp.updateESMSErrorMessageRecord(integrifyId, "Already Posted");
            }
        }
}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

