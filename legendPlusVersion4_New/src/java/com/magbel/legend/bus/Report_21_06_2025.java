/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.legend.bus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.magbel.dao.PersistenceServiceDAO;
import com.magbel.legend.vao.AuditLog;
import com.magbel.legend.vao.newAssetTransaction;

import legend.ConnectionClass;
import legend.admin.objects.User;
import magma.StockRecordsBean;
import magma.net.vao.Asset;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.FleetManatainanceRecord;

/**
 *
 *  @author Kareem Wasiu Aderemi
 */
public class Report_21_06_2025 extends PersistenceServiceDAO {



    public String getCompanyName() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String comp = "";

        try {
            query = "SELECT COMPANY_NAME FROM am_gb_company";
            con = getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {

                comp = rs.getString("COMPANY_NAME");

            }


        } catch (Exception e) {
            System.out.println("WARNING: cannot fetch am_gb_company->" +
                    e.getMessage());

        } finally {
            closeConnection(con, ps, rs);
        }

        return comp;

    }



    public ArrayList getReportByColumn(String query, String[] selCol) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        for (int i = 0; i < count; i++) {

        // System.out.println("SELECTED COLUMNS IN getReportByColumn : " + i + " " + selCol[i]);

        }

        int rowCount = 0;

        //String[] temp = new String[count];
        ArrayList rows = new ArrayList();


        String FINDER_QUERY = query;
 //       System.out.println("<<<<<<<FINDER_QUERY in getReportByColumn: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs = ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

 //           System.out.println("Report Compilation in Progress.......::: " + selCol.length + "  Columns");


            /*while (rs.next()){
            ArrayList row = new ArrayList();
            for (int i = 1; i <= columnCount ; i++){
            row.add(rs.getString(i));
            }
            rows.add(row);
            }
             */
            while (rs.next()) {
                rowCount++;
                //ArrayList row = new ArrayList();
                String[] temp = new String[count];
                //for(int i = 0; i<count;i++){
                for (int i = 0; i < count; i++) {

                    temp[i] = rs.getString(findColById(selCol[i]));
                 //   System.out.println("SEE the COLUMNS: " + i + " " + temp[i]);
                   // rows.add(temp[i]);

                }

                rows.add(temp);

            }

//System.out.println("TOTAL ROW COUNT<<:::>> " + rowCount);

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch the column for the report  <><>>>:  " +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return rows;
    }


    public ArrayList getTabletById(
            String query, String[] selCol) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        String[] temp = new String[count];

        String FINDER_QUERY = query;
//        System.out.println("<<<<<<<FINDER_QUERY in getTabletById: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs =
                    ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

            while (rs.next()) {
                for (int i = 0; i < count;
                        i++) {
                    temp[i] = rs.getString(findTabById(selCol[i]));

                   // System.out.println("SEE the TABLE: " + i + " " + temp[i]);

                }

                collection.add(temp);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch the table by id" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public String[] findTabById(String id, int count) {
  //  	  System.out.println("<<<<<<<id in findTabById: "+id);
        String[] col = new String[count];

        for (int i = 0; i < count;
                i++) {
            col[i] = findTabById(id);
        }

        return col;

    }

    public String findTabById(
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab = "";

        String FINDER_QUERY = "SELECT TABLE_NAME from COL_LOOK_UP WHERE ID =?";
//        System.out.println("<<<<<<<FINDER_QUERY in findTabById: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                tab = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return tab;

    }

    public String[] findByIdQuery(String id, int count, String query) {

        String[] col = new String[count];
//        System.out.println("<<<<<<<query in findByIdQuery: "+query);
        for (int i = 0; i < count;
                i++) {
            col[i] = findByIdQuery(id, query);
        }

        return col;

    }

    public String findByIdQuery(String id, String query) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab = "";

        String FINDER_QUERY = query;
//        System.out.println("<<<<<<<FINDER_QUERY in findByIdQuery: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                tab = rs.getString(1);
               // System.out.println("Output column by Id Query->" + tab);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Id Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return tab;

    }


   // tabNameFrom =  rep.findByIdQuery1(aliasIdFrom[i],aliasCountF,tabQuery);

    public ArrayList findByQuery1(String query, String[] sel) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab;

        ArrayList list = new ArrayList();
        String FINDER_QUERY = query;
//        System.out.println("<<<<<<<FINDER_QUERY in findByQuery1: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            rs =
                    ps.executeQuery();

            while (rs.next()) {



                list.add(rs.getString(1));
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }

    public ArrayList findByQuery(String query) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tab;

        ArrayList list = new ArrayList();
        String FINDER_QUERY = query;
//        System.out.println("<<<<<<<FINDER_QUERY in findByQuery: "+FINDER_QUERY);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);


            rs =
                    ps.executeQuery();

            while (rs.next()) {

                list.add(rs.getString(1));
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch by Query->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;

    }


    public boolean isNumeric(String id,String op) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";
        boolean status = false;

        String FINDER_QUERY = "SELECT OPERAND from COL_FILTER WHERE ID =? AND OPERAND =?";
 //       System.out.println("<<<<<<<FINDER_QUERY in findByQuery: "+FINDER_QUERY+"   ID: "+id+"    OP: "+op);
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
             ps.setString(2, op);
            rs = ps.executeQuery();

            while (rs.next()) {

               status = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNumeric->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return status;
    }

    public boolean isNumericColumn(String id,String dType) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";
        boolean status = false;

        String FINDER_QUERY = "SELECT DATA_TYPE from COL_LOOK_UP WHERE ID =? AND DATA_TYPE =?";
//        System.out.println("<<<<<<<FINDER_QUERY in isNumericColumn: "+FINDER_QUERY+"    ID: "+id+"   dType: "+dType);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
             ps.setString(2, dType);
            rs = ps.executeQuery();

            while (rs.next()) {

               status = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNumeric->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return status;
    }



    public String[] findColById(String id, int count) {
        String[] col = new String[count];

        for (int i = 0; i < count;
                i++) {
            col[i] = findColById(id);
        }

        return col;

    }

    public String findColById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT COLUMN_NAME from COL_LOOK_UP WHERE ID =?";
