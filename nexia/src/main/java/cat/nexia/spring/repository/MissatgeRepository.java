package cat.nexia.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.nexia.spring.models.Missatge;

@Repository
public interface MissatgeRepository extends JpaRepository<Missatge, Long>{
    
}
