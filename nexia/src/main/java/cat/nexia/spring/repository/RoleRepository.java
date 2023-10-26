package cat.nexia.spring.repository;

import java.util.Optional;

import cat.nexia.spring.models.ERole;
import cat.nexia.spring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
