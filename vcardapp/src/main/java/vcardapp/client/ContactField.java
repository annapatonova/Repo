package vcardapp.client;

public class ContactField
{
	private Type type = null;
	private Kind kind = null;
	private ContactFieldValue value = null;
	
	public enum Kind {
		WORK, HOME, MOBILE, WORK_FAX, FACEBOOK, SKYPE, TWITTER, LINKED_IN
	}
	
	public enum Type {
		PHONE, ADDRESS, EMAIL, SOCIAL_NETWORK
	}
	
	public ContactField(Type type, Kind kind, String value) {
		if(value != null && !value.isEmpty()) {
			setType(type);
			setKind(kind);
			setValue(value);
		}
	}
	
	public ContactField(Type type, Kind kind, ContactFieldValue value) {
		if(value != null) {
			setType(type);
			setKind(kind);
			setValue(value);
		}
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public void setValue(String value) {
		this.value = new ContactFieldValue(value);
	}
	
	public void setValue(ContactFieldValue value) {
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}
	
	public Kind getKind() {
		return kind;
	}
	
	public ContactFieldValue getValue() {
		return value;
	}
	
	
}
