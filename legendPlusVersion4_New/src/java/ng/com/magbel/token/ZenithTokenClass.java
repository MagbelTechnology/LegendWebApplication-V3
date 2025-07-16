/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.magbel.token;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ZenithTokenClass {
	
	public static void main(String args[]) {
	      
        //callSoapWebService("9195","361090", "http://172.21.10.103/AuthenticationUtilityServiceTest/AuthenticationService.asmx?op=");
    }
 
    public static void createSoapEnvelope(SOAPMessage soapMessage, String id, String token, String urlName, String IdName) throws SOAPException, IOException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
   	
        String domain = prop.getProperty("Domain");
       // String appKey = prop.getProperty("AppKey");
//       System.out.println("The app id is : " + appId);
//       System.out.println("The app key is : " + appKey);
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.removeNamespaceDeclaration("SOAP");
        envelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.setPrefix("soap");
        
        //SOAP Header
//        SOAPHeader soapHeader = envelope.getHeader();
//     soapHeader.setPrefix("soap");
       

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.setPrefix("soap");
        soapBody.removeNamespaceDeclaration("http://tempuri.org/");
    

        QName bodyName = new QName("http://tempuri.org/",""+urlName+"", "tem");

        SOAPElement soapBodyElem = soapBody.addChildElement(bodyName);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("psDomain","tem");
        soapBodyElem1.addTextNode(domain);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("ps"+IdName+"","tem");
        soapBodyElem2.addTextNode(id);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement( "psOTP","tem");
        soapBodyElem3.addTextNode(token);
        soapMessage.saveChanges();
    }

    public static boolean callSoapWebService(String id, String token, String soapEndpointUrl, String action, String urlName, String IdName, String responseName) {
        boolean res = false ;
    	try {      	
//            System.out.println("The end point url is : " + soapEndpointUrl);
//           System.out.println("The action is : " + action);
//           System.out.println("The url name is : " + urlName);
//           System.out.println("The Id name is : " + IdName);
//           System.out.println("token : " + token);
           System.out.println("id : " + id);
        	
          // Create SOAP Connection
             SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
             SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            // Send SOAP Message to SOAP Server     
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(action, id, token, urlName, IdName), soapEndpointUrl);

            // Print the SOAP Response
      //     System.out.println("Response SOAP Message:");
//            soapResponse.writeTo(System.out);
         //  System.out.println( getSOAPMessageAsString(soapResponse));
            
            SOAPBody soapBody = soapResponse.getSOAPBody();
            
            NodeList nodes = soapBody.getElementsByTagName(""+responseName+"");
            
          // System.out.println("Tag Name >>>> " + soapBody.getElementsByTagName(""+responseName+"").item(0).getTextContent());
            
            // check if the node exists and get the value
            String someMsgContent = null;
            Node node = nodes.item(0);
            someMsgContent = node != null ? node.getTextContent() : "";
           String response =  someMsgContent.substring(0, 2);
           
           if(!response.equals("00")){
        	   res = false;
           }else{
        	   res = true;
           }

            //System.out.println(someMsgContent);
            System.out.println("Response After Substring >>>> " + res);
            
            soapConnection.close();
            
            
            
            
        } catch (Exception e) {
        	System.out.println("Error occurred while sending SOAP Request to Server!"
        			+ "Make sure you have the correct endpoint URL and SOAPAction!\\n");
           System.out.println("Exception in calling SOAP Web Service >>> " + e.getMessage());
        }
        
        return res;
    }

    private static SOAPMessage createSOAPRequest(String soapAction, String id, String token, String urlName, String IdName) throws Exception {
//    	System.out.println("We are here 2:");
    	MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

//        System.out.println("We are here 3:");
        createSoapEnvelope(soapMessage, id, token, urlName, IdName);
      //  System.out.println("Calling SOAP Envelope:");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
       // System.out.println("Request SOAP Message:");
         
      // System.out.println( getSOAPMessageAsString(soapMessage));
        

        return soapMessage;
    }
    
    
    public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
        try {

           TransformerFactory tff = TransformerFactory.newInstance();
           Transformer tf = tff.newTransformer();

           // Set formatting
          
           tf.setOutputProperty(OutputKeys.INDENT, "yes"); 
           tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                 "2");
           
           Source sc = soapMessage.getSOAPPart().getContent();

           ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
           StreamResult result = new StreamResult(streamOut);
           tf.transform(sc, result);

           String strMessage = streamOut.toString();
           return strMessage;     
        } catch (Exception e) {
           System.out.println("Exception in getSOAPMessageAsString "
                 + e.getMessage());
           return null;
        }  
    }

   
  public static String validation() throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
      File file = new File("C:\\Property\\LegendPlus.properties");
      FileInputStream input = new FileInputStream(file);
      prop.load(input);
 	
      String apiUrl = prop.getProperty("BatchApiUrl");
      String channel = prop.getProperty("BatchChannel");
		
		try {     
		    System.out.println("<<<<< apiUrl: " + apiUrl);
		    System.out.println("<<<<< channel: " + channel);
		    
		    // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		         
	        Map<String, String> parameters = new HashMap<>();
			parameters.put("channel", channel);
			//parameters.put("tokenResponse", token);

			
	        
		    URL url = new URL(apiUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    

		    // Setup Connection Properties
		   connection.setRequestMethod("GET");
		   connection.setRequestProperty("Content-Type", "application/json");
		   connection.setRequestProperty("Accept", "application/json");
		   connection.setDoOutput(true);
          
		    InputStream response;                   
		    
		    
//		    JSONObject object = new JSONObject();
//		      object.put("messsage","Not Found");
//		      object.put("responseCode", 404);
//		      
//		    JSONObject jsonObject = new JSONObject();
//		      jsonObject.put("messsage","Unauthorized");
//		      jsonObject.put("responseCode", 401);
//		      
//		      String str = object.toString();
//		      String responseStr = jsonObject.toString();
		      
		     
		    // Check for error , if none store response
		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
//		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
//		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
		    else{response = connection.getErrorStream();}

		    InputStreamReader isr = new InputStreamReader(response);
		    StringBuilder sb = new StringBuilder();
		    BufferedReader br = new BufferedReader(isr);
		    String read = br.readLine();

		    while(read != null){	
		        sb.append(read);
		        read = br.readLine();
		    }   
		    // Print the String     
		    System.out.println("<<<<<: " + sb.toString()); 
		    
		    status = sb.toString();
		    
		    
		    
		    response.close();

		} catch(IOException exc) {
		    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
		}
		return status;
	}
  
  
  public static String accountDetailValidation(String accountNo) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
    File file = new File("C:\\Property\\LegendPlus.properties");
    FileInputStream input = new FileInputStream(file);
    prop.load(input);
	
    String accountValidationUrl = prop.getProperty("AccountValidationUrl");
