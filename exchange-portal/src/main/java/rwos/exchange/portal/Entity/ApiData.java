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
	
	private String name = "";
    private String path = "";
    private int type;
    private String description;
    private List<ApiData> childs =  new ArrayList<>();
    
    public ApiData() {
    }
    public ApiData(String name, String path, String description) {
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public ApiData(String name, String path, int type, List<ApiData> childs) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.childs = childs;
    }
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoute() {
		return path;
	}
	public void setRoute(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ApiData> getChilds() {
		return childs;
	}
	public void setChilds(List<ApiData> childs) {
		this.childs = childs;
	}
	
	public void addChildren(ApiData apidata) {
		this.childs.add(apidata);
	}
	
}
