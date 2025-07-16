package magma;

public class RaiseReclassBean extends legend.ConnectionClass {
  private String entry_date = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
  private String cost_price = "0";
  private String old_accum_dep = "0";
  private String new_accum_dep = "0";
  private String new_asset_gl = "";
  private String old_asset_gl = "";
  private String old_accum_dep_gl = "";
  private String old_dep_gl = "";
  private String new_accum_dep_gl = "";
  private String new_dep_gl = "";
  private String old_dep_rate = "0";
  private String new_dep_rate = "0";
  private String cost_dr_narration;
  private String cost_cr_narration;
  private String old_dr_narration;
  private String old_cr_narration;
  private String new_dr_narration;
  private String new_cr_narration;

  public RaiseReclassBean() throws Exception{
    super();
  }

  /**
   * correctDate
   *
   * @param s String
   * @return String
   */
  private String correctDate(String s) {
    return DateManipulations.CalendarToDb(DateManipulations.DateToCalendar(s));
  }

  /**
   * process
   *
   * @return boolean
   */
  public boolean process() throws Exception {
    StringBuffer sb = new StringBuffer(200);

    //get unique number to use as batch_id
    String batch_id = getIdentity();

    //raise cost entries
    sb = new StringBuffer(300);
    sb.append("am_msp_insert_entry_table '");
    sb.append(new_asset_gl);
    sb.append("','");
    sb.append(old_asset_gl);
    sb.append("','");
    sb.append(cost_dr_narration);
    sb.append("','");
    sb.append(cost_cr_narration);
    sb.append("',");
    sb.append(cost_price);
    sb.append(",'");
    //user
    sb.append("','");
    //supervisor
    sb.append("','");
    //legacy id
    sb.append("','");
    //transaction code
    sb.append("','");
    sb.append(correctDate(entry_date));
    sb.append("','P','N','','");
    sb.append(batch_id);
    sb.append("'"); System.out.print(sb.toString());
    getStatement().executeUpdate(sb.toString());

    //depreciation entries
    sb = new StringBuffer(300);
    sb.append("am_msp_insert_entry_table '");
    sb.append(old_accum_dep_gl);
    sb.append("','");
    if(toOld()){
      sb.append(new_dep_gl);
    }else{
      sb.append(old_dep_gl);
    }
    sb.append("','");
    sb.append(old_dr_narration);
    sb.append("','");
    sb.append(old_cr_narration);
    sb.append("',");
    sb.append(old_accum_dep);
    sb.append(",'");
    //user
    sb.append("','");
    //supervisor
    sb.append("','");
    //legacy id
    sb.append("','");
    //transaction code
    sb.append("','");
    sb.append(correctDate(entry_date));
    sb.append("','P','N','','");
    sb.append(batch_id);
    sb.append("'"); System.out.print(sb.toString());
    getStatement().executeUpdate(sb.toString());

    sb = new StringBuffer(300);
    sb.append("am_msp_insert_entry_table '");
    if(toOld()){
      sb.append(new_dep_gl);
    }else{
      sb.append(old_dep_gl);
    }
    sb.append("','");
    sb.append(new_accum_dep_gl);
    sb.append("','");
    sb.append(new_dr_narration);
    sb.append("','");
    sb.append(new_cr_narration);
    sb.append("',");
    sb.append(new_accum_dep);
    sb.append(",'");
    //user
    sb.append("','");
    //supervisor
    sb.append("','");
    //legacy id
    sb.append("','");
    //transaction code
    sb.append("','");
    sb.append(correctDate(entry_date));
    sb.append("','P','N','','");
    sb.append(batch_id);
    sb.append("'"); System.out.print(sb.toString());
    getStatement().executeUpdate(sb.toString());



    return true;
  }

  public void setEntry_date(String entry_date) {
    //this.entry_date = entry_date;
  }

  public void setCost_price(String cost_price) {
    if(cost_price != null){
      this.cost_price = cost_price;
    }
  }

