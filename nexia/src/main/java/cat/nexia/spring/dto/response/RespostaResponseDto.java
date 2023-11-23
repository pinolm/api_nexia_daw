package cat.nexia.spring.dto.response;

public class RespostaResponseDto {

    private Long id;
    private Long userId;
    private String username;
    private String contenido;

    public RespostaResponseDto(Long id, Long userId, String username, String contenido) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.contenido = contenido;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