//        System.out.println("<<<<<<<FINDER_QUERY in findColById: "+FINDER_QUERY+"  ID: "+id);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return col;
    }

    public String findColDescById(
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT DESCRIPTION from COL_LOOK_UP WHERE ID =?";
//        System.out.println("<<<<<<<FINDER_QUERY in findColDescById: "+FINDER_QUERY+"   ID: "+id);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch COL_LOOK_UP->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return col;
    }

    public ArrayList findApprovalByColumn(
            String query, String[] selCol, String para) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Approval app = null;
        ArrayList collection = new ArrayList();
        int count = selCol.length;

        String[] temp = new String[count];



        String FINDER_QUERY = "SELECT " + query + " from am_raisentry_post";

        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            //ps.setString(1, "Y");
            rs =
                    ps.executeQuery();

            //  String query = "Select +query+ from am_raisentry_post  ";

            while (rs.next()) {
                for (int i = 0; i < count;
                        i++) {
                    temp[i] = rs.getString(selCol[i]);
                }

                collection.add(temp);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return collection;
    }

    public String findAssetWorkbookColDescById(
            String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT DESCRIPTION from ASSETWORKBOOK_EXPCOL WHERE ID =?";
 //       System.out.println("<<<<<<<FINDER_QUERY in findAssetWorkbookColDescById: "+FINDER_QUERY+"   ID: "+id);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch ASSETWORKBOOK_EXPCOL->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return col;
    }

    public String findAssetWorkbookColById(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";

        String FINDER_QUERY = "SELECT COLUMN_NAME from ASSETWORKBOOK_EXPCOL WHERE ID =?";
//        System.out.println("<<<<<<<FINDER_QUERY in findAssetWorkbookColById: "+FINDER_QUERY+"  ID: "+id);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
            rs =
                    ps.executeQuery();

            while (rs.next()) {

                col = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch ASSETWORKBOOK_EXPCOL->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
  //      System.out.println("<<<<<======Column in findAssetWorkbookColById: "+col);
        return col;
    }

    public boolean AssetWorkbookColumn(String id,String dType) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String col = "";
        boolean status = false;

        String FINDER_QUERY = "SELECT DATA_TYPE from ASSETWORKBOOK_EXPCOL WHERE ID =? AND DATA_TYPE =?";
//        System.out.println("<<<<<<<FINDER_QUERY in isNumericColumn: "+FINDER_QUERY+"    ID: "+id+"   dType: "+dType);
        try {
            con = getConnection("legendPlus");
            ps =
                    con.prepareStatement(FINDER_QUERY);

            ps.setString(1, id);
             ps.setString(2, dType); 
            rs = ps.executeQuery();

            while (rs.next()) {

               status = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot get isNumeric->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
 //       System.out.println("<<<<<======Column in findAssetWorkbookColById: "+col);
        return status;
    }

public java.util.ArrayList getWorkBookSqlRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	//	String query = " SELECT * FROM am_gb_workbookupdate ";
//	Transaction transaction = null;
//     System.out.println("====query in getWorkBookSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String strAssetUser = rs.getString("Asset_User");
				String strbarCode = rs.getString("BAR_CODE");
				String StrassetId = rs.getString("ASSET_ID");
				String StroldassetId = rs.getString("OLD_ASSET_ID");
				String StrassetCode = rs.getString("ASSET_CODE");
				String branchId = rs.getString("BRANCH_ID"); 
				String categoryId = rs.getString("CATEGORY_ID"); 
				String batchId = rs.getString("BATCH_ID");
				String comments = rs.getString("COMMENTS"); 
				String sighted = rs.getString("ASSETSIGHTED");
				String function = rs.getString("ASSETFUNCTION"); 
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double improvcostPrice = rs.getDouble("IMPROV_COST");
				double improvmonthldepr = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvaccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvnbv = rs.getDouble("IMPROV_NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
                String sbuCode = rs.getString("SBU_CODE");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");                
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                String purchaseDate = rs.getString("Date_purchased");
                String assetMake = rs.getString("Asset_Make");
                String registrationNo = rs.getString("Registration_No");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setBarCode(strbarCode);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setOldassetId(StroldassetId);
				newTransaction.setAssetCode(StrassetCode);
				newTransaction.setIntegrifyId(batchId);
				newTransaction.setBranchCode(branchId);
				newTransaction.setCategoryCode(categoryId);
//				System.out.println("<<<<<<strDescription: "+strDescription+"   strAssetUser: "+strAssetUser+"   strbarCode: "+strbarCode+"  StrassetId: "+StrassetId);
				newTransaction.setAssetMake(sighted);
				newTransaction.setAssetModel(function);
				newTransaction.setAssetMaintenance(comments);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setAssetModel(assetModel);
//				System.out.println("<<<<<<sighted: "+sighted+"  function: "+function+"   comments: "+comments+"  assetMake: "+assetMake+"   assetModel: "+assetModel);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvcostPrice);
				newTransaction.setImprovmonthlyDep(improvmonthldepr);
				newTransaction.setImprovaccumDep(improvaccumDep);
				newTransaction.setImprovnbv(improvnbv);
				newTransaction.setTotalnbv(totalnbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setEngineNo(AssetEngineNo);
				newTransaction.setSerialNo(AssetSerialNo);
				newTransaction.setSpare1(spare1);
				newTransaction.setSpare2(spare2);
				newTransaction.setSpare1(spare3);
				newTransaction.setSpare2(spare4);
				newTransaction.setSpare1(spare5);
				newTransaction.setSpare2(spare6);				
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setRegistrationNo(registrationNo);
				_list.add(newTransaction);
//				System.out.println("<<<<<<_list.size(): "+_list.size());
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

public java.util.ArrayList getBulkAssetProofSqlRecords(String query,String branch_Id,String prooftranId)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	//	String query = " SELECT * FROM am_gb_workbookupdate ";
//	Transaction transaction = null;
//     System.out.println("===query in getBulkAssetProofSqlRec=ords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	PreparedStatement s = null;
	try {
//		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("BRANCH_ID") && query.contains("BATCH_ID")){
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, prooftranId);
	          }
	          if(query.contains("BATCH_ID")){
	        	  s.setString(1, branch_Id);
	          }
			rs = s.executeQuery();
			
			while (rs.next())
			   {		
//				 System.out.println("===========-----> ");
				String strDescription = rs.getString("Description");
//				 System.out.println("===strDescription-----> "+strDescription);
				String strAssetUser = rs.getString("Asset_User");
				String strbarCode = rs.getString("BAR_CODE");
				String StrassetId = rs.getString("ASSET_ID");
				String StroldassetId = rs.getString("OLD_ASSET_ID");
				String StrassetCode = rs.getString("ASSET_CODE");
				String branchId = rs.getString("BRANCH_ID"); 
				String categoryId = rs.getString("CATEGORY_ID"); 
				String deptId = rs.getString("DEPT_ID");
				String sectionId = rs.getString("SECTION_ID");
				String subcat = rs.getString("SUB_CATEGORY_ID");
				String lpo = rs.getString("LPO");
				String location = rs.getString("LOCATION");
				String state = rs.getString("STATE");
				String batchId = rs.getString("BATCH_ID");
				String comments = rs.getString("COMMENTS"); 
				String sighted = rs.getString("ASSETSIGHTED");
				String function = rs.getString("ASSETFUNCTION"); 
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double improvcostPrice = rs.getDouble("IMPROV_COST");
				double improvmonthldepr = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvaccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvnbv = rs.getDouble("IMPROV_NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
                String sbuCode = rs.getString("SBU_CODE");
                String spare1 = rs.getString("SPARE_1");
                String spare2 = rs.getString("SPARE_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");                
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                String purchaseDate = rs.getString("Date_purchased");
                String assetMake = rs.getString("Asset_Make");
                String registrationNo = rs.getString("Registration_No");
                String maintainBy = rs.getString("Asset_Maintenance");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setBarCode(strbarCode);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setOldassetId(StroldassetId);
				newTransaction.setAssetCode(StrassetCode); 
				newTransaction.setIntegrifyId(batchId);
				newTransaction.setBranchCode(branchId);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSubcategoryCode(subcat); 
				newTransaction.setAssetsighted(sighted);
				newTransaction.setAssetfunction(function);
				newTransaction.setComments(comments);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setAssetModel(assetModel);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvcostPrice);
				newTransaction.setImprovmonthlyDep(improvmonthldepr);
				newTransaction.setImprovaccumDep(improvaccumDep);
				newTransaction.setImprovnbv(improvnbv);
				newTransaction.setTotalnbv(totalnbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setEngineNo(AssetEngineNo);
				newTransaction.setSerialNo(AssetSerialNo);
				newTransaction.setAssetMaintenance(maintainBy); 
				newTransaction.setSpare1(spare1);
				newTransaction.setSpare2(spare2);
				newTransaction.setSpare3(spare3);
				newTransaction.setSpare4(spare4);
				newTransaction.setSpare5(spare5);
				newTransaction.setSpare6(spare6);				
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setRegistrationNo(registrationNo);
				newTransaction.setDeptCode(deptId);
				newTransaction.setSectionCode(sectionId);
				newTransaction.setLpo(lpo);
				newTransaction.setLocation(location);
				newTransaction.setState(state);
				_list.add(newTransaction);
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

public java.util.ArrayList getBulkAssetProofforFinconSqlRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	//	String query = " SELECT * FROM am_gb_workbookupdate ";
//	Transaction transaction = null;
//     System.out.println("====query in getBulkAssetProofforFinconSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String strAssetUser = rs.getString("Asset_User");
				String strbarCode = rs.getString("BAR_CODE");
				String StrassetId = rs.getString("ASSET_ID");
				String branchId = rs.getString("BRANCH_ID"); 
				String categoryId = rs.getString("CATEGORY_ID"); 
                String newsbuCode = rs.getString("NEW_SBUCODE");
                String oldsbuCode = rs.getString("OLD_SBUCODE");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");                
                String assetModel = rs.getString("Asset_Model");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String AssetEngineNo = rs.getString("Asset_Engine_No");
                String assetMake = rs.getString("Asset_Make");
                String registrationNo = rs.getString("Registration_No");
				String deptId = rs.getString("Dept_ID"); 
				String subcatId = rs.getString("SUB_CATEGORY_ID");
				String location = rs.getString("Location");
				String sectionId = rs.getString("Section_id");
				String state = rs.getString("State");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setBarCode(strbarCode);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setDeptCode(deptId);
				newTransaction.setSubcategoryCode(subcatId);
				newTransaction.setLocation(location);
				newTransaction.setSectionCode(sectionId);
				newTransaction.setState(state);
				newTransaction.setBranchCode(branchId);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setAssetModel(assetModel);
				newTransaction.setEngineNo(AssetEngineNo);
				newTransaction.setSerialNo(AssetSerialNo);
				newTransaction.setSpare1(spare1);
				newTransaction.setSpare2(spare2);
				newTransaction.setSpare1(spare3);
				newTransaction.setSpare2(spare4);
				newTransaction.setSpare1(spare5);
				newTransaction.setSpare2(spare6);				
				newTransaction.setNewsbuCode(newsbuCode);
				newTransaction.setOldsbuCode(oldsbuCode);
				
				newTransaction.setRegistrationNo(registrationNo);
				_list.add(newTransaction);
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


public java.util.ArrayList getUploadUserSqlRecords(String query,String user_Id)
{
	java.util.ArrayList _list = new java.util.ArrayList();
//     System.out.println("====query in getUploadUserSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 
	 legend.admin.objects.User user = null;
	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
                String userId = rs.getString("User_Id");
                String userName = rs.getString("User_Name");
                String fullName = rs.getString("Full_Name");
                String legacySysId = rs.getString("Legacy_Sys_Id");
                String Class = rs.getString("Class");
                String branch = rs.getString("Branch");
                String password = rs.getString("Password");
                String phoneNo = rs.getString("Phone_No");
                String isSupervisor = "N";
                String mustChangePwd = "Y";
                String loginStatus = "0";
                String userStatus = "ACTIVE";
                String createDate = rs.getString("Create_Date");
//                String passwordExpiry = rs.getString("Password_Expiry");
//                String loginDate = rs.getString("Login_Date");
//                String loginSystem = rs.getString("Login_System");
                String fleetAdmin = "N";
                String email = rs.getString("email");
                String branchCode = rs.getString("Branch");
                String pwdChanged = "N";
                String deptCode = rs.getString("dept_code");
                String regionCode = rs.getString("region_code");
                String zoneCode = rs.getString("zone_code");
                String regionRestrict = rs.getString("REGION_RESTRICTION");
                String deptRestriction = "Y";
                String branchRestriction = rs.getString("branch_restriction");
                String zoneRestriction = rs.getString("ZONE_RESTRICTION");
                String facilityAdmin = "N";
                String storeAdmin = "N";
                String tokenRequired = rs.getString("token_required");
//                String zoneRestrict = rs.getString("zone_restriction");
//                String facilityAdmin = rs.getString("Facility_Admin");
//                String storeAdmin = rs.getString("Store_Admin");
                
                user = new legend.admin.objects.User();
                user.setUserId(userId);
                user.setUserName(userName);
                user.setUserFullName(fullName);
                user.setLegacySystemId(legacySysId);
                user.setUserClass(Class);
                user.setBranch(branch);
                user.setPassword(password);
                user.setPhoneNo(phoneNo);
                user.setIsSupervisor(isSupervisor);
                user.setMustChangePwd(mustChangePwd);
                user.setLoginStatus(loginStatus);
                user.setCreatedBy(user_Id);
                user.setCreateDate(createDate);
//                user.setPwdExpiry(passwordExpiry);
//                user.setLastLogindate(loginDate);
//                user.setLoginSystem(loginSystem);
                user.setTokenRequire(tokenRequired);
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setUserStatus(userStatus);
                user.setPwdChanged(pwdChanged);
                user.setDeptCode(deptCode);
                user.setRegionCode(regionCode);
                user.setZoneCode(zoneCode);
                user.setBranchRestrict(branchRestriction);
                user.setRegionRestrict(regionRestrict);
                user.setDeptRestrict(deptRestriction);
                user.setZoneRestrict(zoneRestriction);
                user.setIsFacilityAdministrator(facilityAdmin);
                user.setIsStoreAdministrator(storeAdmin);
//                user.setZoneRestrict(zoneRestrict);
//                user.setIsFacilityAdministrator(facilityAdmin);
//                user.setIsStoreAdministrator(storeAdmin);
                _list.add(user);				
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

public java.util.ArrayList getFleetRenewalSqlRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	//	String query = " SELECT * FROM am_gb_workbookupdate ";
//	Transaction transaction = null;
//     System.out.println("====query in getFleetRenewalSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String strAssetUser = rs.getString("Asset_User");
				String strbarCode = rs.getString("BAR_CODE");
				String StrassetId = rs.getString("ASSET_ID");
				String comments = rs.getString("COMMENTS"); 
                String sbuCode = rs.getString("SBU_CODE");
                String assetMake = rs.getString("Asset_Make");
                String registrationNo = rs.getString("Registration_No");
                String insuranceCode = rs.getString("INSURANCE_CODE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(strAssetUser);				
				newTransaction.setBarCode(strbarCode);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setAssetMaintenance(comments);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setRegistrationNo(registrationNo);
				newTransaction.setVendorCode(insuranceCode);
				_list.add(newTransaction);
//				System.out.println("<<<<<<_list.size(): "+_list.size());
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


public java.util.ArrayList getexportexcelSqlRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	//System.out.println("====eschool Server Date-----> "+date);
	date = date.substring(0, 10);
	String finacleTransId= null;
	//	String query = " SELECT * FROM am_gb_workbookupdate ";
//	Transaction transaction = null;
//     System.out.println("====query in getFleetRenewalSqlRecords-----> "+query);
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {	
                String groupId = rs.getString("group_id");
                String description = rs.getString("Description");
                double costPrice = rs.getDouble("Cost_Price");
                String postingDate = rs.getString("Posting_Date");
                double amountRemaining = rs.getDouble("AMOUNT_REM");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(description);
				newTransaction.setAssetId(groupId);	
				newTransaction.setCostPrice(costPrice);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setVatValue(amountRemaining);
				_list.add(newTransaction);
//				System.out.println("<<<<<<_list.size(): "+_list.size());
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

public java.util.ArrayList getDepreciationChargeRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())  
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchId = rs.getString("BRANCH_CODE"); 
				String categoryId = rs.getString("CATEGORY_CODE");  
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double improvcostPrice = rs.getDouble("IMPROV_COST");
				double improvmonthlyDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvaccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double nbv = rs.getDouble("NBV");
				double improvnbv = rs.getDouble("IMPROV_NBV");
				double yearlyCharge = rs.getDouble("YEARDEPRCHARGES");
                String sbuCode = rs.getString("SBU_CODE");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String depDate = rs.getString("DEP_DATE");
                String dependDate = rs.getString("Dep_End_Date");
//                System.out.println("=====dependDate: "+dependDate);
                if(dependDate == null){dependDate = date;}
                String purchaseDate = rs.getString("Date_purchased");
                int totalLife = rs.getInt("TOTAL_LIFE"); 
                int improvtotalLife = rs.getInt("IMPROV_TOTALLIFE"); 
                int calculateLife = rs.getInt("CALC_LIFESPAN");
                double depRate = rs.getDouble("DEP_RATE");
                int remainLife = rs.getInt("Remaining_Life");
                int improveRemainLife = rs.getInt("IMPROV_REMAINLIFE");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchId);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setImprovmonthlyDep(improvmonthlyDep);
				newTransaction.setImprovcostPrice(improvcostPrice);
				newTransaction.setImprovaccumDep(improvaccumDep);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setDependDate(dependDate);
				newTransaction.setDeprChargeToDate(yearlyCharge);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovnbv(improvnbv);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setUsefullife(totalLife);
				newTransaction.setImprovtotallife(improvtotalLife);
				newTransaction.setCalcLifeSpan(calculateLife);
				newTransaction.setDepRate(depRate);
				newTransaction.setRemainLife(remainLife);  
				newTransaction.setImproveRemainLife(improveRemainLife);
				_list.add(newTransaction);
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

public java.util.ArrayList getAssetManagementRecords(String query,String branch_Id,String categoryCode)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0")) {
				 System.out.println("NO Selection in getAssetManagementRecords: ");
			 }else{
//	          if(query.contains("a.branch_id") && !query.contains("a.CATEGORY_CODE")){
	          if(!branch_Id.equals("0") && categoryCode.equals("0")) {	        	  
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	          if(branch_Id.equals("0") && !categoryCode.equals("0")) {	        	  
	        	  s.setString(1, categoryCode);
	          }
//	          if(query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	          if(!branch_Id.equals("0") && !categoryCode.equals("0")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String strOldAssetId = rs.getString("Old_Asset_Id");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String category_Code = rs.getString("category_code"); 
				String categoryName = rs.getString("category_name");
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldassetId(strOldAssetId);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryCode(category_Code);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptCode(deptName);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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


public java.util.ArrayList getConsolidatedAssetManagementRecords(String query,String branch_Id,String categoryCode)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0")) {
				 System.out.println("NO Selection in getAssetManagementRecords: ");
			 }else{
//	          if(query.contains("a.branch_id") && !query.contains("a.CATEGORY_CODE")){
	          if(!branch_Id.equals("0") && categoryCode.equals("0")) {	        	  
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	          }
//	          if(!query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	          if(branch_Id.equals("0") && !categoryCode.equals("0")) {	        	  
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, categoryCode);
	          }
//	          if(query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	          if(!branch_Id.equals("0") && !categoryCode.equals("0")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String strOldAssetId = rs.getString("Old_Asset_Id");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryId = rs.getString("category_name"); 
				String categoryName = rs.getString("category_name");
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldassetId(strOldAssetId);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptCode(deptName);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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


public java.util.ArrayList getDepreciationChargesExportRecords(String query,String chargeYear,String reportDate,String branch_Id,String categoryId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && !query.contains("BRANCH_CODE") && !query.contains("CATEGORY_CODE")){
	        	  System.out.println("First Query");
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("BRANCH_CODE") && !query.contains("CATEGORY_CODE")){
	        	  System.out.println("Second Query");
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	        	  s.setString(3, branch_Id);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("CATEGORY_CODE") && !query.contains("BRANCH_CODE")){
	        	  System.out.println("Third Query");
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	        	  s.setString(3, categoryId);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("BRANCH_CODE") && query.contains("CATEGORY_CODE")){
	        	  System.out.println("Fourth Query");
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, categoryId);
	          }
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
//				String branchName = rs.getString("BRANCH_NAME");
//				String category_name = rs.getString("category_name");  
				String categoryCode = rs.getString("CATEGORY_CODE");
//				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("YearDeprCharges");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String dep_endDate = rs.getString("DEP_END_DATE");
                String depDate = rs.getString("DEP_DATE");
                int calclifeSpan = rs.getInt("CALC_LIFESPAN");
                int totalLife = rs.getInt("TOTAL_LIFE");
                int improveTotalLife = rs.getInt("IMPROV_TOTALLIFE");
                int remainLife = rs.getInt("REMAINING_LIFE");
                double depRate = rs.getDouble("DEP_RATE");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryCode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
//				newTransaction.setDeptCode(deptName);
				newTransaction.setDependDate(dep_endDate);
				newTransaction.setDepDate(depDate);
				newTransaction.setDepRate(depRate);
				newTransaction.setCalcLifeSpan(calclifeSpan);
				newTransaction.setUsefullife(totalLife);
				newTransaction.setImprovtotallife(improveTotalLife);
				newTransaction.setRemainLife(remainLife);
				_list.add(newTransaction);
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



public java.util.ArrayList getPostedTransactionExportRecords(String query,String userName,String startDate,String endDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
	
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
	          if(query.contains("a.User_Name") && query.contains("a.transaction_date")){
	        	  s.setString(1, userName);
	        	  s.setString(2, startDate);
	        	  s.setString(3, endDate);
	          }
	          if(query.contains("a.transaction_date") && !query.contains("a.User_Name") ){
	        	  s.setString(1, startDate);
	        	  s.setString(2, endDate);
	          }
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				double amount = rs.getDouble("AMOUNT");
				String debitAccount = rs.getString("DEBITACCOUNT");
				String debitAccountName = rs.getString("DEBITACCOUNTNAME");
				String creditAccount = rs.getString("CREDITACCOUNT");
				String creditAccountName = rs.getString("CREDITACCOUNTNAME");
				String initiatorId = rs.getString("INITIATORID");
				String supervisorId = rs.getString("SUPERVISORID");
				String postedBy = rs.getString("FULL_NAME");
                String transactionDate = rs.getString("TRANSACTION_DATE");
                String response = rs.getString("Error_Description");
                String action = rs.getString("page1");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setDescription(strDescription);
				newTransaction.setAmount(amount);
				newTransaction.setDebitAccount(debitAccount);
				newTransaction.setCreditAccount(creditAccount);
				newTransaction.setDebitAccountName(debitAccountName);
				newTransaction.setCreditAccountName(creditAccountName);
				newTransaction.setPostedBy(postedBy);
				newTransaction.setTransDate(transactionDate);
				newTransaction.setInitiatorId(initiatorId);
				newTransaction.setSupervisorId(supervisorId);
				newTransaction.setResponse(response);
				newTransaction.setAction(action);
				
				_list.add(newTransaction);
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


public java.util.ArrayList getUserListExportRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null;
	Statement s = null; 

	try {
		    c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next())
			   {				
				String userId = rs.getString("User_Id");
				String userFullName = rs.getString("Full_Name");
				String branchId = rs.getString("Branch");
				String branchRestrict = rs.getString("branch_restriction");
				String createDate = rs.getString("Create_Date");
				String createdBy = rs.getString("UserId");
				String loginStatus = rs.getString("Login_Status");
				String lastLogindate = rs.getString("login_date");
				String userStatus = rs.getString("User_Status");
				String deptId = rs.getString("dept_code");
				String apprvLimit = rs.getString("Approval_Limit");
                String email = rs.getString("email");
//                String lastLoginDate = rs.getString("Error_Description");
                String userloginName = rs.getString("User_Name");
                String classId = rs.getString("class");
                String approvedBy = rs.getString("Supervisor_Id");
				User users = new User();
				users.setUserId(userId);
				users.setUserFullName(userFullName);
				users.setUserName(userloginName);
				users.setBranch(branchId);
				users.setCreateDate(createDate);
				users.setCreatedBy(createdBy);
				users.setBranchRestrict(branchRestrict);
				users.setLoginStatus(loginStatus);
				users.setLastLogindate(lastLogindate);
				users.setApprvLimit(apprvLimit);
				users.setEmail(email);
				users.setDeptCode(deptId);
				users.setUserClass(classId);
				users.setUserStatus(userStatus);
				users.setLoginStatus(loginStatus);
				users.setIsSupervisor(approvedBy);
				
				_list.add(users); 
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


public java.util.ArrayList findStockIssuanceDisplayByBatchId(String tranId)
{
	java.util.ArrayList list = new java.util.ArrayList();

    String assetFilter = " and asset_id in (";
    String selectQuery =
            "select * from am_gb_bulkStocktransfer where batch_id=? ";

    Connection con = null;
    PreparedStatement ps = null;
//    System.out.println("=======>selectQuery: "+selectQuery);
    ResultSet rs = null;

	try {
        con = getConnection();
        ps = con.prepareStatement(selectQuery);
        ps.setInt(1, Integer.parseInt(tranId));
        rs = ps.executeQuery();
			StockRecordsBean aset = null;
			while (rs.next())
			   {				
	            String id = rs.getString("ASSET_ID");
	            String registrationNo = rs.getString("REGISTRATION_NO");
	            String description = rs.getString("DESCRIPTION");
	            String oldbranchId = rs.getString("oldbranch_id");
	            String olddeptId = rs.getString("olddept_id");
	            String oldsbucode = rs.getString("oldSBU_CODE");
	            String oldsectionId = rs.getString("oldsection_id");
	            String oldassetuser = rs.getString("oldAsset_User");
	            String newassetId = rs.getString("NEW_ASSET_ID");
	            String newbranchId = rs.getString("newbranch_id");
	            String newdeptId = rs.getString("newdept_id");
	            String newsbucode = rs.getString("newSBU_CODE");
	            String newsectionId = rs.getString("newsection_id");
	            String newassetuser = rs.getString("newAsset_User");
	            String batchId = rs.getString("Batch_id");
	            String transferbyId = rs.getString("Transferby_id");
	            int assetCode =  rs.getInt("ASSET_CODE");
	            int qtyIssued = rs.getInt("QUANTITY_ISSUED");
	            int issuanceStatus = rs.getInt("ISSUANCE_STATUS");
//	            System.out.println("<<<<<<=======issuanceStatus: "+issuanceStatus);
	             aset = new StockRecordsBean();
	            aset.setAsset_id(id);
	            aset.setDescription(description);
	            aset.setRegistration_no(registrationNo);
	            aset.setBranch_id(oldbranchId);
	            aset.setDepartment_id(olddeptId);
	            aset.setSbu_code(oldsbucode);
	            aset.setSection_id(oldsectionId);
	            aset.setUser(oldassetuser);
	            aset.setNewbranch_id(newbranchId);
	            aset.setNewdepartment_id(newdeptId);
	            aset.setNewsbu_code(newsbucode);
	            aset.setNewsection_id(newsectionId);
	            aset.setNewuser(newassetuser);
	            aset.setNewasset_id(newassetId);
	            aset.setAssetCode(assetCode);
	            aset.setQtyIssued(qtyIssued);
	            aset.setIssuanceStatus(issuanceStatus);
	            list.add(aset);

			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(con, ps, rs);
					}
	return list;
}

public java.util.ArrayList getFinacleAssetUploadExportRecords(String query,String branchCode, String groupid)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getFinacleAssetUploadExportRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("AM_ASSET.GROUP_ID") && query.contains("GROUP_ID") && query.contains("d.branch_code")){
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, branchCode);
	        	  s.setString(4, groupid);
	        	  s.setString(5, branchCode);
	        	  s.setString(6, groupid);
	          }
	          
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String barcode = rs.getString("DR_CR");
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				String E = rs.getString("E");
                String F = rs.getString("F");
                String accountNo = rs.getString("DR_ACCT");
                String H = rs.getString("H");
                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBarCode(barcode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(E);
				newTransaction.setAssetCode(F);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setCategoryCode(H);
				newTransaction.setIntegrifyId(I);
				_list.add(newTransaction);
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

public java.util.ArrayList getLegacyAssetGrpPostRecords(String query,String branchCode, String groupid,String subjectTovat,String subjectTowhTax,int paramCount,String tranType,String assetType,String type)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
	System.out.println("paramCount in getLegacyAssetGrpPostRecords: "+paramCount);
//	System.out.println("query in getLegacyAssetGrpPostRecords: "+query);
	System.out.println("subjectTovat in getLegacyAssetGrpPostRecords: "+subjectTovat);
	System.out.println("subjectTowhTax in getLegacyAssetGrpPostRecords: "+subjectTowhTax);
//	System.out.println("======>>>query.contains(\"Revalue_ID\")=======: "+query.contains("Revalue_ID"));
//	System.out.println("======>>>am_asset_improvement_Upload.Revalue_ID=======: "+query.contains("am_asset_improvement_Upload.Revalue_ID"));
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("S") && paramCount==22){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 22A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
	        	  s.setString(11, groupid);
	        	  s.setString(12, groupid);
	        	  s.setString(13, groupid);
	        	  s.setString(14, groupid);
	        	  s.setString(15, groupid);
	        	  s.setString(16, groupid);
	        	  s.setString(17, groupid);
	        	  s.setString(18, groupid);
	        	  s.setString(19, groupid);
	        	  s.setString(20, groupid);
	        	  s.setString(21, groupid);
	        	  s.setString(22, groupid);     	  
	          }
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("F") && paramCount==22){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 22B==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
	        	  s.setString(11, groupid);
	        	  s.setString(12, groupid);
	        	  s.setString(13, groupid);
	        	  s.setString(14, groupid);
	        	  s.setString(15, groupid);
	        	  s.setString(16, groupid);
	        	  s.setString(17, groupid);
	        	  s.setString(18, groupid);
	        	  s.setString(19, groupid);
	        	  s.setString(20, groupid);
	        	  s.setString(21, groupid);
	        	  s.setString(22, groupid); 
	          }
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("S") && paramCount==16){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 16A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
	        	  s.setString(11, groupid);
	        	  s.setString(12, groupid);
	        	  s.setString(13, groupid);
	        	  s.setString(14, groupid);
	        	  s.setString(15, groupid);
	        	  s.setString(16, groupid);
	          }	   
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("F") && paramCount==16){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 16B==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
	        	  s.setString(11, groupid);
	        	  s.setString(12, groupid);
	        	  s.setString(13, groupid);
	        	  s.setString(14, groupid);
	        	  s.setString(15, groupid);
	        	  s.setString(16, groupid);
	          }	 
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==10){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 10==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
//	        	  System.out.println("s.GROUP_ID in getLegacyAssetGrpPostRecords: "+query.contains("s.GROUP_ID"));
//	        	  System.out.println("group_id in getLegacyAssetGrpPostRecords: "+query.contains("group_id"));
	          }	 
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==16){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 16==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
	        	  s.setString(9, groupid);
	        	  s.setString(10, groupid);
	        	  s.setString(11, groupid);
	        	  s.setString(12, groupid);
	        	  s.setString(13, groupid);
	        	  s.setString(14, groupid);
	        	  s.setString(15, groupid);
	        	  s.setString(16, groupid);
	          }	
	          if(query.contains("GROUP_ID") && query.contains("d.branch_code") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("S") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 6A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	          }		          
	          if(query.contains("GROUP_ID") && query.contains("d.branch_code") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("F") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 6B==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	          }		
	          if(query.contains("GROUP_ID") && query.contains("d.branch_code") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("S") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 6C==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	          }			          
	          if(query.contains("GROUP_ID") && query.contains("d.branch_code") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("F") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 6D==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	          }		
	          if(query.contains("GROUP_ID") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 6E==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	          }		
	          if(query.contains("e.Batch_id") && !query.contains("GROUP_ID") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==6){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 4A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	          }		     
	          if(query.contains("e.Batch_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==5){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 4A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	          }
	          if(query.contains("GROUP_ID") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==2){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2A==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	          }	
	          if(query.contains("GROUP_ID")  && query.contains("d.branch_code") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("S") && paramCount==2){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, branchCode);
	        	  s.setString(4, groupid);
	        	  s.setString(5, branchCode);
	        	  s.setString(6, groupid);
	          }		          
	          if(query.contains("Revalue_ID") && query.contains("am_asset_improvement_Upload.Revalue_ID") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N") && paramCount==3){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	          }	
	          if(assetType.equals("TRANSFERPOSTING") && type.equals("BULK")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }		
	          if(assetType.equals("ASSETCREATION") && type.equals("GROUP")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETCREATION") && type.equals("UPLOAD")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETCREATION") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETTRANSFER") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }	
	          if(assetType.equals("ASSETDISPOSAL") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords Disposal Single==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	        	  s.setString(5, tranType);
	        	  s.setString(6, tranType);
	          }
	          if(assetType.equals("ASSETCREATIONUNCAP") && type.equals("GROUP")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETCREATIONUNCAP") && type.equals("UPLOAD")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETCREATIONUNCAP") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETIMPROVEMENT") && type.equals("UPLOAD")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }
	          if(assetType.equals("ASSETIMPROVEMENT") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("CLOSEDASSET") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }	
	          if(assetType.equals("ASSETRECLASS") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }	
	          if(assetType.equals("ASSETACCELERATION") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("ASSETDISPOSAL") && type.equals("UPLOAD")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	        	  s.setString(5, tranType);
	        	  s.setString(6, tranType);
	          }	
//Uncapitalised Commences
	          if(assetType.equals("ASSETUNCAPIMPROVEMENT") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 2BUncap==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	          }	
	          if(assetType.equals("CLOSEDUNCAPASSET") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords 4AUncap==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }		
	          if(assetType.equals("ASSETDISPOSALUNCAP") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords Uncap Disposal Single 6A==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	        	  s.setString(5, tranType);
	        	  s.setString(6, tranType); 
	          }	
	          if(assetType.equals("ASSETTRANSFERUNCAP") && type.equals("SINGLE")){
	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords Uncap Transfer 4B==== ");
	        	  s.setString(1, tranType);
	        	  s.setString(2, tranType);
	        	  s.setString(3, tranType);
	        	  s.setString(4, tranType);
	          }	          
//Uncapitalised End	          
	          
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String accountNo = rs.getString("accountNo");
				String recType = rs.getString("recType");
				double costPrice = rs.getDouble("costPrice");
				String transType = rs.getString("transType");
                String GROUP_ID = rs.getString("GROUP_ID");
                String branch_Code = rs.getString("BRANCH_CODE");
//                String I = rs.getString("I");
                if(costPrice > 0) {
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBarCode(recType);
				newTransaction.setSbuCode(transType);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(GROUP_ID);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAssetType(transType);
				newTransaction.setBranchCode(branch_Code);
                
				_list.add(newTransaction);
			   }
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

public java.util.ArrayList getLegacyAssetGrpPostRecords(String query,String branchCode, String groupid)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getLegacyAssetGrpPostRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
//	          if(query.contains("GROUP_ID") && query.contains("d.branch_code")){
//	        	  System.out.println("=====query in getLegacyAssetGrpPostRecords==== ");
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid);
//	          }		 
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branch_Code = rs.getString("BRANCH_CODE");
				String accountNo = rs.getString("accountNo");
				String recType = rs.getString("recType");
				double costPrice = rs.getDouble("costPrice");
				String transType = rs.getString("transType");
                String GROUP_ID = rs.getString("GROUP_ID");
//                String branchCode = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branch_Code);
				newTransaction.setBarCode(recType);
				newTransaction.setSbuCode(transType);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(GROUP_ID);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAssetType(transType);
				_list.add(newTransaction);
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



public java.util.ArrayList getFinacleAssetImprovedUploadExportRecords(String query,String branchCode, String groupid)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getFinacleAssetUploadExportRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("Revalue_ID") && query.contains("Revalue_ID") && query.contains("d.branch_code")){
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, branchCode);
	        	  s.setString(4, groupid);
	        	  s.setString(5, branchCode);
	        	  s.setString(6, groupid);
	          }
	          
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String barcode = rs.getString("DR_CR");
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				String E = rs.getString("E");
                String F = rs.getString("F");
                String accountNo = rs.getString("DR_ACCT");
                String H = rs.getString("H");
                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBarCode(barcode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(E);
				newTransaction.setAssetCode(F);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setCategoryCode(H);
				newTransaction.setIntegrifyId(I);
				_list.add(newTransaction);
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


public java.util.ArrayList getFinacleUncapAssetUploadExportRecords(String query,String branchCode, String groupid)
{
//	System.out.println("======branchCode in getFinacleUncapAssetUploadExportRecords: "+branchCode+"    groupid: "+groupid);
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getFinacleUncapAssetUploadExportRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("AM_ASSET_UNCAPITALIZED.GROUP_ID") && query.contains("GROUP_ID") && query.contains("d.branch_code")){
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, branchCode);
	        	  s.setString(4, groupid);
	        	  s.setString(5, branchCode);
	        	  s.setString(6, groupid);
	          }
	          
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String barcode = rs.getString("DR_CR");
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				String E = rs.getString("E");
                String F = rs.getString("F");
                String accountNo = rs.getString("DR_ACCT");
                String H = rs.getString("H");
                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBarCode(barcode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(E);
				newTransaction.setAssetCode(F);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setCategoryCode(H);
				newTransaction.setIntegrifyId(I);
				_list.add(newTransaction);
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

public java.util.ArrayList getAssetListVerificationRecords(String query,String branch_Id,String categoryCode)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//	System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0")) {
//				 System.out.println("NO Selection in getAssetManagementRecords: ");
			 }else{
//				 System.out.println("Selection in getAssetManagementRecords branch_Id: "+query.contains("a.branch_id")+"   categoryCode: "+query.contains("a.CATEGORY_CODE"));
	          if(!branch_Id.equals("0")  && categoryCode.equals("0")){
	      //  	  System.out.println("getAssetManagementRecords with Branch but No Category: ");
	        	  s.setString(1, branch_Id);
	          }
	          if(branch_Id.equals("0") && !categoryCode.equals("0")){
	        //	  System.out.println("getAssetManagementRecords with No Branch but Category Only: ");
	        	  s.setString(1, categoryCode);
	          }
	          if(!branch_Id.equals("0") && !categoryCode.equals("0")){
	        //	  System.out.println("getAssetManagementRecords with Branch and Category: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryId = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String barCode = rs.getString("BAR_CODE");
                String spare1 = rs.getString("Spare_1");
                String serialNo = rs.getString("Asset_Serial_No");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptCode(deptName);
				newTransaction.setBarCode(barCode);
				newTransaction.setSpare1(spare1);
				newTransaction.setAssetSerialNo(serialNo);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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
/*
public java.util.ArrayList getAssetRegisterRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
				 } 				 
					 if(!assetId.equals("0") && branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && !ToDate.equals("") && FromDate.equals("") && ToDate.equals("")) {
						 System.out.println("Asset Id Selection in getAssetRegisterRecords: ");
		        	  s.setString(1, assetId);
		          }				 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double depRate = rs.getDouble("Dep_Rate");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
//				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String oldassetId = rs.getString("OLD_ASSET_ID");
                String barCode = rs.getString("BAR_CODE");
                String deptCode = rs.getString("DEPT_CODE");
                String assetUser = rs.getString("ASSET_USER");
                String assetCode = rs.getString("ASSET_CODE");
                String categoryId = rs.getString("category_Id");  
                String branchId = rs.getString("BRANCH_ID");
                String registrationNo= rs.getString("Registration_No");
                String assetMaintenance = rs.getString("Asset_Maintenance");
                String assetMake = rs.getString("Asset_Make");
                String assetModel = rs.getString("Asset_Model");
                String supplierName = rs.getString("Supplier_Name");
                String lpo = rs.getString("LPO");
                String vendorAcct = rs.getString("Vendor_AC");
                String location = rs.getString("location");
                String state = rs.getString("STATE");
                String sectionName = rs.getString("Section_Name");
                String vendorName = rs.getString("Vendor_Name");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");
                String depr_endDate = rs.getString("Dep_End_Date");
                String postingDate = rs.getString("Posting_Date");
                String sbuName = rs.getString("Sbu_name");
                String sectionCode = rs.getString("SECTION_CODE");
                int remainingLife = rs.getInt("Remaining_Life");
                String mail1 = rs.getString("Email1");
                String mail2 = rs.getString("Email2");
                String dateDisposed = rs.getString("Date_Disposed");
                String stateName = rs.getString("state_name");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setOldassetId(oldassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setSectionCode(sectionCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDependDate(depr_endDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setSectionName(sectionName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setSectionName(sectionName);
				newTransaction.setBarCode(barCode);
				newTransaction.setDeptCode(deptCode);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setAssetCode(assetCode);
				newTransaction.setBranchId(branchId);
				newTransaction.setAssetMaintenance(assetMaintenance);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setAssetModel(assetModel);
				newTransaction.setSupplierName(supplierName);
				newTransaction.setLpo(lpo);
				newTransaction.setVendorAC(vendorAcct);
				newTransaction.setVendorName(vendorName);
				newTransaction.setLocation(location);
				newTransaction.setSpare1(spare1);
				newTransaction.setSpare2(spare2);
				newTransaction.setSpare3(spare3);
				newTransaction.setSpare4(spare4);
				newTransaction.setSpare5(spare5);
				newTransaction.setSpare6(spare6);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setSbuName(sbuName);
				newTransaction.setRemainLife(remainingLife);
				newTransaction.setRegistrationNo(registrationNo);
				newTransaction.setDepRate(depRate);
				newTransaction.setSerialNo(AssetSerialNo);
				
//				newTransaction.setDependDate(depDate);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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
*/
public java.util.ArrayList getAssetRegisterRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
				 } 				 
				 if(!assetId.equals("0") && branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && !ToDate.equals("") && FromDate.equals("") && ToDate.equals("")) {
//					 System.out.println("Asset Id Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, assetId);
		          }	
					 
				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double depRate = rs.getDouble("Dep_Rate");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
//				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String oldassetId = rs.getString("OLD_ASSET_ID");
                String barCode = rs.getString("BAR_CODE");
                String deptCode = rs.getString("DEPT_CODE");
                String assetUser = rs.getString("ASSET_USER");
                String assetCode = rs.getString("ASSET_CODE");
                String categoryId = rs.getString("category_Id");  
                String branchId = rs.getString("BRANCH_ID");
                String registrationNo= rs.getString("Registration_No");
                String assetMaintenance = rs.getString("Asset_Maintenance");
                String assetMake = rs.getString("Asset_Make");
                String assetModel = rs.getString("Asset_Model");
                String supplierName = rs.getString("Supplier_Name");
                String lpo = rs.getString("LPO");
                String vendorAcct = rs.getString("Vendor_AC");
                String location = rs.getString("location");
                String state = rs.getString("STATE");
                String sectionName = rs.getString("Section_Name");
                String vendorName = rs.getString("Vendor_Name");
                String spare1 = rs.getString("Spare_1");
                String spare2 = rs.getString("Spare_2");
                String spare3 = rs.getString("Spare_3");
                String spare4 = rs.getString("Spare_4");
                String spare5 = rs.getString("Spare_5");
                String spare6 = rs.getString("Spare_6");
                String depr_endDate = rs.getString("Dep_End_Date");
                String postingDate = rs.getString("Posting_Date");
                String sbuName = rs.getString("Sbu_name");
                String sectionCode = rs.getString("SECTION_CODE");
                int remainingLife = rs.getInt("Remaining_Life");
                String mail1 = rs.getString("Email1");
                String mail2 = rs.getString("Email2");
                String dateDisposed = rs.getString("Date_Disposed");
                String stateName = rs.getString("state_name");
                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String assetStatus = rs.getString("Asset_Status");
                
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setOldassetId(oldassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setSectionCode(sectionCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDependDate(depr_endDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setSectionName(sectionName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setSectionName(sectionName);
				newTransaction.setBarCode(barCode);
				newTransaction.setDeptCode(deptCode);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setAssetCode(assetCode);
				newTransaction.setBranchId(branchId);
				newTransaction.setAssetMaintenance(assetMaintenance);
				newTransaction.setAssetMake(assetMake);
				newTransaction.setAssetModel(assetModel);
				newTransaction.setSupplierName(supplierName);
				newTransaction.setLpo(lpo);
				newTransaction.setVendorAC(vendorAcct);
				newTransaction.setVendorName(vendorName);
				newTransaction.setLocation(location);
				newTransaction.setSpare1(spare1);
				newTransaction.setSpare2(spare2);
				newTransaction.setSpare3(spare3);
				newTransaction.setSpare4(spare4);
				newTransaction.setSpare5(spare5);
				newTransaction.setSpare6(spare6);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setSbuName(sbuName);
				newTransaction.setRemainLife(remainingLife);
				newTransaction.setRegistrationNo(registrationNo);
				newTransaction.setDepRate(depRate);
				newTransaction.setSerialNo(AssetSerialNo);
				newTransaction.setAssetStatus(assetStatus);
				
//				newTransaction.setDependDate(depDate);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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
/*public java.util.ArrayList getAssetDepreciationComparismRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
System.out.println("query in getAssetDepreciationComparismRecords: "+query);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String categoryCode = rs.getString("CATEGORY_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUM_DEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLY_DEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double prevMonthlyDep = rs.getDouble("Prev_Monthly_Dep");
				double prevAccumDep = rs.getDouble("Prev_Accum_Dep");
				double prevCostPrice = rs.getDouble("Prev_Cost_Price");
				double prevNBV = rs.getDouble("Prev_NBV");
				double monthlyDifference = rs.getDouble("Monthly_Difference");
				double accumDifference = rs.getDouble("Accum_Difference");
				double nbvDifference = rs.getDouble("NBV_Difference");
				double prevIMPROVMonthlyDep = rs.getDouble("Prev_IMPROV_Monthly_Dep");
				double prevIMPROVAccumDep = rs.getDouble("Prev_IMPROV_Accum_Dep");
				double prevIMPROVCostPrice = rs.getDouble("Prev_IMPROV_Cost");
				double prevIMPROVNBV = rs.getDouble("Prev_IMPROV_NBV");
				double prevMonthlyDifference = rs.getDouble("Prev_Monthly_Difference");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryCode);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice); 
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setPrevMonthlyDep(prevMonthlyDep);
				newTransaction.setPrevAccumDep(prevAccumDep);
				newTransaction.setPrevCostPrice(prevCostPrice);
				newTransaction.setPrevNBV(prevNBV);
				newTransaction.setMonthlyDifference(monthlyDifference);
				newTransaction.setAccumDifference(accumDifference);
				newTransaction.setNbvDifference(nbvDifference);
				newTransaction.setPrevIMPROVMonthlyDep(prevIMPROVMonthlyDep);
				newTransaction.setPrevIMPROVAccumDep(prevIMPROVAccumDep);
				newTransaction.setPrevIMPROVCostPrice(prevIMPROVCostPrice);
				newTransaction.setPrevIMPROVNBV(prevIMPROVNBV);
				newTransaction.setPrevMonthlyDifference(prevMonthlyDifference);

				_list.add(newTransaction);
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
*/

public java.util.ArrayList getAssetDepreciationComparismRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetDepreciationComparismRecords: "+query);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String categoryCode = rs.getString("CATEGORY_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_Accum_Dep");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLY_DEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double prevMonthlyDep = rs.getDouble("Prev_Monthly_Dep");
				double prevAccumDep = rs.getDouble("Prev_Accum_Dep");
				double prevCostPrice = rs.getDouble("Prev_Cost_Price");
				double prevNBV = rs.getDouble("Prev_NBV");
				double monthlyDifference = rs.getDouble("Monthly_Difference");
				double accumDifference = rs.getDouble("Accum_Difference");
				double nbvDifference = rs.getDouble("NBV_Difference");
				double prevIMPROVMonthlyDep = rs.getDouble("Prev_IMPROV_Monthly_Dep");
				double prevIMPROVAccumDep = rs.getDouble("Prev_IMPROV_Accum_Dep");
				double prevIMPROVCostPrice = rs.getDouble("Prev_IMPROV_Cost");
				double prevIMPROVNBV = rs.getDouble("Prev_IMPROV_NBV");
				double prevMonthlyDifference = rs.getDouble("Prev_Monthly_Difference");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryCode);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice); 
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setPrevMonthlyDep(prevMonthlyDep);
				newTransaction.setPrevAccumDep(prevAccumDep);
				newTransaction.setPrevCostPrice(prevCostPrice);
				newTransaction.setPrevNBV(prevNBV);
				newTransaction.setMonthlyDifference(monthlyDifference);
				newTransaction.setAccumDifference(accumDifference);
				newTransaction.setNbvDifference(nbvDifference);
				newTransaction.setPrevIMPROVMonthlyDep(prevIMPROVMonthlyDep);
				newTransaction.setPrevIMPROVAccumDep(prevIMPROVAccumDep);
				newTransaction.setPrevIMPROVCostPrice(prevIMPROVCostPrice);
				newTransaction.setPrevIMPROVNBV(prevIMPROVNBV);
				newTransaction.setPrevMonthlyDifference(prevMonthlyDifference);

				_list.add(newTransaction);
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

public java.util.ArrayList getFinacleDisposalUploadExportRecords(String query,String groupid)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getFinacleAssetUploadExportRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("am_asset_disposal_Upload.disposal_ID")){
	        	  s.setString(1, groupid);
	        	  s.setString(2, groupid);
	        	  s.setString(3, groupid);
	        	  s.setString(4, groupid);
	        	  s.setString(5, groupid);
	        	  s.setString(6, groupid);
	        	  s.setString(7, groupid);
	        	  s.setString(8, groupid); 
	          }
	          
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String barcode = rs.getString("DR_CR");
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				String E = rs.getString("E");
                String F = rs.getString("F");
                String accountNo = rs.getString("DR_ACCT");
                String H = rs.getString("H");
                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBarCode(barcode);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(E);
				newTransaction.setAssetCode(F);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setCategoryCode(H);
				newTransaction.setIntegrifyId(I);
				_list.add(newTransaction);
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

public java.util.ArrayList getAssetAdditionRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	        	//  s.setString(3, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, categoryCode);
	        	//  s.setString(3, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, categoryCode);
	        	  s.setString(4, branch_Id);
//	        	  s.setString(5, categoryCode);
//	        	  s.setString(6, branch_Id);
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
//	        	  s.setString(7, FromDate);
//	        	  s.setString(8, ToDate);
//	        	  s.setString(9, FromDate);
//	        	  s.setString(10, ToDate);
	          }	  
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, branch_Id);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
//	        	  s.setString(7, branch_Id);
//	        	  s.setString(8, FromDate);
//	        	  s.setString(9, ToDate);
//	        	  s.setString(10, branch_Id);
//	        	  s.setString(11, FromDate);
//	        	  s.setString(12, ToDate);
//	        	  s.setString(13, branch_Id);
//	        	  s.setString(14, FromDate);
//	        	  s.setString(15, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch Category and Date Selection in getAssetRegisterRecords: ");
//				System.out.println("Branch Id: "+branch_Id+"    categoryCode: "+categoryCode+"    FromDate: "+FromDate+"    ToDate: "+ToDate);
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, branch_Id);
	        	  s.setString(6, categoryCode);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
