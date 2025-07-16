/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magbel.admin.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.magbel.util.ApplicationHelper;

import com.magbel.admin.dao.MagmaDBConnection;

public class GenerateList
{

	MagmaDBConnection mgDbCon = null;
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	ApplicationHelper applHelper = null;

	public GenerateList()
	{
		mgDbCon = new MagmaDBConnection();


	}


	public void closeConnection(Connection con, Statement ps,ResultSet rs)
	{
		try
		{
			if (ps != null)
			{
				ps.close();
			}

			if (rs != null)
			{
				rs.close();
			}

			if (con != null)
			{
				con.close();
			}
		}
		catch (Exception ex)
		{
			System.out.println("WARNING:Error closing Connection ->" + ex);
		}
	}

	public int updateTable(String query)
    {
        Connection con = null;
        PreparedStatement ps = null;
        int result=0;
        try
        {
        	con = mgDbCon.getConnection("helpDesk");
            ps = con.prepareStatement(query);
            result=ps.executeUpdate();
        }
        catch (Exception ex)
        {
            System.out.println("WARNING:GenerateList: cannot update +" + ex.getMessage());
        }
        finally
        {
        	closeConnection(con, ps,rs);
        }
        return result;
    }

	 public String formatDate(String date)
	    {
	    	String dd=date.substring(8, 10);
	    	//System.out.println("dd>>>>" + dd);
			String mm=date.substring(4, 8);
			//System.out.println("mm>>>>" + mm);
			String yyyy=date.substring(0, 4);
			//System.out.println("yyyy>>>>" + yyyy);
			date =dd + mm+ yyyy;
			return date;
	    }



	 public String retrieveEmailAddress(String query)
	    {
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String emailAdd = "";
			String result="";
			String image="";
			try
			{
				con = mgDbCon.getConnection("helpDesk");
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next())
				{
					emailAdd = emailAdd+ rs.getString(1);
					emailAdd= emailAdd +  "," ;
					System.out.println("emailAdd >>>>>>> " + emailAdd);
				}
			}
			catch (Exception ee)
			{
				System.out.println("WARN:GenerateList.retrieveEmailAddress:->" + ee);
			}
			finally
			{
				closeConnection(con,ps,rs);
			}
			return emailAdd;
		}

	 public void insApprovRemarkQry  (String reqnid,String sprvID,String reqnDate,String remark,String status,String apprvLevel,
	    		String IPAdd,String compCode,String emailAdd,int tranID)
	    {
	    	String ins_IA_Approval_Remark_Qry="insert into am_Approval_Remark " +
	    			"(asset_Id,SupervisorId,DateRequisitioned,Remark,status," +
	    			"ApprovalLevel,system_ip,emailAddress,tran_type,transaction_id) values (?,?,?,?,?,?,?,?,?,?)";

	    	Connection con = null;
			PreparedStatement ps = null;
			ResultSet  rs = null;
			boolean done = false;
			try
			{
				con = mgDbCon.getConnection("helpDesk");
				ps = con.prepareStatement(ins_IA_Approval_Remark_Qry);
				ps.setString(1,reqnid);
				ps.setString(2,sprvID);
				ps.setTimestamp(3, getPostingTime(tranID));
				ps.setString(4,remark);
				ps.setString(5,status);
				ps.setString(6,apprvLevel);
				ps.setString(7,IPAdd);
				ps.setString(8,emailAdd);
				ps.setString(9,"Requisition");
				ps.setInt(10,tranID);
				done=(ps.executeUpdate()!=-1);
			}
			catch (Exception ee)
			{
				System.out.println("WARN:GenerateList.insertApprovRemarkQry:->" + ee);
			}
			finally
			{
				closeConnection(con,ps,rs);
			}

	    }

	 public void insApprovRemarkQry  (String reqnid,String sprvID,String reqnDate,String remark,String status,String apprvLevel,
	    		String IPAdd,String compCode,String emailAdd,int tranID,String tranType)
	    {
	    	String ins_IA_Approval_Remark_Qry="insert into am_Approval_Remark " +
	    			"(asset_Id,SupervisorId,DateRequisitioned,Remark,status," +
	    			"ApprovalLevel,system_ip,emailAddress,tran_type,transaction_id) values (?,?,?,?,?,?,?,?,?,?)";

	    	Connection con = null;
			PreparedStatement ps = null;
			ResultSet  rs = null;
			boolean done = false;
			try
			{
				con = mgDbCon.getConnection("helpDesk");
				ps = con.prepareStatement(ins_IA_Approval_Remark_Qry);
				ps.setString(1,reqnid);
				ps.setString(2,sprvID);
				ps.setTimestamp(3, getPostingTime(tranID));
				ps.setString(4,remark);
				ps.setString(5,status);
				ps.setString(6,apprvLevel);
				ps.setString(7,IPAdd);
				ps.setString(8,emailAdd);
				ps.setString(9,tranType);
				ps.setInt(10,tranID);
				done=(ps.executeUpdate()!=-1);
			}
			catch (Exception ee)
			{
				System.out.println("WARN:GenerateList.insertApprovRemarkQry:->" + ee);
			}
			finally
			{
				closeConnection(con,ps,rs);
			}

	    }




		public String Message(String input)
		{
			String message=input+input;
			return message;
		}



                 public void insFacApprovRemarkQry  (String reqnid,String sprvID,String reqnDate,String remark,String status,String apprvLevel,
	    		String IPAdd,String compCode,String emailAdd,int tranID)
	    {
	    	String ins_IA_Approval_Remark_Qry="insert into am_Approval_Remark " +
	    			"(asset_Id,SupervisorId,DateRequisitioned,Remark,status," +
	    			"ApprovalLevel,system_ip,emailAddress,tran_type,transaction_id) values (?,?,?,?,?,?,?,?,?,?)";

	    	Connection con = null;
			PreparedStatement ps = null;
			ResultSet  rs = null;
			boolean done = false;
			try
			{
				con = mgDbCon.getConnection("helpDesk");
				ps = con.prepareStatement(ins_IA_Approval_Remark_Qry);
				ps.setString(1,reqnid);
				ps.setString(2,sprvID);
				ps.setTimestamp(3, getPostingTime(tranID));
				ps.setString(4,remark);
				ps.setString(5,status);
				ps.setString(6,apprvLevel);
				ps.setString(7,IPAdd);
				ps.setString(8,emailAdd);
				ps.setString(9,"Facility Mgt Requisition");
				ps.setInt(10,tranID);
				done=(ps.executeUpdate()!=-1);
			}
			catch (Exception ee)
			{
				System.out.println("WARN:GenerateList.insertFacApprovRemarkQry:->" + ee);
			}
			finally
			{
				closeConnection(con,ps,rs);
			}

	    }


