package vcardapp.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {
	
	// Implementing the Singleton design pattern
	
	private static Client instance = null;
	
	private String userName;
	private String password;
	private Protocole protocol;
	private Method method;
	private String endPoint;
	private int timeout; // in ms
	
	public enum Protocole {
		HTTPS
	}
	
	public enum Method {
		POST
	}
	
	private Client(String userName, String password) {
		// Default values for authentification
		
		this.userName = userName;
		this.password = password;
		this.protocol = Protocole.HTTPS;
		this.method = Method.POST;
		this.endPoint = "api.evercontact.com/pulse-api/tag";
		this.timeout = 15000;
	}
			
	
	public static Client getDefault(String userName, String password) {
		// Retrieves the only instance of Client
		
		if(instance == null) {
			instance = new Client(userName, password);
		} else {
			instance.setUserName(userName);
			instance.setPassword(password);
		}
		
		return instance;
	}
	
	public void execute(Request request, ResponseHandler rh) {
		if(rh == null) {
			System.err.println("Client: ResponseHandler object null");
			return;
		}
		
		try {
			String protocol;
			
			switch(this.protocol) {
				case HTTPS:
					protocol = "https://";
				default:
					protocol = "https://";
			}
			
			URL url = new URL(protocol + endPoint);
			
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			
			String method;
			
			switch(this.method) { // for maintainability
				case POST:
					method = "POST";
					break;
				default:
					method = "POST";
			}
			
			huc.setRequestMethod(method);
			huc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			byte[] rawInput = new String(userName + ":" + password).getBytes(StandardCharsets.UTF_8);
			huc.setRequestProperty("Authorization",  "Basic " + Base64.getEncoder().encodeToString(rawInput));
			huc.setConnectTimeout(timeout); // timeout in ms
			
			String urlParameters = request.getParameters();
			urlParameters += "&ApiUser=" + userName;
			
			
			huc.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
			dos.writeBytes(urlParameters);
			dos.flush();
			dos.close();
			
			int responseCode = huc.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				
				while((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				
				in.close();	
				
				JSONObject responseJSON = new JSONObject(response.toString());
				boolean success = responseJSON.getBoolean("success");
				
				if(success) {
					rh.setStatus(ResponseHandler.ResponseStatus.SUCCEED);
					
					String signature = responseJSON.getString("signature");
					
					rh.createContact(signature);
					
					
					
				} else {
					rh.setStatus(ResponseHandler.ResponseStatus.FAILED);
					
					JSONArray errorsJSON = responseJSON.getJSONArray("errorMessages");
					List<String> errors = new ArrayList<String>();
					
					for(int i = 0; i < errorsJSON.length(); i++) {
						String error = errorsJSON.getString(i);
						errors.add(error);
					}
					
					rh.setErrors(errors);
					
				}
			} else {
				System.err.println("Client: unsuccessful request");
				rh.setStatus(ResponseHandler.ResponseStatus.FAILED);
			}
			
		} catch(SocketTimeoutException e) {
			rh.setStatus(ResponseHandler.ResponseStatus.TIME_OUT);
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
