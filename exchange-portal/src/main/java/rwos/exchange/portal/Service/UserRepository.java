package rwos.exchange.portal.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rwos.exchange.portal.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
		
	@Query(value = "Select * from users where username=?1", nativeQuery=true)
	public User getData(String username);
	
	@Query(value = "Select * from users where email=?1", nativeQuery=true)
	public User isUserPresent(String email);
	
}