//    String channel = prop.getProperty("BatchChannel");
		
		try {   
			
			 System.out.println("<<<<< accountValidationUrl: " + accountValidationUrl);
		    System.out.println("<<<<< Old Customer_account_no: " + accountNo);
		    
		    // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		   
		         
	        Map<String, String> parameters = new HashMap<>();
			parameters.put("Customer_account_no", accountNo);
			//parameters.put("tokenResponse", token);

			
	        
		    URL url = new URL(accountValidationUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    

		    // Setup Connection Properties
		   connection.setRequestMethod("GET");
		   connection.setRequestProperty("Content-Type", "application/json");
		   connection.setRequestProperty("Accept", "application/json");
		   connection.setDoOutput(true);
        
		    InputStream response;                   
		    
		    
//		    JSONObject object = new JSONObject();
//		      object.put("messsage","Not Found");
//		      object.put("responseCode", 404);
//		      
//		    JSONObject jsonObject = new JSONObject();
//		      jsonObject.put("messsage","Unauthorized");
//		      jsonObject.put("responseCode", 401);
//		      
//		      String str = object.toString();
//		      String responseStr = jsonObject.toString();
		      
		     
		    // Check for error , if none store response
		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
//		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
//		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
		    else{response = connection.getErrorStream();}

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
  
  public static  String batchStatusValidation(String batchNo) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
File file = new File("C:\\Property\\LegendPlus.properties");
FileInputStream input = new FileInputStream(file);
prop.load(input);
	
String batchUrl = prop.getProperty("BatchStatusUrl");

		
		try {     
		    System.out.println("<<<<< Batch Status Url: " + batchUrl);
		    System.out.println("<<<<< batchNo: " + batchNo);
		    JSONObject obj = new JSONObject();
		    obj.put("batchNo", batchNo);
		    
		    String jsonInputString = obj.toString();
		    
		    System.out.println("<<<<< Json Input: " + jsonInputString);
		    
		    // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		         
	  
			
	        
	    	URL url = new URL(batchUrl);
			  HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json"); 
				con.setDoOutput(true);
				System.out.println("Starting to call .");
				OutputStream os = con.getOutputStream();
				os.write(jsonInputString.getBytes());  
				os.flush();
				os.close();
				// For POST only - END

				int responseCode = con.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode);

				if (responseCode == HttpURLConnection.HTTP_OK) { //success
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					}
					in.close();

					// print result
					System.out.println("Response: " + response.toString());
					status = response.toString();
				} else {
					System.out.println("POST request did not work.");
				}

		} catch(IOException exc) {
		    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
		}
		return status;
	}


