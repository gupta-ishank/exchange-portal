package rwos.exchange.portal.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rwos.exchange.portal.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    public List<User> findByUserNameAndPassword(String userName, String password);
    
    public User findByEmail(String email);
}
