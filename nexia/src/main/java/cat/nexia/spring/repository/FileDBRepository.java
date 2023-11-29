package cat.nexia.spring.repository;

import cat.nexia.spring.models.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Interface repository entitat FileDb
 */
@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
}
