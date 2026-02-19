package magma.net.manager;

import com.magbel.util.DataConnect;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import magma.ExcelAssetReconcileBean;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.GroupAsset;
import legend.admin.objects.User;
/**
 * <p>Title: GroupAssetManager.java</p>
 *
 * <p>Description:  </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class GroupAssetManager extends MagmaDBConnection {

    SimpleDateFormat sdf;
    private boolean overFlow;

    public GroupAssetManager() {
        super();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    /**
     * createMaintenanceRecord
     *
     * @param type String
     * @param dateOfRepair String
     * @param technicianType String
     * @param milleageBeforeMaintenace double
     * @param milleageAfterMaintenace double
     * @param details String
     * @param cost double
     * @param componentReplaced String
     * @param lastPerformedDate String
     * @param nextPerformedDate String
     * @param maintenanceType String
     */
     //modified by olabo to add invoiceNo and hist_id columns
    public void createGroupAsset() {

        Connection con = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO AM_GROUP_ASSET("+
					"REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID]"+
			")	VALUES(";


            try {
                con = getConnection("legendPlus");
                ps = con.prepareStatement(query);


                ps.execute();
            } catch (Exception e) {
                System.out.println("INFO:Error creating Group Asset ->" +
                                   e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
        }


    public ArrayList findAllGroupAsset(){
	    String queryFilter = "";
	    return findGroupAssetByQuery(queryFilter);
	}

	public ArrayList findGroupAssetByCategoryId(String catId){

		String queryFilter = " AND CATEGORY_ID = "+catId+"";
		ArrayList list = findGroupAssetByQuery(queryFilter);

		return list;

	}

	public GroupAsset findGroupAssetById(String id){
		GroupAsset finder = null;
		String queryFilter = " AND ASSET_ID = '"+id+"'";
		ArrayList list = findGroupAssetByQuery(queryFilter);
		if(list.size() > 0){
			finder = (GroupAsset)list.get(0);
		}

		return finder;
	}

    /**
     * findGroupAssetByQuery
     *
     * @param id String
     * @return FleetManatainanceRecord
     */
    public ArrayList findGroupAssetByQuery(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GroupAsset groupAsset = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
        			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE,ASSET_CODE  "+
					"FROM AM_GROUP_ASSET WHERE ASSET_ID != ''  " + queryFilter;
        
//        System.out.println("selectQuery : " + selectQuery);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String description = rs.getString("DESCRIPTION");

			   String branchCode = rs.getString("BRANCH_ID");
			   String deptCode = rs.getString("DEPT_ID");
			   String sectionCode = rs.getString("SECTION_ID");
			   String category = rs.getString("CATEGORY_ID");
			   double costPrice = rs.getDouble("COST_PRICE");
			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
			   double accumulatedDepreciation = 0.00d;//rs.getString("");
			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

			   String make = rs.getString("ASSET_MAKE");
			   String model = rs.getString("ASSET_MODEL");
			   String tax = rs.getString("WH_TAX");
			   String taxAmount = rs.getString("BRANCH_ID");
  			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
  			   String vat = rs.getString("VAT");
  			   double vatableCost = rs.getDouble("VATABLE_COST");
  			   String process_flag = rs.getString("PROCESS_FLAG");
  			   String sbu_code = rs.getString("SBU_CODE");
  			   String assetcode = rs.getString("ASSET_CODE");
                groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                deptCode,sectionCode, category,costPrice, dateCreated,
                accumulatedDepreciation, depreciationEndDate, make,
                model, tax, taxAmount,  maintainBy , vat, vatableCost);
                groupAsset.setProcess_flag(process_flag);
                groupAsset.setSbu_code(sbu_code);
                groupAsset.setAsset_code(assetcode);
                finder.add(groupAsset);
                
            }
        } catch (Exception e) {
            System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }
    
    
    public String processFlagStatus(String qry)
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status="";
        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(qry);
            rs = ps.executeQuery();
            while (rs.next())
            {
            	status = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error in GroupAssetManager fetching processFlagStatus ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
    	return status;
    }
    
    public ArrayList findGroupAssetByQuery_posting(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GroupAsset groupAsset = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
        			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE ,post_flag,reject_reason "+
					"FROM AM_GROUP_ASSET WHERE ASSET_ID != ''  " + queryFilter;
        
       // System.out.println("selectQuery in findGroupAssetByQuery_posting : " + selectQuery);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String description = rs.getString("DESCRIPTION");

			   String branchCode = rs.getString("BRANCH_ID");
			   String deptCode = rs.getString("DEPT_ID");
			   String sectionCode = rs.getString("SECTION_ID");
			   String category = rs.getString("CATEGORY_ID");
			   double costPrice = rs.getDouble("COST_PRICE");
			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
			   double accumulatedDepreciation = 0.00d;//rs.getString("");
			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));
			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
			   String make = rs.getString("ASSET_MAKE");
			   String model = rs.getString("ASSET_MODEL");
			   String tax = rs.getString("WH_TAX");
			   String taxAmount = rs.getString("BRANCH_ID");
  			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
  			   String vat = rs.getString("VAT");
  			   double vatableCost = rs.getDouble("VATABLE_COST");
  			   String process_flag = rs.getString("PROCESS_FLAG");
  			   String sbu_code = rs.getString("SBU_CODE");
  			   String post_flag= rs.getString("post_flag");
  			   String reject_reason=rs.getString("reject_reason");
  			   
                groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                deptCode,sectionCode, category,costPrice, dateCreated,
                accumulatedDepreciation, depreciationEndDate, make,
                model, tax, taxAmount,  maintainBy , vat, vatableCost);
                groupAsset.setProcess_flag(process_flag);
                groupAsset.setSbu_code(sbu_code);
                groupAsset.setPost_flag(post_flag);
                groupAsset.setReject_reason(reject_reason);
                groupAsset.setDate_of_purchase(purchaseDate);
                groupAsset.setDepreciation_start_date(depreciationStartDate);
                finder.add(groupAsset);

            }

        } catch (Exception e) {
            System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

      public ArrayList findGroupAssetByQueryBranch(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GroupAsset groupAsset = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
        			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE  "+
					"FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE ASSET_ID != ''  " + queryFilter;

        //System.out.println("selectQuery : " + selectQuery);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String description = rs.getString("DESCRIPTION");

			   String branchCode = rs.getString("BRANCH_ID");
			   String deptCode = rs.getString("DEPT_ID");
			   String sectionCode = rs.getString("SECTION_ID");
			   String category = rs.getString("CATEGORY_ID");
			   double costPrice = rs.getDouble("COST_PRICE");
			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
			   double accumulatedDepreciation = 0.00d;//rs.getString("");
			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

			   String make = rs.getString("ASSET_MAKE");
			   String model = rs.getString("ASSET_MODEL");
			   String tax = rs.getString("WH_TAX");
			   String taxAmount = rs.getString("BRANCH_ID");
  			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
  			   String vat = rs.getString("VAT");
  			   double vatableCost = rs.getDouble("VATABLE_COST");
  			   String process_flag = rs.getString("PROCESS_FLAG");
  			   String sbu_code = rs.getString("SBU_CODE");

                groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                deptCode,sectionCode, category,costPrice, dateCreated,
                accumulatedDepreciation, depreciationEndDate, make,
                model, tax, taxAmount,  maintainBy , vat, vatableCost);
                groupAsset.setProcess_flag(process_flag);
                groupAsset.setSbu_code(sbu_code);

                finder.add(groupAsset);

            }

        } catch (Exception e) {
            System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }

      public ArrayList findGroupAsset2ByQueryBranch(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GroupAsset groupAsset = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
        			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE  "+
					"FROM AM_GROUP_ASSET2_UNCAPITALIZED WHERE ASSET_ID != ''  " + queryFilter;

        //System.out.println("selectQuery : " + selectQuery);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String description = rs.getString("DESCRIPTION");

			   String branchCode = rs.getString("BRANCH_ID");
			   String deptCode = rs.getString("DEPT_ID");
			   String sectionCode = rs.getString("SECTION_ID");
			   String category = rs.getString("CATEGORY_ID");
			   double costPrice = rs.getDouble("COST_PRICE");
			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
			   double accumulatedDepreciation = 0.00d;//rs.getString("");
			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

			   String make = rs.getString("ASSET_MAKE");
			   String model = rs.getString("ASSET_MODEL");
			   String tax = rs.getString("WH_TAX");
			   String taxAmount = rs.getString("BRANCH_ID");
  			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
  			   String vat = rs.getString("VAT");
  			   double vatableCost = rs.getDouble("VATABLE_COST");
  			   String process_flag = rs.getString("PROCESS_FLAG");
  			   String sbu_code = rs.getString("SBU_CODE");

                groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                deptCode,sectionCode, category,costPrice, dateCreated,
                accumulatedDepreciation, depreciationEndDate, make,
                model, tax, taxAmount,  maintainBy , vat, vatableCost);
                groupAsset.setProcess_flag(process_flag);
                groupAsset.setSbu_code(sbu_code);

                finder.add(groupAsset);

            }

        } catch (Exception e) {
            System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset2 Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }


          public ArrayList findGroupAssetByQuery_postingBranch(String queryFilter) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GroupAsset groupAsset = null;
        ArrayList finder = new ArrayList();
        String selectQuery =
        			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
					"ASSET_USER,ASSET_MAINTENANCE,"+
					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
					"POSTING_DATE,EFFECTIVE_DATE,"+
					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE ,post_flag,reject_reason "+
					"FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE ASSET_ID != ''  " + queryFilter;

       // System.out.println("selectQuery in findGroupAssetByQuery_posting : " + selectQuery);

        try {
            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

				String id = rs.getString("ASSET_ID");
				String registrationNo = rs.getString("REGISTRATION_NO");
				String description = rs.getString("DESCRIPTION");

			   String branchCode = rs.getString("BRANCH_ID");
			   String deptCode = rs.getString("DEPT_ID");
			   String sectionCode = rs.getString("SECTION_ID");
			   String category = rs.getString("CATEGORY_ID");
			   double costPrice = rs.getDouble("COST_PRICE");
			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
			   double accumulatedDepreciation = 0.00d;//rs.getString("");
			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

			   String make = rs.getString("ASSET_MAKE");
			   String model = rs.getString("ASSET_MODEL");
			   String tax = rs.getString("WH_TAX");
			   String taxAmount = rs.getString("BRANCH_ID");
  			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
  			   String vat = rs.getString("VAT");
  			   double vatableCost = rs.getDouble("VATABLE_COST");
  			   String process_flag = rs.getString("PROCESS_FLAG");
  			   String sbu_code = rs.getString("SBU_CODE");
  			   String post_flag= rs.getString("post_flag");
  			   String reject_reason=rs.getString("reject_reason");

                groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                deptCode,sectionCode, category,costPrice, dateCreated,
                accumulatedDepreciation, depreciationEndDate, make,
                model, tax, taxAmount,  maintainBy , vat, vatableCost);
                groupAsset.setProcess_flag(process_flag);
                groupAsset.setSbu_code(sbu_code);
                groupAsset.setPost_flag(post_flag);
                groupAsset.setReject_reason(reject_reason);
                finder.add(groupAsset);

            }

        } catch (Exception e) {
            System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                               e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return finder;
    }
          public ArrayList findUncapitalizedGroupAssetByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE,ASSET_CODE  "+
      					"FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:findUncapitalizedGroupAssetByQuery-Error Fetching Group Uncapitalized Asset Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              //System.out.println("Finished with INFO:findUncapitalizedGroupAssetByQuery : " );
              return finder;
          }
 
          public ArrayList findGroupStockByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE,ASSET_CODE,  "+
      					"WAREHOUSE_CODE,ITEMTYPE, ITEM_CODE,QUANTITY,UNIT_CODE " +
      					"FROM AM_GROUP_STOCK WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
        			   String warehouseCode = rs.getString("WAREHOUSE_CODE");
        			   String itemType = rs.getString("ITEMTYPE");
        			   String itemCode = rs.getString("ITEM_CODE");
        			   String unitCode = rs.getString("UNIT_CODE");
        			   int quantity = rs.getInt("QUANTITY");
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      groupAsset.setWarehouseCode(warehouseCode);
                      groupAsset.setItemType(itemType);
                      groupAsset.setItemCode(itemCode);
                      groupAsset.setUnitCode(unitCode);
                      groupAsset.setQuantity(quantity);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetImproveByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAssetImproveByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date,"+
      					"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost,"+
      					"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep,"+
      					"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount,"+
      					"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no,"+
      					"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST,"+
      					"IMPROVED,PROJECT_CODE,LOCATION  "+
      					"FROM AM_GROUP_IMPROVEMENT WHERE ASSET_ID != ''  " + queryFilter;
              
