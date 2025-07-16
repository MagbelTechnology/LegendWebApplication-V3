package magma.net.manager;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import magma.net.vao.Component;
import magma.net.vao.CategoryComponent;
import magma.net.vao.ComponentDetail;
import magma.net.dao.MagmaDBConnection;

import com.magbel.util.ApplicationHelper;

import legend.admin.objects.Memo;

import java.sql.Date;

import magma.net.vao.ComponentView;
/**
 * <p>Title: ComponentManager</p>
 *
 * <p>Description: Presently asset creation in the application presents <br>
                  one screen for all inputs save for assets that require<br>
 distribution therefore in order to meet the above objectives <br>
                  it is necessary for us to provide additional screen where<br>
 assets that require such additional input can be setup.<br><br>

                  This class Manages the creation,update and reterieval of <br>
 addtional compoents for an asset marked as multiple components.

                  </p>
 *
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 *
 * <p>Company: Magbel Technologies Limited.</p>
 * @author Jejelowo Babatope Festus
 * @author festus.jejelowo@ocular-minds.com
 * @version 2.0
 * @see magma.net.vao.Component
 * @see Busniness Requirement Specification.doc<br>
 * from the root/doc of this project.

 */

public class ComponentManager extends MagmaDBConnection {

    private String componentAssetId;

    public ComponentManager() {

    }

    private void setComponentAssetId(String componentAssetId) {
        this.componentAssetId = componentAssetId;
    }

    public String getComponentAssetId() {

        return componentAssetId;
    }

