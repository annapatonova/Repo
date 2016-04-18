package vcardapp.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHelper {

	private String subject;
	private String headerFrom;
	private String headerTo;
	private List<String> headerCC;
	private String date;
	private String content;
	private String analysisStrategy;
	private String addressingMode;
	private List<String> attachedFiles;
	
	public enum Accuracy {
		LOW, NORMAL, HIGH
	}
	
	public enum Addressing {
		EXPLICIT_TO, EXPLICIT_FROM, OTHER
	}
	
	public RequestHelper() {
		// We set default values
		
		withAccuracy(Accuracy.HIGH);
		withSubject("");
		withDate(new Date());
		withHeaderFrom("");
		withHeaderTo("");
		headerCC = new ArrayList<String>();
		withAddressingMode(Addressing.EXPLICIT_TO);
		attachedFiles = new ArrayList<String>();
		withContent("");
	}
	
	public Request build()	{
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("Date", date);
		parameters.put("Subject", subject);
		parameters.put("HeaderFrom", headerFrom);
		parameters.put("HeaderTo", headerTo);
		parameters.put("Content", content);
		parameters.put("AddressingMode", addressingMode);
		parameters.put("AnalysisStrategy", analysisStrategy);
		
		if(headerCC.isEmpty()) {
			parameters.put("HeaderCC[]", "");
		} else {
			for(int i = 0; i < headerCC.size(); i++) {
				parameters.put("HeaderCC["+i+"]", headerCC.get(i));
			}
		}
		
		if(attachedFiles.isEmpty()) {
			parameters.put("AttachedFiles[]", "");
		} else {
			for(int i = 0; i < attachedFiles.size(); i++) {
				parameters.put("AttachedFiles["+i+"]", attachedFiles.get(i));
			}
		}
		
        return new Request(parameters);
	}
	
	public RequestHelper withAccuracy(Accuracy accuracy) {
		switch(accuracy) {
			case LOW:
				analysisStrategy = "KWAGA_GADGET";
				break;
			case NORMAL:
				analysisStrategy = "WTN_EVERYWHERE";
				break;
			case HIGH:
				analysisStrategy = "KWAGA_CORE";
				break;
		}
		
		return this;
	}
	
	public RequestHelper withDate(Date date) {
		this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return this;
	}
	
	public RequestHelper withSubject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public RequestHelper withContent(String content) {
		this.content = content;
		return this;
	}
	
	public RequestHelper withHeaderFrom(String headerFrom) {
		this.headerFrom = headerFrom;
		return this;
	}
	
	public RequestHelper withHeaderTo(String headerTo) {
		this.headerTo = headerTo;
		return this;
	}
	
	public RequestHelper withHeaderCC(String headerCC) {
		if(headerCC != null)
			this.headerCC.add(headerCC);
		
		return this;
	}
	
	public RequestHelper withAddressingMode(Addressing mode) {
		
		switch(mode) {
			case EXPLICIT_FROM:
				this.addressingMode = "EXPLICIT_FROM";
				break;
			case EXPLICIT_TO:
				this.addressingMode = "EXPLICIT_TO";
				break;
			default:
				this.addressingMode = "OTHER";
		}
		
		return this;
	}
	
	public RequestHelper withAttachedFile(String file) {
		if(file != null)
			this.attachedFiles.add(file);
		return this;
	}	
}
