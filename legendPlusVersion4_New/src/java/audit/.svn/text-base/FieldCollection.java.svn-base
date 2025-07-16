package audit;


import java.sql.*;




public  class FieldCollection extends Bag //throws Exception
{
   private Field field;
   private String columnname;
   private   int cType = 0;  private int colIndex = 0;
    private Object[] reslt;
 private String[] columnnames;
 private int[]  columntypes;



    public FieldCollection(String Name) throws Exception {

    }




   public void updateInserts(String updatestmt, String columnName)
   {
     try {
 legend.ConnectionClass ccc = new legend.ConnectionClass();
       Connection con =  ccc.getConnection();//
   PreparedStatement pstmnt = con.prepareStatement(updatestmt); //.executeQuery(selectstmt);						//
	    ResultSetMetaData rsmd = pstmnt.getMetaData(); // ResultMetaData rsmd = pstmnt.getParameterMetaData();									//
	 int numColumns = rsmd.getColumnCount();										//
	 Object[] result = new Object[numColumns];
	  String[]  columnNames = new String[numColumns];
	 int[] columnTypes = new int[numColumns];
	 // Get the column names; column indices start from 1
     for (int i=1; i < numColumns + 1; i++)
		{
          columnNames[i] = rsmd.getColumnName(i);
		  columnTypes[i] = rsmd.getColumnType(i);
		}
		this.columnnames = columnNames;
		this.columntypes = columnTypes;
		for(int i = 0; i < columnNames.length; i++)
		 {
		   if(columnNames[i]  == columnName)
		      {
			    cType = columnTypes[i];
				colIndex = i;
			  }

		   //varchar
		   if(cType == 12)
		   { String val = field.getNewValue(); pstmnt.setString(i,  val);  }
			//float
			else if(cType == 6)
			{   String tmp = field.getNewValue();   float val = Float.parseFloat(tmp);  pstmnt.setFloat(i, val); }
			//integer
			else if(cType == 4)
			{    String tmp = field.getNewValue();   int val = Integer.parseInt(tmp); pstmnt.setInt(i, val); }
			//distinct
			else if(cType == 2001)
			{  String tmp = field.getNewValue();   int val = Integer.parseInt(tmp); pstmnt.setInt(i, val); }
			//date
			else if(cType == 91)
			{  Date dt = field.getDate(); pstmnt.setDate(i, dt) ;  }
			//char
			else if(cType == 1)
			{  String val = field.getNewValue(); pstmnt.setString(i, val);  }
			//blob
			else if(cType == 2004)
			{  byte[] val = field.getImage(); pstmnt.setBytes(i, val); }
			//clob
			else if(cType == 2005)
			{  byte[] val = field.getImage(); pstmnt.setBytes(i, val);  }
			//double
			else if(cType == 8)
			{    String tmp = field.getNewValue();   double val = Double.parseDouble(tmp);
			                           pstmnt.setDouble(i, val);  }
			//decimal => integer
			else if(cType == 3)
			{   String tmp = field.getNewValue();   long val = Long.parseLong(tmp);  pstmnt.setLong(i,  val); }
			//bigint => integer
			else if(cType == -5)
			{   String tmp = field.getNewValue();   int val = Integer.parseInt(tmp);  pstmnt.setInt(i,  val);  }
			//real
			else if(cType == 7)
			{  String tmp = field.getNewValue();   long val = Long.parseLong(tmp);  pstmnt.setLong(i,  val);  }
			//smallint => short
			else if(cType == 5)
			{  String tmp = field.getNewValue();   short val = Short.parseShort(tmp); pstmnt.setShort(i, val);  }
			//timyint
			else if(cType == -6)
			{   String tmp = field.getNewValue();   byte val = Byte.parseByte(tmp); pstmnt.setByte(i, val);  }
			// time
			else if(cType == 92)
			{ Time tm = field.getTime(); pstmnt.setTime(i, tm);  }
			// timestamp
			else if(cType == 93)
			{  Timestamp tmsp = field.getTimestamp(); pstmnt.setTimestamp(i, tmsp);  }
			//numeric
			else if(cType == 2)
			{  String tmp = field.getNewValue();   int val = Integer.parseInt(tmp);  pstmnt.setInt(i,  val); }
			//binary
			else if(cType == -2)
			{  String tmp = field.getNewValue();   boolean val = Boolean.parseBoolean(tmp); pstmnt.setBoolean(i, val); }
			//bit
			else if(cType == -7)
			{  String tmp = field.getNewValue();   boolean val = Boolean.parseBoolean(tmp); pstmnt.setBoolean(i, val);  }
	    }
        pstmnt.executeUpdate();
	   }catch(Exception e){ e.printStackTrace(); }

   }



  public String[] getColumnNames()
  {
     return this.columnnames;
  }

  public String getColumnName()
   {
      return this.columnname;
   }



  }
