package com.magbel.util;

import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.magbel.util:
//            DataConnect

public class HtmlUtilily
{

    public HtmlUtilily()
    {
    }

    public String getResourcesOld(String selected, String query)
    {
        Connection mcon;
        PreparedStatement mps;
        ResultSet mrs;
        String html;
        mcon = null;
        mps = null;
        mrs = null;
        html = "";
//        String id = "";
        try
        {        
        if(selected == null)
        {
            selected = "";
        }
        mcon = (new DataConnect("ias")).getConnection();
        mps = mcon.prepareStatement(query);
        for(mrs = mps.executeQuery(); mrs.next();)
        {
            String id = mrs.getString(1);
            html = (new StringBuilder()).append(html).append("<option ").append(id.equals(selected) ? " selected " : "").append(" value='").append(id).append("'>").append(mrs.getString(2)).append("</option> ").toString();
        }

        try
        {
            if(mps != null)
            {
                mps.close();
            }
            if(mrs != null)
            {
                mrs.close();
            }
            if(mcon != null)
            {
                mcon.close();
            }
        }
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        }
	}catch(Exception ee){
        System.out.println((new StringBuilder()).append("WARNING::HtmlUtil:->").append(ee).toString());
	}
        try
        {
            if(mps != null)
            {
                mps.close();
            }
            if(mrs != null)
            {
                mrs.close();
            }
            if(mcon != null)
            {
                mcon.close();
            }
        }
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        }
        try
        {
            if(mps != null)
            {
                mps.close();
            }
            if(mrs != null)
            {
                mrs.close();
            }
            if(mcon != null)
            {
                mcon.close();
            }
        }
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        }    
        return html;
    }
    
    public String getResources(String selected, String query) {

        if (selected == null) {
            selected = "";
        }

        StringBuilder html = new StringBuilder();

        try {Connection con = new DataConnect("ias").getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);

                html.append("<option ")
                    .append(id.equals(selected) ? "selected " : "")
                    .append("value='")
                    .append(id)
                    .append("'>")
                    .append(name)
                    .append("</option>");
            }

        } catch (Exception e) {
            System.out.println("WARNING::HtmlUtil:-> " + e);
        }

        return html.toString();
    }

    public String getUnique(String epostId)
    {
        return (new StringBuilder()).append(epostId).append((new SimpleDateFormat("yyyyMMddhhmmSSSS")).format(new Date())).append(removeNdot((new StringBuilder()).append("").append(Math.random() * 10000000D).toString())).toString();
    }

    private String removeNdot(String tValue)
    {
        String decimalPart = "";
        String strValue = tValue;
        if(strValue.lastIndexOf(".") != -1)
        {
            decimalPart = strValue.substring(strValue.lastIndexOf(".") + 1);
            if(decimalPart.length() == 1)
            {
                decimalPart = (new StringBuilder()).append(decimalPart).append("0").toString();
            }
            strValue = (new StringBuilder()).append(strValue.substring(0, strValue.lastIndexOf("."))).append(decimalPart).toString();
        } else
        {
            strValue = (new StringBuilder()).append(strValue).append("00").toString();
        }
        return strValue;
    }

    public String findObject(String query)
    {

        String finder;

  //      ResultSet result = null;
        String found = null;
        finder = "UNKNOWN";
        double sequence = 0.0D;
        try
        {
        	Connection Con2 = (new DataConnect("ias")).getConnection();
        	Statement Stat = Con2.createStatement();
        for(ResultSet result = Stat.executeQuery(query); result.next();)
        {
            finder = result.getString(1);
        }

        try
        {}
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }
        } catch(Exception ee2){
        System.out.println((new StringBuilder()).append("WARNING::ERROR OBTAINING OBJ --> ").append(ee2).toString());
        ee2.printStackTrace();
	}
       
        return finder;

    }

    public String getResources2(String selected, String query)
    {

        String html;

        html = "";
       // String id = "";
        try
        {
        if(selected == null)
        {
            selected = "";
        }
        Connection mcon = (new DataConnect("ias")).getConnection();
        PreparedStatement mps = mcon.prepareStatement(query);
        for(ResultSet mrs = mps.executeQuery(); mrs.next();)
        {
            String id = mrs.getString(2);
            html = (new StringBuilder()).append(html).append("<option ").append(id.equals(selected) ? " selected " : "").append(" value='").append(id).append("'>").append(mrs.getString(2)).append("</option> ").toString();
        }

        try
        {}
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        }
	}catch(Exception ee){
        System.out.println((new StringBuilder()).append("WARNING::HtmlUtil:->").append(ee).toString());
	}
        try
        {}
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        }
        try
        {}
        catch(Exception closingError)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error cloing Connection->").append(closingError).toString());
        } 
        return html;
    }
    public double findintObject(String query)
    {

      //  double finder;

  //      ResultSet result = null;
        String found = null;
        double finder = 0.0D;
        double sequence = 0.0D;
        try
        {
        	Connection Con2 = (new DataConnect("ias")).getConnection();
        	Statement Stat = Con2.createStatement();
        for(ResultSet result = Stat.executeQuery(query); result.next();)
        {
            finder = result.getDouble(1);
        }
System.out.println("====finder====  "+finder); 
        try
        {
            if(Stat != null)
            {
                Stat.close();
            }
            if(Con2 != null)
            {
                Con2.close();
            }
        }
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }
        } catch(Exception ee2){
        System.out.println((new StringBuilder()).append("WARNING::ERROR OBTAINING OBJ --> ").append(ee2).toString());
        ee2.printStackTrace();
	}
        try
        {

        }
        catch(Exception errorClosing)
        {
            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
        }    
        return finder;

    }

    public String getCodeName(String query) {
		String result = "";

		int rs1 = 0;

	//	System.out.println("query===>> "+query);
		
		try {
			String validate = query.substring(0, 6);
//			con = getConnection();
			Connection con = (new DataConnect("ias")).getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			if(!validate.equalsIgnoreCase("UPDATE")){	
				ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1) == null ? "" : rs.getString(1);
			}
			}
			else{
				rs1 = ps.executeUpdate();
			}
		//	System.out.println("<<<<<<<<<<result: "+result);
	        try
	        {
	            if(con != null)
	            {
	            	con.close();
	            }   
	            
	        }
	        catch(Exception errorClosing)
	        {
	            System.out.println((new StringBuilder()).append("WARNING::Error Closing Connection >> ").append(errorClosing).toString());
	        }			
		} catch (Exception er) {
			System.out.println("Error in " + this.getClass().getName()
					+ "- getCodeName()... ->" + er.getMessage());
			er.printStackTrace();
		} 
		return result;
	}     

}