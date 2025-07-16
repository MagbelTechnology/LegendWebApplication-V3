package com.magbel.legend.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.magbel.ia.vao.InventoryTotal;
import com.magbel.util.ApplicationHelper;

import magma.net.vao.Inventory;
import magma.net.vao.ProcureRequisition;
import magma.net.vao.Requisition;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.FacMgtRequisition;
public class GenerateList 
{
	Requisition reqn = null;
	Inventory inv= null;
	MagmaDBConnection mgDbCon = null;
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;  
	ApplicationHelper applHelper = null;
	FacMgtRequisition facMgtReqn = null;
	ProcureRequisition procureReqn = null;
	
	public GenerateList()
	{       facMgtReqn = new FacMgtRequisition();
                reqn = new Requisition();
		reqn = new Requisition();
		inv = new Inventory();
		mgDbCon = new MagmaDBConnection();
	
	}
	  
	public Requisition findRequisitionById(String reqnID)
	{		
		String query="select * from am_ad_Requisition where ReqnId='"+reqnID+"'";
//		System.out.println("query in findRequisitionById >>>> " + query);
			
		try 
		{
			con = mgDbCon.getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next())
			{
				reqn.setUserId(rs.getString("UserId"));
				reqn.setRequestingBranch(rs.getString("ReqnBranch"));
				reqn.setRequestingSection(rs.getString("ReqnSection"));
				reqn.setRequestingDept(rs.getString("ReqnDepartment"));
				reqn.setReqnUserId(rs.getString("ReqnUserID"));
				reqn.setItemType(rs.getString("ItemType"));
				reqn.setItemRequested(rs.getString("ItemRequested"));
				reqn.setApprovLevel(rs.getInt("ApprovalLevel"));
				reqn.setAprovLevelLimit(rs.getInt("ApprovalLevelLimit"));
				reqn.setRemark(rs.getString("Remark"));
				reqn.setNo_Of_Items(rs.getString("Quantity"));
			}
		} 
		catch (Exception ex) 
		{
			System.out.println("WARN: Error findRequisitionById ->" + ex);
		} 
		finally 
		{
			closeConnection(con, ps,rs);
		}
			
		return reqn;
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
        	con = mgDbCon.getConnection("legendPlus");
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
	 
	 public ArrayList findRequisitionRemarksByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();
			
			String rmarkQry="Select * from AM_Approval_Remark " + filterQry;
//			System.out.println("rmarkQry >>> " + rmarkQry);
			
			try 
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					reqn = new Requisition();
					reqn.setSupervisor(rs.getString("supervisorID"));
					reqn.setRemark(rs.getString("remark"));
					reqn.setRemarkDate(rs.getString("remarkDate"));
					reqn.setStatus(rs.getString("status"));
					_list.add(reqn);
				}
				
			} 
			catch (Exception e) 
			{
				System.out.println(this.getClass().getName()+ " Error  findRequisitionRemarksById ->" + e.getMessage());
			}
			
			finally 
			{
				closeConnection(con, ps, rs);
			}
			
			return _list;
		}
	 
	 public ArrayList findnewStockRemarksByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();
			
			String rmarkQry="Select * from AM_Approval_Remark " + filterQry;
