package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;



public class Menu {
    private String name = "";
    private String path = "";
    private int type;
    private List<Menu> childs =  new ArrayList<>();
    
    public Menu() {
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

    public List<Menu> getChilds() {
        return childs;
    }

    public void setChilds(List<Menu> childs) {
        this.childs = childs;
    }

}
