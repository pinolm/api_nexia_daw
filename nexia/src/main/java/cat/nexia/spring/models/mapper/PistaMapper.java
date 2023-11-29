package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.PistaDto;
import cat.nexia.spring.models.Pista;

/**
 * Classe Mapper per passar de Pista a PistaDto
 */
public class PistaMapper {

    /**
     * Mètode que transforma una Pista amb una PistaDto
     * @param pista Pista
     * @return PistaDto
     */
    public static PistaDto pistaToPistaDto (Pista pista) {
        return new PistaDto(pista.getNumPista());

    }
}
