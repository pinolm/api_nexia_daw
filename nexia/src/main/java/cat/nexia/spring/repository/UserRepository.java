package cat.nexia.spring.repository;

import cat.nexia.spring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface repository JPA de l'entitat User
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findUserById(Long id);

  @Query(value = "select ur.role_id from nexia.users\n" +
          "left join nexia.user_roles ur on users.id = ur.user_id\n" +
          "where users.id = ?;", nativeQuery = true)
  List<Integer> findUserRolByUserId(Long id);


  @Modifying
  @Transactional(propagation = Propagation.REQUIRED)
  @Query(value = "update nexia.users set image=? where id = ?", nativeQuery = true)
  void updateUserById(String image, Long userId);

}
