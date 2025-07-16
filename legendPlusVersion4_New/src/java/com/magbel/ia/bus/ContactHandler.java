package  com.magbel.ia.bus;


import java.sql.*;
import java.text.SimpleDateFormat;
import  com.magbel.ia.vao.Contact;
//import com.magbel.ia.vao.MandatoryField;
//import com.magbel.ia.vao.ErrorCode;
import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;
import  java.util.ArrayList;


public class  ContactHandler extends PersistenceServiceDAO //com.magbel.ia.dao.Config{
  {
    SimpleDateFormat sdf;
    final String space = "  ";
    final String comma = ",";
    java.util.Date dat;
	private  ApplicationHelper helper;
   
			
	
    public   ContactHandler()
		{
        dat = new java.util.Date();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date  = dat.toString();
		helper = new ApplicationHelper();
        System.out.println("USING_ " + this.getClass().getName()+"\n Software written by" 
                               	+"  Ogey Bolaji. L @ " +date );
		System.out.println(" All rights reserved ");
		}
		
	
		
      public java.util.ArrayList   getAllContacts() {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact   contact = null;
		
		 Connection con = null;
		Statement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
            
			  con = getConnection();		 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_CONTACT   ORDER BY MTID ";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
               String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				/**
				contactInformation.setContactInformation(
				    mtId, contactCode, homePhone, homePhoneExt, businessPhone,
					businessPhoneExt,  faxNo, mobilePhone,  postalCode,  homeEmailAddress,  					   businessEmailAddress,         userId);
						
                records.add(contactInformation);
				**/
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    records.add(contact);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}





 public java.util.ArrayList   getContactsByCustomerCode(String   CustomerCode) {
        java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact   contact = null;
		
		 Connection con = null;
		Statement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
            
			  con = getConnection();		 
        String SELECT_QUERY  = " SELECT * FROM   IA_CUSTOMER_CONTACT   WHERE  CUSTOMER_CODE = '"+
		                              CustomerCode+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                
               String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//	String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				/**
				contactInformation.setContactInformation(
				    mtId, contactCode, homePhone, homePhoneExt, businessPhone,
					businessPhoneExt,  faxNo, mobilePhone,  postalCode,  homeEmailAddress,  					   businessEmailAddress,         userId);
						
                records.add(contactInformation);
				**/
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//	contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    records.add(contact);
		    }
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  records;
}








     public   Contact     getContactById(String MtId) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact    contact = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			  con = getConnection();		 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_CONTACT  WHERE MTID = '"+MtId+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
                   String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//		String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//		contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//		contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    //records.add(contact);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return    contact;
}




 public   Contact     getContactByContactCode(String ContactCode) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact    contact = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			  con = getConnection();		 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_CONTACT  WHERE  CONTACT_CODE  = '"+ContactCode+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
                   String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//		String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//	contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    //records.add(contact);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return    contact;
}




 public   Contact     getContactByCustomerCode(String CustomerCode) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact    contact = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			  con = getConnection();		 
        String SELECT_QUERY  = " SELECT * FROM  IA_CUSTOMER_CONTACT  WHERE  CUSTOMER_CODE  = '"+CustomerCode+"'";
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_QUERY);
		
        while (rs.next())
    		{
                   
                   String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//	String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
			
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//	contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    //records.add(contact);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return    contact;
}





 public java.util.ArrayList   getContactByQuery(String filter) {
        java.util.ArrayList records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact  contact = null;
		 String SELECT_QUERY  = null;
		if(filter != null)
		   { SELECT_QUERY = "SELECT * FROM  IA_CUSTOMER_CONTACT " + filter;	}
		else { SELECT_QUERY = "SELECT * FROM   IA_CUSTOMER_CONTACT ";  }

        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;

        try {
            con = getConnection();	
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
              while (rs.next())
    		{
                  String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerNo =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//	String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerNo);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//	contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    records.add(contact);
				}

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
       return records;
    }



	
    public final boolean createContact(Contact  contact)
	{
	   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		boolean  successfull =  false;
	  try{
	          		
		String INSERT_QUERY	 = "INSERT INTO   IA_CUSTOMER_CONTACT (MTID, CONTACT_CODE, "
		                    +" CUSTOMER_CODE, LAST_NAME, FIRST_NAME, MIDDLE_NAME," 
							+" HOME_PHONE,   BUSINESS_PHONE, BUSINESS_PHONE_EXT,  "
							+" PHONE1,  PHONE2, PHONE1_EXT,  PHONE2_EXT,  "
							+" FAX_NO, MOBILE_PHONE, POSTAL_CODE, HOME_EMAIL_ADDRESS,   "
							+" BUSINESS_EMAIL_ADDRESS,	 RELATIONSHIP_OFFICER, OPENING_REASON, "
							+"  USERID,  STATUS,  CREATE_DATE,  EFFECTIVE_DATE)  "
						+"	VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
					   
			
					String mtId = helper.getGeneratedId("IA_CUSTOMER_CONTACT");
					String   contactCode =  contact.getContactCode();
					String   customerNo  =  contact.getCustomerNo();
					String   lastName  =  contact.getLastName();
					String  firstName  =  contact.getFirstName();
					String  middleName  =  contact.getMiddleName();
					String  homePhone =  contact.getHomePhone();
					//String  homePhoneExt  =  contact.getHomePhoneExt();
					String businessPhone = contact.getBusinessPhone();
					String  businessPhoneExt  =  contact.getBusinessPhoneExt();
					String  phone1  =  contact.getPhone1();
					String  phone2   =  contact.getPhone2();
					String  phone1Ext  =  contact.getPhone1Ext();
					String  phone2Ext  =   contact.getPhone2Ext();
					String  faxNo  =  contact.getFaxNo();
					String  mobilePhone  =  contact.getMobilePhone();
					String  postalCode  =  contact.getPostalCode();
					String  homeEmailAddress =  contact.getHomeEmailAddress();
					String  businessEmailAddress =  contact.getBusinessEmailAddress();
					//		String  designation  =  contact.getDesignation();
					String  relationshipOfficer =  contact.getRelationshipOfficer();
					String  openingReason  =  contact.getOpeningReason();
					  int  userId =  contact.getUserId();
					  String  status  =  contact.getStatus();
					 String createDate =  		contact.getCreateDate();
				   String  effectiveDate =  contact.getEffectiveDate(); 

             con = getConnection();	
		ps = con.prepareStatement(INSERT_QUERY);
		
		ps.setString(1, mtId);
		ps.setString(2, contactCode);
		ps.setString(3, customerNo);
		ps.setString(4, lastName);
		ps.setString(5, firstName);
		ps.setString(6, middleName);
		ps.setString(7, homePhone);
		//		ps.setString(8, homePhoneExt);
		ps.setString(8, phone1);
		ps.setString(9,  phone2);
		ps.setString(10,  phone1Ext);
		ps.setString(11,  phone2Ext);
		ps.setString(12, businessPhone);
		ps.setString(13, businessPhoneExt);
		ps.setString(14, faxNo);
		ps.setString(15, mobilePhone);
			ps.setString(16, postalCode);
		ps.setString(17, homeEmailAddress);
		ps.setString(18, businessEmailAddress);
		ps.setString(19, relationshipOfficer);
		ps.setString(20,  openingReason);
		ps.setInt(21, userId);
		ps.setString(22,  status);
		ps.setDate(23,  dateConvert(createDate));
		ps.setDate(24,  dateConvert(effectiveDate));
		
		
			successfull  =   (ps.executeUpdate() != -1);
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }
	
	
	
	
	
	public final boolean updateContact(Contact    contact)
	{
	  Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
		boolean  successfull  =  false;
	  try{
	          	
		String UPDATE_QUERY = " UPDATE     IA_CUSTOMER_CONTACT  SET  CONTACT_CODE  = ?, "
					         +" CUSTOMER_CODE = ?,  LAST_NAME = ?, FIRST_NAME = ?,  MIDDLE_NAME = ?, "
                             +" HOME_PHONE = ?,   BUSINESS_PHONE = ?, BUSINESS_PHONE_EXT = ?,"
							 +"  PHONE1 = ?, PHONE2 = ?,  PHONE1_EXT = ?, PHONE2_EXT = ?, "
							 +"  FAX_NO = ?, MOBILE_PHONE = ?, POSTAL_CODE = ?,"
							 +" HOME_EMAIL_ADDRESS = ?,  BUSINESS_EMAIL_ADDRESS =  ?, "
							 +" RELATIONSHIP_OFFICER = ?, OPENING_REASON = ?,    "
							 +"  STATUS = ? 	 WHERE   MTID = ? ";
		                 
					String mtId = contact.getMtId();
					String   contactCode =   contact.getContactCode();
					String   customerNo  =  contact.getCustomerNo();
					String   lastName  =  contact.getLastName();
					String  firstName  =  contact.getFirstName();
					String  middleName  =  contact.getMiddleName();
					String  homePhone =  contact.getHomePhone();
					//	String  homePhoneExt  =  contact.getHomePhoneExt();
					String businessPhone = contact.getBusinessPhone();
					String  businessPhoneExt  =  contact.getBusinessPhoneExt();
					String  phone1  =  contact.getPhone1();
					String  phone2   =  contact.getPhone2();
					String  phone1Ext  =  contact.getPhone1Ext();
					String  phone2Ext  =   contact.getPhone2Ext();
					String  faxNo  =  contact.getFaxNo();
					String  mobilePhone  =  contact.getMobilePhone();
					String  postalCode  =  contact.getPostalCode();
					String  homeEmailAddress =  contact.getHomeEmailAddress();
					String  businessEmailAddress =  contact.getBusinessEmailAddress();
					//	String  designation  =  contact.getDesignation();
					String  relationshipOfficer =  contact.getRelationshipOfficer();
					String  openingReason  =  contact.getOpeningReason();
					//  int  userId =  contact.getUserId();
					  String  status  =  contact.getStatus();
					// String createDate =  contact.getCreateDate();
				  // String  effectiveDate =  contact.getEffectiveDate(); 
					
		
		  con = getConnection();	
		ps = con.prepareStatement(UPDATE_QUERY);
		
			
		ps.setString(1, contactCode);
		ps.setString(2, customerNo);
		ps.setString(3, lastName);
		ps.setString(4, firstName);
		ps.setString(5, middleName);
		ps.setString(6, homePhone);
		//ps.setString(7, homePhoneExt);
		ps.setString(7, businessPhone);
		ps.setString(8, businessPhoneExt);
		ps.setString(9,	phone1);
		ps.setString(10, phone2);
		ps.setString(11, phone1Ext);
		ps.setString(12, phone2Ext);
		ps.setString(13, faxNo);
		ps.setString(14, mobilePhone);
		
		ps.setString(15, postalCode);
		ps.setString(16, homeEmailAddress);
		ps.setString(17, businessEmailAddress);
		ps.setString(18, relationshipOfficer);
		ps.setString(19, openingReason);
		ps.setString(20,  status);
				
		ps.setString(21, mtId);
		
		successfull  =   (ps.executeUpdate() != -1);
						
	   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, ps, rs);
        }
       return  successfull;
    }
	
	

	

	
	
public void closeConnection(Connection c, Statement st, ResultSet rs)
{
  	c = null;  st = null;  rs = null;
	
}
	

public void closeConnection(Connection c, PreparedStatement pst, ResultSet rs)
{
  	c = null;  pst = null;  rs = null;
	
}







public boolean  isMtIdExisting(String MtId) {
        
         
		 String  SELECT_QUERY = "SELECT  count(MTID)  FROM   IA_CUSTOMER_CONTACT   WHERE MTID = '"+MtId+"'";	
		        
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		boolean exist = false;
         int mtId = 0;
        try {
             con = getConnection();	
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
			
			while (rs.next())
    		{
               mtId = rs.getInt("MTID");
			 }
			if(mtId != 0)
			  {  exist = true;	}
					    
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  exist;
}




public boolean  isContactCodeCodeExisting(String   ContactCode) {
        
         
		 String  SELECT_QUERY = "SELECT CONTACT_CODE   FROM   IA_CUSTOMER_CONTACT   WHERE CONTACT_CODE = '"+ContactCode+"'";	
		        
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		boolean exist = false;
         int icfId = 0;
		 String  contactCode =  null;
        try {
            con = getConnection();	
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_QUERY);
			
			while (rs.next())
    		{
               //icfId = rs.getString("IDENTIFICATION_CODE");
			   contactCode = rs.getString("CONTACT_CODE");
			 }
			if(contactCode.equalsIgnoreCase(ContactCode))
			//if(icfId == 0)
			  {  exist = true;	}
					    
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return  exist;
}



/**
public Connection getConnection2()
{
  Connection conn = null;
  try{
  
	Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
	conn = DriverManager.getConnection
		("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=IAS;User=sa;");
		
   }
   catch(Exception e)
		{ 
		 System.out.println("Connection exception:\n");
		 e.printStackTrace();
		}
  return  conn;
}
**/







 public   Contact     getContactByPhoneNo(String PhoneNo) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact    contact = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			  con = getConnection();		 
        
		String  SELECT_BY_PHONE_NO   = "select  *  from   IA_CUSTOMER_CONTACT  where  HOME_PHONE =   '"+PhoneNo+"'   or  BUSINESS_PHONE = '"+PhoneNo+"'   OR   MOBILE_PHONE = '"+PhoneNo+"'";
		 
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_BY_PHONE_NO);
		
        while (rs.next())
    		{
                   
                   String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//	String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE")); 
                       
				
                contact  = new  com.magbel.ia.vao.Contact();
				
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//		contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    //records.add(contact);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return    contact;
}





 public   Contact     getContactByEmailAddress(String EmailAddress) {
        //java.util.ArrayList  records = new java.util.ArrayList();
        com.magbel.ia.vao.Contact    contact = null;
   Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
	
		try {
           
			  con = getConnection();		
			  
   String    SELECT_BY_EMAIL_ADDRESS  = "select  *  from   IA_CUSTOMER_CONTACT  where  HOME_EMAIL_ADDRESS =   '"+ EmailAddress+"'   OR  	BUSINESS_EMAIL_ADDRESS = '"+ EmailAddress+"'";
		 
		stmt = con.createStatement();
		
        rs = stmt.executeQuery(SELECT_BY_EMAIL_ADDRESS);
		
        while (rs.next())
    		{
                   
                   String mtId = rs.getString("MTID");
				 String  contactCode = rs.getString("CONTACT_CODE");
				 String  customerCode =  rs.getString("CUSTOMER_CODE");
				 String  lastName  =  rs.getString("LAST_NAME");
				 String  firstName  =  rs.getString("FIRST_NAME");
				 String  middleName  =  rs.getString("MIDDLE_NAME");				 
                String  homePhone = rs.getString("HOME_PHONE");
				//String  homePhoneExt = rs.getString("HOME_PHONE_EXT");
		       String   businessPhone = rs.getString("BUSINESS_PHONE");
                String  businessPhoneExt = rs.getString("BUSINESS_PHONE_EXT");
				String  phone1  =  rs.getString("PHONE1");
				String  phone2  =   rs.getString("PHONE2");
				String  phone1Ext  	=	rs.getString("PHONE1_EXT");
				String  phone2Ext   =	rs.getString("PHONE2_EXT");
                String  faxNo = rs.getString("FAX_NO");
				String  mobilePhone  =  rs.getString("MOBILE_PHONE");
				String  postalCode  =  rs.getString("POSTAL_CODE");
                String  homeEmailAddress  =  rs.getString("HOME_EMAIL_ADDRESS");
				String  businessEmailAddress  =  rs.getString("BUSINESS_EMAIL_ADDRESS");
				//String  designation   =  rs.getString("DESIGNATION");
				String  relationshipOfficer =  rs.getString("RELATIONSHIP_OFFICER");
				String  openingReason  =  rs.getString("OPENING_REASON");
				int userId  =  rs.getInt("USERID");
				String  status =  rs.getString("STATUS");
				String  createDate = formatDate(rs.getDate("CREATE_DATE"));
				String  effectiveDate  =  formatDate(rs.getDate("EFFECTIVE_DATE"));                      
				
                contact  = new  com.magbel.ia.vao.Contact();
				
				contact.setMtId(mtId);
				contact.setContactCode(contactCode);
				contact.setCustomerNo(customerCode);
				contact.setLastName(lastName);
				contact.setFirstName(firstName);
				contact.setMiddleName(middleName);
				contact.setHomePhone(homePhone);
				//contact.setHomePhoneExt(homePhoneExt);
				contact.setBusinessPhone(businessPhone);
				contact.setBusinessPhoneExt(businessPhoneExt);
				contact.setPhone1(phone1);
				contact.setPhone2(phone2);
				contact.setPhone1Ext(phone1Ext);
				contact.setPhone2Ext(phone2Ext);
				contact.setFaxNo(faxNo);
				contact.setMobilePhone(mobilePhone);
				contact.setPostalCode(postalCode);
				contact.setHomeEmailAddress(homeEmailAddress);
				contact.setBusinessEmailAddress(businessEmailAddress);
				//	contact.setDesignation(designation);
				contact.setRelationshipOfficer(relationshipOfficer);
				contact.setOpeningReason(openingReason);
				contact.setUserId(userId);
				contact.setStatus(status);
				contact.setCreateDate(createDate);
				contact.setEffectiveDate(effectiveDate);
				
				    //records.add(contact);
			}
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return    contact;
}





public  boolean   isUniqueCode(String   ContactCode)
{

 boolean exists = false;
  boolean  unique = false;
   Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
		PreparedStatement ps = null;
		
  try {
  
  String SELECT_QUERY  = "SELECT count(CODE)  FROM   IA_CONTACT "
							+ "  WHERE  CODE = ?";
  
  
	      con = getConnection();
			ps = con.prepareStatement(SELECT_QUERY);
	
		
		ps.setString(1, ContactCode);
		
		rs = ps.executeQuery();
		
			while(rs.next()){
					int counted = rs.getInt(1);
				if(counted > 0){
				unique = false;
			 }   else {  unique = true; }
			}
	    
	
		
   } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closeConnection(con, stmt, rs);
        }
        return     unique;
}

			
}