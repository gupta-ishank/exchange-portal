package rwos.exchange.portal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rwos.exchange.portal.Entity.LoginData;

@Repository
public interface ApiDataRepository extends JpaRepository<LoginData, Long>{
	@Query(value = "Select * from login_data where username=?1", nativeQuery=true)
	public LoginData getData(String username);
	
	@Query(value = "Select * from login_data where email=?1", nativeQuery=true)
	public LoginData isUserPresent(String email);
}
