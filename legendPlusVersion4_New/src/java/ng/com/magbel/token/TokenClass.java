package ng.com.magbel.token;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TokenClass {
	//static SimpleLogFile log = new SimpleLogFile();
	public static void main(String args[]) {
     
        //callSoapWebService("9195","361090", "http://172.21.10.103/AuthenticationUtilityServiceTest/AuthenticationService.asmx?op=");
    }

    public static void createSoapEnvelope(SOAPMessage soapMessage, String id, String token, String urlName, String IdName) throws SOAPException, IOException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
   	
        String appId = prop.getProperty("AppID");
        String appKey = prop.getProperty("AppKey");
        
//       System.out.println("The app id is : " + appId);
//       System.out.println("The app key is : " + appKey);
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.removeNamespaceDeclaration("SOAP-ENV");
        envelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.setPrefix("soapenv");
        
        //SOAP Header
        SOAPHeader soapHeader = envelope.getHeader();
     soapHeader.setPrefix("soapenv");
       

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.setPrefix("soapenv");
        soapBody.removeNamespaceDeclaration("http://tempuri.org/");
    

        QName bodyName = new QName("http://tempuri.org/",""+urlName+"", "tem" );

        SOAPElement soapBodyElem = soapBody.addChildElement(bodyName);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement(""+IdName+"","tem");
        soapBodyElem1.addTextNode(id);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement( "tokenCode","tem");
        soapBodyElem2.addTextNode(token);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("AppID","tem");
        soapBodyElem3.addTextNode(appId);
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("AppKey","tem");
        soapBodyElem4.addTextNode(appKey);
        soapMessage.saveChanges();
    }

    public static boolean callSoapWebService(String id, String token, String soapEndpointUrl, String action, String urlName, String IdName, String responseName) {
        boolean res = false ;
    	try {      	
//            System.out.println("The end point url is : " + soapEndpointUrl);
//           System.out.println("The action is : " + action);
//           System.out.println("The url name is : " + urlName);
//           System.out.println("The Id name is : " + IdName);
        	
          // Create SOAP Connection
             SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
             SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(action, id, token, urlName, IdName), soapEndpointUrl);

            // Print the SOAP Response
           System.out.println("Response SOAP Message:");
//            soapResponse.writeTo(System.out);
           System.out.println( getSOAPMessageAsString(soapResponse));
            
            SOAPBody soapBody = soapResponse.getSOAPBody();
            
            NodeList nodes = soapBody.getElementsByTagName(""+responseName+"");
            
           System.out.println("Tag Name >>>> " + soapBody.getElementsByTagName(""+responseName+"").item(0).getTextContent());
            
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
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, id, token, urlName, IdName);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
         
       System.out.println( getSOAPMessageAsString(soapMessage));
        

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

    public static String tokenValidation(String userId, String token) throws JSONException, NoSuchAlgorithmException, KeyManagementException, IOException {
		String status = "";
		Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
   	
        String uri = prop.getProperty("ParallexApiUrl");
		
		try {    
		    System.out.println("<<<<< uri: " + uri);
		    
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
			parameters.put("UserId", userId);
			parameters.put("tokenResponse", token);

	        
		    URL url = new URL(uri + "?" + ParameterStringBuilder.getParamsString(parameters));
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		    // Setup Connection Properties
		   connection.setRequestMethod("GET");
		   connection.setRequestProperty("Content-Type", "application/json");	
		   connection.setDoOutput(true);
            
		    InputStream response;                   

		    JSONObject object = new JSONObject();
		      object.put("messsage","Not Found");
		      object.put("responseCode", 404);
		      
		    JSONObject jsonObject = new JSONObject();
		      jsonObject.put("messsage","Unauthorized");
		      jsonObject.put("responseCode", 401);
		      
		      String str = object.toString();
		      String responseStr = jsonObject.toString();
		      
		    
		    // Check for error , if none store response
		    if(connection.getResponseCode() == 200){response = connection.getInputStream();}
		    else if(connection.getResponseCode() == 401) {response= new ByteArrayInputStream(responseStr.getBytes());}
		    else if(connection.getResponseCode() == 404) {response= new ByteArrayInputStream(str.getBytes());}
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
    
}
