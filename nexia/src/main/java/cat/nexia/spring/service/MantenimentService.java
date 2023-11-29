package cat.nexia.spring.service;

import cat.nexia.spring.models.Manteniment;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface del servei de manteniment
 */

public interface MantenimentService {

    void guardarManteniment(LocalDate dia, Long idPista);

    List<Manteniment> findMantenimentByDia (LocalDate dia);

    List<Manteniment> findAllbyDiaAndPista (LocalDate dia, Long pista);

    Integer countAllByDiaAndPista (LocalDate dia, Long pista);
}
