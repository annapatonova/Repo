package vcardapp;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import vcardapp.client.Client;
import vcardapp.client.Contact;
import vcardapp.client.DefaultResponseHandler;
import vcardapp.client.RequestHelper;
import vcardapp.client.ResponseHandler;

import com.google.gson.Gson;

@Path("/EmailService")
public class EmailServices {


	@GET
	@Path("/contact")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContact(@QueryParam("subject") String subject, @QueryParam("from") String from,
			@QueryParam("to") String to, @QueryParam("cc") String cc, 
			@QueryParam("content") String content,@QueryParam("mode") String mode,@QueryParam("strategy") String strategy ){
		RequestHelper requestHelper = new RequestHelper().withSubject(subject).withHeaderFrom(from).withHeaderTo(to).withContent(content).withAttachedFile("fichier.pdf").withDate(new Date()).withAccuracy(RequestHelper.Accuracy.HIGH);
		Client client = Client.getDefault("telecometude", "MWLY2JagzLPafY");
		ResponseHandler responseHandler = new DefaultResponseHandler();

		client.execute(requestHelper.build(), responseHandler);

		if(responseHandler.getStatus() == ResponseHandler.ResponseStatus.SUCCEED && responseHandler.hasDetectedContact()) {
			Contact detectedContact = responseHandler.getDetectedContact();


			Gson gson = new Gson();

			return gson.toJson(detectedContact);

		}
	
	return "{\"status\" : \"KO\"}";
	
	}



	@GET
	@Path("/contact2")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContact2(){
		String vCard = "BEGIN:VCARD\r\nVERSION:3.0\r\nN:De�Forsan;Anne;;;\r\nFN:Anne�De�Forsan\r\nNAME:Anne�De�Forsan\r\nPROFILE:VCARD\r\nSOURCE:http://www.kwaga.com\r\nROLE:Founder�&�CEO\r\nORG:Storiesout\r\nURL:http\\://fr.li nkedin.com/in/annedeforsan\r\nURL:http\\:// www.StoriesOut.com\r\nURL:http\\://www.twitter.com/adeforsan\r\nADR:;;7�passage�du�Chantier;Paris;;75012;Not�specified\r\nTEL;TYPE=CELL:+33�6�07�67�30 38\r\nTEL:+33�9�81�86�41 81\r\nEMAIL:annedeforsan@storiesout.com\r\nX-URL-TWITTER:http://www.twitter.com/adeforsan\r\nX-ID:twitter:adeforsan\r\nX-URL-LINKEDIN:http://fr.linkedin.com/in/annedeforsan\r\nX-ID:skype:achidaye\r\nEND:VCAR";
		Contact  contact = new Contact(vCard);
		Gson gson = new Gson();

		return gson.toJson(contact);
	}

}
