package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.PistaDto;
import cat.nexia.spring.models.Pista;

public class PistaMapper {

    public static PistaDto pistaToPistaDto (Pista pista) {
        return new PistaDto(pista.getNumPista());

    }
}
