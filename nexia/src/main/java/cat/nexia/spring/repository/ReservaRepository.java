package cat.nexia.spring.repository;

import cat.nexia.spring.dto.response.AllReservasResponseDto;
import cat.nexia.spring.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findReservaByDia(LocalDate dia);

    List<Reserva> findAll();

    List<Reserva> findAllByOrderByDiaAsc();

    Reserva findReservaByIdReserva(Long idReserva);

    Reserva findReservaByIdPistaAndIdHorariAndDia(Long idPista, Long idHorari, LocalDate dia);

    List<Reserva> findReservaByIdUsuari(Long idUser);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query(value = "INSERT INTO nexia.reserva (id_pista,id_horari,id_usuari,dia) values (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    void insertarReserva(Long idPista, Long idHorari, Long idUsuari, LocalDate dia);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    void deleteReservaByIdReserva(Long idReserva);

    @Query(value = "select count(*) from nexia.reserva where id_usuari=? and dia=?", nativeQuery = true)
    int countReservaByUserAndDia(Long idUsuari, LocalDate dia);


}
