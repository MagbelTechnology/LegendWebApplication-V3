
package com.magbel.ia.vao;

public class Approval{

	private String id;
	private String code;
	private double minAmt;
	private double maxAmt;
	private String desc;

	public Approval(String id,String code,double minAmt,double maxAmt,String desc){

		setId(id);
		setCode(code);
		setMinAmt(minAmt);
		setMaxAmt(maxAmt);
		setDesc(desc);

	}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMinAmt(double minAmt) {
        this.minAmt = minAmt;
    }

    public double getMinAmt() {
        return minAmt;
    }

    public void setMaxAmt(double maxAmt) {
        this.maxAmt = maxAmt;
    }

    public double getMaxAmt() {
        return maxAmt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
