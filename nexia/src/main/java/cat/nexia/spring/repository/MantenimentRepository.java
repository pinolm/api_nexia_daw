package cat.nexia.spring.repository;

import cat.nexia.spring.models.Manteniment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
/**
 * Interface repository entitat Manteniment
 */
@Repository
public interface MantenimentRepository extends JpaRepository<Manteniment, Long> {


    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query(value = "INSERT INTO nexia.manteniment (dia,id_pista) values (?1, ?2)",
            nativeQuery = true)
    void insertarManteniment(LocalDate dia, Long idPista);

    List<Manteniment> findAllByDia (LocalDate dia);

    List<Manteniment> findAllByDiaAndPista (LocalDate dia, Long idPista);
    @Query(value = "select count(*) from nexia.manteniment where dia = ? and id_pista = ?",
            nativeQuery = true)
    Integer countAllByDiaAndPista(LocalDate dia, Long idPista);


}
