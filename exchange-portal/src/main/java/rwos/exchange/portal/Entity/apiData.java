package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class apiData {
	
	@Id
	@GeneratedValue(generator = "book_seq")
	@GenericGenerator(
			name = "book_seq",
			strategy = "rwos.exchange.portal.Generator.StringPrefixedSequenceIdGenerator"
	)
	private String id;
	private String name;
	private String route;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="parent_id", referencedColumnName = "id")
	private List<apiData> children = new ArrayList<>();
	
	public apiData() {}
	
	public apiData(String id, String name, String route) {
		this.id = id;
		this.name = name;
		this.route = route;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public List<apiData> getChildren() {
		return children;
	}

	public void setChildren(List<apiData> children) {
		this.children = children;
	}
	
	
	
}
