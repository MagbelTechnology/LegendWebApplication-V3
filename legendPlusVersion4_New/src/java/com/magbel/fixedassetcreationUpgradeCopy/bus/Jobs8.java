package com.magbel.fixedassetcreationUpgrade.bus;
 
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
            String integrifyId = ems.getIntegrifyId();
            String status = ems.getStatus();
            String description = ems.getDescription();
            int assetCode = 0;
            String result = comp.IsIntegrifyIdExist(integrifyId);
            System.out.println((new StringBuilder("<<<<<<<<<<<<result: ")).append(result).append("   integrifyId: ").append(integrifyId).append("   histId: ").append(histId).append("  itemType: ").append(itemType).toString());
            if(result == "")
            { 
                comp.setPendingTrans(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", assetCode);
                String lastMTID = comp.getCurrentMtid("am_asset_approval");
                System.out.println((new StringBuilder("<<<<<<<<<<<<lastMTID: ")).append(lastMTID).toString());
                comp.setPendingTransArchive(comp.setApprovalDirectData(histId, histId, tranType, superId, userId, String.valueOf(costPrice), branchId,description), "66", Integer.parseInt(lastMTID), assetCode);
                System.out.println("<<<<<<<<<<<<>>>>>>: ");
                comp.updateFTPostingRecord(integrifyId, "WFP");
                comp.createIntegrify(histId, integrifyId, tranType);
            } else
            {
                System.out.println("<<<<<<<<<<<<=About to Update=====>>>>>>: ");
                comp.updateFTErrorMessageRecord(integrifyId, "Already Posted");
            }
        }
}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
}

