package cat.nexia.spring.service;

import cat.nexia.spring.models.User;
import cat.nexia.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User getUsuariById(Long id) {
       if (userRepository.findUserById(id).isPresent()){
           return userRepository.findUserById(id).get();
        } else {
           return null;
       }
    }
}
