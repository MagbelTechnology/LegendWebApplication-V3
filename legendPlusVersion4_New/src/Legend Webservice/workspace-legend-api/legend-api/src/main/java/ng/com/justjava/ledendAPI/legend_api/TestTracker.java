/**
 * 
 */
package ng.com.justjava.ledendAPI.legend_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kazeem
 *
 */
public class TestTracker {

	/**
	 * @param args
	 */
	private ServerSocket server;
	
	public TestTracker() {
		try {
			server = new ServerSocket(0, 1, InetAddress.getLocalHost());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public static void main(String[] args)  throws Exception{
		// TODO Auto-generated method stub
        TestTracker app = new TestTracker();
        System.out.println("\r\nRunning Server: " + 
                "Host=" + app.getSocketAddress().getHostAddress() + 
                " Port=" + app.getPort());
        
        //app.listen();
       System.out.println(" The final formatted here="+app.getAddressByGpsCoordinates("65.89,62.90"));   

	}
    public String getAddressByGpsCoordinates(String lnglat) {
         
    	URL url = null;
    	HttpURLConnection urlConnection = null;
        String formattedAddress = "";
        try {
        	url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+ lnglat + "&sensor=true");
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result, line = reader.readLine();
            result = line;
            while ((line = reader.readLine()) != null) {
                result += line;
                System.out.println(" result=="+result);
            }
 
            System.out.println(" line=="+line);
            JSONObject resultJSON = new JSONObject(result);

            System.out.println(" The resultJSON=="+resultJSON);
 
            if (resultJSON.has("results")) {
                JSONArray matches = (JSONArray) resultJSON.get("results");
                JSONObject data = (JSONObject) matches.get(0); //TODO: check if idx=0 exists
                formattedAddress = (String) data.get("formatted_address");
                System.out.println(" The formattedAddress=="+formattedAddress);
            }
            //return "";
        } finally {
            urlConnection.disconnect();
            return formattedAddress;
        }
    }
	
    private void listen() throws Exception {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));        
        while ( (data = in.readLine()) != null ) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
        }
    }	
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }
    
    public int getPort() {
        return this.server.getLocalPort();
    }
}
