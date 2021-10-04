package rwos.exchange.portal.Entity;

public class YamlParser {
	
	private String path;
	private String method;
	private String descritpion;
	public YamlParser(String path, String method, String descritpion) {
		this.path = path;
		this.method = method;
		this.descritpion = descritpion;
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
	public String getDescritpion() {
		return descritpion;
	}
	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
	
}
