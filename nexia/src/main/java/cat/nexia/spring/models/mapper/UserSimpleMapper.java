package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.UserSimpleDto;
import cat.nexia.spring.models.User;

/**
 * Classe Mapper de User a UserSimpleDto
 */
public class UserSimpleMapper {

    /**
     * Entrada un User i retorna un UserSimpleDto
     * @param user User
     * @return UserSimpleDto
     */
    public static UserSimpleDto toUserSimpleDto(User user){
        return new UserSimpleDto(user.getId(), user.getUsername());
    }
}
