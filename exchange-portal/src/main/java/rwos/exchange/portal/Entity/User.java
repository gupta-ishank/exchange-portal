package rwos.exchange.portal.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;


@Entity
public class User {
	
	 	@Id
		@SequenceGenerator(
			name = "user_sequence",
			allocationSize = 1,
			initialValue = 100
		)
	   	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
	    private Long id;
	     
	    @Column(nullable = false, unique = true, length = 45)
	    private String email;
	     
	    @Column(nullable = false, length = 64)
	    private String password;
	     
	    @Column(nullable = false, length = 20)
	    private String userName;

		@ManyToMany
		@JoinTable(
			name = "role_user_mapp",
			joinColumns = @JoinColumn(
				name = "user_id",
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

		public User() {
		}

		

		public User(String email, String password, String userName) {
			this.email = email;
			this.password = password;
			this.userName = userName;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public List<Role> getRoles() {
			return roles;
		}

		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}

		
	
}
