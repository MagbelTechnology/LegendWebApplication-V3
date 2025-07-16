package com.magbel.ia.bus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.magbel.ia.dao.PersistenceServiceDAO;
import com.magbel.ia.util.ApplicationHelper;

import com.magbel.ia.vao.LedgerGet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LedgerServiceBus extends PersistenceServiceDAO 
{

	ApplicationHelper helper;   
	public LedgerServiceBus() 
	{

		helper = new ApplicationHelper();
               
	}
	
	public ArrayList getLedger()
	{
        ArrayList list =new ArrayList();
        String query = "SELECT ledger_no,description FROM IA_GL_ACCT_LEDGER";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
        con = getConnection();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        while(rs.next())
			{
				com.magbel.ia.vao.LedgerGet po =new com.magbel.ia.vao.LedgerGet(rs.getString("ledger_no"),rs.getString("description"));
				list.add(po);
			}
        
        }

    
		catch(Exception er)
		{
                System.out.println("Error in fetching records... ->"+er);
        }
		finally
		{
                closeConnection(con,ps);
        }   
    
        return list;
	}
	
}

