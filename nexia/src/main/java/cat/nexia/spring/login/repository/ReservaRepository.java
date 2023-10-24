package cat.nexia.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.nexia.spring.login.models.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
    
}
