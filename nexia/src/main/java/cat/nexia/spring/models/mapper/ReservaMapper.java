package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.ReservaDto;
import cat.nexia.spring.models.Reserva;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe Mapper de Reserva a ReservaDto
 */
public class ReservaMapper {

    private static final Log LOG = LogFactory.getLog(ReservaMapper.class);

    /**
     * Passa de Reserva a ReservaDto
     * @param reserva Reserva
     * @return retorna una ReservaDto
     */
    public static ReservaDto toReservaDto(Reserva reserva) {

        if (reserva == null){
            return null;
        }
        LOG.info("Mapejant la clase Reserva a ReservaDto");
        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setIdReserva(reserva.getIdReserva());
        reservaDto.setDia(reserva.getDia());
        reservaDto.setNumPista(reserva.getPista().getNumPista());
        reservaDto.setIniHora(reserva.getHorari().getIniHora());
        reservaDto.setFiHora((reserva.getHorari().getFiHora()));
        reservaDto.setEmail(reserva.getUser().getEmail());
        reservaDto.setName(reserva.getUser().getName());

        return reservaDto;
    }


}
