package cat.nexia.spring.repository;

import cat.nexia.spring.models.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{
    
    List<Respuesta> findByMissatgeId(Long missatgeId);
}
