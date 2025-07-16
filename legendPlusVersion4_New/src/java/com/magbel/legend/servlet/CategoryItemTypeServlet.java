package com.magbel.legend.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magma.net.dao.MagmaDBConnection;

import com.magbel.util.*;
/**
 * Servlet implementation class RequisitionServlet
 */
public class CategoryItemTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;  
	MagmaDBConnection mgDbCon = null;
	ApplicationHelper applHelper = null;
    public CategoryItemTypeServlet()
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
		// TODO Auto-generated method stub
		mgDbCon = new MagmaDBConnection();
		applHelper = new ApplicationHelper();
		PrintWriter out = response.getWriter();
		
		String itemName = request.getParameter("Desc");
		String ReOrderLevel = request.getParameter("ROrderLvl");
		String MinimumQty = request.getParameter("MinQty");
		String itemStatus = request.getParameter("categoryStatus");
		String requireApproval = request.getParameter("reqApproval");
		String MaxApprovlevel = request.getParameter("MxApprvLvl");
		String MinApprovAmt = request.getParameter("MnApprvAmt");
		String MaxApprovAmt = request.getParameter("MxApprvAmt");
		String AdjustMaxApprvLvl = request.getParameter("AdMxApprvLvl");
		String AdjustMnApprovAmt = request.getParameter("AdMnApprvAmt");
		String AdjustMaxApprovAmt = request.getParameter("AdMxApprvAmt");
		
		String categoryCode = request.getParameter("cat_code");
		String categoryName = request.getParameter("cat_Name");
		int categoryID =  Integer.parseInt(request.getParameter("categoryId"));
		boolean done = false;
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("CurrentUser");
		String companyCode = request.getParameter("comp_code");
		String itemCode = applHelper.getGeneratedId("am_group_asset");
		String userClass = (String) session.getAttribute("UserClass");
		
		 if (!userClass.equals("NULL") || userClass!=null){
		//first check if for that companyCode,categoryCode,categoryID , there is no corresponding
		//itemName
		if(chkItemName(companyCode,categoryCode,categoryID))
		{
			String insItemType_Qry="insert into am_CategoryItemType (" +
					"itemName,ReorderLevel,MinQty,ItemStatus,ReqApproval,MaxApprvLevel,MinApprvAmt,MaxApprvAmt," +
					"AdMaxApprvLevel,AdMinApprvAmt,AdMaxApprvAmt,Company_code,Category_code,Category_id," +
					"WorkStationIP,userId,itemCode" +
					") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
			try
			{
				con = mgDbCon.getConnection("legendPlus");
				pstmt = con.prepareStatement(insItemType_Qry);
				
				pstmt.setString(1, itemName);
				pstmt.setString(2, ReOrderLevel);
				pstmt.setInt(3, Integer.parseInt(MinimumQty));
				pstmt.setString(4, itemStatus);
				pstmt.setString(5, requireApproval);
				pstmt.setInt(6, Integer.parseInt(MaxApprovlevel));
				pstmt.setString(7, MinApprovAmt);
				pstmt.setString(8, MaxApprovAmt);
				pstmt.setInt(9, Integer.parseInt(AdjustMaxApprvLvl));
				pstmt.setString(10, AdjustMnApprovAmt);
				pstmt.setString(11, AdjustMaxApprovAmt);
				pstmt.setString(12, companyCode);
				pstmt.setString(13, categoryCode);
				pstmt.setInt(14, categoryID);
				pstmt.setString(15, request.getRemoteAddr());
				pstmt.setString(16, userId);
				pstmt.setString(17, itemCode);
				
				done = (pstmt.executeUpdate() != -1);
				System.out.println("========checking if successful ===== "+done);
			}
			catch(Exception ex)
			{
				System.out.println("WARNING:Error executing Query ->"+ ex.getMessage());
			} 
			finally 
			{
				closeConnection(con, pstmt,rs);
			}
		
			out.print("<script>alert('Item with Name "+ itemName.toUpperCase()+ "  successfully created ')</script>");
			
		}
		else
		{
			out.print("<script>alert('Item with Name "+ itemName.toUpperCase()+ "  already " +
					"exists with this Category - "+ categoryName + " ')</script>");
			out.print("<script>window.location='DocumentHelp.jsp?np=ItemTypeSetup&CAT="+categoryID+"'</script>");   	
		}
	}
	}
	
	private boolean chkItemName(String companyCode,String categoryCode,int categoryID)
	{
		boolean result = true;
		
		String selItemChk_qry="select * from CategoryItemType where company_code='"+companyCode+"'" +
				" and category_code='"+categoryCode+"' and category_id="+categoryID;
		
		try
		{
			con = mgDbCon.getConnection("legendPlus");
			pstmt = con.prepareStatement(selItemChk_qry);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				result = false;
            }
		}
		catch(Exception ex)
		{
			
		}
		return result;
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
