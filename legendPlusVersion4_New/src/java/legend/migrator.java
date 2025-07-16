package legend;
import legend.admin.objects.*;
import com.magbel.util.*;
import java.sql.*;
import java.util.*;

public class migrator {
    public migrator() {
    
       
    
    }
    
    private java.util.ArrayList getSQLBranchData(){
                    java.util.ArrayList _list = new java.util.ArrayList();
                            Branch branch = null;
                        String query = "SELECT [BRANCH_ID],[BRANCH_CODE]"
                                        + " ,[BRANCH_NAME],[BRANCH_ACRONYM],[GL_PREFIX]"
                                        + ",[BRANCH_ADDRESS],[STATE],[PHONE_NO]"
                                        + ",[FAX_NO],[REGION],[PROVINCE]"
                                        + ",[BRANCH_STATUS],[USER_ID],[GL_SUFFIX]"
                                        + ",[CREATE_DATE]  FROM [am_ad_branch] ";

                        //query = query + filter;
                        Connection c = null;
                        ResultSet rs = null;
                        Statement s = null;
                    PreparedStatement ps = null;
     
     ConnectManager conn = new ConnectManager();
     
                        try {
                                c= conn.getConnection();
                               rs = c.createStatement().executeQuery(query);
                                //rs = s.executeQuery(query);
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
                                        branch = new Branch(branchId, branchCode,
                                                        branchName, branchAcronym, glPrefix, glSuffix,
                                                        branchAddress, state, phoneNo, faxNo, region, province,
                                                        branchStatus, username, createDate);

                                    _list.add(branch);
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        finally {
                                conn.closeOpenConnection(rs,ps,c);
                        }
                        return _list;

                }

   
   private boolean createBranch(Branch branch) {

                    Connection con = null;
                    PreparedStatement ps = null;
                    boolean done = false;
                    String query = "INSERT INTO am_ad_branch (BRANCH_CODE,BRANCH_NAME"
                                    + ",BRANCH_ACRONYM,GL_PREFIX,BRANCH_ADDRESS"
                                    + ",STATE,PHONE_NO,FAX_NO,REGION,PROVINCE"
                                    + ",BRANCH_STATUS,USER_ID],CREATE_DATE],GL_SUFFIX,BRANCH_ID) "
                                    + " VALUES(?,?,? ,?,?,?,?,?,?,?,?,?,?,?)";
                                    
                ConnectManager conn = new ConnectManager();
                DatetimeFormat df = new DatetimeFormat();
                    try {
                            con = conn.getOracleConnection();
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
                            ps.setString(12, branch.getUsername());
                            ps.setDate(13, df.dateConvert(new java.util.Date()));
                            ps.setString(14, branch.getGlSuffix());
                            ps.setLong(15,System.currentTimeMillis()); 
                            done=( ps.executeUpdate()!=-1);

                    } catch (Exception e) {
                            System.out.println(this.getClass().getName()
                                            + " ERROR:Error Creating Branch ->" + e.getMessage());
                    } finally {
                        conn.closeOpenConnection(ps,con);
                    }
                    return done;

            }

    
    public void setSQLBranchData(){
    
    ArrayList branchRecord = getSQLBranchData();
    boolean result = false;
    for(int i= 0; i < branchRecord.size(); i++){
        Branch branch = (Branch)branchRecord.get(i); 
        result = createBranch(branch);
        if(result){
            System.out.print(branch.getBranchCode()+" "+
                            branch.getBranchName()+""+
                            ":"+branch.getBranchAcronym()+":"+
                            branch.getGlPrefix()+""+branch.getBranchAddress()+""+
                            branch.getState()+""+branch.getPhoneNo()+""+branch.getFaxNo()+
                            ":"+branch.getRegion()+""+branch.getProvince()+""+
                            branch.getBranchStatus()+""+branch.getUsername()+""+branch.getCreate_date()+""+
                            ""+branch.getGlSuffix());
        }
        
    }
        
    }
    
    public static void main(String[] args) {
    
    
    
        migrator migrator = new migrator();
        migrator.setSQLBranchData();
    }
}
