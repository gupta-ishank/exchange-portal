package rwos.exchange.portal.Entity;

public class LoginSignupResponse {
	private boolean status;
	
	public LoginSignupResponse(){}
	
	public LoginSignupResponse(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