//	        	  s.setString(9, branch_Id);
//	        	  s.setString(10, categoryCode);
//	        	  s.setString(11, FromDate);
//	        	  s.setString(12, ToDate);
//	        	  s.setString(13, branch_Id);
//	        	  s.setString(14, categoryCode);
//	        	  s.setString(15, FromDate);
//	        	  s.setString(16, ToDate);
//	        	  s.setString(17, branch_Id);
//	        	  s.setString(18, categoryCode);
//	        	  s.setString(19, FromDate);
//	        	  s.setString(20, ToDate);
				 } 				 
				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, categoryCode);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
//	        	  s.setString(7, categoryCode);
//	        	  s.setString(8, FromDate);
//	        	  s.setString(9, ToDate);
//	        	  s.setString(10, categoryCode);
//	        	  s.setString(11, FromDate);
//	        	  s.setString(12, ToDate);
//	        	  s.setString(13, categoryCode);
//	        	  s.setString(14, FromDate);
//	        	  s.setString(15, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double amount = rs.getDouble("AMOUNT");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String depr_endDate = rs.getString("Dep_End_Date");
                String postingDate = rs.getString("transaction_date");
                String finaclePostedDate = rs.getString("Finacle_Posted_Date");
                String tranType = rs.getString("page1");
                String additionDate = rs.getString("Posting_Date");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
//				newTransaction.setOldassetId(oldassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
//				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDependDate(depr_endDate);
				newTransaction.setDeptName(deptName);
//				newTransaction.setSectionName(sectionName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setSpare1(finaclePostedDate);
				newTransaction.setSpare1(additionDate);
				newTransaction.setTranType(tranType);
				newTransaction.setAmount(amount);
				_list.add(newTransaction);
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

