package cat.nexia.spring.service;

import cat.nexia.spring.models.Respuesta;

import java.util.List;

public interface RespuestaService {
    
    List<Respuesta> obtenerRespuestasPorMissatge(Long missatgeId);
    Respuesta crearRespuesta(Long missatgeId, Long userId, String contenido);
}
