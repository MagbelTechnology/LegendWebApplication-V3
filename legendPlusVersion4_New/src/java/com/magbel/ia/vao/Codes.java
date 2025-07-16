/**
 * 
 */
package com.magbel.ia.vao;

/**
 * @author Rahman O. Oloritun
 * @since 27-06-2007
 * 
 */
public class Codes {

	private String priority1;

	private String priority2;

	private String priority3;

	private String priority4;

	private String priority5;

	private String priority6;

	

	private String delimiter;

	/**
	 * 
	 */
	public Codes() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param moduleName
	 * @param priority1
	 * @param priority2
	 * @param priority3
	 * @param priority4
	 * @param priority5
	 * @param priority6
	 * @param moduleAbv
	 * @param is_prefix
	 * @param startNo
	 * @param delimiter
	 */
	public Codes(String priority1, String priority2, String priority3,
			String priority4, String priority5, String priority6,
			 String delimiter) {
		this.priority1 = priority1;
		this.priority2 = priority2;
		this.priority3 = priority3;
		this.priority4 = priority4;
		this.priority5 = priority5;
		this.priority6 = priority6;
		this.delimiter = delimiter;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	
	/**
	 * @return the priority1
	 */
	public String getPriority1() {
		return priority1;
	}

	/**
	 * @param priority1
	 *            the priority1 to set
	 */
	public void setPriority1(String priority1) {
		this.priority1 = priority1;
	}

	/**
	 * @return the priority2
	 */
	public String getPriority2() {
		return priority2;
	}

	/**
	 * @param priority2
	 *            the priority2 to set
	 */
	public void setPriority2(String priority2) {
		this.priority2 = priority2;
	}

	/**
	 * @return the priority3
	 */
	public String getPriority3() {
		return priority3;
	}

	/**
	 * @param priority3
	 *            the priority3 to set
	 */
	public void setPriority3(String priority3) {
		this.priority3 = priority3;
	}

	/**
	 * @return the priority4
	 */
	public String getPriority4() {
		return priority4;
	}

	/**
	 * @param priority4
	 *            the priority4 to set
	 */
	public void setPriority4(String priority4) {
		this.priority4 = priority4;
	}

	/**
	 * @return the priority5
	 */
	public String getPriority5() {
		return priority5;
	}

	/**
	 * @param priority5
	 *            the priority5 to set
	 */
	public void setPriority5(String priority5) {
		this.priority5 = priority5;
	}

	/**
	 * @return the priority6
	 */
	public String getPriority6() {
		return priority6;
	}

	/**
	 * @param priority6
	 *            the priority6 to set
	 */
	public void setPriority6(String priority6) {
		this.priority6 = priority6;
	}

	
}
