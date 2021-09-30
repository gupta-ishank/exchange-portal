package rwos.exchange.portal.Entity;

//import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
	
	 	@Id
	   	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	     
	    @Column(nullable = false, unique = true, length = 45)
	    private String email;
	     
	    @Column(nullable = false, length = 64)
	    private String password;
	     
	    @Column(name = "username", nullable = false, length = 20)
	    private String userName;

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
	     
	    
	
}
