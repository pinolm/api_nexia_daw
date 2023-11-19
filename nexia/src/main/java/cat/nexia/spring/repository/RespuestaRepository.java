package cat.nexia.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import cat.nexia.spring.models.Respuesta;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{
    
    List<Respuesta> findByMissatgeId(Long missatgeId);
}
