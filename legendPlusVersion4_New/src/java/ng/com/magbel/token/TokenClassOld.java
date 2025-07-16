/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.magbel.token;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TokenClassOld {
	
	public static void main(String args[]) {
     
        callSoapWebService("9195","361090");
    }

    public static void createSoapEnvelope(SOAPMessage soapMessage, String id, String token) throws SOAPException, IOException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        Properties prop = new Properties();
        File file = new File("C:\\Property\\LegendPlus.properties");
        FileInputStream input = new FileInputStream(file);
        prop.load(input);
   	
        String appId = prop.getProperty("AppID");
        String appKey = prop.getProperty("AppKey");

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");


        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        QName bodyName = new QName("http://tempuri.org/", "AuthenticateToken" );
//        SOAPHeaderElement security = envelope.getHeader().addHeaderElement(bodyName);
//        security.setMustUnderstand(true);
        
        SOAPElement soapBodyElem = soapBody.addChildElement(bodyName);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("staffId");
        soapBodyElem1.addTextNode(id);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("tokenCode");
        soapBodyElem2.addTextNode(token);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("AppID");
        soapBodyElem3.addTextNode(appId);
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("AppKey");
        soapBodyElem4.addTextNode(appKey);
        soapMessage.saveChanges();
    }

    public static boolean callSoapWebService(String id, String token) {
        String res ="" ;
        boolean tokenResponse = false;
    	try {
        	 Properties prop = new Properties();
             File file = new File("C:\\Property\\LegendPlus.properties");
             FileInputStream input = new FileInputStream(file);
             prop.load(input);
        	  
             String soapEndpointUrl = prop.getProperty("endPointUrl");
             
             System.out.println("The url is : " + soapEndpointUrl); 
        	
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
  
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
           // soapMessage.getSOAPHeader().detachNode();
            createSoapEnvelope(soapMessage, id, token);

            SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();
            
            SOAPBody soapBody = soapResponse.getSOAPBody();
            
            NodeList nodes = soapBody.getElementsByTagName("AuthenticateTokenResult");

            // check if the node exists and get the value
            String someMsgContent = null;
            Node node = nodes.item(0);
            someMsgContent = node != null ? node.getTextContent() : "";
            res =  someMsgContent.substring(0, 2);

            System.out.println(someMsgContent);
            System.out.println(res);
            
            if(res.equals("00")){
            	tokenResponse = true;
            }
            	
            

            soapConnection.close();
            
            
            
            
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        
        return tokenResponse;
    }

 

}
