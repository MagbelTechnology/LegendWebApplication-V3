package ng.com.magbel.token;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ParallexTokenClass {
	
	public static void main(String args[]) throws JSONException, KeyManagementException, NoSuchAlgorithmException, IOException {
	     
       //testValidation();
    }
	
	
	
	public static String tokenValidation(String userId, String token) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
			CloseableHttpClient client = HttpClientBuilder.create().build();
			JSONObject obj = new JSONObject();
        	File file = new File("C:\\Property\\LegendPlus.properties");
        	FileInputStream input = new FileInputStream(file);
        	prop.load(input);
   	
        	String uri = prop.getProperty("ParallexApiUrl");
        	
        	//System.out.println("<<<<< We are here ");
		
		try { 
			//System.out.println("<<<<< We are here 2 ");
			 	obj.put("UserId", userId);
			 	obj.put("TokenResponse", token);
	          
	          System.out.println("Obj: " + obj);
	          
	          String jsonText = obj.toString();
	           System.out.println("Request Param: " + jsonText);
			
		    System.out.println("<<<<< uri: " + uri);
		    
			 URL url = new URL (uri);
			 HttpURLConnection con = (HttpURLConnection)url.openConnection();
			 con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				con.setDoOutput(true);
				 
				    InputStream response;                   
				    
				    
//				    JSONObject object = new JSONObject();
//				      object.put("messsage","Not Found");
//				      object.put("responseCode", 404);
//				      
//				    JSONObject jsonObject = new JSONObject();
//				      jsonObject.put("messsage","Unauthorized");
//				      jsonObject.put("responseCode", 401);
//				      
//				      String str = object.toString();
//				      String responseStr = jsonObject.toString();
				      
				     
				    // Check for error , if none store response
				    if(con.getResponseCode() == 200){response = con.getInputStream();}
//				    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
//				    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
				    else{response = con.getErrorStream();}
		
				    InputStreamReader isr = new InputStreamReader(response);
				    StringBuilder sb = new StringBuilder();
				    BufferedReader br = new BufferedReader(isr);
				    String read = br.readLine();
		
				    while(read != null){
				        sb.append(read);
				        read = br.readLine();
				    }   
				    // Print the String     
				    System.out.println(sb.toString());
				    
				    status = sb.toString();
				    
				    
				    
				    response.close();

				
		  
		} catch(IOException exc) {
		    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
		}
		return status;
	}
	
	public static String postTransaction(String requestPayload)
	        throws IOException {
	    String result = "";
	    Properties prop = new Properties();
	    File file = new File("C:\\Property\\LegendPlus.properties");
	    FileInputStream input = new FileInputStream(file);
	    prop.load(input);

	    String uri = prop.getProperty("ParallexPostingApiUrl");
	    String clientId = prop.getProperty("clientId");
	    String clientKey = prop.getProperty("clientKey");
	    System.out.println("clientId to URI: " + clientId);
	    System.out.println("clientKey to URI: " + clientKey);
	    
	    try {

//	    	 // Create a trust manager that does not validate certificate chains
//	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
//	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//	                    return null;
//	                }
//					@Override
//					public void checkClientTrusted(
//							java.security.cert.X509Certificate[] arg0,
//							String authType) throws CertificateException {
//						// TODO Auto-generated method stub
//						
//					}
//					@Override
//					public void checkServerTrusted(
//							java.security.cert.X509Certificate[] arg0,
//							String authType) throws CertificateException {
//						// TODO Auto-generated method stub
//						
//					}
//	            }
//	        };
//
//	        SSLContext sc = SSLContext.getInstance("SSL");
//	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//	        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);


	        URL url = new URL(uri);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("Accept", "application/json");
	        if (clientId != null && !clientId.isBlank()) {
	            con.setRequestProperty("client-id", clientId);
	        }

	        if (clientKey != null && !clientKey.isBlank()) {
	            con.setRequestProperty("client-key", clientKey);
	        }
	        con.setDoOutput(true);

	        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
	            wr.writeBytes(requestPayload);
	            wr.flush();
	        }

	        int responseCode = con.getResponseCode();
	        InputStream response = (responseCode >= 200 && responseCode < 300)
	                ? con.getInputStream()
	                : con.getErrorStream();

	        StringBuilder sb = new StringBuilder();
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(response))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	        }

	        result = sb.toString();
	        System.out.println("Response Code: " + responseCode);
	        System.out.println("Response Body: " + result);

	    } catch (Exception e) {
	        System.out.println("Error posting transaction: " + e.getMessage());
	    }
	    return result;
	}
	
	
	
	//POST METHOD
