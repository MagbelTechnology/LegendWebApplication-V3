package com.magbel.ia.bus;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import com.magbel.ia.vao.Customer;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerHandler extends PersistenceServiceDAO
{  
 
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    Date dat;
    String date;
    private ApplicationHelper helper;

    public CustomerHandler()
    {
        date = "";
        dat = new Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = dat.toString();
        helper = new ApplicationHelper();
        System.out.println((new StringBuilder()).append("USING_ ").append(getClass().getName()).toString());
    }

    public ArrayList getAllCustomers()
    {
        ArrayList records;
        Connection con;
        Statement stmt;
        ResultSet rs;
        records = new ArrayList();
        con = null;
        stmt = null;
        PreparedStatement ps = null;
        rs = null;
        try {
        con = getConnection();
        String SELECT_QUERY = " SELECT * FROM   IA_CUSTOMER  ORDER BY MTID ";
        stmt = con.createStatement();
        Customer customer;
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next(); records.add(customer))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
            String industry = rs.getString("INDUSTRY");
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            String occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
            customer.setOccupation(occupation);
        }

        } catch (Exception e) {e.printStackTrace();
		System.out.println("WARNING:Error creating IA_customer ->"
				+ e.getMessage());
	} finally {
		closeConnection(con, ps);
	}
        return records;
    }

    public Customer getCustomerById(String MtId)
    {
        Customer customer;
        Connection con;
        Statement stmt;
        ResultSet rs;
        customer = null;
        con = null;
        stmt = null;
        PreparedStatement ps = null;
        rs = null;
        try {
        con = getConnection();
        String SELECT_QUERY = (new StringBuilder()).append(" SELECT * FROM  IA_CUSTOMER  WHERE MTID = '").append(MtId).append("'").toString();
        stmt = con.createStatement();
        String occupation;
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next(); customer.setOccupation(occupation))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
            String industry = rs.getString("INDUSTRY");
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customer ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        return customer;
    }

    public Customer getCustomerByCustomerCode(String CustomerCode)
    {
        Customer customer;
        Connection con;
        Statement stmt;
        ResultSet rs;
        customer = null;
        con = null;
        stmt = null;
        PreparedStatement ps = null;
        rs = null;
        try {
        con = getConnection();
        String SELECT_QUERY = (new StringBuilder()).append(" SELECT * FROM  IA_CUSTOMER  WHERE CUSTOMER_CODE = '").append(CustomerCode).append("'").toString();
        stmt = con.createStatement();
        String occupation;
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next(); customer.setOccupation(occupation))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
    //        System.out.println("=====industryCode===>>> "+industryCode);             
            String industry = rs.getString("INDUSTRY");
     //       System.out.println("=====industry===>>> "+industry);            
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customerc ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        return customer;
    }

    public Customer getCustomerByCompanyCode(String CompanyCode)
    {
        Customer customer;
        Connection con;
        Statement stmt;
        ResultSet rs;
        customer = null;
        con = null;
        stmt = null;
        PreparedStatement ps = null;
        rs = null;
        try {
        con = getConnection();
        String SELECT_QUERY = (new StringBuilder()).append(" SELECT * FROM  IA_CUSTOMER  WHERE COMP_CODE = '").append(CompanyCode).append("'").toString();
        stmt = con.createStatement();
        String occupation;
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next(); customer.setOccupation(occupation))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
            String industry = rs.getString("INDUSTRY");
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customercS ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        return customer;
    }

    public ArrayList getCustomerByQuery(String query)
    {
        ArrayList records;
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        Statement stmt;
        records = new ArrayList();
        Customer customer = null;
        SELECT_QUERY = query;
        SELECT_QUERY = (new StringBuilder()).append(SELECT_QUERY).append(query).toString();
        con = null;
        rs = null;
        stmt = null;
        try {
        con = getConnection();
        stmt = con.createStatement();
        
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next(); records.add(customer))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
            String industry = rs.getString("INDUSTRY");
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            String occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
            customer.setOccupation(occupation);
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customerf ->"
			+ e.getMessage());
} finally {
	closeConnection(con,  stmt,rs);
}
        return records;
    }

    public final boolean createCustomer(Customer customer,String company_code)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        boolean successcustomer;
        con = null;
        rs = null;
        Statement stmt = null;
        ps = null;
        successcustomer = false;
        String INSERT_QUERY = "INSERT INTO   IA_CUSTOMER (MTID, CUSTOMER_CODE,  OLD_CUSTOMER_NO, CUSTOMER_CATEG" +
