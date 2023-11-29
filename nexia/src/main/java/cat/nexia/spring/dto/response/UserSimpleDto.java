package cat.nexia.spring.dto.response;

/**
 * POJO de la clase UserSimpleDto
 */
public class UserSimpleDto {

    private Long id;
    private String username;
    
    public UserSimpleDto() {
    }

    public UserSimpleDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
