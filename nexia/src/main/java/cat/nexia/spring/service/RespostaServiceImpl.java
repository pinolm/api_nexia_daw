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

/**
 * Implementació de la interface RespostaService
 */
@Service
public class RespostaServiceImpl implements RespostaService {

    private final RespostaRepository respuestaRepository;
    private final UserRepository userRepository;
    private final MissatgeRepository missatgeRepository;

    /**
     * Constructor de la classe, inicialitza els repositoris
     * @param respuestaRepository repositori de Resposta
     * @param userRepository repositori de User
     * @param missatgeRepository repositori de Missatge
     */

    @Autowired
    public RespostaServiceImpl(RespostaRepository respuestaRepository,
            UserRepository userRepository,
            MissatgeRepository missatgeRepository) {
        this.respuestaRepository = respuestaRepository;
        this.userRepository = userRepository;
        this.missatgeRepository = missatgeRepository;
    }

    /**
     * Buscar missatge per id de missatge
     * @param missatgeId id de missatge
     * @return una llista de Resposta
     */

    @Override
    public List<Resposta> obtenerRespuestasPorMissatge(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

    /**
     * Guardar una resposta a bbd
     * @param missatgeId id del missatge
     * @param userId id de l'usuari
     * @param content contingut del missatge
     * @return retorna una Resposta si es guarda i null en cas contrari.
     */

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

    /**
     * Llista de Respostes per id de missatge
     * @param missatgeId id de missatge
     * @return llista de respostes
     */

    @Override
    public List<Resposta> getRespuestasByMissatgeId(Long missatgeId) {
        return respuestaRepository.findByMissatgeId(missatgeId);
    }

}
