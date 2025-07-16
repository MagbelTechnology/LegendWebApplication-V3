package legend;

public class PasswordHistory 
{
	private String UserId;
	private int Counter;
	private String Password1;
	private String Password2;
	private String Password3;
	private String Password4;
	private String Password5;
	
	public PasswordHistory( )
	{
		 
	}
	
	
	public PasswordHistory(String userId, int counter, String password1,
			String password2, String password3, String password4,
			String password5) {
		super();
		UserId = userId;
		Counter = counter;
		Password1 = password1;
		Password2 = password2;
		Password3 = password3;
		Password4 = password4;
		Password5 = password5;
	}


	public String getUserId() {
		return UserId;
	}


	public void setUserId(String userId) {
		UserId = userId;
	}


	public int getCounter() {
		return Counter;
	}


	public void setCounter(int counter) {
		Counter = counter;
	}


	public String getPassword1() {
		return Password1;
	}


	public void setPassword1(String password1) {
		Password1 = password1;
	}


	public String getPassword2() {
		return Password2;
	}


	public void setPassword2(String password2) {
		Password2 = password2;
	}


	public String getPassword3() {
		return Password3;
	}


	public void setPassword3(String password3) {
		Password3 = password3;
	}


	public String getPassword4() {
		return Password4; 
	}


	public void setPassword4(String password4) {
		Password4 = password4;
	}


	public String getPassword5() {
		return Password5;
	}


	public void setPassword5(String password5) {
		Password5 = password5;
	}
	
	
}
