package cat.nexia.spring.service;

import java.util.List;
import cat.nexia.spring.models.Resposta;

public interface RespostaService {

    List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId);

    Resposta crearRespuesta(Long missatgeId, Long userId, String contenido);

    List<Resposta> getRespuestasByMissatgeId(Long missatgeId);
}
