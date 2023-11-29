package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.AllReservesResponseDto;
import cat.nexia.spring.models.Reserva;

/**
 * Classe Mapper amb correspondència Reserva amb AllReservesResponseDto
 */
public class AllReservaMapper {

    /**
     * Des de Reserva es crea un DTO AllReservesResponseDto
     * @param reserva Reserva
     * @return AllReservesResponseDto
     */

    public static AllReservesResponseDto toReservaDto(Reserva reserva) {

        if (reserva == null) {
            return null;
        }

        return new AllReservesResponseDto(reserva.getDia(), reserva.getIdReserva(),
                PistaMapper.pistaToPistaDto(reserva.getPista()), HorariMapper.toHorariDto(reserva.getHorari()),
                UserSimpleMapper.toUserSimpleDto(reserva.getUser()));
    }

}
