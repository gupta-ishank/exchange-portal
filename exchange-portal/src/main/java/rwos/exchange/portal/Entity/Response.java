package rwos.exchange.portal.Entity;

public class Response {
	private String content;
	private boolean status;
	
	public Response(){}

	
	public Response(String content) {
		this.content = content;
	}

	public Response(boolean status) {
		this.status = status;
	}

	public String getData() {
		return content;
	}

	public void setData(String data) {
		this.content = data;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
