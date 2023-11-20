package cat.nexia.spring.dto.request;

import javax.validation.constraints.NotBlank;

public class CrearRespostaRequestDto {

    private Long missatgeId;

    private Long userId;

    @NotBlank
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
