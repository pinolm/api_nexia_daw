package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.AllReservasResponseDto;
import cat.nexia.spring.dto.response.HorariDto;
import cat.nexia.spring.dto.response.PistaDto;
import cat.nexia.spring.dto.response.UserSimpleDto;
import cat.nexia.spring.models.Reserva;
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
    public List<AllReservasResponseDto> findReservaByDia(LocalDate dia) {
        if (dia != null) {
            return getAllReservasResponseDtos(reservaRepository.findReservaByDia(dia));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<AllReservasResponseDto> findAll() {
        List<Reserva> reservas = reservaRepository.findAllByOrderByDiaAsc();
        return getAllReservasResponseDtos(reservas);
    }


    @Override
    public Reserva findReservaById(Long idReserva) {
        return reservaRepository.findReservaByIdReserva(idReserva);
    }

    @Override
    public List<AllReservasResponseDto> findReservaByUserId(Long idUser) {
        return  getAllReservasResponseDtos(reservaRepository.findReservaByIdUsuari(idUser));
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

    @Override
    public int countReservaByDiaAndByUser(Reserva reserva) {
        return reservaRepository.countReservaByUserAndDia(reserva.getIdUsuari(), reserva.getDia());
    }


    private List<AllReservasResponseDto> getAllReservasResponseDtos(List<Reserva> reservas) {
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


}
