package vcardapp.client;

import java.util.Map;

public class Request {
	Map<String, String> parameters;
	
	public Request(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public String getParameters() {
		String result = "";
		boolean first = true;
		for(String key : parameters.keySet()) {
			
			if(first) { // so that the string does not begin by "&"
				first = false;
			} else {
				result += "&";
			}
			
			result += key+"="+parameters.get(key);
		}
		
		return result;
	}

}
