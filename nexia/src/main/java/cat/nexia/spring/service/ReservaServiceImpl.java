package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.*;
import cat.nexia.spring.models.Reserva;
import cat.nexia.spring.models.mapper.ReservaMapper;
import cat.nexia.spring.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public List<Reserva> findReservaByDia(LocalDate dia) {
        if (dia != null) {
            return reservaRepository.findReservaByDia(dia);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<AllReservasResponseDto> findAll() {
        List<Reserva> reservas = reservaRepository.findAllByOrderByDiaAsc();
        List<AllReservasResponseDto> allReservasDtos = new ArrayList<>();

        for (Reserva reserva : reservas) {

            AllReservasResponseDto allReservasDto = new AllReservasResponseDto(
                    reserva.getDia(),
                    reserva.getIdReserva(),
                    new PistaDto(reserva.getPista().getNumPista()),
                    new HorariDto(reserva.getHorari().getIniHora(), reserva.getHorari().getFiHora()),
                    new UserSimpleDto(reserva.getUser().getId(), reserva.getUser().getUsername()));

            allReservasDtos.add(allReservasDto);
        }

        return allReservasDtos;
    }

    @Override
    public ReservaDto findReservaById(Long idReserva) {
        Reserva reserva = reservaRepository.findReservaByIdReserva(idReserva);
        if (reserva != null) {
            return ReservaMapper.toReservaDto(reserva);
        }
        return null;
    }

    @Override
    public void guardarReserva(Reserva reserva) {
        reservaRepository.insertarReserva(reserva.getIdPista(), reserva.getIdHorari(), reserva.getIdUsuari(),
                reserva.getDia());
    }

    @Override
    public Reserva findReservaByIdPistaAndIdHorariAndDia(Reserva reserva) {
        return reservaRepository.findReservaByIdPistaAndIdHorariAndDia(reserva.getIdPista(), reserva.getIdHorari(),
                reserva.getDia());
    }

    @Override
    public void eliminarReservaById(Long idReserva) {
        reservaRepository.deleteReservaByIdReserva(idReserva);
    }


}
