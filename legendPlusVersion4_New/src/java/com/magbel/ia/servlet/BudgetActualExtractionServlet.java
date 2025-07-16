package com.magbel.ia.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magma.net.dao.MagmaDBConnection;

import com.magbel.ia.vao.BudgetSummary;
import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class BudgetActualExtractionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
    public BudgetActualExtractionServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processCategoryItemType(request,response);
	}

	private void processCategoryItemType(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
	{
		String pageName = request.getParameter("pageName");
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
	//	applHelper = new ApplicationHelper();
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();

		String []result = null ;
			String batchNo = "";
			String assetId = "";
		  try
		        {  
			  DeletebudgetActualRecords();
	//		  	if((batch_Id!=null) || (batch_Id!="")){
			     java.util.ArrayList list =CurrentBudgetSummaryRecord();
			     
//			     System.out.println("-->size>--> "+list.size()+"  Batch Id:  "+batch_Id);
			     for(int i=0;i<list.size();i++)
			     {
			    	 com.magbel.ia.vao.BudgetSummary  summary = (com.magbel.ia.vao.BudgetSummary)list.get(i);    	 
			    	 String category =  summary.getCategory();
			    	 double actual = summary.getActual();
			    	 double budget = summary.getBudget();
			    	 double variancevalue = summary.getVariancevalue();
			    	 double variancepercent = summary.getVariancepercent();
			    	 String remark = summary.getRemark();
			    	 String quarter = summary.getQuarter();
			    	 String glNo = summary.getGlNo();
			    	 String gldescription = summary.getGldescription();
			    	 if(!category.equalsIgnoreCase(""))
			    		 insertBudgetSummaryEntries(category, actual,budget,variancevalue,variancepercent,remark,quarter,glNo,gldescription);
			     }
		       // }
	//			  	if((batch_Id==null) || (batch_Id=="")){
					     java.util.ArrayList list1 =budgetSummaryArchive();
					     for(int j=0;j<list1.size();j++)
					     {
					    	 com.magbel.ia.vao.BudgetSummary  summary = (com.magbel.ia.vao.BudgetSummary)list1.get(j);
					    	 String category =  summary.getCategory();
					    	 double actual = summary.getActual();
					    	 double budget = summary.getBudget();
					    	 double variancevalue = summary.getVariancevalue();
					    	 double variancepercent = summary.getVariancepercent();
					    	 String remark = summary.getRemark();
					    	 String quarter = summary.getQuarter();
					    	 String glNo = summary.getGlNo();
					    	 String gldescription = summary.getGldescription();
					    	 if(!category.equalsIgnoreCase(""))
					    		 updateBudgetSummaryEntries(category, actual,budget,variancevalue,variancepercent,remark,quarter,glNo,gldescription);
					     }		
			//	  	}			     
			     out.print("<script>window.location='DocumentHelp.jsp?np="+pageName+"'</script>");
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
	//	    }
		
	}

	public java.util.ArrayList CurrentBudgetSummaryRecord()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from Budget_Actual_Summary ";
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("ias");
				s = c.createStatement();
				rs = s.executeQuery(query);
				while (rs.next())
				   {
					String category = rs.getString("CATEGORY");
					double actual = rs.getDouble("ACTUAL");
					double budget = rs.getDouble("BUDGET");
					double variancevalue = rs.getDouble("VARIANCE_VALUE");
					double variancepercent = rs.getDouble("VARIANCE_PERCENT");
					String remark = rs.getString("REMARK");
					String quarter = rs.getString("QUARTER");
					String glNo = rs.getString("GL_ACCT_NO");
					String gldescription = rs.getString("DESCRIPTION");
					BudgetSummary summary = new BudgetSummary();
					summary.setCategory(category);
					summary.setActual(actual);
					summary.setBudget(budget);
					summary.setVariancevalue(variancevalue);
					summary.setVariancepercent(variancepercent);
					summary.setRemark(remark);
					summary.setQuarter(quarter);
					summary.setGlNo(glNo);
					summary.setGldescription(gldescription);
					_list.add(summary);
				   }
		 }
					 catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							closeConnection(c, s, rs);
						}
		return _list;
	}

	public java.util.ArrayList budgetSummaryArchive()
	{
		java.util.ArrayList _list = new java.util.ArrayList();
		String finacleTransId= null;
			String query = " SELECT  * from Budget_Actual_Summary_Archive ";
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;
	String result = "";
		try {
			   // c = getConnection();
			    c = mgDbCon.getConnection("ias");
				s = c.createStatement();
				rs = s.executeQuery(query);
				while (rs.next())
				   {
					String category = rs.getString("CATEGORY");
					double actual = rs.getDouble("ACTUAL");
					double budget = rs.getDouble("BUDGET");
					double variancevalue = rs.getDouble("VARIANCE_VALUE");
					double variancepercent = rs.getDouble("VARIANCE_PERCENT");
					String remark = rs.getString("REMARK");
					String quarter = rs.getString("QUARTER");
					String glNo = rs.getString("GL_ACCT_NO");
					String gldescription = rs.getString("DESCRIPTION");
					BudgetSummary summary = new BudgetSummary();
					summary.setCategory(category);
					summary.setActual(actual);
					summary.setBudget(budget);
					summary.setVariancevalue(variancevalue);
					summary.setVariancepercent(variancepercent);
					summary.setRemark(remark);
					summary.setQuarter(quarter);
					summary.setGlNo(glNo);
					summary.setGldescription(gldescription);
					_list.add(summary);
				   }
		 }
					 catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							closeConnection(c, s, rs);
						}
		return _list;
	}
		
	public boolean insertBudgetSummaryEntries(String category, double actual,double budget,double variancevalue,
			double variancepercent,String remark,String quarter,String glNo,String gldescription) {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
        String familyID;
        query = "INSERT INTO BURGET_ACTUAL_SUMMARY (CATEGORY,GL_ACCT_NO,GL_DESCRIPTION,C_ACTUAL,C_BUDGET,C_VARIANCE_VALUE,C_VARIANCE_PERCENT," +
		"C_REMARK,C_QUARTER)VALUES (?,?,?,?,?,?,?,?,?)"
		;

        con = null;
        ps = null;
        done = false;
        try{  
        con =  mgDbCon.getConnection("ias");
        ps = con.prepareStatement(query);
        ps.setString(1, category);
        ps.setString(2, glNo);
        ps.setString(3, gldescription);
        ps.setDouble(4, actual);
        ps.setDouble(5, budget);
        ps.setDouble(6, variancevalue);
        ps.setDouble(7, variancepercent);
        ps.setString(8, remark);
        ps.setString(9, quarter);
        ps.addBatch();
        done = ps.executeBatch().length != -1;
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR insertBudgetSummaryEntries Posting.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
	}
	public boolean updateBudgetSummaryEntries(String category, double actual,double budget,double variancevalue,
			double variancepercent,String remark,String quarter,String glNo,String gldescription) {
        String query;
        Connection con;
        PreparedStatement ps;
        boolean done;
		 query = "UPDATE BURGET_ACTUAL_SUMMARY SET L_ACTUAL ="+actual+",L_BUDGET = "+budget+",L_VARIANCE_VALUE = "+variancevalue+","
				+ " L_VARIANCE_PERCENT = "+variancepercent+",L_REMARK = '"+remark+"', L_QUARTER = '"+quarter+"' "
				+ " WHERE GL_ACCT_NO ='"+glNo+"' AND C_QUARTER = '"+quarter+"' ";
		System.out.println("<<<query: "+query); 
        con = null;
        ps = null; 
        done = false;  
        try{  
        con =  mgDbCon.getConnection("ias");
        ps = con.prepareStatement(query);
/*        ps.setDouble(1, actual);
        ps.setDouble(2, budget);
        ps.setDouble(3, variancevalue);
        ps.setDouble(4, variancepercent);
        ps.setString(5, remark);
        ps.setString(6, quarter); 
        ps.setString(7, category);*/
        done = (ps.executeUpdate() != -1);
        closeConnection(con, ps);
    	}catch(Exception ex){
        System.out.println((new StringBuilder()).append("ERROR updateBudgetSummaryEntries Posting.. ").append(ex.getMessage()).toString());
        ex.printStackTrace();
        done = false;
        closeConnection(con, ps);
    	} finally{
        closeConnection(con, ps);
    	}
        return done;
	}	
	public void DeletebudgetActualRecords(){
		String query_r ="delete from BURGET_ACTUAL_SUMMARY";

		Connection con = null;
		        PreparedStatement ps = null;
		try {
			con = mgDbCon.getConnection("ias");
		ps = con.prepareStatement(query_r);
		           int i =ps.executeUpdate();
		        } catch (Exception ex) {
		  
		            System.out.println("BudgetActualServlet: DeletebudgetActualRecords()>>>>>" + ex);
		        } finally {
		            closeConnection(con, ps);
		        }
		}	
    private void closeConnection(Connection con, PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}

	}	
	
	private void closeConnection(Connection con, Statement s, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (s != null) {
				s.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("WARNING: Error closing connection ->"
					+ e.getMessage());
		}
	}

}
