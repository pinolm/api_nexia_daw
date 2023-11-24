package cat.nexia.spring.service;

import java.util.List;
import cat.nexia.spring.models.Resposta;

public interface RespostaService {

    List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId);

    Resposta crearResposta(Long missatgeId, Long userId, String content);

    List<Resposta> getRespuestasByMissatgeId(Long missatgeId);
}