public void insWorkComApprovRemarkQry  (String reqnid,String sprvID,String reqnDate,String remark,String status,String apprvLevel,
	    		String IPAdd,String compCode,String emailAdd,int tranID)
	    {
	    	String ins_IA_Approval_Remark_Qry="insert into am_Approval_Remark " +
	    			"(asset_Id,SupervisorId,DateRequisitioned,Remark,status," +
	    			"ApprovalLevel,system_ip,emailAddress,tran_type,transaction_id) values (?,?,?,?,?,?,?,?,?,?)";

	    	Connection con = null;
			PreparedStatement ps = null;
			ResultSet  rs = null;
			boolean done = false;
			try
			{
				con = mgDbCon.getConnection("helpDesk");
				ps = con.prepareStatement(ins_IA_Approval_Remark_Qry);
				ps.setString(1,reqnid);
				ps.setString(2,sprvID);
				ps.setTimestamp(3, getPostingTime(tranID));
				ps.setString(4,remark);
				ps.setString(5,status);
				ps.setString(6,apprvLevel);
				ps.setString(7,IPAdd);
				ps.setString(8,emailAdd);
				ps.setString(9,"Facility Mgt Work Completion");
				ps.setInt(10,tranID);
				done=(ps.executeUpdate()!=-1);
			}
			catch (Exception ee)
			{
				System.out.println("WARN:GenerateList.insWorkComApprovRemarkQry :->" + ee);
			}
			finally
			{
				closeConnection(con,ps,rs);
			}

	    }

 public Timestamp getPostingTime(int tranId)
	 {
	        String query = "select posting_date from am_asset_approval where transaction_id=?";
	        Timestamp postingtime = null;
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;

	        try
	        {
	            con = 	mgDbCon.getConnection("helpDesk");
	            ps = con.prepareStatement(query);
	            ps.setInt(1, tranId);
	            rs = ps.executeQuery();
	            while (rs.next())
	            {
	                postingtime = rs.getTimestamp("posting_date") ;
	            }
	        }
	        catch (Exception ex)
	        {
	            System.out.println("WARN:GenerateList Error occurred in getPostingTime --> ");
	            ex.printStackTrace();
	        }
	        finally
	        {
	            closeConnection(con, ps, rs);
	        }

	        return postingtime;
	    }

	 public String formatDateWithTime(String date)
	    {
	    	String dd=date.substring(8, 10);
	    	//System.out.println("dd>>>>" + dd);
			String mm=date.substring(4, 8);
			//System.out.println("mm>>>>" + mm);
			String yyyy=date.substring(0, 4);
			//System.out.println("yyyy>>>>" + yyyy);


                        String time = date.substring(10,16);
                        String am_pm=" PM";
                        String hour = date.substring(10,13);
                        int hr = Integer.parseInt(hour.trim());

                        if(hr < 12) am_pm = " AM";

                         date =dd + mm+ yyyy + time;// + am_pm;

                        return date;
	    }
	 

}
