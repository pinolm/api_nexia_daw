package cat.nexia.spring.dto.request;

import javax.validation.constraints.NotBlank;

public class CrearRespuestaRequestDto {

    private Long missatgeId;

    private Long userId;

    @NotBlank
    private String contenido;

    public Long getMissatgeId() {
        return missatgeId;
    }

    public void setMissatgeId(Long missatgeId) {
        this.missatgeId = missatgeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
}
