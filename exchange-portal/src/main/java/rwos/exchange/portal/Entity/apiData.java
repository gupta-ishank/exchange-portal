package rwos.exchange.portal.Entity;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToMany;

//import org.hibernate.annotations.GenericGenerator;

public class ApiData {
	
	@Value("${mypath}")
	private String mypath;
	
	private String name;
	private String route;
	private Integer type;
//	private List<String> files = new ArrayList<>();
	private List<ApiData> children = new ArrayList<>();

	public ApiData() {}
	
	public ApiData(String name, List<String> files, String route, Integer type) {
		this.name = name;
		this.route = route;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<String> getFiles() {
//		return files;
//	}
//
//	public void setFiles(List<String> files) {
//		this.files = files;
//	}
	
//	public void addFiles(String str) {
//		this.files.add(str);
//	}

	public List<ApiData> getChildren() {
		return children;
	}

	public void setChildren(List<ApiData> children) {
		this.children = children;
	}

	public void addChildren(ApiData apidata) {
		this.children.add(apidata);
	}
	
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	
//	public void addRoute(String route) {
//		this.route += "/" + route;
//	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
