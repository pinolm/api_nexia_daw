package cat.nexia.spring.service;

import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementació del servei UserService
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Buscar a bbdd usuari pel seu id
     * @param id identificador d'usuari
     * @return usuari trobat a la bbdd o null en cas contrari
     */

    @Override
    public User getUsuariById(Long id) {
       if (userRepository.findUserById(id).isPresent()){
           return userRepository.findUserById(id).get();
        } else {
           return null;
       }
    }

    /**
     * Buscar un usuari pel seu username
     * @param username name de l'usuari
     * @return usuari trobat a la bbdd o null en cas contrari
     */

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Cercar existència d'un email a la taula d'usuaris
     * @param email email a buscar
     * @return true si existeix o false en cas contrari
     */

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Buscar roles d'un usuari
     * @param id id d'usuari
     * @return llista amb els rols d'un usuari
     */

    @Override
    public List<Integer> findRolesByUserId(Long id) {
        return userRepository.findUserRolByUserId(id);
    }
}
