package cat.nexia.spring.service;

import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.models.Respuesta;
import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.MissatgeRepository;
import cat.nexia.spring.repository.RespuestaRepository;
import cat.nexia.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespuestaServiceImpl implements RespuestaService{

    private final RespuestaRepository respuestaRepository;
    private final UserRepository userRepository;
    private final MissatgeRepository missatgeRepository;

    @Autowired
    public RespuestaServiceImpl(RespuestaRepository respuestaRepository, 
                                UserRepository userRepository,
                                MissatgeRepository missatgeRepository) {
        this.respuestaRepository = respuestaRepository;
        this.userRepository = userRepository;
        this.missatgeRepository = missatgeRepository;
    }

    @Override
    public List<Respuesta> obtenerRespuestasPorMissatge(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

    @Override
    public Respuesta crearRespuesta(Long missatgeId, Long userId, String contenido) {
        Missatge missatge = missatgeRepository.findById(missatgeId).orElse(null);
        User usuario = userRepository.findById(userId).orElse(null);

        if (missatge == null || usuario == null) {
            return null;
        }

        Respuesta respuesta = new Respuesta(contenido, usuario, missatge);
        return respuestaRepository.save(respuesta);
    }

}
