package audit;

import java.util.*;

public class DocFieldBean extends java.util.HashMap
{
  private String name;
  StringBuffer mandatoryfieldnulls = new StringBuffer();
  private Map<String, Field>  container = new HashMap<String, Field>();
  
  public final Map getMandatoryFields()
   {
     Map mandatoryFields = new HashMap();
	Iterator it = this.values().iterator(); 
	 while(it.hasNext())
	  { 
	    Field tmf = new Field("");
		      tmf = (Field)it.next();
		if (tmf.isMandatory() == false)
		 {
		      mandatoryFields.put(tmf.getName(),tmf);
		  if(tmf.getNewValue() == null)
		    {
			 mandatoryfieldnulls.append("'" );
			 mandatoryfieldnulls.append(tmf.getName());
			 mandatoryfieldnulls.append("', " );
			 }
		 }
	  }
	 return mandatoryFields;
	}
  
  
  public final Map getChangedFields()
   {
     
	 Map changedFields = new HashMap();
	Iterator it = this.values().iterator(); 
	 while(it.hasNext())
     	  {
	   Field tmf = new Field("");
	         tmf = (Field)it.next();
	    if(tmf.isChanged())
		   {
		    changedFields.put(tmf.getName(),tmf);
		   }
	   }
	  return changedFields;
	}
		

  
   
    public final String getMandatoryFieldNulls()
	  {
	    if( mandatoryfieldnulls != null)
		   return mandatoryfieldnulls.toString();
		else
		   return null;
	   }
	
	
   public final Map getDirtyFields()
   {
     
     Map dirtyFields = new HashMap();
	 Iterator it = this.values().iterator(); 
	 while(it.hasNext())
	  {
	   Field tmf = new Field("");
	         tmf = (Field)it.next();
	    if(tmf.isDirty())
		   {
		    dirtyFields.put((String)tmf.getName(),tmf);
		   }
	   }
	  
	  return dirtyFields; 
	  	
	}

	
	public final void setName(String Name)
	{
	 this.name = Name;
	 }
	 
	 
	public final String  getName()
	{
	 return this.name;
	 }
	 
	 public final void addField(Field Fld)
	  {
	  try{
	    this.put(Fld.getName(),Fld);
		} catch(Exception e) {e.printStackTrace();}
	}
	
}
   		
			 
	 
  
  

  