//              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
   //   				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_CODE");
      			 //  String deptCode = rs.getString("DEPT_ID");
      			//   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_CODE");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   double oldcostPrice = rs.getDouble("old_cost_price");
      			   String dateCreated = formatDate(rs.getDate("REVALUE_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      	//		   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			//   String make = rs.getString("ASSET_MAKE");
      			//   String model = rs.getString("ASSET_MODEL");
      		//	   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_CODE");
        			   String maintainBy  = rs.getString("wht_amount");
        			   String vat = rs.getString("vat_amount");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("IMPROVED");
        			//   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,"",description,branchCode,
                      "","", category,costPrice, dateCreated,
                      accumulatedDepreciation, "", "",
                      "", "", taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setOldcost_price(oldcostPrice);;
  //                    groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Improvement Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetReconciliationByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAssetReconciliationByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
  			"SELECT RECONCILE_ID,ASSET_ID,BAR_CODE,RECONCILE_DATE,"+
  			"SUB_CATEGORY_ID,Dept_ID,Section_id,Location,State,Registration_No,Asset_Make,Asset_Model,Asset_Serial_No,"+
  			"Asset_Engine_No,SBU_CODE,Vendor_AC,Date_purchased,Dep_End_Date,Effective_Date,Spare_1,Spare_2,"+
  			"SPARE_3,SPARE_4,SPARE_5,SPARE_6,LPO,SUB_CATEGORY_CODE,DEPT_CODE,SECTION_CODE,"+
			"USER_ID,raise_entry,BRANCH_CODE,EXIST_BRANCH_CODE,DESCRIPTION,ASSET_CODE,STATUS  "+
			"FROM AM_GROUP_RECONCILIATION WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_CODE");
      			   String existBranchCode = rs.getString("EXIST_BRANCH_CODE");
      			   String assetCode = rs.getString("ASSET_CODE");
      			   String reconcileDate = formatDate(rs.getDate("RECONCILE_DATE"));
      			   String barCode = rs.getString("BAR_CODE");
                   String subCatId = rs.getString("SUB_CATEGORY_ID");
                   String deptId = rs.getString("Dept_ID");
                   String sectionId = rs.getString("Section_id");
                   String location = rs.getString("Location");
                   String state = rs.getString("State");
                   String registrationNo = rs.getString("Registration_No");
                   String make = rs.getString("Asset_Make");
                   String model = rs.getString("Asset_Model");
                   String serialNo = rs.getString("Asset_Serial_No");
                   String endgineNo = rs.getString("Asset_Engine_No");
                   String sbuCode = rs.getString("SBU_CODE");
                   String vendorAcct = rs.getString("Vendor_AC");
                   String puchaseDate = rs.getString("Date_purchased");
                   String deprEndDate = rs.getString("Dep_End_Date");
                   String deprStartDate = rs.getString("Effective_Date");
                   String spare1 = rs.getString("Spare_1");
                   String spare2 = rs.getString("Spare_2");
                   String spare3 = rs.getString("Spare_3");
                   String spare4 = rs.getString("Spare_4");
                   String spare5 = rs.getString("Spare_5");
                   String spare6 = rs.getString("Spare_6");
                   String lpo = rs.getString("LPO");
                   String subCatCode = rs.getString("SUB_CATEGORY_CODE");
                   String deptCode = rs.getString("DEPT_CODE");
                   String sectionCode = rs.getString("SECTION_CODE");      			   
                   groupAsset = new GroupAsset();
                   groupAsset.setAsset_id(id);;
                   groupAsset.setDescription(description);
                   groupAsset.setBranch_code(branchCode);
                   groupAsset.setAsset_code(assetCode);
                   groupAsset.setExistBranchCode(existBranchCode);
                   groupAsset.setBar_code(barCode);
                   groupAsset.setDateCreated(reconcileDate);
                   groupAsset.setSubCategory_id(subCatId);
                   groupAsset.setDepartment_id(deptId);
                   groupAsset.setSection_id(sectionId);
                   groupAsset.setLocation(location);
                   groupAsset.setState(state);
                   groupAsset.setRegistrationNo(registrationNo);
                   groupAsset.setMake(make);
                   groupAsset.setModel(model);
                   groupAsset.setSerial_number(serialNo);
                   groupAsset.setEngine_number(endgineNo);
                   groupAsset.setSbu_code(sbuCode);
                   groupAsset.setVendorAccount(vendorAcct);
                   groupAsset.setDate_of_purchase(puchaseDate);
                   groupAsset.setDepreciation_end_date(deprEndDate);
                   groupAsset.setDepreciation_start_date(deprStartDate);
                   groupAsset.setSpare1(spare1);
                   groupAsset.setSpare2(spare2);
                   groupAsset.setSpare3(spare3);
                   groupAsset.setSpare4(spare4);
                   groupAsset.setSpare5(spare5);
                   groupAsset.setSpare6(spare6);
                   groupAsset.setLpo_no(lpo);
                   groupAsset.setSubCategory_code(subCatCode);
                   groupAsset.setDepartment_code(deptCode);
                   groupAsset.setSection_code(sectionCode);
                   finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:findGroupAssetReconciliationByQuery in GroupAssetManager-Error Fetching GroupAsset Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          
          public ArrayList findGroupFleetByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT GROUP_ID,TRANS_TYPE,BRANCH_ID,BRANCH,ASSET_ID,BAR_CODE,REGISTRATION_NO,LICENCE_NO,FIRST_DATE_OBTAINED,"+
      					"NEXT_DATE_OBTAINED,COST_PRICE,POSTING_DATE,USER_ID,DESCRIPTION,SBU_CODE,VENDOR_ID,VENDOR_ACCT,STATUS "+
      					"FROM FT_GROUP_DUE_PERIOD WHERE ASSET_ID != ''  " + queryFilter;
              
//              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {
                	 String groupId = rs.getString("GROUP_ID");
      				String id = rs.getString("ASSET_ID");
   //   				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String license = rs.getString("LICENCE_NO");
      				String transType = rs.getString("TRANS_TYPE");
      				String barCode = rs.getString("BAR_CODE");
      				
      			    String branchCode = rs.getString("BRANCH");
      			    String branchId = rs.getString("BRANCH_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
  //    			   double oldcostPrice = rs.getDouble("old_cost_price");
      			   String nextDateObtained = formatDate(rs.getDate("NEXT_DATE_OBTAINED"));
      			 String firstDateObtained = formatDate(rs.getDate("FIRST_DATE_OBTAINED"));
      			String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			String vendorId = rs.getString("VENDOR_ID");
      			String vendorAcct = rs.getString("VENDOR_ACCT");
      			String sbuCode = rs.getString("SBU_CODE");
        			   String status = rs.getString("STATUS");
                      groupAsset = new GroupAsset();
                      groupAsset.setId(id);
                      groupAsset.setDescription(description);
                      groupAsset.setCostPrice(costPrice);
                      groupAsset.setFirstDateObtained(firstDateObtained);
                      groupAsset.setNextDateObtained(nextDateObtained);
                      groupAsset.setDateCreated(dateCreated);
                      groupAsset.setTransType(transType);
                      groupAsset.setBar_code(barCode);
                      groupAsset.setRegistrationNo(registrationNo);
                      groupAsset.setLicense(license);
                      groupAsset.setStatus(status);
                      groupAsset.setBranchCode(branchCode);
                      groupAsset.setBranchId(branchId);
                      groupAsset.setVendorAccount(vendorAcct);
                      groupAsset.setVendorId(vendorId);
                      groupAsset.setSbu_code(sbuCode);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Group Fleet ByQuery  ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
                     

          /**
           * findGroupAsset2ByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAsset2ByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE,ASSET_CODE  "+
      					"FROM AM_GROUP_ASSET2 WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:findGroupAsset2ByQuery in GroupAssetManager-Error Fetching GroupAsset2 Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          

          /**
           * findSBUUploadByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findSBUUploadByQuery() {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT SBU_CODE,SBU_NAME,SBU_CONTACT,CONTACT_EMAIL,STATUS "+
      					"FROM Sbu_SetUp_Upload  ";
              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {
                    String sbu_code = rs.getString("SBU_CODE");
      				String registrationNo = rs.getString("SBU_CONTACT");
      				String description = rs.getString("SBU_NAME");
      			   String bar_code = rs.getString("CONTACT_EMAIL");
      			   String transType = rs.getString("STATUS");
                      groupAsset = new GroupAsset();
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setDescription(description);
                      groupAsset.setRegistrationNo(registrationNo);
                      groupAsset.setBar_code(bar_code);
                      groupAsset.setTransType(transType);
                      finder.add(groupAsset);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching SBU Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          
          

          /**
           * findUserUploadByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findUserUploadByQuery() {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              User user = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT User_Name,Full_Name,Legacy_Sys_id,Class,Branch,dept_code,email,region_code,zone_code "+
      					"FROM am_gb_User_Upload  ";
              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {
                    String userName = rs.getString("User_Name");
      				String fullName = rs.getString("Full_Name");
      				String legacySystemId = rs.getString("Legacy_Sys_id");
      				String userClass = rs.getString("Class");
      			   String deptCode = rs.getString("dept_code");
      			   String branch = rs.getString("Branch");
      			   String email = rs.getString("email");
      			   String regionCode = rs.getString("region_code");
      			   String zoneCode = rs.getString("zone_code");
                      user = new User();
                      user.setUserName(userName);
                      user.setUserFullName(fullName);
                      user.setLegacySystemId(legacySystemId);
                      user.setUserClass(userClass);
                      user.setBranch(branch);
                      user.setDeptCode(deptCode);
                      user.setEmail(email);
                      user.setRegionCode(regionCode);
                      user.setZoneCode(zoneCode);
                      finder.add(user);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching SBU Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findBulkAssetTransferByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findBulkAssetTransferByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date,"+
      					"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost,"+
      					"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep,"+
      					"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount,"+
      					"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no,"+
      					"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST,"+
      					"IMPROVED,PROJECT_CODE,LOCATION  "+
      					"FROM AM_GROUP_IMPROVEMENT WHERE ASSET_ID != ''  " + queryFilter;
              
//              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
   //   				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_CODE");
      			 //  String deptCode = rs.getString("DEPT_ID");
      			//   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_CODE");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   double oldcostPrice = rs.getDouble("old_cost_price");
      			   String dateCreated = formatDate(rs.getDate("REVALUE_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      	//		   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			//   String make = rs.getString("ASSET_MAKE");
      			//   String model = rs.getString("ASSET_MODEL");
      		//	   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_CODE");
        			   String maintainBy  = rs.getString("wht_amount");
        			   String vat = rs.getString("vat_amount");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("IMPROVED");
        			//   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,"",description,branchCode,
                      "","", category,costPrice, dateCreated,
                      accumulatedDepreciation, "", "",
                      "", "", taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setOldcost_price(oldcostPrice);;
  //                    groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Improvement Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupStockByQueryList(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],SBU_CODE,ASSET_CODE  "+
      					"FROM ST_STOCK WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			//   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
                 //     groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);
                      
                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupStock Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          
          
          public ArrayList findGroupStockByQuery_posting(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
/*              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE ,post_flag,reject_reason "+
      					"FROM AM_GROUP_STOCK WHERE ASSET_ID != ''  " + queryFilter;*/
              String selectQuery =
            			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
    					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
    					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
    					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
    					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
    					"ASSET_USER,ASSET_MAINTENANCE,"+
    					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
    					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
    					"POSTING_DATE,EFFECTIVE_DATE,"+
    					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
    					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
    					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
    					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID]SBU_CODE "+
    					"FROM ST_STOCK WHERE ASSET_ID != ''  " + queryFilter;              
              //System.out.println("selectQuery in findGroupAssetByQuery_posting : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        	//		   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        	//		   String post_flag= rs.getString("post_flag");
        	//		   String reject_reason=rs.getString("reject_reason");
        			   
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
    //                  groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
    //                  groupAsset.setPost_flag(post_flag);
     //                 groupAsset.setReject_reason(reject_reason);
                      finder.add(groupAsset);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          
          public ArrayList findGroupAssetBidByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null; 
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID,"+
      					"CATEGORY_ID,SECTION_ID,DESCRIPTION,"+
      					"VENDOR_AC,DATE_PURCHASED,DEP_RATE,"+
      					"ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"+
      					"ASSET_ENGINE_NO,SUPPLIER_NAME,"+
      					"ASSET_USER,ASSET_MAINTENANCE,"+
      					"COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"+
      					"AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"+
      					"POSTING_DATE,EFFECTIVE_DATE,"+
      					"PURCHASE_REASON,LOCATION,VATABLE_COST,"+
      					"VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"+
      					"WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"+
      					"EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID],PROCESS_FLAG,SBU_CODE,ASSET_CODE  "+
      					"FROM AM_GROUP_ASSET_BID WHERE ASSET_ID != ''  " + queryFilter;
              
              //System.out.println("selectQuery in findGroupAssetBidByQuery: " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
      				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_ID");
      			   String deptCode = rs.getString("DEPT_ID");
      			   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_ID");
      			   double costPrice = rs.getDouble("COST_PRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");
      			   String depreciationEndDate = formatDate(rs.getDate("DEP_END_DATE"));

      			   String make = rs.getString("ASSET_MAKE");
      			   String model = rs.getString("ASSET_MODEL");
      			   String tax = rs.getString("WH_TAX");
      			   String taxAmount = rs.getString("BRANCH_ID");
        			   String maintainBy  = rs.getString("WH_TAX_AMOUNT");
        			   String vat = rs.getString("VAT");
        			   double vatableCost = rs.getDouble("VATABLE_COST");
        			   String process_flag = rs.getString("PROCESS_FLAG");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
                      groupAsset = new GroupAsset(id,registrationNo,description,branchCode,
                      deptCode,sectionCode, category,costPrice, dateCreated,
                      accumulatedDepreciation, depreciationEndDate, make,
                      model, tax, taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
                      groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      finder.add(groupAsset);
                      
                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records for Bid ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAssetVerificationByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT a.ASSET_ID,a.BRANCH_ID,a.DESCRIPTION,a.ASSET_SERIAL_NO,"+
      					"a.SPARE_1,a.USER_ID,PROCESS_FLAG,a.BAR_CODE,a.POSTING_DATE,a.BRANCH_CODE,b.CATEGORY_CODE  "+
      					"FROM AM_GROUP_ASSET_VERIFICATION a,AM_ASSET b WHERE a.ASSET_ID = b.ASSET_ID and a.ASSET_ID != ''  " + queryFilter;
              
              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String assetId = rs.getString("ASSET_ID");
      				String description = rs.getString("DESCRIPTION");

      			   String branchId = rs.getString("BRANCH_ID");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));

      			   String serialNo = rs.getString("ASSET_SERIAL_NO");
      			   String component = rs.getString("SPARE_1");
    			   String processFlag = rs.getString("PROCESS_FLAG");
    			   String bar_code = rs.getString("BAR_CODE");
    			   String branchCode = rs.getString("BRANCH_CODE");
    			   String categoryCode = rs.getString("CATEGORY_CODE");
                  groupAsset = new GroupAsset();
                  groupAsset.setAsset_id(assetId);
                  groupAsset.setDescription(description);
                  groupAsset.setBranch_code(branchCode);
                  groupAsset.setProcess_flag(processFlag);
                  groupAsset.setBranchId(branchId);
                  groupAsset.setBar_code(bar_code);
                  groupAsset.setSpare1(component);
                  groupAsset.setSerial_number(serialNo);
                  groupAsset.setPostingDate(dateCreated);
                  groupAsset.setCategory_code(categoryCode);
                  finder.add(groupAsset);
                      
                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Records in findGroupAssetVerificationByQuery ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAssetVerifyByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT ASSET_ID,BRANCH_ID,BRANCH_CODE,CATEGORY_CODE,DESCRIPTION,SPARE_1,ASSET_SERIAL_NO,BAR_CODE,PROCESS_FLAG,"+
      					"POSTING_DATE,ASSET_STATUS "+
      					"FROM AM_GROUP_ASSET_VERIFICATION WHERE ASSET_ID != ''  " + queryFilter;
              
