package cat.nexia.spring.repository;

import cat.nexia.spring.models.Missatge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissatgeRepository extends JpaRepository<Missatge, Long>{
    
}
