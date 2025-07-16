package com.magbel.ia.vao;

import java.io.Serializable;

public class Customer
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String mtId;
    private String customerCode;
    private String oldCustomerNo;
    private String lastName;
    private String firstName;
    private String middleName;
    private String motherMaidenName;
    private String gender;
    private String customerName;
    private String residency;
    private String cityOfBirth;
    private String title;
    private String customerCategory;
    private String customerType;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String countryCode;
    private String branch;
    private String branchCode;
    private String industry;
    private String industryCode;
    private String createDate;
    private String phone;
    private String phone2;
    private String fax;
    private String email;
    private String effectiveDate;
    private String status;
    private int userId;
    private String closedDate;
    private String occupation;
    private String maritalStatus;
    private String statementDelivery;
    private String relationship;
    private String customerClassCode;
    private String relationshipCode;
    private String contactCode;
    private String fullName;
    private String customerClass;
    private String dateOfBirth;
    private String companyCode;

    public Customer()
    {
        userId = 0;
    }

    public final String getAddress1()
    {
        return address1;
    }

    public final void setAddress1(String Address1)
    {
        address1 = Address1;
    }

    public final String getAddress2()
    {
        return address2;
    }

    public final void setAddress2(String Address2)
    {
        address2 = Address2;
    }

    public final String getBranch()
    {
        return branch;
    }

    public final void setBranch(String Branch)
    {
        branch = Branch;
    }

    public final String getCity()
    {
        return city;
    }

    public final void setCity(String City)
    {
        city = City;
    }

    public final String getCountry()
    {
        return country;
    }

    public final void setCountry(String Country)
    {
        country = Country;
    }

    public final String getCustomerCode()
    {
        return customerCode;
    }

    public final void setCustomerCode(String CustomerCode)
    {
        customerCode = CustomerCode;
    }

    public final String getCustomerCategory()
    {
        return customerCategory;
    }

    public final void setCustomerCategory(String CustomerCategory)
    {
        customerCategory = CustomerCategory;
    }

    public final String getCustomerType()
    {
        return customerType;
    }

    public final void setCustomerType(String CustomerType)
    {
        customerType = CustomerType;
    }

    public final String getFirstName()
    {
        return firstName;
    }

    public final void setFirstName(String FirstName)
    {
        firstName = FirstName;
    }

    public final String getCustomerName()
    {
        return customerName;
    }

    public final void setCustomerName(String CustomerName)
    {
        customerName = CustomerName;
    }

    public final String getGender()
    {
        return gender;
    }

    public final void setGender(String Gender)
    {
        gender = Gender;
    }

    public final String getIndustry()
    {
        return industry;
    }

    public final void setIndustry(String Industry)
    {
        industry = Industry;
    }

    public final String getLastName()
    {
        return lastName;
    }

    public final void setLastName(String LastName)
    {
        lastName = LastName;
    }

    public final String getMiddleName()
    {
        return middleName;
    }

    public final void setMiddleName(String MiddleName)
    {
        middleName = MiddleName;
    }

    public final String getMtId()
    {
        return mtId;
    }

    public final void setMtId(String MtId)
    {
        mtId = MtId;
    }

    public final String getOldCustomerNo()
    {
        return oldCustomerNo;
    }

    public final void setOldCustomerNo(String OldCustomerNo)
    {
        oldCustomerNo = OldCustomerNo;
    }

    public final String getResidency()
    {
        return residency;
    }

    public final void setResidency(String Residency)
    {
        residency = Residency;
    }

    public final String getState()
    {
        return state;
    }

    public final void setState(String State)
    {
        state = State;
    }

    public final String getStatus()
    {
        return status;
    }

    public final void setStatus(String Status)
    {
        status = Status;
    }

    public final String getTitle()
    {
        return title;
    }

    public final void setTitle(String Title)
    {
        title = Title;
    }

    public final int getUserId()
    {
        return userId;
    }

    public final void setUserId(int UserId)
    {
        userId = UserId;
    }

    public final String getCreateDate()
    {
        return createDate;
    }

    public final void setCreateDate(String CreateDate)
    {
        createDate = CreateDate;
    }

    public final String getEffectiveDate()
    {
        return effectiveDate;
    }

    public final void setEffectiveDate(String EffectiveDate)
    {
        effectiveDate = EffectiveDate;
    }

    public final String getEmail()
    {
        return email;
    }

    public final void setEmail(String Email)
    {
        email = Email;
    }

    public final String getFax()
    {
        return fax;
    }

    public final void setFax(String Fax)
    {
        fax = Fax;
    }

    public final String getPhone()
    {
        return phone;
    }

    public final void setPhone(String Phone)
    {
        phone = Phone;
    }

    public final void setPhone2(String phone2)
    {
        this.phone2 = phone2;
    }

    public final String getPhone2()
    {
        return phone2;
    }

    public final void setBranchCode(String BranchCode)
    {
        branchCode = BranchCode;
    }

    public final void setIndustryCode(String IndustryCode)
    {
        industryCode = IndustryCode;
    }

    public final void setCountryCode(String CountryCode)
    {
        countryCode = CountryCode;
    }

    public final String getCountryCode()
    {
        return countryCode;
    }

    public final String getBranchCode()
    {
        return branchCode;
    }

    public final String getIndustryCode()
    {
        return industryCode;
    }

    public final void setMotherMaidenName(String MotherMaidenName)
    {
        motherMaidenName = MotherMaidenName;
    }

    public final void setCityOfBirth(String CityOfBirth)
    {
        cityOfBirth = CityOfBirth;
    }

    public final void setClosedDate(String ClosedDate)
    {
        closedDate = ClosedDate;
    }

    public final String getMotherMaidenName()
    {
        return motherMaidenName;
    }

    public final String getCityOfBirth()
    {
        return cityOfBirth;
    }

    public final String getClosedDate()
    {
        return closedDate;
    }

    public final String getOccupation()
    {
        return occupation;
    }

    public final void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public final String getMaritalStatus()
    {
        return maritalStatus;
    }

    public final void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public final String getRelationship()
    {
        return relationship;
    }

    public final void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public final String getCustomerClass()
    {
        return customerClass;
    }

    public final void setCustomerClass(String customerClass)
    {
        this.customerClass = customerClass;
    }

    public final String getRelationshipCode()
    {
        return relationshipCode;
    }

    public final void setRelationshipCode(String relationshipCode)
    {
        this.relationshipCode = relationshipCode;
    }

    public final String getContactCode()
    {
        return contactCode;
    }

    public final void setContactCode(String contactCode)
    {
        this.contactCode = contactCode;
    }

    public final String getCustomerClassCode()
    {
        return customerClassCode;
    }

    public final void setCustomerClassCode(String customerClassCode)
    {
        this.customerClassCode = customerClassCode;
    }

    public final String getFullName()
    {
        return fullName;
    }

    public final void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public final String getStatementDelivery()
    {
        return statementDelivery;
    }

    public final void setStatementDelivery(String statementDelivery)
    {
        this.statementDelivery = statementDelivery;
    }

    public final void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public final String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public final String getCompanyCode()
    {
        return companyCode;
    }

    public final void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }
}
