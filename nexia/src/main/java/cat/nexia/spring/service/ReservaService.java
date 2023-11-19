package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.AllReservasResponseDto;
import cat.nexia.spring.models.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    List<AllReservasResponseDto> findReservaByDia (LocalDate dia);

   List<AllReservasResponseDto> findAll();

    Reserva findReservaById (Long idReserva);

    List<AllReservasResponseDto> findReservaByUserId (Long idUser);

    void guardarReserva (Reserva reserva);

    Reserva findReservaByIdPistaAndIdHorariAndDia (Reserva reserva);

    void eliminarReservaById (Long idReserva);


}
