package cat.nexia.spring.service;

import cat.nexia.spring.models.Reserva;
import cat.nexia.spring.models.dto.ReservaDto;
import cat.nexia.spring.models.mapper.ReservaMapper;
import cat.nexia.spring.repository.ReservaRepository;

import java.time.LocalDate;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Override
    public List <Reserva> findReservaByDia(LocalDate dia) {
        if (dia != null) {
            return reservaRepository.findReservaByDia(dia);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @Override
    public ReservaDto findReservaById(Long idReserva) {
        Reserva reserva = reservaRepository.findReservaByIdReserva(idReserva);
        if(reserva!=null){
            return ReservaMapper.toReservaDto(reserva);
        }
        return null;
    }

    @Override
    public void guardarReserva(Reserva reserva) {
        reservaRepository.insertarReserva(reserva.getIdPista(), reserva.getIdHorari(), reserva.getIdUsuari(), reserva.getDia());
    }

    @Override
    public Reserva findReservaByIdPistaAndIdHorariAndDia(Reserva reserva) {
        return reservaRepository.findReservaByIdPistaAndIdHorariAndDia(reserva.getIdPista(), reserva.getIdHorari(),reserva.getDia());
    }

    @Override
    public void eliminarReservaById(Long idReserva) {
         reservaRepository.deleteReservaByIdReserva(idReserva);
    }


}
