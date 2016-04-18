package vcardapp.client;

import java.util.List;

public class DefaultResponseHandler implements ResponseHandler {
	private ResponseStatus status;
	private List<String> errors;
	private Contact contact;
	private String vCard;
	
	public DefaultResponseHandler() {
		setStatus(ResponseStatus.FAILED);
		setErrors(null);
		this.contact = null;
	}
	
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
			
	public ResponseStatus getStatus() {
		return status;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public void createContact(String vCard) {
		this.vCard = vCard;
		
		contact = new Contact(vCard);
	}

	public boolean hasDetectedContact() {
		return !(contact == null);
	}

	public Contact getDetectedContact() {
		return contact;
	}

	public String getDetectedContactAsVCard() {
		return vCard;
	}

}
