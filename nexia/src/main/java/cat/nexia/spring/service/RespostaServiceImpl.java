package cat.nexia.spring.service;

import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.models.Resposta;
import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.MissatgeRepository;
import cat.nexia.spring.repository.RespostaRepository;
import cat.nexia.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Resposta crearResposta(Long missatgeId, Long userId, String content) {
        Missatge missatge = missatgeRepository.findById(missatgeId).orElse(null);
        User usuario = userRepository.findById(userId).orElse(null);

        if (missatge == null) {
            System.out.println("El missatge no s'ha trobat");

        } else if (usuario == null) {
            System.out.println("L'usuari no s'ha trobat");

        } else {
            Resposta respuesta = new Resposta(content, usuario, missatge);
            return respuestaRepository.save(respuesta);
        }
        return null;

    }

    @Override
    public List<Resposta> getRespuestasByMissatgeId(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

}