public java.util.ArrayList getFleetMaintenanceRecords(String query,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
	PreparedStatement s = null;
//System.out.println("query in getFleetMaintenanceRecords: "+query+"  assetId: "+assetId+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());

			 if(assetId.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getFleetMaintenanceRecords: ");
			 }else{
				 if(!assetId.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("assetId and Date Selection in getFleetMaintenanceRecords: ");
	        	  s.setString(1, assetId);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 				 
				 if(!assetId.equals("") && FromDate.equals("") && ToDate.equals("")) {
//					 System.out.println("Asset Id Selection only in getFleetMaintenanceRecords: ");
	        	  s.setString(1, assetId);
		          }	
					 
				 if(!FromDate.equals("") && !ToDate.equals("") && assetId.equals("")) {
//				System.out.println("Date Selection in getFleetMaintenanceRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {	
							
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				double costPrice = rs.getDouble("COST_PRICE");
				String repairedDate = rs.getString("repaired_date");
                String lastPmDate = rs.getString("last_Pm_Date");
                String nextPmDate = rs.getString("next_Pm_Date");
                String createDate = rs.getString("CREATE_DATE");
                String VendorName = rs.getString("Vendor_Name");
                String details = rs.getString("details");
                String componentReplaced = rs.getString("component_replaced");
                String transactionDate = rs.getString("transaction_date");
                String registrationNo= rs.getString("Registration_No");
                
                FleetManatainanceRecord fleetTrans = new FleetManatainanceRecord();
                fleetTrans.setAssetId(StrassetId);
                fleetTrans.setLastPerformedDate(lastPmDate);
                fleetTrans.setComponentReplaced(componentReplaced);
                fleetTrans.setDateOfRepair(repairedDate);
                fleetTrans.setDetails(details);
                fleetTrans.setNextPerformedDate(nextPmDate);
                fleetTrans.setNextNotificationDate(createDate);
                fleetTrans.setDescription(strDescription);
                fleetTrans.setFirstNotificationDate(transactionDate);
                fleetTrans.setCost(costPrice);
				fleetTrans.setRegistrationNo(registrationNo);
				
				_list.add(fleetTrans);
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

public java.util.ArrayList getFacilityMaintenanceRecords(String query,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
	PreparedStatement s = null;
//System.out.println("query in getFacilityMaintenanceRecords: "+query+"  assetId: "+assetId+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());

			 if(assetId.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getFleetMaintenanceRecords: ");
			 }else{
				 if(!assetId.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("assetId and Date Selection in getFleetMaintenanceRecords: ");
	        	  s.setString(1, assetId);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 				 
				 if(!assetId.equals("") && FromDate.equals("") && ToDate.equals("")) {
//					 System.out.println("Asset Id Selection only in getFleetMaintenanceRecords: ");
	        	  s.setString(1, assetId);
		          }	
					 
				 if(!FromDate.equals("") && !ToDate.equals("") && assetId.equals("")) {
//				System.out.println("Date Selection in getFleetMaintenanceRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {	
							
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				double costPrice = rs.getDouble("COST_PRICE");
				String repairedDate = rs.getString("repaired_date");
                String lastPmDate = rs.getString("last_Pm_Date");
                String nextPmDate = rs.getString("next_Pm_Date");
                String createDate = rs.getString("CREATE_DATE");
                String VendorName = rs.getString("Vendor_Name");
                String details = rs.getString("details");
                String componentReplaced = rs.getString("component_replaced");
                String transactionDate = rs.getString("transaction_date");
                String registrationNo= rs.getString("Registration_No");
                
                FleetManatainanceRecord fleetTrans = new FleetManatainanceRecord();
                fleetTrans.setAssetId(StrassetId);
                fleetTrans.setLastPerformedDate(lastPmDate);
                fleetTrans.setComponentReplaced(componentReplaced);
                fleetTrans.setDateOfRepair(repairedDate);
                fleetTrans.setDetails(details);
                fleetTrans.setNextPerformedDate(nextPmDate);
                fleetTrans.setNextNotificationDate(createDate);
                fleetTrans.setDescription(strDescription);
                fleetTrans.setFirstNotificationDate(transactionDate);
                fleetTrans.setCost(costPrice);
				fleetTrans.setRegistrationNo(registrationNo);
				
				_list.add(fleetTrans);
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


public java.util.ArrayList getFacilityPPMRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	
	 FMppmAllocation scheduleList = null;
     ArrayList ppmList = new ArrayList();
     
	Connection c = null;
	ResultSet rs = null; 
	PreparedStatement s = null;
//	System.out.println("query in getFacilityPPMRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());

	          rs = s.executeQuery();  
			while (rs.next())
			   {	
							
                String id = rs.getString("ID");
                String transId = rs.getString("transId");
                String branchCode = rs.getString("BRANCH_CODE");
                String categoryCode = rs.getString("CATEGORY_CODE");
                String subCategoryCode = rs.getString("SUB_CATEGORY_CODE");
                String groupId = rs.getString("GROUP_ID");
                String vendorCode = rs.getString("VENDOR_CODE");
                String description = rs.getString("DESCRIPTION");
                String lastserviceDate = rs.getString("LASTSERVICE_DATE");
                String q1DueDate = rs.getString("Q1_DUE_DATE");
                String q2DueDate = rs.getString("Q2_DUE_DATE");
                String q3DueDate = rs.getString("Q3_DUE_DATE");
                String q4DueDate = rs.getString("Q4_DUE_DATE");
                String q1Status = rs.getString("Q1_STATUS"); 
                String q2Status = rs.getString("Q2_STATUS"); 
                String q3Status = rs.getString("Q3_STATUS"); 
                String q4Status = rs.getString("Q4_STATUS"); 
                String type = rs.getString("TYPE");   
                String zoneCode = rs.getString("ZONE_CODE");   
                String zoneName = rs.getString("Zone_Name");   
//                String status = rs.getString("STATUS"); 
                String postingDate = rs.getString("POSTING_DATE"); 
                
                scheduleList = new FMppmAllocation(id, transId, branchCode, categoryCode, 
                		subCategoryCode, vendorCode, 
                        description,  lastserviceDate,q1DueDate,  q2DueDate, q3DueDate, q4DueDate,
                        q1Status, q2Status, q3Status, q4Status, type,
                        "", postingDate);

				_list.add(scheduleList);
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

public java.util.ArrayList getReportDisposalBulkUploadExportRecords(String query,String groupid)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetDepreciationComparismRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
	          if(query.contains("convert(varchar,am_asset_disposal_Upload.DISPOSAL_ID)")){
	        	  s.setString(1, groupid);
	          }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String StrassetId = rs.getString("AM_ASSET_Asset_id");
				String branchCode = rs.getString("AM_ASSET_BRANCH_CODE");
				String DeptName = rs.getString("am_ad_department_Dept_name");
				String disposalDate = rs.getString("am_asset_disposal_Upload_Disposal_Date");
				double costPrice = rs.getDouble("am_asset_disposal_Upload_cost_price");
				double disposalPrice = rs.getDouble("am_asset_disposal_Upload_disposalAmount");
				String assetUser = rs.getString("AM_ASSET_ASSET_USER");
				String batchId = rs.getString("am_asset_disposal_Upload_Disposal_ID");
				String QRCode = rs.getString("AM_ASSET_BAR_CODE");
				double profitLoss = rs.getDouble("am_asset_disposal_Upload_PROFIT_LOSS");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDeptName(DeptName);
				newTransaction.setDepDate(disposalDate);
				newTransaction.setCostPrice(costPrice); 
				newTransaction.setImprovcostPrice(disposalPrice);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setIntegrifyId(batchId);
				newTransaction.setBarCode(QRCode);
				newTransaction.setTotalCost(profitLoss);

				_list.add(newTransaction);
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

public java.util.ArrayList getScriptExecutionRecords(String query,String branch_Id, String categoryCode, String fromDate,String toDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(fromDate.equals("0") && toDate.equals("0")) {
				 System.out.println("NO Selection in getScriptExecutionRecords: ");
			 }else{
	          if(query.contains("a.branch_id") && !query.contains("a.CATEGORY_CODE")){
	        	  s.setString(1, branch_Id);
	          }
	          if(!query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	        	  s.setString(1, categoryCode);
	          }
	          if(query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryId = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptCode(deptName);
//				newTransaction.setDependDate(depDate);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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


public java.util.ArrayList getAssetMgtSummaryRecords(String query,String branch_Id,String categoryCode)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0")) {
				 System.out.println("NO Selection in getAssetMgtSummaryRecords: ");
			 }else{
//	          if(query.contains("a.branch_id") && !query.contains("a.CATEGORY_CODE")){
				 if(!branch_Id.equals("0")  && categoryCode.equals("0")){ 
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	         if(branch_Id.equals("0")  && !categoryCode.equals("0")){
	        	  s.setString(1, categoryCode);
	          }
//	          if(query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	         if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {		
//				String strDescription = rs.getString("Description");
//				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String deptName = rs.getString("DEPT_NAME");
				String categoryName = rs.getString("category_name"); 
				String CategoryCode = rs.getString("CATEGORY_CODE"); 
//				String deptName = rs.getString("Dept_name");  
				double costPrice = rs.getDouble("Total_Cost");
				double monthlyDepr = rs.getDouble("Total_monthly_dep");
				double accumDepr = rs.getDouble("Total_Accum_dep");
				double nbv = rs.getDouble("TOTAL_NBV");
				double totalnbv = rs.getDouble("FIANL_TOTAL_NBV");
				double improvCostPrice = rs.getDouble("TOTAL_IMPROV_COST");
				double improvAccumDep = rs.getDouble("TOTAL_IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("TOTAL_IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("TOTAL_IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Final_Cost");
//				String purchaseDate = rs.getString("Date_purchased");
//                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                int itemNo = rs.getInt("TOTAL");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setBranchName(branchName);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDeptCode(deptCode);
				newTransaction.setDeptName(deptName);
				newTransaction.setCategoryCode(CategoryCode);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setSbuCode(sbuCode);
//				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
//				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr); 
//				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
//				newTransaction.setDatepurchased(purchaseDate);
//				newTransaction.setDeptCode(deptName);
				newTransaction.setNoofitems(itemNo);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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

public java.util.ArrayList getConsolidatedAssetMgtSummaryRecords(String query,String branch_Id,String categoryCode)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0")) {
				 System.out.println("NO Selection in getAssetMgtSummaryRecords: ");
			 }else{
//	          if(query.contains("a.branch_id") && !query.contains("a.CATEGORY_CODE")){
				 if(!branch_Id.equals("0")  && categoryCode.equals("0")){ 
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	          }
//	          if(!query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	         if(branch_Id.equals("0")  && !categoryCode.equals("0")){
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, categoryCode);
	          }
//	          if(query.contains("a.branch_id") && query.contains("a.CATEGORY_CODE")){
	         if(!branch_Id.equals("0")  && !categoryCode.equals("0")){
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, categoryCode);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {		
//				String strDescription = rs.getString("Description");
//				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String deptName = rs.getString("DEPT_NAME");
				String categoryName = rs.getString("category_name"); 
				String CategoryCode = rs.getString("CATEGORY_CODE"); 
//				String deptName = rs.getString("Dept_name");  
				double costPrice = rs.getDouble("Total_Cost");
				double monthlyDepr = rs.getDouble("Total_monthly_dep");
				double accumDepr = rs.getDouble("Total_Accum_dep");
				double nbv = rs.getDouble("TOTAL_NBV");
				double totalnbv = rs.getDouble("FIANL_TOTAL_NBV");
				double improvCostPrice = rs.getDouble("TOTAL_IMPROV_COST");
				double improvAccumDep = rs.getDouble("TOTAL_IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("TOTAL_IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("TOTAL_IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Final_Cost");
//				String purchaseDate = rs.getString("Date_purchased");
//                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                int itemNo = rs.getInt("TOTAL");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setBranchName(branchName);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDeptCode(deptCode);
				newTransaction.setDeptName(deptName);
				newTransaction.setCategoryCode(CategoryCode);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setSbuCode(sbuCode);
//				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
//				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr); 
//				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
//				newTransaction.setDatepurchased(purchaseDate);
//				newTransaction.setDeptCode(deptName);
				newTransaction.setNoofitems(itemNo);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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

public java.util.ArrayList getDepreciationPostingRecords(String query,String narration)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getDepreciationPostingRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());

			rs = s.executeQuery();
			while (rs.next())
			   {				
//				String strDescription = rs.getString("NARRATION");
				String StrassetId = rs.getString("BRANCH_CODE");
				String branchCode = rs.getString("BRANCH_CODE");
				String accountNo = rs.getString("ACCOUNT");
				String recType = rs.getString("recType");
				double costPrice = rs.getDouble("AMOUNT");
				String transType = rs.getString("transType");
                String GROUP_ID = rs.getString("category_name");
//                String branchCode = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBarCode(recType);
				newTransaction.setSbuCode(transType);
				newTransaction.setDescription(narration);
				newTransaction.setAssetUser(GROUP_ID);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAssetType(transType);
				_list.add(newTransaction);
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


public java.util.ArrayList getBatchExceptionPostingRecords(String query,String batchId,String userId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;

	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
			 s.setString(1, batchId);
			 s.setString(2, userId);
			rs = s.executeQuery();
			while (rs.next())
			   {		
				
				
				String strId = rs.getString("ID");
				String strSerialNo = rs.getString("SERIAL_NO");
				String strBatchNo = rs.getString("BATCH_NO");
				String StrCurrentNo = rs.getString("CURRENCY_NO");
				double StrAmount = rs.getDouble("AMOUNT");
				String StrErrorCode = rs.getString("ERROR_CODE");
				String StrErrorDescription = rs.getString("ERROR_DESCRIPTION");
				String StrTransType = rs.getString("TRANS_TYPE");
				String StrAccountNo = rs.getString("ACCOUNT_NO");
				String StrTransDescription= rs.getString("TRANS_DESCRIPTION");
				String StrStatus = rs.getString("STATUS");
				String StrDisplayStatus = rs.getString("DISPLAY_STATUS");
                String StrUserId = rs.getString("USER_ID");
                String StrCreateDate = rs.getString("CREATE_DATE");
                String StrGroupId = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(strId);
				newTransaction.setBarCode(strSerialNo);
				newTransaction.setSbuCode(strBatchNo);
				newTransaction.setDescription(StrCurrentNo);
				newTransaction.setAmount(StrAmount);
				newTransaction.setAssetType(StrErrorCode);
				newTransaction.setAssetUser(StrErrorDescription);
				newTransaction.setTranType(StrTransType);
				newTransaction.setVendorAC(StrAccountNo);
				newTransaction.setAction(StrTransDescription);
				newTransaction.setAssetStatus(StrStatus);
				newTransaction.setAssetfunction(StrDisplayStatus);
				newTransaction.setUserID(StrUserId);
				newTransaction.setTransDate(StrCreateDate);
				newTransaction.setInitiatorId(StrGroupId);
				_list.add(newTransaction);
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

public java.util.ArrayList getLegacyExceptionpPostRecords(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null; 
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
//	System.out.println("query in getLegacyAssetGrpPostRecords: "+query);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query); 
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("DECRIPTION");
				String strId = rs.getString("ID");
				String StrassetId = rs.getString("ID_GROUP_ID");
				String branch_Code = rs.getString("BRANCH_CODE");
				String accountNo = rs.getString("ACCOUNT_NO");
				String recType = rs.getString("recType");
				double costPrice = rs.getDouble("COST_PRICE");
				String transType = rs.getString("TRANSTYPE");
                String GROUP_ID = rs.getString("GROUP_ID");
//                String branchCode = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setIntegrifyId(strId);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branch_Code);
				newTransaction.setBarCode(recType);
				newTransaction.setSbuCode(transType);
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetUser(GROUP_ID);
				newTransaction.setVendorAC(accountNo);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAssetType(transType);
				_list.add(newTransaction);
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


public java.util.ArrayList getAssetDisposalReportRecords(String query,String branch_Id,String categoryCode,String disposalReason,String fromDate,String endDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//	System.out.println("Query: " + query);
System.out.println("query in getAssetDisposalReportRecords: "+" branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  fromDate: "+fromDate+"   endDate: "+endDate+"   disposalReason: "+disposalReason);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && fromDate.equals("") && endDate.equals("") && disposalReason.equals("0")) {
				// System.out.println("NO Selection in getAssetDisposalReportRecords: ");
			 }
			 else{
				 
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")) {
					// System.out.println("Only Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
					
				 }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")) {
					// System.out.println("branch and Date Selection in getAssetDisposalReportRecords: ");
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
					  s.setString(3, branch_Id);
					 
	          }
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")) {
				//	 System.out.println("Category and Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
				      s.setString(3, categoryCode);
				     
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && fromDate.equals("") && endDate.equals("") && disposalReason.equals("0")) {
				//	 System.out.println("Category Selection in getAssetDisposalReportRecords: ");
					 s.setString(1, categoryCode);
				     
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("") && disposalReason.equals("0")) {
				//	 System.out.println("Branch, Category and Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
					  s.setString(3, branch_Id);
		        	  s.setString(4, categoryCode);
		        	 
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !disposalReason.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
					// System.out.println("Branch, Disposal Reason and  Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
					  s.setString(3, branch_Id);
		        	  s.setString(4, disposalReason);
		        	
		          }				 
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !disposalReason.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
					// System.out.println("Disposal Reason and  Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, fromDate);
		        	  s.setString(2, endDate);
		        	  s.setString(3, disposalReason);
		        	 
		          }
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !disposalReason.equals("0") && fromDate.equals("") && endDate.equals("")) {
					// System.out.println("Disposal Reason Selection in getAssetDisposalReportRecords: ");
					 s.setString(1, disposalReason);
		        	 
		          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !disposalReason.equals("0") && fromDate.equals("") && endDate.equals("")) {
				//	  System.out.println("Branch, Disposal Reason and  Date Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, branch_Id);
		        	  s.setString(2, disposalReason);
		        	
		          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && disposalReason.equals("0") && fromDate.equals("") && endDate.equals("")) {
				//	 System.out.println("Branch Selection in getAssetDisposalReportRecords: "); 
					 s.setString(1, branch_Id);
		        	  
		          }	
				 
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
			     		
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				double costPrice = rs.getDouble("COST_PRICE");
				double accumDepr = rs.getDouble("ACCUMULATED_DEPRECIATION");
				double depChargeCharge = rs.getDouble("DEPRECIATION_CHARGES");
				double profitAmount = rs.getDouble("PROFIT_AMOUNT");
				double nbv = rs.getDouble("NET_BOOK_VALUE");
				double disposalProceed = rs.getDouble("DISPOSAL_PROCEEDS");
				double disposalAmount = rs.getDouble("DISPOSAL_AMOUNT");
				double lifeSpan = rs.getDouble("LIFE_SPAN");
				String disposalDate = rs.getString("DISPOSAL_DATE");
				String purchaseDate = rs.getString("PURCHASE_DATE");
                String disposeReason = rs.getString("Disposal_Reason");
                String assetUser = rs.getString("ASSET_USER");
                String QrCode = rs.getString("BarCode_VehicleNo");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setDescription(strDescription);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeCharge);
				newTransaction.setLifeSpan(lifeSpan);
				newTransaction.setNbv(nbv);
				newTransaction.setDisposalDate(disposalDate);
				newTransaction.setDisposeReason(disposeReason);
				newTransaction.setDisposalAmount(disposalAmount);				
				newTransaction.setDisposalProceed(disposalProceed);
				newTransaction.setProfitAmount(profitAmount);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setQrCode(QrCode);
			//	System.out.println("StrassetId in getAssetDisposalReportRecords: "+StrassetId+"  branchName: "+branchName+"   categoryName: "+categoryName+"  strDescription: "+strDescription+"   costPrice: "+costPrice+"   accumDepr: "+accumDepr);
				_list.add(newTransaction);
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


public java.util.ArrayList getAuditLogRecords(String query,String branch_Id,String user,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   User: "+user+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAuditLogRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, user);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, user);
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAuditLogRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	          }	
				 if(!branch_Id.equals("0") && user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAuditLogRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch, User and Date Selection in getAuditLogRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, user);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
				 } 				 
					 
				 if(!user.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
				System.out.println("Category  and Date Selection in getAuditLogRecords: ");
	        	  s.setString(1, user);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strmtid = rs.getString("MTID");
				String strtableName = rs.getString("TABLE_NAME");
				String StrcolumnName = rs.getString("COLUMN_NAME");
				String rowId = rs.getString("ROW_ID");
				String prevValue = rs.getString("PREV_VAL");
				String newValue = rs.getString("NEW_VAL");  
				String effDate = rs.getString("EFF_DATE");  
				String loginId = rs.getString("LOGIN_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String auditDate = rs.getString("audit_DATE");
                String fullName = rs.getString("Full_Name");
                String companyName= rs.getString("company_name");
                String roleName = rs.getString("role_name");
                String branchName = rs.getString("BRANCH_NAME");
                String userName = rs.getString("User_Name");
                String ipAddress = rs.getString("IP_ADDRESS");
                String machineName = rs.getString("MACHINE_NAME");  
                String macAddress = rs.getString("MAC_ADDRESS");
                String actPerform= rs.getString("ACTPERFORM");
                String approvedBy = rs.getString("APPROVEDBY");
                String doneOn = rs.getString("DONE_ON");
                
                AuditLog log = new AuditLog();
				log.setMtid(Integer.parseInt(strmtid));
				log.setTableName(strtableName);
				log.setColumnName(StrcolumnName);
				log.setRowId(rowId);
				log.setPrevValue(prevValue);
				log.setNewValue(newValue);
				log.setEffDate(effDate);
				log.setLoginId(loginId);
				log.setBranchCode(branchCode);
				log.setAuditDate(auditDate);
				log.setFullName(fullName);
				log.setCompanyName(companyName);
				log.setRoleName(roleName);
				log.setBranchName(branchName);
				log.setUserName(userName);
				log.setIpAddress(ipAddress);
				log.setMachineName(machineName);
				log.setActionPerformed(actPerform);
				log.setApprovedBy(approvedBy);
				log.setDoneOn(doneOn);

				_list.add(log);
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


public java.util.ArrayList getAssetImprovementReportRecords(String query,String branch_Id,String categoryCode,String fromDate,String endDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetDisposalReportRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  fromDate: "+fromDate+"   endDate: "+endDate+"   disposalReason: "+disposalReason);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
	        	  s.setString(1, fromDate);
	        	  s.setString(2, endDate);
	        	  s.setString(3, fromDate);
	        	  s.setString(4, endDate);	        	  
				 System.out.println("Only Date Selection in getAssetImprovementReportRecords: ");
			 }else{
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && fromDate.equals("") && endDate.equals("")) {
					  s.setString(1, branch_Id);
					  s.setString(2, branch_Id);
					  System.out.println("branch Selection in getAssetImprovementReportRecords: ");
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
					  s.setString(1, branch_Id);
		        	  s.setString(2, fromDate);
		        	  s.setString(3, endDate); 
					  s.setString(4, branch_Id);
		        	  s.setString(5, fromDate);
		        	  s.setString(6, endDate); 
					  System.out.println("branch and Date Selection in getAssetImprovementReportRecords: ");
	          }
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && fromDate.equals("") && endDate.equals("")) {
					  s.setString(1, categoryCode);
					  s.setString(2, categoryCode);	        	  
				      System.out.println("Category Selection in getAssetImprovementReportRecords: ");
	          }
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
					  s.setString(1, categoryCode);
		        	  s.setString(2, fromDate);
		        	  s.setString(3, endDate);
					  s.setString(4, categoryCode);
		        	  s.setString(5, fromDate);
		        	  s.setString(6, endDate);		        	  
				      System.out.println("Category and Date Selection in getAssetImprovementReportRecords: ");
	          }
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
		        	  s.setString(1, fromDate);
		        	  s.setString(2, endDate);
		        	  s.setString(3, fromDate);
		        	  s.setString(4, endDate);		        	  
				      System.out.println("Date Selection in getAssetImprovementReportRecords: ");
	          }
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !fromDate.equals("") && !endDate.equals("")) {
					  s.setString(1, branch_Id);
		        	  s.setString(2, categoryCode);					 
		        	  s.setString(3, fromDate);
		        	  s.setString(4, endDate);
					  s.setString(5, branch_Id);
		        	  s.setString(6, categoryCode);					 
		        	  s.setString(7, fromDate);
		        	  s.setString(8, endDate);		        	  
//		        	  System.out.println("Branch, Category and Date Selection in getAssetImprovementReportRecords: ");
	          }				 
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
			     		
				String StrassetId = rs.getString("ASSET_ID");
				String strDescription = rs.getString("Description");
				String transactionDate = rs.getString("transaction_Date");  
				double amount = rs.getDouble("amount");
				double newCostPrice = rs.getDouble("new_cost_price");
				double calculatedepr = rs.getDouble("calcMonthly_Depr");
				double oldCostPrice = rs.getDouble("old_cost_price");
				double oldnbv = rs.getDouble("old_nbv");
				double newnbv = rs.getDouble("new_nbv");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				String revalueReason = rs.getString("revalue_reason");
				String transType = rs.getString("page1");
                String branchName = rs.getString("branch_name");
                String branch_code = rs.getString("branch_code");
                String categoryName = rs.getString("category_name");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branch_code);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setDescription(strDescription);
				newTransaction.setCostPrice(amount); // Amount
				newTransaction.setAccumDep(newCostPrice);  //newCostPrice
				newTransaction.setDeprChargeToDate(calculatedepr);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setNbv(newnbv);
				newTransaction.setDisposalDate(transactionDate);
				newTransaction.setDisposeReason(revalueReason);
				newTransaction.setDisposalAmount(oldCostPrice);				
				newTransaction.setProfitAmount(oldnbv);
				newTransaction.setAssetType(transType);
//				System.out.println("StrassetId in getAssetImprovementReportRecords: "+StrassetId+"  branchName: "+branchName+"   categoryName: "+categoryName+"  strDescription: "+strDescription+"   costPrice: "+costPrice+"   accumDepr: "+accumDepr);
				_list.add(newTransaction);
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


public java.util.ArrayList getAssetBidListRecords(String query,String StaffId,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//	System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
			 if(StaffId.equals("") && FromDate.equals("") && ToDate.equals("")) {
//				 System.out.println("NO Selection in getAssetManagementRecords: ");
			 }else{
//				 System.out.println("Selection in getAssetManagementRecords branch_Id: "+query.contains("a.branch_id")+"   categoryCode: "+query.contains("a.CATEGORY_CODE"));
	          if(!StaffId.equals("")  && FromDate.equals("") && ToDate.equals("")) {
	      //  	  System.out.println("getAssetManagementRecords with Branch but No Category: ");
	        	  s.setString(1, StaffId);
	          }
	          if(StaffId.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
	        //	  System.out.println("getAssetManagementRecords with No Branch but Category Only: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	          }
	          if(!StaffId.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
	        //	  System.out.println("getAssetManagementRecords with Branch and Category: ");
	        	  s.setString(1, StaffId);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
			 }
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String assetId = rs.getString("ASSET_ID");
				String description = rs.getString("Description");
				String bidderName=rs.getString("BidderName");
				double basePrice = rs.getDouble("BASE_PRICE");
				double bidval = rs.getDouble("Bid_Value");
//				String bidtitle = rs.getString("Bid_Title");
				String crationDate = rs.getString("Creation_Date");
				String bidTag = rs.getString("BID_TAG");
				String staffId = rs.getString("staff_Id");
                
				 Asset aset = new Asset();
				 aset.setAssetId(assetId);
				 aset.setBasePrice(basePrice);
		    	 aset.setDescription(description);
	             aset.setBid(bidval);
//	             aset.setBidtitle(bidtitle);
	             aset.setBiddate(crationDate);
	             aset.setBidTag(bidTag);
	             aset.setStaffId(staffId);
	             aset.setFirstname(bidderName);
				_list.add(aset);
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

public java.util.ArrayList getAssetListRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetListRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getAssetListRecords: ");
	        	  s.setString(1, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getAssetListRecords: ");
	        	  s.setString(1, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getAssetListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	          }

			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
//				String deptName = rs.getString("Dept_name");  
//				String sbuCode = rs.getString("SBU_CODE");
//				double depRate = rs.getDouble("Dep_Rate");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
//				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
//				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
//                String oldassetId = rs.getString("OLD_ASSET_ID");
//                String barCode = rs.getString("BAR_CODE");
//                String deptCode = rs.getString("DEPT_CODE");
//                String assetUser = rs.getString("ASSET_USER");
//                String assetCode = rs.getString("ASSET_CODE");
//                String categoryId = rs.getString("category_Id");  
//                String branchId = rs.getString("BRANCH_ID");
//                String registrationNo= rs.getString("Registration_No");
//                String assetMaintenance = rs.getString("Asset_Maintenance");
//                String assetMake = rs.getString("Asset_Make");
//                String assetModel = rs.getString("Asset_Model");
//                String supplierName = rs.getString("Supplier_Name");
//                String lpo = rs.getString("LPO");
//                String vendorAcct = rs.getString("Vendor_AC");
//                String location = rs.getString("location");
//                String state = rs.getString("STATE");
//                String sectionName = rs.getString("Section_Name");
//                String vendorName = rs.getString("Vendor_Name");
//                String spare1 = rs.getString("Spare_1");
//                String spare2 = rs.getString("Spare_2");
//                String spare3 = rs.getString("Spare_3");
//                String spare4 = rs.getString("Spare_4");
//                String spare5 = rs.getString("Spare_5");
//                String spare6 = rs.getString("Spare_6");
                String depr_endDate = rs.getString("Dep_End_Date");
                String postingDate = rs.getString("Posting_Date");
//                String sbuName = rs.getString("Sbu_name");
//                String sectionCode = rs.getString("SECTION_CODE");
//                int remainingLife = rs.getInt("Remaining_Life");
//                String mail1 = rs.getString("Email1");
//                String mail2 = rs.getString("Email2");
//                String dateDisposed = rs.getString("Date_Disposed");
//                String stateName = rs.getString("state_name");
//                String AssetSerialNo = rs.getString("Asset_Serial_No");
                String assetStatus = rs.getString("Asset_Status");
                
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
//				newTransaction.setOldassetId(oldassetId);
//				newTransaction.setBranchCode(branchCode);
//				newTransaction.setCategoryCode(categoryId);
//				newTransaction.setSbuCode(sbuCode);
//				newTransaction.setSectionCode(sectionCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
//				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDependDate(depr_endDate);
//				newTransaction.setDeptName(deptName);
//				newTransaction.setSectionName(sectionName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setSectionName(sectionName);
//				newTransaction.setBarCode(barCode);
//				newTransaction.setDeptCode(deptCode);
//				newTransaction.setAssetUser(assetUser);
//				newTransaction.setAssetCode(assetCode);
//				newTransaction.setBranchId(branchId);
//				newTransaction.setAssetMaintenance(assetMaintenance);
//				newTransaction.setAssetMake(assetMake);
//				newTransaction.setAssetModel(assetModel);
//				newTransaction.setSupplierName(supplierName);
//				newTransaction.setLpo(lpo);
//				newTransaction.setVendorAC(vendorAcct);
//				newTransaction.setVendorName(vendorName);
//				newTransaction.setLocation(location);
//				newTransaction.setSpare1(spare1);
//				newTransaction.setSpare2(spare2);
//				newTransaction.setSpare3(spare3);
//				newTransaction.setSpare4(spare4);
//				newTransaction.setSpare5(spare5);
//				newTransaction.setSpare6(spare6);
				newTransaction.setPostingDate(postingDate);
//				newTransaction.setSbuName(sbuName);
//				newTransaction.setRemainLife(remainingLife);
//				newTransaction.setRegistrationNo(registrationNo);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setSerialNo(AssetSerialNo);
				newTransaction.setAssetStatus(assetStatus);
				
//				newTransaction.setDependDate(depDate);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
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

//public java.util.ArrayList getAssetRegisterRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
//{
//	java.util.ArrayList _list = new java.util.ArrayList();
//	String date = String.valueOf(dateConvert(new java.util.Date()));
//	date = date.substring(0, 10);
//	String finacleTransId= null;
//	Connection c = null;
//	ResultSet rs = null; 
////	Statement s = null;
//	PreparedStatement s = null;
////System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
//	try {
//		    c = getConnection();
////			s = c.createStatement();
////			rs = s.executeQuery(query);
//			 s = c.prepareStatement(query.toString());
////	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
////	          }
//			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//				 System.out.println("NO Selection in getAssetRegisterRecords: ");
//			 }else{
////	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
//				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//	        	  s.setString(1, branch_Id);
//	          }
////	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
//				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//	        	  s.setString(1, categoryCode);
//	          }
////	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
//				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, categoryCode);
//	          }
////		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
//				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
////					 System.out.println("Date Selection in getAssetRegisterRecords: ");
//	        	  s.setString(1, FromDate);
//	        	  s.setString(2, ToDate);
//	          }	
//				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
////				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
//	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, FromDate);
//	        	  s.setString(3, ToDate);
//				 } 
//				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
////				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
//	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, categoryCode);
//	        	  s.setString(3, FromDate);
//	        	  s.setString(4, ToDate);
//				 } 				 
//				 if(!assetId.equals("0") && branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && !ToDate.equals("") && FromDate.equals("") && ToDate.equals("")) {
////					 System.out.println("Asset Id Selection in getAssetRegisterRecords: ");
//	        	  s.setString(1, assetId);
//		          }	
//					 
//				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
////				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
//	        	  s.setString(1, categoryCode);
//	        	  s.setString(2, FromDate);
//	        	  s.setString(3, ToDate);
//				 } 
//			 }
//	          rs = s.executeQuery();  
//			while (rs.next())
//			   {				
//				String strDescription = rs.getString("Description");
//				String StrassetId = rs.getString("ASSET_ID");
//				String branchCode = rs.getString("BRANCH_CODE");
//				String branchName = rs.getString("BRANCH_NAME");
//				String categoryName = rs.getString("category_name");  
//				String deptName = rs.getString("Dept_name");  
//				String sbuCode = rs.getString("SBU_CODE");
//				double depRate = rs.getDouble("Dep_Rate");
//				double costPrice = rs.getDouble("COST_PRICE");
//				double monthlyDepr = rs.getDouble("Monthly_Dep");
////				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
//				double accumDepr = rs.getDouble("Accum_Dep");
//				double nbv = rs.getDouble("NBV");
//				double totalnbv = rs.getDouble("TOTAL_NBV");
//				double improvCostPrice = rs.getDouble("IMPROV_COST");
//				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
//				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
//				double improvNbv = rs.getDouble("IMPROV_NBV");
//				double totalCostPrice = rs.getDouble("Total_Cost_Price");
//				String purchaseDate = rs.getString("Date_purchased");
//                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
//                String oldassetId = rs.getString("OLD_ASSET_ID");
//                String barCode = rs.getString("BAR_CODE");
//                String deptCode = rs.getString("DEPT_CODE");
//                String assetUser = rs.getString("ASSET_USER");
//                String assetCode = rs.getString("ASSET_CODE");
//                String categoryId = rs.getString("category_Id");  
//                String branchId = rs.getString("BRANCH_ID");
//                String registrationNo= rs.getString("Registration_No");
//                String assetMaintenance = rs.getString("Asset_Maintenance");
//                String assetMake = rs.getString("Asset_Make");
//                String assetModel = rs.getString("Asset_Model");
//                String supplierName = rs.getString("Supplier_Name");
//                String lpo = rs.getString("LPO");
//                String vendorAcct = rs.getString("Vendor_AC");
//                String location = rs.getString("location");
//                String state = rs.getString("STATE");
//                String sectionName = rs.getString("Section_Name");
//                String vendorName = rs.getString("Vendor_Name");
//                String spare1 = rs.getString("Spare_1");
//                String spare2 = rs.getString("Spare_2");
//                String spare3 = rs.getString("Spare_3");
//                String spare4 = rs.getString("Spare_4");
//                String spare5 = rs.getString("Spare_5");
//                String spare6 = rs.getString("Spare_6");
//                String depr_endDate = rs.getString("Dep_End_Date");
//                String postingDate = rs.getString("Posting_Date");
//                String sbuName = rs.getString("Sbu_name");
//                String sectionCode = rs.getString("SECTION_CODE");
//                int remainingLife = rs.getInt("Remaining_Life");
//                String mail1 = rs.getString("Email1");
//                String mail2 = rs.getString("Email2");
//                String dateDisposed = rs.getString("Date_Disposed");
//                String stateName = rs.getString("state_name");
//                String AssetSerialNo = rs.getString("Asset_Serial_No");
//                String assetStatus = rs.getString("Asset_Status");
//                
// //               String depDate = rs.getString("DEP_DATE");
// //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
// //               double depRate = rs.getDouble("DEP_RATE");
//                
//				newAssetTransaction newTransaction = new newAssetTransaction();
//				newTransaction.setAssetId(StrassetId);
//				newTransaction.setOldassetId(oldassetId);
//				newTransaction.setBranchCode(branchCode);
//				newTransaction.setCategoryCode(categoryId);
//				newTransaction.setSbuCode(sbuCode);
//				newTransaction.setSectionCode(sectionCode);
//				newTransaction.setDescription(strDescription);
//				newTransaction.setMonthlyDep(monthlyDepr);
//				newTransaction.setEffectiveDate(EffectiveDate);
//				newTransaction.setImprovmonthlyDep(improvMonthDep);
//				newTransaction.setCostPrice(costPrice);
////				newTransaction.setPostingDate(depDate);
//				newTransaction.setTotalnbv(totalnbv);  
//				newTransaction.setAccumDep(accumDepr);
//				newTransaction.setNbv(nbv);
//				newTransaction.setImprovcostPrice(improvCostPrice);
//				newTransaction.setImprovaccumDep(improvAccumDep);
//				newTransaction.setImprovmonthlyDep(improvMonthDep);
//				newTransaction.setImprovnbv(improvNbv);
//				newTransaction.setTotalCost(totalCostPrice);
//				newTransaction.setDatepurchased(purchaseDate);
//				newTransaction.setDependDate(depr_endDate);
//				newTransaction.setDeptName(deptName);
//				newTransaction.setSectionName(sectionName);
//				newTransaction.setBranchName(branchName);
//				newTransaction.setCategoryName(categoryName);
//				newTransaction.setSectionName(sectionName);
//				newTransaction.setBarCode(barCode);
//				newTransaction.setDeptCode(deptCode);
//				newTransaction.setAssetUser(assetUser);
//				newTransaction.setAssetCode(assetCode);
//				newTransaction.setBranchId(branchId);
//				newTransaction.setAssetMaintenance(assetMaintenance);
//				newTransaction.setAssetMake(assetMake);
//				newTransaction.setAssetModel(assetModel);
//				newTransaction.setSupplierName(supplierName);
//				newTransaction.setLpo(lpo);
//				newTransaction.setVendorAC(vendorAcct);
//				newTransaction.setVendorName(vendorName);
//				newTransaction.setLocation(location);
//				newTransaction.setSpare1(spare1);
//				newTransaction.setSpare2(spare2);
//				newTransaction.setSpare3(spare3);
//				newTransaction.setSpare4(spare4);
//				newTransaction.setSpare5(spare5);
//				newTransaction.setSpare6(spare6);
//				newTransaction.setPostingDate(postingDate);
//				newTransaction.setSbuName(sbuName);
//				newTransaction.setRemainLife(remainingLife);
//				newTransaction.setRegistrationNo(registrationNo);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setSerialNo(AssetSerialNo);
//				newTransaction.setAssetStatus(assetStatus);
//				
////				newTransaction.setDependDate(depDate);
////				newTransaction.setDepRate(depRate);
////				newTransaction.setCalcLifeSpan(calclifeSpan);
//				_list.add(newTransaction);
//			   }
//	 }   
//				 catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					finally
//					{
//						closeConnection(c, s, rs);
//					}
//	return _list;
//}
public java.util.ArrayList getAssetRegisterConcolidatedRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, categoryCode);	        	  
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterConcolidatedRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, branch_Id);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, branch_Id);
	        	  s.setString(6, categoryCode);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);	        	  
				 } 				 				 
				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, categoryCode);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String tranType = rs.getString("transType");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				double depRate = rs.getDouble("Dep_Rate");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				String purchaseDate = rs.getString("Date_purchased");
				String postingDate = rs.getString("Posting_Date");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String depEndDate = rs.getString("Dep_End_Date");
//                String deptCode = rs.getString("DEPT_CODE");
                String assetUser = rs.getString("ASSET_USER");
                String vendorName = rs.getString("VENDOR_NAME");
                String oldAssetId = rs.getString("Old_Asset_Id");
                double Dep_Rate =  rs.getDouble("Dep_Rate");
                int total_life = rs.getInt("Total_Life");
                int remaining_life = rs.getInt("Remaining_Life");
                double improv_cost = rs.getDouble("IMPROV_COST");
                double improv_accum_dep = rs.getDouble("IMPROV_ACCUMDEP");
                double improv_nbv = rs.getDouble("IMPROV_NBV");
                double improv_monthly_dep = rs.getDouble("IMPROV_MONTHLYDEP");
                int improv_remain_life = rs.getInt("IMPROV_REMAINLIFE");
                int improv_useful_life = rs.getInt("IMPROV_USEFULLIFE");
                int improv_total_life = rs.getInt("IMPROV_TOTALLIFE");
                String tag = rs.getString("Tag");

               
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldassetId(oldAssetId);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setDeptCode(deptCode);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setDepRate(depRate);
				newTransaction.setAssetStatus(tranType);
				newTransaction.setVendorName(vendorName);
				newTransaction.setDepRate(depRate);
				newTransaction.setUsefullife(total_life);
				newTransaction.setRemainLife(remaining_life);
				newTransaction.setImprovcostPrice(improv_cost);
				newTransaction.setImprovaccumDep(improv_accum_dep);
				newTransaction.setImprovnbv(improv_nbv);
				newTransaction.setImprovmonthlyDep(improv_monthly_dep);
				newTransaction.setImproveRemainLife(improv_remain_life);
				newTransaction.setNoofitems(improv_useful_life);
				newTransaction.setImprovtotallife(improv_total_life);
				newTransaction.setBarCode(tag);

				_list.add(newTransaction);
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

public java.util.ArrayList getAssetTransactionListRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetTransactionListRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, branch_Id);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	  s.setString(7, branch_Id);
	        	  s.setString(8, FromDate);
	        	  s.setString(9, ToDate);
	        	  s.setString(10, branch_Id);
	        	  s.setString(11, FromDate);
	        	  s.setString(12, ToDate);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, categoryCode);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	  s.setString(7, categoryCode);
	        	  s.setString(8, FromDate);
	        	  s.setString(9, ToDate);
	        	  s.setString(10, categoryCode);
	        	  s.setString(11, FromDate);
	        	  s.setString(12, ToDate);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, branch_Id);
	        	  s.setString(6, categoryCode);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	        	  s.setString(9, branch_Id);
	        	  s.setString(10, categoryCode);
	        	  s.setString(11, FromDate);
	        	  s.setString(12, ToDate);
	        	  s.setString(13, branch_Id);
	        	  s.setString(14, categoryCode);
	        	  s.setString(15, FromDate);
	        	  s.setString(16, ToDate);
	          }

			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String StrassetId = rs.getString("ASSET_ID");
				String strDescription = rs.getString("Description");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("CATEGORY_NAME"); 
				double accumDep = rs.getDouble("ACCUM_DEP");
				double monthlyDep = rs.getDouble("MONTHLY_DEP");
				double costPrice = rs.getDouble("COST_PRICE");
				double nbv = rs.getDouble("NBV");
				String purchaseDate = rs.getString("Date_purchased");
				double depRate = rs.getDouble("Dep_Rate");
				String accumDepLedger  = rs.getString("Accum_Dep_Ledger");
				String depLedger = rs.getString("Dep_Ledger");
				String assetLedger = rs.getString("Asset_Ledger");
				String glAccount = rs.getString("gl_account");
				String depr_endDate = rs.getString("Dep_End_Date");
				String EffectiveDate = rs.getString("EFFECTIVE_DATE");
				String postingDate = rs.getString("Posting_Date");
				String deptName = rs.getString("Dept_name");
				String legacySystemDate = rs.getString("Legacy_Posted_Date");
				double amount = rs.getDouble("amount");
				String transactionDate = rs.getString("transaction_date");
				String page1 = rs.getString("page1");
				String processingDate = rs.getString("processing_date");
				String companyName = rs.getString("company_name");
				String branchCode = rs.getString("BRANCH_CODE");
				String category_Code = rs.getString("CATEGORY_CODE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setDescription(strDescription);
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setAccumDep(accumDep);
				newTransaction.setMonthlyDep(monthlyDep);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(glAccount);
				newTransaction.setDependDate(depr_endDate);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setLegacyPostedDate(legacySystemDate);
				newTransaction.setAmount(amount);
				newTransaction.setTransDate(transactionDate);
				newTransaction.setTranType(page1);
				newTransaction.setProcessingDate(processingDate);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(category_Code);

				_list.add(newTransaction);
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

public java.util.ArrayList getAssetTransferRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
System.out.println("query in getAssetTransferRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetTransactionListRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate); 
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, branch_Id);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	        	 
	          }
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, categoryCode);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, categoryCode);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getAssetTransactionListRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, categoryCode);
	        	  s.setString(6, branch_Id);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String categoryName = rs.getString("am_ad_category_category_name");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");  
				String gl_Account = rs.getString("am_ad_category_gl_account");  
				String asset_Id = rs.getString("am_assetTransfer_asset_id");
				String oldBranchId = rs.getString("am_assetTransfer_OLD_branch_Id");
				String oldDeptId = rs.getString("am_assetTransfer_OLD_dept_ID");
				String transferDate = rs.getString("am_assetTransfer_Transfer_Date");
				String oldAssetUser = rs.getString("am_assetTransfer_OLD_Asset_user");
				String oldSection = rs.getString("am_assetTransfer_OLD_Section");
				String oldBranchCode = rs.getString("am_assetTransfer_OLD_BRANCH_CODE");
				String oldSectionCode = rs.getString("am_assetTransfer_OLD_SECTION_CODE");
				String oldDeptCode = rs.getString("am_assetTransfer_OLD_DEPT_CODE");
				String branchName = rs.getString("am_ad_branch_A_BRANCH_NAME");
				String approvalStatus = rs.getString("am_assetTransfer_approval_status");
				String oldCategoryCode = rs.getString("am_assetTransfer_OLD_CATEGORY_CODE");
				String description = rs.getString("am_assetTransfer_description");
				double costPrice = rs.getDouble("am_assetTransfer_cost_price");
				double accumDepr = rs.getDouble("am_assetTransfer_Accum_Dep");
				double monthlyDepr = rs.getDouble("am_assetTransfer_Monthly_Dep");
				String newBranchCode = rs.getString("am_assetTransfer_NEW_BRANCH_CODE");
                String newSectionCode = rs.getString("am_assetTransfer_NEW_SECTION_CODE");
                String newDeptCode = rs.getString("am_assetTransfer_NEW_DEPT_CODE");
                double nbv = rs.getDouble("am_assetTransfer_NBV");
                String newAssetId = rs.getString("am_assetTransfer_New_Asset_id");
                String branchId = rs.getString("am_ad_branch_A_BRANCH_ID");

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setCategoryName(categoryName);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setAssetId(asset_Id);
				newTransaction.setOldBranchId(oldBranchId);
				newTransaction.setOldDeptId(oldDeptId);
				newTransaction.setTransDate(transferDate);
				newTransaction.setOldAssetUser(oldAssetUser);
				newTransaction.setOldSection(oldSection);
				newTransaction.setOldBranchCode(oldBranchCode);
				newTransaction.setOldSectionCode(oldSectionCode);
				newTransaction.setOldDeptCode(oldDeptCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setApprovalStatus(approvalStatus);
				newTransaction.setOldCategoryCode(oldCategoryCode);
				newTransaction.setDescription(description);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setNewBranchCode(newBranchCode);
				newTransaction.setNewSectionCode(newSectionCode);
				newTransaction.setNewDeptCode(newDeptCode);
				newTransaction.setNbv(nbv);
				newTransaction.setNewAssetId(newAssetId);
				newTransaction.setBranchId(branchId);
				

				_list.add(newTransaction);
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

//
//public java.util.ArrayList getAssetTransferRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
//{
//	java.util.ArrayList _list = new java.util.ArrayList();
//	String date = String.valueOf(dateConvert(new java.util.Date()));
//	date = date.substring(0, 10);
//	String finacleTransId= null;
//	Connection c = null;
//	ResultSet rs = null; 
////	Statement s = null;
//	PreparedStatement s = null;
//System.out.println("query in getAssetTransferRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
//	try {
//		    c = getConnection();
////			s = c.createStatement();
////			rs = s.executeQuery(query);
//			 s = c.prepareStatement(query.toString());
////	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
////	          }
//			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//				 System.out.println("NO Selection in getAssetTransactionListRecords: ");
//			 }else{
////	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
//				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetTransactionListRecords: ");
//	        	  s.setString(1, FromDate);
//	        	  s.setString(2, ToDate); 
//	        	  s.setString(3, FromDate);
//	        	  s.setString(4, ToDate);  
//	          }
//				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Branch and Date Selection in getAssetTransactionListRecords: ");
//	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, FromDate);
//	        	  s.setString(3, ToDate);
//	        	  s.setString(4, branch_Id);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
//	          }
////	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
//				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Category and Date Selection in getAssetTransactionListRecords: ");
//	        	  s.setString(1, categoryCode);
//	        	  s.setString(2, FromDate);
//	        	  s.setString(3, ToDate);
//	        	  s.setString(4, categoryCode);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
//	          }
////	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
//				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Branch and Category and Date Selection in getAssetTransactionListRecords: ");
//	        	  s.setString(1, categoryCode);
//	        	  s.setString(2, branch_Id);
//	        	  s.setString(3, FromDate);
//	        	  s.setString(4, ToDate);
//	        	  s.setString(5, categoryCode);
//	        	  s.setString(6, branch_Id);
//	        	  s.setString(7, FromDate);
//	        	  s.setString(8, ToDate);
//	          }
//
//			 }
//
//	          rs = s.executeQuery();  
//			while (rs.next())
//			   {			
//
//				String categoryName = rs.getString("am_ad_category_category_name");
//				double depRate = rs.getDouble("am_ad_category_Dep_rate");
//				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
//				String depLedger = rs.getString("am_ad_category_Dep_ledger");
//				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");  
//				String gl_Account = rs.getString("am_ad_category_gl_account");  
//				String asset_Id = rs.getString("am_assetTransfer_asset_id");
//				String oldBranchId = rs.getString("am_assetTransfer_OLD_branch_Id");
//				String oldDeptId = rs.getString("am_assetTransfer_OLD_dept_ID");
//				String transferDate = rs.getString("am_assetTransfer_Transfer_Date");
//				String oldAssetUser = rs.getString("am_assetTransfer_OLD_Asset_user");
//				String oldSection = rs.getString("am_assetTransfer_OLD_Section");
//				String oldBranchCode = rs.getString("am_assetTransfer_OLD_BRANCH_CODE");
//				String oldSectionCode = rs.getString("am_assetTransfer_OLD_SECTION_CODE");
//				String oldDeptCode = rs.getString("am_assetTransfer_OLD_DEPT_CODE");
//				String branchName = rs.getString("am_ad_branch_A_BRANCH_NAME");
//				String approvalStatus = rs.getString("am_assetTransfer_approval_status");
//				String oldCategoryCode = rs.getString("am_assetTransfer_OLD_CATEGORY_CODE");
//				String description = rs.getString("am_assetTransfer_description");
//				double costPrice = rs.getDouble("am_assetTransfer_cost_price");
//				double accumDepr = rs.getDouble("am_assetTransfer_Accum_Dep");
//				double monthlyDepr = rs.getDouble("am_assetTransfer_Monthly_Dep");
//				String newBranchCode = rs.getString("am_assetTransfer_NEW_BRANCH_CODE");
//                String newSectionCode = rs.getString("am_assetTransfer_NEW_SECTION_CODE");
//                String newDeptCode = rs.getString("am_assetTransfer_NEW_DEPT_CODE");
//                double nbv = rs.getDouble("am_assetTransfer_NBV");
//                String newAssetId = rs.getString("am_assetTransfer_New_Asset_id");
//                String branchId = rs.getString("am_ad_branch_A_BRANCH_ID");
//
//                
//				newAssetTransaction newTransaction = new newAssetTransaction();
//				newTransaction.setCategoryName(categoryName);
//				newTransaction.setDepRate(depRate);
//				newTransaction.setAccumDepLedger(accumDepLedger);
//				newTransaction.setDepLedger(depLedger);
//				newTransaction.setAssetLedger(assetLedger);
//				newTransaction.setGlAccount(gl_Account);
//				newTransaction.setAssetId(asset_Id);
//				newTransaction.setOldBranchId(oldBranchId);
//				newTransaction.setOldDeptId(oldDeptId);
//				newTransaction.setTransDate(transferDate);
//				newTransaction.setOldAssetUser(oldAssetUser);
//				newTransaction.setOldSection(oldSection);
//				newTransaction.setOldBranchCode(oldBranchCode);
//				newTransaction.setOldSectionCode(oldSectionCode);
//				newTransaction.setOldDeptCode(oldDeptCode);
//				newTransaction.setBranchName(branchName);
//				newTransaction.setApprovalStatus(approvalStatus);
//				newTransaction.setOldCategoryCode(oldCategoryCode);
//				newTransaction.setDescription(description);
//				newTransaction.setCostPrice(costPrice);
//				newTransaction.setAccumDep(accumDepr);
//				newTransaction.setMonthlyDep(monthlyDepr);
//				newTransaction.setNewBranchCode(newBranchCode);
//				newTransaction.setNewSectionCode(newSectionCode);
//				newTransaction.setNewDeptCode(newDeptCode);
//				newTransaction.setNbv(nbv);
//				newTransaction.setNewAssetId(newAssetId);
//				newTransaction.setBranchId(branchId);
//				
//
//				_list.add(newTransaction);
//			   }
//	 }   
//				 catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					finally
//					{
//						closeConnection(c, s, rs);
//					}
//	return _list;
//}

public java.util.ArrayList getAssetConsolidatedChargesRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, categoryCode);
//	        	  s.setString(2, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
//	        	  s.setString(3, branch_Id);
//	        	  s.setString(4, categoryCode);	        	  
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
//	        	  s.setString(3, FromDate);
//	        	  s.setString(4, ToDate);
	          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterConcolidatedRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
