package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class FilePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;

    @ManyToMany
    @JoinTable(
        name = "role_file_path_mapp",
        joinColumns = @JoinColumn(
            name = "file_path_id",
            referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "role_id",
            referencedColumnName = "id"
        )
    )
    private List<Role> roles;

    public void addRole(Role role){
        if(role == null) roles = new ArrayList<>();
        roles.add(role);
    }

    public FilePath() {
    }
    public FilePath(String path) {
        this.path = path;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    

}
