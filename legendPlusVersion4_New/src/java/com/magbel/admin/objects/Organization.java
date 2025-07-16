



package com.magbel.admin.objects;




public class Organization
{

    private String organId;
    private String organCode;
    private String organName;
    private String organAcronym;
    private String organAddress;
    private String phoneNo;
    private String faxNo;
    private String region;
    private String province;
    private String organStatus;
    private String userId;
    private String create_date;
    private String organDomain;
    private String organemail;
    private String organContact;
    public Organization()
    { 
    }
    
    public Organization(String organId, String organCode, String organName, String organAcronym, String organAddress,
            String phoneNo, String faxNo, String organStatus, String userId,
            String create_date, String organDomain, String organemail, String organContact)
    {
        this.organId = organId;
        this.organCode = organCode;
        this.organName = organName;
        this.organAcronym = organAcronym;
        this.organAddress = organAddress;
        this.phoneNo = phoneNo;
        this.faxNo = faxNo;
        this.organStatus = organStatus;
        this.userId = userId;
        this.create_date = create_date;  
        this.organDomain =  organDomain;
        this.organemail = organemail;
        this.organContact = organContact;
    }

    public void setorganId(String organId)
    {
        this.organId = organId;
    }

    public String getorganId()
    {
        return organId;
    }

    public void setorganCode(String organCode)
    {
        this.organCode = organCode;
    }

    public String getorganCode()
    {
        return organCode;
    }

    public void setorganName(String organName)
    {
        this.organName = organName;
    }

    public String getorganName()
    {
        return organName;
    }

    public void setorganAcronym(String organAcronym)
    {
        this.organAcronym = organAcronym;
    }

    public String getorganAcronym()
    {
        return organAcronym;
    }

    public void setorganAddress(String organAddress)
    {
        this.organAddress = organAddress;
    }

    public String getorganAddress()
    {
        return organAddress;
    }

    public void setPhoneNo(String phoneNo)
    {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo()
    {
        return phoneNo;
    }

    public void setFaxNo(String faxNo)
    {
        this.faxNo = faxNo;
    }

    public String getFaxNo()
    {
        if(faxNo == null)faxNo ="";
        return faxNo;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getProvince()
    {
        return province;
    }

    public void setorganStatus(String organStatus)
    {
        this.organStatus = organStatus;
    }

    public String getorganStatus()
    {
        return organStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    public void setorganDomain(String organDomain)
    {
        this.organDomain = organDomain;
    }

    public String getorganDomain()
    {
        return organDomain;
    }
    public void setorganemail(String organemail)
    {
        this.organemail = organemail;
    }
    public String getorganemail()
    {
        return organemail;
    }
    public void setorganContact(String organContact)
    {
        this.organContact = organContact;
    }           
    public String getorganContact()
    {
        return organContact;
    }
    


}

