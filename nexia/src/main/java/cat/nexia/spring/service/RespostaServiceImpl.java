package cat.nexia.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.models.Resposta;
import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.MissatgeRepository;
import cat.nexia.spring.repository.RespostaRepository;
import cat.nexia.spring.repository.UserRepository;

@Service
public class RespostaServiceImpl implements RespostaService {

    private final RespostaRepository respuestaRepository;
    private final UserRepository userRepository;
    private final MissatgeRepository missatgeRepository;

    @Autowired
    public RespostaServiceImpl(RespostaRepository respuestaRepository,
            UserRepository userRepository,
            MissatgeRepository missatgeRepository) {
        this.respuestaRepository = respuestaRepository;
        this.userRepository = userRepository;
        this.missatgeRepository = missatgeRepository;
    }

    @Override
    public List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

    @Override
    public Resposta crearRespuesta(Long missatgeId, Long userId, String contenido) {
        Missatge missatge = missatgeRepository.findById(missatgeId).orElse(null);
        User usuario = userRepository.findById(userId).orElse(null);

        if (missatge == null || usuario == null) {
            return null;
        }

        Resposta respuesta = new Resposta(contenido, usuario, missatge);
        return respuestaRepository.save(respuesta);
    }

    @Override
    public List<Resposta> getRespuestasByMissatgeId(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

}