//	 public static String tokenValidation(String userId, String token) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
//			String status = "";
//			Properties prop = new Properties();
//				CloseableHttpClient client = HttpClientBuilder.create().build();
//	        	HttpResponse response;
//	        	JSONObject json = new JSONObject();
//	        	File file = new File("C:\\Property\\LegendPlus.properties");
//	        	FileInputStream input = new FileInputStream(file);
//	        	prop.load(input);
//	   	
//	        	String uri = prop.getProperty("ParallexApiUrl");
//			
//			try {     
//			    System.out.println("<<<<< uri: " + uri);
//			         
//			    json.put("UserId", userId);
//                json.put("TokenResponse", token);
//                System.out.println("Request Param: " + json.toString());
//			    
//		      	 HttpPost post = new HttpPost(uri);
//	                StringEntity se = new StringEntity(json.toString());
//	                post.setEntity(se);
//	                post.setHeader("Accepts", "application/json");
//	                post.setHeader("Content-Type", "application/json");
//	                response = client.execute(post);
//	                
//	                // print your status code from the response
//	                System.out.println(response.getStatusLine().getStatusCode());
//
//	                // take the response body as a json formatted string 
//	                String responseJSON = EntityUtils.toString(response.getEntity());
//
//	                // convert/parse the json formatted string to a json object
//	                JSONObject jobj = new JSONObject(responseJSON);  
//
//	                //print your response body that formatted into json
//	                System.out.println("Json Response: " + jobj);
//			  
//			} catch(IOException exc) {
//			    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
//			}
//			return status;
//		}
	 
	 // GET Method
  
//    public static String tokenValidation(String userId, String token) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
//		String status = "";
//		Properties prop = new Properties();
//        File file = new File("C:\\Property\\LegendPlus.properties");
//        FileInputStream input = new FileInputStream(file);
//        prop.load(input);
//   	
//        String uri = prop.getProperty("ParallexApiUrl");
//		
//		try {     
//		    System.out.println("<<<<< uri: " + uri);
//		         
//	        Map<String, String> parameters = new HashMap<>();
//			parameters.put("UserId", userId);
//			parameters.put("tokenResponse", token);
//
//			
//	        
//		    URL url = new URL(uri + "?" + ParameterStringBuilder.getParamsString(parameters));
//		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		    
//
//		    // Setup Connection Properties
//		   connection.setRequestMethod("GET");
//		   connection.setRequestProperty("Content-Type", "application/json");
//		   connection.setRequestProperty("Accept", "application/json");
//		   connection.setDoOutput(true);
//            
//		    InputStream response;                   
//		    
//		    
////		    JSONObject object = new JSONObject();
////		      object.put("messsage","Not Found");
////		      object.put("responseCode", 404);
////		      
////		    JSONObject jsonObject = new JSONObject();
////		      jsonObject.put("messsage","Unauthorized");
////		      jsonObject.put("responseCode", 401);
////		      
////		      String str = object.toString();
////		      String responseStr = jsonObject.toString();
//		      
//		     
//		    // Check for error , if none store response
//		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
////		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
////		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
//		    else{response = connection.getErrorStream();}
//
//		    InputStreamReader isr = new InputStreamReader(response);
//		    StringBuilder sb = new StringBuilder();
//		    BufferedReader br = new BufferedReader(isr);
//		    String read = br.readLine();
//
//		    while(read != null){
//		        sb.append(read);
//		        read = br.readLine();
//		    }   
//		    // Print the String     
//		    System.out.println(sb.toString());
//		    
//		    status = sb.toString();
//		    
//		    
//		    
//		    response.close();
//
//		} catch(IOException exc) {
//		    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
//		}
//		return status;
//	}
    
    
}
