package com.magbel.ia.vao;

public class LedgerGet
{
	private String ledger_no;
	private String description;
	
public LedgerGet()
 	{}

	public LedgerGet(String ledger_no,String description)
       {
		super();
		this.ledger_no =ledger_no ;
		this.description=description;
       }

	public String getLedger()
		{
		return ledger_no ;
		}
	public void setLedger(String ledger_no )
		{
		this.ledger_no =ledger_no ;
		}		
		public String getDescription()
		{
		return description;
		}
		public void setDescription(String description)
		{
		this.description=description;
		}
}