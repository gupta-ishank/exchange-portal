package rwos.exchange.portal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rwos.exchange.portal.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    public Role findByName(String name);
}