//			System.out.println("rmarkQry >>> " + rmarkQry);
			
			try 
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					inv = new Inventory();
					inv.setSupervisor(rs.getString("supervisorID"));
					inv.setRemark(rs.getString("remark"));
					inv.setRemarkDate(rs.getString("remarkDate"));
					inv.setStock_status(rs.getString("status"));
					_list.add(inv);
				}
				
			} 
			catch (Exception e) 
			{
				System.out.println(this.getClass().getName()+ " Error  findRequisitionRemarksById ->" + e.getMessage());
			}
			
			finally 
			{
				closeConnection(con, ps, rs);
			}
			
			return _list;
		}
	 
	 public void insertIntoProcurement(String compCode,String reqnID,
			                            String IpAdd)
		{
			reqn = new Requisition();
			applHelper = new ApplicationHelper();
			
			boolean result = false;
		
			
			String sel_Ia_Reqn_Qry="Select ReqnBranch,ReqnSection,ReqnDepartment,ReqnDate,ReqnUserID," +
					"ItemType,ItemRequested,Image,Remark,Quantity,UserID from Am_Ad_Requisition " +
					" where ReqnID='"+reqnID+"'";
			
			String procID = "PROC/"+applHelper.getGeneratedId("am_ad_PROCUREMENT");
					
			try 
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(sel_Ia_Reqn_Qry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					reqn.setRequestingBranch(rs.getString("ReqnBranch"));
					reqn.setRequestingSection(rs.getString("ReqnSection"));
					reqn.setRequestingDept(rs.getString("ReqnDepartment"));
					reqn.setRequisitionDate(rs.getString("ReqnDate"));
					reqn.setReqnUserId(rs.getString("ReqnUserID"));
					reqn.setItemType(rs.getString("ItemType"));
					reqn.setItemRequested(rs.getString("ItemRequested"));
					reqn.setIsImage(rs.getString("Image"));
					reqn.setRemark(rs.getString("Remark"));
					reqn.setNo_Of_Items(rs.getString("Quantity"));
					reqn.setUserId(rs.getString("UserID"));
				}
				reqn.setCompany_code(compCode);
				reqn.setReqnID(reqnID);
				
				String procurementInsertQry=
				" insert into am_ad_PROCUREMENT  (mtid,ProcID,ReqnID,UserID,ReqnBranch,ReqnSection,ReqnDepartment," +
				" ReqnDate, ReqnUserID,ItemType,ItemRequested,company_code,Image,Remark,workStationIP,Quantity,ordered )" +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			/*	
				System.out.println("RequisitionDate >>>>>>>>>> " + reqn.getRequisitionDate());
				System.out.println("UserId >>>>>>>>>> " + reqn.getUserId());
				System.out.println("RequestingBranch >>>>>>>>>> " + reqn.getRequestingBranch());
				System.out.println("RequestingSection >>>>>>>>>> " + reqn.getRequestingSection());
				System.out.println("getRequestingDept >>>>>>>>>> " + reqn.getRequestingDept());
				System.out.println("getReqnUserId >>>>>>>>>> " + reqn.getReqnUserId());
				System.out.println("getItemType >>>>>>>>>> " + reqn.getItemType());
				System.out.println("getItemRequested >>>>>>>>>> " + reqn.getItemRequested());
				System.out.println("getCompany_code >>>>>>>>>> " + reqn.getCompany_code());
				System.out.println("getIsImage >>>>>>>>>> " + reqn.getIsImage());
				System.out.println("getRemark >>>>>>>>>> " + reqn.getRemark());
				System.out.println("getNo_Of_Items >>>>>>>>>> " + reqn.getNo_Of_Items());
				System.out.println("IpAdd >>>>>>>>>> " + IpAdd);
							*/
				ps = con.prepareStatement(procurementInsertQry);
				ps.setString(1, applHelper.getGeneratedId("Am_Ad_PROCUREMENT"));
				ps.setString(2, procID);
				ps.setString(3, reqn.getReqnID());
				ps.setString(4, reqn.getUserId());
				ps.setString(5, reqn.getRequestingBranch());
				ps.setString(6, reqn.getRequestingSection());
				ps.setString(7, reqn.getRequestingDept());
				ps.setString(8, reqn.getRequisitionDate());
				ps.setString(9, reqn.getReqnUserId());
				ps.setString(10, reqn.getItemType());
				ps.setString(11, reqn.getItemRequested());
				ps.setString(12, reqn.getCompany_code());
				ps.setString(13, reqn.getIsImage());
				ps.setString(14, reqn.getRemark());
				ps.setString(15, IpAdd);
				ps.setString(16, reqn.getNo_Of_Items());
				ps.setString(17, "N");
				result = (ps.executeUpdate() == -1);
//	            System.out.println("result >>>>>>>>> " + result);
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

			finally 
			{
				closeConnection(con, ps, rs);
			}
			
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
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next())
				{
					emailAdd = emailAdd+ rs.getString(1);
					emailAdd= emailAdd +  "," ;
//					System.out.println("emailAdd >>>>>>> " + emailAdd);
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
				con = mgDbCon.getConnection("legendPlus");
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
				con = mgDbCon.getConnection("legendPlus");
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
	 
	 public Timestamp getPostingTime(int tranId)
	 {
	        String query = "select posting_date from am_asset_approval where transaction_id=?";
	        Timestamp postingtime = null;
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;

	        try
	        {
	            con = 	mgDbCon.getConnection("legendPlus");
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
	 
	 public ArrayList findAdminRequisitionListByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();
			
			String rmarkQry="select b.user_id AS ReqnUserID,a.Description AS itemRequested,CONVERT(VARCHAR(10),a.TRANSFER_DATE,101) AS ReqnDate,a.Batch_id AS ReqnID, "+
							"b.transaction_id,a.PROCESS_STATUS AS status from  am_ad_TransferRequisition a,am_asset_approval b "+ 
							"where (a.Batch_id = b.asset_id) and  b.process_status='A'"+filterQry;
//			System.out.println("rmarkQry >>> " + rmarkQry);
			 
			try 
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					reqn = new Requisition();
					reqn.setUserId(rs.getString("ReqnUserID"));
					reqn.setItemType(rs.getString("itemRequested"));
					reqn.setRequisitionDate(rs.getString("ReqnDate"));
					reqn.setReqnID(rs.getString("ReqnID"));
					reqn.setTranID(rs.getString("transaction_id"));
					reqn.setStatus(rs.getString("status"));
					_list.add(reqn);
				}
				
			} 
			catch (Exception e) 
			{
				System.out.println(this.getClass().getName()+ " Error  findAdminRequisitionListByQry ->" + e.getMessage());
			}
			
			finally 
			{
				closeConnection(con, ps, rs);
			}
			
			return _list;
		} 
		public ArrayList findItemByCode(String itemCode,int total)
		{		
			InventoryTotal tot=null;
			ArrayList _list = new ArrayList();
		 	String query=	" select balance,name,a.warehouse_code from dbo.AM_INVENTORY_TOTALS a,dbo.am_ad_WareHouse b "+
							" where item_code ='"+itemCode+"' and balance > "+total+" and a.warehouse_code=b.warehouse_code ";
				
			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next())
				{
					tot = new InventoryTotal(); 
					tot.setItemBalance(rs.getInt("balance"));
					tot.setDesc(rs.getString("name"));
					
				//	System.out.println(tot.getItemBalance()+"  = "+tot.getDesc());
					_list.add(tot);
					
				}
			} 
			catch (Exception ex) 
			{
				System.out.println("WARN: Error findItemByCode ->" + ex);
			} 
			finally 
			{
				closeConnection(con, ps,rs);
			}
				
			return _list;
		}
		
		public String findItemByCode2(String itemCode,int total)
		{		
			String message="";
			String mes="";
			InventoryTotal tot=null;
			ArrayList _list = new ArrayList();
		 	String query=	" select balance,name,a.warehouse_code from dbo.AM_INVENTORY_TOTALS a,dbo.am_ad_WareHouse b "+
							" where item_code ='"+itemCode+"' and balance > "+total+" and a.warehouse_code=b.warehouse_code ";
				
			try 
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next())
				{
					tot = new InventoryTotal();
					tot.setItemBalance(rs.getInt("balance"));
					tot.setDesc(rs.getString("name"));
					
					System.out.println(tot.getItemBalance()+"  = "+tot.getDesc());
					message=" Quantity in house = "+tot.getItemBalance()+" From warehouse "+tot.getDesc();
				mes=	Message(message); 
				}
			} 
			catch (Exception ex) 
			{
				System.out.println("WARN: Error findItemByCode2 ->" + ex);
			} 
			finally 
			{
				closeConnection(con, ps,rs);
			}
				
			return mes;
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
				con = mgDbCon.getConnection("legendPlus");
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



 public FacMgtRequisition findFacMgtRequisitionById(String reqnID)
	{
		String query="select * from fm_Requisition where ReqnId='"+reqnID+"'";


		try
		{
			con = mgDbCon.getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next())
			{
				facMgtReqn.setUserId(rs.getString("UserId"));
				facMgtReqn.setRequestingBranch(rs.getString("ReqnBranch"));
				facMgtReqn.setRequestingSection(rs.getString("ReqnSection"));
				facMgtReqn.setRequestingDept(rs.getString("ReqnDepartment"));
				facMgtReqn.setReqnUserId(rs.getString("ReqnUserID"));
				//facMgtReqn.setItemType(rs.getString("ItemType"));
				//facMgtReqn.setItemRequested(rs.getString("ItemRequested"));
				facMgtReqn.setApprovLevel(rs.getInt("ApprovalLevel"));
				facMgtReqn.setAprovLevelLimit(rs.getInt("ApprovalLevelLimit"));
				facMgtReqn.setRemark(rs.getString("Remark"));
				//facMgtReqn.setNo_Of_Items(rs.getString("Quantity"));

                                facMgtReqn.setAssetId(rs.getString("Asset_id"));
                                facMgtReqn.setReqMeans(rs.getString("req_Means"));
                                facMgtReqn.setCategoryWork(rs.getString("category_Work"));
                                facMgtReqn.setMaintenanceNature(rs.getString("Maintenance_Nature"));
                                facMgtReqn.setPriority(rs.getString("Priority"));
                                facMgtReqn.setNumOfWork(rs.getInt("No_Work_Description"));
                                facMgtReqn.setLocation(rs.getString("Location"));
                                facMgtReqn.setWorkId(rs.getInt("work_id"));

			}
		}
		catch (Exception ex)
		{
			System.out.println("WARN: Error findFacMgtRequisitionById ->" + ex);
		}
		finally
		{
			closeConnection(con, ps,rs);
		}

		return facMgtReqn;
	}





	 public ArrayList findFacAcceptanceRequisitionListByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();

			String rmarkQry="select a.ReqnUserID,a.itemRequested,a.ReqnDate,a.ReqnID,b.transaction_id,a.status from "+
							" fm_requisition a,am_asset_approval b where (a.ReqnID = b.asset_id) and "+
							" b.process_status='X'";
			//System.out.println("rmarkQry >>> " + rmarkQry);

			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					facMgtReqn = new FacMgtRequisition();
					facMgtReqn.setUserId(rs.getString("ReqnUserID"));
					facMgtReqn.setItemType(rs.getString("itemRequested"));
					facMgtReqn.setRequisitionDate(rs.getString("ReqnDate"));
					facMgtReqn.setReqnID(rs.getString("ReqnID"));
					facMgtReqn.setTranID(rs.getString("transaction_id"));
					facMgtReqn.setStatus(rs.getString("status"));
					_list.add(facMgtReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error  findFacAcceptanceRequisitionListByQry ->" + e.getMessage());
			}

			finally
			{
				closeConnection(con, ps, rs);
			}

			return _list;
		}

 
         public ArrayList findFacilityAcceptanceListByQry(String filterQry)
		{

			ArrayList _list = new ArrayList();

			String rmarkQry="select a.UserID,a.asset_id,a.ReqnDate,a.ReqnID,b.transaction_id,a.status,a.category_Work, "+
							" a.Priority,a.image from FM_Requisition a,am_asset_approval b where (a.ReqnID = b.asset_id) and "+
							" b.process_status='FX' and a.ReqnID not in (select ReqnID from FM_MAITENANCE_DUE ) " + filterQry;
//             System.out.println("rmarkQry in findFacilityAcceptanceListByQry is >>>>>>>> " +rmarkQry);
			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					facMgtReqn = new FacMgtRequisition();
					facMgtReqn.setUserId(rs.getString("UserID"));
					facMgtReqn.setAssetId(rs.getString("asset_id"));
					facMgtReqn.setRequisitionDate(rs.getString("ReqnDate"));
					facMgtReqn.setReqnID(rs.getString("ReqnID"));
					facMgtReqn.setTranID(rs.getString("transaction_id"));
					facMgtReqn.setStatus(rs.getString("status"));
                                        facMgtReqn.setCategoryWork(rs.getString("category_Work"));
                                        facMgtReqn.setPriority(rs.getString("Priority"));
					facMgtReqn.setIsImage(rs.getString("image"));
                                        _list.add(facMgtReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error  findFacilityAcceptanceListByQry ->" + e.getMessage());
			}

			finally
			{
				closeConnection(con, ps, rs);
			}

			return _list;
		}

 public ArrayList findFacRequisitionRemarksByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();

			String rmarkQry="Select * from AM_Approval_Remark " + filterQry;
