package audit;

import java.sql.*;


/**
 * <p>Title: Apex</p>
 * <p>Description: LEGEND Fixed Asset Management System </p>
 * <p>Copyright: Copyright (c) 2006. All rights reserved.</p>
 * <p>Company: Magbel Technologies Limited.</p>
*  @author  Bolaji L. Ogeyingbo.
 * @version 1.0
 */
public final class Field extends Resource implements AuditControl 
{
    private int columntype = 0;
	private String columnname = "";
	private int columnid = 0;
   
  private  boolean dirty = false;
  private  boolean mandatory = false;
  private boolean changed = false;
  private String newvalue ;
   private String oldvalue ;
    private String actionPerformed;
	private byte[] image;
	private byte flags;
	
	private Blob blob;
	private Time time;
	private Date date;
	private Timestamp timestamp;
	
	private Object newObject;
   private Object oldObject;
   private String tablename;
  
	
	
	
	
  public Field(String Name)
  {
      this.setName(Name);
  }

   
   public final  Field createField(String Name, String Oldvalue, String ActionPerformed, String User)
   {
     this.setName(Name);
	 this.oldvalue = Oldvalue;
	 this.actionPerformed = ActionPerformed;
	 this.setUser(User);
	 
	 return this;
    }
  
  
  public final boolean isChanged()
   {
   boolean changedt = false;
    if(newvalue != null || oldvalue != null)
	 {
      if( newvalue.equalsIgnoreCase(oldvalue))
		  changedt = false;
	     else 
	       changedt = true;
	  } 
	  return changedt;
   }
   
   
   
   public final boolean isMandatory()
   {   return this.mandatory;     }
   
   
   public final boolean isDirty()
   {     return this.dirty;     }
   
   
   public final void markDirty()
   {     this.dirty = true;     }
   
   
   public final void setChanged()
    {    this.changed = true;  	}
	
	
   public final void setMandatory()
    {  	 this.mandatory = true;  	}	
	
   
   public  final void setNewValue(String NewValue)
   {  
       this.newvalue = NewValue;
	   //this.newvalue = String.valueOf(NewObject);  
   }
   
   
   public final String getNewValue()
   {   return this.newvalue;       }
   
   
     public final Object getNewObject()
   {   return this.newObject;       }
   
   
   public final void setOldValue(String OldValue)
   {    
        this.oldvalue = OldValue;
        //this.oldvalue = String.valueOf(oldObject);    
    }
   
   
   
   public final String getOldValue()
   {    return this.oldvalue;      }
   
   
    public final Object getOldObject()
   {   return this.oldObject;       }
   
   
   public final void setActionPerformed(String ActionPerformed)
    {      this.actionPerformed = ActionPerformed;    }
	
	 
	public  final String getActionPerformed()
	 {     return this.actionPerformed;     }
	 
   
   public final void setImage(byte[] Image)
     {    this.image = Image;      }
	 
	  
	public final byte[] getImage()
	 {    return this.image;   	 }
	 
	 
	 public final void setFlags(byte Flags)
	  {    this.flags = Flags;    } 
	  
	  
	public final byte getFlags()
	  {   return this.flags;   }
	  
	  
	 public final Blob getBlob()
	 {   return this.blob;   }
	 
	 
	 public final Time getTime()
	 {   return this.time;   }
	 
	 
	 public final Date getDate()
	   {   return this.date;    }
	 
	
	 
	 public final Timestamp getTimestamp()
	   {  return this.timestamp;  }
	   
	   
	   public final void setColumnType(int SqlType)
	   {  this.columntype = SqlType;   }
	   
	   
	   public final int getColumnType()
	   {  return this.columntype;  }
	   
	   
	   public final void setColumnName(String ColumnName)
	   {  this.columnname = ColumnName;   }
	   
	   
	   public final String getColumnName()
	    {  return this.columnname;  }
  	   
	    public final void setName(String ColumnName)
	   {  this.columnname = ColumnName;   }
	   
	   
	   
	    public final String getName()
	    {  return this.columnname;  }
		
		
		public final int getColumnId()
		 { return this.columnid;  }
		 
		 
		public final void setColumnId(int columnId)
		 { this.columnid = columnId;  }
		
		
		public final void setTableName(String tabName)
		 {  this.tablename = tabName;  }
		 
		public final String getTableName()
		 {   return this.tablename;  }
	   
}