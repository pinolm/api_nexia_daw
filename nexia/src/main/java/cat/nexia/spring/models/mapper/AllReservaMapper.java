package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.AllReservasResponseDto;
import cat.nexia.spring.models.Reserva;

public class AllReservaMapper {

    public static AllReservasResponseDto toReservaDto(Reserva reserva) {

        if (reserva == null){
            return null;
        }

        return new AllReservasResponseDto(reserva.getDia(), reserva.getIdReserva(),
                PistaMapper.pistaToPistaDto(reserva.getPista()
                ), HorariMapper.toHorariDto(reserva.getHorari()),
                UserSimpleMapper.toUserSimpleDto(reserva.getUser()));
    }


}
