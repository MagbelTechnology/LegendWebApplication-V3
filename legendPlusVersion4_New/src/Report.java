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

import com.magbel.dao.PersistenceServiceDAO;
import com.magbel.legend.vao.newAssetTransaction;

import legend.admin.objects.User;
import magma.StockRecordsBean;
import magma.net.vao.Asset;
import magma.net.vao.FMppmAllocation;
import magma.net.vao.FleetManatainanceRecord;

/**
 *
 *  @author Kareem Wasiu Aderemi
 */
public class Report extends PersistenceServiceDAO {



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
                String maintainBy = rs.getString("MaintainedBy");
                
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
                String regionCode = rs.getString("region_code");
                String zoneCode = rs.getString("zone_code");
                String regionRestrict = "Y";
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
                user.setFleetAdmin(fleetAdmin);
                user.setEmail(email);
                user.setBranch(branchCode);
                user.setUserStatus(userStatus);
                user.setPwdChanged(pwdChanged);
                user.setRegionCode(regionCode);
                user.setZoneCode(zoneCode);
                user.setRegionRestrict(regionRestrict);
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
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE")){
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("BRANCH_CODE")){
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	        	  s.setString(3, branch_Id);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("CATEGORY_CODE")){
	        	  s.setString(1, chargeYear);
	        	  s.setString(2, reportDate);
	        	  s.setString(3, categoryId);
	          }
	          if(query.contains("CHARGEYEAR") && query.contains("DEP_DATE") && query.contains("BRANCH_CODE") && query.contains("CATEGORY_CODE")){
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

public java.util.ArrayList getLegacyAssetGrpPostRecords(String query,String branchCode, String groupid,String subjectTovat,String subjectTowhTax)
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
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("S")){
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
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("F")){
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
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("S")){
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
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("F")){
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
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("N") && subjectTowhTax.equalsIgnoreCase("N")){
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
	          }	 
	          if(query.contains("s.GROUP_ID") && query.contains("group_id") && subjectTovat.equalsIgnoreCase("Y") && subjectTowhTax.equalsIgnoreCase("N")){
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
//                String branchCode = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
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
	        	  s.setString(3, FromDate);
	        	  s.setString(4, ToDate);
	        	  s.setString(5, FromDate);
	        	  s.setString(6, ToDate);
	        	  s.setString(7, FromDate);
	        	  s.setString(8, ToDate);
	        	  s.setString(9, FromDate);
	        	  s.setString(10, ToDate);
	          }	
				 if(!branch_Id.equals("0") && categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("")) {
//				System.out.println("Branch and Date Selection in getAssetRegisterRecords: ");
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
	        	  s.setString(13, branch_Id);
	        	  s.setString(14, FromDate);
	        	  s.setString(15, ToDate);
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
	        	  s.setString(9, branch_Id);
	        	  s.setString(10, categoryCode);
	        	  s.setString(11, FromDate);
	        	  s.setString(12, ToDate);
	        	  s.setString(13, branch_Id);
	        	  s.setString(14, categoryCode);
	        	  s.setString(15, FromDate);
	        	  s.setString(16, ToDate);
	        	  s.setString(17, branch_Id);
	        	  s.setString(18, categoryCode);
	        	  s.setString(19, FromDate);
	        	  s.setString(20, ToDate);
				 } 				 
				 if(!categoryCode.equals("0") && !FromDate.equals("") && !ToDate.equals("") && branch_Id.equals("0")) {
//				System.out.println("Category  and Date Selection in getAssetRegisterRecords: ");
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
	        	  s.setString(13, categoryCode);
	        	  s.setString(14, FromDate);
	        	  s.setString(15, ToDate);
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
				String accountNo = rs.getString("ACCOUNT");
				String recType = rs.getString("recType");
				double costPrice = rs.getDouble("AMOUNT");
				String transType = rs.getString("transType");
                String GROUP_ID = rs.getString("category_name");
//                String branchCode = rs.getString("GROUP_ID");
//                String I = rs.getString("I");

				newAssetTransaction newTransaction = new newAssetTransaction();
				newTransaction.setAssetId(StrassetId);
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




}
