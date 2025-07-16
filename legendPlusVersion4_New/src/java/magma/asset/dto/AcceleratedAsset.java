package magma.asset.dto;

import java.io.Serializable;

public class AcceleratedAsset implements Serializable {
    private String acceleratedId;
    private String assetId;
    private String reason;
    private int acceleratedmonths;
    private double accum_Dep;
    private int usefullife;
    private String acceleratedDate;
    private String effDate;
    private String userId;
    private int remainlife;
    private String description;
    private double nbv;
    private double monthly_Dep;
    private String assetUser;
    private String supervisor;
    private double depramount;
    private String cracct;
    private String dracct;

    public AcceleratedAsset(String acceleratedId, String assetId, String reason,
                    int acceleratedmonths
                    , double accum_Dep, int usefullife, String acceleratedDate,
                    String effDate, String userId, int remainlife,
                    String desc, double nbv,double monthly_Dep,double depramount,String cracct, String dracct) {
        setAcceleratedId(acceleratedId);
        setAssetId(assetId);
        setReason(reason);
        setAcceleratedmonths(acceleratedmonths);
        setAccum_Dep(accum_Dep);
        setUsefullife(usefullife);
        setAcceleratedDate(acceleratedDate);
        setEffDate(effDate);
        setUserId(userId);
        setRemainlife(remainlife);
        setDescription(desc);
        setNbv(nbv);
        setMonthly_Dep(monthly_Dep);
        setDepramount(depramount);
        setCracct(cracct);
        setDracct(dracct);
    }

     public AcceleratedAsset(String acceleratedId, String assetId, String reason,
                    int acceleratedmonths
                    , double accum_Dep, int usefullife,  String acceleratedDate,
                    String effDate, String userId, int remainlife,
                    String desc, double nbv, double monthly_Dep,double depramount,String supervise,String cracct,String dracct) {
        setAcceleratedId(acceleratedId);
        setAssetId(assetId);
        setReason(reason);
        setAcceleratedmonths(acceleratedmonths);
        setAccum_Dep(accum_Dep);
        setUsefullife(usefullife);
        setAcceleratedDate(acceleratedDate);
        setEffDate(effDate);
        setUserId(userId);
        setRemainlife(remainlife);
        setDescription(desc);
        setNbv(nbv);
        setMonthly_Dep(monthly_Dep);
        setDepramount(depramount);
        setAssetUser(assetUser);
        setSupervisor(supervise);
        setCracct(cracct);
        setDracct(dracct);
        
    }


    public String getAcceleratedId() {
        return acceleratedId;
    }

    public void setAcceleratedId(String acceleratedId) {
        this.acceleratedId = acceleratedId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAcceleratedmonths() {
        return acceleratedmonths;
    }

    public void setAcceleratedmonths(int acceleratedmonths) {
        this.acceleratedmonths = acceleratedmonths;
    }

    public double getAccum_Dep() {  
        return accum_Dep;
    }

    public void setAccum_Dep(double accum_Dep) {
        this.accum_Dep = accum_Dep;
    }

    public double getMonthly_Dep() {  
        return monthly_Dep;
    }

    public void setMonthly_Dep(double monthly_Dep) {
        this.monthly_Dep = monthly_Dep;
    }

    public double getDepramount() {  
        return depramount;
    }

    public void setDepramount(double depramount) {
        this.depramount = depramount;
    }

    public int getUsefullife() {
        return usefullife;
    }

    public void setUsefullife(int usefullife) {
        this.usefullife = usefullife;
    }

    public String getAcceleratedDate() {
        return acceleratedDate;
    }

    public void setAcceleratedDate(String acceleratedDate) {
        this.acceleratedDate = acceleratedDate;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRemainlife() {
        return remainlife;
    }

    public void setRemainlife(int remainlife) {
        this.remainlife = remainlife;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getNbv() {
        return nbv;
    }

    public void setNbv(double nbv) {
        this.nbv = nbv;
    }

    public String getAssetUser() {
        return assetUser;
    }

    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }

    /**
     * @return the supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**    
     * @param cracct the cracct to set
     */
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * @return the cracct
     */
    public String getCracct() {
        return cracct;
    }

    /**    
     * @param cracct the cracct to set
     */
    public void setCracct(String cracct) {
        this.cracct = cracct;
    }    

    /**
     * @return the dracct
     */
    public String getDracct() {
        return dracct;
    }

    /**    
     * @param dracct the dracct to set
     */
    public void setDracct(String dracct) {
        this.dracct = dracct;
    }        
}