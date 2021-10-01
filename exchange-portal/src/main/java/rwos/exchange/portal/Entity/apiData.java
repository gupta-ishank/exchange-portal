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
	
	private String name;
	private String path;
	private Integer type;
	private List<ApiData> childs = new ArrayList<>();

	public ApiData() {}
	
	public ApiData(String name, String path, Integer type) {
		this.name = name;
		this.path = path;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ApiData> getChilds() {
		return childs;
	}

	public void setChildren(List<ApiData> childs) {
		this.childs = childs;
	}

	public void addChildren(ApiData apidata) {
		this.childs.add(apidata);
	}
	
	public String getRoute() {
		return path;
	}

	public void setRoute(String path) {
		this.path = path;
	}
	
//	public void addRoute(String path) {
//		this.path += "/" + path;
//	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
