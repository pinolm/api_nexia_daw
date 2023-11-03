package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.UserSimpleDto;
import cat.nexia.spring.models.User;

public class UserSimpleMapper {

    public static UserSimpleDto toUserSimpleDto(User user){
        return new UserSimpleDto(user.getId(), user.getUsername());
    }
}