    public void createCategoryComponent(String category, String description,
                                        String logDate) {
   
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "INSERT INTO  AM_CT_COMPONENT(" +
                             "CATEGORY, DESCRIPTION ,CREATE_DT,STATUS) VALUES( ?,?,?,?)";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setInt(1, new Integer(category).intValue());
            ps.setString(2, description);
            ps.setString(3, logDate);
            ps.setDate(4, dateConvert(logDate));
            ps.setString(4, "ACTIVE");

            ps.execute();

        } catch (Exception ex) {
            System.out.println("WARN: Error creating Category Components >>" +
                               ex);
        } finally {
            closeConnection(con, ps);
        }

    }

    public void updateCategoryComponent(String id, String description,
                                        String status) {

        Connection con = null;
        PreparedStatement ps = null;
        String updateQuery = "UPDATE AM_CT_COMPONENT(" +
                             "SET DESCRIPTION = ?, STATUS = ? " +
                             "WHERE AM_ID = ?";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(updateQuery);

            ps.setString(1, description);
            ps.setString(2, status);
            ps.setInt(3, new Integer(id).intValue());

            ps.execute();

        } catch (Exception ex) {
            System.out.println("WARN: Error updating Category Components >>" +
                               ex);
        } finally {
            closeConnection(con, ps);
        }

    }

    public ArrayList findAllCategoryComponent() {
        String queryFilter = "";
        return findCategoryComponentByQuery(queryFilter);
    }

    public ArrayList findCategoryComponentByCategoryId(String catId) {

        String queryFilter = " WHERE CATEGORY = " + catId + "";
        ArrayList list = findCategoryComponentByQuery(queryFilter);

        return list;

    }

    public CategoryComponent findCategoryComponentById(String id) {
        CategoryComponent finder = null;
        String queryFilter = " WHERE AM_ID = '" + id + "'";
        ArrayList list = findCategoryComponentByQuery(queryFilter);
        if (list.size() > 0) {
            finder = (CategoryComponent) list.get(0);
        }

        return finder;
    }

    public ArrayList findCategoryComponentByQuery(String queryFilter) {

        ArrayList list = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT AM_ID , CATEGORY,DESCRIPTION,CREATE_DT, STATUS     " +
                "FROM AM_CT_COMPONENT  " + queryFilter;

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("AM_ID");
                String category = rs.getString("CATEGORY");
                String description = rs.getString("DESCRIPTION");
                String logDate = formatDate(rs.getDate("CREATE_DT"));
                String status = rs.getString("STATUS");

                CategoryComponent component = new CategoryComponent(id,
                        category, description, logDate, status);
                list.add(component);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fecthing Category Components >>" +
                               ex);
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }


    public void createComponent(String parentAssetId, String parentCompId,
                                String category, String description,
                                String serialNumber, String make, String model,
                                String additionalField) {

        String assetId = createComponentAssetId(parentAssetId);
        Connection con = null;
        PreparedStatement ps = null;
        String createQuery = "INSERT INTO  AM_AD_COMPONENT(" +
                             "PARENT_ASSET_ID ,ASSET_ID, CATEGORY, " +
                             "DESCRIPTION, SERIAL_NO , MAKE,MODEL, " +
                             "ADDITIONAL_FIELD, STATUS,PARENT_COMP_ID) VALUES( ?,?,?,?,?,?,?,?,?,?)";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, parentAssetId);
            ps.setString(2, assetId);
            ps.setInt(3, new Integer(category).intValue());
            ps.setString(4, description);
            ps.setString(5, serialNumber);
            ps.setString(6, make);
            ps.setString(7, model);
            ps.setString(8, additionalField);
            ps.setString(9, "ACTIVE");
            ps.setInt(10, new Integer(parentCompId).intValue());

            ps.execute();

            setComponentAssetId(assetId);

        } catch (Exception ex) {
            System.out.println("WARN: Error creating Components >>" + ex);
        } finally {
            closeConnection(con, ps);
        }

    }

    public void updateComponent(String id, String description,
                                String serialNumber,
                                String make, String model,
                                String additionalField,
                                String status) {

        Connection con = null;
        PreparedStatement ps = null;
        String updateQuery = "UPDATE AM_AD_COMPONENT(" +
                             "SET DESCRIPTION = ?, SERIAL_NO = ?, MAKE = ?," +
                             "MODEL = ?,  ADDITIONAL_FIELD = ?, STATUS = ? " +
                             "WHERE COMP_ID = ?";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(updateQuery);

            ps.setString(1, description);
            ps.setString(2, serialNumber);
            ps.setString(3, make);
            ps.setString(4, model);
            ps.setString(5, additionalField);
            ps.setString(6, status);
            ps.setInt(7, new Integer(id).intValue());

            ps.execute();

        } catch (Exception ex) {
            System.out.println("WARN: Error updating Components >>" + ex);
        } finally {
            closeConnection(con, ps);
        }

    }

    public ArrayList findAllComponent() {
        String queryFilter = "";
        return findComponentByQuery(queryFilter);
    }


    public ArrayList findComponentByAssetId(String assetId) {

        String queryFilter = " WHERE PARENT_ASSET_ID = '" + assetId + "'";
        ArrayList list = findComponentByQuery(queryFilter);

        return list;

    }

    public Component findComponentByGroupAssetId(String parentAssetId,
                                                 String parentCompId) {

        Component finder = null;
        String queryFilter = " WHERE PARENT_ASSET_ID = '" + parentAssetId +
                             "' " +
                             "AND PARENT_COMP_ID = " + parentCompId;
        ArrayList list = findComponentByQuery(queryFilter);
        if (list.size() > 0) {
            finder = (Component) list.get(0);
        }

        return finder;

    }

    public Component findComponentByUAssetId(String assetId) {

        Component finder = null;
        String queryFilter = " WHERE ASSET_ID = '" + assetId + "'";
        ArrayList list = findComponentByQuery(queryFilter);
        if (list.size() > 0) {
            finder = (Component) list.get(0);
        }

        return finder;

    }

    public Component findComponentById(String id) {
        Component finder = null;
        String queryFilter = " WHERE COMP_ID = '" + id + "'";
        ArrayList list = findComponentByQuery(queryFilter);
        if (list.size() > 0) {
            finder = (Component) list.get(0);
        }

        return finder;
    }

    public ArrayList findComponentByQuery(String queryFilter) {

        ArrayList list = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectQuery =
                "SELECT COMP_ID ,PARENT_ASSET_ID ,ASSET_ID, CATEGORY, " +
                "PARENT_COMP_ID,DESCRIPTION, SERIAL_NO , MAKE,MODEL, " +
                "ADDITIONAL_FIELD, STATUS     " +
                "FROM AM_AD_COMPONENT  " + queryFilter;

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("COMP_ID");
                String parentAssetId = rs.getString("PARENT_ASSET_ID");
                String assetId = rs.getString("ASSET_ID");
                String category = rs.getString("CATEGORY");
                String parentCompId = rs.getString("PARENT_COMP_ID");
                String description = rs.getString("DESCRIPTION");
                String serialNumber = rs.getString("SERIAL_NO");
                String make = rs.getString("MAKE");
                String model = rs.getString("MODEL");
                String additionalField = rs.getString("ADDITIONAL_FIELD");
                String status = rs.getString("STATUS");

                Component component = new Component(id, parentAssetId,
                        parentCompId, assetId,
                        category, description, serialNumber, make, model,
                        additionalField, status);
                list.add(component);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fecthing Components >>" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }

        return list;
    }

    private String createComponentAssetId(String parentAssetId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String suffix = "";
        String selectQuery =
                "SELECT count(PARENT_ASSET_ID)  FROM AM_AD_COMPONENT  " +
                "WHERE PARENT_ASSET_ID = '" + parentAssetId + "'";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();

            while (rs.next()) {

                int counter = rs.getInt(1);

                if (counter == 0) {
                    suffix = "A";
                } else if (counter == 1) {
                    suffix = "B";
                } else if (counter == 2) {
                    suffix = "C";
                } else if (counter == 3) {
                    suffix = "D";
                } else if (counter == 4) {
                    suffix = "E";
                } else if (counter == 5) {
                    suffix = "F";
                } else if (counter == 6) {
                    suffix = "G";
                } else if (counter == 7) {
                    suffix = "H";
                } else if (counter == 8) {
                    suffix = "I";
                } else if (counter == 9) {
                    suffix = "J";
                } else if (counter == 10) {
                    suffix = "K";
                } else {
                    suffix = "X";
                }

            }

        } catch (Exception ex) {
            System.out.println("WARN: Error creating compnent Asset id >>" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }
        return parentAssetId + suffix;
    }

    public boolean isComponentFound(String parentAssetId, String parentCompId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean componentFound = false;
        String createQuery = "SELECT (PARENT_COMP_ID) FROM  AM_AD_COMPONENT  " +
                             "WHERE PARENT_ASSET_ID = ? AND PARENT_COMP_ID = ? ";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, parentAssetId);
            ps.setInt(2, new Integer(parentCompId).intValue());

            rs = ps.executeQuery();
            while (rs.next()) {
                int found = rs.getInt(1);
                componentFound = (found > 0) ? true : false;
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error isComponentFound >>" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }
        return componentFound;
    }

    public void removeComponentFound(String parentAssetId, String parentCompId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String createQuery = "DELETE FROM  AM_AD_COMPONENT  " +
                             "WHERE PARENT_ASSET_ID = ? AND PARENT_COMP_ID = ? ";

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(createQuery);

            ps.setString(1, parentAssetId);
            ps.setInt(2, new Integer(parentCompId).intValue());

            ps.execute();

        } catch (Exception ex) {
            System.out.println("WARN: Error  deleting ComponentFound >>" + ex);
        } finally {
            closeConnection(con, ps);
        }
    }