"ORY, CUSTOMER_TYPE,  LAST_NAME, FIRST_NAME, MIDDLE_NAME, GENDER,  CUSTOMER_NAME," +
" RESIDENCY, TITLE,  ADDRESS1, ADDRESS2,   CITY, STATE,  COUNTRY_CODE, COUNTRY, B" +
"RANCH_CODE, BRANCH,  INDUSTRY_CODE, INDUSTRY,   PHONE,PHONE_2,  FAX, EMAIL, STAT" +
"US,   CREATE_DATE, EFFECTIVE_DATE,  USERID,BIRTHDATE,COMP_CODE, OCCUPATION)  " +
"VALUES       (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
" "
;
        String mtId = helper.getGeneratedId("IA_CUSTOMER");
        String customerCode = customer.getCustomerCode();
        System.out.println("Customer Code in createCustomer : "+customerCode);
        String oldCustomerNo = customer.getOldCustomerNo();
        String customerCategory = customer.getCustomerCategory();
        String customerType = customer.getCustomerType();
        String lastName = customer.getLastName();
        String firstName = customer.getFirstName();
        String middleName = customer.getMiddleName();
        String gender = customer.getGender();
        String customerName = customer.getCustomerName();
        String residency = customer.getResidency();
        String title = customer.getTitle();
        String address1 = customer.getAddress1();
        String address2 = customer.getAddress2();
        String city = customer.getCity();
        String state = customer.getState();
        String countryCode = customer.getCountryCode();
        String country = customer.getCountry();
        String branchCode = customer.getBranchCode();
        String branch = customer.getBranch();
        String industryCode = customer.getIndustryCode();
        String industry = customer.getIndustry();
        String fax = customer.getFax();
        String email = customer.getEmail();
        String phone = customer.getPhone();
        String phone2 = customer.getPhone2();
        String status = customer.getStatus();
        String createDate = formatDate(new Date());
        String effectiveDate = customer.getEffectiveDate();
        if(effectiveDate == "" || effectiveDate == null)
        {
            effectiveDate = createDate;
        }
        int userId = customer.getUserId();
        String dateOfBirth = customer.getDateOfBirth();
        String companyCode = customer.getCompanyCode();
        String occupation = customer.getOccupation();
        try {
        con = getConnection();
        ps = con.prepareStatement(INSERT_QUERY);
        ps.setString(1, mtId);
        ps.setString(2, customerCode);
        ps.setString(3, oldCustomerNo);
        ps.setString(4, customerCategory);
        ps.setString(5, customerType);
        ps.setString(6, lastName);
        ps.setString(7, firstName);
        ps.setString(8, middleName);
        ps.setString(9, gender);
        ps.setString(10, customerName);
        ps.setString(11, residency);
        ps.setString(12, title);
        ps.setString(13, address1);
        ps.setString(14, address2);
        ps.setString(15, city);
        ps.setString(16, state);
        ps.setString(17, countryCode);
        ps.setString(18, country);
        ps.setString(19, branchCode);
        ps.setString(20, branch);
        ps.setString(21, industryCode);
        ps.setString(22, industry);
        ps.setString(23, phone);
        ps.setString(24, phone2);
        ps.setString(25, fax);
        ps.setString(26, email);
        ps.setString(27, status);
        ps.setDate(28, dateConvert(createDate));
        ps.setDate(29, dateConvert(effectiveDate));
        ps.setInt(30, userId);
        ps.setDate(31, dateConvert(dateOfBirth));
        ps.setString(32, company_code);
        ps.setString(33, occupation);
//        ps.setString(34, companycode);
        successcustomer = ps.executeUpdate() != -1;
        
        
    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customert ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
} 
        return successcustomer;
    }

    public String getCodeName(String query)
    {
        String result;
        Connection con;
        PreparedStatement ps;
        result = "";
        con = null;
        ResultSet rs = null;
        ps = null;
        try {
        con = getConnection();
        ps = con.prepareStatement(query);
        for( rs = ps.executeQuery(); rs.next();)
        {
            result = rs.getString(1) != null ? rs.getString(1) : "";
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customert ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        
        
        return result;
    }

    public final boolean updateCustomer(Customer customer)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        boolean successcustomer;
        con = null;
        rs = null;
        ps = null;
        successcustomer = false;
        String UPDATE_QUERY = " UPDATE     IA_CUSTOMER  SET  CUSTOMER_CODE = ?, OLD_CUSTOMER_NO = ?,    CUSTOME" +
"R_CATEGORY = ?,  CUSTOMER_TYPE = ?, LAST_NAME = ?, FIRST_NAME = ?,  MIDDLE_NAME " +
"= ?, GENDER = ?, CUSTOMER_NAME = ?,  RESIDENCY = ?,  TITLE = ?,  ADDRESS1 = ?, A" +
"DDRESS2 = ?,  CITY = ?, STATE = ?, COUNTRY_CODE = ?, COUNTRY = ?,  BRANCH_CODE =" +
" ?, BRANCH = ?,  INDUSTRY_CODE = ?,  INDUSTRY = ?,  PHONE = ?,PHONE_2 = ?, FAX =" +
" ?, EMAIL = ?,    STATUS = ?, CREATE_DATE = ?, EFFECTIVE_DATE = ?,  USERID = ?, " +
"BIRTHDATE =?,COMP_CODE =?, OCCUPATION =?\t\t\t WHERE   MTID = ? "
;
        String mtId = customer.getMtId();
        String customerCode = customer.getCustomerCode();
        String oldCustomerNo = customer.getOldCustomerNo();
        String customerCategory = customer.getCustomerCategory();
        String customerType = customer.getCustomerType();
        String lastName = customer.getLastName();
        String firstName = customer.getFirstName();
        String middleName = customer.getMiddleName();
        String gender = customer.getGender();
        String customerName = customer.getCustomerName();
        String residency = customer.getResidency();
        String title = customer.getTitle();
        String address1 = customer.getAddress1();
        String address2 = customer.getAddress2();
        String city = customer.getCity();
        String state = customer.getState();
        String countryCode = customer.getCountryCode();
        String country = customer.getCountry();
        String branchCode = customer.getBranchCode();
        String branch = customer.getBranch();
        String industryCode = customer.getIndustryCode();
        String industry = customer.getIndustry();
        String fax = customer.getFax();
        String email = customer.getEmail();
        String phone = customer.getPhone();
        String phone2 = customer.getPhone2();
        String status = customer.getStatus();
        String createDate = customer.getCreateDate();
        String effectiveDate = customer.getEffectiveDate();
        if(effectiveDate == "" || effectiveDate == null)
        {
            effectiveDate = createDate;
        }
        int userId = customer.getUserId();
        System.out.println("User Id: "+userId);
        String dateOfBirth = customer.getDateOfBirth();
        String companyCode = customer.getCompanyCode();
        String occupation = customer.getOccupation();
        
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        ps.setString(1, customerCode);
        ps.setString(2, oldCustomerNo);
        ps.setString(3, customerCategory);
        ps.setString(4, customerType);
        ps.setString(5, lastName);
        ps.setString(6, firstName);
        ps.setString(7, middleName);
        ps.setString(8, gender);
        ps.setString(9, customerName);
        ps.setString(10, residency);
        ps.setString(11, title);
        ps.setString(12, address1);
        ps.setString(13, address2);
        ps.setString(14, city);
        ps.setString(15, state);
        ps.setString(16, countryCode);
        ps.setString(17, country);
        ps.setString(18, branchCode);
        ps.setString(19, branch);
        ps.setString(20, industryCode);
        ps.setString(21, industry);
        ps.setString(22, phone);
        ps.setString(23, phone2);
        ps.setString(24, fax);
        ps.setString(25, email);
        ps.setString(26, status);
        ps.setDate(27, dateConvert(createDate));
        ps.setDate(28, dateConvert(effectiveDate));
        ps.setInt(29, userId);
        ps.setDate(30, dateConvert(dateOfBirth));
        ps.setString(31, companyCode);
        ps.setString(32, mtId);
        ps.setString(33, occupation);
        successcustomer = ps.executeUpdate() != -1;

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customerk->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        
        return successcustomer;
    }

    public final boolean updateCustomerStatus(Customer customer)
    {
        Connection con;
        ResultSet rs;
        PreparedStatement ps;
        boolean successcustomer;
        con = null;
        rs = null;
        ps = null;
        successcustomer = false;
        String UPDATE_QUERY = " UPDATE     IA_CUSTOMER  SET   STATUS = ?   \t WHERE   MTID = ? ";
        String mtId = customer.getMtId();
        String status = customer.getStatus();
        try {
        con = getConnection();
        ps = con.prepareStatement(UPDATE_QUERY);
        successcustomer = ps.execute();

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customeri ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        
        
        return successcustomer;
    }

  

    public boolean isUniqueCode(String CustomerCode)
    {
        boolean unique;
        Connection con;
        ResultSet rs;
        Statement stmt;
        boolean exists = false;
        unique = false;
        con = null;
        rs = null;
        stmt = null;
        PreparedStatement ps = null;
        String SELECT_QUERY = "SELECT count(CODE)  FROM   IA_CUSTOMER   WHERE  CODE = ?";
        try {
        con = getConnection();
         ps = con.prepareStatement(SELECT_QUERY);
        ps.setString(1, CustomerCode);
        for(rs = ps.executeQuery(); rs.next();)
        {
            int counted = rs.getInt(1);
            if(counted > 0)
            {
                unique = false;
            } else
            {
                unique = true;
            }
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customerg ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}

        return unique;
    }

    public boolean isCustomerCodeExisting(String CustomerCode)
    {
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        Statement stmt;
        boolean exist;
        int icfId;
        SELECT_QUERY = (new StringBuilder()).append("SELECT CUSTOMER_CODE  FROM   IA_CUSTOMER   WHERE  CUSTOMER_CODE = '").append(CustomerCode).append("'").toString();
        con = null;
        rs = null;
        stmt = null;
        exist = false;
        icfId = 0;
        try {
        con = getConnection();
        stmt = con.createStatement();
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next();)
        {
            icfId = rs.getInt("CUSTOMER_CODE");
        }

        if(icfId != 0)
        {
            exist = true;
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customerj ->"
			+ e.getMessage());
} finally {
	closeConnection(con,stmt,rs);
}
        
        return exist;
    }

    public boolean isMtIdExisting(String MtId)
    {
        String SELECT_QUERY;
        Connection con;
        ResultSet rs;
        Statement stmt;
        boolean exist;
        int icfId;
        SELECT_QUERY = (new StringBuilder()).append("SELECT count(MTID)  FROM   IA_CUSTOMER   WHERE MTID = '").append(MtId).append("'").toString();
        con = null;
        rs = null;
        stmt = null;
        exist = false;
        icfId = 0;
        try {
        con = getConnection();
        stmt = con.createStatement();
        for(rs = stmt.executeQuery(SELECT_QUERY); rs.next();)
        {
            icfId = rs.getInt("MTID");
        }

        if(icfId != 0)
        {
            exist = true;
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_customedS ->"
			+ e.getMessage());
} finally {
	closeConnection(con,stmt,rs);
}
        
        return exist;
    }

    public Customer getCustomerByCustomerName(String LastName, String FirstName)
    {
        Customer customer;
        Connection con;
        Statement stmt;
        ResultSet rs;
        customer = null;
        con = null;
        stmt = null;
        PreparedStatement ps = null;
        rs = null;
        try {
        con = getConnection();
        String SEARCH_BY_NAME = (new StringBuilder()).append("select  *  from   IA_CUSTOMER  where  LAST_NAME =   '").append(LastName).append("'   and  FIRST_NAME = '").append(FirstName).append("'").toString();
        stmt = con.createStatement();
        String occupation;
        for(rs = stmt.executeQuery(SEARCH_BY_NAME); rs.next(); customer.setOccupation(occupation))
        {
            String mtId = rs.getString("MTID");
            String customerCode = rs.getString("CUSTOMER_CODE");
            String oldCustomerNo = rs.getString("OLD_CUSTOMER_NO");
            String customerCategory = rs.getString("CUSTOMER_CATEGORY");
            String customerType = rs.getString("CUSTOMER_TYPE");
            String lastName = rs.getString("LAST_NAME");
            String firstName = rs.getString("FIRST_NAME");
            String middleName = rs.getString("MIDDLE_NAME");
            String gender = rs.getString("GENDER");
            String customerName = rs.getString("CUSTOMER_NAME");
            String residency = rs.getString("RESIDENCY");
            String title = rs.getString("TITLE");
            String address1 = rs.getString("ADDRESS1");
            String address2 = rs.getString("ADDRESS2");
            String city = rs.getString("CITY");
            String state = rs.getString("STATE");
            String countryCode = rs.getString("COUNTRY_CODE");
            String country = rs.getString("COUNTRY");
            String branchCode = rs.getString("BRANCH_CODE");
            String branch = rs.getString("BRANCH");
            String industryCode = rs.getString("INDUSTRY_CODE");
            String industry = rs.getString("INDUSTRY");
            String phone = rs.getString("PHONE");
            String phone2 = rs.getString("PHONE_2");
            String fax = rs.getString("FAX");
            String email = rs.getString("EMAIL");
            String status = rs.getString("STATUS");
            String createDate = formatDate(rs.getDate("CREATE_DATE"));
            String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
            int userId = rs.getInt("USERID");
            String dateOfBirth = formatDate(rs.getDate("BIRTHDATE"));
            String companyCode = rs.getString("COMP_CODE");
            occupation = rs.getString("OCCUPATION");
            customer = new Customer();
            customer.setMtId(mtId);
            customer.setCustomerCode(customerCode);
            customer.setOldCustomerNo(oldCustomerNo);
            customer.setCustomerCategory(customerCategory);
            customer.setCustomerType(customerType);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setMiddleName(middleName);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setResidency(residency);
            customer.setTitle(title);
            customer.setAddress1(address1);
            customer.setAddress2(address2);
            customer.setPhone(phone);
            customer.setPhone2(phone2);
            customer.setFax(fax);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setCountryCode(countryCode);
            customer.setBranch(branch);
            customer.setBranchCode(branchCode);
            customer.setIndustry(industry);
            customer.setIndustryCode(industryCode);
            customer.setCreateDate(createDate);
            customer.setEffectiveDate(effectiveDate);
            customer.setStatus(status);
            customer.setUserId(userId);
            customer.setDateOfBirth(dateOfBirth);
            customer.setCompanyCode(companyCode);
        }

    } catch (Exception e) {e.printStackTrace();
	System.out.println("WARNING:Error creating IA_IcustS ->"
			+ e.getMessage());
} finally {
	closeConnection(con, ps);
}
        return customer;
    }
}
