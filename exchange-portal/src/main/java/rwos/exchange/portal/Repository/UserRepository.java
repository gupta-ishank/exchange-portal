package rwos.exchange.portal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rwos.exchange.portal.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    public User findByUserNameAndPassword(String userName, String password);
}
