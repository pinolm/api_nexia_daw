package cat.nexia.spring.repository;

import cat.nexia.spring.models.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Interface repository entitat Resposta
 */
public interface RespostaRepository extends JpaRepository<Resposta, Long>{
    
    List<Resposta> findByMissatgeId(Long missatgeId);
}