//			System.out.println("rmarkQry >>> " + rmarkQry);

			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					facMgtReqn = new FacMgtRequisition();
					facMgtReqn.setSupervisor(rs.getString("supervisorID"));
					facMgtReqn.setRemark(rs.getString("remark"));
					facMgtReqn.setRemarkDate(rs.getString("remarkDate"));
					facMgtReqn.setStatus(rs.getString("status"));
					_list.add(facMgtReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error  findFacRequisitionRemarksByQry ->" + e.getMessage());
			}

			finally
			{
				closeConnection(con, ps, rs);
			}

			return _list;
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
				con = mgDbCon.getConnection("legendPlus");
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
 

public ArrayList<Requisition> findAllRequisition(String filter) 
{
	ArrayList<Requisition> _list = new ArrayList<Requisition>();
	
	String query = " SELECT ReqnID,itemRequested,Remark,distributedQty,quantity,projectCode " +
			" "+ filter;
	String itemNameQry=" SELECT description from ia_inventory_items where item_code='";
//	System.out.println("findAllRequisition query: "+query);
	try 
	{
		con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		while(rs.next())
		{ 
			reqn = new Requisition();
			reqn.setReqnID(rs.getString("reqnID"));
			//reqn.setItemRequested(applhelper.descCode(itemNameQry + rs.getString("itemRequested")+"'"));
			reqn.setItemRequested(rs.getString("itemRequested"));
			reqn.setRemark(rs.getString("Remark"));
			reqn.setNo_Of_Items(rs.getString("quantity"));
			reqn.setDistributedQty(rs.getString("distributedQty"));
			reqn.setProjCode(rs.getString("projectCode"));
			
			_list.add(reqn);
		}
	} 
	catch (Exception e) 
	{
	System.out.println(this.getClass().getName()+ " Error findAllRequisition ->" + e.getMessage());
	}
	
	finally 
	{
	closeConnection(con, ps, rs);
	}
			return _list;
			
}
public FacMgtRequisition findRequisitionDetailsById(String groupId)
{
	String query="select * from FM_REQUISITION_DETAILS where GROUP_ID='"+groupId+"'";


	try
	{
		con = mgDbCon.getConnection("legendPlus");
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next())
		{
            facMgtReqn.setAssetId(rs.getString("Asset_id"));
            facMgtReqn.setReqnID(rs.getString("group_Id"));
            facMgtReqn.setDescription(rs.getString("description"));

		}
	}
	catch (Exception ex)
	{
		System.out.println("WARN: Error findFacMgtRequisitionById ->" + ex);
	}
	finally
	{
		closeConnection(con, ps,rs);
	}

	return facMgtReqn;
}


