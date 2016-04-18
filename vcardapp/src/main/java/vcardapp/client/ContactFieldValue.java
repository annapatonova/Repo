package vcardapp.client;

public class ContactFieldValue {
	private String value;
	
	private String streetAddress;
	private String extendedAddress;
	private String postalCode;
	private String city;
	private String state;
	private String country;
	
	public ContactFieldValue(String value, String streetAddress, String extendedAddress, String postalCode, String city, String state, String country) {
		this.value = value;
		this.streetAddress = streetAddress;
		this.extendedAddress = extendedAddress;
		this.postalCode = postalCode;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	
	public ContactFieldValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getExtendedAddress() {
		return extendedAddress;
	}

	public void setExtendedAddress(String extendedAddress) {
		this.extendedAddress = extendedAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	

}
