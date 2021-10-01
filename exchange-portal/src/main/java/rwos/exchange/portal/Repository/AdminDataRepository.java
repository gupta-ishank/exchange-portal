package rwos.exchange.portal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rwos.exchange.portal.Entity.AdminData;

public interface AdminDataRepository extends JpaRepository<AdminData, Long>{
	
}
