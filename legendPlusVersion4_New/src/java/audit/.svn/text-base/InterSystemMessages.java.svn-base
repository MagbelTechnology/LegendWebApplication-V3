package audit;

public final class InterSystemMessages implements ISMInterface
 {
  private short intsysmessgflags = 0;
  private StringBuffer alertmessg = new StringBuffer();
  private StringBuffer errormessg = new StringBuffer();
  private StringBuffer systemerrmessg = new StringBuffer();
  private StringBuffer dataconnecterrmessg = new StringBuffer();
  private StringBuffer resavilabilityerrmessg = new StringBuffer();
  private StringBuffer resshareerrmessg = new StringBuffer();
  
  private StringBuffer fieldname = new StringBuffer();
  private StringBuffer fieldvalue = new StringBuffer();
	
	
  public String getAlertMessage()
   {
     return this.alertmessg.toString();
    }
	
  public void addErrorMessage(String error)
    {
	  errormessg.append(error + " \n");
	}
	
   public void addSystemErrorMessage(String error)
    {
	  systemerrmessg.append(error + " \n");
	}
	
	public void addDataConnectErrMessage(String error)
    {
	  dataconnecterrmessg.append(error + " \n");
	}
	
	
	
	
  public void resetAllMessages()
   {
     alertmessg = null;
	 errormessg =  null;
	 systemerrmessg = null;
	 dataconnecterrmessg =  null;
	 resavilabilityerrmessg =  null;
	 resshareerrmessg =  null;
	}

	
}
	
	

	
	
	
	
	
	