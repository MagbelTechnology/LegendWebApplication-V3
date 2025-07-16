package legend.admin.objects;

/**
 * <p>Title: fileName.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2019</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Lekan Matanmi
 * @version 1.0
 */
public class RegionZone {
    private String regionCode;
    private String zoneCode;
    private String regionId;
    private String zoneId;
    private String mtid;
    public RegionZone() {
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public void setMtid(String mtid) {
        this.mtid = mtid;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getMtid() {
        return mtid;
    }


}
