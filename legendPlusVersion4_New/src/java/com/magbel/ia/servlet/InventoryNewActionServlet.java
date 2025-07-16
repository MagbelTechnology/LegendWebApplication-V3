package com.magbel.ia.servlet;

//import com.magbel.ia.bus.SalesOrderServiceBus;

import com.magbel.ia.bus.InventoryBus;
import com.magbel.util.DatetimeFormat;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import magma.util.Codes;

public class InventoryNewActionServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private InventoryBus serviceBus;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        serviceBus = new InventoryBus();
    }
    public void destroy() {

    }
   
    public void service(HttpServletRequest request,HttpServletResponse response) 
                throws ServletException, IOException {
                
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        response.setContentType("text/html");

        java.text.SimpleDateFormat sdf; 
      
        String branch_id = request.getParameter("branch_id");
        String department_id = request.getParameter("department_id");
        String category_id = request.getParameter("category_id");
        String depreciation_start_date = request.getParameter("depreciation_start_date");
        String depreciation_end_date = request.getParameter("depreciation_end_date");
        String posting_date = request.getParameter("posting_date");
        String make = request.getParameter("make"); 
        String location = request.getParameter("location");  
        String maintained_by = request.getParameter("maintained_by"); 
        String authorized_by = request.getParameter("authorized_by");   
        String supplied_by = request.getParameter("supplied_by");   
        String reason = request.getParameter("reason");   
        String inventory_id = request.getParameter("inventory_id");   
        String description = request.getParameter("description");   
        String vendor_account = request.getParameter("vendor_account");   
        String cost_price = request.getParameter("cost_price");   
        String vatable_cost = request.getParameter("vatable_cost");  
        String vat_amount = request.getParameter("vat_amount");   
        String serial_number = request.getParameter("serial_number");   
        String engine_number = request.getParameter("engine_number");   
        String model = request.getParameter("model");   
        String user = request.getParameter("user");  
        String depreciation_rate = request.getParameter("depreciation_rate");   
        String residual_value = request.getParameter("residual_value");   
        String subject_to_vat = request.getParameter("subject_to_vat");   
        String date_of_purchase = request.getParameter("date_of_purchase");   
        String registration_no = request.getParameter("registration_no");   
        String require_depreciation = request.getParameter("require_depreciation"); 
        String who_to_remind = request.getParameter("who_to_remind");   
        String email_1 = request.getParameter("email_1"); 
        String email2 = request.getParameter("email2");   
        String raise_entry = request.getParameter("raise_entry");   
        String section = request.getParameter("section");  
        String user_id = request.getParameter("user_id");   
        String state = request.getParameter("state");   
        String driver = request.getParameter("driver");   
        String who_to_remind_2 = request.getParameter("who_to_remind_2");  
        String spare_1 = request.getParameter("spare_1");   
        String spare_2 = request.getParameter("spare_2");  
        
        String accum_depS= request.getParameter("accum_dep");  
        accum_depS= accum_depS.replaceAll(",","");
        double accum_dep=Double.parseDouble(accum_depS);
         
        String section_id = request.getParameter("section_id");  
        String wh_tax_cb = request.getParameter("wh_tax_cb");   
        String wh_tax_amount = request.getParameter("wh_tax_amount");   
        String require_redistribution = request.getParameter("require_redistribution");   
        String status = request.getParameter("status");  
        String province = request.getParameter("province");   
        String multiple = request.getParameter("multiple");   
        String warrantyStartDate = request.getParameter("warrantyStartDate");   
        String noOfMonths = request.getParameter("noOfMonths");   
        String expiryDate = request.getParameter("expiryDate");   
        String amountPTD = request.getParameter("amountPTD");   
        String amountREM = request.getParameter("amountREM");   
        String partPAY = request.getParameter("partPAY");   
        String fullyPAID = request.getParameter("fullyPAID");   
        String group_id = request.getParameter("group_id"); 
        String auth_user = request.getParameter("auth_user");  
        String authuser = request.getParameter("authuser");   
        String dateFormat = request.getParameter("dateFormat");   
        String bar_code = request.getParameter("bar_code");   
        String sbu_code = request.getParameter("sbu_code");   
        String lpo = request.getParameter("lpo");   
        String supervisor = request.getParameter("supervisor");   
        String deferPay = request.getParameter("deferPay");   
        
        String selectTaxS = request.getParameter("selectTax");   
        selectTaxS= selectTaxS.replaceAll(",","");
        int selectTax=Integer.parseInt(selectTaxS);
        
        String Item_TypeCode = request.getParameter("Item_TypeCode"); 
        String Warehouse_Code = request.getParameter("Warehouse_Code");
        String compCode = request.getParameter("compCode");
        String numOfTransactionLevel = request.getParameter("numOfTransactionLevel");
         
        
        System.out.println("-branch_id-"+ branch_id ); 
        System.out.println("-department_id-"+ department_id ); 
        System.out.println("-category_id-"+ category_id);
        System.out.println("-depreciation_start_date-"+ depreciation_start_date); 
        System.out.println("-depreciation_end_date-"+ depreciation_end_date);
        System.out.println("-posting_date-"+ posting_date );
        System.out.println("-make-"+ make );
        System.out.println("-location-"+ location ); 
        System.out.println("-maintained_by-"+ maintained_by ); 
        System.out.println("-authorized_by-"+ authorized_by );  
        System.out.println("-supplied_by-"+ supplied_by );
        System.out.println("-reason-"+ reason ); 
        System.out.println("-inventory_id-"+ inventory_id ); 
        System.out.println("-description-"+ description); 
        System.out.println("-vendor_account-"+ vendor_account );
        System.out.println("-cost_price-"+ cost_price); 
        System.out.println("-vatable_cost-"+ vatable_cost );
        System.out.println("-vat_amount-"+ vat_amount ); 
        System.out.println("-serial_number-"+ serial_number);
        System.out.println("-engine_number-"+ engine_number );
        System.out.println("-model-"+ model); 
        System.out.println("-user-"+ user );
        System.out.println("-depreciation_rate-"+ depreciation_rate );  
        System.out.println("-residual_value-"+ residual_value);  
        System.out.println("-subject_to_vat-"+ subject_to_vat ); 
        System.out.println("-date_of_purchase-"+ date_of_purchase );
        System.out.println("-registration_no-"+ registration_no);   
        System.out.println("-require_depreciation-"+ require_depreciation );
        System.out.println("-who_to_remind-"+ who_to_remind ); 
        System.out.println("-email_1-"+ email_1 ); 
        System.out.println("-email2-"+ email2 );  
        System.out.println("-raise_entry-"+ raise_entry ); 
        System.out.println("-section-"+ section );
        System.out.println("-user_id-"+ user_id ); 
        System.out.println("-state-"+ state );
        System.out.println("-driver-"+ driver );
        System.out.println("-who_to_remind_2-"+ who_to_remind_2 );
        System.out.println("-spare_1-"+ spare_1 );
        System.out.println("-spare_2-"+ spare_2); 
         System.out.println("-accum_dep-"+ accum_dep); 
        System.out.println("-section_id-"+ section_id);
        System.out.println("-wh_tax_cb-"+ wh_tax_cb );
        System.out.println("-wh_tax_amount-"+ wh_tax_amount);
        System.out.println("-require_redistribution-"+ require_redistribution);  
        System.out.println("-status-"+ status );
        System.out.println("-province-"+ province); 
        System.out.println("-multiple-"+ multiple); 
        System.out.println("-warrantyStartDate-"+ warrantyStartDate);
        System.out.println("-noOfMonths-"+ noOfMonths );
        System.out.println("-expiryDate-"+ expiryDate );
        System.out.println("-amountPTD-"+ amountPTD);
        System.out.println("-amountREM-"+ amountREM );   
        System.out.println("-partPAY-"+ partPAY );
        System.out.println("-fullyPAID-"+ fullyPAID ); 
        System.out.println("-group_id-"+ group_id );
        System.out.println("-auth_user-"+ auth_user);
        System.out.println("-authuser-"+ authuser );
        System.out.println("-dateFormat-"+ dateFormat);
        System.out.println("-bar_code-"+ bar_code );
        System.out.println("-sbu_code-"+ sbu_code );  
        System.out.println("-lpo-"+ lpo ); 
        System.out.println("-supervisor-"+ supervisor );
        System.out.println("-deferPay-"+ deferPay);
        System.out.println("-selectTax-"+ selectTax);
         System.out.println("-Item_TypeCode-"+ Item_TypeCode);
        System.out.println("-Warehouse_Code-"+ Warehouse_Code);
        System.out.println("-compCode-"+ compCode);
        System.out.println("-numOfTransactionLevel-"+ numOfTransactionLevel);
        
        try
        {
        	
        	//Check for approval level before insert operation
        	if(numOfTransactionLevel=="0")
        	{
        		//Check if item exist in inventory total
        	if(serviceBus.checkInventoryTotal(Item_TypeCode, Warehouse_Code))
        	{
        		//Insert into ia inventory
        		if( serviceBus.rinsertAssetRecord(branch_id ,   department_id ,   category_id,
                depreciation_start_date,   depreciation_end_date,  posting_date ,
                make ,  location ,   maintained_by ,   authorized_by ,  
                supplied_by ,  reason ,   inventory_id ,   description, 
                vendor_account ,  cost_price,   vatable_cost ,  vat_amount , 
                serial_number,  engine_number ,  model,   user ,
                depreciation_rate ,  
                residual_value,    subject_to_vat ,   date_of_purchase ,  registration_no,   
                require_depreciation ,  who_to_remind ,   email_1 ,   email2 ,  
                raise_entry ,   section ,  user_id ,   state ,  driver ,
                who_to_remind_2 ,  
                spare_1 ,  spare_2,   accum_dep,   section_id,
                wh_tax_cb ,  wh_tax_amount,  require_redistribution,  
                status ,  province,   multiple,   warrantyStartDate,
                noOfMonths ,  expiryDate ,  amountPTD,  amountREM ,   
                partPAY ,  fullyPAID ,   group_id ,  auth_user,
                authuser ,  dateFormat,  bar_code ,  sbu_code ,  
                lpo ,   supervisor ,  deferPay,  selectTax, Item_TypeCode, Warehouse_Code,compCode))
        			//Update inventory total
        			if(serviceBus.updateInventoryTotal(Item_TypeCode, Warehouse_Code))
        				//Insert into inventory history
        				serviceBus.createInventoryHistory(Item_TypeCode, Warehouse_Code, user_id);
        		 
        	}
        	else
        	{
        		//Insert into ia inventory
        		if(serviceBus.rinsertAssetRecord(branch_id ,   department_id ,   category_id,
                        depreciation_start_date,   depreciation_end_date,  posting_date ,
                        make ,  location ,   maintained_by ,   authorized_by ,  
                        supplied_by ,  reason ,   inventory_id ,   description, 
                        vendor_account ,  cost_price,   vatable_cost ,  vat_amount , 
                        serial_number,  engine_number ,  model,   user ,
                        depreciation_rate ,  
                        residual_value,    subject_to_vat ,   date_of_purchase ,  registration_no,   
                        require_depreciation ,  who_to_remind ,   email_1 ,   email2 ,  
                        raise_entry ,   section ,  user_id ,   state ,  driver ,
                        who_to_remind_2 ,  
                        spare_1 ,  spare_2,   accum_dep,   section_id,
                        wh_tax_cb ,  wh_tax_amount,  require_redistribution,  
                        status ,  province,   multiple,   warrantyStartDate,
                        noOfMonths ,  expiryDate ,  amountPTD,  amountREM ,   
                        partPAY ,  fullyPAID ,   group_id ,  auth_user,
                        authuser ,  dateFormat,  bar_code ,  sbu_code ,  
                        lpo ,   supervisor ,  deferPay,  selectTax, Item_TypeCode, Warehouse_Code,compCode))
        			//Insert into ia inventory total
        			    if(serviceBus.createInventoryTotal(Item_TypeCode, Warehouse_Code, user_id,compCode))
        			    	//Insert into ia inventory history
        			    	serviceBus.createInventoryHistory(Item_TypeCode, Warehouse_Code, user_id);
        		                out.print("<script>window.document.location='DocumentHelp.jsp?np=newStock'</script>");
        		}
    		   
         out.print("<script>window.document.location='DocumentHelp.jsp?np=newStock'</script>");
        	}
        	// Insert into temporary table for further action
        	else 
        	{
        			serviceBus.rinsertAssetRecord(branch_id ,   department_id ,   category_id,
                    depreciation_start_date,   depreciation_end_date,  posting_date ,
                    make ,  location ,   maintained_by ,   authorized_by ,  
                    supplied_by ,  reason ,   inventory_id ,   description, 
                    vendor_account ,  cost_price,   vatable_cost ,  vat_amount , 
                    serial_number,  engine_number ,  model,   user ,
                    depreciation_rate ,  
                    residual_value,    subject_to_vat ,   date_of_purchase ,  registration_no,   
                    require_depreciation ,  who_to_remind ,   email_1 ,   email2 ,  
                    raise_entry ,   section ,  user_id ,   state ,  driver ,
                    who_to_remind_2 , spare_1 ,  spare_2,   accum_dep,   section_id,
                    wh_tax_cb ,  wh_tax_amount,  require_redistribution,  
                    status ,  province,   multiple,   warrantyStartDate,
                    noOfMonths ,  expiryDate ,  amountPTD,  amountREM ,   
                    partPAY ,  fullyPAID ,   group_id ,  auth_user,
                    authuser ,  dateFormat,  bar_code ,  sbu_code ,  
                    lpo ,   supervisor ,  deferPay,  selectTax, Item_TypeCode, Warehouse_Code,compCode,numOfTransactionLevel);
        			
        	out.print("<script>window.document.location='DocumentHelp.jsp?np=newStock'</script>");
        	}
        }
        catch(Throwable e)
        {e.printStackTrace();}
              
                    
        
    }
    
    public String getServletInfo() {
            return "AP Action Servlet";
    }
}
