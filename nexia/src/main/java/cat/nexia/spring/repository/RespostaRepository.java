package cat.nexia.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import cat.nexia.spring.models.Resposta;

public interface RespostaRepository extends JpaRepository<Resposta, Long>{
    
    List<Resposta> findByMissatgeId(Long missatgeId);
}
