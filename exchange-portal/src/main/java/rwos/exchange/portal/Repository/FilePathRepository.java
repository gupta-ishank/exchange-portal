package rwos.exchange.portal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rwos.exchange.portal.Entity.FilePath;

public interface FilePathRepository extends JpaRepository<FilePath, Long>{
    public FilePath findByPath(String path);
}
