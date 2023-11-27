package cat.nexia.spring.service;

import cat.nexia.spring.models.Resposta;

import java.util.List;

public interface RespostaService {

    List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId);

    Resposta crearResposta(Long missatgeId, Long userId, String content);

    List<Resposta> getRespuestasByMissatgeId(Long missatgeId);
}