public String createComponentAssetIdDelimiter(String parentAssetId,String description) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String suffix = "C";
        String number =  new ApplicationHelper().getGeneratedId("delimiterValue");
        String selectQuery =
                "SELECT component_delimiter  FROM am_gb_company " ;

        try {

            con = getConnection("legendPlus");
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();
            while (rs.next())
            {

            	suffix = rs.getString("component_delimiter");
            }


        } catch (Exception ex) {
            System.out.println("WARN: Error getting  compnent delimiter >>" + ex);
        } finally {
            closeConnection(con, ps, rs);
        }
        String assetIdnew = parentAssetId+suffix+number;
        System.out.println("assetIdnew  >>  "+assetIdnew);
        return assetIdnew;
    }
private String getComponentNumber(String parentAssetId,String description)
{

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    String number = "0";

    String selectQuery =
            "SELECT AM_ID FROM AM_CT_COMPONENT WHERE DESCRIPTION = ? " ;

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        ps.setString(1, description);
        rs = ps.executeQuery();
        while (rs.next())
        {

        	number = rs.getString("AM_ID");
        }


    } catch (Exception ex) {
        System.out.println("WARN: Error getting compnent number >>" + ex);
    } finally {
        closeConnection(con, ps, rs);
    }
    return  number;
}
public String getComponentNumber(String description)
{

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    String number = "0";

    String selectQuery =
            "SELECT AM_ID FROM AM_CT_COMPONENT WHERE DESCRIPTION = ? " ;

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        ps.setString(1, description);
        rs = ps.executeQuery();
        while (rs.next())
        {

        	number = rs.getString("AM_ID");
        }


    } catch (Exception ex) {
        System.out.println("WARN: Error getting compnent number >>" + ex);
    } finally {
        closeConnection(con, ps, rs);
    }
    return  number;
}
public boolean isComponentExisting(String category,String description) {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    boolean done = false;

    String selectQuery =
            "SELECT * FROM AM_CT_COMPONENT WHERE DESCRIPTION = ? AND CATEGORY = ? " ;

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        ps.setString(1, description);
        ps.setString(2, category);
        rs = ps.executeQuery();
        while (rs.next())
        {

        	done= true;
        }


    } catch (Exception ex) {
        System.out.println("WARN: Error getting compnent status  >>" + ex);
    } finally {
        closeConnection(con, ps, rs);
    }
    return  done;
}

