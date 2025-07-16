/**
 * 
 */
package com.magbel.admin.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.magbel.util.DataConnect;
import com.magbel.admin.objects.*;
import audit.*;

import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import java.util.Date;
import com.magbel.admin.objects.Branch;

public class AdminHandler {

    AdminHandler handler;
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    DataConnect dc;
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date date;
    com.magbel.util.DatetimeFormat df;
    ApplicationHelper help;
    // AdminHandler handler;
    ApplicationHelper apph;
    ApprovalRecords aprecords = null;
    public AdminHandler() {
    	aprecords = new ApprovalRecords();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        df = new com.magbel.util.DatetimeFormat();
       // System.out.println("USING_ " + this.getClass().getName());
        help = new ApplicationHelper();
    }

    public boolean createCategory(com.magbel.admin.objects.Category category) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_ad_category" + "(category_code,category_name" + ",category_acronym,Required_for_fleet" + ",Category_Class ,PM_Cycle_Period,mileage" + ",Notify_Maint_Days ,notify_every_days,residual_value" + ",Dep_rate ,Asset_Ledger,Dep_ledger" + ",Accum_Dep_ledger ,gl_account,insurance_acct" + ",license_ledger ,fuel_ledger,accident_ledger" + ",Category_Status ,user_id,create_date" + ",acct_type ,currency_Id, category_id,enforceBarcode,category_Type)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            //ps.setLong(1, System.currentTimeMillis());
            ps.setString(1, category.getCategoryCode());
            ps.setString(2, category.getCategoryName());
            ps.setString(3, category.getCategoryAcronym());
            ps.setString(4, category.getRequiredforFleet());
            ps.setString(5, category.getCategoryClass());
            ps.setString(6, category.getPmCyclePeriod());
            ps.setString(7, category.getMileage());
            ps.setString(8, category.getNotifyMaintdays());
            ps.setString(9, category.getNotifyEveryDays());
            ps.setString(10, category.getResidualValue());
            ps.setString(11, category.getDepRate());
            ps.setString(12, category.getAssetLedger());
            ps.setString(13, category.getDepLedger());
            ps.setString(14, category.getAccumDepLedger());
            ps.setString(15, category.getGlAccount());
            ps.setString(16, category.getInsuranceAcct());
            ps.setString(17, category.getLicenseLedger());
            ps.setString(18, category.getFuelLedger());
            ps.setString(19, category.getAccidentLedger());
            ps.setString(20, category.getCategoryStatus());
            ps.setString(21, category.getUserId());
            ps.setDate(22, df.dateConvert(new java.util.Date()));
            ps.setString(23, category.getAcctType());
            ps.setString(24, category.getCurrencyId());
            ps.setLong(25, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_category")));

            ps.setString(26, category.getEnforceBarcode());
            ps.setString(27, category.getCategoryType());
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error creating category ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean createDepartment(com.magbel.admin.objects.Department dept) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false; 
        String query = "INSERT INTO am_ad_department(Dept_code,Dept_name" + " ,Dept_acronym,Dept_Status,user_id ,CREATE_DATE,Dept_ID,UnitHead,UnitHaed_Id,email,UnitMail)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, dept.getDept_code());
            ps.setString(2, dept.getDept_name());
            ps.setString(3, dept.getDept_acronym());
            ps.setString(4, dept.getDept_status());
            ps.setString(5, dept.getUser_id());
            ps.setDate(6, df.dateConvert(new java.util.Date()));
            ps.setLong(7, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_department")));
            ps.setString(8, dept.getUnitHead());
            ps.setString(9, dept.getUnitHeadId());
            ps.setString(10, dept.getUnitMail());
            ps.setString(11, dept.getUnitMail());   
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error creating Department ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean createSection(com.magbel.admin.objects.Section section) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_ad_section(Section_ID,Section_Code,Section_Name" + "  ,section_acronym,Section_Status,User_ID,Create_Date)" + "  VALUES (?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setLong(1, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_ad_section")));
            ps.setString(2, section.getSection_code());
            ps.setString(3, section.getSection_name());
            ps.setString(4, section.getSection_acronym());
            ps.setString(5, section.getSection_status());
            ps.setString(6, section.getUserid());
            ps.setDate(7, df.dateConvert(new java.util.Date()));

            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error creating Section ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean createRegion(com.magbel.admin.objects.Region region) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO AM_AD_REGION( Region_Code, Region_Name" + ", Region_Acronym, Region_Address" + ",Region_Phone , Region_Fax, Region_Status, User_Id,Create_Date,Region_iD) " + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            //ps.setLong(1, System.currentTimeMillis());
            ps.setString(1, region.getRegionCode());
            ps.setString(2, region.getRegionName());
            ps.setString(3, region.getRegionAcronym());
            ps.setString(4, region.getRegionAddress());
            ps.setString(5, region.getRegionPhone());
            ps.setString(6, region.getRegionFax());
            ps.setString(7, region.getRegionStatus());
            ps.setString(8, region.getUserId());
            ps.setDate(9, df.dateConvert(new java.util.Date()));
            ps.setLong(10, Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_AD_REGION")));

            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println("WARNING:Error creating Region ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean createProvince(com.magbel.admin.objects.Province ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_gb_Province" + "(Province_Code, Province" + ",Status, User_id, create_date,Province_ID)" + " VALUES (?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ccode.getProvinceCode());
            ps.setString(2, ccode.getProvince());
            ps.setString(3, ccode.getStatus());
            ps.setString(4, ccode.getUserId());
            ps.setDate(5, df.dateConvert(ccode.getCreateDate()));
            ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_Province")));
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error creating Province ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateCategory(com.magbel.admin.objects.Category category) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "UPDATE am_ad_category" + " SET category_code = ?,category_name = ?,category_acronym = ?" + " , Required_for_fleet = ?,Category_Class = ?,PM_Cycle_Period = ?" + " , mileage = ?,Notify_Maint_Days = ?,notify_every_days = ?" + " , residual_value = ?,Dep_rate = ?,Asset_Ledger = ?" + " , Dep_ledger = ?,Accum_Dep_ledger = ?,gl_account = ?" + " , insurance_acct = ?,license_ledger = ?,fuel_ledger = ?" + " , accident_ledger = ?,Category_Status = ?,user_id = ?" + " , create_date = ?,acct_type = ?,currency_Id = ?,enforceBarcode=?,category_Type=?" + " WHERE category_ID =?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1, category.getCategoryCode());
            ps.setString(2, category.getCategoryName());
            ps.setString(3, category.getCategoryAcronym());
            ps.setString(4, category.getRequiredforFleet());
            ps.setString(5, category.getCategoryClass());
            ps.setString(6, category.getPmCyclePeriod());
            ps.setString(7, category.getMileage());
            ps.setString(8, category.getNotifyMaintdays());
            ps.setString(9, category.getNotifyEveryDays());
            ps.setString(10, category.getResidualValue());
            ps.setString(11, category.getDepRate());
            ps.setString(12, category.getAssetLedger());
            ps.setString(13, category.getDepLedger());
            ps.setString(14, category.getAccumDepLedger());
            ps.setString(15, category.getGlAccount());
            ps.setString(16, category.getInsuranceAcct());
            ps.setString(17, category.getLicenseLedger());
            ps.setString(18, category.getFuelLedger());
            ps.setString(19, category.getAccidentLedger());
            ps.setString(20, category.getCategoryStatus());
            ps.setString(21, category.getUserId());
            ps.setDate(22, df.dateConvert(new java.util.Date()));
            ps.setString(23, category.getAcctType());
            ps.setString(24, category.getCurrencyId());
            ps.setString(25, category.getEnforceBarcode());
            //    System.out.println("----------->--------------------------------->"+category.getCategoryType());
            ps.setString(26, category.getCategoryType());
            ps.setString(27, category.getCategoryId());
            //	System.out.println("----------->"+query);
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateDepartment(com.magbel.admin.objects.Department dept) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
//        String UserQuery = "SELECT Full_Name, User_Name FROM   AM_GB_USER WHERE User_id = '"+dept.getUnitHeadId()+"'";
//        String UnitHead = aprecords.getCodeName(UserQuery);
//        String UserMailQuery = "SELECT email, User_Name FROM   AM_GB_USER WHERE User_id = '"+dept.getUnitHeadId()+"'";
//        String email = aprecords.getCodeName(UserMailQuery);
  //      System.out.println("======updateDepartment query====== "+UserQuery);
  //      System.out.println("======updateDepartment query====== "+UserMailQuery);
        String query = "UPDATE am_ad_department SET Dept_name = ?" + " ,Dept_acronym = ?,Dept_Status = ?" + ",UnitHead = ?,UnitHaed_Id = ?,UnitMail = ?  WHERE dept_code=?";
//System.out.println("======updateDepartment query====== "+query);
//System.out.println("======updateDepartment dept.getUnitHead()====== "+dept.getUnitHead());
//System.out.println("======updateDepartment dept.getUnitHeadId()====== "+dept.getUnitHeadId());
//System.out.println("======updateDepartment dept.getUnitHead()====== "+dept.getUnitHead());
//System.out.println("======updateDepartment dept.getUnitMail()====== "+dept.getUnitMail());

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, dept.getDept_name());
            ps.setString(2, dept.getDept_acronym());
            ps.setString(3, dept.getDept_status());
            ps.setString(4, dept.getUnitHead());
            ps.setString(5, dept.getUnitHeadId());
            ps.setString(6, dept.getUnitMail());      
            ps.setString(7, dept.getDept_code());
            done = (ps.executeUpdate() != -1);
//System.out.println("Save Done for Update");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
  
    }
 
    public boolean updateSubCategory(com.magbel.admin.objects.Category_Sub_Definition Category_Sub_Definition) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        System.out.println("====Category_Sub_Definition.getSUB_CATEGORY_ID()===== "+Category_Sub_Definition.getSUB_CATEGORY_ID());
        System.out.println("====Category_Sub_Definition.getSUB_CATEGORY_NAME()===== "+Category_Sub_Definition.getSUB_CATEGORY_NAME());
        String query = "UPDATE HD_COMPLAIN_SUBCATEGORY" + " SET sub_category_name = ?,sub_category_desc = ? , status = ?  WHERE sub_category_code =?";
        System.out.println("====updateSubCategory query===== "+query);
        try {
            con = getConnection();
            ps = con.prepareStatement(query);        
            ps.setString(1, Category_Sub_Definition.getSUB_CATEGORY_NAME());
            ps.setString(2, Category_Sub_Definition.getSUB_CATEGORY_DESCRIPTION());
            ps.setString(3, Category_Sub_Definition.getStatus());
            ps.setString(4, Category_Sub_Definition.getSUB_CATEGORY_ID());
            //	System.out.println("----------->"+query);
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


    public boolean updateSection(com.magbel.admin.objects.Section section) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE am_ad_section SET Section_Code = ?,Section_Name = ?" + "  ,section_acronym = ?,Section_Status = ?" + "  WHERE Section_ID = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, section.getSection_code());
            ps.setString(2, section.getSection_name());
            ps.setString(3, section.getSection_acronym());
            ps.setString(4, section.getSection_status());
            ps.setString(5, section.getSection_id());

            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating Section ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateState(com.magbel.admin.objects.State state) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE am_gb_states SET state_code = ?,state_name = ?" + "   ,state_status = ?" + "   WHERE state_ID=?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, state.getStateCode());
            ps.setString(2, state.getStateName());
            ps.setString(3, state.getStateStatus());
            ps.setString(4, state.getStateId());

            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateRegion(com.magbel.admin.objects.Region region) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;

        String query = "Update AM_AD_REGION SET Region_Name=?,Region_Acronym=?,Region_Address=?," +
                "Region_Phone=?,Region_Fax=?,Region_Status=? WHERE Region_Code=?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, region.getRegionName());
            ps.setString(2, region.getRegionAcronym());
            ps.setString(3, region.getRegionAddress());
            ps.setString(4, region.getRegionPhone());
            ps.setString(5, region.getRegionFax());
            // ps.setDate(6,df.dateConvert(new java.util.Date()));
            ps.setString(6, region.getRegionStatus());
            //ps.setString(7, region.getUserId());
            ps.setString(7, region.getRegionCode());
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean updateProvince(com.magbel.admin.objects.Province ccode) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE am_gb_Province" + " SET Province_Code = ?" + ",Province = ?,Status = ?" + "  WHERE Province_ID = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            // ps.setString(1, ccode.getProvinceId());
            ps.setString(1, ccode.getProvinceCode());
            ps.setString(2, ccode.getProvince());
            ps.setString(3, ccode.getStatus());
            ps.setString(4, ccode.getProvinceId());
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error Updating Province ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public java.util.List getBranchesByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Branch branch = null;
        String query = "SELECT BRANCH_ID,BRANCH_CODE" + " ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX" + ",BRANCH_ADDRESS,STATE,PHONE_NO" + ",FAX_NO,REGION,PROVINCE" + ",BRANCH_STATUS,USER_ID,GL_SUFFIX" + ",CREATE_DATE  FROM am_ad_branch";

        //query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String branchId = rs.getString("BRANCH_ID");
                String branchCode = rs.getString("BRANCH_CODE");
                String branchName = rs.getString("BRANCH_NAME");
                String branchAcronym = rs.getString("BRANCH_ACRONYM");
                String glPrefix = rs.getString("GL_PREFIX");
                String branchAddress = rs.getString("BRANCH_ADDRESS");
                String state = rs.getString("STATE");
                String phoneNo = rs.getString("PHONE_NO");
                String faxNo = rs.getString("FAX_NO");
                String province = rs.getString("PROVINCE");
                String region = rs.getString("REGION");
                String branchStatus = rs.getString("BRANCH_STATUS");
                String username = rs.getString("USER_ID");
                String glSuffix = rs.getString("GL_SUFFIX");
                String createDate = rs.getString("CREATE_DATE");
                branch = new com.magbel.admin.objects.Branch(branchId, branchCode,
                        branchName, branchAcronym, glPrefix, glSuffix,
                        branchAddress, state, phoneNo, faxNo, region, province,
                        branchStatus, username, createDate);

                _list.add(branch);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Selecting branches ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public com.magbel.admin.objects.Branch getBranchByBranchID(String branchid) {
        String filter = " WHERE BRANCH_ID = " + branchid;
       // System.out.print("===getBranchByBranchID filter==="+filter);
        com.magbel.admin.objects.Branch branch = getABranch(filter);
        return branch;

    }
   
    public com.magbel.admin.objects.Organization getOrganisationByID(String organCode) {
        String filter = " WHERE ORG_CODE = " + organCode;
        com.magbel.admin.objects.Organization organ = getAOrgan(filter);
        //System.out.print("===="+filter);
        //System.out.print("==organ=== "+organ);
        return organ;

    }

    public com.magbel.admin.objects.Branch getBranchByBranchCode(String branchcode) {
        String filter = " WHERE BRANCH_CODE = '" + branchcode + "'";
        com.magbel.admin.objects.Branch branch = getABranch(filter);
        return branch;
 
    }
    
    public java.util.List getDeparmentsByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Department dept = null;
        String query = "SELECT Dept_ID,Dept_code,Dept_name" + "  ,Dept_acronym,Dept_Status" + " ,user_id,CREATE_DATE" + ",UnitHead,UnitHaed_Id,UnitMail,email FROM am_ad_department ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String dept_id = rs.getString("Dept_ID");
                String dept_code = rs.getString("Dept_code");
                String dept_name = rs.getString("Dept_name");
                String dept_acronym = rs.getString("Dept_acronym");
                String dept_status = rs.getString("Dept_Status");
                String user_id = rs.getString("user_id");
                String UnitHead = rs.getString("UnitHead");
                String UnitHaed_Id = rs.getString("UnitHaed_Id");
                String email = rs.getString("email");
                String UnitMail = rs.getString("UnitMail");
                dept = new com.magbel.admin.objects.Department(dept_id, dept_code,
                        dept_name, dept_acronym, dept_status, user_id,UnitHead,UnitHaed_Id,UnitMail,email);

                _list.add(dept);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    private com.magbel.admin.objects.Department getADepartment(String filter) {
        com.magbel.admin.objects.Department dept = null;
        String query = "SELECT Dept_ID,Dept_code,Dept_name" + "  ,Dept_acronym,Dept_Status" + " ,user_id,CREATE_DATE" + ",UnitHead,UnitHaed_Id,email,UnitMail FROM am_ad_department ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String dept_id = rs.getString("Dept_ID");
                String dept_code = rs.getString("Dept_code");
                String dept_name = rs.getString("Dept_name");
                String dept_acronym = rs.getString("Dept_acronym");
                String dept_status = rs.getString("Dept_Status");
                String user_id = rs.getString("user_id");
                String UnitHead = rs.getString("UnitHead");
                String UnitHaed_Id = rs.getString("UnitHaed_Id");
                String email = rs.getString("UnitMail");
                String UnitMail = rs.getString("UnitMail");                
                dept = new com.magbel.admin.objects.Department(dept_id, dept_code,
                        dept_name, dept_acronym, dept_status, user_id,UnitHead,UnitHaed_Id,UnitMail, email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return dept;

    }
    private com.magbel.admin.objects.Organization getAnOrganisation(String filter) {
        com.magbel.admin.objects.Organization organ = null;
        String query = "SELECT mtid,ORG_CODE,ORG_NAME" + "  ,ACRONYM,STATUS" + " ,USER_ID,CREATE_DATE" + ",ADDRESS,Domain,PHONE,FAX,REGION,PROVINCE,CONTACT_PERSON,EMAIL FROM AM_AD_ORGANIZATION ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        
        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
            	 String organ_Id = rs.getString("mtid");
                String organ_code = rs.getString("ORG_CODE");
                String organ_name = rs.getString("ORG_NAME");
                String organ_acronym = rs.getString("ACRONYM");
                String organ_status = rs.getString("STATUS");
                String user_id = rs.getString("USER_ID");
                String CreateDate = rs.getString("CREATE_DATE");
                String Address = rs.getString("ADDRESS");
                String Domain = rs.getString("Domain");
                String Phone = rs.getString("PHONE");
                String Fax = rs.getString("FAX");     
                String mail = rs.getString("EMAIL");     
//                String Province = rs.getString("PROVINCE");     
                String Contact_Person = rs.getString("CONTACT_PERSON");     
                organ = new com.magbel.admin.objects.Organization(organ_Id,organ_code,
                		organ_name, organ_acronym, organ_status, user_id,CreateDate,Address,Domain, Phone, Fax, Contact_Person,mail);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return organ;

    }

    public com.magbel.admin.objects.Department getDeptByDeptID(String deptid) {
        String filter = " WHERE Dept_ID=" + deptid;
        com.magbel.admin.objects.Department dept = getADepartment(filter);
        return dept;

    }
    public com.magbel.admin.objects.Organization getOrganByOrganID(String compid) {
        String filter = " WHERE ORG_CODE=" + compid;
        com.magbel.admin.objects.Organization organ = getAnOrganisation(filter);
        return organ;

    }
    public com.magbel.admin.objects.Department getDeptByDeptCode(String deptcode) {
        String filter = " WHERE Dept_Code='" + deptcode + "'";
        com.magbel.admin.objects.Department dept = getADepartment(filter);
        return dept;

    }

    public java.util.List getSectionByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Section section = null;
        String query = "SELECT Section_ID,Section_Code,Section_Name" + " ,section_acronym,Section_Status" + " ,user_id,CREATE_DATE" + "  FROM am_ad_section ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String section_id = rs.getString("Section_ID");
                String section_code = rs.getString("Section_Code");
                String section_acromyn = rs.getString("section_acronym");
                String section_name = rs.getString("Section_Name");
                String section_status = rs.getString("Section_Status");
                String userid = rs.getString("user_id");
                section = new com.magbel.admin.objects.Section(section_id,
                        section_code, section_acromyn, section_name,
                        section_status, userid);

                _list.add(section);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    private com.magbel.admin.objects.Section getASection(String filter) {
        com.magbel.admin.objects.Section section = null;
        String query = "SELECT Section_ID,Section_Code,Section_Name" + " ,section_acronym,Section_Status" + ",user_id,CREATE_DATE" + "  FROM am_ad_section ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);  
            while (rs.next()) {
                String section_id = rs.getString("Section_ID");
                String section_code = rs.getString("Section_Code");
                String section_acromyn = rs.getString("section_acronym");
                String section_name = rs.getString("Section_Name");
                String section_status = rs.getString("Section_Status");
                String userid = rs.getString("user_id");
                section = new com.magbel.admin.objects.Section(section_id,
                        section_code, section_acromyn, section_name,
                        section_status, userid);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return section;

    }

    public com.magbel.admin.objects.Section getSectionByID(String sectionid) {
        String filter = " WHERE Section_ID=" + sectionid;
        com.magbel.admin.objects.Section section = getASection(filter);
        return section;

    }

    public com.magbel.admin.objects.Section getSectionByCode(String sectioncode) {
        String filter = " WHERE Section_Code='" + sectioncode + "'";
        com.magbel.admin.objects.Section section = getASection(filter);
        return section;

    }

    public java.util.List getStatesByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.State state = null;
        String query = "SELECT state_ID,state_code,state_name" + "  ,state_status,user_id,create_date" + " FROM am_gb_states ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String stateId = rs.getString("state_ID");
                String stateCode = rs.getString("state_code");
                String stateName = rs.getString("state_name");
                String stateStatus = rs.getString("state_status");
                String userId = rs.getString("user_id");
                String createDate = rs.getString("create_date");
                state = new com.magbel.admin.objects.State(stateId, stateCode,
                        stateName, stateStatus, userId, createDate);

                _list.add(state);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    private com.magbel.admin.objects.State getAState(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.State state = null;
        String query = "SELECT state_ID,state_code,state_name" + "  ,state_status,user_id,create_date" + " FROM am_gb_states ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String stateId = rs.getString("state_ID");
                String stateCode = rs.getString("state_code");
                String stateName = rs.getString("state_name");
                String stateStatus = rs.getString("state_status");
                String userId = rs.getString("user_id");
                String createDate = rs.getString("create_date");
                state = new com.magbel.admin.objects.State(stateId, stateCode,
                        stateName, stateStatus, userId, createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return state;

    }

    public com.magbel.admin.objects.State getStateByID(String stateid) {
        String filter = " WHERE state_ID=" + stateid;
        com.magbel.admin.objects.State state = getAState(filter);
        return state;

    }

//NEW CATEGORY FROM LANRE
    private com.magbel.admin.objects.Region getARegion(String filter) {
        com.magbel.admin.objects.Region region = null;
        String query = "SELECT Region_Id, Region_Code, Region_Name" + ", Region_Acronym, Region_Address" + ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date" + " FROM AM_AD_REGION ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String regionId = rs.getString("Region_Id");
                String regionCode = rs.getString("Region_Code");
                String regionName = rs.getString("Region_Name");
                String regionAcronym = rs.getString("Region_Acronym");
                String regionAddress = rs.getString("Region_Address");
                String regionPhone = rs.getString("Region_Phone");
                String regionFax = rs.getString("Region_Fax");
                String regionStatus = rs.getString("Region_Status");
                String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");

                region = new com.magbel.admin.objects.Region();
                region.setRegionId(regionId);
                region.setRegionCode(regionCode);
                region.setRegionName(regionName);
                region.setRegionAcronym(regionAcronym);
                region.setRegionAddress(regionAddress);
                region.setRegionStatus(regionStatus);
                region.setRegionPhone(regionPhone);
                region.setRegionFax(regionFax);
                region.setUserId(userId);
                region.setCreateDate(createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return region;

    }

    public com.magbel.admin.objects.Region getRegionByID(String RegID) {
        com.magbel.admin.objects.Region region = null;

        String filter = " WHERE Region_Id = " + RegID;
        region = getARegion(filter);
        return region;

    }

    public com.magbel.admin.objects.Region getRegionByCode(String Regcode) {
        com.magbel.admin.objects.Region region = null;

        String filter = " WHERE  Region_Code = '" + Regcode + "' ";
        region = getARegion(filter);
        return region;

    }

    public com.magbel.admin.objects.State getStateByCode(String statecode) {
        String filter = " WHERE state_code='" + statecode + "'";
        com.magbel.admin.objects.State state = getAState(filter);
        return state;

    }

    public java.util.List getCategoryByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Category category = null;
        String query = "SELECT category_ID,category_code,category_name" + ",category_acronym,Required_for_fleet" + ",Category_Class ,PM_Cycle_Period,mileage" + ",Notify_Maint_Days ,notify_every_days,residual_value" + ",Dep_rate ,Asset_Ledger,Dep_ledger" + ",Accum_Dep_ledger ,gl_account,insurance_acct" + ",license_ledger ,fuel_ledger,accident_ledger" + ",Category_Status ,user_id,create_date" + ",acct_type ,currency_Id,enforceBarcode" + " FROM am_ad_category  ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String categoryId = rs.getString("category_ID");
                String categoryCode = rs.getString("category_code");
                String categoryName = rs.getString("category_name");
                String categoryAcronym = rs.getString("category_acronym");
                String requiredforFleet = rs.getString("Required_for_fleet");
                String categoryClass = rs.getString("Category_Class");
                String pmCyclePeriod = rs.getString("PM_Cycle_Period");
                String mileage = rs.getString("mileage");
                String notifyMaintdays = rs.getString("Notify_Maint_Days");
                String notifyEveryDays = rs.getString("notify_every_days");
                String residualValue = rs.getString("residual_value");
                String depRate = rs.getString("Dep_rate");
                String assetLedger = rs.getString("Asset_Ledger");
                String depLedger = rs.getString("Dep_ledger");
                String accumDepLedger = rs.getString("Accum_Dep_ledger");
                String glAccount = rs.getString("gl_account");
                String insuranceAcct = rs.getString("insurance_acct");
                String licenseLedger = rs.getString("license_ledger");
                String fuelLedger = rs.getString("fuel_ledger");
                String accidentLedger = rs.getString("accident_ledger");
                String categoryStatus = rs.getString("Category_Status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                String acctType = rs.getString("acct_type");
                String currencyId = rs.getString("currency_Id");
                String enforeBarcode = rs.getString("enforceBarcode");
                category = new com.magbel.admin.objects.Category(categoryId,
                        categoryCode, categoryName, categoryAcronym,
                        requiredforFleet, categoryClass, pmCyclePeriod,
                        mileage, notifyMaintdays, notifyEveryDays,
                        residualValue, depRate, assetLedger, depLedger,
                        accumDepLedger, glAccount, insuranceAcct,
                        licenseLedger, fuelLedger, accidentLedger,
                        categoryStatus, userId, createDate, acctType,
                        currencyId, enforeBarcode);
                _list.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    private com.magbel.admin.objects.Category getACategory(String filter) {
        com.magbel.admin.objects.Category category = null;
        String query = "SELECT category_ID,category_code,category_name" + ",category_acronym,Required_for_fleet" + ",Category_Class ,PM_Cycle_Period,mileage" + ",Notify_Maint_Days ,notify_every_days,residual_value" + ",Dep_rate ,Asset_Ledger,Dep_ledger" + ",Accum_Dep_ledger ,gl_account,insurance_acct" + ",license_ledger ,fuel_ledger,accident_ledger" + ",Category_Status ,user_id,create_date" + ",acct_type ,currency_Id,enforceBarcode" + " FROM am_ad_category ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String categoryId = rs.getString("category_ID");
                String categoryCode = rs.getString("category_code");
                String categoryName = rs.getString("category_name");
                String categoryAcronym = rs.getString("category_acronym");
                String requiredforFleet = rs.getString("Required_for_fleet");
                String categoryClass = rs.getString("Category_Class");
                String pmCyclePeriod = rs.getString("PM_Cycle_Period");
                String mileage = rs.getString("mileage");
                String notifyMaintdays = rs.getString("Notify_Maint_Days");
                String notifyEveryDays = rs.getString("notify_every_days");
                String residualValue = rs.getString("residual_value");
                String depRate = rs.getString("Dep_rate");
                String assetLedger = rs.getString("Asset_Ledger");
                String depLedger = rs.getString("Dep_ledger");
                String accumDepLedger = rs.getString("Accum_Dep_ledger");
                String glAccount = rs.getString("gl_account");
                String insuranceAcct = rs.getString("insurance_acct");
                String licenseLedger = rs.getString("license_ledger");
                String fuelLedger = rs.getString("fuel_ledger");
                String accidentLedger = rs.getString("accident_ledger");
                String categoryStatus = rs.getString("Category_Status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                String acctType = rs.getString("acct_type");
                String currencyId = rs.getString("currency_Id");
                String enforeBarcode = rs.getString("enforceBarcode");
                category = new com.magbel.admin.objects.Category(categoryId,
                        categoryCode, categoryName, categoryAcronym,
                        requiredforFleet, categoryClass, pmCyclePeriod,
                        mileage, notifyMaintdays, notifyEveryDays,
                        residualValue, depRate, assetLedger, depLedger,
                        accumDepLedger, glAccount, insuranceAcct,
                        licenseLedger, fuelLedger, accidentLedger,
                        categoryStatus, userId, createDate, acctType,
                        currencyId, enforeBarcode);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return category;

    }

    public com.magbel.admin.objects.Category getCategoryByID(String categoryid) {
        String filter = " WHERE category_ID=" + categoryid;
        com.magbel.admin.objects.Category category = getACategory(filter);
        return category;

    }

    public com.magbel.admin.objects.Category getCategoryByCode(String categorycode) {
        String filter = " WHERE category_code='" + categorycode + "'";
        com.magbel.admin.objects.Category category = getACategory(filter);
        return category;

    }

    public java.util.List getRegionByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Region region = null;
        String query = "SELECT Region_Id, Region_Code, Region_Name" + ", Region_Acronym, Region_Address" + ",Region_Phone , Region_Fax, Region_Status, User_Id, Create_Date" + " FROM AM_AD_REGION ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String regionId = rs.getString("Region_Id");
                String regionCode = rs.getString("Region_Code");
                String regionName = rs.getString("Region_Name");
                String regionAcronym = rs.getString("Region_Acronym");
                String regionAddress = rs.getString("Region_Address");
                String regionPhone = rs.getString("Region_Phone");
                String regionFax = rs.getString("Region_Fax");
                String regionStatus = rs.getString("Region_Status");
                String userId = rs.getString("User_Id");
                String createDate = rs.getString("Create_Date");

                region = new com.magbel.admin.objects.Region(regionId, regionCode,
                        regionName, regionAcronym, regionAddress, regionPhone,
                        regionFax, regionStatus, userId, createDate);
                _list.add(region);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.List getAllDeptInBranch(String branchid) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.BranchDept dept = null;

        String query = "SELECT branchCode,deptCode,gl_prefix" + ",gl_suffix,branchId,deptId,mtid" + " FROM sbu_branch_dept WHERE branchId='" + branchid + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");

                dept = new com.magbel.admin.objects.BranchDept();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);

                _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }
        return _list;

    }

    public java.util.List getAllSectionInDept(String branchid, String deptId) {
        com.magbel.admin.objects.DeptSection dept = null;
        java.util.ArrayList _list = new java.util.ArrayList();
        String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode" + ",gl_suffix,branchId,deptId,sectionId,mtid" + " FROM sbu_dept_section WHERE branchId='" + branchid + "' AND deptId='" + deptId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");
                String sectioncode = rs.getString("sectionCode");
                String sectiondi = rs.getString("sectionId");

                dept = new com.magbel.admin.objects.DeptSection();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);
                dept.setSectionCode(sectioncode);
                dept.setSectionId(sectiondi);
                _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return _list;

    }

    public java.util.List getProvinceByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Province province = null;
        String query = "SELECT Province_ID, Province_Code, Province" + ",Status, User_id, create_date" + " FROM am_gb_Province ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String provinceid = rs.getString("Province_ID");
                String provincecode = rs.getString("Province_Code");
                String provincename = rs.getString("Province");
                String status = rs.getString("Status");
                String userid = rs.getString("User_id");
                String createdt = df.formatDate(rs.getDate("create_date"));

                province = new com.magbel.admin.objects.Province();
                province.setProvinceId(provinceid);
                province.setProvinceCode(provincecode);
                province.setProvince(provincename);
                province.setStatus(status);
                province.setUserId(userid);
                province.setCreateDate(createdt);
                _list.add(province);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }

        return _list;

    }

    private com.magbel.admin.objects.Province getAProvince(String filter) {
        com.magbel.admin.objects.Province province = null;
        String query = "SELECT Province_ID, Province_Code, Province" + ",Status, User_id, create_date" + " FROM am_gb_Province ";

        query = query + filter;
        //System.out.println(" the query is >> " + query);
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String provinceid = rs.getString("Province_ID");
                String provincecode = rs.getString("Province_Code");
                String provincename = rs.getString("Province");
                String status = rs.getString("Status");
                String userid = rs.getString("User_id");
                String createdt = df.formatDate(rs.getDate("create_date"));

                province = new com.magbel.admin.objects.Province();
                province.setProvinceId(provinceid);
                province.setProvinceCode(provincecode);
                province.setProvince(provincename);
                province.setStatus(status);
                province.setUserId(userid);
                province.setCreateDate(createdt);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return province;

    }

    public com.magbel.admin.objects.Province getProvinceByID(String provinceid) {
        String filter = " WHERE Province_ID= " + provinceid;
        com.magbel.admin.objects.Province province = getAProvince(filter);
        return province;

    }

    public com.magbel.admin.objects.Province getProvinceByCode(String provincecode) {
        String filter = " WHERE Province_Code='" + provincecode + "' ";
        com.magbel.admin.objects.Province province = getAProvince(filter);
        return province;

    }

    public boolean removeDeptFromBranch(java.util.ArrayList list) {
        String query = "DELETE FROM sbu_branch_dept" + " WHERE branchId=?" + " AND deptId=?";
        String query2 = "DELETE FROM sbu_dept_section" + " WHERE branchId=?" + " AND deptId=?";

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        com.magbel.admin.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps2 = con.prepareStatement(query2);
            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.BranchDept) list.get(i);

                ps.setString(1, bd.getBranchId());
                ps.setString(2, bd.getDeptId());

                ps.addBatch();
                ps2.setString(1, bd.getBranchId());
                ps2.setString(2, bd.getDeptId());
                ps2.addBatch();

            }

            d = ps.executeBatch();
            ps2.addBatch();
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public com.magbel.admin.objects.BranchDept getDeptInBranch(String branchid,
            String deptId) {
        com.magbel.admin.objects.BranchDept dept = null;

        String query = "SELECT branchCode,deptCode,gl_prefix" + ",gl_suffix,branchId,deptId,mtid" + " FROM sbu_branch_dept WHERE branchId='" + branchid + "' AND deptId='" + deptId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");

                dept = new com.magbel.admin.objects.BranchDept();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);

                // _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return dept;

    }

    public com.magbel.admin.objects.DeptSection getSectionInDept(String branchid,
            String deptId, String sectionId) {
        com.magbel.admin.objects.DeptSection dept = null;

        String query = "SELECT branchCode,deptCode,gl_prefix,sectionCode" + ",gl_suffix,branchId,deptId,sectionId,mtid" + " FROM sbu_dept_section WHERE branchId='" + branchid + "' AND deptId='" + deptId + "'" + " AND sectionId='" + sectionId + "'";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String branchcode = rs.getString("branchCode");
                String deptcode = rs.getString("deptCode");
                String glprefix = rs.getString("gl_prefix");
                String glsuffix = rs.getString("gl_suffix");
                String branchId = rs.getString("branchId");
                String deptid = rs.getString("branchId");
                String mtid = rs.getString("mtid");
                String sectioncode = rs.getString("sectionCode");
                String sectiondi = rs.getString("sectionId");

                dept = new com.magbel.admin.objects.DeptSection();
                dept.setBranchCode(branchcode);
                dept.setBranchId(branchId);
                dept.setDeptId(deptid);
                dept.setDeptCode(deptcode);
                dept.setGl_prefix(glprefix);
                dept.setGl_suffix(glsuffix);
                dept.setMtid(mtid);
                dept.setSectionCode(sectioncode);
                dept.setSectionId(sectiondi);
                // _list.add(dept);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return dept;

    }

    public boolean removeSectionsFromDept(java.util.ArrayList list) {
        String query = "DELETE FROM sbu_dept_section" + " WHERE branchId=?" + " AND deptId=?" + " AND sectionId=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.DeptSection bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.DeptSection) list.get(i);

                ps.setString(1, bd.getBranchId());
                ps.setString(2, bd.getDeptId());
                ps.setString(3, bd.getSectionId());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error removing Section From Department ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean updateDeptForBranch(java.util.ArrayList list) {
        String query = "UPDATE sbu_branch_dept SET branchCode = ?" + ",deptCode = ?,gl_prefix = ?,gl_suffix = ?" + " WHERE branchId=?" + " AND deptId=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.BranchDept) list.get(i);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getGl_prefix());
                ps.setString(4, bd.getGl_suffix());
                ps.setString(5, bd.getBranchId());
                ps.setString(6, bd.getDeptId());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean insertSectionForDept(java.util.ArrayList list) {
        String query = "INSERT INTO sbu_dept_section(branchCode" + ",deptCode,sectionCode,branchId" + ",deptId,sectionId,gl_prefix" + ",gl_suffix,mtid)" + " VALUES(?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.DeptSection bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.DeptSection) list.get(i);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getSectionCode());
                ps.setString(4, bd.getBranchId());
                ps.setString(5, bd.getDeptId());
                ps.setString(6, bd.getSectionId());
                ps.setString(7, bd.getGl_prefix());
                ps.setString(8, bd.getGl_suffix());
                ps.setLong(9, System.currentTimeMillis());
                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean updateSectionForDept(java.util.ArrayList list) {
        String query = "UPDATE sbu_dept_section SET branchCode = ?" + ",deptCode = ?,sectionCode=?,gl_prefix = ?" + ",gl_suffix = ?" + " WHERE branchId=?" + " AND deptId=?" + " AND sectionId=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.DeptSection bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.DeptSection) list.get(i);

                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getSectionCode());
                ps.setString(4, bd.getGl_prefix());
                ps.setString(5, bd.getGl_suffix());
                ps.setString(6, bd.getBranchId());
                ps.setString(7, bd.getDeptId());
                ps.setString(8, bd.getSectionId());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error updating SectionFOrDept ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    private Connection getConnection() {
        Connection con = null;
        dc = new DataConnect("helpDesk");

        try {
            con = dc.getConnection();
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" + e.getMessage());
        }
        return con;
    }

    private void closeConnection(Connection con, Statement s) {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error getting connection ->" + e.getMessage());
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
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
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
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
        }
    }

    /**
     *
     * @param con
     *            Connection
     * @param ps
     *            PreparedStatement
     * @param rs
     *            ResultSet
     */
    private void closeConnection(Connection con, PreparedStatement ps,
            ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error closing connection ->" + e.getMessage());
        }
    }

    private boolean executeQuery(String query) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            done = ps.execute();

        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public String getProperties(String sele, String[] iden, String[] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }
        for (int i = 0; i < iden.length; i++) {
            html = html + "<option value='" + iden[i] + "' " +
                    (iden[i].equalsIgnoreCase(sele) ? " selected " : "") + ">" +
                    vals[i] + "</option> ";
        }

        return html;
    }

    public String getProperties(String sele, String[][] vals) {
        String html = new String();
        if (sele == null) {
            sele = " ";
        }

        if (vals != null) {
            for (int i = 0; i < vals.length; i++) {
                html = html + "<option value='" + vals[i][0] + "' " +
                        (vals[i][0].equalsIgnoreCase(sele) ? " selected " : "") +
                        ">" + vals[i][2] + "</option> ";
            }

        }

        return html;
    }

    public java.util.ArrayList getStateByStatus(String status) {
        String filter = " WHERE state_status='" + status + "'";
        java.util.ArrayList _list = (java.util.ArrayList) getStatesByQuery(filter);
        return _list;
    }

    public java.util.ArrayList getCategoryByStatus(String categorystatus) {
        String filter = " WHERE Category_Status='" + categorystatus + "'";
        java.util.ArrayList category = (java.util.ArrayList) getCategoryByQuery(filter);
        return category;
    }

    public java.util.ArrayList getProvinceByStatus(String status) {
        String filter = " WHERE Status='" + status + "'";
        java.util.ArrayList _list = (java.util.ArrayList) getProvinceByQuery(filter);
        return _list;
    }

    public java.util.ArrayList getBranchByBranchStatus(String status) {
        java.util.ArrayList _list = null;
        String filter = " WHERE BRANCH_STATUS ='" + status + "'";
        _list = (java.util.ArrayList) getBranchesByQuery(filter);
        return _list;
    }

    public java.util.ArrayList getDeptByDeptStatus(String status) {
        java.util.ArrayList _list = null;
        String filter = " WHERE Dept_Status='" + status + "'";
        _list = (java.util.ArrayList) getDeparmentsByQuery(filter);
        return _list;

    }

    public String createsave2(String sbcode, String sbname, String sbcontact, String sbstatus, String contactmail) {
        Connection c = null;
        Statement s = null;
        ResultSet r = null;
        int rowcount;
        String message = "No Duplicate Found";
        try {
            c = getConnection();
            s = c.createStatement();
        //    System.out.println("established connection...");

            r = s.executeQuery("select * from Sbu_SetUp where [Sbu_code] = '" + sbcode + "' AND [Sbu_name] ='" + sbname + "' AND [Sbu_contact]='" + sbcontact + "' AND [Status]='" + sbstatus + "' AND [Contact_email]='" + contactmail + "'");
            if (r.next()) {
                message = "Duplicates Found";
           //     System.out.println("FOUND >>>>>>>>>>>>>>>>>>>" + message);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, s, r);
        }
        return message;
    }

    public boolean SaveSbuSetup(String sbcode, String sbname, String sbcontact, String sbstatus, String contactmail) {
        boolean done = false;
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO Sbu_SetUp" + "(Sbu_code, Sbu_name" + ",Sbu_contact, Status,Contact_email)" + " VALUES (?,?,?,?,?)";

        //int rowcount;
        //String message = "";
        try {
            con = getConnection();
         //   System.out.println("established connection...");
            ps = con.prepareStatement(query);
            ps.setString(1, sbcode);
            ps.setString(2, sbname);
            ps.setString(3, sbcontact);
            ps.setString(4, sbstatus);
            ps.setString(5, contactmail);
            done = (ps.executeUpdate() != -1);
      //      System.out.println("output " + done);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public String UpdateSbu(String sbucode, String sbuname, String sbucontact, String status, String contactmail) {
     //   System.out.print(sbucode);
  //      System.out.print(sbuname);
     //   System.out.print(sbucontact);
      //  System.out.print(status);



        String mess = "";
        Connection con = null;
        Statement s = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = getConnection();

            String updatequery = "UPDATE Sbu_SetUp SET Sbu_name =?,Sbu_contact =?,Status = ?,Contact_email= ? WHERE Sbu_code=?";
            ps = con.prepareStatement(updatequery);
            ps.setString(1, sbuname);
         //   System.out.print("wetin dey happen self" + sbuname);
            ps.setString(2, sbucontact);
        //    System.out.print("wetin dey happen self" + sbucontact);
            ps.setString(3, status);
         //   System.out.print("wetin dey happen self" + status);
            ps.setString(4, contactmail);
            ps.setString(5, sbucode);

        //    System.out.print("wetin dey happen self" + sbucode);
        //    System.out.print("wetin dey happen self checkpoint 1");
            int cnt = ps.executeUpdate();
            mess = "Success_update";
       //     System.out.print("wetin dey happen self checkpoint 2" + mess);
            if (cnt > 0) {
                mess = "Success_update";
    //            System.out.println("*********************" + mess);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }

        return mess;

    }
 
    public String UpdateMailStatement(String mailcode, String mailheading,String maildescription, String mailaddress, String transactiontype, String status, String category, String mailtype,String helptype) {
   //     System.out.print(mailcode);
    //    System.out.print(maildescription);
   //     System.out.print(mailaddress);


        handler = new AdminHandler();

        String mess = "";
        Connection con = null;
        Statement s = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            String updatequery = "UPDATE HD_mail_statement SET Mail_description=?, mail_heading = ?, Transaction_type=?, Status = ?, Category_Code = ?, Mail_Type = ?, Help_Type = ? WHERE Mail_code=?";
            ps = con.prepareStatement(updatequery);
            ps.setString(1, maildescription);
            ps.setString(2, mailheading);
        //    ps.setString(3, mailaddress);
        //    ps.setString(4, createdate);
            ps.setString(3, transactiontype);
            ps.setString(4, status);
            ps.setString(5, category);
            ps.setString(6, mailtype);
            ps.setString(7, helptype);  
            ps.setString(8, mailcode);


     //       System.out.print("wetin dey happen self" + mailcode);
     //       System.out.print("wetin dey happen self checkpoint 1");
            int cnt = ps.executeUpdate();
            mess = "Success_update";
        //    System.out.print("wetin dey happen self checkpoint 2" + mess);
            if (cnt > 0) {
                mess = "Success_update";
        //        System.out.println("*********************" + mess);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }

        return mess;

    }

    public java.util.List getsbuByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sbu_branch sbu = null;
        String query = "SELECT Sbu_code,Sbu_name,Sbu_contact" + "  ,Status " + " ,Contact_email" + " FROM Sbu_SetUp ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String code = rs.getString("Sbu_code");
                String name = rs.getString("Sbu_name");
                String contact = rs.getString("Sbu_contact");
                String status = rs.getString("Status");
                String email = rs.getString("Contact_email");
                //String user_id = rs.getString("user_id");
                sbu = new com.magbel.admin.objects.Sbu_branch(code, name,
                        contact, status, email);

                _list.add(sbu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public com.magbel.admin.objects.AssignSbu getSbuPrivilege(String classid) {
        com.magbel.admin.objects.AssignSbu classprivilege = null;

        String query = "SELECT SBU_NAME,GL_PREFIX,GL_SUFIX" + " FROM AM_SBU_ATTACHEMENT" + " WHERE SBU_NAME=? ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, classid);
            //ps.setString(2, roleuuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                String clss_uuid = rs.getString("SBU_NAME");

                String role_uuid = rs.getString("GL_PREFIX");

                String role_view = rs.getString("GL_SUFIX");

                //String role_addn = rs.getString("role_addn");

                //String role_edit = rs.getString("role_edit");
                //classprivilege = new legend.admin.objects.AssignSbu(
                //clss_uuid, role_uuid, role_view);
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching Class Privileges ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return classprivilege;
    }

    public boolean insertAssignSbuPrivileges2(java.util.ArrayList list) {



        String query = "INSERT INTO AM_SBU_ATTACHEMENT" + "(MTID, ATTACH_ID" + ",SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE)" + " VALUES (?,?,?,?,?,?,?,?)";




        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;



        com.magbel.admin.objects.AssignSbu cp = null;
        com.magbel.admin.objects.User user = null;
        ApplicationHelper help = new ApplicationHelper();


        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");
            for (int i = 0; i < list.size(); i++) {
                cp = (com.magbel.admin.objects.AssignSbu) list.get(i);

                user = new com.magbel.admin.objects.User();
                // int branchID = getBranchIDforSBU(cp.getAttachid());
                ps.setString(1, id);
                //System.out.println("james bon1");
                ps.setString(2, cp.getAttachid());
             //   System.out.println("james bon2");
                ps.setString(3, cp.getSbuname());
               // System.out.println("james bon3");
                ps.setString(4, cp.getGlprifix());
              //  System.out.println("james bon4");
                ps.setString(5, cp.getGlsurfix());
            //    System.out.println("james bon5");
                ps.setDate(6, df.dateConvert(new java.util.Date()));
            //    System.out.println("james bon6");
                ps.setString(7, user.getUserId());
           //     System.out.println("james bon7");
                ps.setString(8, cp.getSbucode());
            //    System.out.println("james bon8");

                //ps.setInt(9, branchID);
                //System.out.println("the value of branch id from am_sbu_attach is.........." + branchID);
                ps.addBatch();
            //    System.out.println("james bon9");

                //int branchID = getBranchIDforSBU(cp.getAttachid());

            }
            d = ps.executeBatch();
         //   System.out.println("james bon10");
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return (d.length > 0);
    }

    public boolean updateAvaliableSbu(java.util.ArrayList list) {
        String query = "UPDATE AM_SBU_ATTACHEMENT SET ATTACH_ID=?" + ",SBU_NAME= ?,GL_PREFIX = ?,GL_SUFIX = ?,CREATE_DATE =?,CREATE_USER =?,SBU_CODE=?" + " WHERE SBU_NAME=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.AssignSbu bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.AssignSbu) list.get(i);
         /*      System.out.println("*********b4************");
                System.out.println(bd.getAttachid());
                System.out.println(bd.getSbuname());
                System.out.println(bd.getGlprifix());
                System.out.println(bd.getGlsurfix());

                System.out.println(bd.getCreateuser());
                System.out.println(bd.getSbucode());
                System.out.println(bd.getMitd());
                System.out.println("*********f4*************"); */
                ps.setString(1, bd.getAttachid());
                ps.setString(2, bd.getSbuname());
                ps.setString(3, bd.getGlprifix());
                ps.setString(4, bd.getGlsurfix());
                ps.setDate(5, df.dateConvert(new java.util.Date()));
                ps.setString(6, bd.getCreateuser());
                ps.setString(7, bd.getSbucode());
                ps.setString(8, bd.getSbuname());

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public boolean updateAvaliableSbu(java.util.ArrayList list, int userid, String branchCode, int loginId, String eff_date) {
        String query = "UPDATE AM_SBU_ATTACHEMENT SET ATTACH_ID=?" + ",SBU_NAME= ?,GL_PREFIX = ?,GL_SUFIX = ?,CREATE_DATE =?,CREATE_USER =?,SBU_CODE=?" + " WHERE SBU_NAME=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.AssignSbu bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.AssignSbu) list.get(i);
         /*       //System.out.println("*********b4************");
                System.out.println(bd.getAttachid());
                System.out.println(bd.getSbuname());
                System.out.println(bd.getGlprifix());
                System.out.println(bd.getGlsurfix());

                System.out.println(bd.getCreateuser());
                System.out.println(bd.getSbucode());
                System.out.println(bd.getMitd());
                //System.out.println("*********f4*************"); */
                ps.setString(1, bd.getAttachid());
                ps.setString(2, bd.getSbuname());
                ps.setString(3, bd.getGlprifix());
                ps.setString(4, bd.getGlsurfix());
                ps.setDate(5, df.dateConvert(new java.util.Date()));
                ps.setString(6, bd.getCreateuser());
                ps.setString(7, bd.getSbucode());
                ps.setString(8, bd.getSbuname());

                //compareAuditValues(bd.getGl_prefix(),bd.getGl_suffix(),bd.getBranchId(),bd.getDeptCode(),String.valueOf(userid),branchCode,loginId,eff_date);
                compareAuditValuesSbu(bd.getGlprifix(), bd.getGlsurfix(), bd.getAttachid(), bd.getSbucode(), String.valueOf(userid), branchCode, loginId, eff_date);

                ps.addBatch();
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public String getBranchesByQuery2() {
        String cv = "";

        String query = "SELECT Sbu_code,Sbu_name,Sbu_contact, Status, Contact_email  FROM Sbu_SetUp";


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                cv = rs.getString("Sbu_code");

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());
            System.out.print("i don no waiting dey happenn o!");
        } finally {
            closeConnection(c, s, rs);
        }
        return cv;

    }

    public java.util.ArrayList getBranchesByQuery2(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sbu_branch branch = null;
        String query = "SELECT Sbu_code,Sbu_name,Sbu_contact, Status, Contact_email FROM Sbu_SetUp" + filter;


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Sbu_code");
                String branchName = rs.getString("Sbu_name");
                String branchAcronym = rs.getString("Sbu_contact");
                String glPrefix = rs.getString("Status");
                String email = rs.getString("Contact_email");

                branch = new com.magbel.admin.objects.Sbu_branch(branchCode, branchName, branchAcronym, glPrefix, email);

                _list.add(branch);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());

        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getBranchesBysbucode(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Sbu_branch branch = null;
        String query = "SELECT Sbu_code,Sbu_name,Sbu_code ,Contact_email FROM Sbu_SetUP where Sbu_code ='" + filter + "'";


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Sbu_code");
                String branchName = rs.getString("Sbu_name");
                String branchAcronym = rs.getString("Sbu_contact");
                String glPrefix = rs.getString("Status");
                String mail = rs.getString("Contact_email");

                branch = new com.magbel.admin.objects.Sbu_branch(branchCode, branchName, branchAcronym, glPrefix, mail);

                _list.add(branch);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());

        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getApprovalByQuery2(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Aproval_limit branch = null;
        String query = "Select * from Approval_Limit" + filter;
       // System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Level_Code");

                double branchName = rs.getDouble("Min_Amount");

                double branchAcronym = rs.getDouble("Max_Amount");

                String glPrefix = rs.getString("Description");
                Aproval_limit condition = new Aproval_limit(branchCode, branchName, branchAcronym, glPrefix);



                _list.add(condition);


            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getApprovalByQuery2() {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Aproval_limit branch = null;
        String query = "Select * from Approval_Limit ";
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Level_Code");

                double branchName = rs.getDouble("Min_Amount");

                double branchAcronym = rs.getDouble("Max_Amount");

                String glPrefix = rs.getString("Description");

                Aproval_limit condition = new Aproval_limit(branchCode, branchName, branchAcronym, glPrefix);

                _list.add(condition);


            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());

        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public String Approval_Duplicate(String sbcode, double min, double max, String des) {
        Connection c = null;
        Statement s = null;
        ResultSet r = null;
        int rowcount;
        String message = "No Duplicate Found";
        try {
            c = getConnection();
            s = c.createStatement();
            r = s.executeQuery("select * from Approval_Limit where [Level_Code] = '" + sbcode + "' AND [Min_Amount] ='" + min + "' AND [Max_Amount]='" + max + "' AND [Description]='" + des + "'");

            if (r.next()) {

                message = "Duplicates Found";


            } else {
                message = "Message can not be display....";
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, s, r);
        }
        return message;
    }

    public String Approval_Level_Duplicate(String code, String trans_type, String level, String date, String userid) {
        Connection c = null;
        Statement s = null;
        ResultSet r = null;
        int rowcount;
        String message = "No Duplicate Found";
        try {
            c = getConnection();
            s = c.createStatement();
            System.out.println("established connection...");

            r = s.executeQuery("select * from Approval_Level_setup where [Code] = '" + code + "' AND [Transaction_type] ='" + trans_type + "' AND [Level]='" + level + "' AND [Date]='" + date + "' AND [User_id]='" + userid + "'");
            if (r.next()) {
                message = "Duplicates Found";
           //     System.out.println("FOUND >>>>>>>>>>>>>>>>>>>" + message);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, s, r);
        }
        return message;
    }

    public boolean isApprovalExisting(String code) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Level_Code,Min_Amount,Max_Amount,Description from Approval_Limit WHERE Level_Code = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, code);

            rs = ps.executeQuery();


            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    public boolean isApproval_Level_Existing(String codee) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Code,Transaction_type,Level from Approval_Level_setup where Code = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, codee);

            rs = ps.executeQuery();


            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    public String UpdateApproval_Limit(String code, double min_amount, double max_amount, String describ) {
        System.out.print(code);
        System.out.print(min_amount);
        System.out.print(max_amount);
        System.out.print(describ);



        String mess = "";
        Connection con = null;
        Statement s = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = getConnection();

            String updatequery = "UPDATE Approval_Limit SET Min_Amount=?, Max_Amount=?,Description=? WHERE Level_Code=?";
            ps = con.prepareStatement(updatequery);
            ps.setDouble(1, min_amount);

            ps.setDouble(2, max_amount);

            ps.setString(3, describ);
            ps.setString(4, code);

            int cnt = ps.executeUpdate();
            mess = "Success_update";

            if (cnt > 0) {
                mess = "Success_update";

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }

        return mess;

    }

    public java.util.ArrayList getApprovalLevelByQueryGprifix() {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.AssignSbu branch = null;
        String query = "Select * from AM_SBU_ATTACHEMENT ";
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");

                String branchCode = rs.getString("MTID");

                String branchName = rs.getString("ATTACH_ID");

                String branchAcronym = rs.getString("SBU_NAME");

                String glPrefix = rs.getString("GL_PREFIX");

                String userid = rs.getString("GL_SUFIX");

                String date = rs.getString("CREATE_DATE");

                String createuser = rs.getString("CREATE_USER");

                String sbucode = rs.getString("SBU_CODE");

                int y = 6;
                int u = 0;
                int t = (y < u) ? 6 : 9;

                AssignSbu condition = new AssignSbu();
                condition.setSbuname(branchAcronym);
                condition.setGlprifix(glPrefix);
                condition.setGlsurfix(userid);

                _list.add(condition);


            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());
            System.out.print("i don no waiting dey happenn o!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getApprovalLevelByQuery(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Aproval_limit branch = null;
        String query = "Select * from Approval_Level_setup " + filter;
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Code");

                String branchName = rs.getString("Transaction_type");

                String branchAcronym = rs.getString("Level");

                String glPrefix = rs.getString("Date");

                String userid = rs.getString("User_id");




                Approval_Level condition = new Approval_Level(branchCode, branchName, branchAcronym, glPrefix, userid);


                _list.add(condition);


            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());
            System.out.print("i don no waiting dey happenn o!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getApprovalLevelByQuery() {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Aproval_limit branch = null;
        String query = "Select * from Approval_Level_setup ";
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String branchCode = rs.getString("Code");

                String branchName = rs.getString("Transaction_type");

                String branchAcronym = rs.getString("Level");

                String glPrefix = rs.getString("Date");

                String userid = rs.getString("User_id");


                Approval_Level condition = new Approval_Level(branchCode, branchName, branchAcronym, glPrefix, userid);

                _list.add(condition);


            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());
            System.out.print("i don no waiting dey happenn o!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getBranchesByMail(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.mail_setup branch = null;
        String query = "SELECT Mail_code,Mail_heading,Mail_description,Mail_address, Transaction_type," +
                "Status,Category_Code,SubCategory_Code,Mail_Type,Help_Type FROM HD_mail_statement " + filter;


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {

                String MailCode = rs.getString("Mail_code");
                String mailheading = rs.getString("Mail_heading");
                String Description = rs.getString("Mail_description");
            //    String branchAcronym = rs.getString("Mail_address");
              //  String glPrefix = rs.getString("Creation_date");
                
                String TranType = rs.getString("Transaction_type");
              //  String userid = rs.getString("User_id");
                String status = rs.getString("Status");
                String category = rs.getString("Category_Code");
                String SubCategory = rs.getString("SubCategory_Code");
                String mailtype = rs.getString("Mail_Type");
                String helptype = rs.getString("Help_Type");


                branch = new com.magbel.admin.objects.mail_setup(MailCode,mailheading, Description, TranType, status,category,SubCategory,mailtype, helptype);

                _list.add(branch);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting branches ->" + e.getMessage());

        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public String CheckMailSetup(String mailcode,  String description, String address, String date, String trans_type, String userid, String status) {
        Connection c = null;
        Statement s = null;
        ResultSet r = null;
        int rowcount;
        String message = "No Duplicate Found";
        try {
            c = getConnection();
            s = c.createStatement();
            System.out.println("established connection...");

            r = s.executeQuery("select * from hd_mail_statement_view where [Mail_code]='" + mailcode + "' AND [Mail_description]" +
                    "='" + description + "' AND [Mail_address]='" + address + "' AND [Create_date]='" + date + "' AND" +
                    " [Transaction_type]='" + trans_type + "' AND [User_id]='" + userid + "' AND [Status] ='" + status + "'");
            if (r.next()) {
                message = "Duplicates Found";
                System.out.println("FOUND >>>>>>>>>>>>>>>>>>>" + message);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, s, r);
        }
        return message;
    }

    public boolean iscreatesave2(String codee) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Sbu_code,Sbu_name,Sbu_contact,Status from Sbu_SetUp where Sbu_code = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, codee);

            rs = ps.executeQuery();


            while (rs.next()) {
                done = true;
                System.out.println("records already exist " + done);

            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [am_raisentry_post_josh]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;

    }

    public boolean isCheckMailSetup(String codee) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String FINDER_QUERY = "SELECT Mail_code,mail_heading,Mail_description,Mail_address, Creation_date,Transaction_type,User_id,Status,Category_Code,Mail_Type,Help_Type from HD_mail_statement where Mail_code = ?";

        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, codee);
        //    System.out.println("-----codee isCheckMailSetup----- "+codee);
        //    System.out.println("-----FINDER_QUERY isCheckMailSetup----- "+FINDER_QUERY);

            while (rs.next()) {
                done = true;
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot fetch [HD_mail_statement]->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return done;
  
    } 

    public boolean isCheckUserId(String codee,String email) {
        boolean done = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

  //    System.out.println("-----codee isCheckUserId----- "+codee);
  //    System.out.println("-----Phone isCheckUserId----- "+email);

        String FINDER_QUERY = "SELECT User_Name,Full_Name from am_gb_User where User_Name = '"+codee.trim()+"' And email like '%"+email.trim()+"%'";


    //    System.out.println("-----FINDER_QUERY two field----- "+FINDER_QUERY);
	String Phone ="";
        try {
      //      con = getConnection();
      //      ps = con.prepareStatement(FINDER_QUERY);
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(FINDER_QUERY);

            while (rs.next()) {
                done = true;
                Phone = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARNING: cannot find Record->" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
     //   System.out.println("-----Phone isCheckUserId----- "+Phone);
        return done;

    }


    public String UpdateApproval_Level(String code, String trans_type, String level_id) {


        String mess = "";
        Connection con = null;
        Statement s = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = getConnection();

            String updatequery = "UPDATE Approval_Level_setup SET Transaction_type=?,Level=? WHERE Code=?";
            ps = con.prepareStatement(updatequery);
            ps.setString(1, trans_type);

            ps.setString(2, level_id);

            ps.setString(3, code);
            System.out.print("wetin dey happen self" + code);

            int cnt = ps.executeUpdate();
            mess = "Success_update";
            System.out.print("wetin dey happen self checkpoint 2" + mess);
            if (cnt > 0) {
                mess = "Success_update";

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }

        return mess;

    }

    public String getTransactCode(String Trasn_Id) {
        String query =
                "SELECT DESCRIPTION  FROM Am_Transaction_Type  " +
                "WHERE TRANS_CODE = '" + Trasn_Id + "' ";

        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        String branchcode = "0";
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {

                branchcode = rs.getString(1);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, stmt, rs);
        }
        return branchcode;
    }

    public boolean SaveMailSetup(String mailcode, String Heading, String description, String address, String date, String trans_type, String userid, String status, String category, String mailtype,String helptype, String Subcategory) {

        boolean done = false;
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        PreparedStatement ps = null;

        handler = new AdminHandler();


        String query = "INSERT INTO HD_mail_statement" + "(Mail_code,mail_heading, Mail_description" + ",Mail_address,creation_date,Transaction_type,User_id,Status,Category_Code,Mail_Type,Help_Type,SubCategory_Code)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            String id = help.getGeneratedId("HD_mail_statement");
            con = getConnection();

            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, Heading.toUpperCase());
            ps.setString(3, description.toUpperCase());
            ps.setString(4, address);
            ps.setDate(5, df.dateConvert(new java.util.Date()));
           // ps.setString(6, handler.getTransactCode(trans_type));
            ps.setString(6, trans_type);
            ps.setString(7, userid);
            ps.setString(8, status);
            ps.setString(9, category);
            ps.setString(10, mailtype);
            ps.setString(11, helptype);
            ps.setString(12, Subcategory);
            done = (ps.executeUpdate() != -1);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean SaveLostPassword(String UserName, String email, String date) {

        boolean done = false;
        Connection con = null;
        Statement s = null;  
        ResultSet r = null;
        PreparedStatement ps = null;

        handler = new AdminHandler();  
        System.out.println("=====About to Insert into SaveLostPassword====");
        String query = "INSERT INTO am_gb_LostPassword (ID,User_Name,email,Create_Date) VALUES (?,?,?,?)";

        try {
            String id = help.getGeneratedId("am_gb_LostPassword");
            con = getConnection();
            System.out.println("=====id===Inserted === "+id);
            ps = con.prepareStatement(query);
            ps.setString(1, id.trim());
            ps.setString(2, UserName);
            ps.setString(3, email);
            ps.setDate(4, df.dateConvert(new java.util.Date()));
            done = (ps.executeUpdate() != -1);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean deleteRule(String slaId) {
        boolean done = true;
        //Connection conn = null;
        con = getConnection();
        try {  
     //       con = getConnection("helpDesk");
        	System.out.println("<<<<<<slaId deleteRule>>>>> "+slaId);
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from am_criteria_rules where criteria_ID = '"+slaId+"'");
                ps.execute();
                ps.close();

            }
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from SLA_ESCALATE where criteria_ID = '"+slaId+"'");
                ps.execute();
                ps.close();

            }    
            if (!slaId.equals("")) { 
                PreparedStatement ps = con.prepareStatement("Delete from SLA_RESPONSE where criteria_ID = '"+slaId+"'");
                ps.execute();
                ps.close();

            }
            con.close();
        } catch (Exception e) {
            System.out.println(">> Error occued in Criteria Rule " + e);
            done = false;
        }
        return done;
    }

    public boolean SaveApproval(String code, double min_amount, double max_amount, String des) {
        boolean done = false;
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO Approval_Limit" + "(Level_code, Min_Amount" + ",Max_Amount,Description)" + " VALUES (?,?,?,?)";


        try {
            String id = help.getGeneratedId("Approval_Limit");
            con = getConnection();

            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setDouble(2, min_amount);
            ps.setDouble(3, max_amount);
            ps.setString(4, des.toUpperCase());

            done = (ps.executeUpdate() != -1);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public boolean SaveApproval_level(String code, String trans_type, String level, String date, String userid) {
        boolean done = false;
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO Approval_Level_setup" + "(code, Transaction_type" + ",Level,Date,User_id)" + " VALUES (?,?,?,?,?)";


        try {
            String id = help.getGeneratedId("Approval_Level_setup");
            con = getConnection();

            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, trans_type);
            ps.setString(3, level);
            ps.setDate(4, df.dateConvert(new java.util.Date()));
            ps.setString(5, userid);

            done = (ps.executeUpdate() != -1);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    public String checkBoxCheck(String sbu_code, String attach_id, String sbu_name) {
        String result = "N";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String FINDER_QUERY = " select distinct sbu_code from am_sbu_attachement where sbu_code in (select sbu_code from sbu_setup where sbu_code=?  and attach_id=?) and sbu_name in (select sbu_name from sbu_setup where sbu_name=?) ";
        try {
            con = getConnection();
            ps = con.prepareStatement(FINDER_QUERY);
            ps.setString(1, sbu_code);
            ps.setString(2, attach_id);
            ps.setString(3, sbu_name);

            rs = ps.executeQuery();
            while (rs.next()) {
                result = "Y";
            }

        } catch (Exception ex) {
            System.out.println("error generated" +
                    ex.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

        return result;

    }

    public String UpdateMailStatement(String mailcode, String mailheading,  String maildescription, String mailaddress, String transactiontype, String status, String category, String mailtype, String helptype,String subcategory) {

        handler = new AdminHandler();


        String mess = "";
        Connection con = null;
        Statement s = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            con = getConnection();

            String updatequery = "UPDATE HD_mail_statement SET mail_heading=?, Mail_description=?, Transaction_type=?, Status = ?, Category_Code =?, Mail_Type=?, Help_Type=?, SubCategory_Code =? WHERE Mail_code=?";
            ps = con.prepareStatement(updatequery);
            ps.setString(1, mailheading);
            ps.setString(2, maildescription);

          //  ps.setString(3, mailaddress);

          //  ps.setString(4, handler.getTransactCode(transactiontype));
            ps.setString(3, transactiontype);
            ps.setString(4, status);
            ps.setString(5, category);
            ps.setString(6, mailtype);
            ps.setString(7, helptype);
            ps.setString(8, subcategory);
            ps.setString(9, mailcode);

            int cnt = ps.executeUpdate();
            mess = "Success_update";

            if (cnt > 0) {
                mess = "Success_update";

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }

        return mess;

    }

    public com.magbel.admin.objects.AssignSbu getClassPrivilegeSbu(String attched_id) {
        com.magbel.admin.objects.AssignSbu classprivilege = null;

        String query = "Select MTID,Attach_id,SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE from AM_SBU_ATTACHEMENT WHERE Attach_id =?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ApplicationHelper help = new ApplicationHelper();
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1, attched_id);

            rs = ps.executeQuery();
            while (rs.next()) {

                String branchAcronym = rs.getString("SBU_NAME");

                String glPrefix = rs.getString("GL_PREFIX");

                String userid = rs.getString("GL_SUFIX");

                String date = rs.getString("CREATE_DATE");

                String createuser = rs.getString("CREATE_USER");

                String sbbb = rs.getString("SBU_CODE");

                String branchName = rs.getString("ATTACH_ID");

                String id = rs.getString("MTID");

                classprivilege = new com.magbel.admin.objects.AssignSbu();
                System.out.println("11");

                classprivilege.setMitd(id);
                classprivilege.setAttachid(branchName);
                classprivilege.setSbuname(branchAcronym);
                classprivilege.setGlprifix(glPrefix);
                classprivilege.setGlsurfix(userid);
                classprivilege.setCreatedate(date);
                classprivilege.setCreateuser(createuser);
                classprivilege.setSbucode(sbbb);


            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching Class Privileges ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return classprivilege;
    }

    public com.magbel.admin.objects.AssignSbu getClassPrivilegeSbu(String attched_id, String sbucode) {
        com.magbel.admin.objects.AssignSbu classprivilege = null;

        String query = "Select MTID,Attach_id,SBU_NAME,GL_PREFIX,GL_SUFIX,CREATE_DATE,CREATE_USER,SBU_CODE from AM_SBU_ATTACHEMENT WHERE Attach_id =? AND SBU_CODE =?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //ApplicationHelper help = new ApplicationHelper();
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1, attched_id);
            ps.setString(2, sbucode);

            rs = ps.executeQuery();
            while (rs.next()) {
                //	 String id = help.getGeneratedId("AM_SBU_ATTACHEMENT");
                String id = rs.getString("MTID");
                System.out.println("=================MTID====================" + id);
                String sbuname = rs.getString("SBU_NAME");
                System.out.println("=================SBU_NAME====================" + sbuname);


                String glPrefix = rs.getString("GL_PREFIX");
                System.out.println("=================GL_PREFIX====================" + glPrefix);
                String glsufix = rs.getString("GL_SUFIX");
                System.out.println("=================GL_SUFIX====================" + glsufix);
                String date = rs.getString("CREATE_DATE");
                System.out.println("=================CREATE_DATE====================" + date);
                String createuser = rs.getString("CREATE_USER");
                System.out.println("=================CREATE_USER====================" + createuser);
                String sbbbcode = rs.getString("SBU_CODE");
                System.out.println("=================SBU_CODE====================" + sbbbcode);
                String attachid = rs.getString("ATTACH_ID");
                System.out.println("=================ATTACH_ID====================" + attachid);

                classprivilege = new com.magbel.admin.objects.AssignSbu();









                classprivilege.setMitd(id);
                classprivilege.setSbuname(sbuname);

                classprivilege.setGlprifix(glPrefix);
                classprivilege.setGlsurfix(glsufix);
                classprivilege.setCreatedate(date);
                classprivilege.setCreateuser(createuser);
                classprivilege.setAttachid(attachid);
                classprivilege.setSbucode(sbbbcode);
                classprivilege.setCreateuser(createuser);



            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching Class Privileges ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return classprivilege;
    }
//victorial

    public boolean createBranch(Branch branch) {
        boolean done;
        Connection con = null;
        PreparedStatement ps = null;
        done = false;
        String query = "INSERT INTO am_ad_branch(BRANCH_ID,BRANCH_CODE,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH" +
                "_ADDRESS ,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE ,BRANCH_STATUS,USER_ID,CREATE_DA" +
                "TE,GL_SUFFIX,EMAIL)  VALUES(?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            apph = new ApplicationHelper();
            String stringid = apph.getGeneratedId("am_ad_branch");
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, stringid);
            ps.setString(2, branch.getBranchCode());
            ps.setString(3, branch.getBranchName());
            ps.setString(4, branch.getBranchAcronym());
            ps.setString(5, branch.getGlPrefix());
            ps.setString(6, branch.getBranchAddress());
            ps.setString(7, branch.getState());
            ps.setString(8, branch.getPhoneNo());
            ps.setString(9, branch.getFaxNo());
            ps.setString(10, branch.getRegion());
            ps.setString(11, branch.getProvince());
            ps.setString(12, branch.getBranchStatus());
            ps.setString(13, branch.getUsername());
            ps.setDate(14, df.dateConvert(new Date()));
            ps.setString(15, branch.getGlSuffix());
            ps.setString(16, branch.getEmailAddress());

            //ps.setLong(16,System.currentTimeMillis());
            done = ps.executeUpdate() != -1;
        } catch (Exception e) {
            System.out.println(getClass().getName() + " ERROR:Error Creating Branch ->" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;
    }

    private Branch getABranch(String filter) {
        Branch branch;
        branch = null;


        String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
                "S,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
                "ATE,EMAIL FROM am_ad_branch ";
        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        try {
            c = getConnection();
            s = c.createStatement();
            for (rs = s.executeQuery(query); rs.next();) {
                String branchId = rs.getString("BRANCH_ID");
                String branchCode = rs.getString("BRANCH_CODE");
                String branchName = rs.getString("BRANCH_NAME");
                String branchAcronym = rs.getString("BRANCH_ACRONYM");
                String glPrefix = rs.getString("GL_PREFIX");
                String branchAddress = rs.getString("BRANCH_ADDRESS");
                String state = rs.getString("STATE");
                String phoneNo = rs.getString("PHONE_NO");
                String faxNo = rs.getString("FAX_NO");
                String province = rs.getString("PROVINCE");
                String region = rs.getString("REGION");
                String branchStatus = rs.getString("BRANCH_STATUS");
                String username = rs.getString("USER_ID");
                String glSuffix = rs.getString("GL_SUFFIX");
                String createDate = rs.getString("CREATE_DATE");
                String emailAddress = rs.getString("EMAIL");
                //int location = rs.getInt("LOCATION");
                branch = new Branch(branchId, branchCode, branchName, branchAcronym, glPrefix, glSuffix, branchAddress, state, phoneNo, faxNo, region, province, branchStatus, username, createDate, emailAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return branch;
    }
    

    private Organization getAOrgan(String filter) {
    	Organization organ;
    	organ = null;


        String query = "SELECT MTID,ORG_CODE,ORG_NAME,ACRONYM,ADDRESS,PHONE,FAX, STATUS,USER_ID,CREATE_DATE,Domain,email,CONTACT_PERSON FROM AM_AD_ORGANIZATION ";
        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        try {
        	
            c = getConnection();
            s = c.createStatement();
            for (rs = s.executeQuery(query); rs.next();) {
                String organId = rs.getString("MTID");
                String organCode = rs.getString("ORG_CODE");
                String organName = rs.getString("ORG_NAME");
                String organAcronym = rs.getString("ACRONYM");
                String organAddress = rs.getString("ADDRESS");
                String phoneNo = rs.getString("PHONE");
                String faxNo = rs.getString("FAX");
                String organStatus = rs.getString("STATUS");
                String username = rs.getString("USER_ID");
                String createDate = rs.getString("CREATE_DATE");
                String organDomain = rs.getString("Domain");
                String organemail = rs.getString("email");
                String organcontact = rs.getString("CONTACT_PERSON");
                organ = new Organization(organId, organCode, organName, organAcronym, organAddress, phoneNo, faxNo, organStatus, username, createDate,organDomain,organemail,organcontact);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return organ;
    }
        
    
    public boolean updateBranch(Branch branch) {
        boolean done;
        Connection con = null;
        PreparedStatement ps = null;
        done = false;
        String query = "UPDATE am_ad_branch SET BRANCH_CODE =?,BRANCH_NAME = ?,BRANCH_ACRONYM = ? ,GL_PR" +
                "EFIX = ?,BRANCH_ADDRESS = ?,STATE = ? ,PHONE_NO = ?,FAX_NO = ?,REGION = ?,PROVIN" +
                "CE = ? ,BRANCH_STATUS = ?,GL_SUFFIX = ?, EMAIL = ? WHERE BRANCH_ID =?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, branch.getBranchCode());
            ps.setString(2, branch.getBranchName());
            ps.setString(3, branch.getBranchAcronym());
            ps.setString(4, branch.getGlPrefix());
            ps.setString(5, branch.getBranchAddress());
            ps.setString(6, branch.getState());
            ps.setString(7, branch.getPhoneNo());
            ps.setString(8, branch.getFaxNo());
            ps.setString(9, branch.getRegion());
            ps.setString(10, branch.getProvince());
            ps.setString(11, branch.getBranchStatus());
            ps.setString(12, branch.getGlSuffix());
            ps.setString(13, branch.getEmailAddress());

            ps.setString(14, branch.getBranchId());
            done = ps.executeUpdate() != -1;
        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query ->" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public boolean insertDeptForBranch(java.util.ArrayList list) {

        String query = "INSERT INTO sbu_branch_dept(branchCode" + ",deptCode,branchId" + ",deptId,gl_prefix" + ",gl_suffix)" + " VALUES(?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.BranchDept) list.get(i);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getBranchId());
                ps.setString(4, bd.getDeptId());
                ps.setString(5, bd.getGl_prefix());
                ps.setString(6, bd.getGl_suffix());
                ps.addBatch();
            }
            d = ps.executeBatch();
        } catch (Exception ex) {
            System.out.println("AdminHandler: insertDeptForBranch(): WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return (d.length > 0);
    }

    public boolean removeAvaliableSbu(java.util.ArrayList list) {
        String query = "DELETE FROM AM_SBU_ATTACHEMENT" + " WHERE Sbu_Code=?" + " AND Sbu_name=? AND ATTACH_ID=?";
        String query2 = "DELETE FROM Sbu_SetUp" + " WHERE Sbu_Code=?" + " AND Sbu_name=? ";

        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        com.magbel.admin.objects.AssignSbu bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps2 = con.prepareStatement(query2);
            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.AssignSbu) list.get(i);

                ps.setString(1, bd.getSbucode());
                ps.setString(2, bd.getSbuname());
                ps.setString(3, bd.getAttachid());

                ps.addBatch();
                ps2.setString(1, bd.getSbucode());
                ps2.setString(2, bd.getSbuname());
                ps2.addBatch();

            }

            d = ps.executeBatch();
            ps2.addBatch();
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset - from am_sbu_attachement>" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public String createResignation(Resignation resign) {

        Connection con = null;
        PreparedStatement ps = null;
        String result = null;
        String query = "INSERT INTO am_ad_Resignation(Staff_Name,Department" + "  ,Date_of_Resumption,Section_Unit,Exit_Type,Exit_Reason,User_id,Create_date)" + "   VALUES (?,?,?,?,?,?,?,?)";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, resign.getStaffname());
            ps.setString(2, resign.getDepartment());
            ps.setString(3, resign.getDate_of_resumption());
            ps.setString(4, resign.getSection_unit());
            ps.setString(5, resign.getExit_type());
            ps.setString(6, resign.getExit_reason());
            ps.setInt(7, resign.getUserId());
            ps.setDate(8, df.dateConvert(new java.util.Date()));

            int res = ps.executeUpdate();
            if (res > 0) {
                result = "SUCCESS";
            } else {
                result = "FAILED";
            }

        } catch (Exception e) {
            System.out.println("WARNING:Error creating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return result;

    }

    public java.util.ArrayList getResignationList() {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Resignation resign = null;
        String query = "Select * from am_ad_Resignation";
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                int staffid = rs.getInt("Staff_ID");
                String staffname = rs.getString("Staff_Name");
                String department = rs.getString("Department");
                String date_of_Resumption = rs.getString("Date_of_Resumption");
                String section_Unit = rs.getString("Section_Unit");
                String exit_Type = rs.getString("Exit_Type");
                String exit_Reason = rs.getString("Exit_Reason");

                resign = new Resignation();
                resign.setStaffId(staffid);
                resign.setStaffname(staffname);
                resign.setDepartment(department);
                resign.setDate_of_resumption(date_of_Resumption);
                resign.setSection_unit(section_Unit);
                resign.setExit_type(exit_Type);
                resign.setExit_reason(exit_Reason);
                _list.add(resign);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting Resignation ->" + e.getMessage());
            System.out.print("Error viewing Resigination!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getResignationList(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Resignation resign = null;
        String query = "Select * from am_ad_Resignation" + filter;
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                int staffid = rs.getInt("Staff_ID");
                String staffname = rs.getString("Staff_Name");
                String department = rs.getString("Department");
                String date_of_Resumption = rs.getString("Date_of_Resumption");
                String section_Unit = rs.getString("Section_Unit");
                String exit_Type = rs.getString("Exit_Type");
                String exit_Reason = rs.getString("Exit_Reason");

                resign = new Resignation();
                resign.setStaffId(staffid);
                resign.setStaffname(staffname);
                resign.setDepartment(department);
                resign.setDate_of_resumption(date_of_Resumption);
                resign.setSection_unit(section_Unit);
                resign.setExit_type(exit_Type);
                resign.setExit_reason(exit_Reason);
                _list.add(resign);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting Resignation ->" + e.getMessage());
            System.out.print("Error viewing Resigination!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public String updateResignation(Resignation resign) {
        System.out.println("Resignation  enter 1");
        Connection con = null;
        PreparedStatement ps = null;
        String result = null;

        //String query = "UPDATE am_ad_Resignation SET Staff_Name=?,Department=?,Date_of_Resumption=?,Section_Unit=?,Exit_Type=?,Exit_Reason=?,User_id=? WHERE Staff_ID =?";

        String query = "UPDATE am_ad_Resignation SET Staff_Name = ?" + ",Department = ?,Date_of_Resumption=?,Section_Unit = ?" + ",Exit_Type = ?,Exit_Reason=?,User_id=?" + " WHERE Staff_ID=?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            System.out.println("Resignation  enter 2");
            ps.setString(1, resign.getStaffname());
            System.out.println("Resignation  enter 3" + resign.getStaffname());
            ps.setString(2, resign.getDepartment());
            System.out.println("Resignation  enter 4" + resign.getDepartment());
            ps.setString(3, resign.getDate_of_resumption());
            System.out.println("Resignation  enter 5" + resign.getDate_of_resumption());
            ps.setString(4, resign.getSection_unit());
            System.out.println("Resignation  enter 6" + resign.getSection_unit());
            ps.setString(5, resign.getExit_type());
            System.out.println("Resignation  enter 7" + resign.getExit_type());
            ps.setString(6, resign.getExit_reason());
            System.out.println("Resignation  enter 8" + resign.getExit_reason());
            ps.setInt(7, resign.getUserId());
            System.out.println("Resignation  enter 9" + resign.getUserId());
            ps.setInt(8, resign.getStaffId());
            System.out.println("Resignation  enter 10" + resign.getStaffId());

            int res = ps.executeUpdate();

            System.out.println("SUCCESSFUL UPDATED 55" + res);
            if (res > 0) {
                result = "SUCCESS";
                System.out.println("SUCCESSFUL UPDATED 2");
            } else {
                result = "FAILED";
                System.out.println("SUCCESSFUL UPDATED 3");
            }

            System.out.println(query);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return result;

    }

    public String createLeave(Leave leave) {

        Connection con = null;
        PreparedStatement ps = null;
        String result = null;
        String query = "INSERT INTO am_ad_leave(Staff_Name,Department" + "  ,Section_Unit,Last_Leave_Date,Effective_Date,Leave_Days,User_id,Create_date)" + "   VALUES (?,?,?,?,?,?,?,?)";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, leave.getStaffname());
            ps.setString(2, leave.getDepartment());
            ps.setString(3, leave.getSection_unit());
            ps.setString(4, leave.getLast_Leave_Date());
            ps.setString(5, leave.getEffective_Date());
            ps.setString(6, leave.getLeave_Days());
            ps.setInt(7, leave.getUserId());
            ps.setDate(8, df.dateConvert(new java.util.Date()));

            int res = ps.executeUpdate();
            if (res > 0) {
                result = "SUCCESS";
            } else {
                result = "FAILED";
            }

        } catch (Exception e) {
            System.out.println("WARNING:Error creating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return result;

    }

    public java.util.ArrayList getLeaveList() {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Leave resign = null;
        String query = "Select * from am_ad_leave";
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                int staffid = rs.getInt("Staff_ID");
                String staffname = rs.getString("Staff_Name");
                String department = rs.getString("Department");
                String Section_Unit = rs.getString("Section_Unit");
                String Last_Leave_Date = rs.getString("Last_Leave_Date");
                String Effective_Date = rs.getString("Effective_Date");
                String Leave_Days = rs.getString("Leave_Days");
                int User_id = rs.getInt("User_id");


                resign = new Leave();
                resign.setStaffId(staffid);
                resign.setStaffname(staffname);
                resign.setDepartment(department);
                resign.setSection_unit(Section_Unit);
                resign.setLast_Leave_Date(Last_Leave_Date);
                resign.setEffective_Date(Effective_Date);
                resign.setLeave_Days(Leave_Days);
                resign.setUserId(User_id);
                _list.add(resign);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting Resignation ->" + e.getMessage());
            System.out.print("Error viewing Resigination!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public java.util.ArrayList getLeaveList(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.Leave resign = null;
        String query = "Select * from am_ad_leave" + filter;
        System.out.println("" + query);


        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                int staffid = rs.getInt("Staff_ID");
                String staffname = rs.getString("Staff_Name");
                String department = rs.getString("Department");
                String Section_Unit = rs.getString("Section_Unit");
                String Last_Leave_Date = rs.getString("Last_Leave_Date");
                String Effective_Date = rs.getString("Effective_Date");
                String Leave_Days = rs.getString("Leave_Days");
                int User_id = rs.getInt("User_id");


                resign = new Leave();
                resign.setStaffId(staffid);
                resign.setStaffname(staffname);
                resign.setDepartment(department);
                resign.setSection_unit(Section_Unit);
                resign.setLast_Leave_Date(Last_Leave_Date);
                resign.setEffective_Date(Effective_Date);
                resign.setLeave_Days(Leave_Days);
                resign.setUserId(User_id);
                _list.add(resign);

            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error Selecting Resignation ->" + e.getMessage());
            System.out.print("Error viewing Resigination!");
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    public String updateLeave(Leave resign) {
        System.out.println("Resignation  enter 1");
        Connection con = null;
        PreparedStatement ps = null;
        String result = null;



        String query = "UPDATE am_ad_leave SET Staff_Name = ?" + ",Department = ?,Section_Unit=?,Last_Leave_Date = ?" + ",Effective_Date = ?,Leave_Days=?,User_id=?" + " WHERE Staff_ID=?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            System.out.println("Resignation  enter 2");
            ps.setString(1, resign.getStaffname());
            System.out.println("Resignation  enter 3" + resign.getStaffname());
            ps.setString(2, resign.getDepartment());
            System.out.println("Resignation  enter 4" + resign.getDepartment());
            ps.setString(3, resign.getSection_unit());
            System.out.println("Resignation  enter 5" + resign.getSection_unit());
            ps.setString(4, resign.getSection_unit());
            System.out.println("Resignation  enter 6" + resign.getSection_unit());
            ps.setString(5, resign.getLast_Leave_Date());
            System.out.println("Resignation  enter 7" + resign.getLast_Leave_Date());
            ps.setString(6, resign.getEffective_Date());
            System.out.println("Resignation  enter 8" + resign.getEffective_Date());
            ps.setInt(7, resign.getUserId());
            System.out.println("Resignation  enter 9" + resign.getUserId());
            ps.setInt(8, resign.getStaffId());
            System.out.println("Resignation  enter 10" + resign.getStaffId());



            int res = ps.executeUpdate();

            System.out.println("SUCCESSFUL UPDATED 55" + res);
            if (res > 0) {
                result = "SUCCESS";
                System.out.println("SUCCESSFUL UPDATED 2");
            } else {
                result = "FAILED";
                System.out.println("SUCCESSFUL UPDATED 3");
            }

            System.out.println(query);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return result;

    }

    public boolean updateClassPrivilege1(java.util.ArrayList<ClassPrivilege> list, int userid, String branchCode, int loginId, String eff_date) {
        /**
         * edited by Joshua
         */
        for (int i = 0; i < list.size(); i++) {
            System.out.println();
            ClassPrivilege cp = (ClassPrivilege) list.get(i);

            updateClassPrivilege1(cp, userid, branchCode, loginId, eff_date);

        }

        return true;
    }

    public void updateClassPrivilege1(com.magbel.admin.objects.ClassPrivilege cp, int userid, String branchCode, int loginId, String eff_date) {
        AuditTrailGen atg1 = new AuditTrailGen();


        String query = "UPDATE am_ad_class_privileges " +
                "SET role_view = ?,role_addn = ?,role_edit = ?" + " WHERE clss_uuid=? AND Role_uuid=? ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int[] d = null;
        try {

            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1, cp.getRole_view());
            ps.setString(2, cp.getRole_addn());
            ps.setString(3, cp.getRole_edit());
            ps.setString(4, cp.getClss_uuid());
            ps.setString(5, cp.getRole_uuid());



        } catch (Exception ex) {
            System.out.println("WARN: Error doing updateClassPrivilege ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
    }

    public void compareAuditValues(String gl_prefix, String gl_suffix, String branch_id, String dept_code, String user_id, String branchCode, int loginId, String eff_date) {
        AuditTrailGen atg = new AuditTrailGen();
        String gl_prefix1 = "";
        String gl_suffix1 = "";

        String query = "SELECT gl_prefix,gl_suffix FROM sbu_branch_dept WHERE  branchId=? AND deptCode=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(branch_id));
            ps.setInt(2, Integer.parseInt(dept_code));

            rs = ps.executeQuery();

            while (rs.next()) {

                gl_prefix1 = rs.getString("gl_prefix");

                gl_suffix1 = rs.getString("gl_suffix");

            }

            if (!gl_prefix1.equalsIgnoreCase(gl_prefix)) {
                atg.logAuditTrailSecurityComp_Dept("sbu_branch_dept", branchCode, loginId, eff_date, dept_code, gl_prefix1, gl_prefix, "gl_prefix");
            }
            if (!gl_suffix1.equalsIgnoreCase(gl_suffix)) {
                atg.logAuditTrailSecurityComp_Dept("sbu_branch_dept", branchCode, loginId, eff_date, dept_code, gl_suffix1, gl_suffix, "gl_suffix");
            }


        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->" + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        //return UserFullName;
    }

    public void compareAuditValuesSbu(String gl_prefix, String gl_suffix, String attach_id, String sbu_code, String user_id, String branchCode, int loginId, String eff_date) {


        AuditTrailGen atg = new AuditTrailGen();
        String gl_prefix1 = "";
        String gl_suffix1 = "";

        String query = "SELECT GL_PREFIX,GL_SUFIX FROM AM_SBU_ATTACHEMENT WHERE  ATTACH_ID=? AND SBU_CODE=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean done = false;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(attach_id));
            ps.setString(2, sbu_code);

            rs = ps.executeQuery();

            while (rs.next()) {

                gl_prefix1 = rs.getString("GL_PREFIX");
                gl_suffix1 = rs.getString("GL_SUFIX");

            }

            if (gl_prefix1.equalsIgnoreCase(gl_prefix)) {
                atg.logAuditTrailSecurityComp_Sbu("AM_SBU_ATTACHEMENT", branchCode, loginId, eff_date, sbu_code, gl_prefix1, gl_prefix, "GL_PREFIX");
            }
            if (gl_suffix1.equalsIgnoreCase(gl_suffix)) {
                atg.logAuditTrailSecurityComp_Sbu("AM_SBU_ATTACHEMENT", branchCode, loginId, eff_date, sbu_code, gl_suffix1, gl_suffix, "GL_SUFFIX");
            }


        } catch (Exception e) {
            System.out.println("WARNING:Error executing Query User Full NamecompareAuditValues: ->" + e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }

    }

    public boolean updateDeptForBranch(java.util.ArrayList list, int userid, String branchCode, int loginId, String eff_date) {
        String query = "UPDATE sbu_branch_dept SET branchCode = ?" + ",deptCode = ?,gl_prefix = ?,gl_suffix = ?" + " WHERE branchId=?" + " AND deptId=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        com.magbel.admin.objects.BranchDept bd = null;
        int[] d = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);

            for (int i = 0; i < list.size(); i++) {
                bd = (com.magbel.admin.objects.BranchDept) list.get(i);
                ps.setString(1, bd.getBranchCode());
                ps.setString(2, bd.getDeptCode());
                ps.setString(3, bd.getGl_prefix());
                ps.setString(4, bd.getGl_suffix());
                ps.setString(5, bd.getBranchId());
                ps.setString(6, bd.getDeptId());

                ps.addBatch();

                //compareAuditValues(cp.getRole_view(),cp.getRole_addn(),cp.getRole_edit(),cp.getClss_uuid(),cp.getRole_uuid(),String.valueOf(userid),branchCode,loginId,eff_date);
                compareAuditValues(bd.getGl_prefix(), bd.getGl_suffix(), bd.getBranchId(), bd.getDeptCode(), String.valueOf(userid), branchCode, loginId, eff_date);
            }
            d = ps.executeBatch();

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching all asset ->" + ex);
        } finally {
            closeConnection(con, ps);
        }

        return (d.length > 0);
    }

    public void SaveLoginAudit(String userid, String branchcode, String workstname, String ip, String sessionid) {

        boolean done = false;
        Connection con = null;
        Statement s = null;
        ResultSet r = null;
        PreparedStatement ps = null;
        DatetimeFormat format = new DatetimeFormat();

        String query = "INSERT INTO gb_user_login" + "(create_date, user_id" + ",branch_code,mtid,time_in,workstation_name,System_ip,session_id)" + " VALUES (?,?,?,?,?,?,?,?)";
        try {
            apph = new ApplicationHelper();
            String stringid = apph.getGeneratedId("gb_user_login");

            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setDate(1, df.dateConvert(new java.util.Date()));
            ps.setString(2, userid);
            ps.setString(3, branchcode);
            ps.setString(4, stringid);
            //ps.setString(5,String.valueOf(df.dateConvert(new java.util.Date()).getHours())+df.dateConvert(new java.util.Date()).getMinutes()+df.dateConvert(new java.util.Date()).getSeconds());
            ps.setString(5, df.getDateTime().substring(10));
            ps.setString(6, workstname);
            ps.setString(7, ip);
            ps.setString(8, sessionid);
            done = (ps.executeUpdate() != -1);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(con, ps);
        }					//return done

    }

    public void updateLoginAudit(String userid) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE gb_user_login SET time_out = ? WHERE user_id =?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, df.getDateTime().substring(10));
            ps.setString(2, userid);
            done = (ps.executeUpdate() != -1);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating ab_user_login timeout ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
    }

    public boolean createState(com.magbel.admin.objects.State state) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO am_gb_states(state_code,state_name" + "  ,state_status,user_id,create_date,state_id)" + "   VALUES (?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, state.getStateCode());
            ps.setString(2, state.getStateName());
            ps.setString(3, state.getStateStatus());
            ps.setString(4, state.getUserId());
            ps.setDate(5, df.dateConvert(new java.util.Date()));
            ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_states")));
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error creating State ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }

    public String getPrivilegesRole(String role_name) {


        String query = "Select role_uuid from  am_ad_privileges where role_name=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String role_uuid = null;
        //System.out.println("==== getPrivilegesRole role_name==== "+role_name);
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, role_name);
            rs = ps.executeQuery();

            while (rs.next()) {
                role_uuid = rs.getString("role_uuid");
            }
        } catch (Exception ex) {
            System.out.println("WARN: Error fetching role_uuid  getPrivilegesRole ->" + ex);
        } finally {
            closeConnection(con, ps);
        }
        return role_uuid;

    }

    private Branch getABranch2(String filter) {
        Branch branch;
        branch = null;

        String query = "SELECT BRANCH_ID,BRANCH_CODE ,BRANCH_NAME,BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRES" +
                "S,STATE,PHONE_NO,FAX_NO,REGION,PROVINCE,BRANCH_STATUS,USER_ID,GL_SUFFIX,CREATE_D" +
                "ATE,EMAIL FROM am_ad_branch ";
        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;
        try {
            c = getConnection();
            s = c.createStatement();
            for (rs = s.executeQuery(query); rs.next();) {
                String branchId = rs.getString("BRANCH_ID");
                String branchCode = rs.getString("BRANCH_CODE");
                String branchName = rs.getString("BRANCH_NAME");
                String branchAcronym = rs.getString("BRANCH_ACRONYM");
                String glPrefix = rs.getString("GL_PREFIX");
                String branchAddress = rs.getString("BRANCH_ADDRESS");
                String state = rs.getString("STATE");
                String phoneNo = rs.getString("PHONE_NO");
                String faxNo = rs.getString("FAX_NO");
                String province = rs.getString("PROVINCE");
                String region = rs.getString("REGION");
                String branchStatus = rs.getString("BRANCH_STATUS");
                String username = rs.getString("USER_ID");
                String glSuffix = rs.getString("GL_SUFFIX");
                String createDate = rs.getString("CREATE_DATE");
                String emailAddress = rs.getString("EMAIL");


                //int location = rs.getInt("LOCATION");
                branch = new Branch(branchId, branchCode, branchName, branchAcronym, glPrefix, glSuffix, branchAddress, state, phoneNo, faxNo, region, province, branchStatus, username, createDate, emailAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return branch;
    }

    public com.magbel.admin.objects.Branch getBranchByBranchID2(String branchid) {
        String filter = " WHERE BRANCH_ID = " + branchid;
        com.magbel.admin.objects.Branch branch = getABranch2(filter);
        return branch;

    }

    private com.magbel.admin.objects.Category getACategory2(String filter) {
        com.magbel.admin.objects.Category category = null;
        String query = "SELECT category_ID,category_code,category_name" + ",category_acronym,Required_for_fleet" + ",Category_Class ,PM_Cycle_Period,mileage" + ",Notify_Maint_Days ,notify_every_days,residual_value" + ",Dep_rate ,Asset_Ledger,Dep_ledger" + ",Accum_Dep_ledger ,gl_account,insurance_acct" + ",license_ledger ,fuel_ledger,accident_ledger" + ",Category_Status ,user_id,create_date" + ",acct_type ,currency_Id,enforceBarcode,category_type" + " FROM am_ad_category ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String categoryId = rs.getString("category_ID");
                String categoryCode = rs.getString("category_code"); 
                String categoryName = rs.getString("category_name");
                String categoryAcronym = rs.getString("category_acronym");
                String requiredforFleet = rs.getString("Required_for_fleet");
                String categoryClass = rs.getString("Category_Class");
                String pmCyclePeriod = rs.getString("PM_Cycle_Period");
                String mileage = rs.getString("mileage");
                String notifyMaintdays = rs.getString("Notify_Maint_Days");
                String notifyEveryDays = rs.getString("notify_every_days");
                String residualValue = rs.getString("residual_value");
                String depRate = rs.getString("Dep_rate");
                String assetLedger = rs.getString("Asset_Ledger");
                String depLedger = rs.getString("Dep_ledger");
                String accumDepLedger = rs.getString("Accum_Dep_ledger");
                String glAccount = rs.getString("gl_account");
                String insuranceAcct = rs.getString("insurance_acct");
                String licenseLedger = rs.getString("license_ledger");
                String fuelLedger = rs.getString("fuel_ledger");
                String accidentLedger = rs.getString("accident_ledger");
                String categoryStatus = rs.getString("Category_Status");
                String userId = rs.getString("user_id");
                String createDate = sdf.format(rs.getDate("create_date"));
                String acctType = rs.getString("acct_type");
                String currencyId = rs.getString("currency_Id");
                String enforeBarcode = rs.getString("enforceBarcode");
                String categorytype = rs.getString("category_type");
                category = new com.magbel.admin.objects.Category(categoryId,
                        categoryCode, categoryName, categoryAcronym,
                        requiredforFleet, categoryClass, pmCyclePeriod,
                        mileage, notifyMaintdays, notifyEveryDays,
                        residualValue, depRate, assetLedger, depLedger,
                        accumDepLedger, glAccount, insuranceAcct,
                        licenseLedger, fuelLedger, accidentLedger,
                        categoryStatus, userId, createDate, acctType,
                        currencyId, enforeBarcode, categorytype);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return category;

    }

    public com.magbel.admin.objects.Category getCategoryByID2(String categoryid) {
        String filter = " WHERE category_ID=" + categoryid;
        com.magbel.admin.objects.Category category = getACategory2(filter);
        return category;

    }



    public boolean createComplaintCategory(com.magbel.admin.objects.ComplaintCategory comCat) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "INSERT INTO HD_COMPLAIN_CATEGORY(category_code,CATEGORY_DESCRIPTION,status,user_id,create_date,complain_id)" + "   VALUES (?,?,?,?,?,?)";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, comCat.getcomplaintCode());
            ps.setString(2, comCat.getcomplaintName());
            ps.setString(3, comCat.getcomplaintStatus());
            ps.setString(4, comCat.getUserId());
            ps.setDate(5, df.dateConvert(new java.util.Date()));
            ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("HD_COMPLAIN_CATEGORY")));
            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println("WARNING:Error creating Complaint Category ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }



      public java.util.List getComplaintCategoryByQuery(String filter) {
        java.util.List _list = new java.util.ArrayList();
        com.magbel.admin.objects.ComplaintCategory comCat = null;
        String query = "SELECT complain_id,category_code,CATEGORY_DESCRIPTION ,status,user_id,create_date" + " FROM HD_COMPLAIN_CATEGORY ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String complainId = rs.getString("complain_id");
                String complainCode = rs.getString("category_code");
                String complainName = rs.getString("CATEGORY_DESCRIPTION");
                String complainStatus = rs.getString("status");
                String userId = rs.getString("user_id");
                String createDate = rs.getString("create_date");
                comCat = new com.magbel.admin.objects.ComplaintCategory(complainId, complainCode,
                        complainName, complainStatus, userId, createDate);

                _list.add(comCat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return _list;

    }

    private com.magbel.admin.objects.ComplaintCategory getComplaint(String filter) {
        java.util.ArrayList _list = new java.util.ArrayList();
        com.magbel.admin.objects.ComplaintCategory comCat = null;
        String query = "SELECT complain_id,category_code,CATEGORY_DESCRIPTION ,status,user_id,create_date" + " FROM HD_COMPLAIN_CATEGORY ";

        query = query + filter;
        Connection c = null;
        ResultSet rs = null;
        Statement s = null;

        try {
            c = getConnection();
            s = c.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                String complainId = rs.getString("complain_id");
                String complainCode = rs.getString("category_code");
                String complainName = rs.getString("CATEGORY_DESCRIPTION");
                String complainStatus = rs.getString("status");
                String userId = rs.getString("user_id");
                String createDate = rs.getString("create_date");
                comCat = new com.magbel.admin.objects.ComplaintCategory(complainId, complainCode,
                        complainName, complainStatus, userId, createDate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(c, s, rs);
        }
        return comCat;

    }  

    public com.magbel.admin.objects.ComplaintCategory getComplaintCategoryByID(String comid) {
        String filter = " WHERE complain_id =" + comid;
        com.magbel.admin.objects.ComplaintCategory complaint = getComplaint(filter);
        return complaint;
    }

        public com.magbel.admin.objects.ComplaintCategory getCategoryCodeByCode(String catCode) {
        String filter = " WHERE category_code='" + catCode + "'";
        com.magbel.admin.objects.ComplaintCategory comCategory = getComplaint(filter);
        return comCategory;

    }

        public boolean updateComplaintCategory(com.magbel.admin.objects.ComplaintCategory comCat) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean done = false;
        String query = "UPDATE HD_COMPLAIN_CATEGORY SET category_code = ?,CATEGORY_DESCRIPTION = ? ,status = ?" + "   WHERE complain_id=?";

        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, comCat.getcomplaintCode());
            ps.setString(2, comCat.getcomplaintName());
            ps.setString(3, comCat.getcomplaintStatus());
            ps.setString(4, comCat.getcomplaintId());

            done = (ps.executeUpdate() != -1);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " ERROR:Error Updating Complaint Category ->" + e.getMessage());
        } finally {
            closeConnection(con, ps);
        }
        return done;

    }


        public java.util.List getSlaByQuery(String filter) {
            java.util.List _list = new java.util.ArrayList();
            com.magbel.admin.objects.Sla sla = null;
            String query = "SELECT sla_ID,sla_name ,sla_description,Dept_Code,user_id,create_date,STATUS FROM am_gb_sla ";

            query = query + filter;
            Connection c = null;
            ResultSet rs = null;
            Statement s = null;

            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query); 
                while (rs.next()) {
                    String sla_ID = rs.getString("sla_ID");
                    String sla_name = rs.getString("sla_name");
                    String sla_description = rs.getString("sla_description");
                    String DeptCode = rs.getString("Dept_Code");
                   // String CatCode = rs.getString("Category_Code");
                    String userId = rs.getString("user_id");
                    String createDate = rs.getString("create_date");
                    String Status = rs.getString("STATUS");
                    sla = new com.magbel.admin.objects.Sla(sla_ID,DeptCode, sla_name,
                    		sla_description, userId, createDate,Status);

                    _list.add(sla);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }

        public com.magbel.admin.objects.Sla getSlaByID(String slaId) {
            String filter = " WHERE sla_ID= " + slaId;
            com.magbel.admin.objects.Sla sla = getSla(filter);
            return sla;

        }

        private com.magbel.admin.objects.Sla getSla(String filter) {
            java.util.ArrayList _list = new java.util.ArrayList();
            com.magbel.admin.objects.Sla sla = null;
            String query = "SELECT sla_ID,sla_name ,sla_description,Dept_Code,user_id,create_date,STATUS FROM am_gb_sla ";

            query = query + filter;
            Connection c = null;
            ResultSet rs = null;
            Statement s = null;  

            try {
                c = getConnection(); 
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                    String sla_ID = rs.getString("sla_ID");
                    String sla_name = rs.getString("sla_name");
                    String sla_description = rs.getString("sla_description");
                    String DeptCode = rs.getString("Dept_Code");
             //       String CatCode = rs.getString("Category_Code");
                    String userId = rs.getString("user_id");
                    String createDate = rs.getString("create_date");
                    String Status = rs.getString("STATUS");
                    sla = new com.magbel.admin.objects.Sla(sla_ID,DeptCode,  sla_name,
                    		sla_description, userId, createDate,Status);

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return sla;

        }

        public com.magbel.admin.objects.Sla getSlaById(String slaId) {
            String filter = " WHERE sla_ID='" + slaId + "'";
            com.magbel.admin.objects.Sla sla = getSla(filter);
            return sla;

        }

        public boolean createSla(com.magbel.admin.objects.Sla sla) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "INSERT INTO am_gb_sla(sla_ID,sla_name ,sla_description,user_id,create_date,Dept_Code,Category_Code)" + "   VALUES (?,?,?,?,?,?,?)";
System.out.println("I am in createSla "+sla.getSla_ID());
            try {
                con = getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, sla.getSla_ID());
                //ps.setString(1, new ApplicationHelper().getGeneratedId("am_gb_states"));
                ps.setString(2, sla.getSla_name());
                ps.setString(3, sla.getSla_description());
                ps.setString(4, sla.getUserId());
                ps.setDate(5, df.dateConvert(new java.util.Date()));
                ps.setString(6, sla.getDeptCode());
                ps.setString(7, sla.getCatCode());
               // ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_states")));
                done = (ps.executeUpdate() != -1);
 
            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error creating Sla ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }

        public boolean updateSla(com.magbel.admin.objects.Sla sla) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "UPDATE am_gb_sla SET sla_name = ?   ,sla_description = ?  ,Dept_code = ?, Status = ? WHERE sla_ID=?";

            try {
            	//System.out.println("UUUIUUUUU sla.getPriority UUUUU>>>"+sla.getPriority());
                con = getConnection();
                ps = con.prepareStatement(query);
             //   ps.setString(1, sla.getSla_ID());
                ps.setString(1, sla.getSla_name());
                ps.setString(2, sla.getSla_description());
                ps.setString(3, sla.getDeptCode());
                ps.setString(4, sla.getStatus()); 
                ps.setString(5, sla.getSla_ID());
 
                done = (ps.executeUpdate() != -1);
 
            } catch (Exception e) {
                System.out.println(this.getClass().getName() + " ERROR:Error Updating Sla ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }
  public java.util.List getCategoryByQuery2(String filter) { 
            java.util.List _list = new java.util.ArrayList();
            com.magbel.admin.objects.Category_Definition category = null;
            String query = "SELECT Dept_code,Dept_name,Dept_name,Dept_Status,user_id,create_date FROM AM_AD_DEPARTMENT  ";
            
            query = query + filter;
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;

            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                    String categoryId = rs.getString("Dept_code");
                    String categoryName = rs.getString("Dept_name");
                    String categoryDesc = rs.getString("Dept_name");
                    String status = rs.getString("Dept_Status");
                    String user_id = rs.getString("user_id");
                    String create_date = rs.getString("create_date");
                   // String tech_id = rs.getString("tech_id");
                    
                    category = new com.magbel.admin.objects.Category_Definition(categoryId,
                             categoryName, categoryDesc,status,user_id, create_date);
                    _list.add(category);
                    
                    
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }
        public boolean createCategory(com.magbel.admin.objects.Category_Definition category,String userId) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "INSERT INTO HD_COMPLAIN_CATEGORY(CATEGORY_CODE,CATEGORY_NAME ,CATEGORY_DESCRIPTION,user_id,create_date,status)" + "   VALUES (?,?,?,?,?,?)";
            
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
                ps.setString(1, new ApplicationHelper().getGeneratedId("Category_Definition"));
                ps.setString(2, category.getCATEGORY_NAME());
                ps.setString(3, category.getCATEGORY_DESCRIPTION());
                ps.setString(4, userId);
                ps.setDate(5, df.dateConvert(new java.util.Date()));
                ps.setString(6, category.getstatus());
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error creating Category_Definition ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }
        
        public boolean createSubCategory(com.magbel.admin.objects.Category_Sub_Definition category,String userId) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "INSERT INTO HD_COMPLAIN_SUBCATEGORY(SUB_CATEGORY_CODE,CATEGORY_CODE,SUB_CATEGORY_NAME ,SUB_CATEGORY_DESC,user_id,create_date,status)   VALUES (?,?,?,?,?,?,?)";
 //           System.out.println("==== createSubCategory===== "+query);
 //           System.out.println("====category====> "+category);
 //           System.out.println("====userId====> "+userId);
            
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
                ps.setString(1, new ApplicationHelper().getGeneratedId("HD_COMPLAIN_SUBCATEGORY"));
                ps.setString(2, category.getCATEGORY_ID());
                ps.setString(3, category.getSUB_CATEGORY_NAME());
                ps.setString(4, category.getSUB_CATEGORY_DESCRIPTION());
                ps.setString(5, userId);
                ps.setDate(6, df.dateConvert(new java.util.Date()));
                ps.setString(7, category.getStatus());
                done = (ps.executeUpdate() != -1);
              //  System.out.println("==== HD_COMPLAIN_SUBCATEGORY query===== "+query);
            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error creating Sub_Category_Definition ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        } 
        public java.util.List getSubCategoryByQuery2(String filter) { 
            java.util.List _list = new java.util.ArrayList();
            com.magbel.admin.objects.Category_Sub_Definition category = null;
            String query = "SELECT SUB_CATEGORY_CODE,CATEGORY_CODE,SUB_CATEGORY_NAME,SUB_CATEGORY_DESC,STATUS,user_id,create_date FROM HD_COMPLAIN_SUBCATEGORY  ";
                        
            query = query + filter;
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;
           // System.out.println("==== getSubCategoryByQuery2===== "+query);
            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                	 String subcategoryId = rs.getString("SUB_CATEGORY_CODE");
                    String categoryId = rs.getString("CATEGORY_CODE");
                    String name = rs.getString("SUB_CATEGORY_NAME");
                    String desc = rs.getString("SUB_CATEGORY_DESC");
                    String status = rs.getString("STATUS");
                    String user_id = rs.getString("user_id");
                    String create_date = rs.getString("create_date");
                    //String tech_id = rs.getString("tech_id");
             
                    category = new com.magbel.admin.objects.Category_Sub_Definition
                    (subcategoryId,categoryId, name, desc,status,user_id, create_date);
                    _list.add(category);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }
        public boolean updateCategory(com.magbel.admin.objects.Category_Definition category,String userId) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "update HD_COMPLAIN_CATEGORY set CATEGORY_NAME=? ,CATEGORY_DESCRIPTION=?,create_date=?,STATUS=? WHERE CATEGORY_CODE=? ";
            
            try { 
                con = getConnection();
                ps = con.prepareStatement(query); 
                ps.setString(1, category.getCATEGORY_NAME());
                ps.setString(2, category.getCATEGORY_DESCRIPTION());
               // ps.setString(3, userId);
                ps.setDate(3, df.dateConvert(new java.util.Date()));
                ps.setString(4, category.getstatus());
                ps.setString(5, category.getCATEGORY_ID());
   //             System.out.println("====category===== "+category.getstatus());
   //             System.out.println("====query===== "+query);
            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error UPDATING Category_Definition ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }
        public boolean createItemCategory(com.magbel.admin.objects.Item_Definition item,String userId) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "INSERT INTO Item_Definition(ITEM_ID,SUB_CATEGORY_ID,NAME,DESCRIPTION,IS_DELETED,USER_ID,CREATE_DATE,TECH_ID)   VALUES (?,?,?,?,?,?,?,?)";

            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
                ps.setString(1, new ApplicationHelper().getGeneratedId("Item_Definition"));
                ps.setString(2, item.getSUB_CATEGORY_ID());
                ps.setString(3, item.getNAME());
                ps.setString(4, item.getDESCRIPTION());
                ps.setString(5, item.getIS_DELETED());
                ps.setString(6, userId);
                ps.setDate(7, df.dateConvert(new java.util.Date()));
                ps.setString(8, item.getTech_id());
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error creating Item_Definition ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        } 
        public java.util.List getItemByQuery2(String filter) { 
            java.util.List _list = new java.util.ArrayList();
            com.magbel.admin.objects.Item_Definition item = null;
            String query = "SELECT ITEM_ID,SUB_CATEGORY_ID,NAME,DESCRIPTION,IS_DELETED,USER_ID,CREATE_DATE,TECH_ID FROM Item_Definition  ";

            query = query + filter;
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;

            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                	  String itemId = rs.getString("ITEM_ID");
                	 String subcategoryId = rs.getString("SUB_CATEGORY_ID");
                    String name = rs.getString("NAME");
                    String desc = rs.getString("DESCRIPTION");
                    String categoryIsDeleted = rs.getString("IS_DELETED");
                    String user_id = rs.getString("user_id");
                    String create_date = rs.getString("create_date");
                    String tech_id = rs.getString("tech_id");
                    
                    item = new com.magbel.admin.objects.Item_Definition
                    (itemId,subcategoryId, name, desc,categoryIsDeleted,tech_id,user_id, create_date);
                    _list.add(item);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }
        
        public java.util.ArrayList getRecords(String column,String table,String DeptCode) { 
            java.util.ArrayList _list = new java.util.ArrayList();
            com.magbel.admin.objects.TableColumn tc = null;
            String query = "SELECT "+column+" FROM "+table+"  ";
            //String query = "SELECT "+column+" FROM "+table+" WHERE category_code = '"+DeptCode+"' ";
   //         System.out.println("==== getRecords query ====== "+query);
            String columnvalue =""; 
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;

            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                	   columnvalue = rs.getString(1);  
                	    tc = new com.magbel.admin.objects.TableColumn();
                       tc.setColumnName(columnvalue);
                    _list.add(tc);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }
        public boolean createCriteriaRule(String slaId,String crt,String desc,String userId) {

            Connection con = null;
            PreparedStatement ps = null; 
            boolean done = false;
            String query = "INSERT INTO am_criteria_rules(criteria_ID,rules_NAME,criteria_DESCRIPTION, USER_ID,CREATE_DATE)   VALUES (?,?,?,?,?)";
            System.out.println("slaId>>>>> "+slaId+" =====desc== "+desc+" ===crt==== "+crt);
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
               String id=new ApplicationHelper().getGeneratedId("AM_GB_SLA");
                ps.setString(1, id);   
                ps.setString(2, crt);
                ps.setString(3, desc);
                ps.setString(4, userId); 
                ps.setDate(5, df.dateConvert(new java.util.Date())); 
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {e.printStackTrace();
                System.out.println("WARNING:Error creating am_criteria_rules ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }        
        public java.util.ArrayList getRulesRecords(String filter) { 
            java.util.ArrayList _list = new java.util.ArrayList();
            com.magbel.admin.objects.Rules trlc = null;
            String query = " SELECT * FROM am_criteria_rules  ";
            query=query + filter;
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;
            System.out.println("KKKKKqueryKKKKK>>> "+query);
            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                	 String  Id = rs.getString("criteria_ID"); 
                	 String name = rs.getString("rules_NAME"); 
                	 String desc = rs.getString("criteria_DESCRIPTION"); 
                	 String userId = rs.getString("USER_ID"); 
                	    
                	trlc = new com.magbel.admin.objects.Rules(Id,name,desc,userId); 
                    _list.add(trlc);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }  
        
        
         
        public boolean createResponse(com.magbel.admin.objects.RuleConstraints constrsint, String DeptCode, String CatCode, int sla_ID )
        {

            Connection con = null;
            PreparedStatement ps = null;
            String id = "";
            boolean done = false;
            String query = "INSERT INTO SLA_RESPONSE(criteria_ID,ITEM_NAME,RESPONSE_DAY," +
            		"RESPONSE_HOUR,RESPONSE_MINUTE,RESOLVE_DAY,RESOLVE_HOUR," +
            		"RESOLVE_MINUTE,CONSTRAINTS,DEPT_CODE,CAT_CODE)   VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
                id=new ApplicationHelper().getGeneratedId("SLA_RESPONSE");
            //    constrsint=new com.magbel.admin.objects.RuleConstraints();
            //    System.out.println("----------createResponse----------------->"+ constrsint.getCriteria_ID());
                ps.setInt(1, sla_ID); 
                ps.setString(2, constrsint.getNAME());
                ps.setString(3, constrsint.getRESPONSE_DAY());
                ps.setString(4, constrsint.getRESPONSE_HOUR());  
                ps.setString(5, constrsint.getRESPONSE_MINUTE()); 
                ps.setString(6, constrsint.getRESOLVE_DAY());
                ps.setString(7, constrsint.getRESOLVE_HOUR());
                ps.setString(8, constrsint.getRESOLVE_MINUTE());  
                ps.setString(9, (constrsint.getCONSTRAINT()==null)?"":constrsint.getCONSTRAINT());
                ps.setString(10, DeptCode);
                ps.setString(11, CatCode);
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
   //         	System.out.println("---------createResponse------------------>" );
            	e.printStackTrace();
                System.out.println("WARNING:Error creating SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }     
        public boolean createEscalate(com.magbel.admin.objects.RuleConstraints constrsint)
        {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "INSERT INTO SLA_ESCALATE(criteria_ID,NAME,RESPONSE_DAY," +
            		"RESPONSE_HOUR,RESPONSE_MINUTE,RESOLVE_DAY,RESOLVE_HOUR," +
            		"RESOLVE_MINUTE,CONSTRAINTS,NAME2)   VALUES (?,?,?,?,?,?,?,?,?,?)";

            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
              //  constrsint=new com.magbel.admin.objects.RuleConstraints();
              //  System.out.println("-----------createEscalate---------------->"+ constrsint.getCriteria_ID());
                ps.setString(1, constrsint.getCriteria_ID()); 
                ps.setString(2, constrsint.getNAME());
                ps.setString(3, constrsint.getRESPONSE_DAY());
                ps.setString(4, constrsint.getRESPONSE_HOUR());  
                ps.setString(5, constrsint.getRESPONSE_MINUTE()); 
                ps.setString(6, constrsint.getRESOLVE_DAY());
                ps.setString(7, constrsint.getRESOLVE_HOUR());
                ps.setString(8, constrsint.getRESOLVE_MINUTE());  
                ps.setString(9, constrsint.getCONSTRAINT()); 
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
            //	System.out.println("-----------createEscalate---------------->" );
            	e.printStackTrace();
                System.out.println("WARNING:Error creating SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }   
        
        public com.magbel.admin.objects.RuleConstraints getResponseByQuery(String filter) 
        { 
            com.magbel.admin.objects.RuleConstraints values = null;
            String query = "SELECT *  FROM SLA_RESPONSE";
            
            query = query + filter;
            Connection c = null;
            ResultSet rs = null;
            Statement s = null;            
            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                    String Id = rs.getString("criteria_ID");
                    String itemName = rs.getString("ITEM_NAME");
                    String resDay = rs.getString("RESPONSE_DAY");
                    String resHour = rs.getString("RESPONSE_HOUR");
                    String resMinute = rs.getString("RESPONSE_MINUTE");
                    String resovDay = rs.getString("RESOLVE_DAY");
                    String resovHour = rs.getString("RESOLVE_HOUR");
                    String resovMinute = rs.getString("RESOLVE_MINUTE");
                    String contraint = rs.getString("CONSTRAINTS"); 
                    values = new com.magbel.admin.objects.RuleConstraints(Id, itemName,
                    		resDay, resHour, resMinute, resovDay,
                    		resovHour, resovMinute, contraint );

                     
                }

            } catch (Exception e) {
                System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_RESPONSE ->" + e.getMessage());
            } finally {
                closeConnection(c, s, rs);
            }
            return values;

        }
        public com.magbel.admin.objects.RuleConstraints getEscalateByQuery(String filter) 
        { 
            com.magbel.admin.objects.RuleConstraints values = null;
            String query = "SELECT *  FROM SLA_ESCALATE";
            
            query = query + filter;
            Connection c = null;
            ResultSet rs = null;
            Statement s = null;

            try {
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                    String Id = rs.getString("criteria_ID");
                    String itemName = rs.getString("NAME");
                    String resDay = rs.getString("RESPONSE_DAY");
                    String resHour = rs.getString("RESPONSE_HOUR");
                    String resMinute = rs.getString("RESPONSE_MINUTE");
                    String resovDay = rs.getString("RESOLVE_DAY");
                    String resovHour = rs.getString("RESOLVE_HOUR");
                    String resovMinute = rs.getString("RESOLVE_MINUTE");
                    String contraint = rs.getString("CONSTRAINTS"); 
                    String itemName2= rs.getString("NAME2");
                    String contraint2= rs.getString("CONSTRAINTS2");
                    
                    values = new com.magbel.admin.objects.RuleConstraints(Id, itemName,
                    		resDay, resHour, resMinute, resovDay,
                    		resovHour, resovMinute, contraint );
                    values.setItemName2(itemName2);
                    values.setCONSTRAINT2(contraint2);
                     
                }

            } catch (Exception e) {
                System.out.println(this.getClass().getName() + " ERROR:Error Selecting SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(c, s, rs);
            }
            return values;

        }
        
        public boolean updateResponse(com.magbel.admin.objects.RuleConstraints values, String DeptCode, String CatCode) 
        {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "update SLA_RESPONSE set ITEM_NAME=? ,RESPONSE_DAY=?," +
            		"RESPONSE_HOUR=?,RESPONSE_MINUTE=?,RESOLVE_DAY=?,RESOLVE_HOUR=?," +
            		"RESOLVE_MINUTE=?,CONSTRAINTS=?  WHERE Dept_Code=? AND Cat_Code=? ";
           
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
           //     values=new com.magbel.admin.objects.RuleConstraints();
        //        System.out.println("----------updateResponse----------------->"+values.getCriteria_ID() );
        //        System.out.println("-----------updateResponse---------------->"+values.getRESPONSE_DAY() );
                ps.setString(1, values.getNAME());
                ps.setString(2, values.getRESPONSE_DAY());
                ps.setString(3, values.getRESPONSE_HOUR());
              
                ps.setString(4, values.getRESPONSE_MINUTE());
                ps.setString(5, values.getRESOLVE_DAY());
                
                ps.setString(6, values.getRESOLVE_HOUR());
                ps.setString(7, values.getRESOLVE_MINUTE());
              
                ps.setString(8, (values.getCONSTRAINT()==null)?"":values.getCONSTRAINT());
                
               // ps.setString(9, values.getCriteria_ID());
                ps.setString(9, DeptCode);
                ps.setString(10, CatCode);
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
            	//System.out.println("----------updateResponse----------------->" );
            	e.printStackTrace();
                System.out.println("WARNING:Error UPDATING SLA_RESPONSE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done; 

        }
        public boolean updateEscalate(com.magbel.admin.objects.RuleConstraints values) 
        {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String query = "update SLA_ESCALATE set NAME=? ,RESPONSE_DAY=?," +
            		"RESPONSE_HOUR=?,RESPONSE_MINUTE=?,RESOLVE_DAY=?,RESOLVE_HOUR=?," +
            		"RESOLVE_MINUTE=?,CONSTRAINTS=? WHERE criteria_ID=? ";
           
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
            //    values=new com.magbel.admin.objects.RuleConstraints();
        //        System.out.println("----------updateEscalate----------------->"+values.getCriteria_ID() );
        //        System.out.println("-------------updateEscalate-------------->"+values.getRESPONSE_DAY() );
        //        System.out.println("-----------updateEscalate---------------->"+ values.getNAME());
                ps.setString(1, values.getNAME());
                ps.setString(2, values.getRESPONSE_DAY());
                ps.setString(3, values.getRESPONSE_HOUR());
              
                ps.setString(4, values.getRESPONSE_MINUTE());
                ps.setString(5, values.getRESOLVE_DAY());
                
                ps.setString(6, values.getRESOLVE_HOUR());
                ps.setString(7, values.getRESOLVE_MINUTE());
              
                ps.setString(8,(values.getCONSTRAINT()==null)?"":values.getCONSTRAINT()); 
                ps.setString(9, values.getCriteria_ID());
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
            	 System.out.println("-----------updateEscalate---------------->" );
            	e.printStackTrace();
                System.out.println("WARNING:Error UPDATING SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }
    
        public java.util.ArrayList getUserRecords() { 
            java.util.ArrayList _list = new java.util.ArrayList();
            com.magbel.admin.objects.User tc = null;
            String query = "SELECT email,full_name FROM am_gb_user  ";
             
            Connection c = null;   
            ResultSet rs = null;
            Statement s = null;

            try { 
                c = getConnection();
                s = c.createStatement();
                rs = s.executeQuery(query);
                while (rs.next()) {
                	   String user_name = rs.getString(1);  
                	   String full_name = rs.getString(2);  
                	    tc = new com.magbel.admin.objects.User();
                       tc.setUserName(user_name);
                       tc.setUserFullName(full_name);
                    _list.add(tc);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection(c, s, rs);
            }
            return _list;

        }
        
        public String createSlaNew(com.magbel.admin.objects.Sla sla) {

            Connection con = null;
            PreparedStatement ps = null;
            boolean done = false;
            String id="";
            String query = "INSERT INTO am_gb_sla(sla_ID,sla_name ,sla_description,user_id,create_date,Dept_Code)" + "   VALUES (?,?,?,?,?,?)";

            try {
                con = getConnection();
                ps = con.prepareStatement(query);     
                ps.setString(1, sla.getSla_ID() );
                ps.setString(2, sla.getSla_name());
                ps.setString(3, sla.getSla_description());
                ps.setString(4, sla.getUserId());
                ps.setDate(5, df.dateConvert(new java.util.Date()));
                ps.setString(6, sla.getDeptCode());
                id=new ApplicationHelper().UpdateGeneratedId("AM_GB_SLA");
            //    ps.setString(7, sla.getCatCode());
               // ps.setLong(6, Integer.parseInt(new ApplicationHelper().getGeneratedId("am_gb_states")));
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
            	 
            	e.printStackTrace();
                System.out.println("WARNING:Error creating Sla ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            System.out.println("-----------out create-------id ->" + id);
            return id;

        }
        public boolean updateEscalate(com.magbel.admin.objects.RuleConstraints values,String name2, String DeptCode, String CatCode) 
        {

            Connection con = null;
            PreparedStatement ps = null;  
            boolean done = false;
            String query = "update SLA_ESCALATE set NAME=? ,RESPONSE_DAY=?," +
            		"RESPONSE_HOUR=?,RESPONSE_MINUTE=?,RESOLVE_DAY=?,RESOLVE_HOUR=?," +
            		"RESOLVE_MINUTE=?,CONSTRAINTS=?,NAME2=?,CONSTRAINTS2=? WHERE Dept_Code=? AND Cat_Code=? ";
           
            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
            //    values=new com.magbel.admin.objects.RuleConstraints();
//                System.out.println("-----------updateEscalate---------------->"+values.getCriteria_ID() );
//                System.out.println("---------------updateEscalate RESOLVE_DAY------------>"+values.getRESOLVE_DAY() );
//                System.out.println("---------------updateEscalate RESOLVE_HOUR------------>"+values.getRESOLVE_HOUR() );
//                System.out.println("--------updateEscalate values.getNAME()------>"+values.getNAME() );
//                System.out.println("--------updateEscalate name2------>"+name2 );
               // ps.setString(1, values.getNAME());  
                ps.setString(1, name2); 
                ps.setString(2, values.getRESPONSE_DAY());
                ps.setString(3, values.getRESPONSE_HOUR());
              
                ps.setString(4, values.getRESPONSE_MINUTE());
                ps.setString(5, values.getRESOLVE_DAY());
                
                ps.setString(6, values.getRESOLVE_HOUR());
                ps.setString(7, values.getRESOLVE_MINUTE());
              
                ps.setString(8,(values.getCONSTRAINT()==null)?"":values.getCONSTRAINT()); 
                
                ps.setString(9, name2);
                ps.setString(10,(values.getCONSTRAINT2()==null)?"":values.getCONSTRAINT2());  
              //  ps.setString(11, values.getCriteria_ID());
                ps.setString(11, DeptCode);
                ps.setString(12, CatCode);
                done = (ps.executeUpdate() != -1);

            } catch (Exception e) {
         //   	 System.out.println("------------updateEscalate--------------->" );
            	e.printStackTrace();
                System.out.println("WARNING:Error UPDATING SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        }

 public boolean createEscalate(com.magbel.admin.objects.RuleConstraints constrsint,String name2, String DeptCode, String CatCode, int sla_ID)
        {

            Connection con = null;
            PreparedStatement ps = null;
            String id = "";
            boolean done = false;
            String query = "INSERT INTO SLA_ESCALATE(criteria_ID,NAME,RESPONSE_DAY," +
            		"RESPONSE_HOUR,RESPONSE_MINUTE,RESOLVE_DAY,RESOLVE_HOUR," +
            		"RESOLVE_MINUTE,CONSTRAINTS,NAME2,CONSTRAINTS2,DEPT_CODE,CAT_CODE)   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                con = getConnection();
                ps = con.prepareStatement(query); 
                id=new ApplicationHelper().getGeneratedId("SLA_ESCALATE");
              //  constrsint=new com.magbel.admin.objects.RuleConstraints();
      //          System.out.println("======Criterai Id ====== "+ constrsint.getCriteria_ID());
                ps.setInt(1, sla_ID); 
                ps.setString(2, name2);  
                //ps.setString(2, constrsint.getNAME());
                ps.setString(3, constrsint.getRESPONSE_DAY());
                ps.setString(4, constrsint.getRESPONSE_HOUR());   
                ps.setString(5, constrsint.getRESPONSE_MINUTE()); 
                ps.setString(6, constrsint.getRESOLVE_DAY());
                ps.setString(7, constrsint.getRESOLVE_HOUR());
                ps.setString(8, constrsint.getRESOLVE_MINUTE());  
                ps.setString(9, (constrsint.getCONSTRAINT()==null)?"":constrsint.getCONSTRAINT()); 
                
                ps.setString(10, name2);
                ps.setString(11, (constrsint.getCONSTRAINT2()==null)?"":constrsint.getCONSTRAINT2());
                ps.setString(12, DeptCode);
                ps.setString(13, CatCode);
                done = (ps.executeUpdate() != -1);
 
            } catch (Exception e) {
            	System.out.println("--------------------------->" );
            	e.printStackTrace();
                System.out.println("WARNING:Error creating SLA_ESCALATE ->" + e.getMessage());
            } finally {
                closeConnection(con, ps);
            }
            return done;

        } 
 

 public com.magbel.admin.objects.Bank getBankByCode(String bankCode) {
 	String filter = " WHERE Org_Code='" + bankCode + "'";
 	com.magbel.admin.objects.Bank bank = getABank(filter);
 	return bank;

 } 

 private com.magbel.admin.objects.Bank getABank(String filter) {
	 com.magbel.admin.objects.Bank bank = null;
 	String query = "SELECT Org_Code,Org_Name,address,phone,email,user_id,Fax,Domain,Acronym,Status,CREATE_DATE,CONTACT_PERSON FROM AM_AD_ORGANIZATION ";

 	query = query + filter;
 	Connection c = null;
 	ResultSet rs = null;
 	Statement s = null;
//System.out.println("===getBankByCode query====> "+query);
 	try {
 		c = getConnection();
 		s = c.createStatement();
 		rs = s.executeQuery(query);
 		while (rs.next()) {
 			String bankCode = rs.getString("Org_Code");
 			String bankName = rs.getString("Org_Name");
 			String address = rs.getString("address");
 			String phone = rs.getString("phone");
 			String email = rs.getString("email");
 			String user_id = rs.getString("user_id");
 			String Fax = rs.getString("Fax");
 			String Domain = rs.getString("Domain");
 			String Acronym = rs.getString("Acronym");
 			String Status = rs.getString("Status");
 			Date createDate = rs.getDate("CREATE_DATE"); 
 			String organContact = rs.getString("CONTACT_PERSON"); 
 			bank = new com.magbel.admin.objects.Bank(bankCode, bankName,
 					address, phone, email, user_id, Fax,Domain,Acronym,Status, createDate,organContact);
 		}

 	} catch (Exception e) {
 		e.printStackTrace();
 	}

 	finally {
 		closeConnection(c, s, rs);
 	}
 	return bank;

 } 
 
 public java.util.List getBanksByQuery(String filter) {
		java.util.List _list = new java.util.ArrayList();
		com.magbel.admin.objects.Bank bank = null;
		String query = "SELECT Org_Code,Org_Name,address,phone,email,user_id,Fax,Domain,Acronym,Status,CREATE_DATE,CONTACT_PERSON FROM AM_AD_ORGANIZATION ";

		query = query + filter;
		Connection c = null;
		ResultSet rs = null;
		Statement s = null;

		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(query);
			while (rs.next()) {
				String bankCode = rs.getString("Org_Code");
				String bankName = rs.getString("Org_Name");
				String address = rs.getString("address");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String user_id = rs.getString("user_id");
	 			String Fax = rs.getString("Fax");
	 			String Domain = rs.getString("Domain");
	 			String Acronym = rs.getString("Acronym");
	 			String Status = rs.getString("Status");
				Date createDate = rs.getDate("CREATE_DATE");
				String organContact = rs.getString("CONTACT_PERSON");				
				bank = new com.magbel.admin.objects.Bank(bankCode, bankName,
						address, phone, email, user_id,  Fax,Domain,Acronym,Status,createDate,organContact);
				_list.add(bank);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			closeConnection(c, s, rs);
		}
		return _list;

	}
	public boolean createBank(com.magbel.admin.objects.Bank bank) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "INSERT INTO AM_AD_ORGANIZATION(Org_Code,Org_Name,address,phone,email,user_id,CREATE_DATE,mtid,Fax,Domain,Acronym,Status,CONTACT_PERSON)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			con = getConnection();  
			ps = con.prepareStatement(query);
			 
			ps.setString(1,bank.getBankCode());
			ps.setString(2,bank.getBankName());
			ps.setString(3,bank.getAddress());
			ps.setString(4, bank.getPhone());
			ps.setString(5, bank.getEmail());
			ps.setString(6, bank.getUser_id());
			ps.setDate(7, df.dateConvert(new java.util.Date()));			
			ps.setString(8,  new ApplicationHelper().getGeneratedId("AM_AD_ORGANIZATION"));
			ps.setString(9, bank.getFax());
			ps.setString(10, bank.getDomain());
			ps.setString(11, bank.getAcronym());
			ps.setString(12, bank.getStatus());
			ps.setString(13, bank.getorganContact());			
		done=( ps.executeUpdate()!=-1);
 
		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error creating AM_AD_ORGANIZATION ->" + e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

	public boolean updateBank(com.magbel.admin.objects.Bank bank) {

		Connection con = null;
		PreparedStatement ps = null;
		boolean done = false;
		String query = "UPDATE AM_AD_ORGANIZATION SET Org_Name=?,address=?,phone=?,email=?,Fax=?,Domain=?,Acronym=?,Status=?,CONTACT_PERSON=?  WHERE Org_Code=?";
		//System.out.println("=====updateBank query=====<> "+query);
		try { 
			con = getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, bank.getBankName());
			ps.setString(2, bank.getAddress());
			ps.setString(3, bank.getPhone());
			ps.setString(4, bank.getEmail());
			ps.setString(5, bank.getFax());
			ps.setString(6, bank.getDomain());
			ps.setString(7, bank.getAcronym());
			ps.setString(8, bank.getStatus());			
			ps.setString(9, bank.getorganContact());
			ps.setString(10, bank.getBankCode());
			done=( ps.executeUpdate()!=-1);

		} catch (Exception e) {
			System.out.println(this.getClass().getName()
					+ " ERROR:Error Updating Query ->" + e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return done;

	}

 
} 
