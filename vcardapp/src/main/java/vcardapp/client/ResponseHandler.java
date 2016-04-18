package vcardapp.client;

import java.util.List;

public interface ResponseHandler {
	
	public enum ResponseStatus {
		FAILED, SUCCEED, TIME_OUT
	}

	public ResponseStatus getStatus();
	public List<String> getErrors();
	public boolean hasDetectedContact();
	public Contact getDetectedContact();
	public String getDetectedContactAsVCard();
	public void setStatus(ResponseStatus status);
	public void setErrors(List<String> errors);
	public void createContact(String vCard);
	
}
