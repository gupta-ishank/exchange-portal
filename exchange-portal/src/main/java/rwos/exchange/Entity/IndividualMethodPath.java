package rwos.exchange.portal.Entity;

public class IndividualMethodPath {
    
    private String subPath = ""; 
    private String path = "";

    public IndividualMethodPath() {

    }
    public IndividualMethodPath(String subPath, String path) {
        this.subPath = subPath;
        this.path = path;
    }
    public String getSubPath() {
        return subPath;
    }
    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
 

}
