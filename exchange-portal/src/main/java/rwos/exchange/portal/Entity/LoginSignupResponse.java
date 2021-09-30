package rwos.exchange.portal.Entity;

public class LoginSignupResponse {
	private String status;
	
	public LoginSignupResponse(){}
	
	public LoginSignupResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
