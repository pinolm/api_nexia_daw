package cat.nexia.spring.service;

import cat.nexia.spring.models.Manteniment;
import cat.nexia.spring.repository.MantenimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementació de la interface MantenimentService. Mètodes per a gestionar l'entitat Manteniment.
 *
 */
@Service
public class MantenimentServiceImpl implements MantenimentService{

    @Autowired
    private MantenimentRepository mantenimentRepository;

    /**
     * Guardar manteniment
     * @param dia dia del manteniment
     * @param idPista id de la pista
     */
    @Override
    public void guardarManteniment(LocalDate dia, Long idPista) {
        mantenimentRepository.insertarManteniment(dia, idPista);

    }

    /**
     * Buscar manteniment per dia
     * @param dia dia a buscar
     * @return LLista amb els manteniments existents per dia escollit
     */

    @Override
    public List<Manteniment> findMantenimentByDia(LocalDate dia) {
        return mantenimentRepository.findAllByDia(dia);
    }

    /**
     * Bucar manteniment per dia i pista
     * @param dia dia a buscar
     * @param pista id de la pista a buscar
     * @return llista d'objectes del tipus Manteniment
     */

    @Override
    public List<Manteniment> findAllbyDiaAndPista(LocalDate dia, Long pista) {
        return mantenimentRepository.findAllByDiaAndPista(dia, pista);
    }

    /**
     * Comptar quants manteniments existen per dia i pista
     * @param dia data a buscar
     * @param pista id de la pista a buscar
     * @return número de reserves existents per dia i pista
     */

    @Override
    public Integer countAllByDiaAndPista(LocalDate dia, Long pista) {
        return mantenimentRepository.countAllByDiaAndPista(dia, pista);
    }
}
