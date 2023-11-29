package cat.nexia.spring.service;

import cat.nexia.spring.models.User;

import java.util.List;

/**
 * Interface del servei que gestiona la classe User
 */
public interface UserService {

     User getUsuariById(Long id);

     User findByUsername(String username);

     boolean existsByEmail(String email);

     List<Integer> findRolesByUserId(Long id);

}
