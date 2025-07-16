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
 
public class Jobs8
    implements Job
{  
        
    private static Log _log = LogFactory.getLog(Jobs8.class);
    CompanyHandler comp = new CompanyHandler();

    public Jobs8()
    {
      
    }

    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
  //  	int statux = 0;
//		int statuk = 0;
  try
        {            
	  ArrayList list = comp.getFleetSqlRecords();
	  String tempIntegrify = "";
	  String integrifyId = "";
        String tranType = "Fleet Management";
        System.out.println("<<<<<<<< Fleet Management >> "+list.size());
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
            integrifyId = ems.getIntegrifyId();
            String status = ems.getStatus();
            String description = ems.getDescription();
            String errorMessage = "";
            boolean donetemp = false;
//		      System.out.println("=====integrifyId: "+integrifyId+"   tempIntegrify: "+tempIntegrify);
		     if(integrifyId!=tempIntegrify){
		      errorMessage = "Process Ongoing; ";
		       donetemp = comp.updateFTErrorMessageRecord(integrifyId,errorMessage);
		     }
		      
            int assetCode = 0;
            String page1 = "FLEET CREATION RAISE ENTRY";
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
 //           System.out.println("<<<<<<<<<<<<result: "+result+"   integrifyId: "+integrifyId+"   histId: "+histId+"  itemType: "+itemType+"  ======donetemp===: "+donetemp+"    =====>tempIntegrify=====> "+tempIntegrify);
            if((result.equals("")) && (tempIntegrify.equals("")) )
            { 
                comp.setPendingTrans(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", assetCode);
                String lastMTID = comp.getCurrentMtid("am_asset_approval");
//                System.out.println((new StringBuilder("<<<<<<<<<<<<lastMTID: ")).append(lastMTID).toString());
                comp.setPendingTransArchive(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", Integer.parseInt(lastMTID), assetCode);
                comp.updateFTPostingRecord(integrifyId, "WFP");
                comp.createIntegrify(histId, integrifyId, tranType);
                comp.insertApprovalx(histId, description, page1, flag, partPay,approvedbyName,branchName,subjectT,whT,url,lastMTID,assetCode);
                comp.updateFTErrorMessageRecord(integrifyId, "Successfully Posted");
            } else
            {  
 //               System.out.println("<<<<<<<<<<<<=About to Update=====>>>>>>: ");
                comp.updateFTErrorMessageRecord(integrifyId, "Already Posted");
            }
        }
        
        comp.insertFTRecordsFrom_Am_Asset();
        tempIntegrify = integrifyId;
}
        catch(Exception e)
        {
            e.printStackTrace();
        }
 
    }
   
}