//	        	  s.setString(4, branch_Id);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, categoryCode);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
//	        	  s.setString(5, branch_Id);
//	        	  s.setString(6, categoryCode);
//	        	  s.setString(7, FromDate);
//	        	  s.setString(8, ToDate);	        	  
				 } 				 				 
				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
//	        	  s.setString(4, categoryCode);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
				 } 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
//				String tranType = rs.getString("transType");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryName = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				double depRate = rs.getDouble("Dep_Rate");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				String purchaseDate = rs.getString("Date_purchased");
				String postingDate = rs.getString("Posting_Date");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String depEndDate = rs.getString("Dep_End_Date");
//                String deptCode = rs.getString("DEPT_CODE");
                String assetUser = rs.getString("ASSET_USER");

               
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
//				newTransaction.setDeptCode(deptCode);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setDepRate(depRate);
//				newTransaction.setAssetStatus(tranType);

				_list.add(newTransaction);
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

public java.util.ArrayList getAuditLogRecords(String query,String branch_Id,String user,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetRegisterRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
//	        	  s.setString(2, branch_Id);
	          }
//	          if(!query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, user);
//	        	  s.setString(2, categoryCode);
	          }
//	          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && !user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, user);
//	        	  s.setString(3, branch_Id);
//	        	  s.setString(4, categoryCode);	        	  
	          }
//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
//	        	  s.setString(3, FromDate);
//	        	  s.setString(4, ToDate);
	          }	
				 if(!branch_Id.equals("0") && user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterConcolidatedRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
//	        	  s.setString(4, branch_Id);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
				 } 
				 if(!branch_Id.equals("0") && !user.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, user);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
//	        	  s.setString(5, branch_Id);
//	        	  s.setString(6, categoryCode);
//	        	  s.setString(7, FromDate);
//	        	  s.setString(8, ToDate);	        	  
				 } 				 				 
				 if(!user.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, user);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
