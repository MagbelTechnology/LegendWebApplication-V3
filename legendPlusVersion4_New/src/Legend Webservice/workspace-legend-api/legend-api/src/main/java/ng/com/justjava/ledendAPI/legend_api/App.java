package ng.com.justjava.ledendAPI.legend_api;

import java.io.Console;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Server server = null;
		try {
			Properties prop = new Properties();
			InputStream setting = null;
			
			setting = new FileInputStream("setting.properties");
			prop.load(setting);
			String serviceIP = prop.getProperty("serviceIP");
			System.out.println(" Service IP =="+serviceIP);
			// load a properties file
			prop.load(setting);
			JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
			sf.setTransportId("http://cxf.apache.org/transports/http");
			sf.setResourceClasses(new Class[] { LegendRESTServices.class });
			sf.setResourceProvider(LegendRESTServices.class, new SingletonResourceProvider(new LegendRESTServices()));
  
			//String currentIP = InetAddress.getLocalHost().getHostAddress();
			
			String publishURL = "http://"+serviceIP+"/";
			
			System.out.println(" The publishURL=="+publishURL);
			sf.setAddress(publishURL);
			
			JSONObject json = new JSONObject();
			json.put("ReqnID", "ReqnID");
			json.put("UserID", "UserID");
			json.put("ReqnBranch", "ReqnBranch");
			json.put("ReqnSection", "ReqnSection");
			json.put("ReqnDepartment", "ReqnDepartment");
			json.put("ReqnDate", "ReqnDate");
			json.put("ReqnUserID", "ReqnUserID");
			json.put("ItemType", "ItemType");
			json.put("ItemRequested", "ItemRequested");
			json.put("ApprovalLevel", "ApprovalLevel");
			json.put("ApprovalLevelLimit", "ApprovalLevelLimit");
			json.put("Supervisor", "Supervisor");
			json.put("Company_Code", "Company_Code");
			json.put("Image", "Image");
			json.put("Remark", "Remark");
			json.put("workStationIP", "workStationIP");
			json.put("Quantity", "Quantity");
			json.put("distributedstatus", "distributedstatus");
			json.put("distributedQty", "distributedQty");
			json.put("projectCode", "projectCode");
			json.put("Category", "Category");
			json.put("ReqnType", "ReqnType");
			json.put("MEASURING_CODE", "MEASURING_CODE");
			json.put("RETURNED_STOCK", "RETURNED_STOCK");
			json.put("RETURNEDCATEGORY", "RETURNEDCATEGORY");
			json.put("RETURNED_ORDERNO", "RETURNED_ORDERNO");
			json.put("FAULT_ID", "FAULT_ID");
//System.out.println("json=="+json);
			server = sf.create();
			
/*			server.start();
		    Console console = System.console();
		    System.out.println("");

		    // If you run the main class from IDEA/Eclipse then you may not have a console, which is null)
		    if (console != null) {
		        System.out.println("  Press ENTER to stop server");
		        console.readLine();
		    }			
	        System.out.println("  Stopping after 5 minutes or press ctrl + C to stop");
	        Thread.sleep(5 * 60 * 1000);
			System.out.println(" After Starting.......");*/
			onStart();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void onStart() {
		System.out.println("Starts at " + new Date());
	}

	private void onStop() {
		System.out.println("Ends at " + new Date());
		System.out.flush();
		System.out.close();
	}
}