public ArrayList findComponentView(String AssetId)
{
    ArrayList list = new ArrayList();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String selectQuery =
            "SELECT COMP_ID ,PARENT_ASSET_ID,PARENT_COMP_ID " +
            ",ASSET_ID,CATEGORY,DESCRIPTION,SERIAL_NO " +
            ",MAKE,MODEL,ADDITIONAL_FIELD,STATUS " +
            ",COMPONENT,COST,ASSIGNED ,DEPRECIATION " +
            "FROM AM_AD_COMPONENT WHERE PARENT_ASSET_ID = ? " ;

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        ps.setString(1, AssetId);
        rs = ps.executeQuery();

        while (rs.next())
        {


        	 String id = rs.getString("COMP_ID");
             String parentAssetId = rs.getString("PARENT_ASSET_ID");
             String parentCompId = rs.getString("PARENT_COMP_ID");
             String assetId = rs.getString("ASSET_ID");
             String category = rs.getString("CATEGORY");
             String description = rs.getString("DESCRIPTION");
             String serialNumber = rs.getString("SERIAL_NO");
             String make = rs.getString("MAKE");
             String model = rs.getString("MODEL");
             String additionalField = rs.getString("ADDITIONAL_FIELD");
             String status = rs.getString("STATUS");
             String component = rs.getString("COMPONENT");
             double cost = rs.getDouble("COST");
             double assigned = rs.getDouble("ASSIGNED");
             double depreciation = rs.getDouble("DEPRECIATION");



ComponentView comp = new ComponentView( id, parentAssetId,parentCompId, assetId,
                     category, description, serialNumber, make, model,
                     additionalField,status,component,cost,assigned,depreciation );
         list.add(comp);
        }

    }
    catch (Exception ex)
    {
        System.out.println("WARN: Error fecthing Components >>" + ex);
    }
    finally
    {
        closeConnection(con, ps, rs);
    }

    return list;
}
public ArrayList findMemoView(String AssetCode)
{

    ArrayList list = new ArrayList();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String selectQuery =
            "SELECT * FROM AM_ASSET_MEMO WHERE Asset_Code = '"+AssetCode+"' " ;

    try {
System.out.println(selectQuery);
        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        rs = ps.executeQuery();

        while (rs.next())
        {


        	 String id = rs.getString("memo_asset_id");
             String branch = rs.getString("branch_id");
             double cost = rs.getDouble("cost_price");
             Date date = rs.getDate("date_purchased");


         Memo memo = new Memo(id,branch,cost,date);
         list.add(memo);
        }

    }
    catch (Exception ex)
    {
       ex.printStackTrace();
    }
    finally
    {
        closeConnection(con, ps, rs);
    }

    return list;
}

