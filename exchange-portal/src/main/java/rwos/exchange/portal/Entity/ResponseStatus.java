package rwos.exchange.portal.Entity;

public class ResponseStatus {
    private Object success;
    private Object failure;
    private Object successDetails;
    private Object failureDetails;

    public ResponseStatus() {
    }

    public Object getSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = success;
    }

    public Object getFailure() {
        return failure;
    }

    public void setFailure(Object failure) {
        this.failure = failure;
    }

    public Object getSuccessDetails() {
        return successDetails;
    }

    public void setSuccessDetails(Object successDetails) {
        this.successDetails = successDetails;
    }

    public Object getFailureDetails() {
        return failureDetails;
    }

    public void setFailureDetails(Object failureDetails) {
        this.failureDetails = failureDetails;
    }

}
