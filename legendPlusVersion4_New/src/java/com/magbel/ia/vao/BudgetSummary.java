
package com.magbel.ia.vao;

public class BudgetSummary{

	private String category;
	private String remark;
	private String quarter;
	private String glNo;
	private String gldescription;
	private double actual;
	private double budget;
	private double variancevalue;
	private double variancepercent;
	   public BudgetSummary()  
	    {
	          
	    }
	public BudgetSummary(String category,String remark,String quarter,String glNo,String gldescription,
			double actual,double budget,double variancevalue,double variancepercent){

		setCategory(category);
		setRemark(remark);
		setQuarter(quarter);
		setGlNo(glNo);
		setGldescription(gldescription);
		setActual(actual);
		setBudget(budget);
		setVariancevalue(variancevalue);
		setVariancepercent(variancepercent);


	}

	public void setCategory(String category){
		this.category = category;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public void setQuarter(String quarter){
		this.quarter = quarter;
	}
	public void setGlNo(String glNo){
		this.glNo = glNo;
	}
	public void setGldescription(String gldescription){
		this.gldescription = gldescription;
	}	
	public void setActual(double actual){
		this.actual = actual;
	}
	public void setBudget(double budget){
		this.budget = budget;
	}
	public void setVariancevalue(double variancevalue){
		this.variancevalue = variancevalue;
	}
	public void setVariancepercent(double variancepercent){
		this.variancepercent = variancepercent;
	}

	public String getCategory(){
		return this.category;
	}
	public String getRemark(){
		return this.remark;
	}
	public String getQuarter(){
		return this.quarter;
	}
	public String getGlNo(){
		return this.glNo;
	}
	public String getGldescription(){
		return this.gldescription;
	}	
	public double getActual(){
		return this.actual;
	}
	public double getBudget(){
		return this.budget;
	}
	public double getVariancevalue(){
		return this.variancevalue;
	}
	public double getVariancepercent(){
		return this.variancepercent;
	}

}