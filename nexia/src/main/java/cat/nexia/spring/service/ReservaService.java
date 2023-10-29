package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.ReservaDto;
import cat.nexia.spring.models.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    List<Reserva> findReservaByDia (LocalDate dia);

    List<Reserva> findAll ();

    ReservaDto findReservaById (Long idReserva);

    void guardarReserva (Reserva reserva);

    Reserva findReservaByIdPistaAndIdHorariAndDia (Reserva reserva);

    void eliminarReservaById (Long idReserva);
}
