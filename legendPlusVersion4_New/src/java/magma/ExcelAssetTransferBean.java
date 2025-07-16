package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import legend.ConnectionClass;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.Asset;
import magma.util.Codes;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;
// Referenced classes of package magma:
//            DateManipulations

public class ExcelAssetTransferBean extends ConnectionClass
{
	public ApprovalRecords approve;
	public HtmlUtility htmlUtil;	
    private String branch_id;
    private String department_id;
    private String section_id;
    private String category_id;
    private String make;
    private String location;
    private String oldasset_id;
    private String newasset_id;
    private String asset_code;
    private String newbranch_id;
    private String newdepartment_id;
    private String newsection_id;
    private String newsbu_code;
    private String user;
    private String user_id;
    private String sbu_code;
    private String assetuser;
    private String newassetuser;
    private String cost_price = "";
    private String description = "";
    private String newasset_user = "";
    private String asset_user = "";
    private String oldbranch_code = "";
    private String olddepartment_code = "";
    private String oldsection_code = "";
    private String newbranch_code = "";
    private String newdepartment_code = "";
    private String newsection_code = "";    
    private String category_code = "";
    private DatetimeFormat dateFormat;
    private MagmaDBConnection dbConnection;
    SimpleDateFormat sdf;
    
    public ExcelAssetTransferBean()
        throws Exception
    {
    	oldasset_id = "0";
    	newasset_id = "0";
        branch_id = "0";
        department_id = "0";
        section_id = "0";
        category_id = "0";
        make = "0";
        location = "0";
        cost_price = "0";
        user = "";
        user_id = "0";
        newbranch_id = "0";
        sbu_code = "";
        assetuser = "";
        approve = new ApprovalRecords(); 
        htmlUtil = new HtmlUtility(); 
        dbConnection = new MagmaDBConnection();
    }

 
public void createGroupUpload(String asset_id)
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";
	
