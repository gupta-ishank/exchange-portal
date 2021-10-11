package rwos.exchange.portal.Entity;

public class YamlParser {
	
	private Object RequestPayloadDetails;
	private Object ResponsePayloadDetails;
	private Object ParameterPayloadDetails;
	public Object getParameterPayloadDetails() {
		return ParameterPayloadDetails;
	}
	
	public YamlParser() {
		
	}
	public Object getRequestPayloadDetails() {
		return RequestPayloadDetails;
	}
	public void setRequestPayloadDetails(Object requestPayloadDetails) {
		RequestPayloadDetails = requestPayloadDetails;
	}
	public Object getResponsePayloadDetails() {
		return ResponsePayloadDetails;
	}
	public void setResponsePayloadDetails(Object responsePayloadDetails) {
		ResponsePayloadDetails = responsePayloadDetails;
	}
	public void setParameterPayloadDetails(Object parameterPayloadDetails) {
		ParameterPayloadDetails = parameterPayloadDetails;
	}
	
	
}