//              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String assetId = rs.getString("ASSET_ID");
      				String branchCode = rs.getString("BRANCH_CODE");
      				String description = rs.getString("DESCRIPTION");

      			   String branchId = rs.getString("BRANCH_ID");

      			   String categoryCode = rs.getString("CATEGORY_CODE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   String spare1 = rs.getString("SPARE_1");
      			   String serialNo = rs.getString("ASSET_SERIAL_NO");
      			   String barCode = rs.getString("BAR_CODE");
      			   String processFlag = rs.getString("PROCESS_FLAG");
      			   String assetStatus = rs.getString("ASSET_STATUS");

                      groupAsset = new GroupAsset();
                      groupAsset.setAsset_id(assetId);
                      groupAsset.setDescription(description);
                      groupAsset.setBranch_code(branchCode);
                      groupAsset.setDateCreated(dateCreated);
                      groupAsset.setBranchId(branchId);
                      groupAsset.setCategory(categoryCode);
                      groupAsset.setSpare1(spare1);
                      groupAsset.setSerial_number(serialNo);
                      groupAsset.setBar_code(barCode);
                      groupAsset.setProcess_flag(processFlag);
                      groupAsset.setStatus(assetStatus);
                      finder.add(groupAsset);
                      
                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Verification Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }

          /**
           * findGroupAssetDisposalByQuery
           *
           * @param id String
           * @return FleetManatainanceRecord
           */
          public ArrayList findGroupAssetDisposalByQuery(String queryFilter) {

              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery =
              			"SELECT disposal_ID,asset_id,disposalCost,disposal_reason,Disposal_Date,"+
      					"User_ID,raise_entry,buyer_ac,disposalAmount,"+
      					"vat_amount,wht_amount,nbv,accum_dep,cost_price,"+
      					"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,"+
      					"branch_code,category_code,description,lpoNum,invoice_no,"+
      					"asset_code,INTEGRIFY,DISPOSED,PROJECT_CODE,LOCATION,SBU_CODE  "+
      					"FROM AM_GROUP_DISPOSAL WHERE ASSET_ID != ''  " + queryFilter;
              
//              System.out.println("selectQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  rs = ps.executeQuery();

                  while (rs.next()) {

      				String id = rs.getString("ASSET_ID");
   //   				String registrationNo = rs.getString("REGISTRATION_NO");
      				String description = rs.getString("DESCRIPTION");

      			   String branchCode = rs.getString("BRANCH_CODE");
      			 //  String deptCode = rs.getString("DEPT_ID");
      			//   String sectionCode = rs.getString("SECTION_ID");
      			   String category = rs.getString("CATEGORY_CODE");
      			   double costPrice = rs.getDouble("disposalCost");
//      			   double oldcostPrice = rs.getDouble("old_cost_price");
      			   String dateCreated = formatDate(rs.getDate("DISPOSAL_DATE"));
      			   double accumulatedDepreciation = 0.00d;//rs.getString("");

      			   String taxAmount = rs.getString("BRANCH_CODE");
        			   String maintainBy  = rs.getString("wht_amount");
        			   String vat = rs.getString("vat_amount");
        			   double vatableCost = rs.getDouble("disposalAmount");
        			   String process_flag = rs.getString("DISPOSED");
        			   String sbu_code = rs.getString("SBU_CODE");
        			   String assetcode = rs.getString("ASSET_CODE");
        			   String location = rs.getString("LOCATION");
        			   String projectCode = rs.getString("PROJECT_CODE");
        			   String integrifyId = rs.getString("INTEGRIFY");
        			   double nbv = rs.getDouble("nbv");
        			   
                      groupAsset = new GroupAsset(id,"",description,branchCode,
                      "","", category,costPrice, dateCreated,
                      accumulatedDepreciation, "", "",
                      "", "", taxAmount,  maintainBy , vat, vatableCost);
                      groupAsset.setProcess_flag(process_flag);
  //                    groupAsset.setOldcost_price(oldcostPrice);;
  //                    groupAsset.setSbu_code(sbu_code);
                      groupAsset.setAsset_code(assetcode);
                      groupAsset.setLocation(location);
                      groupAsset.setProjectCode(projectCode);
                      groupAsset.setNbv(nbv);
                      groupAsset.setSbu_code(sbu_code);
                      finder.add(groupAsset);

                  }
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching GroupAsset Disposal Records ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }
          
          public ArrayList findAssetSummaryPostingByQuery(String Id,String tranType) {
//        	  	System.out.println("=======Id: "+Id+"    tranType: "+tranType);
              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery = "";
              if(Id.equals("3")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,Vendor_AC AS CREDIT_ACCOUNT, c.Asset_Ledger AS DEBIT_ACCOUNT,'D' AS transType,"
              			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_group_asset a, am_raisentry_post p,am_ad_category c,am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
              			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              			+ "and page = ? --Capitalised Group Asset Creation ";
              }
              if(Id.equals("24")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice,Vendor_AC AS CREDIT_ACCOUNT, c.Asset_Ledger AS DEBIT_ACCOUNT, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_group_asset a, am_raisentry_post p,am_ad_category c,am_asset_approval b where a.Group_id = p.Id and convert(varchar,a.Group_id) = b.asset_id "
              		+ "and a.Category_ID = c.category_ID and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Capitalised Upload Asset Creation Debit "
              		+ "";    
              }
              if(Id.equals("1")) {
              selectQuery = "select a.Group_id, a.Asset_id,Vendor_AC AS CREDIT_ACCOUNT,c.Asset_Ledger AS DEBIT_ACCOUNT,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              		+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_asset a, am_raisentry_post p,am_ad_category c,am_asset_approval b "
              		+ "where a.Asset_id = p.Id and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' and page = ? --Single Asset Creation Debit";    
              }
              if(Id.equals("74")) {
              selectQuery =	" select distinct a.disposal_ID AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE,a.Disposal_Date AS Date_purchased,a.effDate AS effective_date,p.Posting_Date,c.category_code, "
              		+ "(select DISTINCT b.suspense_acct asd from am_ad_category a,  am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b,AM_GROUP_DISPOSAL g where a.currency_id = c.currency_id   "
              		+ "and a.category_code = a.CATEGORY_CODE and d.branch_code = g.branch_code) AS DEBIT_ACCOUNT, "
              		+ "(select TOP 1 a.Asset_Ledger asd from am_ad_category a, am_ad_branch d, AM_GB_CURRENCY_CODE c, am_gb_company b,AM_GROUP_DISPOSAL g where a.currency_id = c.currency_id  "
              		+ "and a.category_code = g.category_code and d.branch_code = g.branch_code) AS CREDIT_ACCOUNT "
              		+ "from AM_GROUP_DISPOSAL a, am_asset s, am_raisentry_post p,am_ad_category c,am_asset_approval b where convert(varchar,a.disposal_ID) = p.Id "
              		+ "and a.asset_id = s.asset_id and convert(varchar,a.disposal_ID) = b.asset_id and a.Category_Code = c.category_Code and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page =  ? --Capitalised Upload Disposal Assset";    
              }
              if(Id.equals("2")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+s.Description AS Description,s.Cost_Price AS costPrice,c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "b.BRANCH_CODE,s.Date_purchased,a.effective_date,a.disposal_date AS Posting_Date,c.category_code, "
              		+ "(select b.suspense_acct asd from am_ad_category a,  am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b where a.currency_id = c.currency_id and a.category_code = s.CATEGORY_CODE "
              		+ "and d.branch_code = s.BRANCH_CODE)AS DEBIT_ACCOUNT, "
              		+ "(select b.suspense_acct asd from am_ad_category a, am_ad_branch d, AM_GB_CURRENCY_CODE c,am_gb_company b where a.currency_id = c.currency_id and a.category_code = s.CATEGORY_CODE  "
              		+ "and d.branch_code = S.BRANCH_CODE) AS CREDIT_ACCOUNT "
              		+ "from am_AssetDisposal a, am_raisentry_post p,am_ad_category c, am_ad_branch b,am_asset s,am_asset_approval ap where a.asset_id = p.Id "
              		+ "and a.asset_id = s.Asset_id and a.Asset_id = ap.asset_id and s.Category_ID = c.category_ID and disposal_status='P' and entryPostFlag = 'N' and GroupIdStatus = 'N' and ap.process_status = 'A' "
              		+ "and s.branch_code = b.BRANCH_CODE and page = ? --Capitalised Asset Disposal Debit ";    
              }
              if(Id.equals("6")) {
              selectQuery =	"select DISTINCT a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.OLD_BRANCH_CODE AS BRANCH_CODE,Date_purchased,a.effDate AS effective_date,a.Transfer_Date AS Posting_Date,c.category_code, "
              		+ "a.NEW_BRANCH_CODE+'-'+ c.Asset_Ledger AS DEBIT_ACCOUNT,a.OLD_BRANCH_CODE+'-'+c.Asset_Ledger AS CREDIT_ACCOUNT from am_assetTransfer a, "
              		+ "am_raisentry_post p,am_ad_category c, am_asset t,am_asset_approval b where a.asset_id = p.Id and a.asset_id = t.Asset_id and a.asset_id = b.asset_id and a.OLD_CATEGORY_CODE = c.category_Code and entryPostFlag = 'N' and GroupIdStatus = 'N' and a.approval_status='ACTIVE' AND b.process_status = 'A' and b.tran_type = 'Asset Transfer' "
              		+ "and page = ? --Capitalised Asset Transfer Debit ";    
              }
              if(Id.equals("28")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "t.BRANCH_CODE, Date_purchased,t.effective_date,a.Transfer_Date AS Posting_Date,c.category_code, "
              		+ "a.NEW_BRANCH_CODE+'-'+ c.Asset_Ledger AS DEBIT_ACCOUNT,(select BRANCH_CODE from am_ad_branch where BRANCH_ID = a.oldbranch_id)+'-'+c.Asset_Ledger AS CREDIT_ACCOUNT from am_gb_bulkTransfer a, "
              		+ "am_raisentry_post p,am_ad_category c, am_asset t, am_asset_approval b where convert(varchar,a.Batch_id) = p.Id and a.asset_id = t.Asset_id and convert(varchar,a.Batch_id) = b.asset_id and t.CATEGORY_CODE = c.category_Code and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Capitalised Bulk Asset Transfer ";    
              }              
              if(Id.equals("4")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.OLD_BRANCH_CODE AS BRANCH_CODE,s.Date_purchased,a.reclassify_date AS effective_date,p.Posting_Date,c.category_code, "
              		+ "(select Asset_Ledger from am_ad_category where CATEGORY_CODE = a.old_category_code) AS CREDIT_ACCOUNT, "
              		+ "(select Asset_Ledger from am_ad_category where category_Id = a.new_category_Id) AS DEBIT_ACCOUNT from am_assetReclassification a, "
              		+ "am_raisentry_post p,am_ad_category c,am_asset s, am_asset_approval b where a.new_asset_id = p.Id and a.asset_id = s.Asset_id and a.asset_id = b.asset_id and a.OLD_CATEGORY_CODE = c.category_Code and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Capitalised Asset Reclasification Debit ";    
              }
              if(Id.equals("12")) {
              selectQuery =	"select distinct a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code, "
              		+ "a.Vendor_AC AS DEBIT_ACCOUNT,(select Asset_Ledger from am_ad_category where category_code = a.category_code) AS CREDIT_ACCOUNT from am_asset a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id "
              		+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Single Close Asset  Debit ";    
              }
              if(Id.equals("10")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE AS BRANCH_CODE,s.Date_purchased,a.effDate AS effective_date,a.revalue_Date AS Posting_Date,c.category_code,Vendor_AC AS CREDIT_ACCOUNT, c.Asset_Ledger AS DEBIT_ACCOUNT from am_asset_improvement a, "
              		+ "am_raisentry_post p,am_ad_category c,am_asset s, am_asset_approval b where a.asset_id = p.Id and a.asset_id = b.asset_id and a.CATEGORY_CODE = c.category_Code and a.asset_id = s.Asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and approval_status = 'ACTIVE' and revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
              		+ "and b.posting_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1))) and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
              		+ "and page = ? --Capitalised Asset Improvement Debit ";    
              }              
              if(Id.equals("13")) {
              selectQuery =	"select distinct a.Revalue_ID AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE,s.Date_purchased,a.effDate AS effective_date,a.revalue_Date AS Posting_Date,c.category_code,Vendor_AC AS CREDIT_ACCOUNT, c.Asset_Ledger AS DEBIT_ACCOUNT from AM_GROUP_IMPROVEMENT a, am_raisentry_post p,am_ad_category c,am_asset s, am_asset_approval b where CONVERT(varchar, a.Revalue_ID) = p.Id "
              		+ "and a.Category_Code = c.category_Code and a.asset_id = s.Asset_id and CONVERT(varchar, a.Revalue_ID) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Capitalised Asset Improvement Upload ";
              }
              if(Id.equals("16")) {
                  selectQuery = "select distinct a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
                  		+ "  a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code, "
                  		+ "  c.Asset_Ledger CREDIT_ACCOUNT,c.Asset_Ledger DEBIT_ACCOUNT from am_asset a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id and a.asset_id = b.asset_id "
                  		+ "  and a.Category_ID = c.category_ID and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
                  		+ "  and page = ? --Single WIP RECLASSIFICATION";  
              }
              if(Id.equals("29")) {
              selectQuery =	" select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+s.Description AS Description,s.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "s.BRANCH_CODE AS BRANCH_CODE,s.Date_purchased,a.effective_date,a.Accelerated_Date AS Posting_Date,c.category_code, "
              		+ "c.Dep_ledger AS DEBIT_ACCOUNT,c.Accum_Dep_ledger AS CREDIT_ACCOUNT from am_AcceleratedDepreciation a, "
              		+ "am_raisentry_post p,am_ad_category c,am_asset s, am_asset_approval b where a.asset_id = p.Id and a.asset_id = b.asset_id and s.CATEGORY_CODE = c.category_Code and a.asset_id = s.Asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              		+ "and page = ? --Capitalised Accelerated Depreciation";    
              }              
              if(Id.equals("EXCPT")) {
                  selectQuery = "select a.Group_id, '' AS Asset_id,a.Description,a.Cost_Price AS costPrice, a.ACCOUNT_NO AS accountNo,'D' AS transType,"
                  		+ "a.BRANCH_CODE,a.DATE_FIELD,a.DATE_FIELD,a.DATE_FIELD,'' AS category_code from AM_GB_BATCH_POSTING a, AM_GB_POSTING_EXCEPTION p "
                  		+ "where a.GROUP_ID = P.GROUP_ID and a.ID = P.SERIAL_NO and a.GROUP_DESCRIPTION = ? --Exception Transaction Posting";  
              }                   
 //             System.out.println("selectQuery in findAssetSummaryPostingByQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  ps.setString(1, tranType);
                  rs = ps.executeQuery();
                  while (rs.next()) {

      				String assetId = rs.getString("ASSET_ID");
      				String description = rs.getString("DESCRIPTION");
      			   double costPrice = rs.getDouble("COSTPRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
      			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
      			   String branchCode = rs.getString("BRANCH_CODE");
      			   String categoryCode = rs.getString("CATEGORY_CODE");
      			   String creditAccount =  rs.getString("CREDIT_ACCOUNT");
      			 String debitAccount =  rs.getString("DEBIT_ACCOUNT");
                      
                      groupAsset = new GroupAsset();
                      groupAsset.setId(assetId); 
                      groupAsset.setDescription(description);
                      groupAsset.setBranchCode(branchCode);
                      groupAsset.setCostPrice(costPrice);
                      groupAsset.setDateCreated(dateCreated);
                      groupAsset.setDate_of_purchase(purchaseDate);
                      groupAsset.setDepreciation_start_date(depreciationStartDate);
                      groupAsset.setCategory_code(categoryCode);
                      groupAsset.setSpare1(creditAccount);
                      groupAsset.setSpare2(debitAccount);
                      finder.add(groupAsset);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Records by findAssetSummaryPostingByQuery ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }                                    
                                               
          
          public ArrayList findUncapitalisedSummaryPostingByQuery(String Id,String tranType) {
//        	  	System.out.println("=======Id: "+Id+"    tranType: "+tranType);
              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery = "";
              if(Id.equals("19")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_GROUP_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
              			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              			+ "and page = ? --Capitalised Group Asset Creation ";
              }
              if(Id.equals("26")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_GROUP_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
            			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
            			+ "and page = ? --Capitalised Upload Asset Creation Debit ";    
              }
              if(Id.equals("1")) {
              selectQuery = "select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
          			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Asset_id) = p.Id "
          			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Asset_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
          			+ "and page = ? --Single Asset Creation Debit";    
              }
              if(Id.equals("20")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+s.Description AS Description,s.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "b.BRANCH_CODE,a.Disposal_Date AS Date_purchased,a.effective_date,a.disposal_date AS Posting_Date,c.category_code from am_UncapitalizedDisposal a, AM_ASSET_UNCAPITALIZED s , "
            			+ "am_raisentry_post p,am_ad_category c, am_ad_branch b, am_asset_approval ab where a.asset_id = p.Id  and a.Disposal_ID = ab.batch_id "
            			+ "and a.asset_id = s.asset_id and s.Category_ID = c.category_ID and a.asset_id = ab.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and ab.process_status = 'A' "
            			+ "and s.branch_code = b.BRANCH_CODE and page = ? --Capitalised Asset Disposal Debit  ";    
              }
              if(Id.equals("22")) {
              selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.OLD_BRANCH_CODE AS BRANCH_CODE,a.Transfer_Date AS Date_purchased,a.effDate AS effective_date,a.Transfer_Date AS Posting_Date,c.category_code from am_UncapitalizedTransfer a, "
            			+ "am_raisentry_post p,am_ad_category c,am_asset_approval b where a.asset_id = p.Id and a.OLD_CATEGORY_CODE = c.category_Code and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
            			+ "and page = ? --Capitalised Asset Transfer Debit ";    
              }

              if(Id.equals("21")) {
              selectQuery =	"select a.Asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+u.Description AS Description,u.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
            			+ "u.BRANCH_CODE,u.Date_purchased,u.effective_date,a.Posting_Date,c.category_code from am_assetUpdate a, AM_ASSET_UNCAPITALIZED u, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id and a.Asset_id = u.Asset_id "
            			+ "and u.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N'  and b.process_status = 'A' "
            			+ "and page = ? --Single Close Asset  Debit ";    
              }
              if(Id.equals("86")) {
              selectQuery =	"select a.Asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,  "
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id  "
            			+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N'  and b.process_status = 'A' "
            			+ "and page = ? --Single Asset Creation Single  Debit ";    
              }   
              if(Id.equals("71")) {
                  selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              			+ "a.BRANCH_CODE AS BRANCH_CODE,revalue_Date AS Date_purchased,a.effDate AS effective_date,a.revalue_Date AS Posting_Date,c.category_code from am_Uncapitalized_improvement a, "
              			+ "am_raisentry_post p,am_ad_category c, am_asset_approval d where a.asset_id = p.Id and a.CATEGORY_CODE = c.category_Code and a.Revalue_ID = d.batch_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and d.process_status = 'A' "
              			+ "and page = ? --Capitalised Asset Improvement Debit ";   
              }                    
//              System.out.println("selectQuery in findAssetSummaryPostingByQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  ps.setString(1, tranType);
                  rs = ps.executeQuery();
                  while (rs.next()) {

      				String assetId = rs.getString("ASSET_ID");
      				String description = rs.getString("DESCRIPTION");
      			   double costPrice = rs.getDouble("COSTPRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
      			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
      			   String branchCode = rs.getString("BRANCH_CODE");
      			   String categoryCode = rs.getString("CATEGORY_CODE");
                      
                      groupAsset = new GroupAsset();
                      groupAsset.setId(assetId); 
                      groupAsset.setDescription(description);
                      groupAsset.setBranchCode(branchCode);
                      groupAsset.setCostPrice(costPrice);
                      groupAsset.setDateCreated(dateCreated);
                      groupAsset.setDate_of_purchase(purchaseDate);
                      groupAsset.setDepreciation_start_date(depreciationStartDate);
                      groupAsset.setCategory_code(categoryCode);
                      finder.add(groupAsset);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Records by findAssetSummaryPostingByQuery ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }                                    
          
          public ArrayList findAssetSummaryPostingExceptByQuery(String Id,String tranType) {
//        	  	System.out.println("=======Id: "+Id+"    tranType: "+tranType);
              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery = "";
 //             if(Id.equals("EXCPT")) {
                  selectQuery = "select a.Group_id, '' AS Asset_id,a.Description,a.Cost_Price AS costPrice, a.ACCOUNT_NO AS accountNo,'D' AS transType,"
                  		+ "a.BRANCH_CODE,a.DATE_FIELD,a.DATE_FIELD,a.DATE_FIELD,'' AS category_code from AM_GB_BATCH_POSTING a, AM_GB_POSTING_EXCEPTION p "
                  		+ "where a.GROUP_ID = P.GROUP_ID and a.ID = P.SERIAL_NO and a.GROUP_DESCRIPTION = ? --Exception Transaction Posting";  
//              }                   
//              System.out.println("selectQuery in findAssetSummaryPostingByQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  ps.setString(1, tranType);
                  rs = ps.executeQuery();
                  while (rs.next()) {

      				String assetId = rs.getString("ASSET_ID");
      				String description = rs.getString("DESCRIPTION");
      			   double costPrice = rs.getDouble("COSTPRICE");
      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
      			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
      			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
      			   String branchCode = rs.getString("BRANCH_CODE");
      			   String categoryCode = rs.getString("CATEGORY_CODE");
                      
                      groupAsset = new GroupAsset();
                      groupAsset.setId(assetId); 
                      groupAsset.setDescription(description);
                      groupAsset.setBranchCode(branchCode);
                      groupAsset.setCostPrice(costPrice);
                      groupAsset.setDateCreated(dateCreated);
                      groupAsset.setDate_of_purchase(purchaseDate);
                      groupAsset.setDepreciation_start_date(depreciationStartDate);
                      groupAsset.setCategory_code(categoryCode);
                      finder.add(groupAsset);

                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Records by findAssetSummaryPostingByQuery ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return finder;
          }                                    
          
          public double findAssetSummaryPostingByQueryTotal(String Id,String tranType) {
//        	  	System.out.println("=======Id: "+Id+"    tranType: "+tranType);
              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery = "";
              double costTotal = 0.00;
              if(Id.equals("3")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_group_asset a, am_raisentry_post p,am_ad_category c,am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
              			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' AND b.process_status = 'A' "
              			+ "and page = ? --Capitalised Group Asset Creation ";
              }
              if(Id.equals("24")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_group_asset a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
            			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' AND b.process_status = 'A' "
            			+ "and page = ? --Capitalised Upload Asset Creation Debit ";    
              }
              if(Id.equals("1")) {
              selectQuery = "select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
          			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_asset a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id "
          			+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' AND b.process_status = 'A' "
          			+ "and page = ? --Single Asset Creation Debit";    
              }
              if(Id.equals("74")) {
              selectQuery =	"select a.disposal_ID AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.BRANCH_CODE,a.Disposal_Date AS Date_purchased,a.effDate AS effective_date,p.Posting_Date,c.category_code from AM_GROUP_DISPOSAL a, am_asset s, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.disposal_ID) = p.Id "
            			+ "and a.asset_id = s.asset_id and a.Category_Code = c.category_Code and convert(varchar,a.disposal_ID) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' AND b.process_status = 'A' "
            			+ "and page = ? --Capitalised Upload Disposal Assset ";    
              }
              if(Id.equals("2")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+s.Description AS Description,s.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "b.BRANCH_CODE,s.Date_purchased,a.effective_date,a.disposal_date AS Posting_Date,c.category_code from am_assetDisposal a, am_raisentry_post p,am_ad_category c, am_ad_branch b,am_asset s, am_asset_approval ab where a.asset_id = p.Id "
              		+ "and a.asset_id = s.Asset_id and s.Category_ID = c.category_ID and a.asset_id = ab.asset_id and disposal_status='P' and entryPostFlag = 'N' and GroupIdStatus = 'N' AND ab.process_status = 'A' "
              		+ "and s.branch_code = b.BRANCH_CODE and page = ? --Capitalised Asset Disposal Debit  ";    
              }            
              if(Id.equals("6")) {
              selectQuery =	"select DISTINCT a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.OLD_BRANCH_CODE AS BRANCH_CODE,Date_purchased,a.effDate AS effective_date,a.Transfer_Date AS Posting_Date,c.category_code from am_assetTransfer a, "
            			+ "am_raisentry_post p,am_ad_category c, am_asset t,am_asset_approval b where a.asset_id = p.Id and a.asset_id = t.Asset_id and a.asset_id = b.asset_id and a.OLD_CATEGORY_CODE = c.category_Code and entryPostFlag = 'N' and GroupIdStatus = 'N' and a.approval_status='ACTIVE' AND b.process_status = 'A' and b.tran_type = 'Asset Transfer' "
            			+ "and page = ? --Capitalised Asset Transfer Debit ";    
              }
              if(Id.equals("28")) {
              selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "t.BRANCH_CODE, Date_purchased,t.effective_date,a.Transfer_Date AS Posting_Date,c.category_code from am_gb_bulkTransfer a, "
            			+ "am_raisentry_post p,am_ad_category c, am_asset t, am_asset_approval b where convert(varchar,a.Batch_id) = p.Id and a.asset_id = t.Asset_id and t.CATEGORY_CODE = c.category_Code and convert(varchar,a.Batch_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' "
            			+ " AND ab.process_status = 'A' and page = ? --Capitalised Bulk Asset Transfer ";    
              }                 
              if(Id.equals("4")) {
              selectQuery =	"select sum(a.Cost_Price) AS costPrice from am_assetReclassification a, am_raisentry_post p,am_asset s, "
              		+ "am_asset_approval b where a.new_asset_id = p.Id and a.asset_id = b.asset_id "
              		+ "and a.asset_id = s.Asset_id and b.tran_type='Asset Reclassification' "
              		+ "and b.process_status = 'A' and entryPostFlag = 'N' "
              		+ "and GroupIdStatus = 'N' and page = ? --Capitalised Asset Reclasification Debit  ";    
              }
              if(Id.equals("12")) {
              selectQuery =	"select sum(a.Cost_Price) AS costPrice from am_asset a, am_raisentry_post p, am_asset_approval b "
              		+ "where a.asset_id = p.Id and a.Asset_id = b.asset_id and b.tran_type='CloseAsset' "
              		+ "and  b.process_status = 'A' and entryPostFlag = 'N' and GroupIdStatus = 'N' "
              		+ "and page = ? --Single Close Asset  Debit ";    
              }
              if(Id.equals("10")) {
              selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
              		+ "a.BRANCH_CODE AS BRANCH_CODE,s.Date_purchased,a.effDate AS effective_date,a.revalue_Date AS Posting_Date,c.category_code from am_asset_improvement a,  "
              		+ "am_raisentry_post p,am_ad_category c,am_asset s, am_asset_approval b where a.asset_id = p.Id and a.CATEGORY_CODE = c.category_Code and a.asset_id = s.Asset_id and a.asset_id = b.asset_id "
              		+ "and entryPostFlag = 'N' and GroupIdStatus = 'N' AND approval_status = 'ACTIVE' AND b.process_status = 'A'  "
              		+ "and revalue_Date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1)))  "
              		+ "and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
              		+ "and b.posting_date between (SELECT DATEADD(DAY,1,EOMONTH((SELECT GETDATE()),-1)))  "
              		+ "and (SELECT CONVERT(DATE,DATEADD(s,-1,DATEADD(mm, DATEDIFF(m,0,GETDATE())+1,0)))) "
              		+ "and page = ? --Capitalised Asset Improvement Debit ";    
              }              
              if(Id.equals("13")) {
              selectQuery =	" select sum(a.Cost_Price) AS costPrice from AM_GROUP_IMPROVEMENT a, am_raisentry_post p,am_ad_category c, am_asset_approval b "
              		+ "where a.Revalue_ID = p.Id "
              		+ "and convert(varchar, a.Revalue_ID) = b.asset_id "
              		+ "and a.Category_Code = c.category_Code "
              		+ "and b.tran_type='Asset Improve Upload' "
              		+ "and  b.process_status = 'A' "
              		+ "and entryPostFlag = 'N' "
              		+ "and GroupIdStatus = 'N' "
              		+ "and page = ? --Capitalised Asset Improvement Upload ";
              }
              if(Id.equals("16")) {
                  selectQuery = "select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
                			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_asset a, am_raisentry_post p,am_ad_caty c, am_asset_approval b where a.Asset_id = p.Id "
                			+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' AND ab.process_status = 'A' "
                			+ "and page = ? --Single WIP RECLASSIFICATION";  
              }       
              if(Id.equals("EXCPT")) {
                  selectQuery = "select a.Group_id, '' AS Asset_id,a.Description,a.Cost_Price AS costPrice, a.ACCOUNT_NO AS accountNo,'D' AS transType,"
                  		+ "a.BRANCH_CODE,a.DATE_FIELD,a.DATE_FIELD,a.DATE_FIELD,'' AS category_code from AM_GB_BATCH_POSTING a, AM_GB_POSTING_EXCEPTION p "
                  		+ "where a.GROUP_ID = P.GROUP_ID and a.ID = P.SERIAL_NO and a.GROUP_DESCRIPTION = ? --Exception Transaction Posting";  
              }    
              if(Id.equals("29")) {
              selectQuery =	"select sum(s.Cost_Price) AS costPrice from am_AcceleratedDepreciation a, am_raisentry_post p,am_ad_category c, am_asset s, am_asset_approval b  "
              		+ "where a.Asset_id = p.Id and a.asset_id = b.asset_id and a.Asset_id = s.Asset_id "
              		+ "and s.Category_ID = c.category_ID "
              		+ "and b.tran_type='Accelerated Depreciation' "
              		+ "and  b.process_status = 'A' "
              		+ "and entryPostFlag = 'N' "
              		+ "and GroupIdStatus = 'N' and page = ? --Capitalised Accelerated Depreciation ";    
              }               
//              System.out.println("selectQuery in findAssetSummaryPostingByQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  ps.setString(1, tranType);
                  rs = ps.executeQuery();
                  while (rs.next()) {
//      				String assetId = rs.getString("ASSET_ID");
//      				String description = rs.getString("DESCRIPTION");
                	  	double costPrice = rs.getDouble("COSTPRICE");
                	  	costTotal = costTotal + rs.getDouble("COSTPRICE");
//      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
//      			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
//      			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
//      			   String branchCode = rs.getString("BRANCH_CODE");
//      			   String categoryCode = rs.getString("CATEGORY_CODE");
                  }
                  
              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Records by findAssetSummaryPostingByQueryTotal ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return costTotal;
          }                                    
          
          public double findUncapitalisedSummaryPostingByQueryTotalCost(String Id,String tranType) {
//        	  	System.out.println("=======Id: "+Id+"    tranType: "+tranType);
              Connection con = null;
              PreparedStatement ps = null;
              ResultSet rs = null;
              GroupAsset groupAsset = null;
              ArrayList finder = new ArrayList();
              String selectQuery = "";
              double costTotal = 0.00;
              if(Id.equals("19")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_GROUP_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
              			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
              			+ "and page = ? --Capitalised Group Asset Creation ";
              }
              if(Id.equals("26")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_GROUP_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where convert(varchar,a.Group_id) = p.Id "
            			+ "and a.Category_ID = c.category_ID and convert(varchar,a.Group_id) = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
            			+ "and page = ? --Capitalised Upload Asset Creation Debit ";    
              }
              if(Id.equals("1")) {
              selectQuery = "select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
          			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id "
          			+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
          			+ "and page = ? --Single Asset Creation Debit";    
              }
              if(Id.equals("20")) {
              selectQuery =	"select distinct a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+s.Description AS Description,s.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,\r\n"
              		+ "b.BRANCH_CODE,a.Disposal_Date AS Date_purchased,a.effective_date,a.disposal_date AS Posting_Date,c.category_code from am_UncapitalizedDisposal a, AM_ASSET_UNCAPITALIZED s , \r\n"
              		+ "am_raisentry_post p,am_ad_category c, am_ad_branch b, am_asset_approval d where a.asset_id = p.Id \r\n"
              		+ "and a.Disposal_ID = d.batch_id and a.asset_id = s.asset_id and s.Category_ID = c.category_ID and a.asset_id = d.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and d.process_status = 'A' \r\n"
              		+ "and s.branch_code = b.BRANCH_CODE and page = ? --Capitalised Asset Disposal Debit";    
              }
              if(Id.equals("22")) {
              selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
            			+ "a.OLD_BRANCH_CODE AS BRANCH_CODE,'' AS Date_purchased,a.effDate AS effective_date,a.Transfer_Date AS Posting_Date,c.category_code from am_UncapitalizedTransfer a, "
            			+ "am_raisentry_post p,am_ad_category c, am_asset_approval b where a.asset_id = p.Id and a.OLD_CATEGORY_CODE = c.category_Code and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
            			+ "and page = ? --Capitalised Asset Transfer Debit ";    
              }
              if(Id.equals("21")) {
              selectQuery =	"select a.Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,u.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from am_assetUpdate a, AM_ASSET_UNCAPITALIZED u, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id and a.Asset_id = u.Asset_id "
            			+ "and u.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and b.process_status = 'A' "
            			+ "and page = ? --Single Close Asset  Debit ";    
              }
              if(Id.equals("86")) {
              selectQuery =	"select a.Asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType, "
            			+ "a.BRANCH_CODE,a.Date_purchased,a.effective_date,a.Posting_Date,c.category_code from AM_ASSET_UNCAPITALIZED a, am_raisentry_post p,am_ad_category c, am_asset_approval b where a.Asset_id = p.Id "
            			+ "and a.Category_ID = c.category_ID and a.asset_id = b.asset_id and entryPostFlag = 'N' and GroupIdStatus = 'N'  and b.process_status = 'A' "
            			+ "and page = ? --Single Asset Creation  Debit ";    
              }
              if(Id.equals("71")) {
                  selectQuery =	"select a.asset_id AS Group_id, a.Asset_id,a.Asset_id+'**'+a.Description AS Description,a.Cost_Price AS costPrice, c.Asset_Ledger AS accountNo,'D' AS transType,"
              			+ "a.BRANCH_CODE AS BRANCH_CODE,revalue_Date AS Date_purchased,a.effDate AS effective_date,a.revalue_Date AS Posting_Date,c.category_code from am_Uncapitalized_improvement a, "
              			+ "am_raisentry_post p,am_ad_category c, am_asset_approval d where a.asset_id = p.Id and a.CATEGORY_CODE = c.category_Code and CONVERT(varchar, a.Revalue_ID) = d.batch_id and entryPostFlag = 'N' and GroupIdStatus = 'N' and d.process_status = 'A' "
              			+ "and page = ? --Capitalised Asset Improvement Debit ";   
              }                   
//              System.out.println("selectQuery in findAssetSummaryPostingByQuery : " + selectQuery);

              try {
                  con = getConnection("legendPlus");
                  ps = con.prepareStatement(selectQuery);
                  ps.setString(1, tranType);
                  rs = ps.executeQuery();
                  while (rs.next()) {
//      				String assetId = rs.getString("ASSET_ID");
//      				String description = rs.getString("DESCRIPTION");
                	  double costPrice = rs.getDouble("COSTPRICE");
                	  costTotal = costTotal + rs.getDouble("COSTPRICE");
//      			   String dateCreated = formatDate(rs.getDate("POSTING_DATE"));
//      			   String purchaseDate = formatDate(rs.getDate("DATE_PURCHASED"));
//      			   String depreciationStartDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
//      			   String branchCode = rs.getString("BRANCH_CODE");
//      			   String categoryCode = rs.getString("CATEGORY_CODE");
                  }

              } catch (Exception e) {
                  System.out.println("INFO:GroupAssetManager-Error Fetching Records by findUncapitalisedSummaryPostingByQueryTotalCost ->" +
                                     e.getMessage());
              } finally {
                  closeConnection(con, ps, rs);
              }
              return costTotal;
          }                                    
                                                                                             
}