	newasset_id = asset_id;
StringBuffer b;
b = new StringBuffer(400);
Codes code = new Codes();

System.out.println("Inside createGroupUpload");
String query = "insert into am_gb_bulktransfer ( " +
        "ASSET_ID,Description,OLDBRANCH_ID," +
        "OLDDEPT_ID,CATEGORY_ID,OLDSBU_CODE," +
        "OLDSECTION_ID,OLDASSET_USER," +
        "NEWBRANCH_ID,NEWDEPT_ID,NEWSBU_CODE,NEWSECTION_ID,NEWASSET_USER," +
        "NEW_ASSET_ID,TRANSFER_DATE,TRANSFERBY_ID)" +
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


try { 
	con = getConnection();
	ps = con.prepareStatement(query);
	ps.setString(1, oldasset_id);
	//ASSET_ID
//	System.out.println("=====oldasset_id=====> "+oldasset_id);
	ps.setString(2, description);
//	DESCRIPTION	
//	System.out.println("=====description=====> "+description);
	ps.setInt(3, Integer.parseInt(branch_id));
//	BRANCH_ID
//	System.out.println("=====branch_id=====> "+branch_id);
	ps.setInt(4, Integer.parseInt(department_id));
//	DEPT_ID
//	System.out.println("=====department_id=====> "+department_id);
	ps.setInt(5, Integer.parseInt(category_id));
//	CATEGORY_ID
	System.out.println("=====category_id=====> "+category_id);
	ps.setString(6, sbu_code);
//	SBU_CODE
//	System.out.println("=====sbu_code=====> "+sbu_code);
	ps.setInt(7, Integer.parseInt(section_id));
//	SECTION_ID
//	System.out.println("=====section_id=====> "+section_id);
	ps.setString(8, assetuser);
//	Asset_User
//	System.out.println("=====assetuser=====> "+assetuser);
	ps.setInt(9, Integer.parseInt(newbranch_id));
//	BRANCH_ID
//	System.out.println("=====newbranch_id=====> "+newbranch_id);
	ps.setInt(10, Integer.parseInt(newdepartment_id));
//	DEPT_ID
//	System.out.println("=====newdepartment_id=====> "+newdepartment_id);
	ps.setString(11, newsbu_code);
//	newsbu_code
//	System.out.println("=====newsbu_code=====> "+newsbu_code);
	ps.setInt(12, Integer.parseInt(newsection_id));
//	newsection_id
//	System.out.println("=====newsection_id=====> "+newsection_id);
	ps.setString(13, newassetuser);
//	Asset_User	
//	System.out.println("=====newasset_user=====> "+newassetuser);
	ps.setString(14, newasset_id);
//	newasset_id	
//	System.out.println("=====newasset_id=====> "+newasset_id);
	ps.setTimestamp(15, dbConnection.getDateTime(new java.util.Date()));
//	System.out.println("=====AFter Date=====> ");
	ps.setInt(16, Integer.parseInt(user_id));
done = (ps.executeUpdate() != -1);
//System.out.println("=====user_id=====> "+user_id);
if(done==true){
	id ="SUCCESS";
}else{
	id ="FAILED";
}

} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error creating group aset >> "
		+ e.getMessage());
} finally {
}

}



    public void setBranch_id(String branch_id)
    {
        if(branch_id != null)
        {
            this.branch_id = branch_id;
        }
    }
    public void setNewbranch_id(String newbranch_id)
    {
        if(newbranch_id != null)
        {
            this.newbranch_id = newbranch_id;
        }
    }
    public void setDepartment_id(String department_id)
    {
        if(department_id != null)
        {
            this.department_id = department_id;
        }
    }
    public void setNewdepartment_id(String newdepartment_id)
    {
        if(newdepartment_id != null)
        {
            this.newdepartment_id = newdepartment_id;
        }
    }
    public void setSection_id(String section_id)
    {
        if(section_id != null)
        {
            this.section_id = section_id;
        }
    }
    public void setNewsection_id(String newsection_id)
    {
        if(newsection_id != null)
        {
            this.newsection_id = newsection_id;
        }
    }    

    public void setCategory_id(String category_id)
    {
        if(category_id != null)
        {
            this.category_id = category_id;
        }
    }
    public void setOldbranch_code(String oldbranch_code)
    {
        if(oldbranch_code != null)
        {
            this.oldbranch_code = oldbranch_code;
        }
    }    
    public void setOlddepartment_code(String olddepartment_code)
    {
        if(olddepartment_code != null)
        {
            this.olddepartment_code = olddepartment_code;
        }
    }
    public void setOldsection_code(String oldsection_code)
    {
        if(oldsection_code != null)
        {
            this.oldsection_code = oldsection_code;
        }
    }
    public void setCategory_code(String category_code)
    {
        if(category_code != null)
        {
            this.category_code = category_code;
        }
    }
        
    
    public void setMake(String make)
    {
        if(make != null)
        {
            this.make = make;
        }
    }

    public void setLocation(String location)
    {
        if(location != null)
        {
            this.location = location;
        }
    }


    public void setCost_price(String cost_price)
    {
        if(cost_price != null)
        {
            this.cost_price = cost_price.replaceAll(",", "");
        }
    }


    public void setUser(String user)
    {
        if(user != null)
        {
            this.user = user;
        }
    }


    public void setUser_id(String user_id)
    {
        if(user_id != null)
        {
            this.user_id = user_id;
        }
    }

    public void setPosting_date(String s)
    {
    }


    public String getBranch_id()
    {
        return branch_id;
    }

    public String getDepartment_id()
    {
        return department_id;
    }

    public String getSection_id()
    {
        return section_id;
    }

    public String getCategory_id()
    {
        return category_id;
    }

    public String getMake()
    {
        return make;
    }

    public String getLocation()
    {
        return location;
    }


    public String getCost_price()
    {
        return cost_price;
    }


    public String getUser()
    {
        return user;
    }


    public String getUser_id()
    {
        return user_id;
    }
    
    public String getAssetuser()
    {
        return assetuser;
    }
    public void setAssetuser(String assetuser)
    {
        this.assetuser = assetuser;
    }
    public String getNewassetuser()
    {
        return newassetuser;
    }
    public void setNewassetuser(String newassetuser)
    {
        this.newassetuser = newassetuser;
    }    
    public void setDescription(String description)
    {
        if(description != null)
        {
            this.description = description;
        }
    }

    public String getOldasset_id()
    {
        return oldasset_id;
    }
    public void setOldasset_id(String oldasset_id)
    {
        this.oldasset_id = oldasset_id;
    }  
    
    public String getResidualvalue()
        throws Exception
    {
        String selectQuery;
        String residualValue;
        selectQuery = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";
        residualValue = "0.00";
        try
        {
            ResultSet rs = getStatement().executeQuery(selectQuery);
            rs.next();
            residualValue = rs.getString(1);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("INFO: Error getting residualValue >>")).append(e).toString()); 
        }
        return residualValue;
    }

    public String getCreatedGroupId(String category, String description)
    {
        String id;
        String selectQuery;
        id = "";
        selectQuery = (new StringBuilder("SELECT ASSET_ID FROM AM_GROUP_ASSET WHERE CATEGORY_ID = ")).append(category).append(" AND DESCRIPTION = '").append(description).append("'").toString();
        try
        {
            ResultSet rs = getStatement().executeQuery(selectQuery);
            rs.next();
            id = rs.getString(1);
            freeResource();
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("INFO: Error fetching groupid >>")).append(er).toString());
            id = "";

        }
        return id;
    }

    public int insertGroupAssetRecordUpload(String gid)
    throws Exception, Throwable
{
    	System.out.println("newbranch_id: "+newbranch_id+"  newdepartment_id: "+newdepartment_id+"    newsection_id: "+newsection_id+"   category_id: "+category_id+"  gid: "+gid);
        newasset_id = new legend.AutoIDSetup().getIdentity(newbranch_id,newdepartment_id, newsection_id, category_id);
     //   String codeno = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'AM_ASSET'");
    //    int newcode = Integer.parseInt(codeno);
    //    newcode = newcode+1;
     //   approve.getCodeName("update IA_MTID_TABLE SET mt_id = "+newcode+" where mt_tablename = 'AM_ASSET'");
    //    String gid = approve.getCodeName("select MAX(group_id) from am_group_asset_main");
     //   String asset_code = Integer.toString(newcode);
    int DONE = 0;
    int value = 0;
        createGroupUpload(newasset_id);
        value = DONE;
    return value;
}

    public String getCatCode()
    {
        String query;
        String catid;
        query = (new StringBuilder("SELECT CATEGORY_CODE  FROM am_ad_category  WHERE category_id = '")).append(category_id).append("' ").toString();
        catid = "0";
        try
        {
            for(ResultSet rs = getStatement().executeQuery(query); rs.next();)
            {
                catid = rs.getString(1);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return catid;
    }

    public String computeTotalLife(String depRate)
    {
        String totalLife = "0";
        if(depRate == null || depRate.equals(""))
        {
            depRate = "0.0";
        }
        double division = 100D / Double.parseDouble(depRate);
        int intTotal = (int)(division * 12D);
        totalLife = Integer.toString(intTotal);
        return totalLife;
    }

    public void setSbu_code(String sbu_code)
    {
        if(sbu_code != null)
        {
            this.sbu_code = sbu_code;
        }
    }    

    public void setNewsbu_code(String newsbu_code)
    {
        if(newsbu_code != null)
        {
            this.newsbu_code = newsbu_code;
        }
    }   
    
    public ArrayList findAssetByQuery(String queryFilter) {
        //System.out.println("the value of filter is ]]]]]]]]]]]]]]]]]]" +queryFilter);
        double minCost =0;
        double maxCost =0;

        String selectQuery_old =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;

         String selectQuery =
                "SELECT ASSET_ID,REGISTRATION_NO,BRANCH_ID,DEPT_ID," +
                "CATEGORY_ID, DESCRIPTION,DATE_PURCHASED,DEP_RATE," +
                "ASSET_MAKE,ASSET_USER,ASSET_MAINTENANCE," +
                "ACCUM_DEP,MONTHLY_DEP,COST_PRICE,DEP_END_DATE," +
                "RESIDUAL_VALUE,POSTING_DATE,RAISE_ENTRY,DEP_YTD,SECTION , " +
                "NBV,REMAINING_LIFE,TOTAL_LIFE,EFFECTIVE_DATE,Asset_Status,asset_code,REVALUE_COST   " +
                "FROM AM_ASSET  WHERE ASSET_ID IS NOT NULL " + queryFilter;
        System.out.println("the query is <<<<<<<<<<<<< "+ selectQuery);
        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        ArrayList list = new ArrayList();

        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ASSET_ID");
                String registrationNo = rs.getString("REGISTRATION_NO");
                String branchId = rs.getString("BRANCH_ID");
                String departmentId = rs.getString("DEPT_ID");
                String section = rs.getString("SECTION");
                String category = rs.getString("CATEGORY_ID");
                String description = rs.getString("DESCRIPTION");
                String datePurchased = formatDate(rs.getDate("DATE_PURCHASED"));
                double depreciationRate = rs.getDouble("DEP_RATE");
                String assetMake = rs.getString("ASSET_MAKE");
                String assetUser = rs.getString("ASSET_USER");
                String assetMaintenance = rs.getString("ASSET_MAINTENANCE");
                double accumulatedDepreciation = rs.getDouble("ACCUM_DEP");
                double monthlyDepreciation = rs.getDouble("MONTHLY_DEP");
                double cost = rs.getDouble("COST_PRICE");
                String depreciationEndDate = formatDate(rs.getDate(
                        "DEP_END_DATE"));
                double residualValue = rs.getDouble("RESIDUAL_VALUE");
                String postingDate = rs.getString("POSTING_DATE");
                String entryRaised = rs.getString("RAISE_ENTRY");
                double depreciationYearToDate = rs.getDouble("DEP_YTD");
                double nbv = rs.getDouble("NBV");
                double revalue_cost = rs.getDouble("REVALUE_COST");
                int remainingLife = rs.getInt("REMAINING_LIFE");
                int totalLife = rs.getInt("TOTAL_LIFE");
                java.util.Date effectiveDate = rs.getDate("EFFECTIVE_DATE");
                String asset_status=rs.getString("ASSET_STATUS");
                int assetCode = rs.getInt("asset_code");
                Asset aset = new Asset(id, registrationNo, branchId,
                                       departmentId, section, category,
                                       description,
                                       datePurchased, depreciationRate,
                                       assetMake,
                                       assetUser, assetMaintenance,
                                       accumulatedDepreciation,
                                       monthlyDepreciation, cost,
                                       depreciationEndDate,
                                       residualValue, postingDate, entryRaised,
                                       depreciationYearToDate);
                aset.setNbv(nbv);
                aset.setRemainLife(remainingLife);
                aset.setTotalLife(totalLife);
                aset.setEffectiveDate(effectiveDate);
                aset.setAsset_status(asset_status);
                aset.setAssetCode(assetCode);
                aset.setRevalue_cost(revalue_cost);
                list.add(aset);

                minCost =Math.min(minCost, cost);
                maxCost = Math.max(maxCost, cost);

            }

        } catch (Exception e) {
            System.out.println("INFO:Error fetching ALL Asset ->" +
                               e.getMessage());
        } finally {
        	dbConnection.closeConnection(con, ps, rs);
        }

        return list;

    }

    

    public ArrayList findExcelAssetUploadByQuery(String queryFilter) {



        String selectQuery =
        		"select * from am_gb_bulkTransfer where batch_id=? " + queryFilter;
        System.out.println("the query in findExcelAssetUploadByQuery is <<<<<<<<<<<<< " + selectQuery);

        Connection con = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        ArrayList listOld = new ArrayList();

        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(selectQuery);

            rs = ps.executeQuery();
            AssetRecordsBean aset = null;
            while (rs.next()) {
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
                
                aset = new AssetRecordsBean();
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

                listOld.add(aset);
                aset = null;

            }



        } catch (Exception e) {
            System.out.println("INFO:Error findAssetByID() in BulkUpdateManager-> " +
                    e.getMessage());
        } finally {

            dbConnection.closeConnection(con, ps, rs);
        }
        System.out.println("the size of listOld is >>>>>>> " + listOld.size());
        return listOld;

    }

    
}
