package cat.nexia.spring.service;

import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.repository.MissatgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementació de la interface MissatgeService
 */
@Service
public class MissatgeServiceImpl implements MissatgeService {

    private final MissatgeRepository missatgeRepository;

    /**
     * Constructor injectant MissatgeRepository
     * @param missatgeRepository
     */

    @Autowired
    public MissatgeServiceImpl(MissatgeRepository missatgeRepository) {
        this.missatgeRepository = missatgeRepository;
    }

    /**
     * Obtenir tots els missatges de la bbdd
     * @return llista amb tots el missatges
     */

    @Override
    public List<Missatge> getAllMissatges() {
        return missatgeRepository.findAll();
    }

    /**
     * Obtenir missatges pel seu Id
     * @param id id del missatge
     * @return objecte del tipus Missatge
     */

    @Override
    public Missatge getMissatgeById(Long id) {
        return missatgeRepository.findById(id).orElse(null);
    }

    /**
     * Crear un missatge i persisitir-lo en bbdd
     * @param missatge instància de la classe Missatge
     * @return missatge guardat
     */

    @Override
    public Missatge createMissatge(Missatge missatge) {
        return missatgeRepository.save(missatge);
    }

    /**
     * Esborrar un missatge de la bbdd
     * @param id identificador del missatge a eliminar
     */

    @Override
    public void deleteMissatge(Long id) {
        missatgeRepository.deleteById(id);
    }
}