public static String batchPostingExceptionValidation(String batchNo) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
  File file = new File("C:\\Property\\LegendPlus.properties");
  FileInputStream input = new FileInputStream(file);
  prop.load(input);
	
  String batchPostingApiUrl = prop.getProperty("BatchPostingExceptionUrl");
		
		try {     
		    System.out.println("<<<<< Batch Posting Exception Url: " + batchPostingApiUrl); 
		    System.out.println("<<<<< batchNo: " + batchNo);    
		    
		    
		    // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		         
	        Map<String, String> parameters = new HashMap<>();
			parameters.put("batchNo", batchNo);
			//parameters.put("tokenResponse", token);

			
	        
		    URL url = new URL(batchPostingApiUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
			// URL url = new URL(batchPostingApiUrl);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    

		    // Setup Connection Properties
		   connection.setRequestMethod("GET");
		   connection.setRequestProperty("Content-Type", "application/json");
		   connection.setRequestProperty("Accept", "application/json");
		   connection.setDoOutput(true);
		   System.out.println("Starting to call ...");
		    InputStream response;                   
		    
		    
//		    JSONObject object = new JSONObject();
//		      object.put("messsage","Not Found");
//		      object.put("responseCode", 404);
//		      
//		    JSONObject jsonObject = new JSONObject();
//		      jsonObject.put("messsage","Unauthorized");
//		      jsonObject.put("responseCode", 401);
//		      
//		      String str = object.toString();
//		      String responseStr = jsonObject.toString();
		      
		     
		    // Check for error , if none store response
		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
//		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
//		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
		    else{response = connection.getErrorStream();}

		    InputStreamReader isr = new InputStreamReader(response);
		    StringBuilder sb = new StringBuilder();
		    BufferedReader br = new BufferedReader(isr);
		    String read = br.readLine();

		    while(read != null){
		        sb.append(read);
		        read = br.readLine();
		    }   
		    // Print the String     
		    System.out.println("Batch Posting Exception Response: " + sb.toString());
		    
		    status = sb.toString();
		    
		    
		    
		    response.close();

		} catch(IOException exc) {
		    System.out.println("There was an error creating the HTTP Call: " + exc.toString());
		}
		return status;
	}
  
  
