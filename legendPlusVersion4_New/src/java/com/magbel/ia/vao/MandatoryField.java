
package com.magbel.ia.vao;
/**

    <code>MandatoryField.java</code>
    @see Handles callback from JAAS framework.
    @Author
    @since 21-03-2007
*/
public class MandatoryField{

	private String id;
	private String formId;
	private String formField;
	private String formLabel;
	private String message;
	private String flag;

	public MandatoryField(String id, String formId, String formField,
							String formLabel, String message,String flag){

		setId(id);
		setFormId(formId);
		setFormField(formField);
		setFormLabel(formLabel);
		setMessage(message);
		setFlag(flag);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setFormId(String formId){
		this.formId = formId;
	}
	public void setFormField(String formField){
		this.formField = formField;
	}
	public void setFormLabel(String formLabel){
		this.formLabel = formLabel;
	}
	public void setMessage(String message){
		this.message = message;
	}

	public void setFlag(String flag){
		this.flag = flag;
	}

	public String getId(){
		return this.id;
	}
	public String getFormId(){
		return this.formId;
	}
	public String getFormField(){
		return this.formField;
	}
	public String getFormLabel(){
		return this.formLabel;
	}
	public String getMessage(){
		return this.message;
	}

	public String getFlag(){
		return this.flag;
	}
}