package rwos.exchange.portal.Entity;

public class ResponseStatus{
	private Object response;
	private Object responseDetails;
	private Object successExample;
	private Object failureExample;

	public ResponseStatus() {
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public Object getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(Object responseDetails) {
		this.responseDetails = responseDetails;
	}

	public Object getSuccessExample() {
		return successExample;
	}

	public void setSuccessExample(Object successExample) {
		this.successExample = successExample;
	}

	public Object getFailureExample() {
		return failureExample;
	}

	public void setFailureExample(Object failureExample) {
		this.failureExample = failureExample;
	}
	
	
}
