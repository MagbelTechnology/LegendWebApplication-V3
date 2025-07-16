package magma;

import java.sql.ResultSet;

public class RaiseAssetBean extends legend.ConnectionClass {
  public RaiseAssetBean()  throws Exception{
    super();
  }
  /**
   * getAssetList
   *
   * @return String[][]
   */
  public String[][] getAssetList() throws Exception {
    StringBuffer b = new StringBuffer(100);
    b.append("am_msp_select_asset_raise_list_supervisor");
    ResultSet rs = getStatement().executeQuery(b.toString());

    b = new StringBuffer(100);
    b.append("am_msp_count_asset_raise_list_supervisor");
    ResultSet rsc = getStatement().executeQuery(b.toString());
    rsc.next();
    int count = rsc.getInt(1);


    if(count > 0){
      String a[][] = new String[count][16];
      int i=0;
      while(rs.next()){
        for(int j=0; j<16; j++){
          a[i][j] = rs.getString(j+1);
        }
        i++;
      }
      return a;
    }
    return null;

  }
}