public void createCategoryComponent(String category, String description,
                                    String logDate,String defaultValue,String type,double Amount,double Percentage) {

    Connection con = null;
    PreparedStatement ps = null;
    String createQuery = "INSERT INTO  AM_CT_COMPONENT(" +
                         "CATEGORY, DESCRIPTION ,CREATE_DT,STATUS,DEFAULT_VALUE, DISTRIBUTION_TYPE,AMOUNT,PERCENTAGE) "+
                         "VALUES( ?,?,?,?,?,?,?,?)";

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(createQuery);

        ps.setInt(1, new Integer(category).intValue());
        ps.setString(2, description);
        ps.setString(3, logDate);
    //    ps.setDate(4, dateConvert(logDate));
        ps.setString(4, "ACTIVE");
        ps.setString(5, defaultValue);
        ps.setString(6, type);
        ps.setDouble(7, Amount);
        ps.setDouble(8, Percentage);

        ps.execute();

    } catch (Exception ex) {
        System.out.println("WARN: Error creating Category Components <<<<>>" +
                           ex);
    } finally {
        closeConnection(con, ps);
    }

}


public ArrayList findCategoryComponentByCategoryIdComponent(String category,String name,String id) {
	    CategoryComponent finder = null;

    String queryFilter = " WHERE CATEGORY = '" + category + "' AND DESCRIPTION = '" + name + "' AND AM_ID = '" + id + "'";
    ArrayList list = findCategoryComponentByQuery2(queryFilter);

    return list;

}


public ArrayList findCategoryComponentByQuery2(String queryFilter) {

    ArrayList list = new ArrayList();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String selectQuery =
            "SELECT AM_ID , CATEGORY,DESCRIPTION,CREATE_DT, STATUS,DEFAULT_VALUE, DISTRIBUTION_TYPE,AMOUNT,PERCENTAGE     " +
            "FROM AM_CT_COMPONENT  " + queryFilter;

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(selectQuery);
        rs = ps.executeQuery();

        while (rs.next()) {

            String id = rs.getString("AM_ID");
            String category = rs.getString("CATEGORY");
            String description = rs.getString("DESCRIPTION");
            String logDate = formatDate(rs.getDate("CREATE_DT"));
            String status = rs.getString("STATUS");
            String creationDate = rs.getString("CREATE_DT");
            String defaultValue = rs.getString("DEFAULT_VALUE");
            String distType = rs.getString("DISTRIBUTION_TYPE");
            double amount = rs.getDouble("AMOUNT");
            double percentage = rs.getDouble("PERCENTAGE");

            ComponentDetail component = new ComponentDetail(id, category, description, status, creationDate,defaultValue,distType, amount, percentage);
           
            list.add(component);
        }

    } catch (Exception ex) {
        System.out.println("WARN: Error fecthing Category Components ======>>" +
                           ex);
    } finally {
        closeConnection(con, ps, rs);
    }

    return list;
}


public void updateCategoryComponent(String id, double amount, double percentage, String description) {

    Connection con = null;
    PreparedStatement ps = null;
    String updateQuery = "UPDATE AM_CT_COMPONENT(" +
                         "SET AMOUNT = ?, PERCENTAGE = ? " +
                         "WHERE AM_ID = ?";

    try {

        con = getConnection("legendPlus");
        ps = con.prepareStatement(updateQuery);

        ps.setDouble(1, amount);
        ps.setDouble(2, percentage);
        ps.setInt(3, new Integer(id).intValue());

        ps.execute();

    } catch (Exception ex) {
        System.out.println("WARN: Error updating Category Components===== >>" +
                           ex);
    } finally {
        closeConnection(con, ps);
    }

}



}
