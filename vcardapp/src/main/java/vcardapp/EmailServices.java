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
			@QueryParam("to") String to, @QueryParam("content") String content,
			@QueryParam("mode") String mode,@QueryParam("strategy") String strategy ){
		RequestHelper requestHelper = new RequestHelper().withSubject(subject).withHeaderFrom(from).withHeaderTo(to).withContent(content).withDate(new Date()).withAccuracy(RequestHelper.Accuracy.HIGH);
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
}
