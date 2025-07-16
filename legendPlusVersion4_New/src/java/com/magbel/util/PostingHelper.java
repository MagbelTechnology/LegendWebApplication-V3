package com.magbel.util;

import magma.GroupAssetToAssetBean;
import com.magbel.legend.bus.ApprovalRecords;

public class PostingHelper
{
	private GroupAssetToAssetBean adGroup;
	private ApprovalRecords approv;

	public PostingHelper()
	{
		try 
		{
			adGroup = new GroupAssetToAssetBean();
			approv = new ApprovalRecords();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void postAssetStatus(String asset_id)
	{
		String upd_Group_Asset_Status="update am_group_asset set post_flag='D',Asset_status='ACTIVE' where asset_id='" + asset_id+"'";
    	System.out.println("upd_Group_Asset_Status Query >>>> " + upd_Group_Asset_Status);
    	adGroup.updateStatusUtil(upd_Group_Asset_Status);
        
    	//stop update on am_asset continued
    	//String upd_Asset_Status="update am_asset set Asset_status='ACTIVE' where asset_id='" + asset_id+"'";
    	String upd_Asset_Status="update am_asset set Asset_status='ACTIVE',finacle_posted_date=getdate() where asset_id='" + asset_id+"'";
        System.out.println("upd_Asset_Status Query >>>> " + upd_Asset_Status);
    	adGroup.updateStatusUtil(upd_Asset_Status);
    	
    	//SEND MAIL FOR ASSET HERE
    	//stamp finnacle posted date for the individual asset
	}

	public void postStatus(String grp_id)
	{
		String chk_asset_count="select count(*) from am_group_asset where group_id=" + grp_id;
		System.out.println("chk_asset_count Query >>>> " + chk_asset_count);
    	String chk_asset_processed_count="select count(*) from am_group_asset where post_flag='D' and group_id="+ grp_id;
    	System.out.println("chk_asset_processed_count Query >>>> " + chk_asset_processed_count);
    	String chk_asset_status_count="select count(*) from am_group_asset where post_flag <>'P' and group_id="+ grp_id;
    	System.out.println("chk_asset_status_count Query >>>> " + chk_asset_status_count);
    	
    	int asset_cnt=Integer.parseInt(approv.getCodeName(chk_asset_count));
    	System.out.println("asset_cnt Result >>>> " + asset_cnt);
    	int asset_proc_cnt= Integer.parseInt(approv.getCodeName(chk_asset_processed_count));
    	System.out.println("asset_proc_cnt Result >>>> " + asset_proc_cnt);
    	int asset_status_cnt= Integer.parseInt(approv.getCodeName(chk_asset_status_count));
    	System.out.println("asset_status_cnt Result >>>> " + asset_status_cnt);
    	
    	if (asset_status_cnt == asset_cnt)// None of the asset has  status =PENDING	
    	{
    	   	if (asset_cnt == asset_proc_cnt)//all the assets were posted
	    	{
	    		String upd_raise_entry_flag="update am_raisentry_post set entrypostflag='Y',GroupIdStatus='Y' where id="+grp_id+" " +
	    				" and page='" + "ASSET GROUP CREATION RAISE ENTRY".trim()+"'";
	    		System.out.println("upd_raise_entry_flag Query >>>> " + upd_raise_entry_flag);
	    		adGroup.updateStatusUtil(upd_raise_entry_flag);
	    		
	    		String upd_Group_Asset_main_Status="update am_group_asset_main set Asset_status='ACTIVE' where group_id='" + grp_id+"'";
	        	System.out.println("upd_Group_Asset_Status Query >>>> " + upd_Group_Asset_main_Status);
	        	adGroup.updateStatusUtil(upd_Group_Asset_main_Status);
	        	
	        	
	    	}
    	   	else
    	   	{
    	   		//do a count of the assets that have a status of r
    	   		String chk_asset_rejected_count="select count(*) from am_group_asset where post_flag='R' and group_id="+ grp_id;
    	   		System.out.println("chk_asset_rejected_count >>>>> " + chk_asset_rejected_count);
    	   		
    	   		int asset_rejected_cnt= Integer.parseInt(approv.getCodeName(chk_asset_rejected_count));
    	   		System.out.println("asset_rejected_cnt Result >>>>> " + asset_rejected_cnt);
    	   		    	   		    	   		
    	   		String rej_am_grp_asset_main_status = 
    	        	"update am_group_asset_main set Asset_Status='Partial Group Rejection',pend_GrpAssets='" +asset_rejected_cnt +"' "
    	        	+ "where group_id = '"+grp_id+"'";
    	   		System.out.println("rej_am_grp_asset_main_status >>>> " + rej_am_grp_asset_main_status);
    	   		
    	   		String 	rej_am_asset_approval_status = "update am_asset_approval set approval_level_count = 0, " +
   				"Asset_Status='Partial Group Rejection',process_status='F' where asset_id = '"+grp_id+"'";
    	   		System.out.println("rej_am_asset_approval_status_ >>>> " + rej_am_asset_approval_status);
    	   		
    	   		
    	   		
    	   		adGroup.updateStatusUtil(rej_am_grp_asset_main_status);
    	   		adGroup.updateStatusUtil(rej_am_asset_approval_status);
    	   	}
    	   	//let's know the asset_id of d ones dt have been rejected
    	   	//CALL A GENERIC METHOD TO BE HANDLED BY REJECT
    	   	
    	}
    	
    	/*String chk_asset_count="select count(*) from am_group_asset where group_id=" + group_id;
		System.out.println("chk_asset_count Query >>>> " + chk_asset_count);
    	String chk_asset_processed_count="select count(*) from am_group_asset where post_flag='D' and group_id="+ group_id;
    	System.out.println("chk_asset_processed_count Query >>>> " + chk_asset_processed_count);
    	
    	int asset_cnt=Integer.parseInt(approv.getCodeName(chk_asset_count));
    	System.out.println("asset_cnt Result >>>> " + asset_cnt);
    	int asset_proc_cnt= Integer.parseInt(approv.getCodeName(chk_asset_processed_count));
    	System.out.println("asset_proc_cnt Result >>>> " + asset_proc_cnt);
    	if (asset_cnt == asset_proc_cnt)
    	{
    		String upd_raise_entry_flag="update am_raisentry_post set entrypostflag='Y' where id="+group_id+" " +
    				"and page='" + "ASSET GROUP CREATION RAISE ENTRY".trim()+"'";
    		System.out.println("upd_raise_entry_flag Query >>>> " + upd_raise_entry_flag);
    		adGroup.updateStatusUtil(upd_raise_entry_flag);
    	}*/
	}
}
