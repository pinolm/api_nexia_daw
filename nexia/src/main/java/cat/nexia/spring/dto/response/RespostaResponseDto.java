package cat.nexia.spring.dto.response;

/**
 * POJO de la clase RespostaResponseDto
 */
public class RespostaResponseDto {

    private Long id;
    private Long userId;
    private String username;
    private String content;

    public RespostaResponseDto(Long id, Long userId, String username, String content) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
