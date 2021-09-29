package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;



public class Menu {
    private String name = "";
    private String path = "";
    private int type;
    private List<MyFile> files = new ArrayList<>();
    private List<Menu> childs =  new ArrayList<>();
    
    public Menu() {
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

    public List<MyFile> getFiles() {
        return files;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    public List<Menu> getChilds() {
        return childs;
    }

    public void setChilds(List<Menu> childs) {
        this.childs = childs;
    }

    public void addFiles(MyFile file){
        if(this.childs == null) this.files = new ArrayList<>();
        else this.files.add(file);
    }
}