//  public static  String batchPostingExceptionValidation(String batchNo) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
//		String status = "";
//		Properties prop = new Properties();
//		File file = new File("C:\\Property\\LegendPlus.properties");
//		FileInputStream input = new FileInputStream(file);
//		prop.load(input);
//		String batchPostingApiUrl = prop.getProperty("BatchPostingExceptionUrl");
//		JSONObject jsonInput = new JSONObject();
//		jsonInput.put("batchNo",batchNo);
//		String jsonInputString = jsonInput.toString();
//
//		
//		try {     
//		    System.out.println("<<<<< batchPostingApiUrl: " + batchPostingApiUrl);
//			 System.out.println("/n ");
//		    System.out.println("<<<<< Json Input: " + jsonInputString);
//			
//			 // Create a trust manager that does not validate certificate chains
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
//	        // Install the all-trusting trust manager
//	        SSLContext sc = SSLContext.getInstance("SSL");
//	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//	 
//	        // Create all-trusting host name verifier
//	        HostnameVerifier allHostsValid = new HostnameVerifier() {
//	            public boolean verify(String hostname, SSLSession session) {
//	                return true;
//	            }
//
//				
//	        };
//
//	        // Install the all-trusting host verifier
//	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//	        
//		      //URL url = new URL(batchUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
//			URL url = new URL(batchPostingApiUrl);
//		  HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestMethod("GET");
//			con.setRequestProperty("Content-Type", "application/json");
//			con.setRequestProperty("Accept", "application/json"); 
//			con.setDoOutput(true);
//			System.out.println("Starting to call .");
//			OutputStream os = con.getOutputStream();
//			os.write(jsonInputString.getBytes());  
//			os.flush();
//			os.close();
//			// For POST only - END
//
//			int responseCode = con.getResponseCode();
//			System.out.println("GET Response Code :: " + responseCode);
//
//			if (responseCode == HttpURLConnection.HTTP_OK) { //success
//				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//				String inputLine;
//				StringBuffer response = new StringBuffer();
//
//				while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//				}
//				in.close();
//
//				// print result
//				System.out.println("Batch Posting Exception Response: " + response.toString());
//				status = response.toString();
//			} else {
//				System.out.println("GET request did not work.");
//			}
//		} catch(IOException exc) {
//		    exc.getMessage();
//		}
//			
//		return status;
//	}
	
	
	
	 public static  String postingDateValidation(String branchCode) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
File file = new File("C:\\Property\\LegendPlus.properties");
FileInputStream input = new FileInputStream(file);
prop.load(input);
	
String postingDateUrl = prop.getProperty("postingDateUrl");

		
		try {     
		   // System.out.println("<<<<< Batch Status Url: " + batchUrl);
		    System.out.println("<<<<< postingDateUrl: " + postingDateUrl);
		    
		    // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
					@Override
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] arg0,
							String authType) throws CertificateException {
						// TODO Auto-generated method stub
						
					}
	            }
	        };
	 
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		         
	        Map<String, String> parameters = new HashMap<>();
			parameters.put("branchCode", branchCode);
			//parameters.put("tokenResponse", token);

			
	        
		    URL url = new URL(postingDateUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
			//URL url = new URL(postingDateUrl);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    

		    // Setup Connection Properties
		   connection.setRequestMethod("GET");
		   connection.setRequestProperty("Content-Type", "application/json");
		   connection.setRequestProperty("Accept", "application/json");
		   connection.setDoOutput(true);
    
		    InputStream response;                   
		    
		    
//		    JSONObject object = new JSONObject();
//		      object.put("messsage","Not Found");
//		      object.put("responseCode", 404);
//		      
//		    JSONObject jsonObject = new JSONObject();
//		      jsonObject.put("messsage","Unauthorized");
//		      jsonObject.put("responseCode", 401);
//		      
//		      String str = object.toString();
//		      String responseStr = jsonObject.toString();
		      
		     
		    // Check for error , if none store response
		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
//		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
//		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
		    else{response = connection.getErrorStream();}

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
	
	
	 public static String realTimeTransactionValidation(String jsonInputString) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
		File file = new File("C:\\Property\\LegendPlus.properties");
		FileInputStream input = new FileInputStream(file);
		prop.load(input);
		String devUrl = prop.getProperty("SinglePostingAPI");

		
		try {     
		    System.out.println("<<<<< devUrl: " + devUrl);
		    System.out.println("<<<<< Json Input: " + jsonInputString);
	        
		      //URL url = new URL(batchUrl + "?" + ParameterStringBuilder.getParamsString(parameters));
			URL url = new URL(devUrl);
		  HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json"); 
			con.setDoOutput(true);
			System.out.println("Starting to call .");
			OutputStream os = con.getOutputStream();
			os.write(jsonInputString.getBytes());  
			os.flush();
			os.close();
			// For POST only - END

			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode); 

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				}
				in.close();

				// print result
				System.out.println("Response: " + response.toString());
				status = response.toString();
			} else {
				System.out.println("POST request did not work.");
			}
		} catch(IOException exc) {
			 System.out.println("There was an error creating the HTTP Call: " +  exc.getMessage()); 
		}
			
		return status;
	}


}