public ArrayList findProcurmentRequisitionListByQry(String filterQry)
		{
			ArrayList _list = new ArrayList();

String rmarkQry="select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity,itemtype from " +
                               "PR_AWAIT_REQUISITION where status='X'"+ filterQry;
			//System.out.println("rmarkQry >>> " + rmarkQry);

			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					procureReqn = new ProcureRequisition();
					procureReqn.setUserId(rs.getString("ReqnUserID"));
					procureReqn.setItemRequested(rs.getString("itemRequested"));
                                     	procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
					procureReqn.setReqnID(rs.getString("ReqnID"));
					//procureReqn.setTranID(rs.getString("transaction_id"));
					procureReqn.setStatus(rs.getString("status"));
                                       procureReqn.setQuantity(rs.getInt("quantity"));
                                       procureReqn.setItemType(rs.getString("itemtype"));
					_list.add(procureReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error findProcurmentRequisitionListByQry ->" + e.getMessage());
			}

			finally
			{
				closeConnection(con, ps, rs);
			}

			return _list;
		}

public ArrayList findProcurmentRequisitionQry(String filterQry)
		{
			ArrayList _list = new ArrayList();

			String rmarkQry="select ReqnUserID,itemRequested,ReqnDate,ReqnID,status,quantity,itemtype from " +
                               "PR_REQUISITION where status='ACTIVE'"+ filterQry;
			//System.out.println("rmarkQry >>> " + rmarkQry);

			try
			{
				con = mgDbCon.getConnection("legendPlus");
				ps = con.prepareStatement(rmarkQry);
				rs = ps.executeQuery();
				while(rs.next())
				{
					procureReqn = new ProcureRequisition();
					procureReqn.setUserId(rs.getString("ReqnUserID"));
					procureReqn.setItemRequested(rs.getString("itemRequested"));
                                     	procureReqn.setRequisitionDate(rs.getString("ReqnDate"));
					procureReqn.setReqnID(rs.getString("ReqnID"));
					//procureReqn.setTranID(rs.getString("transaction_id"));
					procureReqn.setStatus(rs.getString("status"));
                                       procureReqn.setQuantity(rs.getInt("quantity"));
                                       procureReqn.setItemType(rs.getString("itemtype"));
					_list.add(procureReqn);
				}

			}
			catch (Exception e)
			{
				System.out.println(this.getClass().getName()+ " Error findProcurmentRequisitionListByQry ->" + e.getMessage());
			}

			finally
			{
				closeConnection(con, ps, rs);
			}

			return _list;
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
 
 public String formatDateWithDateTime(String date)
 {
 	String dd=date.substring(8, 10);
 	//System.out.println("dd>>>>" + dd);
		String mm=date.substring(4, 8);
		//System.out.println("mm>>>>" + mm);
		String yyyy=date.substring(0, 4);
		//System.out.println("yyyy>>>>" + yyyy);
		String ss = date.substring(18, 19);
                 String time = date.substring(12,19);
                 String am_pm=" PM";
                 String hour = date.substring(12,13);
                 int hr = Integer.parseInt(hour.trim());

                 if(hr < 12) am_pm = " AM";

                  date =dd + mm+ yyyy + time;// + am_pm;

                 return date;
 }

 
}
