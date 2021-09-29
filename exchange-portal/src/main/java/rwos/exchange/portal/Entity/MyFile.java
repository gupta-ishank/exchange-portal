package rwos.exchange.portal.Entity;

public class MyFile{
    private String name;
    private int type;
    public MyFile(String name, int type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }  
}
