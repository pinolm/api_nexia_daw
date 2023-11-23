package cat.nexia.spring.service;

import cat.nexia.spring.models.Resposta;

import java.util.List;

public interface RespuestaService {
    
    List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId);
    Resposta crearRespuesta(Long missatgeId, Long userId, String contenido);
}
