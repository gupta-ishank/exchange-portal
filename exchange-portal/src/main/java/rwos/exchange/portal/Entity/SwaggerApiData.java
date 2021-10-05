package rwos.exchange.portal.Entity;

public class SwaggerApiData {
	private String path;
	private String method;
	private Object paramaters;
	private Object response;
	
	public SwaggerApiData(){}
	
	public SwaggerApiData(String path, String method, Object response, Object parameters){
		this.path = path;
		this.method = method;
		this.response = response;
		this.paramaters = parameters;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getParamaters() {
		return paramaters;
	}

	public void setParamaters(Object paramaters) {
		this.paramaters = paramaters;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
	
}
