package cat.nexia.spring.service;

import java.util.List;
import cat.nexia.spring.models.Respuesta;

public interface RespuestaService {
    
    List<Respuesta> obtenerRespuestasPorMissatge(Long missatgeId);
    Respuesta crearRespuesta(Long missatgeId, Long userId, String contenido);
}
