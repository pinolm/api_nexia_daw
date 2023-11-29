package cat.nexia.spring.dto.request;

import javax.validation.constraints.NotBlank;

/**
 * POJO del DTO de la clase RespostaRequestDto
 */

public class RespostaRequestDto {
    
    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
