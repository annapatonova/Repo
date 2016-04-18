package vcardapp.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {
	private String firstName;
	private String lastName;
	private String fullName;
	private String organization;
	private String jobPosition;
	private List<ContactField> fields;

	public Contact(String vCard) {
		fields = new ArrayList<ContactField>();
		initFromVCard(vCard);
	}
	
    private void initFromVCard(String vCard) {
    	
    	Matcher matcher = null;
    	
    	vCard = vCard.replaceAll("\r\n", "\r\n\r\n"); // compulsory not to omit any occurrence
        
        // Names
    	
    	matcher = Pattern.compile("\r\nFN:(.*)\r\n").matcher(vCard);
    	
        if(matcher.find())
            setFullName(matcher.group(1));
        
        
        matcher = Pattern.compile("\r\nN:(.*)\r\n").matcher(vCard);
    	
        if(matcher.find())
            setNames(matcher.group(1));
        
        
        // Job
        
        matcher = Pattern.compile("\r\nORG:(.*)\r\n").matcher(vCard);
        if(matcher.find())
            setOrganization(matcher.group(1));
        
        matcher = Pattern.compile("\r\nROLE:(.*)\r\n").matcher(vCard);
        if(matcher.find())
        	setJobPosition(matcher.group(1));
        
        
        // Phone
        
        matcher = Pattern.compile("\r\nTEL(;TYPE=(.*))?:(.*)\r\n").matcher(vCard);
        
        while(matcher.find()) {
        	ContactField.Kind kind = ContactField.Kind.WORK;
        	
        	if(matcher.group(2) != null) {
	        	if(matcher.group(2).equals("CELL"))
	        		kind = ContactField.Kind.MOBILE;
	        	else if(matcher.group(2).equals("FAX"))
	        		kind = ContactField.Kind.WORK_FAX;
        	}
        	
        	addField(new ContactField(ContactField.Type.PHONE, kind, matcher.group(3)));
    	}
        
   
        // Email
        
        matcher = Pattern.compile("\r\nEMAIL:(.*)\r\n").matcher(vCard);
        if(matcher.find())
        	addField(new ContactField(ContactField.Type.EMAIL, ContactField.Kind.WORK, matcher.group(1)));
        
        
       
        // Address
        
        matcher = Pattern.compile("\r\nADR(;(TYPE=|.*)(.*))?:(.*);(.*);(.*);(.*);(.*);(.*);(.*)\r\n").matcher(vCard);
        
        if(matcher.find()) {
        	ContactField.Kind kind = ContactField.Kind.WORK;
        	
        	if(matcher.group(3) != null) {
	        	if(matcher.group(3).equals("HOME")) // if TYPE=HOME option, defines ContactFieldKind accordingly
	        		kind = ContactField.Kind.HOME;
	        	else if(matcher.group(3).equals("WORK"))
	        		kind = ContactField.Kind.WORK;
        	}
        	
        	String extendedAddress = matcher.group(5);
        	String streetAddress = matcher.group(6);
        	String city = matcher.group(7); // locality
        	String state = matcher.group(8); // state or province
        	String postalCode = matcher.group(9);
        	String country = matcher.group(10);
        	
        	// We parameter the address display
        	
        	if(country != null && country.contains("specified")) // country equals "Not specified" (charset issue)
        		country = "";
        	
        	String address = streetAddress;
        	
        	if(extendedAddress != null && !extendedAddress.isEmpty())
        		address += ", " + extendedAddress;
        	if(postalCode != null && !postalCode.isEmpty())
        		address += ", " + postalCode;
        	if(city != null && !city.isEmpty())
        		address += " " + city;
        	if(state != null && !state.isEmpty())
        		address += ", " + state;
        	if(country != null && !country.isEmpty())
        		address += ", " + country;
        	
        	address = address.trim(); // delete surrounding spaces
        	address = address.replaceAll(",$", ""); // delete end comma
        	address = address.replaceAll("^,", ""); // delete beginning comma
        	
        	ContactFieldValue value = new ContactFieldValue(address, streetAddress, extendedAddress, postalCode, city, state, country);
        	
        	addField(new ContactField(ContactField.Type.ADDRESS, kind, value));
        	
        }
        
        
       
        // Social networks
        
        Set<ContactField.Kind> socialNetworksAlreadyAdded = new HashSet<ContactField.Kind>(); // To avoid repetitions with X-URL and X-ID
        
        for(String field : new String[] {"URL-", "ID:"}) { // We parse X-URL then X-ID
        	
        	matcher = Pattern.compile("\r\nX-"+field+"([^:]*):(.*)\r\n").matcher(vCard);
        	
        	while(matcher.find()){
        	    String socialNetwork = matcher.group(1);
        	    
        	    if(socialNetwork != null && !socialNetwork.isEmpty()) {
	        	    socialNetwork = socialNetwork.toUpperCase(); // case insensitive: we convert to uppercase
	        	    
	        	    ContactField.Kind kind = null;
	        	    
	        	    if(socialNetwork.equals("TWITTER"))
	        	    	kind = ContactField.Kind.TWITTER;
	        	    else if(socialNetwork.equals("LINKEDIN"))
	        	    	kind = ContactField.Kind.LINKED_IN;
	        	    else if(socialNetwork.equals("SKYPE"))
	        	    	kind = ContactField.Kind.SKYPE;
	        	    else if(socialNetwork.equals("FACEBOOK"))
	        	    	kind = ContactField.Kind.FACEBOOK;
	        	    
	        	    if(kind != null && !socialNetworksAlreadyAdded.contains(kind)) { // If the social network has been recognized and not added yet
	        	    	addField(new ContactField(ContactField.Type.SOCIAL_NETWORK, kind, matcher.group(2)));
	        	    	socialNetworksAlreadyAdded.add(kind);
	        	    }
        	    }
        	}	
        }    
    }
    
	
	public void setFullName(String fullName) {
		this.fullName = fullName.replace("\\", ""); // slashes may have been added by the API
	}
    
	public void setNames(String name) {
		// Sets the firstname and the lastname from the N field
		
		String[] information = name.split(";");
		
		if(information.length >= 2) {
			this.firstName = information[1];
			this.lastName = information[0];
		}
	}
        
	public void setOrganization(String organization) {
		this.organization = organization.replace("\\", "");
	}
	
	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition.replace("\\", "");
	}
	
	public void addField(ContactField field) {
		if(field.getValue() != null)
			fields.add(field);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getOrganization() {
		return organization;
	}
	
	public String getJobPosition() {
		return jobPosition;
	}
	
	public List<ContactField> getFields() {
		return fields;
	}
	
	public String debugInfo() {
		String result = "";
		
		result += "******************\n";
		result += "* Contact object *\n";
		result += "******************\n";
		result += "Firstname: " + firstName + "\n";
		result += "Lastname: " + lastName + "\n";
		result += "Full name: " + fullName + "\n";
		result += "Organization: " + organization + "\n";
		result += "Job position: " + jobPosition + "\n";
		
		for(ContactField field : fields) {
			result += "Field: " + field.getType() + " " + field.getKind() + " " + field.getValue() + "\n";
			if(field.getType() == ContactField.Type.ADDRESS) {
				ContactFieldValue value = field.getValue();
				result += "Street address: " + value.getStreetAddress() + ", extended address: " + value.getExtendedAddress() + ", postalCode: " + value.getPostalCode() + ", city: " + value.getCity() + ", state: " + value.getState() + ", country: " + value.getCountry() + "\n";
			}
		}

		result += "******************" + "\n";
		
		return result;
		
	}
	
}
