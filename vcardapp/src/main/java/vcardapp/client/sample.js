var RequestHelper = require('./RequestHelper');
var Client = require('./Client');
var ResponseHandler = require('./ResponseHandler');
var Contact = require('./Contact');

/* 
	Sample code.
	It is expected that the example of the extended vCard will be displayed first since 
	the first example is called asynchronously (node.js' habit) and will take more time 
	to execute due to the http request.
*/

var requestHelper = new RequestHelper().withSubject("Le sujet de mon mail").withHeaderFrom("terrasson@kwaga.com").withHeaderTo("doctrinal@kwaga.com").withContent("Salut Thomas,\nVoici le rapport.\n \n-- \nMarc Terrasson,\nIT, Kwaga\nMail: terrasson@kwaga.com\nEurope: +33 6-08-57-53-44");

var client = Client.getDefault("myLogin", "myPassword");

client.execute(requestHelper.build(), function(responseHandler) { // asynchronous (callback function) : node.js' way of developing
	if(responseHandler.status == ResponseHandler.ResponseStatus.SUCCEED && responseHandler.hasDetectedContact()) {
		var detectedContact = responseHandler.detectedContact;
		
		console.log("We have a Contact: " + detectedContact.firstName+ "\n");
		console.log("VCard: " + responseHandler.detectedContactAsVCard + "\n");
    
		console.log(detectedContact.debugInfo());
	} else {
		console.log("marc");
		console.log(responseHandler.errors);
	}
});


// Example of parsing with an extended vCard

var vCard = "BEGIN:VCARD\r\nVERSION:3.0\r\nN:De Forsan;Anne;;;\r\nFN:Anne De Forsan\r\nNAME:Anne De Forsan\r\nPROFILE:VCARD\r\nSOURCE:http://www.kwaga.com\r\nROLE:Founder & CEO\r\nORG:Storiesout\r\nURL:http\\://fr.linkedin.com/in/annedeforsan\r\nURL:http\\://www.StoriesOut.com\r\nURL:http\\://www.twitter.com/adeforsan\r\nADR:;;7 passage du Chantier;Paris;;75012;Not specified\r\nTEL;TYPE=CELL:+33 6 07 67 30 38\r\nTEL:+33 9 81 86 41 81\r\nEMAIL:annedeforsan@storiesout.com\r\nX-URL-TWITTER:http://www.twitter.com/adeforsan\r\nX-ID:twitter:adeforsan\r\nX-URL-LINKEDIN:http://fr.linkedin.com/in/annedeforsan\r\nX-ID:skype:achidaye\r\nEND:VCAR";
var contact = new Contact(vCard);

console.log(contact.debugInfo());
