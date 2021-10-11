package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;



public class Menu {
    private String name = "";
    private String path = "";
    private int type;
    private String description;
    private Object subDescription;
    
	
	private List<Menu> childs =  new ArrayList<>();
    private Object schema;
    
    
    public Menu() {
    }
    public Menu(String name, String path, String description) {
        this.name = name;
        this.path = path;
        this.description = description;
    }

    
	public Menu(String name, String path, int type, List<Menu> childs) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
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
    public List<Menu> getChilds() {
        return childs;
    }

    public void setChilds(List<Menu> childs) {
        this.childs = childs;
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }
    public Object getSchema() {
        return schema;
    }
    
    public void setSubDescription(Object subDescription) {
		this.subDescription = subDescription;
	}
    
    public Object getSubDescription() {
		return subDescription;
	}
    
}
