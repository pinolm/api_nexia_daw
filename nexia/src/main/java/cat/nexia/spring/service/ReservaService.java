package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.AllReservesResponseDto;
import cat.nexia.spring.models.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    List<AllReservesResponseDto> findReservaByDia(LocalDate dia);

    List<AllReservesResponseDto> findAll();

    Reserva findReservaById(Long idReserva);

    List<AllReservesResponseDto> findReservaByUserId(Long idUser);

    void guardarReserva(Reserva reserva);

    Reserva findReservaByIdPistaAndIdHorariAndDia(Reserva reserva);

    void eliminarReservaById(Long idReserva);

    int countReservaByDiaAndByUser(Reserva reserva);

}