//	        	  s.setString(4, categoryCode);
//	        	  s.setString(5, FromDate);
//	        	  s.setString(6, ToDate);
				 } 
				 if(!branch_Id.equals("0") && user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//					 System.out.println("Branch Selection in getAssetRegisterRecords Only: ");
	        	  s.setString(1, branch_Id);
	          }	
				 if(branch_Id.equals("0") && !user.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//					 System.out.println("User Selection in getAssetRegisterRecordsn Only: ");
	        	  s.setString(1, user);
	          }					 
			 }
	          rs = s.executeQuery();  
			while (rs.next())
			   {				
				String strDescription = rs.getString("FULL_NAME");
//				String tranType = rs.getString("transType");
				String userName = rs.getString("USER_NAME");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String fullName = rs.getString("FULL_NAME");  
				String ipAddress = rs.getString("IP_ADDRESS");  
				String createDate = rs.getString("CREATE_DATE");
				String timeIn = rs.getString("TIME_IN");
                String timeOut = rs.getString("TIME_OUT");
//                String depEndDate = rs.getString("Dep_End_Date");
//                String deptCode = rs.getString("DEPT_CODE");
//                String assetUser = rs.getString("ASSET_USER");

               
 //               String depDate = rs.getString("DEP_DATE");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldassetId(userName);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDescription(fullName);
				newTransaction.setBranchName(branchName);
				newTransaction.setSystemIp(ipAddress);
				newTransaction.setAssetUser(timeIn);
				newTransaction.setAssetType(timeOut);
				newTransaction.setDatepurchased(createDate);


				_list.add(newTransaction);
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

public java.util.ArrayList getConsolidatedFixedAssetVerificationRecords(String query,String region,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetTransferRecords: "+query+"  region: "+region+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.region") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getConsolidatedFixedAssetVerificationRecords: ");
			 }else{
//	          if(query.contains("region") && !query.contains("CATEGORY_CODE")){
				 if(region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getConsolidatedFixedAssetVerificationRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!region.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Region and Date Selection in getConsolidatedFixedAssetVerificationRecords: ");
	        	  s.setString(1, region);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!region.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Region Selection in getConsolidatedFixedAssetVerificationRecords: ");
	        	  s.setString(1, region);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			
				String level = rs.getString("LEVEL");
				String classification = rs.getString("CLASIFICATION");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String zoneCode = rs.getString("ZONE_CODE");  
				String regionCode = rs.getString("REGION_CODE");  
				String zoneName = rs.getString("ZONE_NAME");
				String regionName = rs.getString("REGION_NAME");
				String computer = rs.getString("COMPUTER");
				String furniture_Fittings = rs.getString("FURNITURE & FITTINGS");
				String machine_Equipment = rs.getString("MACHINE AND EQUIPMENT");
				String building = rs.getString("BUILDING");
				String motorVehicle = rs.getString("MOTOR VEHICLE");
				String staff_Furniture_Fittings = rs.getString("STAFF FUNITURE & FITTINGS");
				String leaseholdimprovement1 = rs.getString("LEASEHOLD IMPROVEMENT 1");
				String intangibleAsset = rs.getString("INTANGIBLE ASSETS - SOFTWARE");
				String leasehold_Land = rs.getString("LEASEHOLD LAND");
				String leaseholdimprovement2 = rs.getString("LEASEHOLD IMPROVEMENT 2");
				String leaseholdimprovement3 = rs.getString("LEASEHOLD IMPROVEMENT 3");
				String leaseholdimprovement4 = rs.getString("LEASEHOLD IMPROVEMENT 4");
				String leaseholdimprovement5 = rs.getString("LEASEHOLD IMPROVEMENT 5");
				String staff_Machine_Equipment = rs.getString("STAFF MACHINE & EQUIPMENTS");
				String work_In_Progress = rs.getString("WORK-IN-PROGRESS");
         
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(level);
				newTransaction.setCategoryName(classification);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setDeptCode(zoneCode);
				newTransaction.setSbuCode(regionCode);
				newTransaction.setDeptName(zoneName);
				newTransaction.setSbuName(regionName);
				newTransaction.setGlAccount(computer);
				newTransaction.setAssetLedger(furniture_Fittings);
				newTransaction.setOldBranchId(machine_Equipment);
				newTransaction.setOldDeptId(building);
				newTransaction.setTransDate(motorVehicle);
				newTransaction.setOldAssetUser(staff_Furniture_Fittings);
				newTransaction.setOldSection(leaseholdimprovement1);
				newTransaction.setOldBranchCode(intangibleAsset);
				newTransaction.setOldSectionCode(leasehold_Land);
				newTransaction.setOldDeptCode(leaseholdimprovement2);
				newTransaction.setApprovalStatus(leaseholdimprovement3);
				newTransaction.setOldCategoryCode(leaseholdimprovement4);
				newTransaction.setDescription(leaseholdimprovement5);
				newTransaction.setNewSectionCode(staff_Machine_Equipment);
				newTransaction.setNewBranchCode(work_In_Progress);
				
				

				_list.add(newTransaction);
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

public java.util.ArrayList getUncapitalisedAsseRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getUncapitalisedAsseRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getUncapitalisedAsseRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String companyName = rs.getString("am_gb_company_company_name");
				String oldAssetId = rs.getString("AM_ASSET_UNCAPITALIZED_Old_Asset_id");
				String asset_Id = rs.getString("AM_ASSET_UNCAPITALIZED_Asset_id");
				String description = rs.getString("AM_ASSET_UNCAPITALIZED_Description");
				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String branchCode = rs.getString("am_ad_branch_BRANCH_CODE");
				String categoryName = rs.getString("am_ad_category_category_name");
				double accumDepr = rs.getDouble("AM_ASSET_UNCAPITALIZED_Accum_dep");
				double monthlyDepr = rs.getDouble("AM_ASSET_UNCAPITALIZED_monthly_dep");
				double costPrice = rs.getDouble("AM_ASSET_UNCAPITALIZED_Cost_Price");
				double nbv = rs.getDouble("AM_ASSET_UNCAPITALIZED_NBV");
				String datePurchased = rs.getString("AM_ASSET_UNCAPITALIZED_Date_purchased");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");  
				String gl_Account = rs.getString("am_ad_category_gl_account"); 
				String depreciationEndDate = rs.getString("AM_ASSET_UNCAPITALIZED_dep_end_date");
				String effectiveDate = rs.getString("AM_ASSET_UNCAPITALIZED_effective_date");
				String postingDate = rs.getString("AM_ASSET_UNCAPITALIZED_Posting_Date");
				String deptName = rs.getString("am_ad_department_Dept_name");
				String sbuCode = rs.getString("AM_ASSET_UNCAPITALIZED_SBU_CODE");
				String barCode = rs.getString("AM_ASSET_UNCAPITALIZED_BAR_CODE");
				String assetUser = rs.getString("AM_ASSET_UNCAPITALIZED_Asset_User"); 

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldBranchId(companyName);
				newTransaction.setOldassetId(oldAssetId);
				newTransaction.setAssetId(asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setBranchName(branchName);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(datePurchased);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setDependDate(depreciationEndDate);
				newTransaction.setEffectiveDate(effectiveDate);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setDeptName(deptName);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setBarCode(barCode);
				newTransaction.setAssetUser(assetUser);
				

				_list.add(newTransaction);
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

public java.util.ArrayList getAssetReclassificationRecords(String query,String branch_Id,String categoryCode, String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetReclassificationRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getAssetReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String categoryName = rs.getString("am_ad_category_category_name");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");
				String gl_Account = rs.getString("am_ad_category_gl_account");
				String asset_Id =  rs.getString("am_assetReclassification_asset_id");
				String old_Category_Id = rs.getString("am_assetReclassification_old_category_id");	
				String new_Category_Id = rs.getString("am_assetReclassification_new_category_id");	
				double old_Depr_Rate = rs.getDouble("am_assetReclassification_old_depr_rate");
				String reclassify_Reason = rs.getString("am_assetReclassification_reclassify_reason");
				String reclassify_Date = rs.getString("am_assetReclassification_reclassify_date");
				double old_Accum_Dep = rs.getDouble("am_assetReclassification_old_accum_dep");
				double new_Depr_Rate = rs.getDouble("am_assetReclassification_new_depr_rate");
				double new_Accum_Dep = rs.getDouble("am_assetReclassification_new_accum_dep");
				String recalc_Depr = rs.getString("am_assetReclassification_recalc_depr");
				String raise_Entry = rs.getString("am_assetReclassification_raise_entry");
				double recalc_Difference = rs.getDouble("am_assetReclassification_recalc_difference");
				double costPrice = rs.getDouble("am_assetReclassification_cost_price");
				String userId = rs.getString("am_assetReclassification_User_ID");
				String old_Dep_Ytd = rs.getString("am_assetReclassification_old_dep_ytd");
				String old_Category_Code = rs.getString("am_assetReclassification_old_category_code");
				String old_Branch_Code = rs.getString("am_assetReclassification_old_branch_code");
				String description = rs.getString("am_assetReclassification_description");
				String new_AssetId = rs.getString("am_assetReclassification_New_asset_id");
				String reclassifyId = rs.getString("am_assetReclassification_Reclassify_ID");
				double monthly_Dep = rs.getDouble("am_assetReclassification_Monthly_Dep");
				double nbv = rs.getDouble("am_assetReclassification_nbv");

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setOldassetId(asset_Id);
				newTransaction.setCategoryCode(old_Category_Id);
				newTransaction.setPrevMonthlyDep(old_Depr_Rate);
				newTransaction.setDisposeReason(reclassify_Reason);
				newTransaction.setDisposalDate(reclassify_Date);
				newTransaction.setPrevAccumDep(old_Accum_Dep);
				newTransaction.setPrevIMPROVAccumDep(new_Depr_Rate);;
				newTransaction.setAccumDep(new_Accum_Dep);
				newTransaction.setAction(recalc_Depr);
				newTransaction.setAssetEngineNo(raise_Entry);
				newTransaction.setAmount(recalc_Difference);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setUserID(userId);
				newTransaction.setOldDeptCode(old_Dep_Ytd);
				newTransaction.setOldCategoryCode(old_Category_Code);
				newTransaction.setOldBranchCode(old_Branch_Code);
				newTransaction.setDescription(description);
				newTransaction.setAssetId(new_AssetId);
				newTransaction.setAssetMake(reclassifyId);
				newTransaction.setMonthlyDep(monthly_Dep);
				newTransaction.setNbv(nbv);
				newTransaction.setNewsbuCode(new_Category_Id);
				
				
				

				_list.add(newTransaction);
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

public java.util.ArrayList getFullyDepreciatedAssetRecords(String query,String branch_Id, String categoryCode, String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getFullyDepreciatedAssetRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getFullyDepreciatedAssetRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				//String companyName = rs.getString("am_gb_company_company_name");

				String asset_Id = rs.getString("am_Asset_Asset_id");
				String description = rs.getString("am_Asset_Description");
				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String categoryName = rs.getString("am_ad_category_category_name");
				double accumDepr = rs.getDouble("am_Asset_Accum_dep");
				double monthlyDepr = rs.getDouble("am_Asset_monthly_dep");
				double costPrice = rs.getDouble("am_Asset_Cost_Price");
				double nbv = rs.getDouble("am_Asset_NBV");
				double improvCost = rs.getDouble("am_Asset_IMPROV_COST");
				double improvMonthlyDep = rs.getDouble("am_Asset_IMPROV_MONTHLYDEP");
				double improvAccumDep = rs.getDouble("am_Asset_IMPROV_IMPROV_ACCUMDEP");
				double improvNbv = rs.getDouble("am_Asset_IMPROV_IMPROV_NBV");
				String datePurchased = rs.getString("am_Asset_Date_purchased");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");  
				String gl_Account = rs.getString("am_ad_category_gl_account"); 
				String assetUser = rs.getString("am_Asset_Asset_User");
				//String sbuName = rs.getString("Sbu_SetUp_Sbu_name");
				
				//System.out.println("sbuName: " + sbuName);

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				//newTransaction.setOldBranchId(companyName);
				newTransaction.setAssetId(asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCost);
				newTransaction.setImprovmonthlyDep(improvMonthlyDep);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setDatepurchased(datePurchased);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setAssetUser(assetUser);
				//newTransaction.setSbuName(sbuName);
				

				_list.add(newTransaction);
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

public java.util.ArrayList getDepreciationValidationReportRecords(String query,String branch_Id,String categoryCode,String fromDate,String endDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetDisposalReportRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  fromDate: "+fromDate+"   endDate: "+endDate+"   disposalReason: "+disposalReason);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			
				 if(!fromDate.equals("")) {
		        	  s.setString(1, fromDate);
		        	  s.setString(2, fromDate);
		        	  s.setString(3, fromDate);
		        	  s.setString(4, fromDate);
		        	  s.setString(5, fromDate);
		        	  s.setString(6, fromDate);
		        	  s.setString(7, fromDate);
		        	  s.setString(8, fromDate);
		        	  s.setString(9, fromDate);
					 System.out.println("Only Date Selection in getAssetDisposalReportRecords: ");
				 }
				
			 
	          rs = s.executeQuery();
			while (rs.next())
			   {				
				String asset_Id = rs.getString("ASSET_ID");
				String description = rs.getString("DESCRIPTION");
				double monthlyDep = rs.getDouble("Monthly_Dep");
				double nbv = rs.getDouble("NBV");
				int used = rs.getInt("USED");
				int useful_Life = rs.getInt("USEFUL_LIFE");
				String effectiveDate = rs.getString("effective_date");
				int diff = rs.getInt("DIFF");
				String branchName = rs.getString("BRANCH_NAME");
                String categoryName = rs.getString("category_name");
                
               
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setMonthlyDep(monthlyDep);
				newTransaction.setNbv(nbv);
				newTransaction.setRemainLife(used);
				newTransaction.setUsefullife(useful_Life);
				newTransaction.setEffectiveDate(effectiveDate);
				newTransaction.setCalcLifeSpan(diff);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				
			//	System.out.println("StrassetId in getAssetDisposalReportRecords: "+StrassetId+"  branchName: "+branchName+"   categoryName: "+categoryName+"  strDescription: "+strDescription+"   costPrice: "+costPrice+"   accumDepr: "+accumDepr);
				_list.add(newTransaction);
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

public java.util.ArrayList getAssetGroupRecords(String query,String branch_Id,String categoryCode, String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getAssetGroupRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetGroupRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getAssetGroupRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getAssetGroupRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getAssetGroupRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getAssetGroupRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getAssetGroupRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getAssetGroupRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getAssetGroupRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String asset_Id =  rs.getString("am_Asset_Asset_id");
				String description = rs.getString("am_Asset_Description");
				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String categoryName = rs.getString("am_ad_category_category_name");
				double accumDep = rs.getDouble("am_Asset_Accum_dep");
				double monthlyDep = rs.getDouble("am_Asset_monthly_dep");
				double costPrice = rs.getDouble("am_Asset_Cost_Price");
				double nbv = rs.getDouble("am_Asset_NBV");
				String date_Purchased = rs.getString("am_Asset_Date_purchased");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDepLedger = rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");
				String gl_Account = rs.getString("am_ad_category_gl_account");
				String dep_End_Date = rs.getString("am_Asset_dep_end_date");
				String effective_Date = rs.getString("am_Asset_effective_date");
				String posting_Date = rs.getString("am_Asset_Posting_Date");
				String dept_Name = rs.getString("am_ad_department_Dept_name");
				String assetUser = rs.getString("am_Asset_Asset_User");
				String sbu_Name = rs.getString("Sbu_SetUp_Sbu_name"); 
				String branch_Code = rs.getString("am_Asset_BRANCH_CODE"); 
				String barCode = rs.getString("am_Asset_BAR_CODE"); 
				String serial_No   = rs.getString("am_Asset_Asset_Serial_No");	
				String asset_Spare1 = rs.getString("am_Asset_Spare_1");	
				String asset_Spare2 = rs.getString("am_Asset_Spare_2");
				String asset_Group_Id = rs.getString("am_Asset_group_id");
				String purchase_Reason = rs.getString("am_Asset_Purchase_Reason");

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setAccumDep(accumDep);
				newTransaction.setMonthlyDep(monthlyDep);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(date_Purchased);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDepLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setDependDate(dep_End_Date);
				newTransaction.setPostingDate(posting_Date);
				newTransaction.setEffectiveDate(effective_Date);
				newTransaction.setDeptName(dept_Name);
				newTransaction.setAssetUser(assetUser);
				newTransaction.setSbuName(sbu_Name);
				newTransaction.setBranchCode(branch_Code);
				newTransaction.setBarCode(barCode);
				newTransaction.setAssetSerialNo(serial_No);
				newTransaction.setSpare1(asset_Spare1);
				newTransaction.setSpare2(asset_Spare2);
				newTransaction.setNewAssetId(asset_Group_Id);
				newTransaction.setPurchaseReason(purchase_Reason);
				
				
				

				_list.add(newTransaction);
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


public java.util.ArrayList getBulkTransferListRecords(String query,String branch_Id,String batch_Id, String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getBulkTransferListRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getBulkTransferListRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getBulkTransferListRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getBulkTransferListRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !batch_Id.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Batch Id and Date Selection in getBulkTransferListRecords: ");
	        	  s.setString(1, batch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !batch_Id.equals("") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Batch Id Selection in getBulkTransferListRecords: ");
	        	  s.setString(1, batch_Id);
	          }
				 


			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				 String asset_Id =  rs.getString("am_gb_bulkTransfer_Asset_id");
					String regisstrationNo = rs.getString("am_gb_bulkTransfer_Registration_No");
					String description = rs.getString("am_gb_bulkTransfer_Description");
					String oldBranchId = rs.getString("am_gb_bulkTransfer_oldbranch_id");
					String oldDeptId = rs.getString("am_gb_bulkTransfer_olddept_id");
					String oldSbuCode = rs.getString("am_gb_bulkTransfer_oldSBU_CODE");
					String oldSectionId = rs.getString("am_gb_bulkTransfer_oldsection_id");
					String oldAssetUser = rs.getString("am_gb_bulkTransfer_oldAsset_User");
					String newBranchId = rs.getString("am_gb_bulkTransfer_newbranch_id");
					String newDeptId = rs.getString("am_gb_bulkTransfer_newdept_id");
					String newSbuCode = rs.getString("am_gb_bulkTransfer_newSBU_CODE");
					String newSectionId = rs.getString("am_gb_bulkTransfer_newsection_id");
					String newAssetUser = rs.getString("am_gb_bulkTransfer_newAsset_User");
					String batchId = rs.getString("am_gb_bulkTransfer_Batch_id");
					String transferById = rs.getString("am_gb_bulkTransfer_Transferby_id");
					String categoryId = rs.getString("am_gb_bulkTransfer_CATEGORY_ID");
					String newAssetId = rs.getString("am_gb_bulkTransfer_NEW_ASSET_ID");
					double accumDep = rs.getDouble("am_gb_bulkTransfer_ACCUM_DEP");
					double monthlyDep = rs.getDouble("am_gb_bulkTransfer_MONTHLY_DEP");
					double nbv = rs.getDouble("am_gb_bulkTransfer_NBV");
					String transfer_Date = rs.getString("am_gb_bulkTransfer_TRANSFER_DATE");
					String transId = rs.getString("am_gb_bulkTransfer_transId");
					double transfer_Cost = rs.getDouble("am_gb_bulkTransfer_TRANSFER_COST");
					String assetCode = rs.getString("am_gb_bulkTransfer_asset_code");	
					String old_BranchName = rs.getString("am_ad_branch_BRANCH_NAME");
					String new_BranchName = rs.getString("am_ad_branch_A_BRANCH_NAME");	
					String full_Name = rs.getString("am_gb_User_Full_Name");
					String userName = rs.getString("am_gb_User_User_Name");
					double costPrice = rs.getDouble("am_gb_bulkTransfer_COST_PRICE");

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setRegistrationNo(regisstrationNo);
				newTransaction.setDescription(description);
				newTransaction.setOldBranchId(oldBranchId);
				newTransaction.setOldDeptId(oldDeptId);
				newTransaction.setOldsbuCode(oldSbuCode);
				newTransaction.setOldSection(oldSectionId);
				newTransaction.setOldAssetUser(oldAssetUser);
				newTransaction.setNewBranchCode(newBranchId);
				newTransaction.setNewDeptCode(newDeptId);
				newTransaction.setNewsbuCode(newSbuCode);
				newTransaction.setNewSectionCode(newSectionId);
				newTransaction.setAssetUser(newAssetUser);
				newTransaction.setBarCode(batchId);
				newTransaction.setComments(transferById);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setNewAssetId(newAssetId);
				newTransaction.setAccumDep(accumDep);
				newTransaction.setMonthlyDep(monthlyDep);
				newTransaction.setNbv(nbv);
				newTransaction.setTransDate(transfer_Date);
				newTransaction.setTranType(transId);
				newTransaction.setTotalCost(transfer_Cost);
				newTransaction.setAssetCode(assetCode);
				newTransaction.setCategoryName(old_BranchName);
				newTransaction.setBranchName(new_BranchName);
				newTransaction.setAssetfunction(full_Name);
				newTransaction.setUserID(userName);
				newTransaction.setCostPrice(costPrice);
				
				

				_list.add(newTransaction);
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



public java.util.ArrayList getBranchSummaryVerificationRecords(String query,String branch_Id,String departmentCode, String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   departmentCode: "+departmentCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getBranchSummaryVerificationRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Department and Date Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, departmentCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Department Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, departmentCode);
	          }
				 
				 if(!branch_Id.equals("0") && !departmentCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Department Selection in getBranchSummaryVerificationRecords: ");
	        	  s.setString(1, departmentCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !departmentCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Department and Date Selection in getAssetGroupRecords: ");
	        	  s.setString(1, departmentCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			


				String level =  rs.getString("LEVEL");
				String clasification = rs.getString("CLASIFICATION");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String deptName = rs.getString("DEPT_NAME");
				String computer = rs.getString("OFFICE EQUIPMENT - COMPUTER");
				String furniture_and_Fitting = rs.getString("HOUSEHOLD FURNITURE & FITTING");
				String machine_and_equipment = rs.getString("OFFICE EQUIPMENT - OTHERS");
				String building = rs.getString("BUILDING");
				String motorVehicle = rs.getString("MOTOR VEHICLES");
				String staff_Furniture_and_Fitting = rs.getString("OFFICE FURNITURE & FITTINGS");
				String leaseholdImprovement1 = rs.getString("LEASEHOLD IMPROVEMENT - HOUSE");
				String intangibleAsset = rs.getString("COMPUTER SOFTWARE");
				String leaseholdLand = rs.getString("LEASEHOLD IMPROVEMENT");
				String leaseholdImprovement2 = rs.getString("WIP-LEASEHOLD IMPROVEMENT");
				String leaseholdImprovement3 = rs.getString("WIP-LEASEHOLD IMPROVEMENT-HOUSE");
				String leaseholdImprovement4 = rs.getString("WIP-HOUSEHOLD FURNITURE AND FITTINGS");
				String leaseholdImprovement5 = rs.getString("WIP-OFFICE FURNITURE AND FITTINGS");
				String staff_Machine_and_Equipments = rs.getString("WIP-OFFICE EQUIPMENT - OTHERS"); 
				String work_In_Progress = rs.getString("WIP-BUILDING"); 
				String airCraft = rs.getString("AIRCRAFT"); 
				String land = rs.getString("LAND"); 
				String wipLand = rs.getString("WIP-LAND");
				String wipComputerSoftware = rs.getString("WIP-COMPUTER SOFTWARE"); 
				String wipGenerators = rs.getString("WIP-GENERATORS");
				String wipGeneratorsHouse = rs.getString("WIP-GENERATORS-HOUSE");
				String wipMotorVehicles = rs.getString("WIP-MOTOR VEHICLES");
				String wipOfficeEquipmentComputers = rs.getString("WIP-OFFICE EQUIPMENT - COMPUTERS");
				

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				
				newTransaction.setAssetId(level);
				newTransaction.setCategoryName(clasification);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setDeptCode(deptCode);
				newTransaction.setDeptName(deptName);
				newTransaction.setGlAccount(computer);
				newTransaction.setAssetLedger(furniture_and_Fitting);
				newTransaction.setOldBranchId(machine_and_equipment);
				newTransaction.setOldDeptId(building);
				newTransaction.setTransDate(motorVehicle);
				newTransaction.setOldAssetUser(staff_Furniture_and_Fitting);
				newTransaction.setOldSection(leaseholdImprovement1);
				newTransaction.setOldBranchCode(intangibleAsset);
				newTransaction.setOldSectionCode(leaseholdLand);
				newTransaction.setOldDeptCode(leaseholdImprovement2);
				newTransaction.setApprovalStatus(leaseholdImprovement3);
				newTransaction.setOldCategoryCode(leaseholdImprovement4);
				newTransaction.setDescription(leaseholdImprovement5);
				newTransaction.setNewSectionCode(staff_Machine_and_Equipments);
				newTransaction.setNewBranchCode(work_In_Progress);
				newTransaction.setAirCraft(airCraft);				
				newTransaction.setLand(wipLand);
				newTransaction.setWipLand(wipLand);
				newTransaction.setWipComputerSoftware(wipComputerSoftware);
				newTransaction.setWipGenerators(wipGeneratorsHouse);
				newTransaction.setWipGeneratorsHouse(wipGeneratorsHouse);
				newTransaction.setWipMotorVehicles(wipMotorVehicles);
				newTransaction.setWipOfficeEquipmentComputers(wipOfficeEquipmentComputers);

				_list.add(newTransaction);
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


public java.util.ArrayList getUncapitalizedTransferRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getUncapitalizedTransferRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getUncapitalizedTransferRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String companyName = rs.getString("am_gb_company_company_name");
				String categoryName = rs.getString("am_ad_category_category_name");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDeprLedger= rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");
				String gl_Account = rs.getString("am_ad_category_gl_account"); 
				String asset_Id = rs.getString("am_assetTransfer_asset_id");
				String oldBranchId = rs.getString("am_assetTransfer_OLD_branch_Id");
				String oldDeptId = rs.getString("am_assetTransfer_OLD_dept_ID");
				String transferDate = rs.getString("am_assetTransfer_Transfer_Date");
				String oldAssetUser = rs.getString("am_assetTransfer_OLD_Asset_user");
				String oldSectionId = rs.getString("am_assetTransfer_OLD_Section");
				String oldBranchCode = rs.getString("am_assetTransfer_OLD_BRANCH_CODE");
				String oldSectionCode = rs.getString("am_assetTransfer_OLD_SECTION_CODE");
				String oldDeptCode = rs.getString("am_assetTransfer_OLD_DEPT_CODE");
				String branchName = rs.getString("am_ad_branch_A_BRANCH_NAME");
				String approvalStatus = rs.getString("am_assetTransfer_approval_status");
				String old_CategoryCode = rs.getString("am_assetTransfer_OLD_CATEGORY_CODE");
				String description = rs.getString("am_assetTransfer_description");
				double costPrice = rs.getDouble("am_assetTransfer_cost_price");
				double accumDep = rs.getDouble("am_assetTransfer_Accum_Dep");
				double monthlyDepr = rs.getDouble("am_assetTransfer_Monthly_Dep");
				String newBranchCode = rs.getString("am_assetTransfer_NEW_BRANCH_CODE");
				String newSectionCode = rs.getString("am_assetTransfer_NEW_SECTION_CODE");
				String newDeptCode = rs.getString("am_assetTransfer_NEW_DEPT_CODE");
				double nbv = rs.getDouble("am_assetTransfer_NBV");
				String newAssetId = rs.getString("am_assetTransfer_New_Asset_id");
				String branchId = rs.getString("am_ad_branch_A_BRANCH_ID");
		
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setOldBranchId(companyName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDeprLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setAssetId(asset_Id);
				newTransaction.setOldBranchId(oldBranchId);
				newTransaction.setOldDeptId(oldDeptId);
				newTransaction.setTransDate(transferDate);
				newTransaction.setOldAssetUser(oldAssetUser);
				newTransaction.setOldSection(oldSectionId);
				newTransaction.setOldBranchCode(oldBranchCode);
				newTransaction.setOldSectionCode(oldSectionCode);
				newTransaction.setOldDeptCode(oldDeptCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setApprovalStatus(approvalStatus);
				newTransaction.setOldCategoryCode(old_CategoryCode);
				newTransaction.setDescription(description);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accumDep);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setNewBranchCode(newBranchCode);
				newTransaction.setNewSectionCode(newSectionCode);
				newTransaction.setNewDeptCode(newDeptCode);
				newTransaction.setNbv(nbv);
				newTransaction.setNewAssetId(newAssetId);
				newTransaction.setBranchId(branchId);
				

				_list.add(newTransaction);
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


public java.util.ArrayList getWipReclassificationRecords(String query,String branch_Id,String categoryCode, String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getWipReclassificationRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getWipReclassificationRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

                String Wip_Asset_Id =  rs.getString("AM_WIP_RECLASSIFICATION_Asset_id");
				String description = rs.getString("AM_ASSET_Description");
				double costPrice = rs.getDouble("AM_ASSET_Cost_Price");
				double accum_Dep = rs.getDouble("AM_ASSET_Accum_Dep");
				double monthly_Dep = rs.getDouble("AM_ASSET_Monthly_Dep");
				double nbv = rs.getDouble("AM_ASSET_NBV");
				String purchaseDate = rs.getString("AM_ASSET_Date_purchased");
				String postingDate = rs.getString("AM_ASSET_Posting_Date");
				String effectiveDate = rs.getString("AM_ASSET_Effective_Date");
				String branch_Code = rs.getString("AM_ASSET_BRANCH_CODE");
				String category_Code = rs.getString("AM_ASSET_CATEGORY_CODE");
				String companyName = rs.getString("am_gb_company_company_name");
				String asset_Id = rs.getString("AM_ASSET_Asset_id");
				String old_AssetId = rs.getString("AM_ASSET_OLD_ASSET_ID");
				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String assetStatus = rs.getString("AM_ASSET_Asset_Status");

                
				newAssetTransaction newTransaction = new newAssetTransaction();
				
				newTransaction.setAction(Wip_Asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setAccumDep(accum_Dep);
				newTransaction.setMonthlyDep(monthly_Dep);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setEffectiveDate(effectiveDate);
				newTransaction.setBranchCode(branch_Code);
				newTransaction.setCategoryCode(category_Code);
				newTransaction.setSpare1(companyName);
				newTransaction.setAssetId(asset_Id);
				newTransaction.setOldassetId(old_AssetId);
				newTransaction.setBranchName(branchName);
				newTransaction.setAssetStatus(assetStatus);
				

				_list.add(newTransaction);
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

public java.util.ArrayList getUncapitalizedDisposalRecords(String query,String branch_Id,String categoryCode,String FromDate, String ToDate, String assetId)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getUncapitalizedDisposalRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);  
	          }
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Date Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, branch_Id);
	          }

				 if(branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Category and Date Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	          }
				 
				 if(branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Category Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, categoryCode);
	          }
				 
				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Branch and Category Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	 
	          }

				 if(!branch_Id.equals("0") && !categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Branch and Category and Date Selection in getUncapitalizedDisposalRecords: ");
	        	  s.setString(1, categoryCode);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	          }

			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String asset_Id = rs.getString("am_Asset_Asset_id");
				String description = rs.getString("am_Asset_Description");
				String branchName = rs.getString("am_ad_branch_BRANCH_NAME");
				String categoryName = rs.getString("am_ad_category_category_name");
				double accumDep = rs.getDouble("am_Asset_Accum_dep");
				double monthlyDepr = rs.getDouble("am_Asset_monthly_dep");
				double costPrice = rs.getDouble("am_Asset_Cost_Price");
				double nbv = rs.getDouble("am_Asset_NBV");
				String purchaseDate = rs.getString("am_Asset_Date_purchased");
				double depRate = rs.getDouble("am_ad_category_Dep_rate");
				String accumDeprLedger= rs.getString("am_ad_category_Accum_Dep_ledger");
				String depLedger = rs.getString("am_ad_category_Dep_ledger");
				String assetLedger = rs.getString("am_ad_category_Asset_Ledger");
				String gl_Account = rs.getString("am_ad_category_gl_account"); 
				String branch_Code = rs.getString("am_Asset_BRANCH_CODE");
				String category_Code = rs.getString("am_Asset_CATEGORY_CODE");
				String disposalReason = rs.getString("am_AssetDisposal_Disposal_reason");
				String buyerAccount = rs.getString("am_AssetDisposal_Buyer_Account");
				double disposalAmount = rs.getDouble("am_AssetDisposal_Disposal_Amount");
				String disposalDate = rs.getString("am_AssetDisposal_Disposal_Date");
				double profit_loss = rs.getDouble("am_AssetDisposal_Profit_Loss");
				String disposal_Description = rs.getString("am_ad_disposalReasons_description");
				String asset_user = rs.getString("am_Asset_Asset_User");
				String bar_Code = rs.getString("am_Asset_BAR_CODE");
				String asset_status = rs.getString("am_Asset_Asset_Status");
				String company_name = rs.getString("am_gb_company_company_name");
		
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setDescription(description);
				newTransaction.setBranchName(branchName);
				newTransaction.setCategoryName(categoryName);
				newTransaction.setAccumDep(accumDep);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setNbv(nbv);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDepRate(depRate);
				newTransaction.setAccumDepLedger(accumDeprLedger);
				newTransaction.setDepLedger(depLedger);
				newTransaction.setAssetLedger(assetLedger);
				newTransaction.setGlAccount(gl_Account);
				newTransaction.setBranchCode(branch_Code);
				newTransaction.setCategoryCode(category_Code);
				newTransaction.setDisposeReason(disposalReason);
				newTransaction.setCreditAccount(buyerAccount);
				newTransaction.setDisposalAmount(disposalAmount);
				newTransaction.setDisposalDate(disposalDate);
				newTransaction.setProfitAmount(profit_loss);
				newTransaction.setAction(disposal_Description);
				newTransaction.setAssetUser(asset_user);
				newTransaction.setBarCode(bar_Code);
				newTransaction.setAssetStatus(asset_status);
				newTransaction.setOldBranchId(company_name);
			

				_list.add(newTransaction);
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

public java.util.ArrayList getAssetTransactionApprovalRecords(String query,String branch_Id,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(branch_Id.equals("0") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getAssetRegisterRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!branch_Id.equals("0") && FromDate.equals("") && ToDate.equals("")) {
	        	  s.setString(1, branch_Id);
	        	  s.setString(2, branch_Id);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, branch_Id);
	        	  s.setString(5, branch_Id);
	        	  s.setString(6, branch_Id);
	        	  s.setString(7, branch_Id);
	          }

//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(branch_Id.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//					 System.out.println("Date Selection in getAssetRegisterRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	        	  s.setString(9, FromDate);
	        	  s.setString(10, ToDate);
	        	  s.setString(11, FromDate);
	        	  s.setString(12, ToDate);
	        	  s.setString(13, FromDate);
	        	  s.setString(14, ToDate);
	          }	
				 if(!branch_Id.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Branch and Date Selection in getAssetRegisterConcolidatedRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, branch_Id);
	        	  s.setString(4, FromDate);
	        	  s.setString(5, ToDate);
	        	  s.setString(6, branch_Id);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	        	  s.setString(9, branch_Id);
	        	  s.setString(10, FromDate);
	        	  s.setString(11, ToDate);
	        	  s.setString(12, branch_Id);
	        	  s.setString(13, FromDate);
	        	  s.setString(14, ToDate);
	        	  s.setString(15, branch_Id);
	        	  s.setString(16, FromDate);
	        	  s.setString(17, ToDate);
	        	  s.setString(18, branch_Id);
	        	  s.setString(19, FromDate);
	        	  s.setString(20, ToDate);
	        	  s.setString(21, branch_Id);
				 } 
			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String asset_Id = rs.getString("asset_id");
				String user_id = rs.getString("user_id");
				String super_id = rs.getString("super_id");
				String posted_by = rs.getString("POSTED_BY");
				String approved_by = rs.getString("APPROVED_BY");
				String branchName = rs.getString("BRANCH_NAME");
				String description = rs.getString("description");
				String tran_type = rs.getString("tran_type");
				String posting_date = rs.getString("posting_date");
				String date_approved = rs.getString("DATE_APPROVED");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setAssetCode(user_id);
				newTransaction.setDescription(description);
				newTransaction.setAction(super_id);
				newTransaction.setPostedBy(posted_by);
				newTransaction.setApprovalStatus(approved_by);
				newTransaction.setBranchName(branchName);
				newTransaction.setTranType(tran_type);
				newTransaction.setPostingDate(posting_date);				
				newTransaction.setDatepurchased(date_approved);
				_list.add(newTransaction);
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

public java.util.ArrayList getFixedAssetMovementScheduleRecords(String query,String FromDate, String ToDate, String bankingApp)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String endmonth = "";
	String endyear = "";
    if(!FromDate.equals("")){
	String yyyy = FromDate.substring(6, 10);
//	System.out.println("======>yyyy: "+yyyy);
	String mm = FromDate.substring(3, 5);
//	System.out.println("======>mm: "+mm);
	String dd = FromDate.substring(0, 2);
	FromDate = yyyy+"-"+mm+"-"+dd;
    }
 //   System.out.println("======>FromDate: "+FromDate);
    if(!ToDate.equals("")){
	endyear = ToDate.substring(6, 10);
//	System.out.println("======>endyear: "+endyear);
	endmonth = ToDate.substring(3, 5);
//	System.out.println("======>endmonth: "+endmonth);
	String Tdd = ToDate.substring(0, 2);
	ToDate = endyear+"-"+endmonth+"-"+Tdd;
    }	
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//	System.out.println("query in getFixedAssetMovementScheduleRecords: "+query+"  FromDate: "+FromDate+"  ToDate: "+ToDate+"  bankingApp: "+bankingApp);
	try {
		if(bankingApp.equalsIgnoreCase("FLEXICUBE")) {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
			 if(FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getFixedAssetMovementScheduleRecords: ");
			 }
			 if(!FromDate.equals("") && !ToDate.equals("")) {
				 System.out.println("Date Selection in getFixedAssetMovementScheduleRecords: ");
	          }
	          rs = s.executeQuery();  
			while (rs.next())
			   {	
				String narration = rs.getString("NARRATION");
				String building = rs.getString("BUILDING");				
				String land = rs.getString("LAND");
				String leasehold_Improvement = rs.getString("LEASEHOLD IMPROVEMENT");
				String furniture_fittings = rs.getString("FURNITURE FITTINGS & EQUIPMENT");
				String computer_equipment = rs.getString("COMPUTER EQUIPMENT");
				String motor_vehicles = rs.getString("MOTOR VEHICLES");
				String airCraft = rs.getString("AIRCRAFT") == "NULL" ? "0" :  rs.getString("AIRCRAFT");
				//if(airCraft.equals("NULL")){airCraft = "0";}
				String computerSoftware = rs.getString("COMPUTER SOFTWARE") == "NULL" ? "0" :  rs.getString("COMPUTER SOFTWARE");
				//if(computerSoftware.equals("NULL")){computerSoftware = "0";}
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setBarCode(narration);
				newTransaction.setAssetUser(land);
				newTransaction.setAssetMaintenance(furniture_fittings);
				newTransaction.setAssetType(computer_equipment);
				newTransaction.setDescription(leasehold_Improvement);
				newTransaction.setCategoryName(airCraft);
				newTransaction.setEngineNo(motor_vehicles);
				newTransaction.setLocation(computerSoftware);
				newTransaction.setSpare2(building);
				_list.add(newTransaction);			
			   }
		}

		if(bankingApp.equalsIgnoreCase("FINACLE")) {
		    c = getConnection();
			 s = c.prepareStatement(query.toString());
			 if(FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getFixedAssetMovementScheduleRecords: ");
			 }
			 if(!FromDate.equals("") && !ToDate.equals("")) {
				 System.out.println("Date Selection in getFixedAssetMovementScheduleRecords: ");
	          }
	          rs = s.executeQuery();  
			while (rs.next())
			   {	
				String narration = rs.getString("NARRATION");
				String building = rs.getString("LAND & LEASEHOLD");
				String land = rs.getString("WORK IN PROGRESS");
				String leasehold_Improvement = rs.getString("MACHINE & EQUIPMENT");
				String furniture_fittings = rs.getString("FURNITURE & FITTINGS");
				String computer_equipment = rs.getString("COMPUTER");
				String motor_vehicles = rs.getString("MOTOR VEHCILE");
				String airCraft = rs.getString("");
				String computerSoftware = rs.getString("INTANGIBLE ASSETS");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setBarCode(narration);
				newTransaction.setAssetUser(land);
				newTransaction.setAssetMaintenance(furniture_fittings);
				newTransaction.setAssetType(computer_equipment);
				newTransaction.setDescription(leasehold_Improvement);
				newTransaction.setCategoryName(airCraft);
				newTransaction.setEngineNo(motor_vehicles);
				newTransaction.setLocation(computerSoftware);
				newTransaction.setSpare2(building);
				_list.add(newTransaction);				
			   }
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


public java.util.ArrayList getVendorDetails(String query)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());


	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String vendorId =  rs.getString("Vendor_ID");
				String vendorCode = rs.getString("Vendor_Code");
				String vendorName = rs.getString("Vendor_Name");
				String vendorBranchId = rs.getString("VendorBranchId");
				String contactPerson = rs.getString("Contact_Person");
				String contactAddress = rs.getString("Contact_Address");
				String vendorPhone = rs.getString("Vendor_Phone");
				String vendorFax = rs.getString("Vendor_fax");
				String vendorEmail = rs.getString("Vendor_email");
				String vendorState = rs.getString("Vendor_State");
				String acquisitionVendor = rs.getString("Acquisition_Vendor");
				String maintenanceVendor = rs.getString("Maintenance_Vendor");
				String accountNumber = rs.getString("account_number");
				String vendorStatus = rs.getString("Vendor_Status");
				String userId = rs.getString("User_ID");
				String createDate = rs.getString("Create_date");
				String accountType = rs.getString("account_type");
				                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(vendorId);
				newTransaction.setAssetCode(vendorCode);
				newTransaction.setVendorName(vendorName);
				newTransaction.setBranchId(vendorBranchId);
				newTransaction.setAction(contactPerson);
				newTransaction.setComments(contactAddress);
				newTransaction.setAssetCode(vendorPhone);
				newTransaction.setAssetLedger(vendorFax);
				newTransaction.setAssetfunction(vendorEmail);
				newTransaction.setAssetModel(vendorState);
				newTransaction.setAssetSerialNo(acquisitionVendor);
				newTransaction.setAssetMaintenance(maintenanceVendor);
				newTransaction.setGlAccount(accountNumber);
				newTransaction.setAssetStatus(vendorStatus);
				newTransaction.setUserID(userId);
				newTransaction.setPostingDate(createDate);
				newTransaction.setAssetType(accountType);


				_list.add(newTransaction);
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

public java.util.ArrayList getCloseAssetRecords(String query,String assetId,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null; 
//	Statement s = null;
	PreparedStatement s = null;
//System.out.println("query in getUncapitalisedAsseRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode+"  FromDate: "+FromDate+"  ToDate: "+ToDate);
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
//	          if(!query.contains("b.branch_id") && !query.contains("a.CATEGORY_CODE")){
//	          }
			 if(assetId.equals("") && FromDate.equals("") && ToDate.equals("")) {
				 System.out.println("NO Selection in getCloseAssetRecords: ");
			 }else{
//	          if(query.contains("branch_id") && !query.contains("CATEGORY_CODE")){
				 if(!assetId.equals("") && FromDate.equals("") && ToDate.equals("")) {
					 System.out.println("Asset Id Selection in getCloseAssetRecords: ");
	        	  s.setString(1, assetId);
	        	  s.setString(2, assetId);
	          }

//		          if(query.contains("branch_id") && query.contains("CATEGORY_CODE")){
				 if(assetId.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
					 System.out.println("Date Selection in getCloseAssetRecords: ");
	        	  s.setString(1, FromDate);
	        	  s.setString(2, ToDate);
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  
	          }	
				 if(!assetId.equals("") && !FromDate.equals("") && !ToDate.equals("")) {
				System.out.println("Asset Id and Date Selection in getCloseAssetRecords: ");
				  s.setString(1, assetId);
				  s.setString(2, FromDate);
	        	  s.setString(3, ToDate);
	        	  s.setString(4, assetId);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	 
	        	
				 } 
			 }

	          rs = s.executeQuery();  
			while (rs.next())
			   {			

				String asset_Id = rs.getString("Asset_ID"); 
				String categoryCode = rs.getString("CATEGORY_CODE");
				String branchCode = rs.getString("BRANCH_CODE");
				String description = rs.getString("description");
				double costPrice = rs.getDouble("Cost_Price");
				String dateClosed = rs.getString("Date_Closed");
				String postingDate = rs.getString("Posting_Date");	
				String assetType = rs.getString("AssetType");
				String closedBy = rs.getString("CLOSEDBY");
				String approvedBy = rs.getString("APPROVEDBY");
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(asset_Id);
				newTransaction.setCategoryCode(categoryCode);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setDescription(description);
				newTransaction.setCostPrice(costPrice);
				newTransaction.setEffectiveDate(dateClosed);
				newTransaction.setPostingDate(postingDate);
				newTransaction.setAssetType(assetType);
				newTransaction.setAction(closedBy);
				newTransaction.setApprovalStatus(approvedBy);				
		
				
				_list.add(newTransaction);
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

public java.util.ArrayList getPostedBatchTransactionExportRecords(String query,String userName,String startDate,String endDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
	String finacleTransId= null;
	Connection c = null;
	ResultSet rs = null;
//	Statement s = null; 
	PreparedStatement s = null;
	
	try {
		    c = getConnection();
//			s = c.createStatement();
//			rs = s.executeQuery(query);
			 s = c.prepareStatement(query.toString());
	          if(query.contains("a.User_Name") && query.contains("a.transaction_date")){
	        	  s.setString(1, userName);
	        	  s.setString(2, startDate);
	        	  s.setString(3, endDate);
	          }
	          if(query.contains("a.transaction_date") && !query.contains("a.User_Name") ){
	        	  s.setString(1, startDate);
	        	  s.setString(2, endDate);
	          }
			rs = s.executeQuery();
			while (rs.next())
			   {				
				String strgroupId = rs.getString("GROUP_ID");
				String strbatchNo = rs.getString("BATCH_NO");
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				double amount = rs.getDouble("AMOUNT");
				String debitAccount = rs.getString("ACCOUNT_NO");
				String transType = rs.getString("TRANSTYPE");
//				String initiatorId = rs.getString("INITIATORID");
//				String supervisorId = rs.getString("SUPERVISORID");
				String postedBy = rs.getString("FULL_NAME");
                String transactionDate = rs.getString("TRANSACTION_DATE");
 //               String response = rs.getString("Error_Description");
 //               String action = rs.getString("page1");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setBranchName(branchName);
				newTransaction.setDescription(strDescription);
				newTransaction.setAmount(amount);
				newTransaction.setDebitAccount(debitAccount);
				newTransaction.setTranType(transType);
				newTransaction.setBarCode(strbatchNo);
				newTransaction.setOldassetId(strgroupId);
				newTransaction.setPostedBy(postedBy);
				newTransaction.setTransDate(transactionDate);
//				newTransaction.setInitiatorId(initiatorId);
//				newTransaction.setSupervisorId(supervisorId);
				
				_list.add(newTransaction);
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


public java.util.ArrayList getLegacyPostedTransactionRecords(String query,String branch_Id,String FromDate, String ToDate)
{
	java.util.ArrayList _list = new java.util.ArrayList();
	String date = String.valueOf(dateConvert(new java.util.Date()));
	date = date.substring(0, 10);
//	String finacleTransId= null;
	ResultSet rs = null; 
	Connection conn = null;
	PreparedStatement ps = null;
//	System.out.println("query in getAssetManagementRecords: "+query+"  branch_Id: "+branch_Id+"   categoryCode: "+categoryCode);
	try {
        	
        	
        ConnectionClass connection = new ConnectionClass();
        	conn = connection.getOracleConnection();
        	ps = conn.prepareStatement(query.toString());        	
        	
			 if(branch_Id.equals("0") && FromDate.equals("") && ToDate.equals("")) {
//				 System.out.println("NO Selection in getAssetManagementRecords: ");
			 }else{
//				 System.out.println("Selection in getAssetManagementRecords branch_Id: "+query.contains("a.branch_id")+"   categoryCode: "+query.contains("a.CATEGORY_CODE"));
	          if(!branch_Id.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	      //  	  System.out.println("getAssetManagementRecords with Branch but No Category: ");
	        	  ps.setString(1, branch_Id);
	        	  ps.setString(2, FromDate);
	        	  ps.setString(3, ToDate);
	          }
	          if(branch_Id.equals("0") && !FromDate.equals("") && !ToDate.equals("")){
	        //	  System.out.println("getAssetManagementRecords with No Branch but Category Only: ");
	        	  ps.setString(2, FromDate);
	        	  ps.setString(3, ToDate);
	          }
	          if(!branch_Id.equals("0") && FromDate.equals("") && ToDate.equals("")){
	        //	  System.out.println("getAssetManagementRecords with Branch and Category: ");
	        	  ps.setString(1, branch_Id);
	          }	  				 
			 }
	          rs = ps.executeQuery();
			while (rs.next())
			   {				
				String strDescription = rs.getString("Description");
				String StrassetId = rs.getString("ASSET_ID");
				String branchCode = rs.getString("BRANCH_CODE");
				String branchName = rs.getString("BRANCH_NAME");
				String categoryId = rs.getString("category_name");  
				String deptName = rs.getString("Dept_name");  
				String sbuCode = rs.getString("SBU_CODE");
				double costPrice = rs.getDouble("COST_PRICE");
				double monthlyDepr = rs.getDouble("Monthly_Dep");
				double depChargeToDate = rs.getDouble("DEPCHARGETODATE");
				double accumDepr = rs.getDouble("Accum_Dep");
				double nbv = rs.getDouble("NBV");
				double totalnbv = rs.getDouble("TOTAL_NBV");
				double improvCostPrice = rs.getDouble("IMPROV_COST");
				double improvAccumDep = rs.getDouble("IMPROV_ACCUMDEP");
				double improvMonthDep = rs.getDouble("IMPROV_MONTHLYDEP");
				double improvNbv = rs.getDouble("IMPROV_NBV");
				double totalCostPrice = rs.getDouble("Total_Cost_Price");
				String purchaseDate = rs.getString("Date_purchased");
                String EffectiveDate = rs.getString("EFFECTIVE_DATE");
                String barCode = rs.getString("BAR_CODE");
                String spare1 = rs.getString("Spare_1");
                String serialNo = rs.getString("Asset_Serial_No");
 //               int calclifeSpan = rs.getInt("CALC_LIFESPAN");
 //               double depRate = rs.getDouble("DEP_RATE");
                
				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
				newTransaction.setBranchCode(branchCode);
				newTransaction.setCategoryCode(categoryId);
				newTransaction.setSbuCode(sbuCode);
				newTransaction.setDescription(strDescription);
				newTransaction.setMonthlyDep(monthlyDepr);
				newTransaction.setEffectiveDate(EffectiveDate);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setCostPrice(costPrice);
//				newTransaction.setPostingDate(depDate);
				newTransaction.setTotalnbv(totalnbv);  
				newTransaction.setAccumDep(accumDepr);
				newTransaction.setDeprChargeToDate(depChargeToDate);
				newTransaction.setNbv(nbv);
				newTransaction.setImprovcostPrice(improvCostPrice);
				newTransaction.setImprovaccumDep(improvAccumDep);
				newTransaction.setImprovmonthlyDep(improvMonthDep);
				newTransaction.setImprovnbv(improvNbv);
				newTransaction.setTotalCost(totalCostPrice);
				newTransaction.setDatepurchased(purchaseDate);
				newTransaction.setDeptCode(deptName);
				newTransaction.setBarCode(barCode);
				newTransaction.setSpare1(spare1);
				newTransaction.setAssetSerialNo(serialNo);
//				newTransaction.setCalcLifeSpan(calclifeSpan);
				_list.add(newTransaction);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						closeConnection(conn, ps, rs);
					}
	return _list;
}


public ArrayList<newAssetTransaction> getBidBasePriceRecords(String baseQuery, String branch_Id, String categoryCode) {
    ArrayList<newAssetTransaction> list = new ArrayList<>();
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        c = getConnection();

        StringBuilder query = new StringBuilder(baseQuery);

        System.out.println("<<<<< getBidBasePriceRecords: " + query);
        
        System.out.println("NO Selection in getBidBasePriceRecords: ");

        ps = c.prepareStatement(query.toString());

        rs = ps.executeQuery();

        while (rs.next()) {
            newAssetTransaction newTransaction = new newAssetTransaction();

            newTransaction.setDescription(rs.getString("Description"));
            newTransaction.setCategoryCode(rs.getString("LOCATION_CODE"));
            newTransaction.setLocation(rs.getString("LOCATION"));
            newTransaction.setAssetId(rs.getString("BID_TAG"));
            newTransaction.setAmount(rs.getDouble("BASE_PRICE"));

            list.add(newTransaction);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, ps, rs);
    }

    return list;
}



public ArrayList<newAssetTransaction> getBranchVisitListRecords(String baseQuery, String branch_Id, String fromDate, String toDate) {
    StringBuilder sqlQuery = new StringBuilder(baseQuery);
    List<Object> params = new ArrayList<>();
    ArrayList<newAssetTransaction> list = new ArrayList<>();

    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        c = getConnection();

        if (!branch_Id.equals("0")) {
            sqlQuery.append(" AND A.BRANCH_CODE = ?");
            params.add(branch_Id);
            System.out.println("Branch selected");
        }

        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            sqlQuery.append(" AND a.TRANS_DATE BETWEEN ? AND ?");
            params.add(fromDate);
            params.add(toDate);
            System.out.println("Date selected");
        }

        sqlQuery.append(" ORDER BY a.GROUP_ID");
        System.out.println("<<<<<<<<< SqlQuery: " + sqlQuery);

        ps = c.prepareStatement(sqlQuery.toString());

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        rs = ps.executeQuery();

        // Process result
        while (rs.next()) {
            newAssetTransaction newTransaction = new newAssetTransaction();

            newTransaction.setAction(rs.getString("GROUP_ID"));
            newTransaction.setBranchCode(rs.getString("BRANCH_CODE"));
            newTransaction.setAssetfunction(rs.getString("INSPECTED_BY"));
            newTransaction.setAssetEngineNo(rs.getString("INSPECT_DATE"));
            newTransaction.setAssetLedger(rs.getString("VISIT_SUMMARY"));
            newTransaction.setDatepurchased(rs.getString("TRANS_DATE"));
            newTransaction.setAssetStatus(rs.getString("STATUS"));

            list.add(newTransaction);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, ps, rs);
    }

    return list;
}




public ArrayList<newAssetTransaction> getLedgerExtracts(String baseQuery, String branch_Id, String categoryCode) {
    ArrayList<newAssetTransaction> list = new ArrayList<>();
    List<String> params = new ArrayList<>();
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        c = getConnection();

        StringBuilder query = new StringBuilder(baseQuery);


        if (!branch_Id.equals("0")) {
        	query.append(" AND am_ad_branch.branch_id = ?");
            params.add(branch_Id);
            System.out.println("Branch selected");
        }
        
        if (!categoryCode.equals("0")) {
        	query.append(" AND am_ad_category.category_id =?");
            params.add(categoryCode);
            System.out.println("Category selected");
        }

        ps = c.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setString(i + 1, params.get(i));
        }

        rs = ps.executeQuery();

        while (rs.next()) {
            newAssetTransaction newTransaction = new newAssetTransaction();

            newTransaction.setDescription(rs.getString("FINACLE_EXT_TYPE"));
            newTransaction.setAccumDepLedger(rs.getString("FINACLE_EXT_DR_ACCT"));
            newTransaction.setCategoryCode(rs.getString("FINACLE_EXT_CR_ACCT"));
            newTransaction.setAmount(rs.getDouble("FINACLE_EXT_AMOUNT"));
            newTransaction.setLocation(rs.getString("FINACLE_EXT_NARRATION"));
            newTransaction.setAssetId(rs.getString("FINACLE_EXT_VALUE_DATE"));
            newTransaction.setNewBranchCode(rs.getString("am_gb_company_company_name"));
            

            list.add(newTransaction);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, ps, rs);
    }

    return list;
}


public ArrayList<newAssetTransaction> getSocialEnvironmentListRecords(String baseQuery, String branch_Id, String categoryCode, String fromDate, String toDate) {
	 StringBuilder sqlQuery = new StringBuilder(baseQuery);
	 List<Object> params = new ArrayList<>();
	 ArrayList<newAssetTransaction> list = new ArrayList<>();
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        c = getConnection();

        StringBuilder query = new StringBuilder(baseQuery);

        if (!branch_Id.equals("0")) {
            sqlQuery.append(" AND b.BRANCH_CODE = ?");
            params.add(branch_Id);
            System.out.println("Branch selected");
        }

        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            sqlQuery.append(" AND TRANS_DATE BETWEEN ? AND ?");
            params.add(fromDate);
            params.add(toDate);
            System.out.println("Date selected");
        }
        

        ps = c.prepareStatement(query.toString());

        rs = ps.executeQuery();

        while (rs.next()) {
            newAssetTransaction newTransaction = new newAssetTransaction();

            newTransaction.setBranchName(rs.getString("BRANCH_NAME"));
            newTransaction.setDeptName(rs.getString("Region_Name"));
            newTransaction.setCategoryName(rs.getString("Zone_Name"));
            newTransaction.setBranchCode(rs.getString("BRANCH_CODE"));
            newTransaction.setAmount(rs.getDouble("001"));
            newTransaction.setCostPrice(rs.getDouble("002"));
            newTransaction.setImprovaccumDep(rs.getDouble("003"));
            newTransaction.setImprovcostPrice(rs.getDouble("004"));
            newTransaction.setDepRate(rs.getDouble("005"));
            newTransaction.setDeprChargeToDate(rs.getDouble("006"));
            

            list.add(newTransaction);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection(c, ps, rs);
    }

    return list;
}


public ArrayList<newAssetTransaction> getStockIssuancePostedRecords(String baseQuery, String fromDate, String toDate) {
	 StringBuilder sqlQuery = new StringBuilder(baseQuery);
	 List<String> params = new ArrayList<>();
	 ArrayList<newAssetTransaction> list = new ArrayList<>();
Connection c = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    c = getConnection();

    //StringBuilder query = new StringBuilder(baseQuery);


    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
        sqlQuery.append(" AND a.transaction_date BETWEEN ? AND ?");
        params.add(fromDate);
        params.add(toDate);
        System.out.println("Date selected");
    }
    

    ps = c.prepareStatement(sqlQuery.toString());
    for (int i = 0; i < params.size(); i++) {
        ps.setString(i + 1, params.get(i));
    }
    rs = ps.executeQuery();

    while (rs.next()) {
        newAssetTransaction newTransaction = new newAssetTransaction();

        newTransaction.setAssetId(rs.getString("asset_id"));
        newTransaction.setUserID(rs.getString("User_id"));
        newTransaction.setInitiatorId(rs.getString("initiatorName"));
        newTransaction.setCreditAccountName(rs.getString("creditAccountName"));
        newTransaction.setDebitAccountName(rs.getString("debitAccountName"));
        newTransaction.setSupervisor(rs.getString("supervisorName"));
        newTransaction.setCreditAccount(rs.getString("creditAccount"));
        newTransaction.setDebitAccount(rs.getString("debitAccount"));
        newTransaction.setDescription(rs.getString("Description"));
        newTransaction.setAmount(rs.getDouble("amount"));
        newTransaction.setAssetUser(rs.getString("Full_Name"));
        newTransaction.setTransDate(rs.getString("transaction_date"));
        
        
        list.add(newTransaction);
    }

} catch (Exception e) {
    e.printStackTrace();
} finally {
    closeConnection(c, ps, rs);
}

return list;
}


public ArrayList<newAssetTransaction> getStockIssuanceTransactionsReportRecords(String baseQuery, String branch_Id, String categoryCode, String fromDate, String toDate) {
	 StringBuilder sqlQuery = new StringBuilder(baseQuery);
	 List<String> params = new ArrayList<>();
	 ArrayList<newAssetTransaction> list = new ArrayList<>();
	 Connection c = null;
	 PreparedStatement ps = null;
	 ResultSet rs = null;

try {
    c = getConnection();

    //StringBuilder query = new StringBuilder(baseQuery);

    if (!branch_Id.equals("0")) {
        sqlQuery.append(" AND e.newbranch_id = ?");
        params.add(branch_Id);
        System.out.println("Branch selected");
    }
    
    if (!categoryCode.equals("0")) {
        sqlQuery.append(" AND b.Category_Code =?");
        params.add(categoryCode);
        System.out.println("Category selected");
    }
    

    if (!fromDate.isEmpty() && !toDate.isEmpty()) {
        sqlQuery.append(" AND a.CREATE_DATE BETWEEN ? AND ?");
        params.add(fromDate);
        params.add(toDate);
        System.out.println("Date selected");
    }
    

    ps = c.prepareStatement(sqlQuery.toString());
    for (int i = 0; i < params.size(); i++) {
        ps.setString(i + 1, params.get(i));
    }
    rs = ps.executeQuery();

    while (rs.next()) {
        newAssetTransaction newTransaction = new newAssetTransaction();
        
        
        newTransaction.setProjectCode(rs.getString("PROJECT_CODE"));
        newTransaction.setBarCode(rs.getString("STOCK_CODE"));
        newTransaction.setDescription(rs.getString("description"));
        newTransaction.setCostPrice(rs.getDouble("Cost_Price"));
        newTransaction.setNoofitems(rs.getInt("QUANTITY_DELIVER"));
        newTransaction.setCategoryCode(rs.getString("CATEGORY_CODE"));
        newTransaction.setCategoryName(rs.getString("category_name"));
        
        

        list.add(newTransaction);
    }

} catch (Exception e) {
    e.printStackTrace();
} finally {
    closeConnection(c, ps, rs);
}

return list;
}


public ArrayList<newAssetTransaction> getStockAtHandRecords(String baseQuery) {

	 ArrayList<newAssetTransaction> list = new ArrayList<>();
	 Connection c = null;
	 PreparedStatement ps = null;
	 ResultSet rs = null;

try {
   c = getConnection();


   ps = c.prepareStatement(baseQuery);

   rs = ps.executeQuery();

   while (rs.next()) {
       newAssetTransaction newTransaction = new newAssetTransaction();
       
       newTransaction.setBranchName(rs.getString("BRANCH_NAME"));
       newTransaction.setCategoryName(rs.getString("category_name"));
       newTransaction.setDescription(rs.getString("DESCRIPTION"));
       newTransaction.setNoofitems(rs.getInt("QTY"));
       
       

       list.add(newTransaction);
   }

} catch (Exception e) {
   e.printStackTrace();
} finally {
   closeConnection(c, ps, rs);
}

return list;
}


public ArrayList<newAssetTransaction> getStockClosingRecords(String baseQuery, String branch_Id, String categoryCode, String itemCode) {
	 StringBuilder sqlQuery = new StringBuilder(baseQuery);
	 List<String> params = new ArrayList<>();
	 ArrayList<newAssetTransaction> list = new ArrayList<>();
Connection c = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    c = getConnection();

    //StringBuilder query = new StringBuilder(baseQuery);

    if (!branch_Id.equals("0")) {
        sqlQuery.append(" AND a.Branch_ID  = ?");
        params.add(branch_Id);
        System.out.println("Branch selected");
    }
    
    if (!categoryCode.equals("0")) {
        sqlQuery.append(" AND b.Category_Code =?");
        params.add(categoryCode);
        System.out.println("Category selected");
    }
    
    if (!itemCode.equals("0")) {
        sqlQuery.append(" AND a.ITEM_CODE =?");
        params.add(itemCode);
        System.out.println("itemCode selected");
    }



    ps = c.prepareStatement(sqlQuery.toString());
    for (int i = 0; i < params.size(); i++) {
        ps.setString(i + 1, params.get(i));
    }
    rs = ps.executeQuery();

    while (rs.next()) {
        newAssetTransaction newTransaction = new newAssetTransaction();

        newTransaction.setAssetId(rs.getString("Asset_id"));
        newTransaction.setRegistrationNo(rs.getString("Registration_No"));
        newTransaction.setBranchId(rs.getString("Branch_ID"));
        newTransaction.setDeptName(rs.getString("Dept_ID"));
        newTransaction.setInvoiceNo(rs.getString("Category_ID"));
        newTransaction.setSectionName(rs.getString("Section_id"));
        newTransaction.setDescription(rs.getString("Description"));
        newTransaction.setVendorAC(rs.getString("Vendor_AC"));
        newTransaction.setDatepurchased(rs.getString("Date_purchased"));
        newTransaction.setDepRate(rs.getDouble("Dep_Rate"));
        newTransaction.setAssetMake(rs.getString("Asset_Make"));
        newTransaction.setAssetModel(rs.getString("Asset_Model"));
        newTransaction.setAssetSerialNo(rs.getString("Asset_Serial_No"));
        newTransaction.setAssetEngineNo(rs.getString("Asset_Engine_No"));
        newTransaction.setSupplierName(rs.getString("Supplier_Name"));
        newTransaction.setAssetUser(rs.getString("Asset_User"));
        newTransaction.setAssetMaintenance(rs.getString("Asset_Maintenance"));
        newTransaction.setAccumDep(rs.getDouble("Accum_Dep"));
        newTransaction.setMonthlyDep(rs.getDouble("Monthly_Dep"));
        newTransaction.setCostPrice(rs.getDouble("Cost_Price"));
        newTransaction.setNbv(rs.getDouble("NBV"));
        newTransaction.setDependDate(rs.getString("Dep_End_Date"));
        newTransaction.setDisposalAmount(rs.getDouble("Residual_Value"));
        newTransaction.setAuthorizedBy(rs.getString("Authorized_By"));
        newTransaction.setWhTax(rs.getString("Wh_Tax"));
        newTransaction.setWhTaxValue(rs.getDouble("Wh_Tax_Amount"));
        newTransaction.setOldSection(rs.getString("Req_Redistribution"));
        newTransaction.setPostingDate(rs.getString("Posting_Date"));
        newTransaction.setEffectiveDate(rs.getString("Effective_Date"));
        newTransaction.setPurchaseReason(rs.getString("Purchase_Reason"));
        newTransaction.setUsefullife(rs.getInt("Useful_Life"));
        newTransaction.setSupervisorId(String.valueOf(rs.getInt("Total_Life")));
        newTransaction.setLocation(rs.getString("Location"));
        newTransaction.setRemainLife(rs.getInt("Remaining_Life"));
        newTransaction.setPrevCostPrice(rs.getDouble("Vatable_Cost"));
        newTransaction.setVatValue(rs.getDouble("Vat"));
        newTransaction.setNewBranchCode(rs.getString("Req_Depreciation"));
        newTransaction.setSubjectTOVat(rs.getString("Subject_TO_Vat"));
        newTransaction.setAirCraft(rs.getString("Raise_entry"));
        newTransaction.setPrevAccumDep(rs.getDouble("Dep_Ytd"));
        newTransaction.setNewsbuCode(rs.getString("Section"));
        newTransaction.setAssetStatus(rs.getString("Asset_Status"));
        newTransaction.setState(rs.getString("State"));
        newTransaction.setDriver(rs.getString("Driver"));
        newTransaction.setSpare1(rs.getString("Spare_1"));
        newTransaction.setSpare2(rs.getString("Spare_2"));
        newTransaction.setDisposalDate(rs.getString("Date_Disposed")==null ? "":rs.getString("Date_Disposed"));
        newTransaction.setChargeYear(rs.getString("Province"));
        newTransaction.setMultiple(rs.getString("Multiple"));
        newTransaction.setAssetsighted(rs.getString("WAR_START_DATE"));   
        newTransaction.setProcessingDate(rs.getString("WAR_MONTH"));
        newTransaction.setLegacyPostedDate(rs.getString("WAR_EXPIRY_DATE"));
        newTransaction.setAssetfunction(rs.getString("Last_Dep_Date")==null ? "":rs.getString("Last_Dep_Date"));
        newTransaction.setBranchCode(rs.getString("BRANCH_CODE"));
        newTransaction.setSectionCode(rs.getString("SECTION_CODE"));
        newTransaction.setDeptCode(rs.getString("DEPT_CODE"));
        newTransaction.setCategoryCode(rs.getString("CATEGORY_CODE"));
        newTransaction.setTotalCost(rs.getDouble("AMOUNT_PTD"));
        newTransaction.setProfitAmount(rs.getDouble("AMOUNT_REM"));
        newTransaction.setNewAssetId(rs.getString("PART_PAY"));
        newTransaction.setWipGenerators(rs.getString("FULLY_PAID"));
        newTransaction.setOldDeptCode(rs.getString("GROUP_ID") ==null ? "":rs.getString("GROUP_ID"));
        newTransaction.setBarCode(rs.getString("BAR_CODE"));
        newTransaction.setSbuCode(rs.getString("SBU_CODE"));
        newTransaction.setLpo(rs.getString("LPO"));
        newTransaction.setSupervisor(rs.getString("supervisor")==null ? "":rs.getString("supervisor"));
        newTransaction.setResponse(rs.getString("defer_pay"));
        newTransaction.setOldassetId(rs.getString("OLD_ASSET_ID")==null ? "":rs.getString("OLD_ASSET_ID"));
        newTransaction.setDisposalProceed(rs.getDouble("WHT_PERCENT"));
        newTransaction.setWipOfficeEquipmentComputers(rs.getString("Post_reject_reason")==null ? "":rs.getString("Post_reject_reason"));
        newTransaction.setTransDate(rs.getString("Finacle_Posted_Date")==null ? "":rs.getString("Finacle_Posted_Date"));
        newTransaction.setAssetType(rs.getString("ItemType"));
        newTransaction.setWipMotorVehicles(rs.getString("WAREHOUSE_CODE"));
        newTransaction.setSubcategoryCode(rs.getString("SUB_CATEGORY_CODE"));
        newTransaction.setSpare3(rs.getString("SPARE_3"));
        newTransaction.setSpare4(rs.getString("SPARE_4"));
        newTransaction.setSpare5(rs.getString("SPARE_5"));
        newTransaction.setSpare6(rs.getString("SPARE_6"));
        newTransaction.setMemo(rs.getString("MEMO"));
        newTransaction.setMemovalue(rs.getString("memovalue"));
        newTransaction.setIntegrifyId(rs.getString("INTEGRIFY"));
        newTransaction.setPrevIMPROVCostPrice(rs.getDouble("IMPROV_COST"));
        newTransaction.setImprovaccumDep(rs.getDouble("IMPROV_ACCUMDEP"));
        newTransaction.setPrevIMPROVNBV(rs.getDouble("IMPROV_NBV"));
        newTransaction.setImprovnbv(rs.getDouble("IMPROV_VATABLECOST"));
        newTransaction.setTotalnbv(rs.getDouble("TOTAL_NBV"));
        newTransaction.setDisposeReason(String.valueOf(rs.getInt("IMPROV_USEFULLIFE")));
        newTransaction.setCalcLifeSpan(rs.getInt("IMPROV_TOTALLIFE"));
        newTransaction.setImproveRemainLife(rs.getInt("IMPROV_REMAINLIFE"));
        newTransaction.setPrevMonthlyDep(rs.getDouble("IMPROV_MONTHLYDEP")); 
        newTransaction.setNoofitems(rs.getInt("QUANTITY"));
        newTransaction.setNewSectionCode(rs.getString("UNIT_CODE"));
        newTransaction.setAssetCode(rs.getString("ASSET_CODE"));
        newTransaction.setOldBranchCode(rs.getString("REGION_CODE"));
        newTransaction.setOldDeptId(rs.getString("ZONE_CODE"));
        newTransaction.setProjectCode(rs.getString("PROJECT_CODE"));
        newTransaction.setImprovcostPrice(rs.getDouble("UNIT_PRICE"));
        newTransaction.setOldsbuCode(rs.getString("MTID"));
        newTransaction.setOldSectionCode(rs.getString("COMP_CODE"));
        newTransaction.setSystemIp(rs.getString("ITEM_CODE"));
        newTransaction.setApprovalStatus(rs.getString("STATUS"));
        newTransaction.setErrormessage(rs.getString("ITEMTYPE_CODE"));
        newTransaction.setAction(rs.getString("TAX_CODE"));
        newTransaction.setLifeSpan(rs.getDouble("MIN_QUANTITY"));
        newTransaction.setNbvDifference(rs.getDouble("WEIGHT_AVG_COST"));
        newTransaction.setPrevMonthlyDifference(rs.getDouble("STANDARD_COST"));
        newTransaction.setPrevNBV(rs.getDouble("WEIGHT"));
        newTransaction.setInitiatorId(rs.getString("BACKORDERABLE"));
        newTransaction.setUserID(rs.getString("USERID"));
        newTransaction.setWipLand(rs.getString("COG_SOLD"));
        newTransaction.setWipComputerSoftware(rs.getString("INVENTORY_ACCT"));     
        newTransaction.setComments(String.valueOf(rs.getInt("REORDER_LEVEL")));
        newTransaction.setGlAccount(rs.getString("REQ_APPROVAL"));
        newTransaction.setQrCode(String.valueOf(rs.getInt("MAX_APPROVE_LEVEL")));
        newTransaction.setMonthlyDifference(rs.getDouble("FIFO"));
        newTransaction.setWipGeneratorsHouse(rs.getString("MEASURING_CODE"));
        newTransaction.setOldCategoryCode(rs.getString("category_ID"));
        newTransaction.setCategoryName(rs.getString("category_name"));
        newTransaction.setLand(rs.getString("category_acronym"));
        newTransaction.setNewDeptCode(rs.getString("Required_for_fleet"));
        newTransaction.setAssetLedger(rs.getString("Asset_Ledger"));
        newTransaction.setDepLedger(rs.getString("Dep_ledger"));
        newTransaction.setAccumDepLedger(rs.getString("Accum_Dep_ledger"));
        newTransaction.setOldBranchId(rs.getString("Category_Status"));
        newTransaction.setOldAssetUser(rs.getString("create_date"));
        
        
       

        list.add(newTransaction);
    }

} catch (Exception e) {
    e.printStackTrace();
} finally {
    closeConnection(c, ps, rs);
}

return list;
}

}