  public void setOld_accum_dep(String old_accum_dep) {
    if(old_accum_dep != null){
      this.old_accum_dep = old_accum_dep;
    }
  }

  public void setNew_accum_dep(String new_accum_dep) {
    if(new_accum_dep != null){
      this.new_accum_dep = new_accum_dep;
    }
  }

  public void setNew_asset_gl(String new_asset_gl) {
    if(new_asset_gl != null){
      this.new_asset_gl = new_asset_gl;
    }
  }

  public void setOld_asset_gl(String old_asset_gl) {
    if(old_asset_gl != null){
      this.old_asset_gl = old_asset_gl;
    }
  }

  public void setOld_accum_dep_gl(String old_accum_dep_gl) {
    if(old_accum_dep_gl != null){
      this.old_accum_dep_gl = old_accum_dep_gl;
    }
  }

  public void setOld_dep_gl(String old_dep_gl) {
    if(old_dep_gl != null){
      this.old_dep_gl = old_dep_gl;
    }
  }

  public void setNew_accum_dep_gl(String new_accum_dep_gl) {
    if(new_accum_dep_gl != null){
      this.new_accum_dep_gl = new_accum_dep_gl;
    }
  }

  public void setNew_dep_gl(String new_dep_gl) {
    if(new_dep_gl != null){
      this.new_dep_gl = new_dep_gl;
    }
  }

  public void setOld_dep_rate(String old_dep_rate) {
    if(old_dep_rate != null){
      this.old_dep_rate = old_dep_rate;
    }
  }

  public void setNew_dep_rate(String new_dep_rate) {
    if(new_dep_rate != null){
      this.new_dep_rate = new_dep_rate;
    }
  }

  public void setCost_dr_narration(String cost_dr_narration) {
    this.cost_dr_narration = cost_dr_narration;
  }

  public void setCost_cr_narration(String cost_cr_narration) {
    this.cost_cr_narration = cost_cr_narration;
  }

  public void setOld_dr_narration(String old_dr_narration) {
    this.old_dr_narration = old_dr_narration;
  }

  public void setOld_cr_narration(String old_cr_narration) {
    this.old_cr_narration = old_cr_narration;
  }

  public void setNew_dr_narration(String new_dr_narration) {
    this.new_dr_narration = new_dr_narration;
  }

  public void setNew_cr_narration(String new_cr_narration) {
    this.new_cr_narration = new_cr_narration;
  }

  public String getEntry_date() {
    return entry_date;
  }

  public String getCost_price() {
    return cost_price;
  }

  public String getOld_accum_dep() {
    return old_accum_dep;
  }

  public String getNew_accum_dep() {
    return new_accum_dep;
  }

  public String getNew_asset_gl() {
    return new_asset_gl;
  }

  public String getOld_asset_gl() {
    return old_asset_gl;
  }

  public String getOld_accum_dep_gl() {
    return old_accum_dep_gl;
  }

  public String getOld_dep_gl() {
    return old_dep_gl;
  }

  public String getNew_accum_dep_gl() {
    return new_accum_dep_gl;
  }

  public String getNew_dep_gl() {
    return new_dep_gl;
  }

  public String getOld_dep_rate() {
    return old_dep_rate;
  }

  public String getNew_dep_rate() {
    return new_dep_rate;
  }

  public String getCost_dr_narration() {
    return cost_dr_narration;
  }

  public String getCost_cr_narration() {
    return cost_cr_narration;
  }

  public String getOld_dr_narration() {
    return old_dr_narration;
  }

  public String getOld_cr_narration() {
    return old_cr_narration;
  }

  public String getNew_dr_narration() {
    return new_dr_narration;
  }

  public String getNew_cr_narration() {
    return new_cr_narration;
  }

  public boolean toOld(){
    return Float.parseFloat(new_dep_rate) < Float.parseFloat(old_dep_rate);
  }

}
