package rwos.exchange.portal.Entity;

public class YamlParser {

    private Object requestPayload = "";
    private Object requestPayloadExample = "";
    private Object requestPayloadDetails = "";

    private Object parameterPayload = "";
    private Object parameterPayloadExample = "";
    private Object parameterPayloadDetails = "";
    private Object responsePayloadExample = "";
    private Object security = "";
    private ResponseStatus responsePayload;

    public YamlParser() {

    }

    public Object getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(Object requestPayload) {
        this.requestPayload = requestPayload;
    }

    public Object getRequestPayloadDetails() {
        return requestPayloadDetails;
    }

    public void setRequestPayloadDetails(Object requestPayloadDetails) {
        this.requestPayloadDetails = requestPayloadDetails;
    }

    public Object getParameterPayload() {
        return parameterPayload;
    }

    public void setParameterPayload(Object parameterPayload) {
        this.parameterPayload = parameterPayload;
    }

    public Object getParameterPayloadDetails() {
        return parameterPayloadDetails;
    }

    public void setParameterPayloadDetails(Object parameterPayloadDetails) {
        this.parameterPayloadDetails = parameterPayloadDetails;
    }

    public Object getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(ResponseStatus responsePayload) {
        this.responsePayload = responsePayload;
    }

    public Object getRequestPayloadExample() {
        return requestPayloadExample;
    }

    public void setRequestPayloadExample(Object requestPayloadExample) {
        this.requestPayloadExample = requestPayloadExample;
    }

    public Object getParameterPayloadExample() {
        return parameterPayloadExample;
    }

    public void setParameterPayloadExample(Object parameterPayloadExample) {
        this.parameterPayloadExample = parameterPayloadExample;
    }

    public Object getResponsePayloadExample() {
        return responsePayloadExample;
    }

    public void setResponsePayloadExample(Object responsePayloadExample) {
        this.responsePayloadExample = responsePayloadExample;
    }

    public Object getSecurity() {
        return security;
    }

    public void setSecurity(Object security) {
        this.security = security;
    }

}
