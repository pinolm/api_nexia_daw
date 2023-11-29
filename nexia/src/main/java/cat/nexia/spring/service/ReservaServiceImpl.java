package cat.nexia.spring.service;

import cat.nexia.spring.dto.response.AllReservesResponseDto;
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

/**
 * Implementació de la interface ReservaService
 */
@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;


    /**
     * Llistar les reserves per dia
     * @param dia data que es busca
     * @return llista amb totes les reserves per dia escollit
     */
    @Override
    public List<AllReservesResponseDto> findReservaByDia(LocalDate dia) {
        if (dia != null) {
            return getAllReservasResponseDtos(reservaRepository.findReservaByDia(dia));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retorna totes les reserves
     * @return retorna les reserves en llista d'instàncies de la classe AllReservesResponseDto
     */

    @Override
    public List<AllReservesResponseDto> findAll() {
        List<Reserva> reservas = reservaRepository.findAllByOrderByDiaAsc();
        return getAllReservasResponseDtos(reservas);
    }

    /**
     * Buscar Reserva pel seu identificador
     * @param idReserva identificador de la reserva
     * @return retorna una llista de reserves
     */

    @Override
    public Reserva findReservaById(Long idReserva) {
        return reservaRepository.findReservaByIdReserva(idReserva);
    }

    /**
     * LLista de AllReservesResponseDto per id d'usuari
     * @param idUser identificador d'usuari
     * @return retorna les reserves en llista d'instàncies de la classe AllReservesResponseDto
     */

    @Override
    public List<AllReservesResponseDto> findReservaByUserId(Long idUser) {
        return getAllReservasResponseDtos(reservaRepository.findReservaByIdUsuari(idUser));
    }

    /**
     * Guardar una reserva a la bbdd
     * @param reserva Reserva a guardar
     */

    @Override
    public void guardarReserva(Reserva reserva) {
        reservaRepository.insertarReserva(reserva.getIdPista(), reserva.getIdHorari(), reserva.getIdUsuari(),
                reserva.getDia());
    }

    /**
     * Buscar una reserva per horari, dia i pista
     * @param reserva instància de Reserva
     * @return la reserva que es busca
     */

    @Override
    public Reserva findReservaByIdPistaAndIdHorariAndDia(Reserva reserva) {
        return reservaRepository.findReservaByIdPistaAndIdHorariAndDia(reserva.getIdPista(), reserva.getIdHorari(),
                reserva.getDia());
    }

    /**
     * Eliminar una reserva pel seu id
     * @param idReserva id de la reserva
     */

    @Override
    public void eliminarReservaById(Long idReserva) {
        reservaRepository.deleteReservaByIdReserva(idReserva);
    }

    /**
     * Número de reserves per dia d'un usuari
     * @param reserva instància de Reserva
     * @return número de reserves
     */

    @Override
    public int countReservaByDiaAndByUser(Reserva reserva) {
        return reservaRepository.countReservaByUserAndDia(reserva.getIdUsuari(), reserva.getDia());
    }

    /**
     * Converteix una llista de Reserva a una llista de instàncies de la classe AllReservesResponseDto
     * @param reservas llista de Reserves
     * @return llista d'instàncies de la classe AllReservesResponseDto
     */

    private List<AllReservesResponseDto> getAllReservasResponseDtos(List<Reserva> reservas) {
        List<AllReservesResponseDto> allReservasDtos = new ArrayList<>();

        for (Reserva reserva : reservas) {

            AllReservesResponseDto allReservasDto = new AllReservesResponseDto(
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
