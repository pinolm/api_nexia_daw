package cat.nexia.spring.service;

import cat.nexia.spring.models.User;

public interface UserService {

     User getUsuariById(Long id);

     User findByUsername(String username);


}
