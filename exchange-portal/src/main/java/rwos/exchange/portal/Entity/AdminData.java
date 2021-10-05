package rwos.exchange.portal.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AdminData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	private String filePath;
	private Integer roleAssigned;
	
	AdminData(){}
	
	AdminData(String filePath, Integer roleAssigned){
		this.filePath = filePath;
		this.roleAssigned = roleAssigned;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getRole() {
		return roleAssigned;
	}

	public void setRole(Integer roleAssigned) {
		this.roleAssigned = roleAssigned;
	}
	
	
}
